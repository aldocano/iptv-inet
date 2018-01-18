package com.LiveTv.adapter;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.LiveTv.DataContainer;
import com.LiveTv.GetOption;
import com.LiveTv.VideoViewActivity;
import com.LiveTv.fragment.RecentFragment;
import com.LiveTv.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by creativeinfoway2 on 03/12/16.
 */

public class FavouriteChannelListAdapter extends RecyclerView.Adapter<FavouriteChannelListAdapter.ViewHolder> {

    FragmentActivity fragmentActivity;
    ArrayList<DataContainer> title_list;
    public LayoutInflater layoutInflater;
    RecentChannelListAdapter.ViewHolder holder;
    ImageLoader imageLoader = ImageLoader.getInstance();
    RecentFragment recentFragment;

    public FavouriteChannelListAdapter(FragmentActivity fragmentActivity, ArrayList<DataContainer> title_list) {
        this.fragmentActivity = fragmentActivity;
        this.title_list = title_list;

    }
    @Override
    public FavouriteChannelListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(fragmentActivity).inflate(R.layout.listview_recent, null);
        return new FavouriteChannelListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavouriteChannelListAdapter.ViewHolder holder, final int position) {

        holder.title_txt.setText(title_list.get(position).title);
        if (!(title_list.get(position).image.equals(""))) {

            imageLoader.displayImage(title_list.get(position).image, holder.image_img, GetOption.getFullOption((int)fragmentActivity.getResources().getDimension(R.dimen.onezerozero)));
//			picasso.load(title_list.get(position).image).noFade().into(holder.image_img);
        }
        holder.rel_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(fragmentActivity, VideoViewActivity.class);
                i.putExtra("v_url", title_list.get(position));
                fragmentActivity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return title_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_txt;
        RelativeLayout rel_favorite;
        ImageView image_img;
        public ViewHolder(View itemView) {
            super(itemView);
            title_txt = (TextView) itemView.findViewById(R.id.txt_recent);
            image_img = (ImageView) itemView.findViewById(R.id.img_recent);
            rel_favorite = (RelativeLayout)itemView.findViewById(R.id.rel_recent);
        }
    }
}
