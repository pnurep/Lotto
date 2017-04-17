package com.ahchim.android.ritto.util;

import java.util.Comparator;

/**
 * Created by Gold on 2017. 4. 14..
 */

public class Ascending implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
        return o1.compareTo(o2);
    }
}
