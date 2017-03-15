package com.handoop.mhamad.handoop.Tools;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Created by Mhamad on 19-Jan-17.
 */

public class CallAPI extends AsyncTask<String, String, String> {

    public static String ipServer = "192.168.43.188";
    public  static  String link ="http://"+ipServer+":9099/Handoop/services.php";
    public Context context;


    public CallAPI(Context context){
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {


        String resultToDisplay = "";

        InputStream in = null;
        try {

         //   String urlParameters  = "param1=a&param2=b&param3=c";
          //  String urlParameters  = "serviceRequest=signIn";
            String urlParameters = getPostDataString(params);
            byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;
           // String request        = "http://example.com/index.php";
            URL    url            = new URL( link );
            HttpURLConnection conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput( true );
            conn.setInstanceFollowRedirects( false );
            conn.setRequestMethod( "POST" );
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
            conn.setUseCaches( false );
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                wr.write( postData );
            }

            in = new BufferedInputStream(conn.getInputStream());

          //  List<NameValuePair> params = new ArrayList<NameValuePair>();
          //  params.add(new BasicNameValuePair("firstParam", paramValue1));

            Scanner s = new Scanner(in).useDelimiter("\\A");
            resultToDisplay = s.hasNext() ? s.next() : "";
        }
        catch (Exception e) {
            return null;
        }
        return resultToDisplay;
    }


    @Override
    protected void onPostExecute(String result)
    {
        if(result == null)
        {
            Toast.makeText(context, "Connecion error", Toast.LENGTH_LONG).show();
            return;
        }
    }




    public String getPostDataString(String[] params)   {
      try {
          StringBuilder result = new StringBuilder();
          boolean first = true;


          String token = FirebaseInstanceId.getInstance().getToken();

          if (token != null) {
              if (!token.equals("")) {
                  first = false;
                  result.append(URLEncoder.encode("id", "UTF-8"));
                  result.append("=");
                  result.append(URLEncoder.encode(FirebaseInstanceId.getInstance().getToken(), "UTF-8"));
              }
          }

          for (int i = 0; i < params.length; i = i + 2) {
              if (first)
                  first = false;
              else
                  result.append("&");

              result.append(URLEncoder.encode(params[i], "UTF-8"));
              result.append("=");
              result.append(URLEncoder.encode(params[i + 1], "UTF-8"));
          }

          return result.toString();
      }catch (UnsupportedEncodingException e){}

      return "";

      }






}