package com.LiveTv.fragment;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.LiveTv.adapter.CategoryFragmentAdapter;
import com.LiveTv.Constant;
import com.LiveTv.DataContainer;
import com.LiveTv.XMLParser;
import com.LiveTv.R;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.LiveTv.adapter.CategoryFragmentAdapter.KEY_ITEM;

/**
 * Created by creativeinfoway2 on 02/12/16.
 */

public class CategoryFragment extends Fragment {

    public ArrayList<DataContainer> title_list = new ArrayList<>();
    public RecyclerView recyclerView;
    ArrayList<Integer> img = new ArrayList<>();
    ArrayList<Integer> img1 = new ArrayList<>();
    ArrayList<String> channel_name = new ArrayList<>();
    boolean callFristtime ;
    SharedPreferences prefs = null;
    CategoryFragmentAdapter adapter;
    Constant constant = new Constant();
    public CategoryFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_view, container, false);

        String url = "http://fillatv.com/fillatv/service/get_channels_list.php";
        prefs =getActivity().getSharedPreferences("com.fillatv.live", MODE_PRIVATE);
        img.add(R.drawable.local_channel);
        img.add(R.drawable.news_channel);
        img.add(R.drawable.sport_channel);
        img.add(R.drawable.movie_channel);
        img.add(R.drawable.music_channel);
        img.add(R.drawable.kids_channel);
        img.add(R.drawable.videos_channel);
        img.add(R.drawable.adult_channel);

        img1.add(R.drawable.local_icon);
        img1.add(R.drawable.news_icon);
        img1.add(R.drawable.sport_icon);
        img1.add(R.drawable.movie_icon);
        img1.add(R.drawable.music_icon);
        img1.add(R.drawable.kids_icon);
        img1.add(R.drawable.video_icon);
        img1.add(R.drawable.adult_icon);

        channel_name.add("LOCAL CHANNELS");
        channel_name.add("NEWS CHANNELS");
        channel_name.add("SPORTS CHANNELS");
        channel_name.add("MOVIE CHANNELS");
        channel_name.add("MUSIC CHANNELS");
        channel_name.add("KIDS CHANNELS");
        channel_name.add("VIDEOS CHANNELS");
        channel_name.add("ADULT CHANNELS");

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences prefs =getActivity().getSharedPreferences("globalArraysize", 0);
        boolean globalArraysize = prefs.getBoolean("globalArraysize", false);
        boolean flag = prefs.getBoolean("flag",false);
        if(!globalArraysize){
            if(!flag){
                new XmlParsing(url).execute(new String[]{null});
                callFristtime = false;
         //       pd.show();
            }
        }else{
            adapter=new CategoryFragmentAdapter(img,img1,channel_name,getActivity(),constant.myGlobalArray);
            recyclerView.setAdapter(adapter);
        }

        return v;
    }

    class XmlParsing extends AsyncTask<String, Void, String> {

        String urls;

        public XmlParsing(String urls) {
            this.urls = urls;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                XMLParser parser = new XMLParser();

                String xml = parser.getXmlFromUrl(urls); // getting XML
                Document doc = parser.getDomElement(xml); // getting DOM element

                NodeList nl = doc.getElementsByTagName(KEY_ITEM);

                for (int i = 0; i < nl.getLength(); i++) {
                    Element e = (Element) nl.item(i);
                    DataContainer container = new DataContainer();
                    container.count = parser.getValue(e, "count");
                    container.channel_name = parser.getValue(e, "Channels_Name");

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
            constant.myGlobalArray.add(title_list);
            adapter=new CategoryFragmentAdapter(img,img1,channel_name,getActivity(),constant.myGlobalArray);
            recyclerView.setAdapter(adapter);

        }
    }
}
