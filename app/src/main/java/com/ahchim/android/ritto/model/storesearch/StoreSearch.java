package com.ahchim.android.ritto.model.storesearch;

/**
 * Created by Gold on 2017. 4. 17..
 */

public class StoreSearch
{
    private Channel channel;

    public Channel getChannel ()
    {
        return channel;
    }

    public void setChannel (Channel channel)
    {
        this.channel = channel;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [channel = "+channel+"]";
    }
}
