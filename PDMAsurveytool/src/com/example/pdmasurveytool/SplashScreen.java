package com.example.pdmasurveytool;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashScreen extends Activity {

	SharedPreferences users;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splashscreen);
		users = getSharedPreferences("Login Credentials", MODE_PRIVATE);

		Thread timer = new Thread() {

			public void run() {
				try {
					sleep(5000);

				} catch (InterruptedException ex) {

				} finally {
					boolean loginstatus = users
							.getBoolean("LoginStatus", false);
					Log.v("status",String.valueOf(loginstatus));
					if (loginstatus) {
						startnextactivity();
					} else {
						Intent i = new Intent(SplashScreen.this, Login.class);
						startActivity(i);
						SplashScreen.this.finish();
					}

				}

			}

		};
		timer.start();

	}

	private void startnextactivity() {
		DbHelper db = new DbHelper(getApplicationContext());
		Cursor cur = db.alldata();
		int count = cur.getCount();
		db.close();
		Log.v("count",String.valueOf(count));
		if (count > 0) {
			Intent i = new Intent(SplashScreen.this, UnsyncDataActivity.class);
			startActivity(i);
			SplashScreen.this.finish();
		} else {

			Intent i = new Intent(SplashScreen.this, MainActivity.class);
			startActivity(i);
			SplashScreen.this.finish();
		}
	}
}
