package com.LiveTv;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.LiveTv.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;

public class WebAPIHelper {

    public boolean isloading;
    ProgressDialog pd;
    int requestNumber;
    MainActivity mainactivity;
    ListviewActivity listviewactivity;
    VideoViewActivity videoviewactivity;
    Bitmap bitmap;
    AsyncHttpClient client = new AsyncHttpClient();

    public WebAPIHelper(int requestno, MainActivity mainactivity, boolean isloading) {
        this.requestNumber = requestno;
        this.mainactivity = mainactivity;
        this.isloading = isloading;

        pd = new ProgressDialog(mainactivity, R.style.dialogTheme);
        pd.setCancelable(true);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    }

    public WebAPIHelper(int requestno, ListviewActivity listactivity, boolean isloading) {
        this.isloading = isloading;
        this.requestNumber = requestno;
        this.listviewactivity = listactivity;
        pd = new ProgressDialog(listviewactivity, R.style.dialogTheme);
        pd.setCancelable(true);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    }

    public WebAPIHelper(int requestno, VideoViewActivity videoviewactivity, boolean isloading) {
        this.isloading = isloading;
        this.requestNumber = requestno;
        this.videoviewactivity = videoviewactivity;

        pd = new ProgressDialog(videoviewactivity, R.style.dialogTheme);
        pd.setCancelable(true);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
    }

    public void callRequestGet(String Url) {
        Log.e("url ", Url + "");
        client.setTimeout(20000);
        client.get(("" + Url).replace("  ", "%20"), new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                if (isloading) {
                    pd.show();
                } else {
                    pd.dismiss();
                }
                super.onStart();
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
                try {
                    switch (requestNumber) {
                        case Constant.getaddimage:
                            mainactivity.viewdata(new String(arg2, "UTF-8"));
                            break;
//                        case Constant.nextaddimage:
//                            filltvnextactivity.viewdata(new String(arg2, "UTF-8"));
//                            break;
                        case Constant.listaddimage:
                            listviewactivity.viewdata(new String(arg2, "UTF-8"));
                            break;
                        case Constant.videoaddimage:
                            videoviewactivity.viewdata(new String(arg2, "UTF-8"));

                        default:
                            break;
                    }

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
				if (pd.isShowing() && pd != null){
                    pd.dismiss();
                }else{
                    pd.dismiss();
                }

            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				if (pd.isShowing())
					pd.dismiss();
                Toast.makeText(mainactivity, "Make sure have have connected to Internet ..! ", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
