package com.appssearch;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import com.appssearch.GoogleServicesFramework.AndroidBuildProto;
import com.appssearch.GoogleServicesFramework.AndroidCheckinProto;
import com.appssearch.GoogleServicesFramework.AndroidCheckinRequest;
import com.appssearch.GoogleServicesFramework.AndroidCheckinResponse;
import com.appssearch.GoogleServicesFramework.DeviceConfigurationProto;
import com.appssearch.Googleplay.AndroidAppDeliveryData;
import com.appssearch.Googleplay.BrowseResponse;
import com.appssearch.Googleplay.BulkDetailsRequest;
import com.appssearch.Googleplay.BulkDetailsResponse;
import com.appssearch.Googleplay.BuyResponse;
import com.appssearch.Googleplay.DetailsResponse;
import com.appssearch.Googleplay.HttpCookie;
import com.appssearch.Googleplay.ListResponse;
import com.appssearch.Googleplay.ResponseWrapper;
import com.appssearch.Googleplay.SearchResponse;
import com.appssearch.Googleplay.BulkDetailsRequest.Builder;





/**
 * 
 * XXX : DO NOT call checkin, login and download consecutively. To allow server to catch up, sleep for a while before download! (5 sec will do!)
 * Also it is recommended to call checkin once and use generated android-id for further operations.
 * 
 * @author akdeniz
 * 
 * Copyright (c) 2012, Akdeniz
	All rights reserved.
	
	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions are met: 
	
	1. Redistributions of source code must retain the above copyright notice, this
	list of conditions and the following disclaimer. 
	2. Redistributions in binary form must reproduce the above copyright notice,
	this list of conditions and the following disclaimer in the documentation
	and/or other materials provided with the distribution. 
	
	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
	ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
	WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
	DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
	ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
	(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
	LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
	ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
	(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
	
	The views and conclusions contained in the software and documentation are those
	of the authors and should not be interpreted as representing official policies, 
	either expressed or implied, of the FreeBSD Project.
 */


public class GooglePlayAPI {

	private static final String FDFE_URL = "https://android.clients.google.com/fdfe/";
	private static String SERVICE = "androidmarket";
	private static String URL_LOGIN = "https://android.clients.google.com/auth";
	private static String ACCOUNT_TYPE_HOSTED_OR_GOOGLE = "HOSTED_OR_GOOGLE";

	private String authSubToken;
	private String androidId;
	private String email;
	private String password;

	public GooglePlayAPI(String email, String password, String androidId) {
		this(email, password);
		this.setAndroidId(androidId);
	}

	public GooglePlayAPI(String email, String password) {
		this.email = email;
		this.password = password;
	}

	/**
	 * Performs authentication on "ac2dm" service and match up android id, security token and email by
	 * checking them in.
	 * 
	 */
	public AndroidCheckinResponse checkin() throws Exception {
		
		// this first checkin is for generating android-id
		AndroidCheckinResponse checkinResponse = postCheckin(Utils.generateAndroidCheckinRequest().toByteArray());
		this.setAndroidId(BigInteger.valueOf(checkinResponse.getAndroidId()).toString(16));
		String securityToken = (BigInteger.valueOf(checkinResponse.getSecurityToken()).toString(16));

		System.out.println(securityToken);
		HttpEntity c2dmResponseEntity = executePost(URL_LOGIN, new String[][] { { "Email", this.email },
																				{ "Passwd", this.password },
																				{ "service", "ac2dm" },
																				{ "accountType",
																					ACCOUNT_TYPE_HOSTED_OR_GOOGLE },
																				{ "has_permission", "1" },
																				{ "source", "android" },
																				{ "app", "com.google.android.gsf" },
																				{ "device_country", "us" },
																				{ "device_country", "us" },
																				{ "lang", "en" },
																				{ "sdk_version", "16" }, }, null);

		Map<String, String> c2dmAuth = Utils.parseResponse(new String(Utils.readAll(c2dmResponseEntity.getContent())));

		GoogleServicesFramework.AndroidCheckinRequest.Builder checkInbuilder = GoogleServicesFramework.AndroidCheckinRequest
				.newBuilder(Utils.generateAndroidCheckinRequest());

		AndroidCheckinRequest build = checkInbuilder.setId(new BigInteger(this.getAndroidId(), 16).longValue())
				.setSecurityToken(new BigInteger(securityToken, 16).longValue())
				.addAccountCookie("[" + email + "]").addAccountCookie(c2dmAuth.get("Auth")).build();
		// this is the second checkin to match credentials with android-id
		return postCheckin(build.toByteArray());
	}

