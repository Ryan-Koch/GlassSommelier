/**
 * 
 */
package com.rkts.glasssommelier;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.TimelineManager;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;

/**
 * @author ryan
 * @Class: cardHandler
 * @Purpose: Create and public live cards into the glass time line.
 * @Methods: 
 * 			onBind:
 * 				Purpose:
 * 				Input: intent
 * 				Output: null
 * 			onCreate: 
 * 				Input: void
 * 				output: void
 *			onStartCommand:
 *				Purpose: initialize main live card once service is started. 
 * 				Input: void
 * 				output: void
 * 
 * 				
 *
 */
public class cardHandler extends Service {
	public cardHandler() {
	
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate() {
		//nothing to do here for now
		
	
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		//publishCard(getApplicationContext(), "main_card");
		System.out.println("Publishing main card");
		//return START_STICKY so the service keeps running unless we kill it explicitly.
		return START_STICKY;
	}
	
	//Cached instances of live cards. Currently I've decided to only have two historical search cards and one launch card
	private LiveCard mLiveCard; 
	private LiveCard r1LiveCard;
	private LiveCard r2LiveCard;
	private LiveCard mainMenuCard;
	/**
	public void publishCard(Context context, String _cardId) {
		//Check for null. If this variable isn't null then we've already published the card.
		if(mLiveCard == null) {
			String cardId = _cardId;
			TimelineManager tm = TimelineManager.from(context);
			//Set the cardId and layout
			mLiveCard = tm.getLiveCard(cardId);
			mLiveCard.setViews(new RemoteViews(context.getPackageName(),R.layout.maintile));
			//Set the entry class and context for the card's intent
			Intent intent = new Intent(context, MainActivity.class);
			
			mLiveCard.setAction(PendingIntent.getActivity(context, 0, intent, 0));
			
			//publish the card (default behavior is to publish silently
			mLiveCard.publish();
			
		}
		else {
			//do nothing because the card is already there
			return;
		}
		
	}
	
	**/
	public void unpublishAllCards(Context context) {
		//if for some reason I need to destroy a card I'll use this method 
		//Check to make sure there is a card to destroy
		if(mLiveCard != null) {
			mLiveCard.unpublish();
			//set the card back to null so that we can publish a new card if necessary
			mLiveCard = null;	
		}
		else {
			//do nothing as there is no card to destroy
			
		}
		if(r1LiveCard != null) {
			r1LiveCard.unpublish();
			//set the card back to null so that we can publish a new card if necessary
			r1LiveCard = null;	
		}
		else {
			//do nothing as there is no card to destroy
		
		}
		if(r2LiveCard != null) {
			r2LiveCard.unpublish();
			//set the card back to null so that we can publish a new card if necessary
			r2LiveCard = null;	
		}
		else {
			//do nothing as there is no card to destroy
		
		}
	}
	
	
}
