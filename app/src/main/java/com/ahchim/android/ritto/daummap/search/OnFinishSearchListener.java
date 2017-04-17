package com.ahchim.android.ritto.daummap.search;

/**
 * Created by Gold on 2017. 4. 1..
 */

import java.util.List;

public interface OnFinishSearchListener {
    public void onSuccess(List<Item> itemList);
    public void onFail();
}
