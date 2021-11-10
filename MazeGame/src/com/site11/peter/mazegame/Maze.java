package com.site11.peter.mazegame;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class Maze {
	private List<Wall> walls;
	private List<DoorWall> doors;
	private int screenWidth, screenHeight;
	private Ball ball;
	
	private long accelUpdate;	
	private MainGamePanel panel;
	
	private int curMapID;
	private int[][] mazeGrid;
	private ButtonTarget[] buttonMap;
	private NotifyLink[] buttonLinks;
	
	private List<Bitmap> bitmaps;	
	private Random gen;
	
	public Maze(int screenWidth, int screenHeight, MainGamePanel panel)
	{
		this.panel = panel;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		walls = new ArrayList<Wall>();
		doors = new ArrayList<DoorWall>();

		loadTextures(panel.getContext());
		
		gen = new Random();
		
		loadRandomMap();
		
		//curMapID = 0;
		
		/*
		// This defines the map
		// 0 = nothing
		// 1 = button 
		// 2 = door
		// 3 = level end
		// 4 = normal wall
		// 5 = ball start		
		                   // 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19
		int[][] mazeGrid = { {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, // 0
							 {4, 1, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4}, // 1
							 {4, 0, 0, 0, 0, 4, 0, 0, 0, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 4}, // 2
							 {4, 0, 0, 0, 0, 4, 0, 0, 0, 4, 0, 0, 0, 4, 0, 0, 0, 0, 0, 4}, // 3
							 {4, 0, 0, 0, 0, 4, 4, 4, 0, 4, 4, 0, 4, 4, 0, 0, 0, 0, 0, 4}, // 4
							 {4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4}, // 5
							 {4, 0, 0, 0, 0, 0, 0, 0, 0, 5, 4, 4, 4, 4, 0, 0, 0, 0, 0, 4}, // 6
							 {4, 0, 0, 0, 0, 4, 4, 2, 4, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 4}, // 7
							 {4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4}, // 8
							 {4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4}, // 9
							 {4, 3, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4}, // 10
							 {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4} }; // 11
		this.mazeGrid = mazeGrid;
		ButtonTarget[] buttonMap = new ButtonTarget[1];
		buttonMap[0] = new ButtonTarget(1, 1, 7, 7);
		this.buttonMap = buttonMap;
		
		setMaze(mazeGrid, buttonMap);
		*/
		/*
		int cols = 10;
		int rows = 5;
		float wallWidth = screenWidth / cols;
		float wallHeight = screenHeight / rows;
		
		walls = new ArrayList<Wall>();
		for(int row = 0; row < rows; row++)
		{
			for(int col = 0; col < cols; col++)
			{
				if(col == 0 || row == 0 || col == cols-1 || row == rows-1)
					walls.add(new Wall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this));
			}
		}
		walls.add(new DoorWall(1 * wallWidth, 1 * wallHeight, wallWidth, wallHeight, 1, 1, this));
		walls.add(new ButtonWall(7 * wallWidth, 3 * wallHeight, wallWidth, wallHeight, 8, 4, this, 1, 1));
		walls.add(new LevelEndWall(3 * wallWidth, 1 * wallHeight, wallWidth, wallHeight, 3, 1, this));
		
		float ballRadius = (wallWidth > wallHeight) ? wallHeight / 2 : wallWidth / 2;
		ballRadius *= 0.8f;
		ball = new Ball(screenWidth / 2, screenHeight / 2, ballRadius, this);
		accelUpdate = -1;
		*/
	}
	
	public void update(long elapsedTime)
	{
		ball.update(elapsedTime);
	}
	
	public boolean testCollision(Ball b)
	{
		ball.setColliding(false);
		for(Wall w : walls)
		{
			if(w.collidingCircle(ball))
			{
				ball.setColliding(true);
				return w.reactCollision(b);
			}
		}
		return false;
	}
	
	public void draw(Canvas canvas)
	{
		for(Wall w : walls)
		{
				w.draw(canvas);
		}
		
		ball.draw(canvas);
	}
	
	public void handleTouchEvent(float x, float y)
	{
		//ball.setLocation(x, y);
	}
	
	public void handleSensorEvent(float xAcc, float yAcc, float zAcc)
	{
		if(accelUpdate != -1)
		{
			long diff = System.currentTimeMillis() - accelUpdate;
			ball.updateVelocity(xAcc * diff, yAcc * diff);
		}
		
		accelUpdate = System.currentTimeMillis();
	}
	
	public void switchDoor(int targetX, int targetY, boolean isWallUp)
	{
		for(Wall w : walls)
		{
			if(w.isGridCoord(targetX, targetY))
			{
				if(w.getWallType() == 2)
				{
					((DoorWall)w).setWallUp(isWallUp);
				}
				else
				{
					Log.d("MAZE", "ERROR: " + targetX + "," + targetY + " : " + w.getWallType());
				}
				break;
			}
		}
	}
	
	public void notifyDoors(int notifyID)
	{
		//Log.d("MAZE", "Notifying: " + notifyID);
		
		for(int i = 0; i < doors.size(); i++)
		{
			if(doors.get(i).getListenID() == notifyID)
			{
				doors.get(i).setWallUp(false);
				//Log.d("MAZE", "Notified " + doors.get(i).getCol() + " " + doors.get(i).getRow());
			}
		}
	} 
	
	public void triggerEndLevel()
	{
		panel.handleLevelFinished();
	}
	
	public void setMaze(int[][] mazeGrid, ButtonTarget[] buttonMap)
	{
		walls.clear();
		
		float wallWidth = screenWidth / mazeGrid[0].length;
		float wallHeight = screenHeight / mazeGrid.length;
		
		int ballCol = 0;
		int ballRow = 0;
		
		for(int row = 0; row < mazeGrid.length; row++)
		{
			for(int col = 0; col < mazeGrid[row].length; col++)
			{
				if(mazeGrid[row][col] == 0) continue;
				
				if(mazeGrid[row][col] == 5)
				{
					ballCol = col;
					ballRow = row;
				}
				
				int targetX = 0, targetY = 0;
				for(int k = 0; k < buttonMap.length; k++)
				{
					if(buttonMap[k].isSource(col, row))
					{
						targetX = buttonMap[k].targetX;
						targetY = buttonMap[k].targetY;
					}
				}
				
				walls.add(createWall(mazeGrid[row][col], col, row, wallWidth, wallHeight, targetX, targetY));
			}
		}
		
		float ballRadius = (wallWidth > wallHeight) ? wallHeight / 2 : wallWidth / 2;
		ballRadius *= 0.7f;
		ball = new Ball(ballCol * wallWidth + ballRadius, ballRow * wallHeight + ballRadius, ballRadius, this);
		accelUpdate = -1;
	}
		
	public void setMaze(int[][] mazeGrid, NotifyLink[] buttonLinks)
	{
		walls.clear();
		doors.clear();
		
		float wallWidth = screenWidth / mazeGrid[0].length;
		float wallHeight = screenHeight / mazeGrid.length;
		
		int ballCol = 0;
		int ballRow = 0;
		
		for(int row = 0; row < mazeGrid.length; row++)
		{
			for(int col = 0; col < mazeGrid[row].length; col++)
			{
				int type = mazeGrid[row][col];
				if(type == 0) continue;
				
				if(type == 5)
				{
					ballCol = col;
					ballRow = row;
				} 
				else if(type == 1 || type == 2)
				{
					int notifyID = -1;
					for(int k = 0; k < buttonLinks.length; k++)
					{
						if(buttonLinks[k].isLocation(col, row))
							notifyID = buttonLinks[k].id;
					}
					if(notifyID == -1)
					{
						Log.d("MAZE", "HUGE ERROR LINK NOT FOUND!!!");
					}
					
					Wall w = createWall(type, col, row, wallWidth, wallHeight, notifyID);
					walls.add(w);
					if(type == 2)
						doors.add((DoorWall)w);
				}
				else
				{
					walls.add(createWall(type, col, row, wallWidth, wallHeight, 0, 0));
				}
			}
		}
		
		float ballRadius = (wallWidth > wallHeight) ? wallHeight / 2 : wallWidth / 2;
		ballRadius *= 0.7f;
		ball = new Ball(ballCol * wallWidth + ballRadius, ballRow * wallHeight + ballRadius, ballRadius, this);
		accelUpdate = -1;
	}
	
	public void resetMaze()
	{
		setMaze(mazeGrid, buttonMap);
	}
	
	public Wall createWall(int type, int col, int row, float wallWidth, float wallHeight, int targetX, int targetY)
	{
		switch(type)
		{
		case 4:
			return new Wall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this);
		case 1:
			return new ButtonWall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this, targetX, targetY);
		case 2: 
			return new DoorWall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this);
		case 3:
			return new LevelEndWall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this);
		case 5:
			return new BallStartWall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this);
		}
		
		return null;
	}
	
	public Wall createWall(int type, int col, int row, float wallWidth, float wallHeight, int notifyID)
	{
		switch(type)
		{
		case 4:
			return new Wall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this);
		case 1:
			return new ButtonWall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this, notifyID);
		case 2: 
			return new DoorWall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this, notifyID);
		case 3:
			return new LevelEndWall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this);
		case 5:
			return new BallStartWall(col * wallWidth, row * wallHeight, wallWidth, wallHeight, col, row, this);
		}
		
		return null;
	}
	
	public Bitmap getTexture(int textureID)
	{
		if(textureID >= bitmaps.size())
			return bitmaps.get(0);
		return bitmaps.get(textureID);
	}
	
	public void setMap(int map)
	{
		this.curMapID = map;
	}
	
	public int getMapID()
	{
		return curMapID;
	}
	
	public void loadRandomMap()
	{
		int nextMap = gen.nextInt(2) + 1;
		loadMap(nextMap);
	}
	
	public void loadMap(int id)
	{		
		switch(id)
		{
		case 1:
			loadMap1();
			break;
		case 2:
			loadMap2();
			break;
		default:
			loadMap1();
			id = 1;
		}
		curMapID = id;
		
		setMaze(mazeGrid, buttonLinks);
	}
	
	private void loadTextures(Context context)
	{
		String[] fileNames = { "buttonhit.png", "buttonunhit.png", "doorlocked.png",
							   "dooropen.png", "levelend.png", "wall.png" };
		bitmaps = new ArrayList<Bitmap>();
		AssetManager assetManager = context.getAssets();
		
		for(String fileName : fileNames)
		{
			try {
				InputStream inputStream = assetManager.open(fileName);
				bitmaps.add(BitmapFactory.decodeStream(inputStream));
				inputStream.close();
			} catch (IOException e) {
				Log.d("MAZE", "CRITICAL ERROR Reading image: " + fileName);
			}
		}
		
	}
	
	private void loadMap1()
	{
		// This defines the map
		// 0 = nothing
		// 1 = button 
		// 2 = door
		// 3 = level end
		// 4 = normal wall
		// 5 = ball start		
		                   // 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19
		int[][] mazeGrid = { {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, // 0
							 {4, 1, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4}, // 1
							 {4, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4}, // 2
							 {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 0, 4}, // 3
							 {4, 0, 0, 2, 0, 0, 4, 1, 4, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 4}, // 4
							 {4, 0, 0, 4, 0, 0, 4, 0, 4, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 4}, // 5
							 {4, 0, 0, 4, 0, 0, 4, 2, 4, 0, 0, 4, 0, 0, 4, 0, 0, 0, 1, 4}, // 6
							 {4, 0, 1, 4, 0, 0, 0, 0, 4, 0, 0, 4, 0, 0, 4, 0, 4, 4, 4, 4}, // 7
							 {4, 2, 4, 4, 0, 0, 0, 0, 4, 0, 0, 4, 0, 0, 4, 2, 4, 4, 4, 4}, // 8
							 {4, 0, 0, 4, 0, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 4}, // 9
							 {4, 3, 0, 4, 1, 0, 4, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 5, 4}, // 10
							 {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4} }; // 11
		this.mazeGrid = mazeGrid;
		/*ButtonTarget[] buttonMap = new ButtonTarget[5];
		buttonMap[0] = new ButtonTarget(1, 1, 1, 8);
		buttonMap[1] = new ButtonTarget(2, 7, 3, 1);
		buttonMap[2] = new ButtonTarget(7, 4, 3, 4);
		buttonMap[3] = new ButtonTarget(18, 6, 7, 6);
		buttonMap[4] = new ButtonTarget(4, 10, 15, 8);
		this.buttonMap = buttonMap;*/
		NotifyLink[] buttonLinks = new NotifyLink[10];
		buttonLinks[0] = new NotifyLink(1, 1, 1);
		buttonLinks[1] = new NotifyLink(1, 8, 1);
		buttonLinks[2] = new NotifyLink(2, 7, 2);
		buttonLinks[3] = new NotifyLink(3, 1, 2);
		buttonLinks[4] = new NotifyLink(7, 4, 3);
		buttonLinks[5] = new NotifyLink(3, 4, 3);
		buttonLinks[6] = new NotifyLink(18, 6, 4);
		buttonLinks[7] = new NotifyLink(7, 6, 4);
		buttonLinks[8] = new NotifyLink(4, 10, 5);
		buttonLinks[9] = new NotifyLink(15, 8, 5);
		this.buttonLinks = buttonLinks;
	}
	
	private void loadMap2()
	{
		// This defines the map
		// 0 = nothing
		// 1 = button 
		// 2 = door
		// 3 = level end
		// 4 = normal wall
		// 5 = ball start		
		                   // 0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19
		int[][] mazeGrid = { {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4}, // 0
							 {4, 0, 1, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 1, 0, 4}, // 1
							 {4, 0, 0, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 4}, // 2
							 {4, 0, 4, 4, 0, 0, 4, 4, 4, 0, 4, 4, 4, 0, 0, 0, 4, 4, 0, 4}, // 3
							 {4, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 4, 0, 0, 0, 0, 0, 0, 4}, // 4
							 {4, 4, 4, 4, 4, 4, 4, 0, 0, 5, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4}, // 5
							 {4, 4, 4, 4, 4, 4, 4, 0, 0, 0, 0, 0, 4, 4, 4, 4, 4, 4, 4, 4}, // 6
							 {4, 0, 0, 0, 0, 0, 4, 4, 4, 0, 4, 4, 4, 0, 0, 0, 0, 0, 0, 4}, // 7
							 {4, 0, 4, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 4, 4, 0, 4}, // 8
							 {4, 0, 0, 4, 0, 0, 0, 4, 4, 4, 4, 4, 4, 0, 0, 0, 4, 0, 0, 4}, // 9
							 {4, 0, 1, 4, 0, 0, 0, 4, 3, 2, 2, 2, 2, 0, 0, 0, 4, 1, 0, 4}, // 10
							 {4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4} }; // 11
		this.mazeGrid = mazeGrid;
		NotifyLink[] buttonLinks = new NotifyLink[8];
		buttonLinks[0] = new NotifyLink(2, 1, 1);
		buttonLinks[1] = new NotifyLink(12, 10, 1);
		buttonLinks[2] = new NotifyLink(2, 10, 2);
		buttonLinks[3] = new NotifyLink(11, 10, 2);
		buttonLinks[4] = new NotifyLink(17, 1, 3);
		buttonLinks[5] = new NotifyLink(10, 10, 3);
		buttonLinks[6] = new NotifyLink(17, 10, 4);
		buttonLinks[7] = new NotifyLink(9, 10, 4);
		this.buttonLinks = buttonLinks;
	}
}
