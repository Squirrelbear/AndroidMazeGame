package com.site11.peter.mazegame;

import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;

public class LevelEndWall extends Wall {
	public LevelEndWall(float x, float y, float width, float height, int col, int row, Maze maze) {
		super(x, y, width, height, col, row, maze);
		wallType = 3;
		texture = maze.getTexture(4);
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		super.draw(canvas);
		/*
		Paint paint = new Paint();
		paint.setColor(Color.RED);
		canvas.drawRect(rect, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rect, paint);*/
	}
	
	@Override
	public boolean collidingCircle(Ball b)
	{
		if(collidingCircle(b.getX(), b.getY(), b.getRadius()))
		{
			maze.triggerEndLevel();
		}
		return false;
	}
}