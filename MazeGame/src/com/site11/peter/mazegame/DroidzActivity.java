package com.site11.peter.mazegame;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

public class DroidzActivity extends Activity implements SensorEventListener {
    /** Called when the activity is first created. */
	
	private static final String TAG = DroidzActivity.class.getSimpleName();


	private SensorManager sensorManager;
	private MainGamePanel panel;
	private boolean accSensorReg;
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // force window to stay enabled
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		Display display = getWindowManager().getDefaultDisplay();
		// Following two lines require API level 13 or higher
		//Point size = new Point();
		//display.getSize(size);
        // set our MainGamePanel as the View
		panel = new MainGamePanel(this, display.getWidth(), display.getHeight(), this);
        setContentView(panel);
        Log.d(TAG, "View added");
        
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accSensorReg = false;
    }

	@Override
	protected void onDestroy() {
		Log.d(TAG, "Destroying...");
		panel.setRunning(false);
		super.onDestroy();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		panel.onAccEvent(event);
	}
    
   @Override
    protected void onResume()
    {
       super.onResume();
       if(!accSensorReg) 
       {
	        // Register this class as a listener for the accelerometer sensor
	        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
	                SensorManager.SENSOR_DELAY_GAME);
	        accSensorReg = true;
       }
        panel.onResume();
    }
   
   @Override
   protected void onPause()
   {
	   super.onPause();
	   if(accSensorReg)
	   	{
	   		sensorManager.unregisterListener(this);
	   		accSensorReg = false;
	   	}
	   panel.onPause();
   }

    @Override
    protected void onStop()
    {
        super.onStop();
        // Unregister the listener
    	if(accSensorReg)
    	{
    		sensorManager.unregisterListener(this);
    		accSensorReg = false;
    	}
		Log.d(TAG, "Stopping...");
		panel.setRunning(false);
    }
    
    @Override 
    public void onBackPressed()
    {
    	panel.handleBackPressed();
    }
   
    /*
    @Override 
    public void onMenuPressed()
    {
    	panel.handleMenuPressed();
    }*/
    
    public void closeGame()
    {
    	//onBackPressed();
    	finish();
    }
}