package com.handoop.mhamad.handoop;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;


import com.handoop.mhamad.handoop.Adapters.ResultsExpandableListAdapter;
import com.handoop.mhamad.handoop.Models.Result;
import com.handoop.mhamad.handoop.Tools.MySQLLite;

import java.util.ArrayList;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {

    public ArrayList<Result> results ;
    public ResultsExpandableListAdapter myAdapter;
    public ExpandableListView ExpandListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //results = new ArrayList<>();
       // createStatic();


        MySQLLite ms = new MySQLLite(this);
        results =  ms.getAllHistories();

        ExpandListView = (ExpandableListView) findViewById(R.id.exp_list);
        myAdapter = new ResultsExpandableListAdapter(this , results);
        ExpandListView.setAdapter(myAdapter);
        ExpandListView.setGroupIndicator(null);
        askForPermission();

    }




    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }



    public void askForPermission()
    {
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!checkIfAlreadyhavePermission()) {
                requestForSpecificPermission();
            }
        }
    }



    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivityForResult(myIntent, 0);
        finish();
        return true;

    }



    public void createStatic()
    {
        results.add(new Result("guid0" , "task0" , "title0" ,   "preview0", "pathresult0" , new Date().getTime() + ""));
        results.add(new Result("guid1" , "task1" , "title1" ,   "preview1", "pathresult1" , new Date().getTime() + ""));
        results.add(new Result("guid2" , "task2" , "title2" ,   "preview2", "pathresult2", new Date().getTime() + ""));
        results.add(new Result("guid3" , "task3" , "title3" ,   "preview3", "pathresult3", new Date().getTime() + ""));
        results.add(new Result("guid4" , "task4" , "title4" ,   "preview4", "pathresult4" , new Date().getTime() + ""));
        results.add(new Result("guid5" , "task5" , "title5" ,   "preview5", "pathresult5", new Date().getTime() + ""));
        results.add(new Result("guid6" , "task6" , "title6" ,   "preview6", "pathresult6" , new Date().getTime() + ""));
        results.add(new Result("guid7" , "task7" , "title7" ,   "preview7", "pathresult7" , new Date().getTime() + ""));
        results.add(new Result("guid8" , "task8" , "title8" ,   "preview8", "pathresult8", new Date().getTime() + ""));
    }


    public void removeAnElement(int position)
    {
        results.remove(position);
        myAdapter = new ResultsExpandableListAdapter(this , results);
        ExpandListView.setAdapter(myAdapter);
    }




}
