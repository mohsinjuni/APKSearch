package com.newproject.BOs;

import java.util.ArrayList;

public class PermissionsProtection {

	private String name="";
	private int ID=0;
	private String icon="";
	private boolean isFiltered = false;
	private ArrayList<PermissionsCategory> categList;
	
	public PermissionsProtection()
	{
		setCategList(new ArrayList<PermissionsCategory>());
	}

    @Override
  public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof PermissionsProtection){
        	PermissionsProtection ptr = (PermissionsProtection) v;
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

	public void setName(String name) {
		this.name = name;
	}


	public ArrayList<PermissionsCategory> getCategList() {
		return categList;
	}

	public void setCategList(ArrayList<PermissionsCategory> categList) {
		this.categList = categList;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
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
