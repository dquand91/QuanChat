package luongduongquan.com.quanchat;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import luongduongquan.com.quanchat.Utils.Common;

/**
 * Created by luong.duong.quan on 4/3/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
	private static final String TAG = "Notficiation";
	public static final String ACTION         = "PUSH_RECEIVED";



	@Override
	public void onMessageReceived(RemoteMessage remoteMessage) {
		super.onMessageReceived(remoteMessage);
		// ...
		// TODO(developer): Handle FCM messages here.
		// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
		Log.d(TAG, "Receive a message");
		Log.d(TAG, "From: " + remoteMessage.getFrom());



		// Check if message contains a data payload.
//		if (remoteMessage.getData().size() > 0) {
//			Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//			sender_id = remoteMessage.getData().get("sender_id");
//
//			if (/* Check if data needs to be processed by long running job */ true) {
//				// For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
////				scheduleJob();
//			} else {
//				// Handle message within 10 seconds
////				handleNow();
//			}
//
//		}

		if(remoteMessage.getData().size() > 0){
			Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().toString());

			String click_action = remoteMessage.getData().get("click_action");



			String sender_id = remoteMessage.getData().get("sender_id");

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
					.setSmallIcon(R.drawable.heart)
					.setContentTitle(remoteMessage.getData().get("title"))
					.setContentText(remoteMessage.getData().get("body"))
					.setPriority(NotificationCompat.PRIORITY_DEFAULT);

			Intent intentToProfile = new Intent(click_action);
			intentToProfile.putExtra(Common.USERS_ID_TAG, sender_id);

			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentToProfile, PendingIntent.FLAG_UPDATE_CURRENT);

			mBuilder.setContentIntent(pendingIntent);

			int mNotificationID = (int) System.currentTimeMillis();

			NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
			notificationManager.notify(mNotificationID, mBuilder.build());
		}

		// Check if message contains a notification payload.
//		if (remoteMessage.getNotification() != null) {
//			Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//
//			String click_action = remoteMessage.getNotification().getClickAction();
//
//			String sender_id = "";
//			if(remoteMessage.getData().size() > 0){
//				sender_id = remoteMessage.getData().get("sender_id").toString();
//			}
//
//			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//					.setSmallIcon(R.drawable.heart)
//					.setContentTitle(remoteMessage.getNotification().getTitle())
//					.setContentText(remoteMessage.getNotification().getBody())
//					.setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//			Intent intentToProfile = new Intent(click_action);
//			intentToProfile.putExtra(Common.USERS_ID_TAG, sender_id);
//
//			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentToProfile, PendingIntent.FLAG_UPDATE_CURRENT);
//
//			mBuilder.setContentIntent(pendingIntent);
//
//			int mNotificationID = (int) System.currentTimeMillis();
//
//			NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//			notificationManager.notify(mNotificationID, mBuilder.build());
//		}

		// Also if you intend on generating your own notifications as a result of a received FCM
		// message, here is where that should be initiated. See sendNotification method below.
	}
}
