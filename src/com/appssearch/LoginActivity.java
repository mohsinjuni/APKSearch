package com.appssearch;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.appsearch.R;
import com.newproject.BOs.UserInfo;
import com.newproject.DALs.MyDatabase;

public class LoginActivity extends Activity {

	
	public static String ANDROID_ID = "3c5bb8cefa181e52"; 

	public static String GOOGLE_LOGIN = "";
	public static String GOOGLE_PASSWORD = "";
	private boolean isUserValidated= false;
	
	private static GooglePlayAPI service = new GooglePlayAPI(GOOGLE_LOGIN, GOOGLE_PASSWORD, ANDROID_ID);
	
	public static String accessToken = "";

	private SharedPreferences sharedPrefs=null;

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		setContentView(R.layout.login2);
		
	

		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//         sharedPrefsEditor.apply();
//         
		 		 
/*	     if(!ui.getUsername().equalsIgnoreCase("myUsername"))
	     {
	    	 Intent intent = new Intent(getBaseContext(), MainActivity.class );
	    	 startActivity(intent);
	     }
*/	
	 }
	
	public void onButtonClicked(View v)
	{
		boolean successfulLogin = true;
		String accessToken= "";

		if(v.getId() == R.id.cancel)
		{
			Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
		}
		else if(v.getId() == R.id.confirm)
		{
		
			EditText usernameET = (EditText) findViewById(R.id.username);
			String username = usernameET.getText().toString();

			EditText passwordET = (EditText) findViewById(R.id.password);
			String password = passwordET.getText().toString();

			GOOGLE_LOGIN = username;
			GOOGLE_PASSWORD = password;
						
			AsyncTaskRunner task = new AsyncTaskRunner();
			task.execute();
			

			
		}
	}
	
	 private class AsyncTaskRunner extends AsyncTask<String, String, String> {

		  //query string to adopter class for further loading of data.
//			ArrayList<MyAppDetails> appList;
			private String maturityFilter;
			private String searchResultFilter;
			

		  @Override
		  protected String doInBackground(String... params) {



			  accessToken = "";
		
				service = new GooglePlayAPI(GOOGLE_LOGIN, GOOGLE_PASSWORD, ANDROID_ID);
				try {
					accessToken = service.login();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
//					Toast.makeText(getApplicationContext(), "Login failed..", Toast.LENGTH_SHORT).show();

					
				}
				
	
		   return " ";
		  }

	
		  @Override
		  protected void onPostExecute(String result) {
		   // execution of result of Long time consuming operation

			  
				if(!accessToken.isEmpty())
				{
//					Toast.makeText(getApplicationContext(), "Access Token" + accessToken, Toast.LENGTH_SHORT).show();
					MyDatabase myDB = null;
					
					try{
					
				     myDB = new MyDatabase(getBaseContext());
				     
					}catch(Exception ex)
					{
						Toast.makeText(getApplicationContext(), "DB not found.", Toast.LENGTH_SHORT).show();
					}
				     UserInfo ui = new UserInfo(); //myDB.getUserInfo();
				     ui.setUsername(GOOGLE_LOGIN);
				     ui.setPassword(GOOGLE_PASSWORD);
				     myDB.updateUserInfo(ui);
				     
					EditText usernameET = (EditText) findViewById(R.id.username);
					EditText passwordET = (EditText) findViewById(R.id.password);
					
					usernameET.setText("");
					passwordET.setText("");
					
//		        	//first time app creation
		            Editor sharedPrefsEditor = sharedPrefs.edit();
		            
		            sharedPrefsEditor.putBoolean("isUserLoggedin", true);
		            sharedPrefsEditor.apply();
//		            
		            sharedPrefsEditor.commit();
		            
/*			    	 Intent intent = new Intent(getBaseContext(), MainActivity.class );
			    	 startActivity(intent);
*/			    	 
					//Toast.makeText(this, "Confirm", Toast.LENGTH_SHORT).show();
			    	 
			    	 finish();
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Account information is not correct.", Toast.LENGTH_SHORT).show();
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
}
