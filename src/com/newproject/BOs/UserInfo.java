package com.newproject.BOs;

public class UserInfo {

	private int ID=0;
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	private String username="";
	private String password="";

	public UserInfo(String perm, String shortDesc, String longDesc)
	{
		this.setUsername(perm);
		this.setPassword(shortDesc);
		
	}
	
	public UserInfo(){}
	
    @Override
  public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof UserInfo){
        	UserInfo ptr = (UserInfo) v;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    

}
