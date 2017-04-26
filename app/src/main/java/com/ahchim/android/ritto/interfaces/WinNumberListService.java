package com.ahchim.android.ritto.interfaces;

import com.ahchim.android.ritto.model.WinListPojo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Gold on 2017. 4. 19..
 */

public interface WinNumberListService {

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @FormUrlEncoded
    @POST("common.do?method=getLottoNumber")
    Call<WinListPojo> getWinData (
            @Field("drwNo") String round
    );

    @Headers("Content-Type:application/x-www-form-urlencoded")
    @POST("common.do?method=getLottoNumber")
    Call<WinListPojo> getLatestWinData ( );

}
