package com.LiveTv;

import java.io.Serializable;
import java.util.Comparator;

public class DataContainer implements Serializable, Comparator<DataContainer> {
    private static final long serialVersionUID = 1L;

    public String title;
    public String link;
    public String description;
    public String image;
    public String count;
    public String channel_name;


    @Override
    public int compare(DataContainer lhs, DataContainer rhs) {
        return 0;
    }
}
