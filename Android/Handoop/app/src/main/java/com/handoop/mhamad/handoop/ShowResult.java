package com.handoop.mhamad.handoop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.handoop.mhamad.handoop.Adapters.PreviewAdapter;

import java.util.ArrayList;

public class ShowResult extends AppCompatActivity {


    String guid ;
    String[] previews;
    String task;
    ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result);
        listview = (ListView)findViewById(R.id.listresult);

        getInfoFromIntent();
        String z = "";
        listview.setAdapter(new PreviewAdapter(this , previews));
    }




    void getInfoFromIntent()
    {
        Bundle b = getIntent().getExtras();
        if(b!=null)
        {
            guid =  (String) b.get("guid");
            task =  (String) b.get("task");

            previews = ((String) b.get("preview")).split("\\$\\!\\$\\!\\$");

        }
    }





}
