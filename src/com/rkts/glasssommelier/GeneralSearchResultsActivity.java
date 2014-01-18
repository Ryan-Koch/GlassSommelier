package com.rkts.glasssommelier;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.TimelineManager;
import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;
import com.rkts.glasssommelier.apiSearcher.apiTask;

public class GeneralSearchResultsActivity extends Activity {
	TextView tv1;
	private GestureDetector mGestureDetector;
	int index;
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
	private List<Card> mCards;
	private CardScrollView mCardScrollView;
	WineCardScrollAdapter adapter;
	int winePosition;
	private static final int SPEECH_REQUEST = 0;
	private ShareActionProvider shareActionProvider;

	Context context;
	ArrayList<Bitmap> wineImgs = new ArrayList<Bitmap>();
	ProgressBar mProgress;
	URL url;
	FileOutputStream fos;
	ArrayList<String> wineFileNames = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_general_search_results);
		context = this;
		URL[] params = new URL[apiSearcher.wineList.size()];
		
		/*
		 * this stuff is for the image retrieval feature that is not added in yet. more comments below in this
		 * activity
		 * 
		//Getting all the image urls for the asynctask that will download and cache them.
		for (int i = 0; i < apiSearcher.wineList.size(); i++) {
			resultObject product = apiSearcher.wineList.get(i);
			String strUrl = product.getImage();
			System.out.println(strUrl);
			try {
				//System.out.println(product.getImage());
				url = new URL(strUrl);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
				params[i] = url;
				//params[i] = new URL("http://ei.isnooth.com/multimedia/1/0/4/image_187033_square.jpeg");
		}
		//mProgress = (ProgressBar) findViewById(R.id.ProgressBar);
	  //  new DownloadWineImages().execute(params);
		//mProgress.setVisibility(ProgressBar.VISIBLE);
	*/
		createCards();
		
		mCardScrollView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				System.out.println("Wine clicked.. position is.." + position );
				winePosition = position;
				openOptionsMenu();
			}
		});
		


		
	}
	
    private void createCards() {
        mCards = new ArrayList<Card>();
        if(apiSearcher.wineList.size() == 0 || apiSearcher.wineList == null) {
        	String text = "No results found. Please try again.";
        	Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        	toast.show();
        	finish();
        	
        }
        for (int i = 0; i < apiSearcher.wineList.size(); i++) {
    		resultObject product = apiSearcher.wineList.get(i);
    		name = product.getName();
    		winery = product.getWinery();
    		type = product.getType();
    		rating = product.getRating();
    		vintage = product.getVintage();
    		price = product.getPrice();
    		/*
    		 * this stiff is for the commented out section further down that is for an unimplemented feature
    		 * I want to have this app download and display product images in the card objects eventually.
    		 * 
    		String lol = "http://ei.isnooth.com/multimedia/1/8/f/image_522037_square.jpeg";
    		String wName = wineFileNames.get(i).toString();
    		
    		String path = CacheManager.getImagePath(context, wName);
    		Uri uri = Uri.parse(path);
    		*/
    		String wineText = vintage + " " + name + "\n"
					+ "Vineyard: " + winery + "\n"
					+ "Type: " + type + "\n"
					+ "Price: " + price + "\n"
					+ "Rating: " + rating + "\n";
        	
	        Card card;
	        card = new Card(context);
	        card.setText(wineText);
	        card.addImage(R.drawable.wineglasses);
	        //card.setInfo("!");
	        mCards.add(card);

        }
        
	      mCardScrollView = new CardScrollView(this);
	      adapter = new WineCardScrollAdapter();
	      mCardScrollView.setAdapter(adapter);
	      mCardScrollView.activate();
	      setContentView(mCardScrollView);	
        
      }
	
	
	private class WineCardScrollAdapter extends CardScrollAdapter {
		
		@Override
	    public int findIdPosition(Object id) {
	        return -1;
	    }

	    @Override
	    public int findItemPosition(Object item) {
	        return mCards.indexOf(item);
	    }

	    @Override
	    public int getCount() {
	        return mCards.size();
	    }

	    @Override
	    public Object getItem(int position) {
	        return mCards.get(position);
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	        return mCards.get(position).toView();
	    }
	    

	
	}
	
	
    /*
     * Menu stuff
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	System.out.println("inflating!");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_general_search_results, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	System.out.println("Listening for selection...");
        // Handle item selection.
        switch (item.getItemId()) {

            case R.id.menu_item_timeline:
            	TimelineManager tm = TimelineManager.from(this);
            	tm.insert(mCards.get(winePosition));
            	String text = "Wine saved to timeline";
            	Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            	toast.show();
            	

            	return true;
            default:
                return super.onOptionsItemSelected(item); 
        }
    }
	
	
	/*
	 * this commented out stuff is for later. I eventually want to add the ability to insert the product images into the 
	 * card objects. 
	 * 
	private class DownloadWineImages extends AsyncTask<URL, Void, ArrayList<String>> {

		@Override
		protected ArrayList<String> doInBackground(URL... params) {
			System.out.println("Downloading...");
			wineFileNames.clear();
			for(int i = 0; i < params.length; i++ ) {
				byte[] imageByte = new byte[1024];
				ByteArrayOutputStream imageByteOp = new ByteArrayOutputStream();

				System.out.println("Img number.. " + i);
				String name = "wine_" + i;
				URL url = params[i];
				Bitmap wineImg = null;
				try {
					InputStream in = url.openStream();
					wineImg = BitmapFactory.decodeStream(in);
					int bufferSize = 1024;
					byte[] buffer = new byte[bufferSize];
					
					int len = 0;
					
					while((len = in.read(buffer, 0, len)) != -1) {
						imageByteOp.write(buffer, 0, len);
					
						
					}
					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				imageByte = imageByteOp.toByteArray();

				try {
					CacheManager.cacheData(context, imageByte, name);
				} catch (IOException e) {
				
					e.printStackTrace();
				}
				
				wineFileNames.add(name);
				
			
			}
			return wineFileNames;
				
		}
		
		
		protected void onPostExecute(ArrayList<Bitmap> results) {
			System.out.println("Post Execute");
			mProgress.setVisibility(ProgressBar.GONE);
			createCards();
		}
		
		
	}
	*/


	}
    



