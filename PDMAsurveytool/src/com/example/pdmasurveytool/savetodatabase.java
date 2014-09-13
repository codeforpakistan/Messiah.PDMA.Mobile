package com.example.pdmasurveytool;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class savetodatabase extends Activity {
	public static String damagetype = null;
	public static String Location = null;
	public static String Detail = null;

	GridView dataList;
	DbHelper db;
	Button addImage, save_sumbit;
	ContactImageAdapter imageAdapter;
	ArrayList<Contact> imageArry;
	EditText detail;
	TextView tvGPS;
	private static final int CAMERA_REQUEST = 1;
	private static final int PICK_FROM_GALLERY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.savetodatabase);
		Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
		damagetype = spinner1.getSelectedItem().toString();
		dataList = (GridView) findViewById(R.id.list);
		detail = (EditText) findViewById(R.id.etDetail);
		Detail = detail.getText().toString();
		db = new DbHelper(getApplicationContext());
		tvGPS = (TextView) findViewById(R.id.tvGPS);
		imageArry = new ArrayList<Contact>();
		save_sumbit = (Button) findViewById(R.id.btnSave_Sumbit);
		save_sumbit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveit();

			}

			private void saveit() {
				if (CheckNetwork.isInternetAvailable(savetodatabase.this)) {
					Toast.makeText(getApplicationContext(),
							"Sending to server", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"saving to database", Toast.LENGTH_SHORT).show();
					SharedPreferences users = getSharedPreferences(
							"Login Credentials", MODE_PRIVATE);
					String Username = users.getString("Username", null);
					db.addData(MainActivity.GID, MainActivity.CNIC,
							MainActivity.ApplicantName,
							MainActivity.FatherName, MainActivity.Address,
							MainActivity.PhoneNumber, Location, damagetype,
							Detail, Username);
					db.close();
					startActivity(new Intent(savetodatabase.this,UnsyncDataActivity.class));
				}

			}
		});
		addImage = (Button) findViewById(R.id.btnAddImage);

		final String[] option = new String[] { "Take from Camera",
				"Select from Gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, option);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Select Option");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Log.e("Selected Item", String.valueOf(which));
				if (which == 0) {
					callCamera();
				}
				if (which == 1) {
					callGallery();
				}

			}
		});
		final AlertDialog dialog = builder.create();
		addImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.show();

			}
		});
		Log.v("GId", MainActivity.GID);
		List<Contact> contacts = db.getAllContacts(MainActivity.GID);
		for (Contact cn : contacts) {
			String log = "ID:" + cn.getID() + " Name: " + cn.getName()
					+ " ,Image: " + cn.getImage();

			// Writing Contacts to log
			Log.d("Result: ", log);
			// add contacts data in arrayList
			imageArry.add(cn);

		}
		imageAdapter = new ContactImageAdapter(this, R.layout.rowview_images,
				imageArry);
		dataList.setAdapter(imageAdapter);
		getlocation();
	}

	private void getlocation() {
		GPSTracker gps = new GPSTracker(savetodatabase.this);
		if (gps.canGetLocation()) {

			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			String loc = String.valueOf(longitude) + "," + String.valueOf(latitude);
			tvGPS.setText(loc);
	}
	}
	@Override
	protected void onResume() {
		super.onResume();
		List<Contact> contacts = db.getAllContacts(MainActivity.GID);
		for (Contact cn : contacts) {
			String log = "ID:" + cn.getID() + " Name: " + cn.getName()
					+ " ,Image: " + cn.getImage();

			// Writing Contacts to log
			Log.d("Result: ", log);
			// add contacts data in arrayList
			imageArry.add(cn);

		}
		imageAdapter = new ContactImageAdapter(this, R.layout.rowview_images,
				imageArry);
		dataList.setAdapter(imageAdapter);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case CAMERA_REQUEST:

			Bundle extras = data.getExtras();

			if (extras != null) {
				Bitmap yourImage = extras.getParcelable("data");
				// convert bitmap to byte
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte imageInByte[] = stream.toByteArray();
				Log.e("output before conversion", imageInByte.toString());
				// Inserting Contacts
				Log.d("Insert: ", "Inserting ..");
				db.addContact(new Contact(MainActivity.GID, imageInByte));
				Intent i = new Intent(savetodatabase.this, savetodatabase.class);
				startActivity(i);
				finish();

			}
			break;
		case PICK_FROM_GALLERY:
			Bundle extras2 = data.getExtras();

			if (extras2 != null) {
				Bitmap yourImage = extras2.getParcelable("data");
				// convert bitmap to byte
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte imageInByte[] = stream.toByteArray();
				Log.e("output before conversion", imageInByte.toString());
				// Inserting Contacts
				Log.d("Insert: ", "Inserting ..");
				db.addContact(new Contact(MainActivity.GID, imageInByte));
				Intent i = new Intent(savetodatabase.this, savetodatabase.class);
				startActivity(i);
				finish();
			}

			break;
		}
	}

	/**
	 * open camera method
	 */
	public void callCamera() {
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		// cameraIntent.putExtra("crop", "true");
		// cameraIntent.putExtra("aspectX", 0);
		// cameraIntent.putExtra("aspectY", 0);
		// cameraIntent.putExtra("outputX", 200);
		// cameraIntent.putExtra("outputY", 150);
		startActivityForResult(cameraIntent, CAMERA_REQUEST);

	}

	/**
	 * open gallery method
	 */

	public void callGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 0);
		intent.putExtra("aspectY", 0);
		intent.putExtra("outputX", 200);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(
				Intent.createChooser(intent, "Complete action using"),
				PICK_FROM_GALLERY);

	}
}
