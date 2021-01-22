package com.appssearch;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.appssearch.Googleplay.BulkDetailsEntry;

public class MyAppDetails implements Parcelable {

	private String imageURI="";
	private String maturityLevel="";
	private String pkgName="";
	private String appName="";
	private String developerName="";
	private float avgRating= 0;
	private ArrayList<String> permList = new ArrayList<String>();
	private ArrayList<String> strippedPermList = new ArrayList<String>();
	
	private int permissionCount=0;
//	private DetailResponse detailResponse;
	
	public MyAppDetails()
	{
		permList = new ArrayList<String>();
	}
	
	public String getImageURI() {
		return imageURI;
	}
	public void setImageURI(String imageURI) {
		this.imageURI = imageURI;
	}
	public String getMaturityLevel() {
		return maturityLevel;
	}
	public void setMaturityLevel(String matLevel) {
		this.maturityLevel = matLevel;
	}
	public String getPkgName() {
		return pkgName;
	}
	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getDeveloperName() {
		return developerName;
	}
	public void setDeveloperName(String developerName) {
		this.developerName = developerName;
	}
	public ArrayList<String> getPermList() {
		return permList;
	}
	public void setPermList(ArrayList<String> permList) {
		this.permList = permList;
	}

	public float getAvgRating() {
		return avgRating;
	}

	public void setAvgRating(float avgRating) {
		this.avgRating = avgRating;
	}

	public int getPermissionCount() {
		return permissionCount;
	}

	public void setPermissionCount(int permissionCount) {
		this.permissionCount = permissionCount;
	}

	public ArrayList<String> getStrippedPermList() {
		return strippedPermList;
	}

	public void setStrippedPermList(ArrayList<String> strippedPermList) {
		this.strippedPermList = strippedPermList;
	}


	   public MyAppDetails (Parcel in)
	   {
	       imageURI = in.readString();
	       maturityLevel = in.readString();
	       pkgName = in.readString();
	       appName = in.readString();

	       developerName = in.readString();
	       avgRating = in.readFloat();
	       in.readStringList(permList);
	       in.readStringList(strippedPermList);
	       
	       permissionCount = in.readInt();
	       
	   }
	    

	    public static final Parcelable.Creator<MyAppDetails> CREATOR
	    = new Parcelable.Creator<MyAppDetails>() 
	   {
	         public MyAppDetails createFromParcel(Parcel in) 
	         {
	           // Log.d (TAG, "createFromParcel()");
	             return new MyAppDetails(in);
	         }

	         public MyAppDetails[] newArray (int size) 
	         {
	           // Log.d (TAG, "createFromParcel() newArray ");
	             return new MyAppDetails[size];
	         }
	    };

	    public int describeContents ()
	   {
	        //Log.d (TAG, "describe()");
	       return 0;
	   }
		@Override
	   public void writeToParcel (Parcel dest, int flags)
	   {
	       dest.writeString(imageURI);
	       dest.writeString (maturityLevel);
	       dest.writeString (pkgName);
	       dest.writeString (appName);

	       dest.writeString (developerName);
	       dest.writeFloat(avgRating);
	       dest.writeStringList(permList);
	       dest.writeStringList(strippedPermList);
	       
	       dest.writeInt(permissionCount);

	   }
}
