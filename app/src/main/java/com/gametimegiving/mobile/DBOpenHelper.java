package com.gametimegiving.mobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBOpenHelper extends SQLiteOpenHelper {
    //Constants for identifying table and columns
    public static final String TABLE_GAME = "game";
    public static final String ID = "_id";
    public static final String GAME_GTG_ID = "gtg_game_id";
    public static final String HOME_ID = "hometeam_id";
    public static final String AWAY_ID = "awayteam_id";
    public static final String HOME_SCORE = "hometeam_score";
    public static final String AWAY_SCORE = "awayteam_score";
    public static final String HOME_PLEDGE = "hometeam_pledge";
    public static final String AWAY_PLEDGE = "awayteam_pledge";
    public static final String CLOCK = "clocktime";
    public static final String PERIOD = "period";
    public static final String GAME_CREATED = "date_created";
    public static final String LASTUPDATED = "lastupdate_date";
    public static final String[] ALL_GAME_TABLE_COLUMNS = {ID, GAME_GTG_ID, HOME_ID, AWAY_ID, HOME_SCORE, AWAY_SCORE,
            HOME_PLEDGE, AWAY_PLEDGE, CLOCK, PERIOD,};
    // Constants to create the plede table
    public static final String TABLE_PLEDGE = "pledge";
    public static final String PLEDGE_ID = "_id";
    public static final String PLEDGE_GTG_ID = "gtg_pledge_id";
    public static final String USER_ID = "user_id";
    public static final String GAME_ID = "game_id";
    public static final String TEAM_ID = "team_id";
    public static final String PLEDGE_AMT = "pledge_amt";
    public static final String PLEDGE_TS = "date_created";
    // Constants to create the team table
    public static final String TABLE_TEAM = "team";
    public static final String TID = "_id";
    public static final String TEAM_GTG_ID = "gtg_team_id";
    public static final String SPORT_TYPE = "sport_type";
    public static final String TEAM_NAME = "name";
    public static final String PHOTO = "photo";
    public static final String CREATION_DATE = "date_created";
    // Constants to create the charity table
    public static final String TABLE_CHARITY = "charity";
    public static final String CID = "_id";
    public static final String CHARITY_GTG_ID = "gtg_charity_id";
    public static final String CHARITY_NAME = "name";
    public static final String DETAIL = "detail";
    public static final String MISSION = "mission";
    public static final String CHARITY_PHOTO = "charity_photo";
    public static final String PURPOSE = "purpose";
    public static final String VIDEO = "video";
    public static final String CONTACT_EMAIL = "contact_email";
    public static final String CONTACT_PHONE = "contact_phone";
    public static final String CREATE_DATE = "date_created";
    //Constants for db name and version
    private static final String DATABASE_NAME = "gtg.db";
    private static final int DATABASE_VERSION = 1;
    //SQL to create Game table
    private static final String CREATE_GAME_TABLE =
            "CREATE TABLE " + TABLE_GAME + " (" +
                    GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GAME_GTG_ID + " INTEGER, " +
                    HOME_ID + " INTEGER, " +
                    AWAY_ID + " INTEGER, " +
                    HOME_SCORE + " INTEGER, " +
                    AWAY_SCORE + " INTEGER, " +
                    HOME_PLEDGE + " INTEGER, " +
                    AWAY_PLEDGE + " INTEGER, " +
                    CLOCK + " INTEGER, " +
                    PERIOD + " INTEGER, " +
                    LASTUPDATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";
    //SQL to create Pledge table
    private static final String CREATE_PLEDGE_TABLE =
            "CREATE TABLE " + TABLE_PLEDGE + " (" +
                    PLEDGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PLEDGE_GTG_ID + " INTEGER, " +
                    USER_ID + " INTEGER, " +
                    GAME_ID + " INTEGER, " +
                    PLEDGE_AMT + " INTEGER, " +
                    PLEDGE_TS + " TEXT default CURRENT_TIMESTAMP" +
                    ")";
    //SQL to create Team table
    private static final String CREATE_TEAM_TABLE =
            "CREATE TABLE " + TABLE_TEAM + " (" +
                    PLEDGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PLEDGE_GTG_ID + " INTEGER, " +
                    USER_ID + " INTEGER, " +
                    GAME_ID + " INTEGER, " +
                    PLEDGE_AMT + " INTEGER, " +
                    PLEDGE_TS + " TEXT default CURRENT_TIMESTAMP" +
                    ")";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_GAME_TABLE);
        db.execSQL(CREATE_PLEDGE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLEDGE);
        Log.w(DBOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        onCreate(db);
    }
}
