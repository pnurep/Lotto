package com.ahchim.android.ritto.model.storesearch;

/**
 * Created by Gold on 2017. 4. 17..
 */

public class Samename
{
    private String keyword;

    private String selected_region;

    public String getKeyword ()
    {
        return keyword;
    }

    public void setKeyword (String keyword)
    {
        this.keyword = keyword;
    }

    public String getSelected_region ()
    {
        return selected_region;
    }

    public void setSelected_region (String selected_region)
    {
        this.selected_region = selected_region;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [keyword = "+keyword+", selected_region = "+selected_region+"]";
    }
}
