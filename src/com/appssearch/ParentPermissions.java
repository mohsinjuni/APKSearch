package com.appssearch;

public class ParentPermissions {

	private String permissionName;
	private int drawableID;
	
	public ParentPermissions(String perm, int drawableIDParam)
	{
		this.permissionName = perm;
		this.drawableID = drawableIDParam;
	}
	
	
	public String getPermissionName() {
		return permissionName;
	}
	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}
	public int getDrawableID() {
		return drawableID;
	}
	public void setDrawableID(int drawableID) {
		this.drawableID = drawableID;
	}
}
