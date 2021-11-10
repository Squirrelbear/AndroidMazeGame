package com.site11.peter.mazegame;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;

public class Menu {
	private int screenWidth, screenHeight;
	//private long accelUpdate;
	private MainGamePanel panel;
	private List<MenuButton> buttons;
	
	public Menu(int screenWidth, int screenHeight, MainGamePanel panel)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.panel = panel;
		buttons = new ArrayList<MenuButton>();
		float btnWidth = 300;
		float btnHeight = 100;
		float btnX = this.screenWidth / 2 - btnWidth / 2;
		float btnNGY = this.screenHeight / 2 - btnHeight / 2;
		float btnEGY = this.screenHeight / 2 + btnHeight / 2 + 30;
		float btnRGY = this.screenHeight / 2 - 3 * btnHeight / 2 - 30;
		buttons.add(new MenuButton(new RectF(btnX,btnRGY,btnX+btnWidth,btnRGY+btnHeight), "Resume", 2));
		buttons.add(new MenuButton(new RectF(btnX,btnNGY,btnX+btnWidth,btnNGY+btnHeight), "New Game", 0));
		buttons.add(new MenuButton(new RectF(btnX,btnEGY,btnX+btnWidth,btnEGY+btnHeight), "Exit Game", 1));
	}
	
	public void update(long elapsedTime)
	{
		
	}
	
	public void draw(Canvas canvas)
	{
		canvas.drawColor(Color.LTGRAY);
		for(MenuButton btn : buttons)
		{
			btn.draw(canvas);
		}
	}
	
	public void handleTouchEvent(float x, float y)
	{
		//ball.setLocation(x, y);
		int resultID = -1;
		for(MenuButton btn : buttons)
		{
			if(btn.isPointInButton(x, y))
			{
				resultID = btn.getActionID();
				break;
			}
		}
		
		if(resultID == 0)
		{
			panel.newGame();
		}
		else if(resultID == 1)
		{
			panel.closeGame();
		}
		else if(resultID == 2)
		{
			panel.hideMenu();
		}
	}
	
	public void handleSensorEvent(float xAcc, float yAcc, float zAcc)
	{
		/*if(accelUpdate != -1)
		{
			long diff = System.currentTimeMillis() - accelUpdate;
			//ball.updateVelocity(xAcc * diff, yAcc * diff);
		}
		
		accelUpdate = System.currentTimeMillis();*/
	}
}
