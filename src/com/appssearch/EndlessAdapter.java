package com.appssearch;

/*
 * Copyright (C) 2012 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.appsearch.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class EndlessAdapter extends ArrayAdapter<MyAppDetails> {
	
	private List<MyAppDetails> itemList;
	private Context ctx;
	private int layoutId;
	private Set<String> prefSelectedPermissions;
	
	public EndlessAdapter(Context ctx, List<MyAppDetails> itemList, int layoutId, Set<String> selectedPerms) {
		super(ctx, layoutId, itemList);
		this.itemList = itemList;
		this.ctx = ctx;
		this.layoutId = layoutId;
		this.prefSelectedPermissions = selectedPerms;
	}

	@Override
	public int getCount() {		
		return itemList.size() ;
	}

	@Override
	public MyAppDetails getItem(int position) {		
		return itemList.get(position);
	}

	
	@Override
	public long getItemId(int position) {		
		return itemList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View result = convertView;
		
		if (result == null) {
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			result = inflater.inflate(layoutId, parent, false);
		}
		
		// We should use class holder pattern
		TextView tv = (TextView) result.findViewById(R.id.label);
		tv.setText(itemList.get(position).getAppName());

		TextView permissionTxt = (TextView) result.findViewById(R.id.permissionCount);
		ArrayList<String> permsnList = itemList.get(position).getPermList();
		int size = permsnList.size();
		
//		System.out.println(size);
		permissionTxt.setText("#Permissions: " + size);
		
		RatingBar ratingTxt = (RatingBar) result.findViewById(R.id.ratingBar);
		float rating = itemList.get(position).getAvgRating();
		ratingTxt.setRating(rating);
//		ratingTxt.setBackgroundColor(Color.GREEN);


		ImageView iv = (ImageView) result.findViewById(R.id.logo);
//		iv.setImageDrawable(new Drawable(" "));
		
//		String fnm = "android_logo"; //  this is image file name
//		String PACKAGE_NAME = this.getCtx().getPackageName();
//		int imgId = this.getCtx().getResources().getIdentifier(PACKAGE_NAME+":drawable/"+fnm , null, null);
//		iv.setImageBitmap(BitmapFactory.decodeResource(this.getCtx().getResources(),imgId));
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inSampleSize = 2;

		String fileName = itemList.get(position).getImageURI();
		
		try {
			File filePath = this.getCtx().getFileStreamPath(fileName);
			FileInputStream fi = new FileInputStream(filePath);
			Bitmap thumbnail = BitmapFactory.decodeStream(fi);
			
			iv.setImageBitmap(thumbnail);
			} catch (Exception ex) {
				Log.e("image loading for search results failed", ex.getMessage());
			}
		
		ArrayList<String> strippedPermList = itemList.get(position).getStrippedPermList();
		
		ArrayList<String> newPermList = new ArrayList<String>(strippedPermList);
		
		newPermList.retainAll(prefSelectedPermissions);
		
		ImageView filterIV = (ImageView) result.findViewById(R.id.mainfilterBar);
		if(newPermList.size() > 0)
			filterIV.setBackgroundColor(Color.RED);
		else
			filterIV.setBackgroundColor(Color.TRANSPARENT);
		
		return result;

	}
	

	public Context getCtx() {
		return ctx;
	}

	public void setCtx(Context ctx) {
		this.ctx = ctx;
	}
	

}
