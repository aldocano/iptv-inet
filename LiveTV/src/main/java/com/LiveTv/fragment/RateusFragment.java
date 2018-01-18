package com.LiveTv.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.LiveTv.R;

/**
 * Created by creativeinfoway2 on 25/11/16.
 */
public class RateusFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from moreappfragmentent.xml
        View view = inflater.inflate(R.layout.rateusfragment, container, false);
        launchMarket();
        return view;
    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" +getActivity().getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
}
