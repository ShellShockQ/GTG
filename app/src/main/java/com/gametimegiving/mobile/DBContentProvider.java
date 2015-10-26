package com.gametimegiving.mobile;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DBContentProvider extends ContentProvider {
    public static final String CONTENT_ITEM_TYPE = "Note";
    private static final String TAG = "DBContentProvider";
    private static final String AUTHORITY = "com.gametimegiving.mobile.dbcontentprovider";
    private static final String BASE_PATH = "gtg";

    // Constant to identify the requested operation
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final int NOTES = 1;
    private static final int NOTES_ID = 2;
    private static final UriMatcher _uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        _uriMatcher.addURI(AUTHORITY, BASE_PATH, NOTES);
        _uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", NOTES_ID);

    }

    private SQLiteDatabase _database;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        _database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (_uriMatcher.match(uri) == NOTES_ID) {
            selection = DBOpenHelper.GAME_ID + "=" + uri.getLastPathSegment();
        }
        return _database.query(DBOpenHelper.TABLE_GAME,
                DBOpenHelper.ALL_GAME_TABLE_COLUMNS,
                selection,
                null,
                null,
                null,
                DBOpenHelper.LASTUPDATED + " DESC");
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = _database.insert(DBOpenHelper.TABLE_GAME, null, values);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return _database.delete(DBOpenHelper.TABLE_GAME, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return _database.update(DBOpenHelper.TABLE_GAME, values, selection, selectionArgs);
    }
}
