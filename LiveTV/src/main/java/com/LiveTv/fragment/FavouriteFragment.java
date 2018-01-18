package com.LiveTv.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.LiveTv.DataBaseHandler;
import com.LiveTv.DataContainer;
import com.LiveTv.adapter.FavouriteChannelListAdapter;
import com.LiveTv.R;

import java.util.ArrayList;

public class FavouriteFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    public DataBaseHandler dbHelper;
    public ArrayList<DataContainer> array_favourite_channel;
    public RecyclerView mRecycler;
    public RecyclerView.LayoutManager mLayout;


    public FavouriteFragment() {
        // Required empty public constructor
    }
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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
        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);

        mRecycler = (RecyclerView) view.findViewById(R.id.rec_favourite);
        mLayout = new LinearLayoutManager(getActivity());
        mRecycler.setLayoutManager(mLayout);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        dbHelper = new DataBaseHandler(getActivity());
        array_favourite_channel = new ArrayList<>(dbHelper.getFavouriteItem());
        mRecycler.setAdapter(new FavouriteChannelListAdapter(getActivity(), array_favourite_channel));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