	/**
	 * Authenticate with given email and password.
	 * 
	 * If android id is not supplied while initializing, this method will try to
	 * acquire one.
	 */
	
	public String login() throws Exception {

		String authToken = "";
		HttpEntity responseEntity = executePost(URL_LOGIN, new String[][] { { "Email", this.email },
																			{ "Passwd", this.password },
																			{ "service", SERVICE },
																			{ "accountType", ACCOUNT_TYPE_HOSTED_OR_GOOGLE },
																			{ "has_permission", "1" },
																			{ "source", "android" },
																			{ "androidId", this.getAndroidId()}, //this.getAndroidId() },
																			{ "app", "com.android.vending" },
																			{ "device_country", "en" },
																			{ "lang", "en" },
																			{ "sdk_version", "16" }, }, null);

		Map<String, String> response = Utils.parseResponse(new String(Utils.readAll(responseEntity.getContent())));
		if (response.containsKey("Auth")) {
			authToken = response.get("Auth");
			setAuthSubToken(authToken);
		} else {
			throw new GooglePlayException("Authentication failed!");
		}
		return authToken;

	}

	private HttpEntity executePost(String url, String[][] postParams, String[][] headerParams) throws IOException {

		List<NameValuePair> formparams = new ArrayList<NameValuePair>();

		for (String[] param : postParams) {
			if (param[0] != null && param[1] != null) {
				formparams.add(new BasicNameValuePair(param[0], param[1]));
			}
		}

		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");

		return executePost(url, entity, headerParams);
	}

	private HttpEntity executePost(String url, HttpEntity postData, String[][] headerParams) throws IOException {
		HttpPost httppost = new HttpPost(url);

		if (headerParams != null) {
			for (String[] param : headerParams) {
				if (param[0] != null && param[1] != null) {
					httppost.setHeader(param[0], param[1]);
				}
			}
		}

		httppost.setEntity(postData);

		return executeHttpRequest(httppost);
	}

	private HttpEntity executeGet(String url, String[][] postParams, String[][] headerParams) throws IOException {

		if (postParams != null) {
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();

			for (String[] param : postParams) {
				if (param[0] != null && param[1] != null) {
					formparams.add(new BasicNameValuePair(param[0], param[1]));
				}
			}

			url = url + "?" + URLEncodedUtils.format(formparams, "UTF-8");
		}

		HttpGet httpget = new HttpGet(url);

		if (headerParams != null) {
			for (String[] param : headerParams) {
				if (param[0] != null && param[1] != null) {
					httpget.setHeader(param[0], param[1]);
				}
			}
		}

		return executeHttpRequest(httpget);
	}

	private HttpEntity executeHttpRequest(HttpUriRequest request) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();

		/*try {
			client.getConnectionManager().getSchemeRegistry().register(Utils.getMockedScheme());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		// proxy TODO : disable!
		HttpHost proxy = new HttpHost("localhost", 8888);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);*/

