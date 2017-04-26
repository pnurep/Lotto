package com.ahchim.android.ritto.util;

import android.support.annotation.NonNull;
import android.util.Log;

import com.ahchim.android.ritto.interfaces.WinNumberListService;
import com.ahchim.android.ritto.model.WinListPojo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Gold on 2017. 4. 19..
 */

public class Loader {

    public static void getList(@NonNull String url, String round, final CallBack callback){
        if(!url.startsWith("http")){
            url = "http://"+url;
        }

        WinNumberListService winNumberListService;
        ArrayList<ArrayList<String>> allWinRoundList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        winNumberListService = retrofit.create(WinNumberListService.class);

        try {
            round = URLEncoder.encode(round, "euc-kr");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Call<WinListPojo> result = winNumberListService.getWinData(round);
        result.enqueue(new Callback<WinListPojo>() {
            @Override
            public void onResponse(Call<WinListPojo> call, Response<WinListPojo> response) {
                Log.e("성공", response.body().toString());

//                ArrayList<String> winRoundInfo = new ArrayList<>();
//                winRoundInfo.add(response.body().getDrwNo());
//                winRoundInfo.add(response.body().getDrwNoDate());

                //callback.call(winRoundInfo);
            }

            @Override
            public void onFailure(Call<WinListPojo> call, Throwable throwable) {
                Log.e("실패", throwable.getMessage());
            }
        });
    }

    public interface CallBack{
        void call(List list);
    }
}
