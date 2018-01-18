package com.LiveTv;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class Constant extends Application {

    public static final String base_url ="cctv.pop-al.com:19800";

    public final static int getaddimage = 50;
    public final static int listaddimage = 51;
    public final static int nextaddimage = 52;
    public final static int videoaddimage = 53;
    public final static int getchannelcount = 54;

    public static String webserver_path = "http://cctv.pop-al.com:19800";


    public List<ArrayList<DataContainer>> myGlobalArray = null;

    public Constant() {
        myGlobalArray = new ArrayList();
    }
}