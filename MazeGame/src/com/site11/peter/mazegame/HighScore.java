package com.site11.peter.mazegame;

public class HighScore {
	private int id;
	private float score;
	private String name;
	private int map;
	
	public HighScore()
	{
	}
	
	public HighScore(int id, int map, float score, String name)
	{
		this.id = id;
		this.score = score;
		this.name = name;
		this.map = map;
	}
	
	public HighScore(int map, float score, String name)
	{
		this.map = map;
		this.score = score;
		this.name = name;
	}
	
	public int getID()
	{
		return id;
	}
	
	public void setID(int id)
	{
		this.id = id;
	}
	
	public float getScore()
	{
		return score;
	}
	
	public void setScore(float score)
	{
		this.score = score;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}

	public int getMap()
	{
		return map;
	}
	
	public void setMap(int map) {
		this.map = map;
	}
}
