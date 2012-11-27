package com.example.cryptprototype;

import java.util.Date;
import com.example.cryptprototype.webservlet.CommonUtilities;
import com.example.cryptprototype.messages.DateFormatter;
import com.example.cryptprototype.messages.MessageCryptor;
import com.example.cryptprototype.messages.MessageProvider;
import com.example.cryptprototype.messages.MessagesContract;
import android.os.Bundle;
import android.annotation.TargetApi;
import com.google.android.gcm.GCMRegistrar;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.app.ActionBar.Tab;
import android.app.ListFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

@TargetApi(13)
public class CryptPrototype extends Activity {
	public static final int white = Color.rgb(255, 255, 255); // the color white
	public static final int enc_color = Color.rgb(0, 0, 255); // color for button when encrypted
	public static final int unenc_color = Color.rgb(0, 255, 0); // color for button when unencrypted

	public static int enctype;
	private static Context context;
	private static MessageProvider messages;
	private static DialogBox dialog_box;

	/** Used to implement the Messages tab functionality **/
	public static class MessagesFragment extends Fragment {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			/** GCM Register **/
			GCMRegistrar.checkDevice(context);
			GCMRegistrar.checkManifest(context);
			final String regId = GCMRegistrar.getRegistrationId(context);
			if (regId.equals("")) {
				GCMRegistrar.register(context, "458779572003"); //SENDER_ID = #project id
				Toast.makeText(context, regId , Toast.LENGTH_LONG).show();
			} else {
				Log.v(CommonUtilities.TAG, "Already registered");
				//Toast.makeText(context, "Already registered" , Toast.LENGTH_LONG).show();
			}
			
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			View v = inflater.inflate(R.layout.messages_fragment, container, false);
			v.setBackgroundColor(Color.rgb(0, 0, 0));
			Button inButton = (Button) v.findViewById(R.id.inboxButton);
			Button sentButton = (Button) v.findViewById(R.id.sentButton);
			inButton.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	// go to InboxFragment
			    	FragmentManager fragmentManager = getFragmentManager();
			    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			    	InboxFragment fragment = new InboxFragment();
			    	fragmentTransaction.add(android.R.id.content, fragment);
			    	fragmentTransaction.commit();
			    }
			});
			sentButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					// go to SentFragment
					FragmentManager fragmentManager = getFragmentManager();
			    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			    	SentFragment fragment = new SentFragment();
			    	fragmentTransaction.add(android.R.id.content, fragment);
			    	fragmentTransaction.commit();
				}
			});

	        return v;
	    }

		@Override
		public void onPause() {
			super.onPause();
		}

		@Override
		public void onResume() {
			super.onResume();
		}
	}

	/** Used to implement the inbox functionality **/
	public static class InboxFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
		// This is the Adapter being used to display the list's data
		MessageListAdapter mAdapter;

		// These are the columns that we will retrieve
		static final String[] PROJECTION = new String[] { MessagesContract.Cols._ID, MessagesContract.Cols.SENDER, MessagesContract.Cols.DATE };

		// This is the select criteria
		static final String SELECTION = "(" + MessagesContract.Cols.RECEIVER + " = '" + MessageListAdapter.user + "' )";
		//static final String SELECTION = "((" + MessagesContract.Cols.SENDER + " NOTNULL) AND (" + MessagesContract.Cols.SENDER + " != '' ) AND (" + MessagesContract.Cols.SENDER + " != '" + MessageListAdapter.user + "' ))";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			View v = inflater.inflate(android.R.layout.list_content, container, false);
			v.setBackgroundColor(Color.rgb(0, 0, 0));

			loadList();

	        return v;
	    }

		public void loadList() {
			// Create an empty adapter we will use to display the loaded data.
			// We pass null for the cursor, then update it in onLoadFinished()
			mAdapter = new MessageListAdapter(context, null, MessageListAdapter.in_from, 0, true);
			this.setListAdapter(mAdapter);

			// Prepare the loader.  Either re-connect with an existing one, or start a new one.
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// Now create and return a CursorLoader that will take care of
			// creating a Cursor for the data being displayed.
			return new CursorLoader(context, MessagesContract.UriInfo.CONTENT_URI, PROJECTION, SELECTION, null, null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			// Swap the new cursor in.  (The framework will take care of closing the
			// old cursor once we return.)
			mAdapter.swapCursor(data);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			// This is called when the last Cursor provided to onLoadFinished()
			// above is about to be closed.  We need to make sure we are no
			// longer using it.
			mAdapter.swapCursor(null);
		}

		@Override 
		public void onListItemClick(ListView l, View v, int position, long id) {
			// go to MessageFragment when list item is clicked
			Cursor cursor = mAdapter.getCursor();
			cursor.moveToPosition(position);
			String _id = cursor.getString(cursor.getColumnIndex(MessagesContract.Cols._ID));
	    	FragmentManager fragmentManager = getFragmentManager();
	    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    	MessageFragment fragment = new MessageFragment();
	    	Bundle args = new Bundle();
	        args.putString(MessagesContract.Cols._ID, _id);
	    	fragment.setArguments(args);
	    	fragmentTransaction.add(android.R.id.content, fragment);
	    	fragmentTransaction.commit();
		}

		@Override
		public void onPause() {
			mAdapter = null;
			super.onPause();
		}

		@Override
		public void onResume() {
			super.onResume();
			loadList();
		}
	}

	/** Used to implement the sent messages functionality **/
	public static class SentFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
		// This is the Adapter being used to display the list's data
		MessageListAdapter mAdapter;

		// These are the columns that we will retrieve
		static final String[] PROJECTION = new String[] { MessagesContract.Cols._ID, MessagesContract.Cols.RECEIVER, MessagesContract.Cols.DATE };

		// This is the select criteria
		static final String SELECTION = "(" + MessagesContract.Cols.SENDER + " = '" + MessageListAdapter.user + "' )";
		//static final String SELECTION = "((" + MessagesContract.Cols.RECEIVER + " NOTNULL) AND (" + MessagesContract.Cols.RECEIVER + " != '' ) AND (" + MessagesContract.Cols.RECEIVER + " != '" + MessageListAdapter.user + "' ))";

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			View v = inflater.inflate(android.R.layout.list_content, container, false);
			v.setBackgroundColor(Color.rgb(0, 0, 0));

			loadList();

	        return v;
	    }

		public void loadList() {
			// Create an empty adapter we will use to display the loaded data.
			// We pass null for the cursor, then update it in onLoadFinished()
			mAdapter = new MessageListAdapter(context, null, MessageListAdapter.out_from, 0, false);
			this.setListAdapter(mAdapter);

			// Prepare the loader.  Either re-connect with an existing one, or start a new one.
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// Now create and return a CursorLoader that will take care of
			// creating a Cursor for the data being displayed.
			return new CursorLoader(context, MessagesContract.UriInfo.CONTENT_URI, PROJECTION, SELECTION, null, null);
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			// Swap the new cursor in.  (The framework will take care of closing the
			// old cursor once we return.)
			mAdapter.swapCursor(data);
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			// This is called when the last Cursor provided to onLoadFinished()
			// above is about to be closed.  We need to make sure we are no
			// longer using it.
			mAdapter.swapCursor(null);
		}

		@Override 
		public void onListItemClick(ListView l, View v, int position, long id) {
			// go to MessageFragment when list item is clicked
			Cursor cursor = mAdapter.getCursor();
			cursor.moveToPosition(position);
			String _id = cursor.getString(cursor.getColumnIndex(MessagesContract.Cols._ID));
	    	FragmentManager fragmentManager = getFragmentManager();
	    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
	    	MessageFragment fragment = new MessageFragment();
	    	Bundle args = new Bundle();
	        args.putString(MessagesContract.Cols._ID, _id);
	    	fragment.setArguments(args);
	    	fragmentTransaction.add(android.R.id.content, fragment);
	    	fragmentTransaction.commit();
		}

		@Override
		public void onPause() {
			mAdapter = null;
			super.onPause();
		}

		@Override
		public void onResume() {
			super.onResume();
			loadList();
		}
	}

	/** Used to display a message **/
	public static class MessageFragment extends Fragment {
		private String name;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			// Get the cursor for the message
			final String _id = getArguments().getString(MessagesContract.Cols._ID);
			Cursor cursor = messages.query(MessagesContract.UriInfo.CONTENT_URI.buildUpon().appendPath(_id).build(), null, null, null, null);
			//Cursor cursor = messages.query(MessagesContract.UriInfo.CONTENT_URI, null, null, null, null);
			cursor.moveToFirst();

	        // Inflate the layout for this fragment
			View v = inflater.inflate(R.layout.message_fragment, container, false);
			v.setBackgroundColor(Color.rgb(0, 0, 0));

			Button encButton = (Button) v.findViewById(R.id.EncButton);
			if (cursor.getInt(cursor.getColumnIndex(MessagesContract.Cols.ENCRYPTED)) == MessageCryptor.TRUE)
				encButton.setBackgroundColor(enc_color);
			else
				encButton.setBackgroundColor(unenc_color);
			encButton.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	// check if encrypted
			    	if (((ColorDrawable) ((Button) v).getBackground()).getColor() == enc_color) {
			    		// get the corresponding row from the database
			    		Cursor c = messages.query(MessagesContract.UriInfo.CONTENT_URI.buildUpon().appendPath(_id).build(), null, null, null, null);
			    		c.moveToFirst();
			    		String etext = c.getString(c.getColumnIndex(MessagesContract.Cols.TEXT));
			    		int etype = c.getInt(c.getColumnIndex(MessagesContract.Cols.ENCTYPE));
			    		ContentValues values = new ContentValues();
			    		// decrypt the text
			    		values.put(MessagesContract.Cols.TEXT, MessageCryptor.decrypt(etext, etype).toString());
			    		values.put(MessagesContract.Cols.ENCRYPTED, MessageCryptor.FALSE);
			    		values.put(MessagesContract.Cols.ENCTYPE, MessageCryptor.NONE);
			    		// update the row's info (text, encrypted and enc_type)
			    		messages.update(MessagesContract.UriInfo.CONTENT_URI.buildUpon().appendPath(_id).build(), values, null, null);
			    		// change button to unenc_color
			    		((Button) v).setBackgroundColor(unenc_color);
			    		((TextView) ((View) v.getParent()).findViewById(R.id.Text)).setText(MessageCryptor.decrypt(etext, etype).toString());
			    	}
			    }
			});

			name = cursor.getString(cursor.getColumnIndex(MessagesContract.Cols.SENDER));
			if (name.equals(MessageListAdapter.user)) // if looking in the inbox
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

			String msg = cursor.getString(cursor.getColumnIndex(MessagesContract.Cols.TEXT));
			// Next set the name of the entry.
			TextView msg_text = (TextView) v.findViewById(R.id.Text);
			if (msg_text != null)
				msg_text.setText(msg);

			Button replyButton = (Button) v.findViewById(R.id.Reply);
			replyButton.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	// go to ComposeFragment *TAB* when reply button is clicked
			    	FragmentManager fragmentManager = getFragmentManager();
			    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			    	ComposeFragment fragment = new ComposeFragment();
			    	Bundle args = new Bundle();
			        args.putString("Name", name);
			    	fragment.setArguments(args);
			    	fragmentTransaction.add(android.R.id.content, fragment);
			    	fragmentTransaction.commit();
			    }
			});

			Button deleteButton = (Button) v.findViewById(R.id.Delete);
			deleteButton.setOnClickListener(new View.OnClickListener() {
			    public void onClick(View v) {
			    	// go to ComposeFragment *TAB* when reply button is clicked
			    	messages.delete(MessagesContract.UriInfo.CONTENT_URI.buildUpon().appendPath(_id).build(), null, null);
			    	/*FragmentManager fragmentManager = getFragmentManager();
			    	FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			    	MessagesFragment fragment = new MessagesFragment();
			    	Bundle args = new Bundle();
			        args.putString("Name", name);
			    	fragment.setArguments(args);
			    	fragmentTransaction.add(android.R.id.content, fragment);
			    	fragmentTransaction.commit();*/
			    }
			});

	        return v;
	    }

		@Override
		public void onPause() {
			super.onPause();
		}

		@Override
		public void onResume() {
			super.onResume();
		}
	}

	/** Used to implement the Compose tab functionality **/
	public static class ComposeFragment extends Fragment {
		private TextView to;
		private TextView text;

		/*@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}*/

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                             Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			View v = inflater.inflate(R.layout.compose_fragment, container, false);
			v.setBackgroundColor(Color.rgb(0, 0, 0));
			to = (TextView) v.findViewById(R.id.To);
			text = (TextView) v.findViewById(R.id.Text);
			Bundle args = getArguments();
			if (null != args) {
				String name = getArguments().getString("Name");
				if (null != name)
					to.setText(name);
			}
			to.setOnEditorActionListener(new OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (actionId == EditorInfo.IME_ACTION_SEND) {
						// Send the user message
						String to_text = to.getText().toString();
						if (to_text.equals("CLEAR"))
							messages.clear();
						else {
							ContentValues values = new ContentValues();
							values.put(MessagesContract.Cols.SENDER, MessageListAdapter.user);
							values.put(MessagesContract.Cols.RECEIVER, to.getText().toString());
							values.put(MessagesContract.Cols.DATE, new Date().toString());
							values.put(MessagesContract.Cols.TEXT, MessageCryptor.encrypt(text.getText(), enctype).toString());
							if (enctype != MessageCryptor.NONE)
								values.put(MessagesContract.Cols.ENCRYPTED, MessageCryptor.TRUE);
							else
								values.put(MessagesContract.Cols.ENCRYPTED, MessageCryptor.FALSE);
							values.put(MessagesContract.Cols.ENCTYPE, enctype);
			        		messages.insert(MessagesContract.UriInfo.CONTENT_URI, values);
						}
						dialog_box.showMessage("Message sent!");
						to.setText("");
						text.setText("");
						return true;
					}
					return false;
				}
			});
	        return v;
	    }

		@Override
		public void onPause() {
			to.setText("");
			text.setText("");
			super.onPause();
		}
	}

	/** Used to implement the Settings tab functionality **/
	public static class SettingsFragment extends Fragment {
		/*@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}*/

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        // Inflate the layout for this fragment
			View v = inflater.inflate(R.layout.settings_fragment, container, false);
			v.setBackgroundColor(Color.rgb(0, 0, 0));
			Spinner enctypespinner = (Spinner) v.findViewById(R.id.enctype_spinner);
			// Create an ArrayAdapter using the string array and a default spinner layout
			ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context, R.array.enctype_array, android.R.layout.simple_spinner_item);
			// Specify the layout to use when the list of choices appears
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// Apply the adapter to the spinner
			enctypespinner.setAdapter(adapter);
			enctypespinner.setBackgroundColor(white);
			enctypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
			        // Set the enctype
			        String text = (String) parent.getItemAtPosition(pos);
			        if (text.equals("Type 1"))
			        	enctype = MessageCryptor.TYPE1;
			        else if (text.equals("Rijndael"))
			        	enctype = MessageCryptor.RIJNDAEL;
			        else
			        	enctype = MessageCryptor.NONE;
			    }

				public void onNothingSelected(AdapterView<?> parent) {
			        // Another interface callback
			    }
			});
	        return v;
	    }

		/*@Override
		public void onPause() {
			super.onPause();
		}*/
	}

	@TargetApi(13)
	public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
	    private Fragment mFragment;
	    private final Activity mActivity;
	    private final String mTag;
	    private final Class<T> mClass;

	    /** Constructor used each time a new tab is created.
	      * @param activity  The host Activity, used to instantiate the fragment
	      * @param tag  The identifier tag for the fragment
	      * @param clz  The fragment's Class, used to instantiate the fragment
	      */
	    public TabListener(Activity activity, String tag, Class<T> clz) {
	        mActivity = activity;
	        mTag = tag;
	        mClass = clz;
	    }

	    /* The following are each of the ActionBar.TabListener callbacks */

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
	        // Check if the fragment is already initialized
	        if (mFragment == null) {
	            // If not, instantiate and add it to the activity
	            mFragment = Fragment.instantiate(mActivity, mClass.getName());
	            ft.add(android.R.id.content, mFragment, mTag);
	        } else {
	            // If it exists, simply attach it in order to show it
	            ft.attach(mFragment);
	        }
	    }

	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	        if (mFragment != null) {
	            // Detach the fragment, because another one is being attached
	            ft.detach(mFragment);
	        }
	    }

	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	    	onTabUnselected(tab, ft);
	    	onTabSelected(tab, ft);
	    }
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
		context = this;
		messages  = new MessageProvider();
		dialog_box = new DialogBox(this);
		enctype = MessageCryptor.NONE;
        super.onCreate(savedInstanceState);

        ActionBar actionBar = this.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        Tab tab = actionBar.newTab()
                .setText(R.string.messages)
                .setTabListener(new TabListener<MessagesFragment>(
                        this, "Messages", MessagesFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.compose)
                .setTabListener(new TabListener<ComposeFragment>(
                        this, "Compose", ComposeFragment.class));
        actionBar.addTab(tab);

        tab = actionBar.newTab()
                .setText(R.string.settings)
                .setTabListener(new TabListener<SettingsFragment>(
                        this, "Settings", SettingsFragment.class));
        actionBar.addTab(tab);

        View v = new View(this);
        v.setBackgroundColor(Color.rgb(0, 0, 0));
        setContentView(v);
        //setContentView(R.layout.crypt_prototype);
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.crypt_prototype, menu);
        return true;
    }
}