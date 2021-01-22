package com.newproject.BOs;

public class PermissionInfo {

	private int ID=0;
	private String name="";
	private String shortDesc="";
	private String longDesc="";
	private boolean isFiltered = false;
	
	public PermissionInfo(String perm, String shortDesc, String longDesc)
	{
		this.name = perm;
		this.shortDesc = shortDesc;
		this.longDesc = longDesc;
	}
	
	public PermissionInfo(){}
	
    @Override
  public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof PermissionInfo){
        	PermissionInfo ptr = (PermissionInfo) v;
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
    
	public String getName() {
		return name;
	}
	public void setName(String permissionName) {
		this.name = permissionName;
	}


	public String getLongDesc() {
		return longDesc;
	}


	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}


	public String getShortDesc() {
		return shortDesc;
	}


	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}


	public int getID() {
		return ID;
	}


	public void setID(int iD) {
		ID = iD;
	}

	public boolean isFiltered() {
		return isFiltered;
	}

	public void setFiltered(boolean isFiltered) {
		this.isFiltered = isFiltered;
	}
}
