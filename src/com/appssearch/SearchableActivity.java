package com.appssearch;

import com.appsearch.R;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

public class SearchableActivity extends Activity {

	static final String[] MOBILE_OS = 
            new String[] { "Android", "iOS", "WindohwsMobile", "Blacksberry"};
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
//	  setContentView(R.layout.activity_main);

//	  setListAdapter(new MobileArrayAdapter(getBaseContext(), MOBILE_OS));
		
	  handleIntent(getIntent()); 

	 }
	 
	  public void onNewIntent(Intent intent) { 
	      setIntent(intent); 
	      handleIntent(intent); 
	   } 

	   public void onListItemClick(ListView l, 
	      View v, int position, long id) { 
	      // call detail activity for clicked entry 
	   } 

	   private void handleIntent(Intent intent) { 
	      if (Intent.ACTION_SEARCH.equals(intent.getAction())) { 
	         String query = 
	               intent.getStringExtra(SearchManager.QUERY); 
	         doSearch(query); 
	      } 
	   }    

	   private void doSearch(String queryStr) { 
	   // get a Cursor, prepare the ListAdapter
	   // and set it
	   } 
	   

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu) {
	  // Inflate the menu; this adds items to the action bar if it is present.
	  getMenuInflater().inflate(R.menu.main, menu);
	  
//	  SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//	  SearchView searchView = (SearchView) menu.findItem(R.id.action_settings).getActionView();
//	    // Assumes current activity is the searchable activity
//	  searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//	  searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
//	  android:actionViewClass="android.widget.SearchView"
			  
//	  MenuInflater inflater = getMenuInflater();
//	    inflater.inflate(R.menu.main, menu);
//	    
//	    SearchManager searchManager =
//	            (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//	     SearchView searchView =
//	             (SearchView) menu.findItem(R.id.search).getActionView();
//	     searchView.setSearchableInfo(
//	             searchManager.getSearchableInfo(getComponentName()));

	    return true;
	 }
	 
	 @Override
	    public boolean onSearchRequested() {
	    	Bundle bundle=new Bundle();
			bundle.putString("extra", "exttra info");
			Log.d("AAAAAAAAA", "aaaaaaaaaaaaaaaaaaaaaa");
			// search initial query
//			startSearch("Country", false, bundle, false);
			return true;
	    }

	 /**
	  * @author Prabu
	  * Private class which runs the long operation. ( Sleeping for some time )
	  */
	
	}