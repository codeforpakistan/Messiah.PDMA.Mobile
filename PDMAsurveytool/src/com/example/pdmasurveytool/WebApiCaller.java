package com.example.pdmasurveytool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.StrictMode;
import android.util.Log;

public class WebApiCaller {

	public static final String Url = "http://192.168.1.2/Messiah-PDMA/inc/api.login_controller.php"; // for
																			// local
	// testing
	// http://192.168.1.*/api/
	InputStream inputStream;

	
	public JSONObject SignIn(String Username, String Password) {
		JSONObject jobject = null;
		try {
			
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postData = new HttpPost(Url);

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					2);
			nameValuePairs.add(new BasicNameValuePair("Username", Username));
			nameValuePairs.add(new BasicNameValuePair("Password", Password));
			//nameValuePairs.add(new BasicNameValuePair("Type", "login"));

			postData.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(postData);
			HttpEntity entity = response.getEntity();

			inputStream = entity.getContent();
			
			try {
				BufferedReader bReader = new BufferedReader(
						new InputStreamReader(inputStream, "iso-8859-1"), 8);
				StringBuilder sBuilder = new StringBuilder();

				String line = null;
				while ((line = bReader.readLine()) != null) {
					sBuilder.append(line + "\n");
				}

				inputStream.close();
				String result = sBuilder.toString();

				jobject = new JSONObject(result);

			} catch (Exception e) {
				Log.e("StringBuilding & BufferedReader",
						"Error converting result " + e.toString());
			}
		} catch (Exception ex) {
			Log.e("WebApiCaller Error: ", "Error converting result "
					+ ex.getMessage().toString());
		}
		return jobject;
	}

	public JSONObject SignUp(String Username, String Password, String Email) {
		JSONObject array = null;
		String message = null;
		try {

			HttpClient httpClient = new DefaultHttpClient();
			HttpPost postData = new HttpPost(Url + "user");

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					4);
			nameValuePairs.add(new BasicNameValuePair("Email", Email));
			nameValuePairs.add(new BasicNameValuePair("Username", Username));
			nameValuePairs.add(new BasicNameValuePair("Password", Password));
			nameValuePairs.add(new BasicNameValuePair("Type", "SignUp"));

			postData.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(postData);
			HttpEntity entity = response.getEntity();

			inputStream = entity.getContent();

			try {
				BufferedReader bReader = new BufferedReader(
						new InputStreamReader(inputStream, "iso-8859-1"), 8);
				StringBuilder sBuilder = new StringBuilder();

				String line = null;
				while ((line = bReader.readLine()) != null) {
					sBuilder.append(line + "\n");
				}

				inputStream.close();
				String result = sBuilder.toString();

				array = new JSONObject(result);

			} catch (Exception e) {
				Log.e("StringBuilding & BufferedReader",
						"Error converting result " + e.toString());
			}
		} catch (Exception ex) {
			Log.e("WebApiCaller Error: ", "Error converting result "
					+ ex.getMessage().toString());
		}
		return array;

	}
}