package com.learn.popularmovies.database;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by E01090 on 1/9/2016.
 */
public class PopularMovieContentProvider extends ContentProvider {

    private final String TAG = getClass().getSimpleName();

    private static final int MOVIE_LIST = 1;
    private static final int MOVIE_ID = 2;

    private static final UriMatcher URI_MATCHER;

    private PopularMoviesOpenHelper mHelper = null;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(PopularMovieContract.AUTHORITY, PopularMovieContract.PATH_MOVIE, MOVIE_LIST);
        URI_MATCHER.addURI(PopularMovieContract.AUTHORITY, PopularMovieContract.PATH_MOVIE+"/#", MOVIE_ID);
    }

    @Override
    public boolean onCreate() {
        mHelper = new PopularMoviesOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        boolean useAuthorityUri = false;
        switch (URI_MATCHER.match(uri)) {
            case MOVIE_LIST:
                builder.setTables(PopularMovieContract.Movies.TABLE_MOVIES);
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = PopularMovieContract.Movies.SORT_ORDER_DEFAULT;
                }
                break;
            case MOVIE_ID:
                builder.setTables(PopularMovieContract.Movies.TABLE_MOVIES);
                // limit query to one row at most:
                builder.appendWhere(PopularMovieContract.Movies._ID + " = "
                        + uri.getLastPathSegment());
                break;

            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        // if you like you can log the query
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            logQuery(builder,  projection, selection, sortOrder);
        }
        else {
            logQueryDeprecated(builder, projection, selection, sortOrder);
        }
        Cursor cursor = null;
        try {
            cursor = builder.query(db, projection, selection, selectionArgs,
                    null, null, sortOrder);
        }catch (SQLException e){
            e.printStackTrace();
        }
        // if we want to be notified of any changes:
        if(cursor!=null){
            if (useAuthorityUri) {
                cursor.setNotificationUri(getContext().getContentResolver(), PopularMovieContract.Movies.CONTENT_URI);
            }
            else {
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
            }
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case MOVIE_LIST:
                return PopularMovieContract.Movies.CONTENT_DIR_TYPE;
            case MOVIE_ID:
                return PopularMovieContract.Movies.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (URI_MATCHER.match(uri) != MOVIE_LIST) {
            throw new IllegalArgumentException(
                    "Unsupported URI for insertion: " + uri);
        }
        try {
            SQLiteDatabase db = mHelper.getWritableDatabase();
            long id = db.insert(PopularMovieContract.Movies.TABLE_MOVIES, null, values);
            return getUriForId(id, uri);
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    private Uri getUriForId(long id, Uri uri) {
        if (id > 0) {
            Uri itemUri = ContentUris.withAppendedId(uri, id);
                // notify all listeners of changes and return itemUri:
                getContext().
                        getContentResolver().
                        notifyChange(itemUri, null);
            return itemUri;
        }
        throw new SQLException("Problem while inserting into uri: " + uri);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int delCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case MOVIE_ID:
                String idStr = uri.getLastPathSegment();
                String where = PopularMovieContract.Movies.MOVIE_ID + " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                delCount = db.delete(PopularMovieContract.Movies.TABLE_MOVIES, where, selectionArgs);
                break;
            default:
                // no support for deleting photos or entities -
                // photos are deleted by a trigger when the item is deleted
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        // notify all listeners of changes:
        if (delCount > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return delCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int updateCount = 0;
        switch (URI_MATCHER.match(uri)) {
            case MOVIE_LIST:
                updateCount = db.update(PopularMovieContract.Movies.TABLE_MOVIES, values, selection,
                        selectionArgs);
                break;
            case MOVIE_ID:
                String idStr = uri.getLastPathSegment();
                String where = PopularMovieContract.Movies.MOVIE_ID+ " = " + idStr;
                if (!TextUtils.isEmpty(selection)) {
                    where += " AND " + selection;
                }
                updateCount = db.update(PopularMovieContract.Movies.TABLE_MOVIES, values, where,
                        selectionArgs);
                break;
            default:
                // no support for updating photos!
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        // notify all listeners of changes:
        if (updateCount > 0 ) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return updateCount;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void logQuery(SQLiteQueryBuilder builder, String[] projection, String selection, String sortOrder) {
            Log.v(TAG, "query: " + builder.buildQuery(projection, selection, null, null, sortOrder, null));
    }

    @SuppressWarnings("deprecation")
    private void logQueryDeprecated(SQLiteQueryBuilder builder, String[] projection, String selection, String sortOrder) {
            Log.v(TAG, "query: " + builder.buildQuery(projection, selection, null, null, null, sortOrder, null));
    }
}
