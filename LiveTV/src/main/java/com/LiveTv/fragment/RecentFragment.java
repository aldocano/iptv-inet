package com.LiveTv.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.LiveTv.DataBaseHandler;
import com.LiveTv.DataContainer;
import com.LiveTv.adapter.RecentChannelListAdapter;
import com.LiveTv.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class RecentFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public DataBaseHandler dbHelper;
    public ArrayList<DataContainer>  channel_list;
    public RecyclerView mRecycler;
    public RecentChannelListAdapter mRecentChannelListAdapter;
    public RecyclerView.LayoutManager mLayout;
    SharedPreferences prefs = null;

    public RecentFragment() {
    }

    public static RecentFragment newInstance(String param1, String param2) {
        RecentFragment fragment = new RecentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        prefs =getActivity().getSharedPreferences("com.fillatv.live", MODE_PRIVATE);
        mRecycler = (RecyclerView) view.findViewById(R.id.rec_recent);
        mLayout = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayout);
        return  view;
    }
    @Override
    public void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }else{

            dbHelper = new DataBaseHandler(getActivity());
            channel_list = new ArrayList<>(dbHelper.getRecentItem());
            mRecycler.setAdapter(new RecentChannelListAdapter(getActivity(), channel_list));

        }



    }
}
