package com.handoop.mhamad.handoop.Tools;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import static com.handoop.mhamad.handoop.Tools.CallAPI.ipServer;

/**
 * Created by Mhamad on 13-Feb-17.
 */

public class MyListener implements View.OnClickListener {

    public Context context;
    public int position;

    public MyListener( Context context , int position) {
        this.position = position;
        this.context = context;
    }

    public MyListener(){}

    @Override
    public void onClick(View v) {
    }


 //   download("http://10.0.0.18:8080/download/j.jpg" , "hayda.jpg"  , "hayde" , "hayde houwe l file");


    public static String getDownloadLink(String pathresult)
    {
      return  "http://"+ipServer+":9099/Handoop/"+pathresult+".txt";
    }


    public void download(String pathresult , String guid  )
    {

        String exit = guid+".txt";
        String title="downloading "+exit ;
        String description="downloading "+exit;

        String url = getDownloadLink( pathresult);
       // String url = "http://"+ipServer+":9099/Handoop/"+pathresult+".txt";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription(description);
        request.setTitle(title);
// in order for this if to run, you must use the android 3.2 to compile your app
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        // request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, exit);

        request.setDestinationInExternalPublicDir("/Handoop",exit);

// get download service and enqueue file
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            manager.enqueue(request);
        }catch (Exception e)
        {
            String x = "";
        }

    }







    public void setClipboard( String text) {

       try {
           if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
               android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
               clipboard.setText(text);
           } else {
               android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
               android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
               clipboard.setPrimaryClip(clip);
           }
       }catch (Exception e)
       {
           String s = "";
       }
    }




}
