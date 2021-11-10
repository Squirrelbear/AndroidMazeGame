package com.site11.peter.mazegame;

import android.graphics.Canvas;

public class BallStartWall extends Wall {
	
	public BallStartWall(float x, float y, float width, float height, int col, int row, Maze maze) {
		super(x, y, width, height, col, row, maze);
		wallType = 5;
	}
	
	@Override
	public void draw(Canvas canvas)
	{
		// draw nothing
	}
	
	@Override
	public boolean collidingCircle(Ball b)
	{
		return false;
	}
}
