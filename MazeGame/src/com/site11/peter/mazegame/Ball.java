package com.site11.peter.mazegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
//import android.util.Log;

public class Ball {

	private float x, y, radius;
	@SuppressWarnings("unused")
	private boolean colliding;
	private float velX;
	private float velY;
	private Maze maze;
	private float maxVel, minVel;
	
	public Ball(float x, float y, float radius, Maze maze)
	{
		this.maze = maze;
		
		this.x = x;
		this.y = y;
		this.radius = radius;
		colliding = false;
		velX = velY = 0;
		maxVel = 100;
		minVel = -100;
	}
	
	public void update(long gameTime)
	{
		float oldX = x;
		float oldY = y;

        //Log.d("BALL", "Vel: x=" + velX + " y=" + velY + " gameTime=" + gameTime);
		setLocation(x + velX * gameTime/1000.0f, y + velY * gameTime/1000.0f);
		if(maze.testCollision(this))
		{
			float newX = x;
			this.x = oldX;
			if(maze.testCollision(this))
			{
				this.x = newX;
				this.y = oldY;
				if(maze.testCollision(this))
				{
					setLocation(oldX, oldY);
				}
			}
		}
	}
	
	public void draw(Canvas canvas)
	{
		Paint circlePaint = new Paint();
		//if(colliding)
		//	circlePaint.setColor(Color.YELLOW);
		//else
		circlePaint.setColor(Color.BLUE);
		//circlePaint.setStyle(Style.FILL);
		//circlePaint.setStrokeWidth((float) 5.0);
		canvas.drawCircle(x, y, radius, circlePaint);
	}
	
	public void setLocation(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void setColliding(boolean colliding)
	{
		this.colliding = colliding;
		if(colliding)
		{
			velX = velY = 0;
		}
	}
	
	public float getX()
	{
		return x - (int)(1.4* radius);// - radius;
	}
	
	public float getY()
	{
		return y - (int)(1.40* radius);
	}
	
	public float getRadius()
	{
		return radius;
	}
	
	public void updateVelocity(float modX, float modY)
	{
		this.velX += modY;
		this.velY += modX;
		
		if(velX > maxVel)
			velX = maxVel;
		if(velX < minVel)
			velX = minVel;
		if(velY > maxVel)
			velY = maxVel;
		if(velY < minVel)
			velY = minVel;
			
	}
}
