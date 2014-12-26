package com.techfest.virtualbetting;

import org.json.JSONException;
import org.json.JSONObject;

import com.techfest.library.JsonParser;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ResultActivity extends Activity{

	public static final String USER_DATA = "usr_data";
	public static final String registerCode = "1234qwert"; // change it
	
	public static final String UserId_pref = "uid";
	public static final String balence_pref = "amount";
	private static String baseURL = "http://testapi.com/";
	public static final Integer initialAmount = 1000;
	
	String status,lstatus,lname,rname,balence, uid;
	Integer GameNum, curGameNum, betamt, ibal, rpublished;
	
	TextView txtviewResult, txtviewBalence;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_result);
		
		Intent i = getIntent();
		uid = i.getStringExtra(UserId_pref);
		ibal = i.getIntExtra(balence_pref, 0);
		GameNum = i.getIntExtra("GameNum",0);
		

	
	}
	
	private class checkResult extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			
			JsonParser jsonParser = new JsonParser();
	        String json = jsonParser
					.getJSONFromUrl(baseURL+"matchResult?num=" + GameNum);
	        while(true){
				if (json != null) {
					try 
					{
						JSONObject jObj = new JSONObject(json)
								.getJSONObject("result");
						rpublished = jObj.getInt("published"); 
						if (rpublished == 1) {
							return null;
						}					
					} catch (JSONException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			
				//delay 1 seconds
				try 
				{
			        Thread.sleep(1000);         
			    } catch (InterruptedException e) {
			       e.printStackTrace();
			    }
				
	        }
	        
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
		}
		
		
	}
}
