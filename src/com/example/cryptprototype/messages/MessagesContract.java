package com.example.cryptprototype.messages;

import android.net.Uri;

public final class MessagesContract {
	public static final String TABLE_NAME = "messages";

	/** Specifies Uri information for queries to the messages database **/
	public static final class UriInfo {
		public static final String CONTENT_TYPE_DIR = "vnd.android.cursor.dir/vnd.cryptprototype.app";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.cryptprototype.app";
		public static final String AUTHORITY = "com.example.cryptprototype.messages";
		public static final String PATH = "messages";
		public static final int ALL_MESSAGES = 1;
		public static final int SPECIFIC_MESSAGE = 2;
		private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);
		public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH).build();
	}

	/** Specifies column names for the messages database **/
	public static final class Cols {
		public static final String _ID = "_id";
		public static final String SENDER = "sender";
		public static final String RECEIVER = "receiver";
		public static final String DATE = "date";
		public static final String TEXT = "text";
		public static final String ENCRYPTED = "encrypted";
		public static final String ENCTYPE = "enc_type";
	}
}