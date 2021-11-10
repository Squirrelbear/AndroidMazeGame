package com.site11.peter.mazegame;

import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;

public class DoorWall extends Wall {

	public boolean isWallUp;
	public int listenID;
	
	public DoorWall(float x, float y, float width, float height, int col, int row, Maze maze) {
		super(x, y, width, height, col, row, maze);
		isWallUp = true;
		wallType = 2;
		texture = maze.getTexture(2);
	}
	
	public DoorWall(float x, float y, float width, float height, int col, int row, Maze maze, int listenID) {
		super(x, y, width, height, col, row, maze);
		isWallUp = true;
		wallType = 2;
		texture = maze.getTexture(2);
		this.listenID = listenID;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		/*Paint paint = new Paint();
		if(isWallUp)
			paint.setColor(Color.GRAY);
		else
			paint.setColor(Color.GREEN);
		canvas.drawRect(rect, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rect, paint);*/
	}
	
	public void setWallUp(boolean isWallUp)
	{
		if(isWallUp)
		{
			texture = maze.getTexture(2);
		}
		else
		{
			texture = maze.getTexture(3);
		}
		
		this.isWallUp = isWallUp;
	}
	
	public boolean collidingCircle(Ball b)
	{
		if(!isWallUp) return false;
		
		return collidingCircle(b.getX(), b.getY(), b.getRadius());
	}
	
	public boolean reactCollision(Ball b)
	{
		return true;
	}
	
	public int getListenID()
	{
		return listenID;
	}
}
