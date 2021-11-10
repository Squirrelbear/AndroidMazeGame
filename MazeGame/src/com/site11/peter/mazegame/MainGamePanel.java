package com.site11.peter.mazegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * This is the main surface that handles the ontouch events and draws
 * the image to the screen.
 */
public class MainGamePanel extends SurfaceView implements
		SurfaceHolder.Callback {

	private static final String TAG = MainGamePanel.class.getSimpleName();
	
	private MainThread thread;
	private Maze maze;
	private HighScoreScreen highScoreScreen;
	private Menu menu;
	private boolean levelFinished;
	private boolean menuEnabled;
	private long levelStartTime;
	private int screenWidth, screenHeight;
	private DroidzActivity activity;
	private boolean mGameIsRunning;
	
	public MainGamePanel(Context context, int screenWidth, int screenHeight, DroidzActivity activity) {
		super(context);
		// adding the callback (this) to the surface holder to intercept events
		getHolder().addCallback(this);
		
		// reference the controlling activity
		this.activity = activity;
		
		// create the game loop thread
		thread = new MainThread(getHolder(), this);
		mGameIsRunning = false;
		
		// make the GamePanel focusable so it can handle events
		setFocusable(true);
		
		levelFinished = false;
		menuEnabled = false;
		levelStartTime = System.currentTimeMillis();
		maze = new Maze(screenWidth, screenHeight, this);
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		menu = new Menu(screenWidth, screenHeight, this);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// at this point the surface is created and
		// we can safely start the game loop
		start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed");
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		thread.setRunning(false);
		thread.onResume();
		boolean retry = true;
		while (retry) {
			try {
				thread.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
		
		if(menuEnabled)
			menu.handleTouchEvent(event.getX(), event.getY());
		else if(!levelFinished)
			maze.handleTouchEvent(event.getX(), event.getY());
		else
			highScoreScreen.handleTouchEvent(event.getX(), event.getY());
		
		return super.onTouchEvent(event);
	}

	public void update(long elapsedTime)
	{
		if(menuEnabled)
			menu.update(elapsedTime);
		else if(!levelFinished)
			maze.update(elapsedTime);
		else 
			highScoreScreen.update(elapsedTime);
	}
	
	public void render(Canvas canvas)
	{
		// clear the backbuffer
		canvas.drawColor(Color.WHITE);
		
		// draw the maze
		if(menuEnabled)
			menu.draw(canvas);			
		else if(!levelFinished)
			maze.draw(canvas);
		else
			highScoreScreen.draw(canvas);
		
		 //Log.d(TAG, "Render done");
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		render(canvas);
		/*Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		circlePaint.setColor(0xFF000000);
		circlePaint.setStyle(Style.FILL);
		circlePaint.setStrokeWidth((float) 5.0);
		canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, 100, circlePaint);*/
	}

	public void onAccEvent(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {			
			float[] values = event.values;
		    float x = values[0];
		    float y = values[1];
		    float z = values[2];

			//Log.d(TAG, "Accel EVent: x=" + x + ",y=" + y);
			if(menuEnabled)
				menu.handleSensorEvent(x, y, z);
			else if(!levelFinished)
				maze.handleSensorEvent(x, y, z);
			else
				highScoreScreen.handleSensorEvent(x, y, z);
		}
	}
	
	public void handleLevelFinished()
	{
		long levelTime = (System.currentTimeMillis() - levelStartTime);// / 1000;
		highScoreScreen = new HighScoreScreen(screenWidth, screenHeight, levelTime, this);
		levelFinished = true;
	}
	
	public void handleBackPressed()
	{
		menuEnabled = !menuEnabled;
	}
	
	public void handleMenuPressed()
	{
		
	}
	
	public void setRunning(boolean running)
	{
		//thread.setRunning(running);
		mGameIsRunning = running;
		
		if(!running)
			thread.onPause();
		
	}
	
	public void closeGame()
	{
		activity.closeGame();
	}
	
	public void newGame()
	{
		//maze.resetMaze();
		maze.loadRandomMap();
		levelStartTime = System.currentTimeMillis();
		hideMenu();
		levelFinished = false;
	}
	
	public void resetMap()
	{
		maze.resetMaze();
		levelStartTime = System.currentTimeMillis();
		hideMenu();
		levelFinished = false;
	}
	
	public void hideMenu()
	{
		menuEnabled = false;
	}
	
	public void start() {
	    if (!mGameIsRunning) {
			thread.setRunning(true);
	        thread.start();
	        mGameIsRunning = true;
	    } else {
	        thread.onResume();
	    }
	}

	public void onPause()
	{
		thread.onPause();
	}

	public void onResume() {
		thread.onResume();
	}
	
	public int getMapID()
	{
		return maze.getMapID();
	}
}

