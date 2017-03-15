package com.handoop.mhamad.handoop.Tools;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.TextView;

import com.handoop.mhamad.handoop.MainActivity;
import com.handoop.mhamad.handoop.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Mhamad on 19-Jan-17.
 */

public class Utils {

/*
    public static  void addNotification(Context context ) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Notifications Example")
                        .setContentText("This is a test notification");

        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
*/

    public static boolean verify(TextView t)
    {
        if(t.getText().toString().equals(""))
        {
            t.setError("required");
            return false;
        }
        return true;
    }


    public static String formatDate(String da)
    {
        Date date = new Date(Long.parseLong(da));
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yy#HH:mm");
        return dateFormat.format(date);
    }


}
