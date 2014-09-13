package com.example.pdmasurveytool;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	EditText username, password;
	Button signin;
	DbHelper db;
	String Username = null;
	String Password = null;
	SharedPreferences users;
	Editor editor;
	int status;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in_screen);
		username = (EditText) findViewById(R.id.etUserNameSignIn);
		password = (EditText) findViewById(R.id.etPassSignIn);
		signin = (Button) findViewById(R.id.btnSignIn);
		signin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Username = username.getText().toString();
				Password = password.getText().toString();
				if ((username.getText().length() < 15)
						&& (username.getText().length() > 15))
					username.setError("Incorrect Username");
				else if (password.getText().length() < 1)
					password.setError("Must Required");
				else
					login(Username, Password);
			}

		});

	}

	private void login(String username, String password) {
		db = new DbHelper(getApplicationContext());
		boolean x = db.check(username, password);
		if (x) {
			users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
			editor = users.edit();
			editor.putString("Username", username);
			editor.putString("Password", password);
			editor.putBoolean("LoginStatus", true);
			editor.commit();
			startnextactivity();
		} else {

			if (CheckNetwork.isInternetAvailable(Login.this)) {
				new SetConnection().execute();
				if(status==1){
					
					Toast.makeText(getApplicationContext(), "Welcome",Toast.LENGTH_SHORT);
				}
				if(status==0){
					Toast.makeText(getApplicationContext(), "Error",Toast.LENGTH_SHORT);
					
				}
				
			} else {
				Toast.makeText(Login.this, "No Internet Connection",
						Toast.LENGTH_LONG).show();// dialog
			}
		}
	}
	private void startnextactivity() {
		DbHelper db = new DbHelper(getApplicationContext());
		Cursor cur = db.alldata();
		int count = cur.getCount();
		db.close();
		if (count > 1) {
			Intent i = new Intent(Login.this, UnsyncDataActivity.class);
			startActivity(i);
			Login.this.finish();
		} else {

			Intent i = new Intent(Login.this, MainActivity.class);
			startActivity(i);
			Login.this.finish();
		}
	}
	private void OnlineLogin() {
		UserFunctions userFunction = new UserFunctions();
		JSONObject json = userFunction.loginUser(Username, Password);
		Log.v("Error",json.toString());
//		WebApiCaller caller = new WebApiCaller();
//		JSONObject object = caller.SignIn(Username, Password);
		
		try {
			status = json.getInt("Status");//object.getBoolean("Status");
			
			if (status==1) {
				Log.v("Login", "Username and Password");
				
			} else {
				Log.v("Login", "Invalid Username and Password");

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private class SetConnection extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			OnlineLogin();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			Log.v("Database", "Insertion");
			db.addUser(Username, Password);
			users = getSharedPreferences("Login Credentials", MODE_PRIVATE);
			editor = users.edit();
			editor.putString("Username", Username);
			editor.putString("Password", Password);
			editor.putBoolean("LoginStatus", true);
			editor.commit();
			db.close();
			startnextactivity();
		}

	}
}
