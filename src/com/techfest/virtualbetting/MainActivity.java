package com.techfest.virtualbetting;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfest.library.JsonParser;

import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
	
	public static final String USER_DATA = "usr_data";
	public static final String registerCode = "1234qwert"; // change it
	
	public static final String UserId_pref = "uid";
	public static final String balence_pref = "amount";
	private static String baseURL = "http://testapi.com/";
	public static final Integer initialAmount = 1000;
	
	String status,lstatus;
	Integer GameNum;
	
	TextView uid;
	TextView balence,lanem,rname;
	EditText betamount;
	Button bet;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
	
	private class checkBet extends AsyncTask<Void, Integer, Void>
	{
		private Integer curGameNum; // to be set to game num intially
		
		
	    protected void onPreExecute (){
	        Log.d("PreExceute","On pre Exceute......");
	        //set the last status here
	        JsonParser jsonParser = new JsonParser();
	        String json = jsonParser
					.getJSONFromUrl(baseURL+"matchStatus");

			if (json != null) {
				try {
					JSONObject jObj = new JSONObject(json)
							.getJSONObject("game_details");
					lstatus = jObj.getString("status"); 
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.d("lsatstus set",lstatus);
			}
	    }
	    
	    
	    protected Void doInBackground(Void...arg0) {
	        Log.d("DoINBackGround","On doInBackground...");
	        JsonParser jsonParser = new JsonParser();
	        
	        while (true) {
	        	String json = jsonParser
						.getJSONFromUrl(baseURL+"matchStatus");

				if (json != null) {
					try {
						JSONObject jObj = new JSONObject(json)
								.getJSONObject("game_details");
						status = jObj.getString("status");
						curGameNum =  jObj.getInt("GameNum");
						if ( GameNum == curGameNum)
						{	
							
							if (status.equals("on") ) {
								//betting on enable bet button
								publishProgress(1);
								Log.d("enable btn", status);
								
							}else if(status.equals("off"))
							{
								//betting off disable
								publishProgress(0);
								Log.d("disable btn", status);
							}
						}else
						{
							// game has been changed change betting page
							initiate();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}else
				{
					Log.d("error", "fetched jason is null");
				}
				//delay 2 sec
				try {
			        Thread.sleep(2000);         
			    } catch (InterruptedException e) {
			       e.printStackTrace();
			    }
				
			}
	       return null;
	    }

	    protected void onProgressUpdate(Integer...a){
	        Log.d("You are in progress update ... " , a[0].toString());
	        if (a[0] == 1) {
				//enable button
	        	
			} else if (a[0] == 0) {
				//disable button
				
			}
	    }
	    
	    protected void onPostExecute() {
	        Log.d("error","async ends");
	    }
	}
	
}
