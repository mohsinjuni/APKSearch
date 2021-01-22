package com.appssearch;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.appsearch.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class MainActivity extends ListActivity implements OnScrollListener  {

	private final static int PREF_RESULT_CODE = 1;
	
	private MyAppDetails selectedListViewItem;
	private String searchQuery="";
	private static SharedPreferences sharedPrefs;
	private static Set<String> prefSelectedPermissions;

	EndlessListView lv;
	
	int mult = 1;
	private ProgressBar progressBar;
	private ProgressDialog progDialog;
	private static EndlessAdapter  adp;


	private TestGooglePlay tgp;
	private static final String TAG = "AppSearch";
	private AlarmManager alarm;
	private PendingIntent pendingNotificationIntent;	
	private static String oldDeleteDataFrequency="-1";
	private ArrayList<MyAppDetails> appList;
	private boolean isUserValidated= false;
	private boolean isMenuInflated= false;
	
	/*TODO:
	 * 
	 * 1) Delete existing search data
	 * 2) Update permission descriptions
	 * 3) Landscape and portrait views
	 * 4) Different versions
	 * 5) Icons
	 * 
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		tgp = new TestGooglePlay();

		progDialog = new ProgressDialog(MainActivity.this);
		
		Log.d("OnCREAAAAAAAAAAAATEESSSD  ","OnCREAAAAAAAAAAAATEE");

		registerForContextMenu(getListView());
		
//        sharedPrefs = getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE);
        
//		SharedPreferences sharedPrefs = PreferenceManager
//                .getDefaultSharedPreferences(this);

		 PreferenceManager.setDefaultValues(this, R.xml.user_settings, false);
		
		 sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		 
/*	     MyDatabase myDB = new MyDatabase(this);
	     
	     UserInfo ui = myDB.getUserInfo();

	     Toast.makeText(this, "username:" + ui.getUsername() , Toast.LENGTH_SHORT).show();
*/		 		 
			 //getApplicationContext().getSharedPreferences("AppSearch", MODE_PRIVATE);
        
//		 sharedPre
		    if(oldDeleteDataFrequency.equalsIgnoreCase("-1"))
	        {
//	        	//first time app creation
//	            Editor sharedPrefsEditor = sharedPrefs.edit();
	            
//	            sharedPrefsEditor.putString("prefSearchResult", "10");
//	            sharedPrefsEditor.putString("prefDeleteData", "1");
	            
	            oldDeleteDataFrequency = sharedPrefs.getString("prefDeleteData", "1");
//	            sharedPrefsEditor.apply();
//	            
//	            sharedPrefsEditor.commit();
	            
	 //           existingPrefSearchResult = sharedPrefs.getString("prefSearchResult", "-1");
	            
	          
	            
	            Intent intent = new Intent(MainActivity.this, DeteteDataService.class);
	            pendingNotificationIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
	            alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
	            int oldDataFrequencyInt = Integer.parseInt(oldDeleteDataFrequency);
	            int interval = oldDataFrequencyInt * 24 * 60 * 60 * 1000; //8 * 1000; 
	            long now = Calendar.getInstance().getTimeInMillis();
	            alarm.setRepeating(AlarmManager.RTC_WAKEUP, now, interval, pendingNotificationIntent);
	     
	        }
		    
		    searchQuery = sharedPrefs.getString("searchQuery", "");
		    
		    
		    Gson gson = new Gson();
		    String json = sharedPrefs.getString("appList", "");
		    
		    Type type = new TypeToken < List < MyAppDetails >> () {}.getType();
		    appList = new Gson().fromJson(json, type);
		    
		    
	    	if(appList!= null)
	    	{
				Set setA = new HashSet();
				
				prefSelectedPermissions = sharedPrefs.getStringSet("prefPermSelection",setA);
				
				if(appList != null && prefSelectedPermissions != null)
				{
					adp = new EndlessAdapter(getBaseContext(), appList, R.layout.search_result, prefSelectedPermissions);
					
					setListAdapter(adp);
					
			//		getListView().setOnScrollListener(MainActivity.this);
			//		SearchView searchItem = (SearchView) findViewById(R.id.options_menu_main_search);
			//		searchItem.clearFocus(); //setEnabled(false);
				}
		    	Log.v("askkkkd", "asdasd");
	    	}
//		  String existingDeleteData = sharedPrefs.getString("prefDeleteData", "-1");
 	}

	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		 sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
	     isUserValidated = sharedPrefs.getBoolean("isUserLoggedin", false);
	     
