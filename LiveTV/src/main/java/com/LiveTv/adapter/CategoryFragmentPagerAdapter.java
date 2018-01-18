package com.LiveTv.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.LiveTv.DataContainer;
import com.LiveTv.fragment.CategoryFragment;
import com.LiveTv.fragment.FavouriteFragment;
import com.LiveTv.fragment.RecentFragment;

import java.util.ArrayList;

/**
 * Created by creativeinfoway2 on 02/12/16.
 */

public class CategoryFragmentPagerAdapter extends FragmentStatePagerAdapter {
    public ArrayList<DataContainer> title_list;
    public CategoryFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        String st1 = "", st2 = "";

        switch (position) {
            case 0:
                CategoryFragment fragment = new CategoryFragment();
                Bundle args = new Bundle();
                args.putInt("TAB POSITION", position);
                fragment.setArguments(args);
                return fragment;
            case 1:
                RecentFragment fragment2 = new RecentFragment();
                Bundle args1 = new Bundle();
                args1.putInt("TAB POSITION", position);
                fragment2.setArguments(args1);
                return fragment2;
            case 2:
                FavouriteFragment fragment3 = new FavouriteFragment();
                Bundle args2 = new Bundle();
                args2.putInt("TAB POSITION", position);
                fragment3.setArguments(args2);
                return fragment3;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Home";
        } else if (position == 1) {
            return "Recent";
        } else if (position == 2) {
            return "favourites";
        }
        return "Home";
    }
}
