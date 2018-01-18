package com.LiveTv.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.LiveTv.R;

/**
 * Created by creativeinfoway2 on 25/11/16.
 */
public class MoreappFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get the view from moreappfragment.xmlxml
        View view = inflater.inflate(R.layout.moreappfragment, container, false);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Intent i = new Intent(getActivity(),MainActivity.class);
//        startActivity(i);

    }
}
