package gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.android.gms.gcm.GcmListenerService;
import com.rutvik.bhagwatiholidays.App;
import com.rutvik.bhagwatiholidays.OffersActivity;
import com.rutvik.bhagwatiholidays.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by ACER on 01-Mar-16.
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = App.APP_TAG + MyInstanceIDListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.i(TAG,"INCOMING PUSH NOTIFICATION....");

        String message=data.getString("message");
        Log.i(TAG,"FROM: "+from);
        Log.i(TAG,"MESSAGE: "+message);

        try{
            JSONObject obj=new JSONObject(message).getJSONObject("push_message");
            Log.i(TAG,"TITLE: "+obj.getString("title"));
            Log.i(TAG,"DESCRIPTION: "+obj.getString("description"));
            Log.i(TAG,"OFFER_TYPE: "+obj.getString("offer_type"));

            sendNotification(obj.getString("title"),obj.getString("description"),obj.getString("offer_type"),obj.getString("image_url"));

        }catch(JSONException e){
            Log.i(TAG,"JSON EXCEPTION: "+e.getMessage());
            sendNotification(message);
        }




    }



    private void sendNotification(String message) {
        Intent intent = new Intent(this, OffersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Trip Enjoy")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification(String title,String message,String type,String ImageUrl) {

        Log.i(TAG,"MAKING NOTIFICATION...");

        Intent intent = new Intent(this, OffersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);






        Notification myNotification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(defaultSoundUri)
                .setContentTitle(title)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentText(message).build();

        // expanded notification icon
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){

            Log.i(TAG,"FOUND API LEVEL < 16 ");

            Bitmap image = null;

            RemoteViews expandedView = new RemoteViews(this.getPackageName(),
                    R.layout.notification_custom_remote);
            expandedView.setTextViewText(R.id.tv_notificationText, title);

            try {
                image = BitmapFactory.decodeStream((InputStream) new URL(ImageUrl).getContent());
                expandedView.setImageViewBitmap(R.id.iv_notificationImage,image);
            } catch (IOException e) {

                e.printStackTrace();

                Log.i(TAG,"FAILED TO LOAD IMAGE IN NOTIFICATION");
            }

            myNotification.bigContentView=expandedView;
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, myNotification);


        sendBroadcast(new Intent("bhagwatiholidays.OFFER_NOTIFICATION"));


    }


}
