package com.LiveTv.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.LiveTv.Constant;
import com.LiveTv.DataContainer;
import com.LiveTv.ListviewActivity;
import com.LiveTv.TransitionHelper;
import com.LiveTv.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by creativeinfoway2 on 25/11/16.
 */

public class CategoryFragmentAdapter extends RecyclerView.Adapter<CategoryFragmentAdapter.ViewHolder> {

    public Activity mainActivity;
    public FragmentActivity fragmentActivity;
    ArrayList<Integer> imgs, img1;
    ArrayList<String> channel_name;
    ArrayList<DataContainer> title_list;
    public ArrayList<String> added_api_channel;
    public ArrayList<String> array_for_count = new ArrayList<>();
    public ArrayList<String> static_channel_name = new ArrayList<>();
    String str, url;
    private List<String> mItems;
    String[] static_channel;
    String[] api_channel;
    public static final String KEY_ITEM = "item";
    List<ArrayList<DataContainer>> myGlobalArray;
    ProgressDialog pd;

    public CategoryFragmentAdapter(ArrayList<Integer> img, ArrayList<Integer> img1, ArrayList<String> channel_name, Activity mainActivity, ArrayList<DataContainer> title_list) {
        imgs = img;
        this.img1 = img1;
        this.mainActivity = mainActivity;
        this.channel_name = channel_name;
        this.title_list = title_list;
        //this.list_channel = list_channel;
    }

    public CategoryFragmentAdapter(ArrayList<Integer> img, ArrayList<Integer> img1, ArrayList<String> channel_name, FragmentActivity fragmentActivity, List<ArrayList<DataContainer>> myGlobalArray) {
        imgs = img;
        this.img1 = img1;
        this.fragmentActivity = fragmentActivity;
        this.channel_name = channel_name;
        this.myGlobalArray = myGlobalArray;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_categorylist, viewGroup, false);
        return new ViewHolder(v);
    }

    public void addItem(List<DataContainer>list)
    {
        title_list.clear();
        title_list.addAll(list);
       // Log.e("titlelist",title_list+""+ title_list.size()+"");
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Integer img = imgs.get(i);
        Integer img_icon = img1.get(i);
        if (i == 7) {
            viewHolder.txt_adult_channel_warnning.setVisibility(View.VISIBLE);
        } else {
            viewHolder.txt_adult_channel_warnning.setVisibility(View.INVISIBLE);
        }

        static_channel = new String[8];
        api_channel = new String[8];

        added_api_channel = new ArrayList<>();
        for (int j = 0 ; j < imgs.size() ; j++){
            array_for_count.add(myGlobalArray.get(0).get(j).count);
            static_channel_name.add(channel_name.get(j).toLowerCase()); ;
            added_api_channel.add(myGlobalArray.get(0).get(j).channel_name.toLowerCase());
        }

        SharedPreferences sp = fragmentActivity.getSharedPreferences("globalArraysize", 0);
        SharedPreferences.Editor ed = sp.edit();
        ed.putBoolean("globalArraysize", true);

        for(int k = i ; k <= i ; k ++){
            for(int l = 0 ; l < 8 ; l++){
                if (static_channel_name.get(i).equals(added_api_channel.get(l))){
                    viewHolder.txt_channel_count.setText(array_for_count.get(l) +" channels");
                }
            }
        }
//        if(title_list.size()!= 0){
//            viewHolder.txt_channel_count.setText(title_list.get(i).count + "channels");
//        }

        viewHolder.txt_channel.setText(channel_name.get(i));
        //    viewHolder.mTextView.setText(item);
        //  viewHolder.txt_channel.setText(channel);
        viewHolder.img_channel.setBackgroundResource(img);
        viewHolder.img_icon.setBackgroundResource(img_icon);
        viewHolder.img_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (i) {

                    case 0:
                        str = "LOCAL CHANNELS";
                        url = Constant.webserver_path + "get_local_channels.php";
                        // onstartIntent(str,url,viewHolder.img_channel);
                        transitionToActivity(ListviewActivity.class, viewHolder, i);
                        break;
                    case 1:
                        str = "NEWS CHANNELS";
                        url = Constant.webserver_path + "get_news_channel.php";
                        transitionToActivity(ListviewActivity.class, viewHolder, i);
                        //onstartIntent(str,url,viewHolder.img_channel,i);
                        break;
                    case 2:
                        str = "SPORTS CHANNELS";
                        url = Constant.webserver_path + "get_sports_channel.php";
                        transitionToActivity(ListviewActivity.class, viewHolder, i);
                        break;
                    case 3:
                        str = "MOVIE CHANNELS";
                        url = Constant.webserver_path + "get_movie_channels.php";
                        transitionToActivity(ListviewActivity.class, viewHolder, i);
                        break;
                    case 4: {
                        str = "MUSIC CHANNELS";
                        url = Constant.webserver_path + "get_music_channels.php";
                        transitionToActivity(ListviewActivity.class, viewHolder, i);
                    }
                    break;
                    case 5: {
                        str = "KIDS CHANNELS";
                        url = Constant.webserver_path + "get_kids_channels.php";
                        transitionToActivity(ListviewActivity.class, viewHolder, i);
                    }
                    break;
                    case 6: {
                        str = "VIDEOS ON DEMAND";
                        url = Constant.webserver_path + "get_videos_on_demand.php";
                        transitionToActivity(ListviewActivity.class, viewHolder, i);
                    }
                    break;
                    case 7: {
                        str = "ADULT";
                        url = Constant.webserver_path + "get_entertainment.php";
                        transitionToActivity(ListviewActivity.class, viewHolder, i);
                        break;
                    }
                }
            }
        });
    }

    private void transitionToActivity(Class target, ViewHolder viewHolder, int i) {
        if (check_network()) {

            Intent intent = new Intent(fragmentActivity, ListviewActivity.class);
            intent.putExtra("str_link", url);
            intent.putExtra("str_h", str);
            intent.putExtra("pos", i);

            final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(fragmentActivity, false,
                    new Pair<>(viewHolder.img_channel, "profile"), new Pair<>(viewHolder.img_icon, "icon"), new Pair<>(viewHolder.txt_channel, "channel_name"));

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(fragmentActivity, pairs);
            fragmentActivity.startActivity(intent, options.toBundle());
        } else {
            Toast.makeText(fragmentActivity, "Check Your Network.", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean check_network() {
        ConnectivityManager connectivity = (ConnectivityManager) fragmentActivity.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();

            if (info == null)
                return false;
            else if (!info.isConnected())
                return false;
            else return info.isAvailable();

        }
        return false;
    }

    @Override
    public int getItemCount() {
        //  Log.e("itemsize", list_channel.size() + "");
        return imgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTextView, txt_channel, txt_adult_channel_warnning, txt_channel_count;
        ImageView img_channel, img_icon;


        ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.list_item);
            img_channel = (ImageView) v.findViewById(R.id.img_channel);
            img_icon = (ImageView) v.findViewById(R.id.img_icon);
            txt_channel = (TextView) v.findViewById(R.id.txt_channel);
            txt_adult_channel_warnning = (TextView) v.findViewById(R.id.txt_adult_channel_warnning);
            txt_channel_count = (TextView) v.findViewById(R.id.txt_channel_count);

        }
    }

}