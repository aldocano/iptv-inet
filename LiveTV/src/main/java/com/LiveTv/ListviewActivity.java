package com.LiveTv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.LiveTv.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class ListviewActivity extends Activity {
    static final String KEY_ITEM = "item";
    public ArrayList<DataContainer> title_list = new ArrayList<DataContainer>();
    public TextView txt_channel_listactivity;
    public int position;
    public RelativeLayout relativeLayout;
    String url = "";
    ListView list;
    String str_head;
    AdView img_add;
    ImageView img_channel;
    ImageView img_icon;
    DataBaseHandler dbHelper;
    ImageLoader imageLoader = ImageLoader.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        dbHelper = new DataBaseHandler(this);
        setupWindowAnimations();
        relativeLayout = (RelativeLayout) findViewById(R.id.rel_layout_listview);
        img_add = (AdView) findViewById(R.id.img_add);
        ImageView back_btn = (ImageView) findViewById(R.id.img_back);
        img_channel = (ImageView) findViewById(R.id.img_channel);
        img_icon = (ImageView) findViewById(R.id.img_icon);
        txt_channel_listactivity = (TextView) findViewById(R.id.txt_channel_listActivity);
        imageLoader.init(GetOption.getConfig(this));

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        img_add.loadAd(adRequest);

        WebAPIHelper web = new WebAPIHelper(Constant.listaddimage, ListviewActivity.this, true);
        web.callRequestGet("http://fillatv.com/fillatv/service/get_banners.php");

        back_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                setupWindowAnimations();
                onBackPressed();
                //ListviewActivity.this.finish();
            }
        });

        Intent intent = getIntent();
        url = intent.getStringExtra("str_link");
        Log.e("url news", url);
        position = intent.getIntExtra("pos", 0);
        str_head = intent.getStringExtra("str_h");
        list = (ListView) findViewById(R.id.listView1);
//        TextView tv = (TextView) findViewById(R.id.txt_head);
//        tv.setText(str_head);
        if (position == 0) {
            img_channel.setBackgroundResource(R.drawable.local_channel);
            img_icon.setBackgroundResource(R.drawable.local_icon);
            txt_channel_listactivity.setText(R.string.local_channel);
        } else if (position == 1) {
            img_channel.setBackgroundResource(R.drawable.news_channel);
            img_icon.setBackgroundResource(R.drawable.news_icon);
            txt_channel_listactivity.setText(R.string.news_channel);
        } else if (position == 2) {
            img_channel.setBackgroundResource(R.drawable.sport_channel);
            img_icon.setBackgroundResource(R.drawable.sport_icon);
            txt_channel_listactivity.setText(R.string.sport_channel);
        } else if (position == 3) {
            img_channel.setBackgroundResource(R.drawable.movie_channel);
            img_icon.setBackgroundResource(R.drawable.movie_icon);
            txt_channel_listactivity.setText(R.string.movie_channel);
        } else if (position == 4) {
            img_channel.setBackgroundResource(R.drawable.music_channel);
            img_icon.setBackgroundResource(R.drawable.music_icon);
            txt_channel_listactivity.setText(R.string.music_channel);
        } else if (position == 5) {
            img_channel.setBackgroundResource(R.drawable.kids_channel);
            img_icon.setBackgroundResource(R.drawable.kids_icon);
            txt_channel_listactivity.setText(R.string.kids_channel);
        } else if (position == 6) {
            img_channel.setBackgroundResource(R.drawable.videos_channel);
            img_icon.setBackgroundResource(R.drawable.video_icon);
            txt_channel_listactivity.setText(R.string.video_channel);
        } else if (position == 7) {
            img_channel.setBackgroundResource(R.drawable.adult_channel);
            img_icon.setBackgroundResource(R.drawable.adult_icon);
            txt_channel_listactivity.setText(R.string.adult_channel);
        } else {
            img_channel.setBackgroundResource(R.drawable.local_channel);
            img_icon.setBackgroundResource(R.drawable.local_icon);
            txt_channel_listactivity.setText(R.string.local_channel);
        }

        new XmlParsing(url).execute(new String[]{null});

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

               // ((Constant) getApplicationContext()).myGlobalArray.add(title_list.get(arg2).title);

                if(!dbHelper.dataAddedAlreadyorNot(title_list.get(arg2).title)){
                    dbHelper.addRecentItem(title_list.get(arg2).title,title_list.get(arg2).image,title_list.get(arg2).link);
                }
                Intent i = new Intent(ListviewActivity.this, VideoViewActivity.class);
                i.putExtra("v_url", title_list.get(arg2));
                startActivity(i);
            }
        });
    }

    private void setupWindowAnimations() {
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(500);
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setupWindowAnimations();
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
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void viewdata(String data) {
        // TODO Auto-generated method stub
        String img;
        try {
            JSONArray j_array = new JSONArray(data);
            for (int i = 0; i < j_array.length(); i++) {
                JSONObject j_obj = j_array.getJSONObject(i);
                System.out.print(j_obj.getString("status"));
                System.out.print(j_obj.getString("id"));
                System.out.print(j_obj.getString("Image"));
                img = j_obj.getString("Image");

//                imageLoader.displayImage("drawable://" + R.mipmap.ic_launcher, viewHeader.img_userImg, GetOption.getFullOption((int) getResources().getDimension(R.dimen.onezerozero)));
               // imageLoader.displayImage(img, img_add, GetOption.getOption());

            }
        } catch (Exception e) {
        }
    }

    public class XmlParsing extends AsyncTask<String, Void, String> {

        String urls;

        public XmlParsing(String urls) {
            this.urls = urls;
        }

        @Override
        protected void onPreExecute() {
            //	pDialog = ProgressDialog.show(ListviewActivity.this, "Fetching Details..", "Please wait...", true);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                XMLParser parser = new XMLParser();
                String xml = parser.getXmlFromUrl(url); // getting XML
                Document doc = parser.getDomElement(xml); // getting DOM element

                NodeList nl = doc.getElementsByTagName(KEY_ITEM);

                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);
                    DataContainer container = new DataContainer();
                    container.title = parser.getValue(e, "title");
                    container.image = parser.getValue(e, "enclosure");
                    container.link = parser.getValue(e, "link");
                    container.description = parser.getValue(e, "description");

                    title_list.add(container);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            list.setAdapter(new CustomList(ListviewActivity.this, title_list));
//			if (pDialog.isShowing())
//				pDialog.dismiss();

        }

    }
}
