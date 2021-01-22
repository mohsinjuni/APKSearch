package com.newproject.BOs;

import java.util.ArrayList;

public class PermissionList {


	private ArrayList<PermissionsProtection> queryResultPermissions;
	
	
	public PermissionList()
	{
		setQueryResultPermissions(new ArrayList<PermissionsProtection>());
	}

	public ArrayList<PermissionsProtection> getQueryResultPermissions() {
		return queryResultPermissions;
	}


	public void setQueryResultPermissions(ArrayList<PermissionsProtection> queryResultPermissions) {
		this.queryResultPermissions = queryResultPermissions;
	}

	
	
}
