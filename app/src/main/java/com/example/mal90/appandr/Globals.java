package com.example.mal90.appandr;

import android.app.Application;

/**
 * Created by mal90 on 17/06/2017.
 */

public class Globals extends Application{

    private String user_key = "/?user_key=0000";
    private String url = "http://192.168.0.134:8000/";


    public String getUrl() {
        return url;
    }
    public String getUser_key(){
        return user_key;
    }


}
