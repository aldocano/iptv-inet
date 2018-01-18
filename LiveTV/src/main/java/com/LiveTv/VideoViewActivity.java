package com.LiveTv;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.LiveTv.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.LiveTv.R.id.webview;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class VideoViewActivity extends Activity {

    public ProgressDialog pd;
    boolean isStart;
    VideoView mVideoView;
    android.widget.VideoView videoView;
    ProgressBar pb;
    TextView downloadRateView, loadRateView;
    String videourl = "";
    AdView img_add;
    ImageLoader imageLoader = ImageLoader.getInstance();
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupWindowAnimations();

        setContentView(R.layout.video_view);

        img_add = (AdView) findViewById(R.id.img_add);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        img_add.loadAd(adRequest);
        WebAPIHelper web = new WebAPIHelper(Constant.videoaddimage, VideoViewActivity.this, true);
        web.callRequestGet("http://fillatv.com/fillatv/service/get_banners.php");
        videoView = (android.widget.VideoView) findViewById(R.id.surface_view);

        webView = (WebView) findViewById(webview);
        videoView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {return false;
            }
        });


        ImageView btn_back = (ImageView) findViewById(R.id.back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (videoView.isPlaying()) {
                    videoView.stopPlayback();
                }
                setupWindowAnimations();
                onBackPressed();

            }
        });


        Intent intent = getIntent();
        videourl = ((DataContainer) intent.getSerializableExtra("v_url")).link;
        if (videourl.contains(".html")) {

            webView.setVisibility(View.VISIBLE);
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

            final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

            final ProgressDialog progressBar = ProgressDialog.show(VideoViewActivity.this, "WebView Example", "Loading...");

            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Log.i(TAG, "Processing webview url click...");
                    view.loadUrl(url);
                    return true;
                }

                public void onPageFinished(WebView view, String url) {
                    Log.i(TAG, "Finished loading URL: " + url);
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e(TAG, "Error: " + description);
                    Toast.makeText(VideoViewActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
                    alertDialog.show();
                }
            });
            webView.loadUrl(videourl);
        }

        PlayVideo();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (img_add != null) {
            img_add.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if (img_add != null) {
            img_add.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (img_add != null) {
            img_add.pause();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setupWindowAnimations();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setupWindowAnimations() {
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(5000);
        getWindow().setExitTransition(slideTransition);
    }
    private void PlayVideo() {

        Log.e("video url", videourl);
        Uri uri = Uri.parse(videourl);
        android.widget.MediaController mediaController = new android.widget.MediaController(this);
        mediaController.setAnchorView(videoView);
        mediaController.setMediaPlayer(videoView);
        videoView.requestFocus();
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(uri);

        pd = new ProgressDialog(VideoViewActivity.this, R.style.dialogTheme);
        pd.setCancelable(true);
        pd.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pd.show();
        videoView.setOnPreparedListener(new android.media.MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(android.media.MediaPlayer mp) {
                videoView.start();
                pd.dismiss();
            }
        });


        Handler handler = null;
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                pd.cancel();
                pd.dismiss();
            }
        }, 5000);


        videoView.setOnErrorListener(new android.media.MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(android.media.MediaPlayer mp, int what, int extra) {
                Toast.makeText(VideoViewActivity.this, "Error in Loading channel Please try after some time ", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                VideoViewActivity.this.finish();
                return true;
            }
//                switch (what) {
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                        if (mVideoView.isPlaying()) {
//                            mVideoView.pause();
//                            isStart = true;
//                            pd.show();
//                            downloadRateView.setVisibility(View.VISIBLE);
//                            loadRateView.setVisibility(View.VISIBLE);
//
//                        }
//                        break;
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                        if (isStart) {
//                            mVideoView.start();
//                            pd.dismiss();
//                            downloadRateView.setVisibility(View.GONE);
//                            loadRateView.setVisibility(View.GONE);
//                        }
//                        break;
//                    case MediaPlayer.MEDIA_INFO_DOWNLOAD_RATE_CHANGED:
//                        downloadRateView.setText("  " + extra + "kb/s" + "  ");
//                        break;
//                }
//                return true;
//            }
        });
    }



    public void viewdata(String data) {
        String img;
        try {
            JSONArray j_array = new JSONArray(data);
            for (int i = 0; i < j_array.length(); i++) {
                JSONObject j_obj = j_array.getJSONObject(i);
                System.out.print(j_obj.getString("status"));
                System.out.print(j_obj.getString("id"));
                System.out.print(j_obj.getString("Image"));
                img = j_obj.getString("Image");

              //  imageLoader.displayImage(img, img_add, GetOption.getOption());

            }
        } catch (Exception e) {
        }
    }

}
