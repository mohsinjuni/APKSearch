package com.appssearch;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

import com.appsearch.R;

import android.content.Context;

public class Config {

	private static Config instance;
	private static Properties appDescriptions;
	private static Hashtable permHTable;
	private static Context context;
	
	private Config(Context contxParam)
	{
		context = contxParam;
		loadAppDescription();
	}
	
	
	public static Config getInstance(Context contextParam) 
	{
        if (null == instance) 
        {
            synchronized (Config.class)
            {
            	  if (null == instance) 
	              {
	            	  instance = new Config(contextParam);
	            	  
	              }
            }
        }
        return instance;
    }
	private void loadAppDescription()
	{
		appDescriptions = new Properties();
		try {
			appDescriptions.load(context.getResources().openRawResource(R.raw.perm_descrptions));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private void loadPermissionsInfo()
	{
		Properties permissionsInfo = new Properties();
		try {
			permissionsInfo.load(context.getResources().openRawResource(R.raw.permissions));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	private void buildPermHTable(Properties permissionsInfo)
	{
		permHTable = new Hashtable(permissionsInfo);

		
	}

	
	public Properties getAppDescriptions() {
		return appDescriptions;
	}


	public static void setAppDescriptions(Properties appDescriptions) {
		Config.appDescriptions = appDescriptions;
	}


	public static Hashtable getPermHTable() {
		return permHTable;
	}


	public static void setPermHTable(Hashtable permHTable) {
		Config.permHTable = permHTable;
	}
	

}
