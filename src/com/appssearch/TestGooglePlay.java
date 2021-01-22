package com.appssearch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Toast;

import com.appssearch.Googleplay.AppDetails;
import com.appssearch.Googleplay.BrowseLink;
import com.appssearch.Googleplay.BrowseResponse;
import com.appssearch.Googleplay.BulkDetailsEntry;
import com.appssearch.Googleplay.BulkDetailsResponse;
import com.appssearch.Googleplay.DetailsResponse;
import com.appssearch.Googleplay.DocV2;
import com.appssearch.Googleplay.Image;
import com.appssearch.Googleplay.ListResponse;
import com.appssearch.Googleplay.Offer;
import com.appssearch.Googleplay.SearchResponse;
import com.newproject.BOs.UserInfo;
import com.newproject.DALs.MyDatabase;



/**
 * 
 * @author akdeniz
 * 
 */
public class TestGooglePlay 

{
	// your device id, which can be optained from Gtalk Service Monitor(aid key)
	// by "*#*#8255#*#*" combination, but you dont need one.
//	
	
	private ArrayList<MyAppDetails> appDetailsList = new ArrayList<MyAppDetails>();
//	public static String ANDROID_ID = ""; 
//	public static String ANDROID_ID = "2001446a3b6f77e8";
	public static String ANDROID_ID = "3b18cac0870e2c1f"; 


	public static String GOOGLE_LOGIN = "appsearchfromgplay@gmail.com";
	public static String GOOGLE_PASSWORD = "appsearch";

//	public static String GOOGLE_LOGIN = "appsearchfromgplay@gmail.com";
//	public static String GOOGLE_PASSWORD = "appsearch";
	
//	private static GooglePlayAPI service = new GooglePlayAPI(GOOGLE_LOGIN, GOOGLE_PASSWORD);
	
	private static GooglePlayAPI service; 
	
	public static String accessToken = "";

	public void performOperations(Context context, String query, String maturityFilter, String searchResultFilter) throws IOException, Exception
	{

	     MyDatabase myDB = new MyDatabase(context);
	     
	     UserInfo ui = myDB.getUserInfo();

//	     GOOGLE_LOGIN = ui.getUsername();
//	     GOOGLE_PASSWORD = ui.getPassword();
	     
	     service = new GooglePlayAPI(GOOGLE_LOGIN, GOOGLE_PASSWORD, ANDROID_ID);
//	     Toast.makeText(this, "username:" + ui.getUsername() , Toast.LENGTH_SHORT).show();

	     
		service.login();
	
//		String token = service.getAuthSubToken();
//	
//		System.out.println("SubAuthToken : " + token);
//	
		
		int searchResultFilterInt = Integer.parseInt(searchResultFilter);
		
		appDetailsList = getBulkDetails(context, query, 1, searchResultFilterInt);

	}

	/*
	 * 
	 *  If you have rooted phone, get the access token directly.
	 *  
	 *  
	 *  http://sbktech.blogspot.com/2014/01/inside-android-play-services-magic.html
	 */
	
	
	
