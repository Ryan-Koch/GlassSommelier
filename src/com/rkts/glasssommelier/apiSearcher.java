/**
 * 
 */
package com.rkts.glasssommelier;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;

/**
 * @author ryan
 *
 */
public class apiSearcher {
	InputStream finalContent;
	static String finalResult;
	//MainActivity mActivity;
	static ArrayList<resultObject> wineList = new ArrayList<resultObject>();

	
	apiSearcher() {
		
	}
	
	//This method is used to parse the JSON data from the initial wine search.
	public void parseResult(String result) {
		JSONArray wines;
		String name;
		String code;
		String region;
		String winery;
		String winery_id;
		String varietal;
		String price;
		String vintage;
		String type;
		String link;
		String image;
		String rating;
		JSONObject wine;
		
		try {
			JSONObject object = new JSONObject(result);
			wines = object.getJSONArray("wines");
			System.out.println(wines);
			for(int i = 0; i < wines.length(); i++) {
				wine = wines.getJSONObject(i);
				name = wine.getString("name");
				code = wine.getString("code");
				region = wine.getString("region");
				winery = wine.getString("winery");
				winery_id = wine.getString("winery_id");
				varietal = wine.getString("varietal");
				price = wine.getString("price");
				vintage = wine.getString("vintage");
				type = wine.getString("type");
				link = wine.getString("link");
				image = wine.getString("image");
				rating = wine.getString("snoothrank");
				
				System.out.println(name + " " + vintage);
				wineList.add(new resultObject(name,code,region,winery,winery_id,varietal,price,vintage,type,link,image,rating));
				System.out.println("Added wine to list!");
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			apiSearcher.wineList.clear();
			e.printStackTrace();
			
		}
		
		
		
	}
	
	
	//This method is used for fetching the primary wine search results.
	public class apiTask extends AsyncTask<String, Integer, String> {
		Context context;
		public apiTask(Context context) {
			this.context = context.getApplicationContext();
		}
		
		
		protected String doInBackground(String... params) {
			System.out.println("Starting GET");
			InputStream content = null;
			BufferedReader inBuffer = null;
			String stringResult = "";
		if(params.length == 1) {
			String url = params[0];
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(new HttpGet(url));
				HttpEntity responseEntity = response.getEntity();
				if(responseEntity == null) {
					System.out.println("No response");
					return null;
				}
				content = responseEntity.getContent();
				inBuffer = new BufferedReader(
						new InputStreamReader(
							content));

					StringBuffer stringBuffer = new StringBuffer("");
					String line = "";
					String newLine = System.getProperty("line.separator");
					while ((line = inBuffer.readLine()) != null) {
						stringBuffer.append(line + newLine);
					}
					inBuffer.close();
					stringResult = stringBuffer.toString();
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

			return stringResult;
	     }

	     protected void onProgressUpdate(Integer... progress) {
	  
	     }

	     protected void onPostExecute(String result) {
	    	System.out.println("Post execute");
	    	System.out.println(result);
	    	parseResult(result);
	    
	    	Intent intent = new Intent(context, GeneralSearchResultsActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(intent);
	    	 
	    	 
	     }

	
	
	}
	
	
	//This method is pretty much just like the one above but it's for store results so at the 
	// post execute portion it launches the StoreInfoActivity instead.
	public class apiTaskStore extends AsyncTask<String, Integer, String> {
		Context context;
		public apiTaskStore(Context context) {
			this.context = context.getApplicationContext();
		}
		
		
		protected String doInBackground(String... params) {
			System.out.println("Starting GET");
			InputStream content = null;
			BufferedReader inBuffer = null;
			String stringResult = "";
		if(params.length == 1) {
			String url = params[0];
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(new HttpGet(url));
				HttpEntity responseEntity = response.getEntity();
				if(responseEntity == null) {
					System.out.println("No response");
					return null;
				}
				content = responseEntity.getContent();
				inBuffer = new BufferedReader(
						new InputStreamReader(
							content));

					StringBuffer stringBuffer = new StringBuffer("");
					String line = "";
					String newLine = System.getProperty("line.separator");
					while ((line = inBuffer.readLine()) != null) {
						stringBuffer.append(line + newLine);
					}
					inBuffer.close();
					stringResult = stringBuffer.toString();
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

			return stringResult;
	     }

	     protected void onProgressUpdate(Integer... progress) {
	  
	     }

	     protected void onPostExecute(String result) {
	    	System.out.println("Post execute");
	    	parseResult(result);
	    
	    	Intent intent = new Intent(context, GeneralSearchResultsActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	context.startActivity(intent);
	    	 
	    	 
	     }

	
	
	}

	
}