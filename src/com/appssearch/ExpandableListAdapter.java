package com.appssearch;

import java.util.ArrayList;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsearch.R;
import com.newproject.BOs.PermissionInfo;
import com.newproject.BOs.PermissionList;
import com.newproject.BOs.PermissionsCategory;
import com.newproject.BOs.PermissionsProtection;


public class ExpandableListAdapter extends BaseExpandableListAdapter 
{

    private Activity activity;
    private ArrayList<PermissionsCategory> childtems;
    private LayoutInflater inflater;
    private ArrayList<String> child;
    private ArrayList<PermissionsProtection> parentItems;
    private Properties permDesc;
    private Context context;
    private ExpandableListView eLV=null;
    private Activity callerActivity;
    private int lastExpandedGroupPosition;
    private boolean oneTimeCondition = true;
    // constructor
//    public ExpandableListAdapter(Context contextParam, ArrayList<Object> parents, ArrayList<Object> childern, Activity mainActivity)
//    {
//        this.parentItems = parents;
//        this.childtems = childern;
//        this.context = contextParam;
//        this.callerActivity = mainActivity;
//
//    }

    public ExpandableListAdapter(Context contextParam, PermissionList permList,  Activity expandableMainActivity)
    {
        this.parentItems =  permList.getQueryResultPermissions();
        this.context = contextParam;
        this.callerActivity = expandableMainActivity;
        this.lastExpandedGroupPosition = 0;

    }
    public void setInflater(LayoutInflater inflater, Activity activity) 
    {
        this.inflater = inflater;
        this.activity = activity;
        
//        Config config = Config.getInstance(context); //Even though context is null, instance would be already NOT-NULL due to initializations in mainactivity.
//        permDesc = config.getAppDescriptions();

    }
    
    // method getChildView is called automatically for each child view.
    //  Implement this method as per your requirement
    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
    {

    	   CustExpListview categoryLevelExpandLV = new CustExpListview(this.context);
    	   
    	   PermissionsProtection pp = this.parentItems.get(groupPosition);
    	   this.childtems = pp.getCategList();
    	   int ppID = pp.getID();
    	   
   	   
    	   SecondLevelAdapter secondAdapter = new SecondLevelAdapter(this.context, this.childtems);
    	   
    	   secondAdapter.setInflater((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE), this.callerActivity);
    	   
    	   categoryLevelExpandLV.setAdapter(secondAdapter);
    	   
    	   categoryLevelExpandLV.setOnGroupClickListener(new OnGroupClickListener() {
               @Override
               public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                       Log.v("onCategoryGroupClick  :", "worked");
                       
//                       if(groupPosition ==0)
//                       {
//                       	return true;
//                       }	
//                       else 
                       	return false;
                   }
               });


    	   
//    	   categoryLevelExpandLV.setGroupIndicator(context.getResources().getDrawable(R.drawable.ic_menu_expander_minimized_light)); 
    	   categoryLevelExpandLV.setGroupIndicator(null);
    	   return categoryLevelExpandLV;
    }

    // method getGroupView is called automatically for each parent item
    // Implement this method as per your requirement
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
    {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.parent_view, parent, false);
        }


        PermissionsProtection ppr = parentItems.get(groupPosition);

      TextView cTV = (TextView) convertView.findViewById(R.id.textViewGroupName);
      cTV.setText(ppr.getName());
      
      ImageView protectLogo = (ImageView) convertView.findViewById(R.id.protectLogo); 
      
      String  iconName = ppr.getIcon();
      
      int pprID = ppr.getID();

      switch (pprID) {
      case 1:      // case "ic_gplay_dangerous.png":
    	  if(isExpanded)
    	  {
    	   	  protectLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gplay_dangerous_downward));	    		  
    	  }
    	  else
    	  {
    	   	  protectLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gplay_dangerous_right));	    		  
    	  }
    	  protectLogo.setBackgroundColor(Color.TRANSPARENT);
          break;
      case 2:
       	  if(isExpanded)
    	  {
    	   	  protectLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gplay_normal_downward));	    		  
    	  }
    	  else
    	  {
    	   	  protectLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gplay_normal_right));	    		  
    	  }    	  
    	  protectLogo.setBackgroundColor(Color.TRANSPARENT);
          break;
      case 3:
      	  if(isExpanded)
    	  {
    	   	  protectLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gplay_signature_downward));	    		  
    	  }
    	  else
    	  {
    	   	  protectLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gplay_signature_right));	    		  
    	  }    	 
