package com.wuchangi.dailymood.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wuchangi.dailymood.constants.Constants;
import com.wuchangi.dailymood.constants.MoodTable;

/**
 * Created by WuchangI on 2018/12/21.
 */

public class MoodDatabaseHelper extends SQLiteOpenHelper {

    public MoodDatabaseHelper(Context context) {
        super(context, Constants.MoodDatabaseWithFullName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MoodTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
