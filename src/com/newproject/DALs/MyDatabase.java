package com.newproject.DALs;

import java.util.ArrayList;
import java.util.Hashtable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.newproject.BOs.PermissionInfo;
import com.newproject.BOs.PermissionList;
import com.newproject.BOs.PermissionsCategory;
import com.newproject.BOs.PermissionsProtection;
import com.newproject.BOs.UserInfo;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class MyDatabase extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "gplaywrapper-5.db";
	private static final int DATABASE_VERSION = 1;
	
	public MyDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
				
		// you can use an alternate constructor to specify a database location 
		// (such as a folder on the sd card)
		// you must ensure that this folder is available and you have permission
		// to write to it
		//super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
		
	}
//TODO implement onUpdrade options....
	
// Use firefox SQLite plugin.	
	public PermissionList getPermission(ArrayList<String> permissionNames) {

		PermissionList permList = new PermissionList();
		
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String query = "Select p.PermissionID, p.Name, p.Description, c.CategoryID, c.IconName, c.Name, pr.ProtectionLevelID, pr.Name, pr.IconName " 
						+ "from Permissions p " 
						+ " INNER JOIN Categories c ON p.CategoryID = c.CategoryID"
						+ " INNER JOIN ProtectionLevels pr ON p.ProtectionLevelID = pr.ProtectionLevelID" 
						+ " WHERE ";
		
		for(int i=0; i < permissionNames.size(); i++)
		{
			String pName = permissionNames.get(i);
			
			pName = pName.trim();
			
			if(i!= 0)
				query = query.concat(" OR ");
			query = query.concat("p.Name like '" + pName + "'");
		}
		
		query = query.concat(" ORDER BY pr.ProtectionLevelID, c.CategoryID ASC");
		
//		'SET_WALLPAPER' OR p.NAME like 'READ_SMS'" 
//						+ " OR p.NAME like 'CAMERA' OR p.NAME like 'TRANSMIT_IR' OR p.NAME like 'NFC'"
//						+  " ORDER BY pr.ProtectionLevelID, c.CategoryID ASC";

		
		Cursor cursor = db.rawQuery(query, null);

		ArrayList<PermissionsProtection> ppList = new ArrayList<PermissionsProtection>();

		cursor.moveToFirst();
		
		permList = cursorToPermissionParent(cursor);

		
		cursor.close();
		
		return permList;
	}
	
	public boolean updateUserInfo(UserInfo ui) {

		SQLiteDatabase db = getWritableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

//		String query = "Update UserInfo Set Username='" + ui.getUsername() + "', Password='" + ui.getPassword() + "' " +
//				"Where ID=1;";

		ContentValues cv=new ContentValues();
	    cv.put("Username", ui.getUsername());
	    cv.put("Password", ui.getPassword());

	    String where = "ID=?";
	    String[] whereArgs = {Integer.toString(1)};

	    db.update("UserInfo", cv, where , whereArgs);  
	    
//		Cursor cursor = db.rawQuery(query, null);
		
		
		return true;

	}
	public UserInfo getUserInfo() {

		UserInfo ui = new UserInfo();
		
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String query = "Select * from UserInfo;";

		Cursor cursor = db.rawQuery(query, null);

		cursor.moveToFirst();
		
		ui = cursorToUserInfo(cursor);
		
		cursor.close();
		
		return ui;
	}
	
	private PermissionList cursorToPermissionParent(Cursor cursor) 
	{
	
/*		Select p.PermissionID = 0, p.Name = 1, p.Description = 2, c.CategoryID = 3, c.IconName= 4 c.Name = 5, 
		pr.ProtectionLevelID = 6, pr.Name = 7, pr.IconName = 8 from Permissions p INNER JOIN Categories c ON p.CategoryID = c.CategoryID 
*/				
//TODO Probably I should use tree datastructure here.
		
		PermissionList pList = new PermissionList();
		
		Hashtable<Integer, PermissionsProtection> ht = new Hashtable<Integer, PermissionsProtection>();

		ArrayList<PermissionsProtection> ppList = new ArrayList<PermissionsProtection>();
		
		
		while (!cursor.isAfterLast()) {
			
			int permID = cursor.getInt(0);
			String permName = cursor.getString(1);
			String permDescr = cursor.getString(2);
			int categoryID = cursor.getInt(3);
			String categoryIcon = cursor.getString(4);
			String categoryName = cursor.getString(5);

			int protectionID = cursor.getInt(6);
			String protectionlevelName = cursor.getString(7);
			String protecIcon = cursor.getString(8);

			PermissionInfo pi = new PermissionInfo();
			pi.setID(permID);
			pi.setName(permName);
			pi.setLongDesc(permDescr);

			PermissionsCategory pc = new PermissionsCategory();
			pc.setID(categoryID);
			pc.setName(categoryName);
			pc.setIcon(categoryIcon);

			PermissionsProtection pp = new PermissionsProtection();
			pp.setID(protectionID);
			pp.setName(protectionlevelName);
			pp.setIcon(protecIcon);

			
			Integer prInteger = Integer.valueOf(protectionID);
			
			if(ht.containsKey(prInteger))
			{
				PermissionsProtection ppTemp = ht.get(prInteger);
				ArrayList<PermissionsCategory> pcList = ppTemp.getCategList();
				
				
				if(pcList.contains(pc)) //It just checks based on ID
				{
					int index = pcList.indexOf(pc);
					PermissionsCategory pcc = pcList.get(index);
					
					ArrayList<PermissionInfo> piList = pcc.getPermissions();
					piList.add(pi);
					pcc.setPermissions(piList);
				}
				else
				{
					ArrayList<PermissionInfo> piList = new ArrayList<PermissionInfo>();
					piList.add(pi);
					pc.setPermissions(piList);
					pcList.add(pc);

					int index = ppList.indexOf(pp); //based on ID, we get the previous item from the list.
					PermissionsProtection oldPP = ppList.get(index);
					oldPP.setCategList(pcList);
				}
				
				
				
			}
			else
			{
				ArrayList<PermissionInfo> piList = new ArrayList<PermissionInfo>();
				piList.add(pi);
				
				pc.setPermissions(piList);

				ArrayList<PermissionsCategory> pcList = new ArrayList<PermissionsCategory>();
				pcList.add(pc);

				
				pp.setCategList(pcList);

				ppList.add(pp);
				ht.put(Integer.valueOf(pp.getID()), pp);
				
				
				
			}
			
			cursor.moveToNext();
    	}

		pList.setQueryResultPermissions(ppList);
	return pList;

	}
	
	private UserInfo cursorToUserInfo(Cursor cursor) 
	{
	
		UserInfo userinfo = null;
		
		while (!cursor.isAfterLast()) {
			
			userinfo = new UserInfo();
			int userID = cursor.getInt(0);
			String userName = cursor.getString(1);
			String password = cursor.getString(2);
			
			userinfo.setID(userID);
			userinfo.setUsername(userName);
			userinfo.setPassword(password);

			cursor.moveToNext();
    	}

	return userinfo;

	}
	
}