		HttpResponse response = client.execute(request);

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new GooglePlayException(new String(Utils.readAll(response.getEntity().getContent())));
		}

		return response.getEntity();
	}

	public String getAuthSubToken() {
		return authSubToken;
	}

	public void setAuthSubToken(String authSubToken) {
		this.authSubToken = authSubToken;
	}

	public String getAndroidId() {
		return androidId;
	}

	public void setAndroidId(String androidId) {
		this.androidId = androidId;
	}

	public SearchResponse search(String query) throws IOException {
		return search(query, null, null);
	}

	public SearchResponse search(String query, Integer offset, Integer numberOfResult) throws IOException {

		ResponseWrapper responseWrapper = executeGETRequest("search",
				new String[][] { { "c", "3" },
								{ "q", query },
								{ "o", (offset == null) ? null : String.valueOf(offset) },
								{ "n", (numberOfResult == null) ? null : String.valueOf(numberOfResult) }, });

		return responseWrapper.getPayload().getSearchResponse();
	}

	public DetailsResponse details(String packageName) throws IOException {
		ResponseWrapper responseWrapper = executeGETRequest("details", new String[][] { { "doc", packageName }, });

		return responseWrapper.getPayload().getDetailsResponse();
	}

	public BulkDetailsResponse bulkDetails(String[] packageNames) throws IOException {

		Builder bulkDetailsRequestBuilder = BulkDetailsRequest.newBuilder();
		bulkDetailsRequestBuilder.addAllDocid(Arrays.asList(packageNames));

		ResponseWrapper responseWrapper = executePOSTRequest("bulkDetails", bulkDetailsRequestBuilder.build()
				.toByteArray(), "application/x-protobuf");

		return responseWrapper.getPayload().getBulkDetailsResponse();
	}

	public BrowseResponse browse() throws IOException {

		return browse(null, null);
	}

	public BrowseResponse browse(String categoryId, String subCategoryId) throws IOException {

		ResponseWrapper responseWrapper = executeGETRequest("browse", new String[][] {	{ "c", "3" },
																						{ "cat", categoryId },
																						{ "ctr", subCategoryId } });

		return responseWrapper.getPayload().getBrowseResponse();
	}

	public ListResponse list(String categoryId) throws IOException {
		return list(categoryId, null, null, null);
	}

	public ListResponse list(String categoryId, String subCategoryId, Integer offset, Integer numberOfResult)
			throws IOException {
		ResponseWrapper responseWrapper = executeGETRequest("list",
				new String[][] { { "c", "3" },
								{ "cat", categoryId },
								{ "ctr", subCategoryId },
								{ "o", (offset == null) ? null : String.valueOf(offset) },
								{ "n", (numberOfResult == null) ? null : String.valueOf(numberOfResult) }, });

		return responseWrapper.getPayload().getListResponse();
	}

	private AndroidCheckinResponse postCheckin(byte[] request) throws IOException {

		HttpEntity httpEntity = executePost("https://android.clients.google.com/checkin", new ByteArrayEntity(request),
				new String[][] { { "User-Agent", "Android-Checkin/2.0 (generic JRO03E); gzip" },
								{ "Host", "android.clients.google.com" },
								{ "Content-Type", "application/x-protobuffer" } });
		return AndroidCheckinResponse.parseFrom(httpEntity.getContent());
	}

	private BuyResponse purchase(String packageName, int versionCode, int offerType) throws IOException {

		ResponseWrapper responseWrapper = executePOSTRequest(
				"purchase",
				new String[][] { { "ot", String.valueOf(offerType) },
								{ "doc", packageName },
								{ "vc", String.valueOf(versionCode) }, });

		return responseWrapper.getPayload().getBuyResponse();
	}

	public InputStream download(String packageName, int versionCode, int offerType) throws IOException {

		BuyResponse buyResponse = purchase(packageName, versionCode, offerType);

		AndroidAppDeliveryData appDeliveryData = buyResponse.getPurchaseStatusResponse().getAppDeliveryData();

		String downloadUrl = appDeliveryData.getDownloadUrl();
		HttpCookie downloadAuthCookie = appDeliveryData.getDownloadAuthCookie(0);

		return executeDownload(downloadUrl, downloadAuthCookie.getName() + "=" + downloadAuthCookie.getValue());

	}

	private ResponseWrapper executeGETRequest(String path, String[][] datapost) throws IOException {

		HttpEntity httpEntity = executeGet(FDFE_URL + path, datapost, getHeaderParameters(null));
		return Googleplay.ResponseWrapper.parseFrom(httpEntity.getContent());

	}

	private ResponseWrapper executePOSTRequest(String path, String[][] datapost) throws IOException {

		HttpEntity httpEntity = executePost(FDFE_URL + path, datapost, getHeaderParameters(null));
		return Googleplay.ResponseWrapper.parseFrom(httpEntity.getContent());

	}

	private ResponseWrapper executePOSTRequest(String path, byte[] datapost, String contentType) throws IOException {

		HttpEntity httpEntity = executePost(FDFE_URL + path, new ByteArrayEntity(datapost),
				getHeaderParameters(contentType));
		return Googleplay.ResponseWrapper.parseFrom(httpEntity.getContent());

	}

	private String[][] getHeaderParameters(String contentType) {

		return new String[][] { { "Accept-Language", "en-EN" },
								{ "Authorization", "GoogleLogin auth=" + getAuthSubToken() },
								{ "X-DFE-Enabled-Experiments", "cl:billing.select_add_instrument_by_default" },
								{	"X-DFE-Unsupported-Experiments",
									"nocache:billing.use_charging_poller,market_emails,buyer_currency,prod_baseline,checkin.set_asset_paid_app_field,shekel_test,content_ratings,buyer_currency_in_app,nocache:encrypted_apk,recent_changes" },
								{ "X-DFE-Device-Id", this.getAndroidId() },
								{ "X-DFE-Client-Id", "am-android-google" },
								{	"User-Agent",
									"Android-Finsky/3.7.13 (api=3,versionCode=8013013,sdk=16,device=crespo,hardware=herring,product=soju)" },
								{ "X-DFE-SmallestScreenWidthDp", "320" },
								{ "X-DFE-Filter-Level", "3" },
								{ "Host", "android.clients.google.com" },
								{	"Content-Type",
									(contentType != null) ? contentType
											: "application/x-www-form-urlencoded; charset=UTF-8" } };
	}

	private InputStream executeDownload(String url, String cookie) throws IOException {

		String[][] headerParams = new String[][] {	{ "Cookie", cookie },
													{	"User-Agent",
														"AndroidDownloadManager/4.1.1 (Linux; U; Android 4.1.1; Nexus S Build/JRO03E)" }, };

		HttpEntity httpEntity = executeGet(url, null, headerParams);
		return httpEntity.getContent();

	}
	public static AndroidCheckinRequest generateAndroidCheckinRequest() {

		return AndroidCheckinRequest
				.newBuilder()
				.setId(0)
				.setCheckin(
						AndroidCheckinProto
								.newBuilder()
								.setBuild(
										AndroidBuildProto.newBuilder()
												.setId("samsung/m0xx/m0:4.0.4/IMM76D/I9300XXALF2:user/release-keys")
												.setProduct("smdk4x12").setCarrier("Google").setRadio("I9300XXALF2")
												.setBootloader("PRIMELA03").setClient("android-google")
												.setTimestamp(new Date().getTime() / 1000).setGoogleServices(16)
												.setDevice("m0").setSdkVersion(16).setModel("GT-I9300")
												.setManufacturer("Samsung").setBuildProduct("m0xx")
												.setOtaInstalled(false)).setLastCheckinMsec(0)
								.setCellOperator("310260").setSimOperator("310260").setRoaming("mobile-notroaming")
								.setUserNumber(0))
				.setLocale("en_US")
				.setTimeZone("Europe/Istanbul")
				.setVersion(3)
				.setDeviceConfiguration(
						DeviceConfigurationProto
								.newBuilder()
								.setTouchScreen(3)
								.setKeyboard(1)
								.setNavigation(1)
								.setScreenLayout(2)
								.setHasHardKeyboard(false)
								.setHasFiveWayNavigation(false)
								.setScreenDensity(320)
								.setGlEsVersion(131072)
								.addAllSystemSharedLibrary(
										Arrays.asList("android.test.runner", "com.android.future.usb.accessory",
												"com.android.location.provider", "com.android.nfc_extras",
												"com.google.android.maps", "com.google.android.media.effects",
												"com.google.widevine.software.drm", "javax.obex"))
								.addAllSystemAvailableFeature(
										Arrays.asList("android.hardware.bluetooth", "android.hardware.camera",
												"android.hardware.camera.autofocus", "android.hardware.camera.flash",
												"android.hardware.camera.front", "android.hardware.faketouch",
												"android.hardware.location", "android.hardware.location.gps",
												"android.hardware.location.network", "android.hardware.microphone",
												"android.hardware.nfc", "android.hardware.screen.landscape",
												"android.hardware.screen.portrait",
												"android.hardware.sensor.accelerometer",
												"android.hardware.sensor.barometer", "android.hardware.sensor.compass",
												"android.hardware.sensor.gyroscope", "android.hardware.sensor.light",
												"android.hardware.sensor.proximity", "android.hardware.telephony",
												"android.hardware.telephony.gsm", "android.hardware.touchscreen",
												"android.hardware.touchscreen.multitouch",
												"android.hardware.touchscreen.multitouch.distinct",
												"android.hardware.touchscreen.multitouch.jazzhand",
												"android.hardware.usb.accessory", "android.hardware.usb.host",
												"android.hardware.wifi", "android.hardware.wifi.direct",
												"android.software.live_wallpaper", "android.software.sip",
												"android.software.sip.voip", "com.cyanogenmod.android",
												"com.cyanogenmod.nfc.enhanced",
												"com.google.android.feature.GOOGLE_BUILD", "com.nxp.mifare",
												"com.tmobile.software.themes"))
								.addAllNativePlatform(Arrays.asList("armeabi-v7a", "armeabi"))
								.setScreenWidth(720)
								.setScreenHeight(1184)
								.addAllSystemSupportedLocale(
										Arrays.asList("af", "af_ZA", "am", "am_ET", "ar", "ar_EG", "bg", "bg_BG", "ca",
												"ca_ES", "cs", "cs_CZ", "da", "da_DK", "de", "de_AT", "de_CH", "de_DE",
												"de_LI", "el", "el_GR", "en", "en_AU", "en_CA", "en_GB", "en_NZ",
												"en_SG", "en_US", "es", "es_ES", "es_US", "fa", "fa_IR", "fi", "fi_FI",
												"fr", "fr_BE", "fr_CA", "fr_CH", "fr_FR", "hi", "hi_IN", "hr", "hr_HR",
												"hu", "hu_HU", "in", "in_ID", "it", "it_CH", "it_IT", "iw", "iw_IL",
												"ja", "ja_JP", "ko", "ko_KR", "lt", "lt_LT", "lv", "lv_LV", "ms",
												"ms_MY", "nb", "nb_NO", "nl", "nl_BE", "nl_NL", "pl", "pl_PL", "pt",
												"pt_BR", "pt_PT", "rm", "rm_CH", "ro", "ro_RO", "ru", "ru_RU", "sk",
												"sk_SK", "sl", "sl_SI", "sr", "sr_RS", "sv", "sv_SE", "sw", "sw_TZ",
												"th", "th_TH", "tl", "tl_PH", "tr", "tr_TR", "ug", "ug_CN", "uk",
												"uk_UA", "vi", "vi_VN", "zh_CN", "zh_TW", "zu", "zu_ZA"))
								.addAllGlExtension(
										Arrays.asList("GL_EXT_debug_marker", "GL_EXT_discard_framebuffer",
												"GL_EXT_multi_draw_arrays", "GL_EXT_shader_texture_lod",
												"GL_EXT_texture_format_BGRA8888",
												"GL_IMG_multisampled_render_to_texture", "GL_IMG_program_binary",
												"GL_IMG_read_format", "GL_IMG_shader_binary",
												"GL_IMG_texture_compression_pvrtc", "GL_IMG_texture_format_BGRA8888",
												"GL_IMG_texture_npot", "GL_IMG_vertex_array_object",
												"GL_OES_EGL_image", "GL_OES_EGL_image_external",
												"GL_OES_blend_equation_separate", "GL_OES_blend_func_separate",
												"GL_OES_blend_subtract", "GL_OES_byte_coordinates",
												"GL_OES_compressed_ETC1_RGB8_texture",
												"GL_OES_compressed_paletted_texture", "GL_OES_depth24",
												"GL_OES_depth_texture", "GL_OES_draw_texture", "GL_OES_egl_sync",
												"GL_OES_element_index_uint", "GL_OES_extended_matrix_palette",
												"GL_OES_fixed_point", "GL_OES_fragment_precision_high",
												"GL_OES_framebuffer_object", "GL_OES_get_program_binary",
												"GL_OES_mapbuffer", "GL_OES_matrix_get", "GL_OES_matrix_palette",
												"GL_OES_packed_depth_stencil", "GL_OES_point_size_array",
												"GL_OES_point_sprite", "GL_OES_query_matrix", "GL_OES_read_format",
												"GL_OES_required_internalformat", "GL_OES_rgb8_rgba8",
												"GL_OES_single_precision", "GL_OES_standard_derivatives",
												"GL_OES_stencil8", "GL_OES_stencil_wrap", "GL_OES_texture_cube_map",
												"GL_OES_texture_env_crossbar", "GL_OES_texture_float",
												"GL_OES_texture_half_float", "GL_OES_texture_mirrored_repeat",
												"GL_OES_vertex_array_object", "GL_OES_vertex_half_float")))
				.setFragment(0).build();
	}
	

}
