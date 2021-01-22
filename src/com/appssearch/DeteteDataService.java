package com.appssearch;

import java.io.File;
import java.util.Random;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DeteteDataService extends Service {

    // Binder given to clients
 //   private final IBinder mBinder = new LocalBinder();

    public DeteteDataService() {
//		super("LocalServiceConstructor");
		
		Log.d("LocalIntentService", "ConstructorCalled");
		// TODO Auto-generated constructor stub
	}
    
//
//	@Override
//	protected void onHandleIntent(Intent intent) {
//		// TODO Auto-generated method stub
//
//		Log.d("LocalIntentService", "onHHHHHHAndleIntent");
//
//		//Do whatever you want to do here for service. It will create a thread and run it.
//		
//	}

    @Override
    public void onCreate() {
        // The service is no longer used and is being destroyed
        Log.d("LocalIntentService", "onCCCRRRREEEEATTTTEEE");

        super.onCreate();
    }

//	  public class LocalBinder extends Binder {
//	        DeteteDataService getService() {
//	            // Return this instance of LocalService so clients can call public methods
//	        	
//	            return DeteteDataService.this;
//	        }
//	    }
//
//	    @Override
//	    public IBinder onBind(Intent intent) {
//	        Log.d("LocalIntentService", "onBBBBBBBBBBIND");
//
//	        
//	        return mBinder;
//	    }
	    
	    @Override
	    public int onStartCommand(Intent intent, int flags, int startId) {
	        // The service is starting, due to a call to startService()
	    	
//	        Log.d("LocalIntentService", "onSTARRRTTTT");
	        
			File dir = getApplicationContext().getFilesDir();  //getDir("images", Context.MODE_PRIVATE);
			
			Log.v("DeleteDataService", "FFFFFFFFFFFFFFFFFILLLEEEEEEEEEEEEPAAAAAAAATH");
			
			if(dir.isDirectory())
			{
				for(File f: dir.listFiles())
				{
//					Log.v("DeleteDataService", f.getAbsolutePath());
					
					String filePath = f.getAbsolutePath();
					
					if(filePath.endsWith(".png"))
					{
//						f.delete(); // Not sure if I want to delete all the .png files.
					}
				}
			}

	        stopSelf();
	        
	        return super.onStartCommand(intent, flags, startId);
	    }
//	    @Override
//	    public boolean onUnbind(Intent intent) {
//	        // All clients have unbound with unbindService()
//	        Log.d("LocalIntentService", "onUUUUUUUNNNBBBBBBIND");
//
//	        return super.onUnbind(intent);
//	    }
//	    @Override
//	    public void onRebind(Intent intent) {
//	        // A client is binding to the service with bindService(),
//	        // after onUnbind() has already been called
//	        Log.d("LocalIntentService", "onRRRRRRREEEBIND");
//	        super.onRebind(intent);
//
//	    }
	    @Override
	    public void onDestroy() {
	        // The service is no longer used and is being destroyed
	        Log.d("LocalIntentService", "onDESTRRROOOYYYY");

	        super.onDestroy();
	    }

	
	// Random number generator
    private final Random mGenerator = new Random();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    /** method for clients */
    
    public int getRandomNumber() {
      return mGenerator.nextInt(100);
    }

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	protected void onHandleIntent(Intent intent) {
//		// TODO Auto-generated method stub
//		
//	}
}
