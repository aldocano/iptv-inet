package com.LiveTv.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.LiveTv.DataContainer;
import com.LiveTv.GetOption;
import com.LiveTv.VideoViewActivity;
import com.LiveTv.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by creativeinfoway2 on 02/12/16.
 */

public class RecentChannelListAdapter extends RecyclerView.Adapter<RecentChannelListAdapter.ViewHolder> {

    FragmentActivity fragmentActivity;
    ArrayList<DataContainer> title_list;
    public LayoutInflater layoutInflater;
    ViewHolder holder;
    ImageLoader imageLoader = ImageLoader.getInstance();


    public RecentChannelListAdapter(FragmentActivity fragmentActivity, ArrayList<DataContainer> title_list) {
        this.fragmentActivity = fragmentActivity;
        this.title_list = title_list;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fragmentActivity).inflate(R.layout.listview_recent, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.title_txt.setText(title_list.get(position).title);
        Log.e("url ", title_list.get(position).link+"");
        if (!(title_list.get(position).image.equals(""))) {

            imageLoader.displayImage(title_list.get(position).image, holder.image_img, GetOption.getFullOption((int)fragmentActivity.getResources().getDimension(R.dimen.onezerozero)));
//			picasso.load(title_list.get(position).image).noFade().into(holder.image_img);
        }

        holder.rel_recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(fragmentActivity, VideoViewActivity.class);
                i.putExtra("v_url", title_list.get(position));
                fragmentActivity.startActivity(i);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return title_list.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title_txt;
        TextView desc_txt;
        ImageView image_img;
        RelativeLayout rel_recent;
        public ViewHolder(View itemView) {
            super(itemView);

            title_txt = (TextView) itemView.findViewById(R.id.txt_recent);
            image_img = (ImageView) itemView.findViewById(R.id.img_recent);
            rel_recent = (RelativeLayout)itemView.findViewById(R.id.rel_recent);
        }
    }
}
