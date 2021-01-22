package com.newproject.BOs;

import java.util.ArrayList;

public class PermissionsCategory {

	private int ID=0;
	private String name="";
	private String icon="";
	private boolean isFiltered = false;
	private ArrayList<PermissionInfo> permissions;
	
	public PermissionsCategory()
	{

	}
	
    @Override
  public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof PermissionsCategory){
        	PermissionsCategory ptr = (PermissionsCategory) v;
            retVal = ptr.ID == this.ID;
        }

     return retVal;
  }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + this.ID;
        return hash;
    }

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<PermissionInfo> getPermissions() {
		return permissions;
	}

	public void setPermissions(ArrayList<PermissionInfo> permissions) {
		this.permissions = permissions;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public boolean isFiltered() {
		return isFiltered;
	}

	public void setFiltered(boolean isFiltered) {
		this.isFiltered = isFiltered;
	}

	
}