//	     if(!isUserValidated)
//	     {
//	    	 Intent intent = new Intent(getBaseContext(), LoginActivity.class );
//	    	 startActivity(intent);
//			//Toast.makeText(this, "Confirm", Toast.LENGTH_SHORT).show();
//	     }
		
	}
	 @Override
	  public Object getLastNonConfigurationInstance() {
	    return(getListAdapter());
	  }
	 
	 @Override
	  public void onNewIntent(Intent intent) { 
		  super.onNewIntent(intent);
		  
		  adp = null;

		  Log.d("onNEWWWWWWINTENT","onNEWWWWWWINTENT");
	      setIntent(intent); 
	      handleIntent(getIntent());
	      
//	      
	   } 
	   private void handleIntent(Intent intent) { 
		   
		   Log.d("onHANNNDDDLEEEINTENT","onHANNNDDDLEEEINTENT");
	      if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
	    	  
	          searchQuery = intent.getStringExtra(SearchManager.QUERY); 
	         doSearch(); 
	      } 
	   }    

	   private void doSearch() { 

		   AsyncTaskRunner runner = new AsyncTaskRunner();
//	    String sleepTime = time.getText().toString();
		   runner.execute();
	
	   } 
	
	 private class AsyncTaskRunner extends AsyncTask<String, String, String> {

		  //query string to adopter class for further loading of data.
//			ArrayList<MyAppDetails> appList;
			private String maturityFilter;
			private String searchResultFilter;
			

		  @Override
		  protected String doInBackground(String... params) {
		   publishProgress("Sleeping..."); // Calls onProgressUpdate()

	   
//	          maturityFilter = sharedPrefs.getString("prefContentFiltering", "NULL");
	          searchResultFilter = sharedPrefs.getString("prefSearchResult", "5");

		try {
			tgp.performOperations(getApplicationContext() ,searchQuery, maturityFilter, searchResultFilter);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		   Log.d("TTTTTTTTTTTTTT", "Inside doInBackgrommund");
			
		   return " ";
		  }


		  
		  @Override
		  protected void onPostExecute(String result) {
		   // execution of result of Long time consuming operation
			  progDialog.dismiss();
			  appList = new ArrayList<MyAppDetails>();
			  appList = tgp.getAppDetailsList();

			  lv = (EndlessListView) findViewById(R.id.listview);
				
			  if(adp == null)
			  {
//			  setContentView(R.layout.activity_main);
				  
				List<String> appNames = new ArrayList<String>();
				if(appList != null && appList.size() >0)
				{
					String[] values = new String[appList.size()];
					int i = 0;
					for(MyAppDetails app : appList)
					{
						appNames.add(app.getAppName());
					}
				}
				
				Collections.sort(appList, new MyAppDetailsComarator());
				
				//Set<String> defValues = new Set<>();
				Set setA = new HashSet();
				
				prefSelectedPermissions = sharedPrefs.getStringSet("prefPermSelection",setA);
						
				adp = new EndlessAdapter(getBaseContext(), appList, R.layout.search_result, prefSelectedPermissions);
				
				setListAdapter(adp);
				
				getListView().setOnScrollListener(MainActivity.this);
				
				SearchView searchView = (SearchView) findViewById(R.id.options_menu_main_search);
				searchView.setQuery(searchQuery, false);
//				searchView.setIconified(true);
				searchView.clearFocus(); //setEnabled(false);
				
				 Editor prefsEditor = sharedPrefs.edit();
			     prefsEditor.putString("searchQuery", searchQuery);
			     prefsEditor.commit();

		        
//		        String existingDeleteData = sharedPrefs.getString("prefDeleteData", "-1");
//		        
//		        Toast.makeText(getApplicationContext(), "DeleteData " + existingDeleteData, Toast.LENGTH_SHORT).show();
		        

			  }
			  else
			  {
//				  adp.addAll(createItems(mult));
//				  adp.notifyDataSetChanged();
			  }				
			  
			  
			  
		  }
		
		  
		  /*
		   * (non-Javadoc)
		   * 
		   * @see android.os.AsyncTask#onPreExecute()
		   */
		  @Override
		  protected void onPreExecute() {
		   // Things to be done before execution of long running operation. For
			  
			  progDialog.setMessage("Downloading data........");
			  progDialog.show();

		   // example showing ProgessDialog
		  }

		  /*
		   * (non-Javadoc)
		   * 
		   * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		   */
		  @Override
		  protected void onProgressUpdate(String... text) {
//		   finalResult.setText(text[0]);
		   // Things to be done while execution of long running operation is in
			  
		   // progress. For example updating ProgessDialog
		  }
		 }
			 


	 

	    @Override
	    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
	        super.onCreateContextMenu(menu, v, menuInfo);

	        getMenuInflater().inflate(R.menu.context_menu , menu);

	    }
	 
	    @Override
	    public boolean onContextItemSelected(MenuItem item) {

	    	switch (item.getItemId()){
	    	
	    	case R.id.item_gplay:
			        if(selectedListViewItem != null)
			        {
				        String pkgName = selectedListViewItem.getPkgName();
						   try {
							    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkgName));
							    startActivity(i);
							} catch (android.content.ActivityNotFoundException anfe) {
								anfe.printStackTrace();
							}
			        }
	    		}
	    	

	        return true;
	    }
	    
	    
	   public void onListItemClick(ListView l, View v, int position, long id) { 
	      // call detail activity for clicked entry 
		   
		   super.onListItemClick(l, v, position, id);
		   selectedListViewItem  = (MyAppDetails) this.getListAdapter().getItem(position);
		   
		   if(selectedListViewItem != null)
		   {
			   ArrayList<String> permsList = selectedListViewItem.getPermList();
			   
			   if(permsList.size() > 0)
			   {
				   ArrayList<String> strippedPermList = selectedListViewItem.getStrippedPermList();
				   
				   ArrayList<String> commonPermList = new ArrayList<String>(strippedPermList);
				   
				   commonPermList.retainAll(prefSelectedPermissions);
				   
				   Intent intent = new Intent().setClass(MainActivity.this, ExpandableListMainActivity.class);
				   
				   intent.putStringArrayListExtra("permissions", permsList);
				   intent.putStringArrayListExtra("commonStrippedPermissions", commonPermList);
				   
				   intent.putExtra("appName", selectedListViewItem.getAppName());
				   
				   startActivity(intent);
			   }
			   else
			   {
				   Toast.makeText(this, "This app does not need any permission for installation.", Toast.LENGTH_SHORT).show();
			   }

		   }
	   } 


	 @Override
	    public boolean onCreateOptionsMenu( Menu menu )
	    {
		 

	        getMenuInflater().inflate( R.menu.main, menu );

	        // Add SearchWidget.
	        SearchManager searchManager = (SearchManager) getSystemService( Context.SEARCH_SERVICE );
	        SearchView searchView = (SearchView) menu.findItem( R.id.options_menu_main_search ).getActionView();

	        searchView.setSearchableInfo( searchManager.getSearchableInfo( getComponentName() ) );
	        	        
	        isMenuInflated = true;
	        if(searchQuery != null && !searchQuery.isEmpty())
	        {
				searchView.setQuery(searchQuery, false);
				searchView.setIconified(false);
	        	searchView.clearFocus(); //setEnabled(false);
	        }

	        return super.onCreateOptionsMenu( menu );
	    }

	 
