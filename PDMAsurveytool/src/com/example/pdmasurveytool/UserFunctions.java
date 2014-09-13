/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package com.example.pdmasurveytool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class UserFunctions {

	private JSONParser jsonParser;

	private static String loginURL = "http://192.168.1.5:8383/Messiah.PDMA.Web/inc/api.login_controller.php/";// "http://messiah-pdma.herokuapp.com/inc/api.login_controller.php";
	private static String registerURL = "http://10.0.2.2/ah_login_api/";

	private static String login_tag = "login";
	private static String register_tag = "register";

	// constructor
	public UserFunctions() {
		jsonParser = new JSONParser();
	}

	/**
	 * function make Login Request
	 * 
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String Username, String Password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		// params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("Username", Username));
		params.add(new BasicNameValuePair("Password", Password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		Log.e("JSON", json.toString());
		return json;
	}

	/**
	 * function make Login Request
	 * 
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password) {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}

	/**
	 * Function for uploading data
	 * @param images 
	 * */
	public JSONObject UploadData(String GID, String CNIC, String Name,
			String FName, String Address, String Phone, String Location,
			String Damage, String Detail, String Username, String images) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("GID", GID));
		params.add(new BasicNameValuePair("CNIC", CNIC));
		params.add(new BasicNameValuePair("PhoneNumber", Phone));
		params.add(new BasicNameValuePair("ApplicantName", Name));
		params.add(new BasicNameValuePair("FatherName", FName));
		params.add(new BasicNameValuePair("Address", Address));
		params.add(new BasicNameValuePair("Location", Location));
		params.add(new BasicNameValuePair("Damage", Damage));
		params.add(new BasicNameValuePair("Detail", Detail));
		params.add(new BasicNameValuePair("Username", Username));
		params.add(new BasicNameValuePair("RawData", images));

		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;

	}

	public JSONObject uploadImage(String image_str,String GID) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("GID", GID));
		params.add(new BasicNameValuePair("Image", image_str));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}

	/**
	 * Function get Login status
	 * */
	// public boolean isUserLoggedIn(Context context){
	// DatabaseHandler db = new DatabaseHandler(context);
	// int count = db.getRowCount();
	// if(count > 0){
	// // user logged in
	// return true;
	// }
	// return false;
	// }
	//
	// /**
	// * Function to logout user
	// * Reset Database
	// * */
	// public boolean logoutUser(Context context){
	// DatabaseHandler db = new DatabaseHandler(context);
	// db.resetTables();
	// return true;
	// }

}
