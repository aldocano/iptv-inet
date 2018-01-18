package com.LiveTv;

import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.LiveTv.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class CustomList extends BaseAdapter {
    public ListviewActivity listviewActivity;
    public LayoutInflater layoutInflater;
    ArrayList<DataContainer> title_list = new ArrayList<DataContainer>();
    ViewHolder holder;
    ImageLoader imageLoader = ImageLoader.getInstance();
    public DataBaseHandler dbHelper;
    public String channelName_addToFavourite;
    public String channelImage_addToFavourite;
    public String channelLink_addTofavourite;
    boolean addToFavourite = false, removeFromFavourite = false;
    public PopupMenu popup;

    CustomList(ListviewActivity listviewActivity, ArrayList<DataContainer> title_list) {
        layoutInflater = LayoutInflater.from(listviewActivity);
        this.title_list = title_list;
        imageLoader.init(GetOption.getConfig(listviewActivity));
        this.listviewActivity = listviewActivity;
    }

    @Override
    public int getCount() {
        return title_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            dbHelper = new DataBaseHandler(listviewActivity);
            convertView = layoutInflater.inflate(R.layout.listview_row, null);
            holder = new ViewHolder();
            holder.title_txt = (TextView) convertView.findViewById(R.id.title);
            holder.desc_txt = (TextView) convertView.findViewById(R.id.desc);
            holder.image_img = (ImageView) convertView.findViewById(R.id.image);
            holder.img_more = (ImageView) convertView.findViewById(R.id.img_more);
            holder.rel_listview = (RelativeLayout)convertView.findViewById(R.id.rel_listview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title_txt.setText(title_list.get(position).title);
        holder.desc_txt.setText(title_list.get(position).description);
        if (!(title_list.get(position).image.equals(""))) {

            imageLoader.displayImage(title_list.get(position).image, holder.image_img, GetOption.getFullOption((int) listviewActivity.getResources().getDimension(R.dimen.onezerozero)));
        }
        holder.img_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup = new PopupMenu(listviewActivity, v, Gravity.END);
                channelName_addToFavourite = title_list.get(position).title;
                channelImage_addToFavourite = title_list.get(position).image;
                channelLink_addTofavourite = title_list.get(position).link;
                addToFavourite = false;
                removeFromFavourite = false;

                if (dbHelper.channelAlereadyAddedToFavouriteorNot(channelName_addToFavourite)) {
                    removeFromFavourite = true;
                    popup.getMenuInflater()
                            .inflate(R.menu.removefromfavourite, popup.getMenu());
                } else {
                    popup.getMenuInflater()
                            .inflate(R.menu.popup, popup.getMenu());
                }

                switch (position) {
                    case 0:
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                if (removeFromFavourite){
                                    dbHelper.delete(channelName_addToFavourite);
                                    removeFromFavourite = false;
                                }else{
                                    dbHelper.addChannelToFavourite(channelName_addToFavourite, channelImage_addToFavourite,channelLink_addTofavourite);
                                }
                                return true;
                            }
                        });
                        popup.show(); //showing popup menu
                        break;

                    case 1:
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {

                                if (removeFromFavourite){
                                    dbHelper.delete(channelName_addToFavourite);
                                    removeFromFavourite = false;
                                }else{
                                    dbHelper.addChannelToFavourite(channelName_addToFavourite, channelImage_addToFavourite,channelLink_addTofavourite);
                                }
                                return true;
                            }
                        });
                        popup.show(); //showing popup menu
                        break;
                }
            }
        });
        return convertView;
    }


    static class ViewHolder {
        TextView title_txt;
        TextView desc_txt;
        ImageView image_img, img_more;
        RelativeLayout rel_listview;
    }
}
