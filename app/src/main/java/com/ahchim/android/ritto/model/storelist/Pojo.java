package com.ahchim.android.ritto.model.storelist;

import com.ahchim.android.ritto.model.storelist.Arr;

/**
 * Created by Gold on 2017. 4. 13..
 */

public class Pojo
{
    private String pageIsPrev;

    private String pageIsNext;

    private Arr[] arr;

    private String pageStart;

    private String nowPage;

    private String pageEnd;

    private String totalPage;

    private String sltSido2;

    public String getPageIsPrev ()
    {
        return pageIsPrev;
    }

    public void setPageIsPrev (String pageIsPrev)
    {
        this.pageIsPrev = pageIsPrev;
    }

    public String getPageIsNext ()
    {
        return pageIsNext;
    }

    public void setPageIsNext (String pageIsNext)
    {
        this.pageIsNext = pageIsNext;
    }

    public Arr[] getArr ()
    {
        return arr;
    }

    public void setArr (Arr[] arr)
    {
        this.arr = arr;
    }

    public String getPageStart ()
    {
        return pageStart;
    }

    public void setPageStart (String pageStart)
    {
        this.pageStart = pageStart;
    }

    public String getNowPage ()
    {
        return nowPage;
    }

    public void setNowPage (String nowPage)
    {
        this.nowPage = nowPage;
    }

    public String getPageEnd ()
    {
        return pageEnd;
    }

    public void setPageEnd (String pageEnd)
    {
        this.pageEnd = pageEnd;
    }

    public String getTotalPage ()
    {
        return totalPage;
    }

    public void setTotalPage (String totalPage)
    {
        this.totalPage = totalPage;
    }

    public String getSltSido2 ()
    {
        return sltSido2;
    }

    public void setSltSido2 (String sltSido2)
    {
        this.sltSido2 = sltSido2;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [pageIsPrev = "+pageIsPrev+", pageIsNext = "+pageIsNext+", arr = "+arr+", pageStart = "+pageStart+", nowPage = "+nowPage+", pageEnd = "+pageEnd+", totalPage = "+totalPage+", sltSido2 = "+sltSido2+"]";
    }
}

