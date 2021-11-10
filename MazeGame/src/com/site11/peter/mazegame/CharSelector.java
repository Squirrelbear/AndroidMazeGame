package com.site11.peter.mazegame;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class CharSelector {
	public static String[] CHARS = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
									"K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
									"U", "V", "W", "X", "Y", "Z" };
	
	private float textPosX, textPosY;
	private float minSelX, maxSelX, topY, bottomY, curSelX;
	private float minSelY, maxSelY, leftX, rightX, curSelY;
	private RectF arrowBottomRect, arrowTopRect;
	
	private int curChar;
	private boolean hasFocus;
	
	private Bitmap arrowTop, arrowBottom;
	
	public CharSelector(float x, float y, Bitmap arrowTop, Bitmap arrowBottom)
	{
		this.arrowTop = arrowTop;
		this.arrowBottom = arrowBottom;
		curChar = 0;
		hasFocus = false;
		
		// bars
		minSelX = x - 60; // -20
		maxSelX = x + 100; //200 + 20;
		topY = y - 125 - 60;//15 - 40;
		bottomY = y + 10; //+ 40;//+ 200 + 20;
		
		// balls
		leftX = x - 30 - 20;
		rightX = x + 170;// 200 + 20;
		minSelY = y - 125 - 10; //y - 10;
		maxSelY = y - 10; //y + 200 - 10 - 20;
		
		textPosX = x;
		textPosY = y;
		
		curSelX = (minSelX + maxSelX) / 2;
		curSelY = (minSelY + maxSelY) / 2;
		
		arrowTopRect = new RectF(curSelX, topY, curSelX + 80, topY + 40);
		arrowBottomRect = new RectF(curSelX, bottomY, curSelX + 80, bottomY + 40);
	}
	
	public void draw(Canvas canvas)
	{
		if(hasFocus)
		{
			Paint paint = new Paint();
			paint.setColor(Color.GRAY);
			canvas.drawBitmap(arrowTop, null, arrowTopRect, null);
			canvas.drawBitmap(arrowBottom, null, arrowBottomRect, null);
			//canvas.drawRect(new RectF(curSelX, topY, curSelX + 80, topY + 40), paint);
			//canvas.drawRect(new RectF(curSelX, bottomY, curSelX + 80, bottomY + 40), paint);
			canvas.drawCircle(leftX, curSelY, 10, paint);
			canvas.drawCircle(rightX, curSelY, 10, paint);
		}
		
		Paint paintText = new Paint(); 
		paintText.setColor(Color.BLACK); 
		paintText.setTextSize(200); 
		canvas.drawText(CHARS[curChar], textPosX, textPosY, paintText); 
	}
	
	public void previous()
	{
		curChar--;
		if(curChar < 0)
			curChar = CHARS.length - 1;
	}
	
	public void next()
	{
		curChar++;
		if(curChar >= CHARS.length)
			curChar = 0;
	}
	
	public void setSelCurX(float percent)
	{
		if(percent > 1)
			percent = 1;
		else if(percent < 0)
			percent = 0;
		
		curSelX = minSelX + (maxSelX - minSelX) * percent;
		arrowTopRect = new RectF(curSelX, topY, curSelX + 80, topY + 40);
		arrowBottomRect = new RectF(curSelX, bottomY, curSelX + 80, bottomY + 40);
	}
	
	public void setSelCurY(float percent)
	{
		if(percent > 1)
			percent = 1;
		else if(percent < 0)
			percent = 0;
		
		curSelY = minSelY + (maxSelY - minSelY) * percent;
	}
	
	public void setFocus(boolean hasFocus)
	{
		if(!this.hasFocus)
		{
			curSelX = (minSelX + maxSelX) / 2;
			curSelY = (minSelY + maxSelY) / 2;
		}
		this.hasFocus = hasFocus;
	}
	
	public String getChar()
	{
		return CHARS[curChar];
	}
}