	static ArrayList<MyAppDetails> getBulkDetails(Context context, String query, int offset, int numberOfResults) throws IOException {
		
		ArrayList<MyAppDetails> mAppDetailList = new ArrayList<MyAppDetails>();

		SearchResponse searchResponse = service.search(query, offset, numberOfResults);
		String[] pkgIDs = new String[numberOfResults];
		
		int i=0;
		for (DocV2 child : searchResponse.getDoc(0).getChildList()) {
//			System.out.println(" Printing details of " + child + ", start:");
			pkgIDs[i] = child.getBackendDocid(); 
			i++;
//			DetailsResponse detailsResponse = service.details(child.getBackendDocid());
		}
		BulkDetailsResponse bulkDetails = service.bulkDetails(pkgIDs);

		//mAppDetailList.addAll(bulkDetails.getEntryList());

		for (BulkDetailsEntry bulkDetailsEntry : bulkDetails.getEntryList()) {

			DocV2 child = bulkDetailsEntry.getDoc();
//			System.out.println(bulkDetailsEntry);
			
			MyAppDetails mAppDetails = new MyAppDetails();
			
			String pkgName = child.getBackendDocid().toString();
			mAppDetails.setPkgName(pkgName);
			mAppDetails.setAppName(child.getTitle());
			mAppDetails.setAvgRating(child.getAggregateRating().getStarRating());
			
			String localImagePath = pkgName.concat(".png");
			List<Image> imgList = child.getImageList();
			if(imgList != null && imgList.size()>0)
			{
				String imageWebURL =imgList.get(0).getImageUrl(); 
				File f = new File(imageWebURL);
				if(!f.exists())
				{
					saveImage(context, imageWebURL, localImagePath);
					mAppDetails.setImageURI(localImagePath);
				}
			}
			
			ArrayList<String> permisnList = new ArrayList<String>();
			ArrayList<String> strippedPermisnList = new ArrayList<String>();

			AppDetails appDetails = child.getDetails().getAppDetails();
			for (String permission : appDetails.getPermissionList()) {
				permisnList.add(permission);
				
				String[] permSplit = permission.split("[.]");
				String strippedPermission = permSplit[permSplit.length-1];
				
				strippedPermisnList.add(strippedPermission);
				
			}
//		set ratings here
			mAppDetails.setPermList(permisnList);
			mAppDetails.setStrippedPermList(strippedPermisnList);

			mAppDetails.setPermissionCount(permisnList.size());

//			mAppDetails.setAppBulkDetails(bulkDetailsEntry);
			
//			System.out.println(child);
			
			mAppDetailList.add(mAppDetails);
//			testDownload(child.getBackendDocid());
//			System.out.println("Downloaing " + child.getBackendDocid() + " end:");			

		}
		return mAppDetailList;

	}
	
	
	static ArrayList<MyAppDetails> getAppsDetails (Context context, String query, Integer offset, Integer numberOfResults) throws IOException{

		ArrayList<MyAppDetails> mAppDetailList = new ArrayList<MyAppDetails>();
		
		SearchResponse searchResponse = service.search(query, offset, numberOfResults);
		for (DocV2 child : searchResponse.getDoc(0).getChildList()) {
//			System.out.println(" Printing details of " + child + ", start:");
			MyAppDetails mAppDetails = new MyAppDetails();
			String pkgName = child.getBackendDocid().toString();
			mAppDetails.setPkgName(pkgName);
			mAppDetails.setAppName(child.getTitle());
			
			if(child != null)
			{
				String localImagePath = pkgName.concat(".png");
				List<Image> imgList = child.getImageList();
				if(imgList != null && imgList.size()>0)
				{
					String imageWebURL =imgList.get(0).getImageUrl(); 
					File f = new File(imageWebURL);
					if(!f.exists())
					{
						saveImage(context, imageWebURL, localImagePath);
						mAppDetails.setImageURI(localImagePath);
					}
				}
			}
			AppDetails appDetails = child.getDetails().getAppDetails();
//			mAppDetails.setAppDetails(appDetails);
			
//			System.out.println(child);
			
			mAppDetailList.add(mAppDetails);
//			testDownload(child.getBackendDocid());
//			System.out.println("Downloaing " + child.getBackendDocid() + " end:");			
		}
		
		return mAppDetailList;
	}
	

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		TestGooglePlay.accessToken = accessToken;
	}

	static void testBrowse() throws IOException {
		BrowseResponse browseResponse = service.browse();
		for (BrowseLink browseLink : browseResponse.getCategoryList()) {
			String[] splitedStrs = browseLink.getDataUrl().split("&cat=");
			System.out.println(splitedStrs[splitedStrs.length - 1]);
		}
	}

	static void testBrowseSubCategories() throws IOException {
		BrowseResponse browseResponse = service.browse("GAME", "SPORTS_GAMES");
		for (BrowseLink browseLink : browseResponse.getCategoryList()) {
			String[] splitedStrs = browseLink.getDataUrl().split("&cat=");
			System.out.println(splitedStrs[splitedStrs.length - 1]);
		}
	}

	static void testList() throws IOException {
		ListResponse listResponse = service.list("GAME", null, null, null);
		for (DocV2 child : listResponse.getDocList()) {
			System.out.println(child.getDocid());
		}
	}

	static void testListSubCategories() throws IOException {
		ListResponse listResponse = service.list("GAME", "apps_topselling_free", 6, 36);
		for (DocV2 child : listResponse.getDoc(0).getChildList()) {
			System.out.println(child.getBackendDocid());
		}
	}

	static void testSearch() throws IOException {
		SearchResponse searchResponse = service.search("criticker");
		for (DocV2 child : searchResponse.getDoc(0).getChildList()) {
			System.out.println(child.getBackendDocid());
		}
	}

	static void testDetails() throws IOException {
		DetailsResponse detailsResponse = service.details("com.mobulasoft.criticker");
		System.out.println(detailsResponse);
	}

	private static void testPermissions() throws IOException {
		DetailsResponse details = service.details("com.mobulasoft.criticker");
		AppDetails appDetails = details.getDocV2().getDetails().getAppDetails();

		for (String permission : appDetails.getPermissionList()) {
			System.out.println(permission);
		}
	}

	private static void testBulkDetails() throws IOException {
		BulkDetailsResponse bulkDetails = service.bulkDetails(new String[] { "com.mobulasoft.criticker",
																			"com.cricbuzz.android",
																			"com.sticksports.stickcricket",
																			"com.indiagames.cricketfever", });

		for (BulkDetailsEntry bulkDetailsEntry : bulkDetails.getEntryList()) {
			DocV2 doc = bulkDetailsEntry.getDoc();
			AppDetails appDetails = doc.getDetails().getAppDetails();
			System.out.println(doc.getDocid());
			for (String permission : appDetails.getPermissionList()) {
				System.out.println("\t" + permission);
			}
		}

	}
	

	public ArrayList<AppDetails> getAppsDetailsWithContentFiltering (String query, Integer offset, Integer numberOfResults) throws IOException{

		ArrayList<AppDetails> appdetailList = new ArrayList<AppDetails>();
		System.out.println(" Printing DETAILLLLLLLSSSSSSSSSss");

		SearchResponse searchResponse = service.search(query, offset, numberOfResults);
		for (DocV2 child : searchResponse.getDoc(0).getChildList()) {
//			System.out.println(" Printing details of " + child.getBackendDocid() + ", start:");
			DetailsResponse details = service.details(child.getBackendDocid());
			AppDetails appDetails = details.getDocV2().getDetails().getAppDetails();
			
			appdetailList.add(appDetails);
			
			//DetailResponse contains content-rating filter.
			
//			System.out.println(appDetails);
//			testDownload(child.getBackendDocid());
//			System.out.println("Downloaing " + child.getBackendDocid() + " end:");			
		}

		return appdetailList;
	}
	static void testDownload() throws IOException {
		DetailsResponse details = service.details("com.indiagames.cricketfever");
		AppDetails appDetails = details.getDocV2().getDetails().getAppDetails();
		Offer offer = details.getDocV2().getOffer(0);

		int versionCode = appDetails.getVersionCode();
		long installationSize = appDetails.getInstallationSize();
		int offerType = offer.getOfferType();
		boolean checkoutRequired = offer.getCheckoutFlowRequired();

		// paid application...ignore
		if (checkoutRequired) {
			System.out.println("Checkout required! Ignoring.." + appDetails.getPackageName());
			return;
		}

		System.out.println("Downloading..." + appDetails.getPackageName() + " : " + installationSize + " bytes");
		InputStream downloadStream = service.download(appDetails.getPackageName(), versionCode, offerType);

		FileOutputStream outputStream = new FileOutputStream(appDetails.getPackageName() + ".apk");

		byte buffer[] = new byte[1024];
		for (int k = 0; (k = downloadStream.read(buffer)) != -1;) {
			outputStream.write(buffer, 0, k);
		}
		downloadStream.close();
		outputStream.close();
		System.out.println("Downloaded! " + appDetails.getPackageName() + ".apk");
	}
	
	static void downloadAppsList (String query, Integer offset, Integer numberOfResults) throws IOException{

		SearchResponse searchResponse = service.search(query, offset, numberOfResults);
		for (DocV2 child : searchResponse.getDoc(0).getChildList()) {
			System.out.println("Downloaing " + child.getBackendDocid() + " start:");
			testDownload(child.getBackendDocid());
			System.out.println("Downloaing " + child.getBackendDocid() + " end:");			
		}

		
	}
	
	static void testDownload(String packageName) throws IOException {
		DetailsResponse details = service.details(packageName);
		AppDetails appDetails = details.getDocV2().getDetails().getAppDetails();
		Offer offer = details.getDocV2().getOffer(0);

		int versionCode = appDetails.getVersionCode();
		long installationSize = appDetails.getInstallationSize();
		int offerType = offer.getOfferType();
		boolean checkoutRequired = offer.getCheckoutFlowRequired();

		// paid application...ignore
		if (checkoutRequired) {
			System.out.println("Checkout required! Ignoring.." + appDetails.getPackageName());
			return;
		}

		 try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Downloading..." + appDetails.getPackageName() + " : " + installationSize + " bytes");
		
		 try {
			Thread.currentThread().sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		InputStream downloadStream = service.download(appDetails.getPackageName(), versionCode, offerType);

		FileOutputStream outputStream = new FileOutputStream(appDetails.getPackageName() + ".apk");

		byte buffer[] = new byte[1024];
		for (int k = 0; (k = downloadStream.read(buffer)) != -1;) {
			outputStream.write(buffer, 0, k);
		}
		downloadStream.close();
		outputStream.close();
		System.out.println("Downloaded! " + appDetails.getPackageName() + ".apk");
	}

	public ArrayList<MyAppDetails> getAppDetailsList() {
		return appDetailsList;
	}

	public void setAppDetailsList(ArrayList<MyAppDetails> appDetailsList) {
		this.appDetailsList = appDetailsList;
	}
	
	public static void saveImage(Context context, String webImageUrl, String localImageName) throws IOException {
		URL url = new URL(webImageUrl);
		File dir = context.getDir("images", Context.MODE_PRIVATE);
		
		String fileName = url.getFile();
		FileOutputStream os;

		try {
			InputStream is = url.openStream();
		    os = context.openFileOutput(localImageName, Context.MODE_PRIVATE);
		  
			byte[] b = new byte[2048];
			int length;
			while ((length = is.read(b)) != -1) {
				os.write(b, 0, length);
			}
			is.close();
		    os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static GooglePlayAPI getService() {
		return service;
	}

	public static void setService(GooglePlayAPI service) {
		TestGooglePlay.service = service;
	}
	
}
