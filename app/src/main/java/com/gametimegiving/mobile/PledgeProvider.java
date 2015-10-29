package com.gametimegiving.mobile;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class PledgeProvider extends ContentProvider {
    public static final int PLEDGES = 1;
    public static final int PLEDGE_ID = 2;
    public static final String CONTENT_ITEM_TYPE = "Note";
    private static final String TAG = "PledgeProvider";
    private static final String AUTHORITY = "com.gametimegiving.mobile.pledgeprovider";
    private static final String BASE_PATH = "pledge";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher _uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        _uriMatcher.addURI(AUTHORITY, BASE_PATH, PLEDGES);
        _uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PLEDGE_ID);

    }

    private SQLiteDatabase _database;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        _database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = _database.insert(DBOpenHelper.TABLE_PLEDGE, null, values);
        return Uri.parse(BASE_PATH + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
