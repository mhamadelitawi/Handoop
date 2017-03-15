package com.handoop.mhamad.handoop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.handoop.mhamad.handoop.Tools.CallAPI;

import static com.handoop.mhamad.handoop.Tools.CallAPI.ipServer;
import static com.handoop.mhamad.handoop.Tools.CallAPI.link;
import static com.handoop.mhamad.handoop.Tools.Utils.verify;

public class HomeActivity extends AppCompatActivity {

    TextView Username;
    TextView keys;
    TextView ip;

    String token ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Username = (TextView)findViewById(R.id.username);
        keys = (TextView)findViewById(R.id.keys);
        ip = (TextView)findViewById(R.id.ip);
        token = FirebaseInstanceId.getInstance().getToken();


    }


    public void getDifferentUser(View view)
    {
        new CallAPI(this).execute("serviceRequest","getDifferentUser");
        Toast.makeText(this, "Different User request sent", Toast.LENGTH_LONG).show();
    }



    public void getFollowing(View view)
    {
        if ( verify(Username)) {
        new CallAPI(this).execute("serviceRequest","getMyFollowing","user" , Username.getText().toString() );
        Toast.makeText(this, "getFollowing", Toast.LENGTH_LONG).show();
        }
    }


    public void getFollower(View view)
    {
        if ( verify(Username)) {
            new CallAPI(this).execute("serviceRequest","getMyFollower","user" , Username.getText().toString() );
            Toast.makeText(this, "getFollowing", Toast.LENGTH_LONG).show();
        }
    }


    public void getSuggestion(View view)
    {
        if ( verify(Username)) {
            new CallAPI(this).execute("serviceRequest","getSuggestion","user" , Username.getText().toString() );
            Toast.makeText(this, "getSuggestion", Toast.LENGTH_LONG).show();
        }

    }






    public void   serverip(View view)
    {
        if ( verify(ip)) {
            ipServer = ip.getText().toString();
            link ="http://"+ipServer+":9099/Handoop/services.php";
            Toast.makeText(this, "ip changed", Toast.LENGTH_LONG).show();
        }

    }



    public void viewTrendingHashtag(View view)
    {
        new CallAPI(this).execute("serviceRequest","viewTrendingHashtag" );
        Toast.makeText(this, "viewTrendingHashtag", Toast.LENGTH_LONG).show();
    }


    public void relatedTweet(View view)
    {
        //relatedTweet
        if ( verify(keys)) {
            new CallAPI(this).execute("serviceRequest","relatedTweet","keys" , keys.getText().toString() );
            Toast.makeText(this, "getSuggestion", Toast.LENGTH_LONG).show();
        }
        Toast.makeText(this, "relatedTweet", Toast.LENGTH_LONG).show();
    }


    public void showhistory(View view)
    {
        Intent intent = new Intent(HomeActivity.this, ResultActivity.class);
        startActivity(intent);
        finish();
    }

}
