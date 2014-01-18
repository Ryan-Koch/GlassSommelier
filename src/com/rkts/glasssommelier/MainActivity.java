package com.rkts.glasssommelier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.rkts.glasssommelier.apiSearcher.apiTask;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private ProgressBar mProgress;
	private ImageView wineglass;
	private TextView text01;
	private TextView text02;
	private String key ="jr1ko3g8sm984jh6wfyi470br40dmxy1sqeox9cg5y3t1u21"; //snooth key

	static Context maContext; 
	static boolean listMade;
	//declaring gesture detector at class cope
	private GestureDetector mGestureDetector;
	static String searchResults;
	static String searchType;
	//tags for logging

	
	//for voice activity
	private static final int SPEECH_REQUEST = 0;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		maContext = getApplicationContext();
    	mProgress = (ProgressBar) findViewById(R.id.ProgressBar);
    	wineglass = (ImageView) findViewById(R.id.ImageView01);
    	text01 = (TextView) findViewById(R.id.TextView01);
    	text02 = (TextView) findViewById(R.id.TextView02);


    	
		System.out.println(getIntent().getComponent());
		Intent voiceIntent = getIntent();
		
		
		if(voiceIntent == null) {
			
		}
		else if(voiceIntent.getExtras() == null) {
			
			
		}
		else {
		Bundle voiceExtras = voiceIntent.getExtras();	
		
		
		ArrayList<String> voiceQuery = voiceExtras.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
		searchType = voiceQuery.get(0);
		
		
		
		if(searchType == null) {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "What sort of wine are you looking for? Tip: Use the phrase 'under (dollar amount)' to set a budget. You can also mention food dishes");
			startActivityForResult(intent, SPEECH_REQUEST);
    	}
		else if (searchType.equalsIgnoreCase("concierge")) {
			// TODO concierge feature!
		}
    	else {
    		String price = null;
            String re1=".*?";	// Non-greedy match on filler
            String re2="(\\d+)";	// Integer Number 1

            Pattern p = Pattern.compile(re1+re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(searchType);
            if (m.find())
            {
                String int1=m.group(1);
                
                price = int1.toString();
                
                System.out.print("("+int1.toString()+")"+"\n");
            }
    		
    		
    		
    		
    		Pattern whitespace = Pattern.compile("\\u0020");
 	        Matcher matcher = whitespace.matcher(searchType);
 	        String regResult = searchType;
 	        while(matcher.find()) {
 	        	regResult = matcher.replaceAll("+");
 	        	System.out.println(regResult);
 	        }
 	        	String spokenText = regResult;
 	        	
        	apiSearcher api = new apiSearcher();
        	
        	

        	
        	
        	//get last location
        	
        	LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        	Criteria criteria = new Criteria();
        	String provider = lm.getBestProvider(criteria, true);
        	Location location = lm.getLastKnownLocation(provider);
        	
        	Double latitude = location.getLatitude();
        	Double longitude = location.getLongitude();
        	
        	String latStr = latitude.toString();
        	String longStr = longitude.toString();
        	String url;
        	
        	
        	if(price != null) {
        	url = "http://api.snooth.com/wines/?akey=" + key + "&q=" + regResult + "&xp=" + price +  "&lat=" + latStr + "&lng=" + longStr + "&s=sr";	
        	}
        	else {
    		url = "http://api.snooth.com/wines/?akey=" + key + "&q=" + regResult + "&lat=" + latStr + "&lng=" + longStr + "&s=sr";
        	}
    		
    		
    		System.out.println("Request URL: " + url);
    		 
    		System.out.println("GET: " + url);
    		apiSearcher as = new apiSearcher();
    		apiTask at = as.new apiTask(this);
    		mProgress.setVisibility(ProgressBar.VISIBLE);
    		wineglass.setVisibility(ProgressBar.GONE);
    		text01.setVisibility(ProgressBar.GONE);
    		text02.setVisibility(ProgressBar.GONE);
    		at.execute(url);
    		
    	}
		
		
		


		
		
		}
		//Create the gesture detector
		mGestureDetector = detectGesture(this);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
	        Intent data) {
	    if (requestCode == SPEECH_REQUEST && resultCode == RESULT_OK) {
	        List<String> results = data.getStringArrayListExtra(
	                RecognizerIntent.EXTRA_RESULTS);
	        String spokenText = results.get(0);
	       

	        Pattern whitespace = Pattern.compile("\\u0020");
	        Matcher matcher = whitespace.matcher(spokenText);
	        String regResult = spokenText;
	        while(matcher.find()) {
	        	regResult = matcher.replaceAll("+");
	        	System.out.println(regResult);
	        }
	        	spokenText = regResult;
	        	System.out.println("Regular search");
	        	searchType = "general search";
            	
            	
            	LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            	Criteria criteria = new Criteria();
            	String provider = lm.getBestProvider(criteria, true);
            	Location location = lm.getLastKnownLocation(provider);
            	
            	Double latitude = location.getLatitude();
            	Double longitude = location.getLongitude();
            	
            	String latStr = latitude.toString();
            	String longStr = longitude.toString();
            	String url;
            	
            	
    	        String price = null;
                String re1=".*?";	// Non-greedy match on filler
                String re2="(\\d+)";	// Integer Number 1
                System.out.println("Looking for price");
                Pattern p = Pattern.compile(re1+re2,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                Matcher m = p.matcher(regResult);
               // System.out.println("Matcher:" + p.matcher(searchResults));
                if (m.find())
                {
                	System.out.println("found match for price");
                    String int1=m.group(1);
                    
                    price = int1.toString();
                    
                    System.out.print("("+int1.toString()+")"+"\n");
                
                    String re12="(\\+)";	// Any Single Character 1
                    String re22="(under)";	// Word 1
                    String re32="(\\+)";	// Any Single Character 2
                    String re42="(\\$[0-9]+(?:\\.[0-9][0-9])?)(?![\\d])";	// Dollar Amount 1

                    Pattern p2 = Pattern.compile(re12+re22+re32+re42,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                    Matcher m2 = p2.matcher(regResult);
                    if(m2.find()) {
                    	System.out.println("Found price stuff need to remove");
                    	
                    	regResult = m2.replaceAll("");
                    	System.out.println(regResult);
                    }
                
                }
    	        System.out.println("Past price regex");
    	        
    	        
            	
               	if(price != null) {
               		System.out.println("price is null");
                	url = "http://api.snooth.com/wines/?akey=" + key + "&q=" + regResult + "&xp=" + price +  "&lat=" + latStr + "&lng=" + longStr + "&s=sr";	
                	}
               	
                else {
                	System.out.println("Price is null");
            		url = "http://api.snooth.com/wines/?akey=" + key + "&q=" + regResult + "&lat=" + latStr + "&lng=" + longStr + "&s=sr";
                	} 
               	
        		System.out.println("GET: " + url);
        		apiSearcher as = new apiSearcher();
        		apiTask at = as.new apiTask(this);
        		mProgress.setVisibility(ProgressBar.VISIBLE);
        		wineglass.setVisibility(ProgressBar.GONE);
        		text01.setVisibility(ProgressBar.GONE);
        		text02.setVisibility(ProgressBar.GONE);
        		at.execute(url);
	        	
	    
	        
	        
	        
	    }
	    super.onActivityResult(requestCode, resultCode, data);
	}
	

	
	private GestureDetector detectGesture(Context context) {
		GestureDetector gestureDetector = new GestureDetector(context);
		
		//base listener
		gestureDetector.setBaseListener(new GestureDetector.BaseListener() {
			
			@Override
			public boolean onGesture(Gesture gesture) {
                if (gesture == Gesture.TAP) {
                    // do something on tap
            		System.out.println("Tap");
            		//open the options menu
            		openOptionsMenu();
                    return true;
                    
                } else if (gesture == Gesture.TWO_TAP) {
                    // do something on two finger tap
            		System.out.println("Two tap"); 

                    return true;
                } else if (gesture == Gesture.SWIPE_RIGHT) {
                    // do something on right (forward) swipe
            		System.out.println("forward");

                	
                    return true;
                } else if (gesture == Gesture.SWIPE_LEFT) {
                    // do something on left (backwards) swipe
            		System.out.println("forward");

                	
                    return true;
                }
                return false;
            }
			
		});
		
        gestureDetector.setFingerListener(new GestureDetector.FingerListener() {
            @Override
            public void onFingerCountChanged(int previousCount, int currentCount) {
              // do something on finger count changes
            }
        });
        gestureDetector.setScrollListener(new GestureDetector.ScrollListener() {
            @Override
            public boolean onScroll(float displacement, float delta, float velocity) {
                // do something on scrolling
            	return true;
            }
        });
		
		System.out.println(gestureDetector);
		return gestureDetector;
		
	}
	
    /*
     * Send generic motion events to the gesture detector
     */
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            return mGestureDetector.onMotionEvent(event);
        }
        return false;
    }

    
    /*
     * Menu stuff
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	System.out.println("inflating!");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	System.out.println("Listening for selection...");
        // Handle item selection.
        switch (item.getItemId()) {

            case R.id.search:
        		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        		startActivityForResult(intent, SPEECH_REQUEST);
            	return true;
            default:
                return super.onOptionsItemSelected(item); 
        }
    }

    @Override
	protected void onResume() {
		super.onResume();
		Intent intent = getIntent();
		
		
		mProgress.setVisibility(ProgressBar.GONE);
		wineglass.setVisibility(ProgressBar.VISIBLE);
		text01.setVisibility(ProgressBar.VISIBLE);
		text02.setVisibility(ProgressBar.VISIBLE);
		
    }
    
    /**
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        // Nothing else to do, closing the activity.
        //finish();
    }
	**/

    

}
