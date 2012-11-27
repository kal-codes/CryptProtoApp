package com.example.cryptprototype;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogBox {
	private Context context;

	public DialogBox(Context context) {
		this.context = context;
	}

	public void showMessage(CharSequence message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
		.setNeutralButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
		});
		builder.create().show();
	}

}