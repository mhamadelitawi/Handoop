package com.handoop.mhamad.handoop;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.handoop.mhamad.handoop.Models.Result;
import com.handoop.mhamad.handoop.Tools.CallAPI;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.handoop.mhamad.handoop.Tools.MySQLLite;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Mhamad on 18-Jan-17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";



    public void handleGetResult(String guid)
    {
        new CallAPI(this) {
            @Override
            protected void onPostExecute(String result)
            {
                if(result == null)
                {
                    return;
                }
                else
                {
                    Result res = new Result(result);
                    MySQLLite ms = new MySQLLite(context);
                    ms.addHistory(res);
                    ArrayList<Result> ar =  ms.getAllHistories();
                    sendNotification(res.title , "The task is finished");
                }
            }
        }.execute("serviceRequest","getResult","guid",guid);

    }





    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
      //  Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0  && remoteMessage.getNotification() != null) {
            String guid = remoteMessage.getData().get("guid");
            String title = remoteMessage.getData().get("title");
            String status = remoteMessage.getData().get("status");

            if (guid.equals("Error")) sendNotification(title , status);
            else
            {
                handleGetResult(guid);
            }
        }
    }







    public  void sendNotification(String title , String message) {


        Intent intent = new Intent(this, ResultActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title) //Handoop
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());










    }
}