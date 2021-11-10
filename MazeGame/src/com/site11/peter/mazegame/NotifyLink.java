package com.site11.peter.mazegame;

public class NotifyLink {
	public int x, y, id;
	
	public NotifyLink(int x, int y, int id)
	{
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	public boolean isLocation(int x, int y)
	{
		return this.x == x && this.y == y;
	}
}
