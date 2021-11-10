package com.site11.peter.mazegame;

import java.util.Collections;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

public class HighScoreScreen {
	private int screenWidth, screenHeight;
	private float time;
	private CharSelGroup nameInput;
	private MenuButton save, skip, newGame, quitGame;
	private MainGamePanel panel;
	private DatabaseHandler db;
	private List<HighScore> scores;
	private boolean showScoreInput;
	
	public HighScoreScreen(int screenWidth, int screenHeight, long time, MainGamePanel panel)
	{
		db = new DatabaseHandler(panel.getContext());
		//db.clearDatabase();
		
		this.panel = panel;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		
		this.time = time/1000.0f;
		
		// get the list of scores and check if the score is a high score
		scores = db.getScores(panel.getMapID());
		Log.d("HIGHSCORES", "Scores: " + scores.size());
		
		boolean isHighScore = false;
		if(scores.size() >= 5)
		{
			for(int i = 0; i < scores.size(); i++)
			{
				//Log.d("HIGHSCORES", time + " " + scores.get(i).getScore() + " " + (time < scores.get(i).getScore()));
				if(this.time < scores.get(i).getScore())
				{
					isHighScore = true;
					break;
				}
			}
		}
		else
		{
			isHighScore = true;
		}
		showScoreInput = isHighScore;
		
		Collections.sort(scores, new ScoreComparator());
		
		float btnWidth = 300;
		float btnHeight = 100;
		float btnYPos = this.screenHeight - 110;
		float btnLeftX = this.screenWidth / 2 - btnWidth - 30;
		float btnRightX = this.screenWidth / 2 + 30;
		
		if(isHighScore)
		{
			nameInput = new CharSelGroup(100,300, panel.getContext());
			
			save = new MenuButton(new RectF(btnLeftX, btnYPos, btnLeftX+btnWidth, btnYPos + btnHeight), "Save", 1);
			skip = new MenuButton(new RectF(btnRightX, btnYPos, btnRightX+btnWidth, btnYPos + btnHeight), "Skip", 0);
		}

		newGame = new MenuButton(new RectF(btnLeftX, btnYPos, btnLeftX+btnWidth, btnYPos + btnHeight), "New Game", 2);
		quitGame = new MenuButton(new RectF(btnRightX, btnYPos, btnRightX+btnWidth, btnYPos + btnHeight), "Exit Game", 3);
	}
	
	public void update(long elapsedTime)
	{
		if(showScoreInput)
			nameInput.update(elapsedTime);
	}
	
	public void draw(Canvas canvas)
	{
		
		if(showScoreInput)
		{
			Paint paint = new Paint(); 
			paint.setColor(Color.rgb(178,34,34));//Color.RED); 
			paint.setTextSize(80); 
			canvas.drawText("Your Score: " + time, 10, 70, paint); 
			
			nameInput.draw(canvas);
			save.draw(canvas);
			skip.draw(canvas);
		}
		else 
		{
			Paint paint = new Paint(); 
			paint.setColor(Color.BLACK);//Color.RED); 
			paint.setTextSize(50); 
			canvas.drawText("RANK  NAME  TIME", 10, 50, paint);
			float y = 50;
			//int rank = 1;
			//for(int i = scores.size() - 1; i >= 0; i--)
			for(int i = 0; i < scores.size(); i++)
			{
				y += 50;
				HighScore s = scores.get(i);
				canvas.drawText((i+1) + ".         " + s.getName() + "     " + s.getScore(), 10, y, paint);
				//rank++;
			}
			paint.setColor(Color.RED);//Color.RED); 
			paint.setTextSize(80); 
			y = screenHeight/2 - 40;
			float x = screenWidth/2 + 100;
			canvas.drawText(time + "", x, y, paint);
			paint.setTextSize(50);
			canvas.drawText("Your score:", x, y-80, paint);
			
			newGame.draw(canvas);
			quitGame.draw(canvas);
		}
	}
	
	public void handleTouchEvent(float x, float y)
	{
		int result = -1;
		if(showScoreInput)
		{
			if(save.isPointInButton(x, y))
			{
				result = 1;
			}
			else if(skip.isPointInButton(x, y))
			{
				result = 0;
			}
		}
		else 
		{
			if(newGame.isPointInButton(x, y))
			{
				db.close();
				panel.newGame();
			}
			else if(quitGame.isPointInButton(x, y))
			{
				db.close();
				panel.closeGame();
			}
		}
		
		if(result == 0 || result == 1)
		{
			if(result == 1)
			{
				// remove the lowest score if needed
				if(scores.size() >= 5)
				{
					int removeID = scores.get(scores.size()-1).getID();
					db.deleteScore(removeID);
					Log.d("HIGHSCORE", "Deleting score: " + removeID + " time: " + scores.get(scores.size()-1).getScore());
				}
				HighScore score = new HighScore(panel.getMapID(), time, nameInput.getString());
				db.addScore(score);
				Log.d("HIGHSCORE", "Score Inserted");
				
				// update the known list of scores
				scores = db.getScores(panel.getMapID());
				Collections.sort(scores, new ScoreComparator());

				Log.d("HIGHSCORES", "New Score Count: " + scores.size());
			}
			
			// switch score view
			showScoreInput = false;
		}
	}
	
	public void handleSensorEvent(float xAcc, float yAcc, float zAcc)
	{
		if(showScoreInput)
			nameInput.handleSensorEvent(xAcc, yAcc, zAcc);
	}
}
