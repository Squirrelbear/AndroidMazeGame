package com.site11.peter.mazegame;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class MenuButton {
	protected RectF rect;
	protected String text;
	protected int actionID;
	
	public MenuButton(RectF rect, String text, int actionID)
	{
		this.rect = rect;
		this.text = text;
		this.actionID = actionID;
	}
	
	public void draw(Canvas canvas)
	{
		Paint paint = new Paint();
		paint.setColor(Color.GRAY);
		canvas.drawRect(rect, paint);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rect, paint);
		
		Paint paintText = new Paint(); 
		paintText.setColor(Color.BLACK); 
		paintText.setTextSize(50); 
		canvas.drawText(text, rect.left + 30, rect.top + 60, paintText); 
	}
	
	public boolean isPointInButton(float x, float y)
	{
		return rect.contains(x, y);
	}
	
	public int getActionID()
	{
		return actionID;
	}
}
