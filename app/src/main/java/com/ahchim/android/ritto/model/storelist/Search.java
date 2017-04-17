package com.ahchim.android.ritto.model.storelist;

import java.net.URLEncoder;

/**
 * Created by Gold on 2017. 4. 13..
 */

public class Search {
    String searchType;
    String nowPage;
    String sltSIDO;
    String sltGUGUN;
    String rtlrSttus;

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getNowPage() {
        return nowPage;
    }

    public void setNowPage(String nowPage) {
        this.nowPage = nowPage;
    }

    public String getSltSIDO() {
        return sltSIDO;
    }

    public void setSltSIDO(String sltSIDO) {
        this.sltSIDO = sltSIDO;
    }

    public String getSltGUGUN() {
        return sltGUGUN;
    }

    public void setSltGUGUN(String sltGUGUN) {
        this.sltGUGUN = sltGUGUN;
    }

    public String getRtlrSttus() {
        return rtlrSttus;
    }

    public void setRtlrSttus(String rtlrSttus) {
        this.rtlrSttus = rtlrSttus;
    }

    public String toFormString() {
        String result = "";
        try {
            result = "searchType=" + searchType
                    + "&nowPage=" + nowPage
                    + "&sltSIDO=" + URLEncoder.encode(sltSIDO, "UTF-8")
                    + "&sltGUGUN=" + URLEncoder.encode(sltGUGUN, "UTF-8")
                    + "&rtlrSttus=" + rtlrSttus;
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
