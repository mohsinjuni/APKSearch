package com.appssearch;

import java.util.ArrayList;

import com.newproject.BOs.PermissionInfo;
import com.newproject.BOs.PermissionList;
import com.newproject.BOs.PermissionsCategory;
import com.newproject.BOs.PermissionsProtection;
import com.newproject.DALs.MyDatabase;

import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;


public class ExpandableListMainActivity extends ExpandableListActivity 
{
    // Create ArrayList to hold parent Items and Child Items
    private ArrayList<Object> parentItems = new ArrayList<Object>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    private ArrayList<Object> categoryItems = new ArrayList<Object>();
    private ArrayList<String> permissions;
    private String appName="";
    private boolean isZeroIndexExpanded = true;

    @Override
    public void onCreate(Bundle savedInstanceState) 
    {

        super.onCreate(savedInstanceState);

        
        Intent recvdIntent = getIntent();
        permissions = recvdIntent.getStringArrayListExtra("permissions");
        appName = recvdIntent.getStringExtra("appName");
        
        this.setTitle(appName);
        ArrayList<String> commonStrippedPermissions = recvdIntent.getStringArrayListExtra("commonStrippedPermissions");
        
        ArrayList<String> commPermQualifiedNames = generateQualifiedPermNames(commonStrippedPermissions);

        // Create Expandable List and set it's properties
        ExpandableListView expndListView = getExpandableListView(); 
        expndListView.setDividerHeight(2);
        expndListView.setGroupIndicator(null);
//        expndListView.setClickable(true);
        
//        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
//        	expndListView.setIndicatorBounds(myLeft, myRight);
//        	} else {
//        		expndListView.setIndicatorBoundsRelative(myLeft, myRight);
//        	}
        
  
        MyDatabase myDB = new MyDatabase(this);
        PermissionList permList = myDB.getPermission(permissions);
        
        //Get the fully qualified names for permissions that are common between app permissions and Settings
        PermissionList commonPermList = null;
        if(commPermQualifiedNames.size() > 0)
        	commonPermList = myDB.getPermission(commPermQualifiedNames);
        // Set the Items of Parent
//        setGroupParents();
//        // Set The Child Data
//        setChildData();


        markFilteredItems(permList, commonPermList);
        ExpandableListAdapter adapter = new ExpandableListAdapter(getBaseContext(), permList, ExpandableListMainActivity.this);

        adapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
        
        // Set the Adapter to expandableList
        expndListView.setAdapter(adapter);
 //       expndListView.setOnChildClickListener(this);
//        expndListView
        
        expndListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    Log.v("onGrouppppppppppClick:", "worked");

                    	
                    parent.smoothScrollToPosition(groupPosition);

                    // Need default behaviour here otherwise group does not get expanded/collapsed
                    // on click
                    if (parent.isGroupExpanded(groupPosition)) {
                        parent.collapseGroup(groupPosition);
                    } else {
                        parent.expandGroup(groupPosition);
                    }

                    return true;
                    
//                    boolean isExpanded = parent.isGroupExpanded(groupPosition);
//                    
//                    if(isExpanded)
//                    	  parent.collapseGroup(groupPosition);
//                    else
//                    	parent.expandGroup(groupPosition);
                    
//                    if(groupPosition ==0)
//                    {
//                    	return true;
//                    }	
//                    else 
 //                   	return false;
                }
            });
        
    }
    
    public void markFilteredItems(PermissionList appPermList, PermissionList prefPermList)
    {
    	if(prefPermList != null)
    	{
	    	ArrayList<PermissionsProtection> ppList = appPermList.getQueryResultPermissions();
	    	ArrayList<PermissionsProtection> prefPPList = prefPermList.getQueryResultPermissions();
	    	
	    	for(PermissionsProtection prefPP: prefPPList)
	    	{
	    		if(ppList.contains(prefPP))
	    		{
	    			int ppIndex = ppList.indexOf(prefPP);
	    			PermissionsProtection pp = ppList.get(ppIndex);
	    			pp.setFiltered(true);
	    			Log.v("EXXXXXXXXXXXPANDABLEmainActiivty:", "PP: " + pp.getName());
	    			
	    			ArrayList<PermissionsCategory> ppCategoryList = pp.getCategList();
	    			ArrayList<PermissionsCategory> prefCategoryList = prefPP.getCategList();
	    			
	    			for(PermissionsCategory prefPC: prefCategoryList)
	    			{
	    				if(ppCategoryList.contains(prefPC))
	    				{
	    					int pcIndex = ppCategoryList.indexOf(prefPC);
	    					PermissionsCategory pc = ppCategoryList.get(pcIndex); 
	    					pc.setFiltered(true);
	    	    			Log.v("EXXXXXXXXXXXPANDABLEmainActiivty:", "PC: " + pc.getName());
	
	    					ArrayList<PermissionInfo> piList = pc.getPermissions();
	    					ArrayList<PermissionInfo> prefPIList = prefPC.getPermissions();
	    					
	    					for(PermissionInfo prefPI : prefPIList)
	    					{
	    						if(piList.contains(prefPI))
	    						{
	    							int piIndex = piList.indexOf(prefPI);
	    							PermissionInfo pi = piList.get(piIndex);
	    							pi.setFiltered(true);
	    	    	    			Log.v("EXXXXXXXXXXXPANDABLEmainActiivty:", "PI: " + pi.getName());
	    							
	    						}
	    						
	    					}
	    				}
	    			}
	    		}
	    	}
    	}
    	
    }

    public ArrayList<String> generateQualifiedPermNames(ArrayList<String> strippedPermissions)
    {
    	ArrayList<String> qualifiedPermissions= new ArrayList<String>();
    	
    	for(String perm: strippedPermissions)
    	{
    		String qualifiedPermission = "android.permission." + perm;
    		qualifiedPermissions.add(qualifiedPermission);
    	}
    	
    	return qualifiedPermissions;
    }

}
