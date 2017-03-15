package com.handoop.mhamad.handoop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.handoop.mhamad.handoop.Tools.CallAPI;
import com.google.firebase.iid.FirebaseInstanceId;

import static com.handoop.mhamad.handoop.Tools.Utils.verify;

public class SignUpActivity extends AppCompatActivity {

    TextView firstName;
    TextView lastName;
    TextView username;

    TextView password;
    TextView vpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstName = (TextView)findViewById(R.id.firstname);
        lastName = (TextView)findViewById(R.id.lastname);
        username = (TextView)findViewById(R.id.username);
        password = (TextView)findViewById(R.id.password);
        vpassword = (TextView)findViewById(R.id.retypepassword);

    }





    public void SignUp(View view)
    {

      String us = username.getText().toString();
         String fi = firstName.getText().toString();
         String la = lastName.getText().toString();
         String pa = password.getText().toString();
        String pushId =      FirebaseInstanceId.getInstance().getToken();



        if ( verify(firstName)  && verify(lastName)  && verify(username)  && verify(password)  && verify(vpassword)  )
        {
            if (password.getText().toString().equals( vpassword.getText().toString())  )
            {

                new CallAPI(this) {

                    @Override
                    protected void onPostExecute(String result)
                    {
                        if(result == null)
                        {
                            Toast.makeText(context, "Connecion error", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (result.equals("-1"))
                        {
                            Toast.makeText(context, "Username alreaady exist", Toast.LENGTH_LONG).show();
                            return;
                        }
                        Toast.makeText(context, "Registred", Toast.LENGTH_LONG).show();

                    }

                }.execute("serviceRequest","register","name",fi,"lastName",la,"username",us,"password",pa,"pushId", pushId);


            }
            else
            {
                password.setError("should be the same");
                password.setText("");
                vpassword.setError("should be the same");
                vpassword.setText("");
            }
        }

    }











}