//    	  protectLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gplay_signature_right));	
    	  protectLogo.setBackgroundColor(Color.TRANSPARENT);
          break;
      case 4:
      	  if(isExpanded)
    	  {
    	   	  protectLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gplay_system_downward));	    		  
    	  }
    	  else
    	  {
    	   	  protectLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gplay_system_right));	    		  
    	  }    	 
 //   	  protectLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gplay_system_right));	
    	  protectLogo.setBackgroundColor(Color.TRANSPARENT);
          break;
          
      }
//      int pprID = ppr.getID();
//      
//      for(PermissionsProtection commPPR: commonPermParentItems)
//      {
//    	  if(pprID == commPPR.getID())
//    	  {
//    		  convertView.setBackgroundColor(Color.RED);
//    	  }
//      }
      

      ImageView filterIV = (ImageView) convertView.findViewById(R.id.protectfilterBar); 
		  
   	  if(ppr.isFiltered())
  	  {
   		 filterIV.setBackgroundColor(Color.RED);
//  		  convertView.setBackgroundColor(Color.RED);
  	  }
   	  else
   	  {
   		  filterIV.setBackgroundColor(Color.TRANSPARENT);
   	  }

      eLV = (ExpandableListView) parent;
      if(oneTimeCondition)
    	  eLV.expandGroup(0);
      
      oneTimeCondition = false;
        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) 
    {
        return null; // this.parentItems.get(groupPosition).getCategList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) 
    {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) 
    {
//    	Log.v("EXXXXXXXXXXXx", "getChildrenCount +" + groupPosition);
        return 1; //parentItems.get(groupPosition).getCategList().size();
    }

    @Override
    public Object getGroup(int groupPosition) 
    {
        return null; //parentItems.get(groupPosition);
    }

    @Override
    public int getGroupCount() 
    {
        return parentItems.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) 
    {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition)
    {
    	if(groupPosition != lastExpandedGroupPosition){
    		eLV.collapseGroup(lastExpandedGroupPosition);
        }

        super.onGroupExpanded(groupPosition);           
        lastExpandedGroupPosition = groupPosition;
        
    }

    @Override
    public long getGroupId(int groupPosition) 
    {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() 
    {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}

	class CustExpListview extends ExpandableListView
	{
	
		int intGroupPosition, intChildPosition, intGroupid;
		
		public CustExpListview(Context context) 
		{
			super(context);     
		}
		
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) 
		{
//			widthMeasureSpec = MeasureSpec.makeMeasureSpec(900, MeasureSpec.AT_MOST);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(20000, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}  
	}
    
    class SecondLevelAdapter extends BaseExpandableListAdapter 
    {

        private Activity activity;
        private ArrayList<PermissionInfo> childtems;
        private LayoutInflater inflater;
        private PermissionInfo child;
        private ArrayList<PermissionsCategory> parentItems;

        private Properties permDesc;
        private Context context;
        private ExpandableListView eLV=null;
        private View groupView = null;
        private int lastExpandedCategoryPosition;
        
        // constructor
        public SecondLevelAdapter(Context contextParam, ArrayList<PermissionsCategory> parents )
        {
            this.parentItems = parents;
            this.context = contextParam;
            this.lastExpandedCategoryPosition=0; 
        }

        public void setInflater(LayoutInflater inflater, Activity activity) 
        {
            this.inflater = inflater;
            this.activity = activity;
            
//            Config config = Config.getInstance(context); //Even though context is null, instance would be already NOT-NULL due to initializations in mainactivity.
//            permDesc = config.getAppDescriptions();

        }
        
        // method getChildView is called automatically for each child view.
        //  Implement this method as per your requirement
        @Override
        public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) 
        {

 //           PermissionsCategory pc = parentItems.get(groupPosition);
//            childtems = pc.getPermissions();
//            int pcID = pc.getID();
            
            child = childtems.get(childPosition);
            

            TextView textView = null;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.child_view, parent, false);
            }
        
    		
            
             // get the textView reference and set the value
            textView = (TextView) convertView.findViewById(R.id.textViewChild);
            
            String permission = child.getName(); 
            permission = "- " + permission;
            
            textView.setText(permission);
            
            String strippedPermission="";
//            if(permission.length() > 19)
//            	strippedPermission = permission.substring(19);
            
            //Permission description
            String desc = child.getLongDesc();
            
//            if(permDesc != null && permDesc.containsKey(strippedPermission))
//            {
//            	 desc = permDesc.getProperty(strippedPermission);
//            }
            
            desc = desc.trim();
        	TextView descrTV = (TextView) convertView.findViewById(R.id.descr);
            descrTV.setText(desc);

            // set the ClickListener to handle the click event on child item
//            convertView.setOnClickListener(new OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(activity, child.get(childPosition),
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
            

            
          ImageView filterIV = (ImageView) convertView.findViewById(R.id.permfilterBar); 
  		  
       	  if(child.isFiltered())
      	  {
       		 filterIV.setBackgroundColor(Color.RED);
//      		  convertView.setBackgroundColor(Color.RED);
      	  }
       	  else
       	  {
       		  filterIV.setBackgroundColor(Color.TRANSPARENT);
       	  }

            return convertView;
        }

        // method getGroupView is called automatically for each parent item
        // Implement this method as per your requirement
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) 
        {

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.category_view, parent, false);
            }


            groupView = convertView;
            
            PermissionsCategory pc = parentItems.get(groupPosition);

            childtems = pc.getPermissions();
            
            TextView cTV = (TextView) convertView.findViewById(R.id.textViewCategoryName);
            cTV.setText(pc.getName());
          
          
          ImageView iv = (ImageView) convertView.findViewById(R.id.icon_category);
          String  iconName = pc.getIcon();
          

