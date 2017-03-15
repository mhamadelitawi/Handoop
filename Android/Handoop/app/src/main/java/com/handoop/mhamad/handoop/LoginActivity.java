package com.handoop.mhamad.handoop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import static com.handoop.mhamad.handoop.Tools.Utils.verify;

public class LoginActivity extends AppCompatActivity {


    TextView username;
    TextView password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username = ((TextView) findViewById(R.id.username));
        password = ((TextView) findViewById(R.id.password));



    }

    public void SignIn(View view)
    {
        if ( verify(username)  && verify(password))
        {


        }

    }


    public void Register(View view)
    {
        startActivity(new Intent(this, SignUpActivity.class));

    }




}
