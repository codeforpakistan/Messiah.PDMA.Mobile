package com.example.pdmasurveytool;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

	public static final String TAG = "DbHelper";
	public static final String DB_NAME = "PDMA_DB.db";
	public static final int DB_ver = 1;
	public final String TABLE_1 = "TBL_Data";
	public final String TABLE_2 = "TBL_IMAGE";
	public final String TABLE_3 = "TBL_Users";
	public static final boolean Debug = false;

	public DbHelper(Context context) {
		super(context, DB_NAME, null, DB_ver);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String query = "Create Table "
				+ TABLE_1
				+ "( ID INTEGER PRIMARY KEY AUTOINCREMENT, GID TEXT, CNIC TEXT,PhoneNumber TEXT, ApplicantName TEXT, FatherName TEXT, Address TEXT, Location TEXT, Damage TEXT, Detail TEXT, Username Text)";
		String query1 = "Create Table "
				+ TABLE_2
				+ "( ID INTEGER PRIMARY KEY AUTOINCREMENT, GID TEXT, IMAGE BLOB)";
		String query2 = "Create Table "
				+ TABLE_3
				+ "( ID INTEGER PRIMARY KEY AUTOINCREMENT, UserName TEXT, Password TEXT)";

		db.execSQL(query);
		db.execSQL(query1);
		db.execSQL(query2);

	}

	public Cursor query(SQLiteDatabase db, String query) {
		Cursor cur = db.rawQuery(query, null);
		return cur;

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(String.format("DROP TABLE IF EXIST %s", TABLE_1));
		db.execSQL(String.format("DROP TABLE IF EXIST %s", TABLE_2));
		db.execSQL(String.format("DROP TABLE IF EXIST %s", TABLE_3));
		if (Debug) {

			Log.d(TAG, "Droping table");

		}

		this.onCreate(db);

	}

	public// Adding new contact
	void addContact(Contact contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("GID", contact._GID); // Contact Name
		values.put("IMAGE", contact._image); // Contact Phone

		// Inserting Row
		db.insert(TABLE_2, null, values);
		db.close(); // Closing database connection
	}

	public void addUser(String UserName, String Password) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put("UserName", UserName); // Contact Name
		values.put("Password", Password); // Contact Phone

		// Inserting Row
		db.insert(TABLE_3, null, values);
		db.close(); // Closing database connection

	}

	public void addData(String GID, String CNIC, String Name, String FName,
			String Address, String Phone, String Location, String Damage,
			String Detail, String Username) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("GID", GID);
		values.put("CNIC", CNIC);
		values.put("PhoneNumber", Phone);
		values.put("ApplicantName", Name);
		values.put("FatherName", FName);
		values.put("Address", Address);
		values.put("Location", Location);
		values.put("Damage", Damage);
		values.put("Detail", Detail);
		values.put("Username", Username);
		// Inserting Row
		db.insert(TABLE_1, null, values);
		db.close(); // Closing database connection
	}
public Cursor getimages(String GID){
	SQLiteDatabase db = this.getReadableDatabase();
	String selectQuery = "SELECT  * FROM TBL_IMAGE WHERE GID = '" + GID
			+ "'";
	Cursor cursor = db.rawQuery(selectQuery, null);
	db.close();
return cursor;
	
	
}
	// Getting single contact
	// Contact getContact(int id) {
	// SQLiteDatabase db = this.getReadableDatabase();
	//
	// Cursor cursor = db.query(TABLE_2, new String[] { KEY_ID,
	// KEY_NAME, KEY_IMAGE }, KEY_ID + "=?",
	// new String[] { String.valueOf(id) }, null, null, null, null);
	// if (cursor != null)
	// cursor.moveToFirst();
	//
	// Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
	// cursor.getString(1), cursor.getBlob(1));

	// return contact
	// return contact;
	//
	// }

	// Getting All Contacts
	public List<Contact> getAllContacts(String GID) {
		List<Contact> contactList = new ArrayList<Contact>();
		// Select All Query
		String selectQuery = "SELECT  * FROM TBL_IMAGE WHERE GID = '" + GID
				+ "'";

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Contact contact = new Contact();
				contact.setID(Integer.parseInt(cursor.getString(0)));
				contact.setName(cursor.getString(1));
				contact.setImage(cursor.getBlob(2));
				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		// close inserting data from database
		db.close();
		// return contact list
		return contactList;

	}

	public boolean check(String UserName, String Password) {
		String selectQuery = "SELECT  * FROM TBL_Users WHERE UserName = '"
				+ UserName + "' AND Password = '" + Password + "'";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		int count = cursor.getCount();
		Log.v("counter",String.valueOf(count));
		if (count > 0){
			db.close();
			return true;}
		else{
			db.close();
			return false;}
	}

	// // Updating single contact
	// public int updateContact(Contact contact) {
	// SQLiteDatabase db = this.getWritableDatabase();
	//
	// ContentValues values = new ContentValues();
	// values.put(KEY_NAME, contact.getName());
	// values.put(KEY_IMAGE, contact.getImage());
	//
	// // updating row
	// return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
	// new String[] { String.valueOf(contact.getID()) });
	//
	// }
	//
	// // Deleting single contact
	// public void deleteContact(Contact contact) {
	// SQLiteDatabase db = this.getWritableDatabase();
	// db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
	// new String[] { String.valueOf(contact.getID()) });
	// db.close();
	// }

	// Getting contacts Count
	public int getContactsCount() {
		String countQuery = "SELECT  * FROM TBL_IMAGE";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}

	public Cursor alldata() {
		String countQuery = "SELECT  * FROM TBL_Data";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		return cursor;
	}
}