//          switch (iconName) {
//          case "ic_android.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_android));	
//              break;
//          case "ic_perm_location.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perm_location));	
//              break;
//          case "ic_perm_scan_wifi.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perm_scan_wifi));	
//              break;
//          case "ic_perm_phone.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perm_phone));	
//              break;
//          case "ic_accounts.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_accounts));	
//              break;
//          case "ic_bluetooth.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bluetooth));	
//              break;
//          case "ic_health.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_health));	
//              break;
//          case "ic_system_tools.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_system_tools));	
//              break;
//          case "ic_perm_camera_microphone.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perm_camera_microphone));	
//              break;
//          case "ic_perm_data_setting.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perm_data_setting));	
//              break;
//          case "ic_private_info.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_private_info));	
//              break;
//          case "ic_internet.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_internet));	
//              break;
//          case "ic_storage.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_storage));	
//              break;
//          case "ic_perm_contacts.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perm_contacts));	
//              break;
//          case "ic_sms.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sms));	
//              break;
//          case "ic_device_control.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_device_control));	
//              break;
//          case "ic_google_cloud.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_google_cloud));	
//              break;
//          case "ic_perm_unknown.png":
//        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_perm_unknown));	
//              break;
//        	 
//          }
          if(isExpanded)
          {
        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_expander_minimized_light_down));	
          }
          else{
        	  
        	  iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_expander_maximized_light_right));	 // it is maximized
          }

          
          
          
    ///      cTV.setChecked(isExpanded);
          //cTV.setClickable(true);
//          cTV.setFocusable(false);
            
          eLV = (ExpandableListView) parent;
//          eLV.expandGroup(groupPosition);
          
//          eLV.setClickable(true);
     
          int pcID = pc.getID();
          
          ImageView filterIV = (ImageView) convertView.findViewById(R.id.categoryfilterBar); 
        		  
     	  if(pc.isFiltered())
    	  {
     		 filterIV.setBackgroundColor(Color.RED);
    	  }
     	  else
     	  {
     		  filterIV.setBackgroundColor(Color.TRANSPARENT);
     	  }
     	  
            return convertView;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) 
        {
            return  null; //this.parentItems.get(groupPosition).getPermissions().get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) 
        {
            return childPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) 
        {
//        	Log.v("EXXXXXXXXXXXx", "getChildrenCount +" + groupPosition);
            return parentItems.get(groupPosition).getPermissions().size();
        }

        @Override
        public Object getGroup(int groupPosition) 
        {
            return null; //parentItems.get(groupPosition);
        }

        @Override
        public int getGroupCount() 
        {
            return parentItems.size();
        }

        @Override
        public void onGroupCollapsed(int groupPosition) 
        {
            super.onGroupCollapsed(groupPosition);
//            if(groupView != null)
//            {
//                ImageView iv = (ImageView) groupView.findViewById(R.id.icon_category);
//                iv.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_menu_expander_minimized_light));	
//            }
        }

        @Override
        public void onGroupExpanded(int groupPosition)
        {
            super.onGroupExpanded(groupPosition);
            
          	if(groupPosition != lastExpandedCategoryPosition){
        		eLV.collapseGroup(lastExpandedCategoryPosition);
            }

            super.onGroupExpanded(groupPosition);           
            lastExpandedCategoryPosition = groupPosition;
        }

        @Override
        public long getGroupId(int groupPosition) 
        {
            return groupPosition;
        }

        @Override
        public boolean hasStableIds() 
        {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition)
        {
            return true;
        }
}
