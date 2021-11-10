package com.site11.peter.mazegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
import android.graphics.RectF;

public class Wall {
	protected RectF rect;
	protected int col, row;
	protected int wallType;
	protected Maze maze;
	protected Bitmap texture;
	
	public Wall(RectF rect, int col, int row, Maze maze)
	{
		this.col = col;
		this.row = row;
		this.maze = maze;
		this.rect = rect;	
		wallType = 4;
		texture = maze.getTexture(5);
	}
	
	public Wall(float x, float y, float width, float height, int col, int row, Maze maze)
	{
		this.col = col;
		this.row = row;
		this.maze = maze;
		rect = new RectF(x, y, x + width, y + height);
		wallType = 4;
		texture = maze.getTexture(5);
	}
	
	public void draw(Canvas canvas)
	{
		canvas.drawBitmap(texture, null, rect, null);
		
		/*
		Paint paint = new Paint();
		paint.setColor(Color.MAGENTA);
		canvas.drawRect(rect, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rect, paint);*/
	}
	
	// http://stackoverflow.com/questions/401847/circle-rectangle-collision-detection-intersection
	public boolean collidingCircle(float x, float y, float radius)
	{
		float cDX = Math.abs(x - rect.left);
		float cDY = Math.abs(y - rect.top);
		
		if(cDX > (rect.width()/2.0f + radius)) return false;
		if(cDY > (rect.height()/2.0f + radius)) return false;
		
		if(cDX <= (rect.width()/2.0f)) return true;
		if(cDY <= (rect.height()/2.0f)) return true;
		
		float cD_sq = (float)(Math.pow(cDX - rect.width()/2.0f, 2.0f) + Math.pow(cDY - rect.height()/2.0f, 2.0f));
		
		return (cD_sq <= Math.pow(radius, 2.0f));
	}
	
	public boolean collidingCircle(Ball b)
	{
		return collidingCircle(b.getX(), b.getY(), b.getRadius());
	}
	
	public boolean reactCollision(Ball b)
	{
		return true;
	}

	public int getWallType()
	{
		return wallType;
	}
	
	public int getCol()
	{
		return col;
	}
	
	public int getRow()
	{
		return row;		
	}
	
	public boolean isGridCoord(int row, int col)
	{
		return this.row == row && this.col == col;
	}
}
