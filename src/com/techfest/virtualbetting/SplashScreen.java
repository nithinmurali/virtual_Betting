package com.techfest.virtualbetting;

import java.util.ArrayList;
import java.util.List;

import com.techfest.library.JsonParser;
import com.techfest.library.ConnectionDetector;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class SplashScreen extends Activity {
	
	public static final String USER_DATA = "usr_data";
	public static final String registerCode = "1234qwert"; // change it
	
	public static final String UserId_pref = "uid";
	public static final String balence_pref = "balence";
	private static String baseURL = "http://testapi.com/";
	public static final Integer initialAmount = 1000;
	
	ConnectionDetector cd;
	Boolean netPresent,error;
	final Context context = this;
	String uid,Rname,Lname;
	int ibal,ldate,GameNum;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		/*
		 * Showing splashscreen while making network calls to download necessary
		 * data before launching the app Will use AsyncTask to make http call
		 */
		cd = new ConnectionDetector(getApplicationContext());
		netPresent = cd.isConnectingToInternet();
		
		if (netPresent == false ) {
			//maybe try connecitng a few more times
			
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	 
				// set title
				alertDialogBuilder.setTitle("Internet Connection");
	 
				// set dialog message
				alertDialogBuilder
					.setMessage("Can't conenct to Internet")
					.setCancelable(false)
					.setPositiveButton("OK",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// if this button is clicked, close
							// current activity
							SplashScreen.this.finish();
						}
					  });
	 
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
	 
					// show it
					alertDialog.show();

		} else {

			new PrefetchData().execute();
	
		}
		
	}

	
	/*
	 * Async Task to make http call
	 */
	private class PrefetchData extends AsyncTask<Void, Void, Void> {

		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// before making http calls
			Log.e("JSON", "Pre execute");

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			
			JsonParser jsonParser = new JsonParser();
			SharedPreferences prefs = getSharedPreferences(USER_DATA, MODE_PRIVATE);
			SharedPreferences.Editor editor = getSharedPreferences(USER_DATA, MODE_PRIVATE).edit();
			
			uid = prefs.getString("text", null);
			
			if (uid == null) {
				//register user
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("code", registerCode));
				JSONObject json = jsonParser.getJSONFromUrl(baseURL + "admin/registerUsr", params);
				Log.e("Response: ", "> " + json);
				if (json != null) {
					try {
						//fetch userid
						JSONObject json_user = json.getJSONObject("user");
						uid = json_user.getString("uid");
						ibal = initialAmount;
						//save uid
						editor.putString("uid", uid);
						editor.putInt("balence", initialAmount);
						editor.commit();
						Log.e("JSON", "> " +uid + "registred");

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				
			} else {
				//user already registred
				//cross check uid
				
			}
			
			//will fetch n update data needed for  main page
			String json = jsonParser
					.getJSONFromUrl(baseURL+"currentMatchDetails");

			Log.e("Response: ", "> " + json);

			if (json != null) {
				try {
					JSONObject jObj = new JSONObject(json)
							.getJSONObject("game_details");
					Lname = jObj.getString("left_name");
					Rname = jObj.getString("right_name");
					ldate = jObj.getInt("Date");
					GameNum = jObj.getInt("GameNum");
					editor.putInt("lastAcessed", ldate);
					editor.commit();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			// After completing http call
			// will close this activity and lauch main activity
			Intent i = new Intent(SplashScreen.this, MainActivity.class);
			i.putExtra(UserId_pref, uid);
			i.putExtra(balence_pref, ibal);
			i.putExtra("Lname", Lname);
			i.putExtra("Rname", Rname);
			i.putExtra("GameNum", GameNum);
			startActivity(i);

			// close this activity
			finish();
		}

	}
	
}