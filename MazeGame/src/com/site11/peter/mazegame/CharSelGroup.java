package com.site11.peter.mazegame;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class CharSelGroup {
	private CharSelector[] charSels;
	private int focus;
	
	private float percentX, percentY;
	private float velX, velY;
	//private float maxVelX, maxVelY;
	private long accelUpdate;
	
	private Bitmap arrowTop, arrowBottom;
	
	public CharSelGroup(float x, float y, Context context)
	{
		loadTextures(context);
		charSels = new CharSelector[3];
		charSels[0] = new CharSelector(x,y, arrowTop, arrowBottom);
		charSels[1] = new CharSelector(x+250,y, arrowTop, arrowBottom);
		charSels[2] = new CharSelector(x+500,y, arrowTop, arrowBottom);
		
		focus = 0;
		charSels[0].setFocus(true);
		percentX = 0.5f;
		percentY = 0.5f;
	
		//maxVelX = 0.5f;
		//maxVelY = 0.8f;
	}
	
	public void update(long gameTime)
	{
		percentX = percentX + velX * gameTime/1000.0f;
		percentY = percentY + velY * gameTime/1000.0f;
		
		if(percentX > 1.1)
			prevSel();
		else if(percentX < -0.1)
			nextSel();
		else if(percentY > 1.1)
			charSels[focus].next();
		else if(percentY < -0.1)
			charSels[focus].previous();
		
		if(percentX < -0.1)
			percentX = 0.5f;
		if(percentX > 1.1)
			percentX = 0.5f;
		if(percentY < -0.1)
			percentY = 0.5f;
		if(percentY > 1.1)
			percentY = 0.5f;
			
		charSels[focus].setSelCurX(0.5f);//1-percentX);
		charSels[focus].setSelCurY(percentY);
	}
	
	public void draw(Canvas canvas)
	{
		for(int i = 0; i < charSels.length; i++)
			charSels[i].draw(canvas);
	}
	
	public void handleSensorEvent(float xAcc, float yAcc, float zAcc)
	{
		if(accelUpdate != -1)
		{
			if(yAcc > 1)
				this.velX = -0.5f;
			else if(yAcc < -1)
				this.velX = 0.5f;
			else 
			{
				this.velX = 0;
				this.percentX = 0.5f;
			}
				
			if(xAcc > 1)
				this.velY = 0.5f;
			else if(xAcc < -1)
				this.velY = -0.5f;
			else 
			{
				this.velY = 0;
				this.percentY = 0.5f;
			}
			
			/*long diff = System.currentTimeMillis() - accelUpdate;

			if(yAcc > 1 || yAcc < -1)
				this.velX += yAcc * diff / 900;
			else 
			{
				this.velX = 0;
				this.percentX = 0.5f;
			}
				
			if(xAcc > 1 || xAcc < -1)
				this.velY -= yAcc * diff / 90;
			else 
			{
				this.velY = 0;
				this.percentY = 0.5f;
			}
			
			if(velX > maxVelX)
				velX = maxVelX;
			if(velX < -maxVelX)
				velX = -maxVelX;
			if(velY > maxVelY)
				velY = maxVelY;
			if(velY < -maxVelY)
				velY = -maxVelY;*/
		}
		
		accelUpdate = System.currentTimeMillis();
	}
	
	public void nextSel()
	{
		percentX = 0.5f;
		
		if(focus < charSels.length-1)
		{
			charSels[focus].setFocus(false);
			focus++;
			charSels[focus].setFocus(true);
		}
	}
	
	public void prevSel()
	{
		percentX = 0.5f;
		
		if(focus > 0)
		{
			charSels[focus].setFocus(false);
			focus--;
			charSels[focus].setFocus(true);
		}
	}
	
	public String getString()
	{
		String result = "";
		for(CharSelector c : charSels)
		{
			result += c.getChar();
		}
		return result;
	}
	
	private void loadTextures(Context context)
	{
		AssetManager assetManager = context.getAssets();
		InputStream inputStream;
		try {
			inputStream = assetManager.open("arrowtop.png");
			arrowTop = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e) {
			// SERIOUS ERROR
		}
		try {
			inputStream = assetManager.open("arrowbottom.png");
			arrowBottom = BitmapFactory.decodeStream(inputStream);
			inputStream.close();
		} catch (IOException e) {
			// SERIOUS ERROR
		}
	}
}
