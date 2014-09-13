package com.example.pdmasurveytool;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony.Sms.Conversations;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UnsyncDataActivity extends Activity implements OnClickListener {
	ListView list;
	ArrayList<String> names;
	Button sync, cont;
	SharedPreferences users;
	Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unsyncdata);
		sync = (Button) findViewById(R.id.btnSync);
		sync.setOnClickListener(this);
		cont = (Button) findViewById(R.id.btnSkip);
		cont.setOnClickListener(this);
		list = (ListView) findViewById(R.id.listunsync);
		names = new ArrayList<String>();
		getNames();
		updateadapter();

	}

	private void getNames() {
		DbHelper db = new DbHelper(getApplicationContext());
		Cursor cursor = db.alldata();
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor
					.getColumnIndex("ApplicantName"));
			names.add(name);
			Toast.makeText(getApplicationContext(), name, 5000).show();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.logout, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// action with ID action_logout was selected
		case R.id.action_logout:
			users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
			editor = users.edit();
			editor.putString("Username", null);
			editor.putString("Password", null);
			editor.putBoolean("LoginStatus", false);
			editor.commit();
			startActivity(new Intent(UnsyncDataActivity.this, Login.class));
			break;
		// action with ID action_settings was selected
		case R.id.action_settings:
			Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}

		return true;

	}

	public void updateadapter() {
		listrow adapter = new listrow(getApplicationContext(), names);
		list.setAdapter(adapter);
	}

	private class listrow extends ArrayAdapter<String> {
		private final Context context;

		public listrow(Context context, ArrayList<String> names) {
			super(context, R.layout.unsyncdata, names);
			this.context = context;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View v = inflater.inflate(R.layout.rowview_unsyncdata,
					parent, false);
			TextView tv = (TextView) v.findViewById(R.id.tvName);
			tv.setText(names.get(position));
			return v;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSync:
			Toast.makeText(getApplicationContext(), "SYVN",5000).show();
			new SendData().execute();
			break;
		case R.id.btnSkip:
			startActivity(new Intent(UnsyncDataActivity.this,
					MainActivity.class));
			break;
		}
	}

	private void syncSQLiteMySQLDB() throws JSONException, UnsupportedEncodingException {
		DbHelper db = new DbHelper(getApplicationContext());
		UserFunctions ftn = new UserFunctions();
		String images = null;
		boolean x = true;
		Cursor cur = db.alldata();
		//cur.moveToLast();
		if (cur.moveToFirst()) {
			do {
				String GID = cur.getString(cur.getColumnIndex("GID"));
				String CNIC = cur.getString(cur.getColumnIndex("CNIC"));
				String PhoneNumber = cur.getString(cur
						.getColumnIndex("PhoneNumber"));
				String ApplicantName = cur.getString(cur
						.getColumnIndex("ApplicantName"));
				String FatherName = cur.getString(cur
						.getColumnIndex("FatherName"));
				String Address = cur.getString(cur.getColumnIndex("Address"));
				String Location = cur.getString(cur.getColumnIndex("Location"));
				String Damage = cur.getString(cur.getColumnIndex("Damage"));
				String Detail = cur.getString(cur.getColumnIndex("Detail"));
				String Username = cur.getString(cur.getColumnIndex("Username"));

				Cursor cur1 = db.getimages(GID);
				if (cur.moveToFirst()) {
					do {
						byte[] image = cur.getBlob(cur1.getColumnIndex("IMAGE"));
						String image_str = Base64.encodeToString(image,Base64.URL_SAFE);
						String s = new String(image);
						
						if (x) {
							images = image_str;
							x = false;
						} else {
							images += "|" + image_str;
						}
					} while (cur.moveToNext());
				}
//				JSONObject json = ftn.UploadData(GID, CNIC, ApplicantName,
//						FatherName, Address, PhoneNumber, Location, Damage,
//						Detail, Username, images);
//				boolean status = json.getBoolean("Status");
				// delete data against the current GID
				
Toast.makeText(getApplicationContext(), "HELP", Toast.LENGTH_SHORT).show();
			} while (cur.moveToNext());
		}
		// boolean status = json.getBoolean("Status");
	}

	private class SendData extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			try {
				syncSQLiteMySQLDB();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
}
