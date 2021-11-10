package com.site11.peter.mazegame;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "ScoresManager";
	private static final String TABLE_SCORES = "scores";
	
	private static final String KEY_ID = "id";
	private static final String KEY_SCORE = "score";
	private static final String KEY_NAME = "name";
	private static final String KEY_MAP = "map";
	
	public DatabaseHandler(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		String CREATE_SCORES_TABLE = "CREATE TABLE " + TABLE_SCORES + "("
						+ KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_MAP + " INTEGER, " 
						+ KEY_NAME + " TEXT," + KEY_SCORE + " TEXT" + ")";
		Log.d("DATABASE", CREATE_SCORES_TABLE);
		db.execSQL(CREATE_SCORES_TABLE);
		
		/*
		String insert = "INSERT INTO " + TABLE_SCORES + " (" + KEY_MAP + ", " + KEY_NAME + ", " + KEY_SCORE
				 			+ ") VALUES (0, 'ABC', '1.4')";
		db.execSQL(insert);
		
		 insert = "INSERT INTO " + TABLE_SCORES + " (" + KEY_MAP + ", " + KEY_NAME + ", " + KEY_SCORE
	 			+ ") VALUES (0, 'ABC', '1.4')";
db.execSQL(insert);
 insert = "INSERT INTO " + TABLE_SCORES + " (" + KEY_MAP + ", " + KEY_NAME + ", " + KEY_SCORE
	+ ") VALUES (0, 'ABC', '1.4')";
db.execSQL(insert);*/
		
		//addScore(new HighScore(0,1.4f,"TES"));
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		// drop older table if it exists
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
		
		onCreate(db);
	}
	
	
	public void addScore(HighScore score)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_MAP, score.getMap());
		values.put(KEY_NAME, score.getName());
		values.put(KEY_SCORE, score.getScore() + "");
		
		try{
			db.beginTransaction();
			db.insertOrThrow(TABLE_SCORES, null, values);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
		db.close();
	}
	
	public List<HighScore> getScores(int mapID)
	{
		List<HighScore> scoreList = new ArrayList<HighScore>();
		String selectQuery = "SELECT * FROM " + TABLE_SCORES + " WHERE " + KEY_MAP + " = ?";
		
		SQLiteDatabase db = this.getReadableDatabase();
		/*if(!db.isOpen())
		{
			Log.d("DATABASE", "DB is not open????");
		}*/
		Cursor cursor = db.rawQuery(selectQuery, new String [] { String.valueOf(mapID) } );
		//Log.d("DATABASE", "CursorCount: " + cursor.getCount());
		
		if(cursor.moveToFirst()) {
			do {
				HighScore score = new HighScore();
				score.setID(Integer.parseInt(cursor.getString(0)));
				score.setMap(Integer.parseInt(cursor.getString(1)));
				score.setName(cursor.getString(2));
				score.setScore(Float.parseFloat(cursor.getString(3)));
				scoreList.add(score);
			} while(cursor.moveToNext());
		}
		db.close();
		return scoreList;
	}
	
	public void deleteScore(int scoreID)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SCORES, KEY_ID + " = ?", new String[] { String.valueOf(scoreID) });
		db.close();
	}
	
	public void clearDatabase()
	{
		Log.d("DATABASE", "Clearing Database");
		SQLiteDatabase db = this.getWritableDatabase();
		onUpgrade(db, 1, 1);
		db.close();
	}
}
