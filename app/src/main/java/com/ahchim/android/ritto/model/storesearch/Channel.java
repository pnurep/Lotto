package com.ahchim.android.ritto.model.storesearch;

/**
 * Created by Gold on 2017. 4. 17..
 */

public class Channel
{
    private Item[] item;

    private Info info;

    public Item[] getItem ()
    {
        return item;
    }

    public void setItem (Item[] item)
    {
        this.item = item;
    }

    public Info getInfo ()
    {
        return info;
    }

    public void setInfo (Info info)
    {
        this.info = info;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [item = "+item+", info = "+info+"]";
    }
}
