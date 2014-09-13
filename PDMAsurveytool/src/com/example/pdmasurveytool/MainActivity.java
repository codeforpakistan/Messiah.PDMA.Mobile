package com.example.pdmasurveytool;

import com.example.pdmasurveytool.SimpleGestureFilter.SimpleGestureListener;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity implements
		SimpleGestureListener {
	public static String GID = null;
	public static String CNIC = null;
	public static String ApplicantName = null;
	public static String FatherName = null;
	public static String PhoneNumber = null;
	public static String Address = null;
	EditText cnic, applicantname, fathername, phonenumber, address, gid;
	private SimpleGestureFilter detector;
	Button next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		detector = new SimpleGestureFilter(this, this);

		
		next = (Button) findViewById(R.id.btnSumbit_main_activity);
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(gid.getText().length()<1)
					gid.setError("Greviance ID must required");
				else if((cnic.getText().length()<13)&&(cnic.getText().length()>13))
					cnic.setError("CNIC number must be 13 digit long");
				else if(applicantname.getText().length()<1)
					applicantname.setError("must required");
				else if(fathername.getText().length()<1)
					fathername.setError("must required");
				else if(phonenumber.getText().length()<1)
					phonenumber.setError("must required");
				else if(address.getText().length()<1)
					address.setError("must required");
				else{
					GID = gid.getText().toString();
					CNIC = cnic.getText().toString();
					ApplicantName = applicantname.getText().toString();
					FatherName = fathername.getText().toString();
					PhoneNumber = phonenumber.getText().toString();
					Address = address.getText().toString();
				startActivity(new Intent(MainActivity.this,
						savetodatabase.class));
				}
			}
		});
		gid = (EditText) findViewById(R.id.etGID);
		cnic = (EditText) findViewById(R.id.etCNIC);
		applicantname = (EditText) findViewById(R.id.etName);
		fathername = (EditText) findViewById(R.id.etFatherName);
		phonenumber = (EditText) findViewById(R.id.etPhoneNumber);
		address = (EditText) findViewById(R.id.etAddress);
		address.setImeOptions(EditorInfo.IME_ACTION_DONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// gid.setText(GID);
		// cnic.setText(CNIC);
		// applicantname.setText(ApplicantName);
		// fathername.setText(FatherName);
		// phonenumber.setText(PhoneNumber);
		// address.setText(Address);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent me) {
		// Call onTouchEvent of SimpleGestureFilter class
		this.detector.onTouchEvent(me);
		return super.dispatchTouchEvent(me);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	@Override
	public void onSwipe(int direction) {
		switch (direction) {

		case SimpleGestureFilter.SWIPE_RIGHT:

			startActivity(new Intent(MainActivity.this, savetodatabase.class));
			// overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
			break;
		case SimpleGestureFilter.SWIPE_LEFT:
			Toast.makeText(getApplicationContext(), "left", Toast.LENGTH_SHORT)
					.show();
			break;

		}

	}

	@Override
	public void onDoubleTap() {
		// TODO Auto-generated method stub

	}

}
