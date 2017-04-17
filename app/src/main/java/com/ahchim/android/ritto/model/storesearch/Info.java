package com.ahchim.android.ritto.model.storesearch;

/**
 * Created by Gold on 2017. 4. 17..
 */

public class Info
{
    private Samename samename;

    private String count;

    private String totalCount;

    private String page;

    public Samename getSamename ()
    {
        return samename;
    }

    public void setSamename (Samename samename)
    {
        this.samename = samename;
    }

    public String getCount ()
    {
        return count;
    }

    public void setCount (String count)
    {
        this.count = count;
    }

    public String getTotalCount ()
    {
        return totalCount;
    }

    public void setTotalCount (String totalCount)
    {
        this.totalCount = totalCount;
    }

    public String getPage ()
    {
        return page;
    }

    public void setPage (String page)
    {
        this.page = page;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [samename = "+samename+", count = "+count+", totalCount = "+totalCount+", page = "+page+"]";
    }
}
