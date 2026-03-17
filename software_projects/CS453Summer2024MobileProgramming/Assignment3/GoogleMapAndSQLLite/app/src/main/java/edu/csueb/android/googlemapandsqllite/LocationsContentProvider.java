package edu.csueb.android.googlemapandsqllite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class LocationsContentProvider extends ContentProvider {
    public static final String AUTHORITY = "edu.csueb.android.googlemapandsqlite.locations";
    private static final String BASE_PATH = "locations";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int LOCATIONS = 1;
    private static final int LOCATION_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, LOCATIONS);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", LOCATION_ID);
    }

    private LocationsDB database;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        database = new LocationsDB(context);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(LocationsDB.TABLE_NAME);

        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case LOCATIONS:
                break;
            case LOCATION_ID:
                queryBuilder.appendWhere(LocationsDB.FIELD_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id;
        switch (uriType) {
            case LOCATIONS:
                id = sqlDB.insert(LocationsDB.TABLE_NAME, null, values);
                if (id > 0) {
                    Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
                    getContext().getContentResolver().notifyChange(newUri, null);
                    return newUri;
                }
                break;
            default:
                throw new SQLException("Failed to insert row into " + uri);
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted;
        switch (uriType) {
            case LOCATIONS:
                rowsDeleted = sqlDB.delete(LocationsDB.TABLE_NAME, selection, selectionArgs);
                break;
            case LOCATION_ID:
                String id = uri.getLastPathSegment();
                if (selection == null) {
                    rowsDeleted = sqlDB.delete(LocationsDB.TABLE_NAME, LocationsDB.FIELD_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(LocationsDB.TABLE_NAME, LocationsDB.FIELD_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
