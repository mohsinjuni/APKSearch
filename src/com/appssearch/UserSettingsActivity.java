package com.appssearch;

import com.appsearch.R;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class UserSettingsActivity extends PreferenceActivity {

	  @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	
	        
	        addPreferencesFromResource(R.xml.user_settings);
	        }
	

}
