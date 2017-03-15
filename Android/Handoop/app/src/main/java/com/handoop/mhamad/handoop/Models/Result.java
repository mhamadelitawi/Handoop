package com.handoop.mhamad.handoop.Models;

import com.handoop.mhamad.handoop.Tools.Utils;

/**
 * Created by Mhamad on 13-Feb-17.
 */

public class Result {

    public String guid;
    public String task;
    public String title;
    public String preview;
    public String pathresult;
    public String date;
    public String time;

    public Result(String guid, String task, String title, String preview, String pathresult, String date, String time) {
        this.guid = guid;
        this.task = task;
        this.title = title;
        this.preview = preview;
        this.pathresult = pathresult;
        this.date = date;
        this.time = time;
    }

    public Result(String guid, String task, String title, String preview, String pathresult , String date) {
        this.guid = guid;
        this.task = task;
        this.title = title;
        this.preview = preview;
        this.pathresult = pathresult;
        String t[] = Utils.formatDate(date).split("#");
        this.date = t[0];
        this.time = t[1];
    }


    public Result(String response)
    {
        //"&#$#&"
       String spliter = "&#$#&";
        //"\\&\\#\\$\\#\\&"
        String t[] = response.split("\\&\\#\\$\\#\\&");
        this.guid = t[0];
        this.task =  t[1];
        this.title =  t[2];
        this.preview =  t[3];
        this.pathresult =  t[4];
        String t1[] = Utils.formatDate( t[5]).split("#");
        this.date = t1[0];
        this.time = t1[1];
    }


    public Result(){}

}
