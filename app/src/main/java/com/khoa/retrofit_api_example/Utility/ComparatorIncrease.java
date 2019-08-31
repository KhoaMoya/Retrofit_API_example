package com.khoa.retrofit_api_example.Utility;

import com.github.mikephil.charting.data.Entry;

import java.util.Comparator;

public class ComparatorIncrease implements Comparator<Entry> {
    @Override
    public int compare(Entry o1, Entry o2) {
        float f1 = o1.getY();
        float f2 = o2.getY();
        if(f1>f2) return 1;
        else if(f1<f2) return -1;
        else return 0;
    }
}
