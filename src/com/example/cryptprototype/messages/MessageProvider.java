package com.example.cryptprototype.messages;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class MessageProvider extends ContentProvider {
	private static MessageDatabase messageDb;
	private static UriMatcher sUriMatcher;
	private static Context context;

	public void clear() {
		SQLiteDatabase db = messageDb.getWritableDatabase();
		db.execSQL("DELETE FROM " + MessagesContract.TABLE_NAME);
		context.getContentResolver().notifyChange(MessagesContract.UriInfo.CONTENT_URI, null);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int result;
		switch(sUriMatcher.match(uri)) {
			case MessagesContract.UriInfo.ALL_MESSAGES:
				result = messageDb.getWritableDatabase().delete(MessagesContract.TABLE_NAME, selection, selectionArgs);
				context.getContentResolver().notifyChange(uri, null);
				return result;
			case MessagesContract.UriInfo.SPECIFIC_MESSAGE:
				String empID = uri.getLastPathSegment();
				result = messageDb.getWritableDatabase().delete(MessagesContract.TABLE_NAME, MessagesContract.Cols._ID + " = ?", new String[] {empID});
				context.getContentResolver().notifyChange(uri, null);
				return result;
			default:
				throw new UnsupportedOperationException("URI: " + uri + " not supported.");
		}
	}

	@Override
	public String getType(Uri uri) {
		switch (sUriMatcher.match(uri)) {
    		case MessagesContract.UriInfo.ALL_MESSAGES:
    			return MessagesContract.UriInfo.CONTENT_TYPE_DIR;
    		case MessagesContract.UriInfo.SPECIFIC_MESSAGE:
    			return MessagesContract.UriInfo.CONTENT_ITEM_TYPE;
    		default:
    			throw new UnsupportedOperationException ("URI " + uri + " is not supported.");
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		switch(sUriMatcher.match(uri)){
			case MessagesContract.UriInfo.ALL_MESSAGES:
				SQLiteDatabase db = messageDb.getWritableDatabase();
				long id = db.insert(MessagesContract.TABLE_NAME, null, values);
				// TODO: fix this so that it is notified of change; getContext() returns null
				//getContext().getContentResolver().notifyChange(uri, null);
				context.getContentResolver().notifyChange(uri, null);
				//getContext().getContentResolver().notifyChange(uri, null);
				return MessagesContract.UriInfo.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
            default:
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
		}
	}

	@Override
	public boolean onCreate() {
		context = this.getContext();
		messageDb = new MessageDatabase(context);
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(MessagesContract.UriInfo.AUTHORITY, MessagesContract.UriInfo.PATH, MessagesContract.UriInfo.ALL_MESSAGES);
		sUriMatcher.addURI(MessagesContract.UriInfo.AUTHORITY, MessagesContract.UriInfo.PATH + "/#", MessagesContract.UriInfo.SPECIFIC_MESSAGE);
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		// SQLiteQueryBuilder is a helper class that creates the
        // proper SQL syntax for us.
        SQLiteQueryBuilder qBuilder = new SQLiteQueryBuilder();

        // Set the table we're querying.
        qBuilder.setTables(MessagesContract.TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
        	case MessagesContract.UriInfo.ALL_MESSAGES:
        		break;
        	case MessagesContract.UriInfo.SPECIFIC_MESSAGE:
        		// If the query ends in a specific record number, we're
                // being asked for a specific record, so set the
                // WHERE clause in our query.
        		qBuilder.appendWhere(MessagesContract.Cols._ID + " = " + uri.getLastPathSegment());
        		break;
        	default:
				throw new UnsupportedOperationException("URI: " + uri + " not supported.");
        }

        // Make the query.
        Cursor c = qBuilder.query(messageDb.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        // TODO: c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int rows = 0;
		switch(sUriMatcher.match(uri)) {
			case MessagesContract.UriInfo.SPECIFIC_MESSAGE:
				if(values != null) {
					String empID = uri.getLastPathSegment();
					rows = messageDb.getWritableDatabase().update(MessagesContract.TABLE_NAME, values, MessagesContract.Cols._ID + " = ?", new String[] {empID});
					context.getContentResolver().notifyChange(uri, null);
				}
				return rows;
			default:
                throw new UnsupportedOperationException("URI: " + uri + " not supported.");
		}
	}
}