//	 @Override
//	 public boolean onPrepareOptionsMenu (Menu menu) {
//	      SearchView searchView = (SearchView) menu.findItem(R.id.options_menu_main_search).getActionView();
//	      searchView.setQuery("Jango", false);
//	      searchView.setSaveEnabled(true);
//	      // rest of code...
//	      
//	      return true;
//	 }
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if(id == R.id.action_settings)
		{
//		   	SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE); 
//	    	
//	    	String newDeleteDataFrequency = sharedPrefs.getString("prefDeleteData", "7"); 
//	    	
//	    	Toast.makeText(this, newDeleteDataFrequency, Toast.LENGTH_SHORT).show();
	    	
			Intent intent = new Intent(getBaseContext(), UserSettingsActivity.class);
			startActivityForResult(intent, PREF_RESULT_CODE);
		}
		else if(id == R.id.action_update_gmail_account)
		{
			Intent intent = new Intent(getBaseContext(), LoginActivity.class);
			startActivity(intent);
//			startActivityForResult(intent, PREF_RESULT_CODE);
		}
		return super.onOptionsItemSelected(item);
	}
	 
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
 
        switch (requestCode) {
        case PREF_RESULT_CODE:
 //           showUserSettings();
            setAlarmService();
            break;
 
        }
    }
    
   
    private void setAlarmService()
    {
    	
//    	SharedPreferences sharedPrefs = getApplicationContext().getSharedPreferences(TAG, Context.MODE_PRIVATE); 
    	
    	String newDeleteDataFrequency = sharedPrefs.getString("prefDeleteData", "2"); 
    	
//    	Toast.makeText(getApplicationContext(), "Old=" + oldDeleteDataFrequency + ", new=" + newDeleteDataFrequency,Toast.LENGTH_SHORT).show();
    	
    	if(!newDeleteDataFrequency.equalsIgnoreCase(oldDeleteDataFrequency))
    	{
            Intent intent = new Intent(MainActivity.this, DeteteDataService.class);
            pendingNotificationIntent = PendingIntent.getService(MainActivity.this, 0, intent, 0);
            alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
            
//    		Toast.makeText(getApplicationContext(), "Different",Toast.LENGTH_SHORT).show();
            alarm.cancel(pendingNotificationIntent);

            int newDeleteFrequencyInt = Integer.parseInt(newDeleteDataFrequency);
            int interval = newDeleteFrequencyInt * 24 * 60 * 60 * 1000; //* 8 * 1000; // 
           
            long now = Calendar.getInstance().getTimeInMillis();
            alarm.setRepeating(AlarmManager.RTC_WAKEUP, now, interval, pendingNotificationIntent);

            
           oldDeleteDataFrequency = newDeleteDataFrequency;
    	}
        
    	
    	
    }
    private void showUserSettings() {
 
    	SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
 
        StringBuilder builder = new StringBuilder();
 
//        builder.append("\n Username: "
//                + sharedPrefs.getString("prefUsername", "NULL"));
 
//        builder.append("\n Send report:"
//                + sharedPrefs.getBoolean("prefSendReport", false));
 
//        builder.append("\n Content Filtering: "
//                + sharedPrefs.getString("prefContentFiltering", "NULL"));

        builder.append("Search Result:  "
                + sharedPrefs.getString("prefSearchResult", "-1"));
        
    	String newDeleteDataFrequency = sharedPrefs.getString("prefDeleteData", "-1"); 

        builder.append("\n deleteFrequency: " + newDeleteDataFrequency);
        

//       TextView settingsTextView = (TextView) findViewById(R.id.textUserSettings);
//        settingsTextView.setText(builder.toString());
        
//        Toast.makeText(this, builder.toString(), Toast.LENGTH_LONG).show();
        
        
    }
    
    @Override
    public void onSaveInstanceState(Bundle state)
    {
    	super.onSaveInstanceState(state);
    	state.putParcelableArrayList("appList", appList);
    	
    }

    @Override
    public void onRestoreInstanceState(Bundle state)
    {
    	super.onRestoreInstanceState(state);
    	
    	if(state!= null)
    	{
	    	appList = state.getParcelableArrayList("appList");
	    	
			Set setA = new HashSet();
			
			prefSelectedPermissions = sharedPrefs.getStringSet("prefPermSelection",setA);
			
			if(appList != null && prefSelectedPermissions != null)
			{
				adp = new EndlessAdapter(getBaseContext(), appList, R.layout.search_result, prefSelectedPermissions);
				
				setListAdapter(adp);
				
		//		getListView().setOnScrollListener(MainActivity.this);
				
			     searchQuery = sharedPrefs.getString("searchQuery", "");
				
	
			}
	    	Log.v("asd", "asdasd");
    	}
    	
    }
	 @Override
	 public void onPause()
	 {
		 super.onPause();
		 
		 Editor prefsEditor = sharedPrefs.edit();
	     Gson gson = new Gson();
	     String json = gson.toJson(appList);
	     prefsEditor.putString("appList", json);
//	     prefsEditor.putString("searchQuery", searchQuery);
	     prefsEditor.commit();
	 }
 
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//	    this.currentFirstVisibleItem = firstVisibleItem;
//	    this.currentVisibleItemCount = visibleItemCount;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
//	    this.currentScrollState = scrollState;
//	    this.isScrollCompleted();
	 }

//	private void isScrollCompleted() {
//	    if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE) {
//	        /*** In this way I detect if there's been a scroll which has completed ***/
//	        /*** do the work for load more date! ***/
//	        if(!loadingData){
//	        	loadingData = true;
//	             doSearch();
//	        }
//	    }
//	}
	

	public class MyAppDetailsComarator implements Comparator<MyAppDetails>{
	
		@Override
		public int compare(MyAppDetails obj1, MyAppDetails obj2){
			return obj1.getPermissionCount()-obj2.getPermissionCount();
		}
	}

}

