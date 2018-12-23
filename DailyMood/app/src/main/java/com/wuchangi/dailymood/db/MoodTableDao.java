package com.wuchangi.dailymood.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wuchangi.dailymood.bean.Mood;
import com.wuchangi.dailymood.constants.MoodTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuchangI on 2018/12/21.
 */


public class MoodTableDao {

    private MoodDatabaseHelper mDbHelper;

    public MoodTableDao(Context context){
        mDbHelper = new MoodDatabaseHelper(context);
    }

    public List<Mood> listMoods(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql = "SELECT * FROM " + MoodTable.TABLE_NAME + ";";
        Cursor cursor = db.rawQuery(sql, null);

        List<Mood> moodList = new ArrayList<>();
        Mood mood = null;

        while(cursor.moveToNext()){
             mood = new Mood();
             mood.setDate(cursor.getString(cursor.getColumnIndex(MoodTable.COL_DATE)));
             mood.setDescription(cursor.getString(cursor.getColumnIndex(MoodTable.COL_DESCRIPTION)));
             mood.setPicturePath(cursor.getString(cursor.getColumnIndex(MoodTable.COL_PICTURE_PATH)));

             moodList.add(mood);
        }

        cursor.close();

        return moodList;
    }

    public void addMood(Mood mood) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoodTable.COL_DATE, mood.getDate());
        values.put(MoodTable.COL_DESCRIPTION, mood.getDescription());
        values.put(MoodTable.COL_PICTURE_PATH, mood.getPicturePath());

        db.replace(MoodTable.TABLE_NAME, null, values);
    }

    public void updateMood(String date, Mood mood){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MoodTable.COL_DATE, mood.getDate());
        values.put(MoodTable.COL_DESCRIPTION, mood.getDescription());
        values.put(MoodTable.COL_PICTURE_PATH, mood.getPicturePath());

        db.update(MoodTable.TABLE_NAME, values, MoodTable.COL_DATE + "=?", new String[]{date});
    }

    public void removeAllMoods(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(MoodTable.TABLE_NAME, null, null);
    }

    public Mood findMoodByDate(String date){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql = "SELECT * FROM " + MoodTable.TABLE_NAME
                + " WHERE " + MoodTable.COL_DATE + "=?;";

        Cursor cursor = db.rawQuery(sql, new String[]{date});

        Mood mood = null;

        if (cursor.moveToNext()){
            mood = new Mood();
            mood.setDate(cursor.getString(cursor.getColumnIndex(MoodTable.COL_DATE)));
            mood.setDescription(cursor.getString(cursor.getColumnIndex(MoodTable.COL_DESCRIPTION)));
            mood.setPicturePath(cursor.getString(cursor.getColumnIndex(MoodTable.COL_PICTURE_PATH)));
        }

        cursor.close();

        return mood;
    }


    public void deleteMoodByDate(String date) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(MoodTable.TABLE_NAME, MoodTable.COL_DATE + "=?", new String[]{date});
    }
}
