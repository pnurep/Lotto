package com.ahchim.android.ritto.interfaces;

import com.ahchim.android.ritto.model.storesearch.StoreSearch;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * Created by Gold on 2017. 4. 17..
 */

public interface SearchService {

    public static final String DAUM_SEARCH_URL = "https://apis.daum.net/";

    //full address : https://apis.daum.net/local/v1/search/keyword.json?
    // apikey={apikey}&query=카카오프렌즈&location=37.514322572335935,127.06283102249932&radius=20000

    //apikey = da734c36814e0d0404fe4a78ec9e394e

//    @Headers({"Content-Type:application/x-www-form-urlencoded; charset=euc-kr"
//            ,"Cache-Control: no-cache"})
    @Headers("Content-Type:application/x-www-form-urlencoded")
    @GET("local/v1/search/keyword.json")
    Call<StoreSearch> getStoreData(
            @Query(value = "apikey", encoded = true) String apikey,
            @Query(value = "location", encoded = true) String location,
            @Query(value = "query", encoded = true) String query,
            @Query(value = "radius", encoded = true) String radius
    );
}
