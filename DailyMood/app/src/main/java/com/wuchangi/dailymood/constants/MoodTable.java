package com.wuchangi.dailymood.constants;

/**
 * Created by WuchangI on 2018/12/21.
 */


public class MoodTable {

    public static final String TABLE_NAME = "t_mood";

    public static final String COL_DATE = "date";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_PICTURE_PATH = "picture_path";

    public static final String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + " ("
            + COL_DATE + " TEXT PRIMARY KEY, "
            + COL_DESCRIPTION + " TEXT, "
            + COL_PICTURE_PATH + " TEXT);";
}
