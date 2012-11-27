package com.example.cryptprototype.messages;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessageDatabase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "messages.db";
	private static final int DATABASE_VERSION = 2;

	public MessageDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + MessagesContract.TABLE_NAME + "(" +
				MessagesContract.Cols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				MessagesContract.Cols.SENDER + " TEXT NOT NULL, " +
				MessagesContract.Cols.RECEIVER + " TEXT NOT NULL, " +
				MessagesContract.Cols.DATE + " TEXT, " +
				MessagesContract.Cols.TEXT + " TEXT, " +
				MessagesContract.Cols.ENCRYPTED + " INTEGER, " +
				MessagesContract.Cols.ENCTYPE + " INTEGER, " +
				"UNIQUE (" + MessagesContract.Cols._ID + ") ON CONFLICT REPLACE )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) {
			db.execSQL("DROP IF TABLE EXISTS" + MessagesContract.TABLE_NAME);
			onCreate(db);
		}
	}
}