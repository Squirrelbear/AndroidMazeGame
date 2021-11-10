package com.site11.peter.mazegame;

import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;

public class ButtonWall extends Wall {

	public boolean isHit;
	private int targetX, targetY;
	private int notifyID;
	private boolean useTarget;
	
	public ButtonWall(float x, float y, float width, float height, int col, int row, Maze maze, int targetX, int targetY) {
		super(x, y, width, height, col, row, maze);
		wallType = 1;
		isHit = false;
		this.targetX = targetX;
		this.targetY = targetY;
		texture = maze.getTexture(1);
		useTarget = true;
	}
	
	public ButtonWall(float x, float y, float width, float height, int col, int row, Maze maze, int notifyID) {
		super(x, y, width, height, col, row, maze);
		wallType = 1;
		isHit = false;
		this.notifyID = notifyID;
		texture = maze.getTexture(1);
		useTarget = false;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		/*Paint paint = new Paint();
		if(isHit)
			paint.setColor(Color.BLACK);
		else
			paint.setColor(Color.CYAN);
		canvas.drawRect(rect, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rect, paint);*/
	}
	
	public void setHit(boolean isHit)
	{
		if(isHit)
		{
			texture = maze.getTexture(0);
		}
		else 
		{
			texture = maze.getTexture(1);
		}
		this.isHit = isHit;
	}
	
	public boolean collidingCircle(Ball b)
	{
		if(!isHit && collidingCircle(b.getX(), b.getY(), b.getRadius()))
		{
			setHit(true);
			if(!useTarget)
				maze.notifyDoors(notifyID);
			else
				maze.switchDoor(targetX, targetY, false);
		}
		return false;
	}
	
	public boolean reactCollision(Ball b)
	{
		return true;
	}
}
