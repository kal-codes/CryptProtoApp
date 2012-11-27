package com.example.cryptprototype;

import com.example.cryptprototype.messages.DateFormatter;
import com.example.cryptprototype.messages.MessagesContract;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

@TargetApi(13)
public class MessageListAdapter extends SimpleCursorAdapter implements Filterable {
	public static final int layout = R.layout.message_list_item;
	public static final String[] in_from = new String[] { MessagesContract.Cols.SENDER, MessagesContract.Cols.DATE };
	public static final String[] out_from = new String[] { MessagesContract.Cols.RECEIVER, MessagesContract.Cols.DATE };
	public static final int[] to = new int[] { R.id.Name, R.id.Date };
	public static final String user = "User";

	private Context context;
	private boolean inbox; // true = inbox, false = sent messages

	public MessageListAdapter(Context context, Cursor c, String[] from, int flags, boolean inbox) {
		super(context, layout, c, from, to, flags);
		this.context = context;
		this.inbox = inbox;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		//Cursor c = getCursor();
		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(layout, parent, false);

		String name = "";
		if (inbox) // if looking in the inbox
			name = cursor.getString(cursor.getColumnIndex(MessagesContract.Cols.SENDER)); // set display name to sender
		else // if looking in sent messages
			name = cursor.getString(cursor.getColumnIndex(MessagesContract.Cols.RECEIVER));
		// Next set the name of the entry.
		TextView name_text = (TextView) v.findViewById(R.id.Name);
		if (name_text != null)
			name_text.setText(name);

		String full_date = cursor.getString(cursor.getColumnIndex(MessagesContract.Cols.DATE));
		String date = (DateFormatter.getStandardDate(full_date) + ", " + DateFormatter.getStandardTime(full_date));
		TextView date_text = (TextView) v.findViewById(R.id.Date);
		if (date_text != null)
			date_text.setText(date);
		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor cursor) {
		String name = "";
		if (inbox) // if looking in the inbox
			name = cursor.getString(cursor.getColumnIndex(MessagesContract.Cols.SENDER)); // set display name to sender
		else // if looking in sent messages
			name = cursor.getString(cursor.getColumnIndex(MessagesContract.Cols.RECEIVER));
		// Next set the name of the entry.
		TextView name_text = (TextView) v.findViewById(R.id.Name);
		if (name_text != null)
			name_text.setText(name);

		String full_date = cursor.getString(cursor.getColumnIndex(MessagesContract.Cols.DATE));
		String date = (DateFormatter.getStandardDate(full_date) + ", " + DateFormatter.getStandardTime(full_date));
		TextView date_text = (TextView) v.findViewById(R.id.Date);
		if (date_text != null)
			date_text.setText(date);
	}

	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		if (getFilterQueryProvider() != null) { return getFilterQueryProvider().runQuery(constraint); }
		StringBuilder buffer = null;
		String[] args = null;
		if (constraint != null) {
			buffer = new StringBuilder();
			buffer.append("UPPER(");
			buffer.append(MessagesContract.Cols._ID);
			buffer.append(") GLOB ?");
			args = new String[] { constraint.toString().toUpperCase() + "*" };
		}
		return context.getContentResolver().query(MessagesContract.UriInfo.CONTENT_URI, null, buffer == null ? null : buffer.toString(), args, MessagesContract.Cols._ID + " ASC");
	}
}