package com.ahchim.android.ritto.maps;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ahchim.android.ritto.interfaces.LottoService;
import com.ahchim.android.ritto.R;
import com.ahchim.android.ritto.model.storelist.Arr;
import com.ahchim.android.ritto.model.storelist.Pojo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
    "RTLRID": "21100485",
      "RTLRSTRTELNO": "02-459-7996",
      "RECORDNO": 1,
      "LATITUDE": 37.489199,
      "BPLCLOCPLC4": null,
      "BPLCLOCPLCDTLADRES": "186-15번지 소망빌딩109호110호",
      "DEAL645": "1",
      "DEALSPEETO": "N",
      "DEAL520": "N",
      "LONGITUDE": 127.067941,
      "FIRMNM": "GS25(개포소망점)",
      "BPLCLOCPLC3": "개포동",
      "BPLCLOCPLC2": "강남구",
      "BPLCLOCPLC1": "서울"
 */


public class NationStoreActivity extends AppCompatActivity {

    public static int LOCATION_DEPTH_FLAG = 1;
    public String tempLocation = "";

//    public static final int POST_ITEM_TYPE_COVER_NORMAL = 0;
//    public static final int POST_ITEM_TYPE_COVER_DETAIL = 1;

    Retrofit retrofit;
    LottoService lottoService;

    ListView listView;
    LocationAdapter adapter;
    TextView tv_Navi, tv_Navi1, tv_Navi2;
    String locationName;

    String sido = "";
    String gugun = "";
    int requestPage = 1;


    private final List<String> datas = new ArrayList<>();
    public static ArrayList<ArrayList<String>> detailLoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nation_store);

        adapter = new LocationAdapter(this, getAssetsFolderNameList(), detailLoc, LOCATION_DEPTH_FLAG);
        listView = (ListView) findViewById(R.id.location_list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (LOCATION_DEPTH_FLAG < 3) {
                    TextView textView = (TextView) view.findViewById(R.id.tv_location);
                    locationName = textView.getText().toString();
                    Log.e("EEE", "================locationName=" + locationName);

                    if (LOCATION_DEPTH_FLAG == 1) {
                        tv_Navi1.setText(locationName + " > ");
                        sido = textView.getText().toString();
                        Log.e("sido", sido);
                        getAssetsFolderNameList(locationName);
                        adapter.notifyDataSetChanged();
                    } else if (LOCATION_DEPTH_FLAG == 2) {
                        tv_Navi2.setText(locationName + " > ");
                        gugun = textView.getText().toString();
                        LOCATION_DEPTH_FLAG = 3;
                        Log.e("gugun", gugun);
                        requestPage = 1;
                        processRetrofit(requestPage); // requestPage = 1
                        adapter.notifyDataSetChanged();

                        //((BaseAdapter)listView.getAdapter()).notifyDataSetChanged();
                        Log.e("END", "================================");
                    }
                } else if (LOCATION_DEPTH_FLAG >= 3){
                    if (LOCATION_DEPTH_FLAG == 3) {
                        TextView sangho = (TextView) view.findViewById(R.id.tv_sangho);
                        TextView address = (TextView) view.findViewById(R.id.tv_address);
                        TextView phoneNum = (TextView) view.findViewById(R.id.tv_phoneNum);
                        TextView latitude = (TextView) view.findViewById(R.id.tv_latitude);
                        TextView longitude = (TextView) view.findViewById(R.id.tv_longitude);

                        Bundle bundle = new Bundle();
                        bundle.putString("sangho", sangho.getText().toString());
                        bundle.putString("address", address.getText().toString());
                        bundle.putString("phoneNum", phoneNum.getText().toString());
                        bundle.putString("latitude", latitude.getText().toString());
                        bundle.putString("longitude", longitude.getText().toString());

                        Intent intent = new Intent(NationStoreActivity.this, MapsActivity.class);
                        intent.putExtra("Map", "Nation");
                        intent.putExtras(bundle);
                        startActivity(intent);

                        //TODO : 해당 아이템에 해당하는 구글 지도 좌표로 이동해 POI세팅을 해준다.
                        return;
                    }
                }
            }
        });

        //리스트뷰 페이징을 위한 스크롤 리스너 등록
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState == SCROLL_STATE_IDLE){
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(NationStoreActivity.this, "가져왔다!", Toast.LENGTH_SHORT).show();
                        }
                    }, 700);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(LOCATION_DEPTH_FLAG == 3){
                    // 화면에 그려진 맨첫번째 뷰의 포지션값과, 현재 보여지고 있는 아이템의 갯수를 합친게
                    // 전체리스트아이템 개수보다 같거나 클때가 마지막까지 스크롤된 상태가 된다.
                    if(totalItemCount > 0 && firstVisibleItem + visibleItemCount == totalItemCount){
                        requestPage++;
                        //Toast.makeText(NationStoreActivity.this, "마지막포지션 도달!", Toast.LENGTH_SHORT).show();
                        Log.e("firstVisibleItem","===" + firstVisibleItem);
                        Log.e("visibleItemCount","===" + visibleItemCount);
                        Log.e("totalItemCount","===" + totalItemCount);
                        processRetrofit(requestPage);
                        Log.e("requestPage","===" + requestPage);
                    } else {

                    }
                }
            }
        });

        retrofit = new Retrofit.Builder().baseUrl(LottoService.LOTTO_URL).addConverterFactory(GsonConverterFactory.create()).build();
        lottoService = retrofit.create(LottoService.class);

        tv_Navi = (TextView) findViewById(R.id.tv_Navi);
        tv_Navi1 = (TextView) findViewById(R.id.tv_Navi1);
        tv_Navi2 = (TextView) findViewById(R.id.tv_Navi2);
        tv_Navi.setText(" 지역별선택 > ");
    }


    //전지역 맨처음 세팅
    public List<String> getAssetsFolderNameList() {
        try {
            String[] fileList = getAssets().list("location");
            datas.clear();
            for (int i = 0; i < fileList.length; i++) {
                Log.e("리스트", "============" + fileList[i]);
                datas.add(fileList[i]);
            }
            Log.e("DEPTH", "============" + LOCATION_DEPTH_FLAG);
            return datas;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public List<String> getAssetsFolderNameList(String locationName) {

        switch (LOCATION_DEPTH_FLAG) {
            case 1:
                tempLocation = locationName;
                LOCATION_DEPTH_FLAG = 2;
                try {
                    String[] fileList = getAssets().list("location/" + locationName);
                    datas.clear();
                    for (int i = 0; i < fileList.length; i++) {
                        Log.e("DEPTH", "============" + LOCATION_DEPTH_FLAG);
                        datas.add(fileList[i]);
                    }
                    return datas;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case 2:
                LOCATION_DEPTH_FLAG = 3;
//                try {
//                    LOCATION_DEPTH_FLAG = 3;
//                    String[] fileList = getAssets().list("location/" + tempLocation +"/"+ locationName);
//                    datas.clear();
//                    for(int i=0; i < fileList.length; i++){
//                        Log.e("DEPTH","============" + LOCATION_DEPTH_FLAG);
//                        datas.add(fileList[i]);
//                    }
//                    return datas;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                //datas.clear();
                return datas;
            case 3:
                break;
        }
        return null;
    }

    // Retrofit으로 가져오기
    public void processRetrofit(int requestPage) {

        Map<String, String> postValues = new HashMap<>();
        postValues.put("searchType", "1");
        postValues.put("nowPage", requestPage + "");
        postValues.put("sltSIDO", sido);
        postValues.put("sltGUGUN", gugun);
        postValues.put("rtlrSttus", "001");

        ArrayList<String> keyset = new ArrayList<>(postValues.keySet());
        ArrayList<String> encode = new ArrayList<>();

        for (String key : keyset) {
            try {
                encode.add(URLEncoder.encode(postValues.get(key), "euc-kr"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

//      Call<Pojo> data = lottoService.getData(nowPage, rtlrSttus, searchType, sltSIDO, sltGUGUN);
        Call<Pojo> pojoData = lottoService.getData(encode.get(0), encode.get(1), encode.get(2), encode.get(3), encode.get(4));
        pojoData.enqueue(new Callback<Pojo>() {
            @Override
            public void onResponse(Call<Pojo> call, Response<Pojo> response) {
                Pojo pojo = response.body();

//                datas.clear();
                int index = 0;
                for (Arr arr : pojo.getArr()) {
                    ArrayList<String> arrTemp = new ArrayList<>();
                    Log.e("성공", arr.toString());
//                    Log.e("index", "" + index);

                    arrTemp.clear();
//                    Log.e("arrTemp.clear", "" + arrTemp);
//                    Log.e("detailLoc-1", "" + detailLoc);
                    String addTemp = arr.getBPLCLOCPLC1() + " " + arr.getBPLCLOCPLC2() + " " + arr.getBPLCLOCPLC3() + " " + arr.getBPLCLOCPLCDTLADRES();
                    arrTemp.add(arr.getFIRMNM());
                    arrTemp.add(addTemp);
                    arrTemp.add(arr.getRTLRSTRTELNO());
                    arrTemp.add(arr.getLATITUDE());
                    arrTemp.add(arr.getLONGITUDE());
                    detailLoc.add(index, arrTemp);

//                    Log.e("arrTemp", "" + arrTemp);
                    Log.e("detailLoc-2", "" + detailLoc);
                    index++;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Pojo> call, Throwable throwable) {
                Log.e("실패", "======" + throwable.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (LOCATION_DEPTH_FLAG) {
            case 1:
                super.onBackPressed();
                break;
            case 2:
                LOCATION_DEPTH_FLAG = 1;
                getAssetsFolderNameList();
                tempLocation = "";
                tv_Navi1.setText("");
                adapter.notifyDataSetChanged();
                break;
            case 3:
                LOCATION_DEPTH_FLAG = 2;
                try {
                    detailLoc.clear();
                    String[] fileList = getAssets().list("location/" + tempLocation);
                    datas.clear();
                    for (int i = 0; i < fileList.length; i++) {
                        Log.e("LOCATION_DEPTH_FLAG = 2", "============" + fileList[i]);
                        datas.add(fileList[i]);
                    }
                    tv_Navi2.setText("");
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    //전국 판매점 데이터 POST로 가져옴 / httpUrlConnection
    public void process() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(Void... params) {
                String url = "http://www.nlotto.co.kr/store.do?method=sellerInfo645Result";
                Map<String, String> postValues = new HashMap<String, String>();
                postValues.put("searchType", "1");
                postValues.put("nowPage", "1");
                postValues.put("sltSIDO", "서울");
                postValues.put("sltGUGUN", "강북");
                postValues.put("rtlrSttus", "001");
                try {
                    return postData(url, postValues);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                Log.e("result", "=======" + result);
            }
        }.execute();
    }

    public String postData(String webUrl, Map<String, String> params) throws Exception {
        StringBuffer result = new StringBuffer();
        URL url = new URL(webUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=euc-kr");
        // post 처리 ------------
        connection.setDoOutput(true);
        OutputStream os = connection.getOutputStream();

        ArrayList<String> keyset = new ArrayList<>(params.keySet());
        for (String key : keyset) {
            String param = key + "=" + URLEncoder.encode(params.get(key), "euc-kr") + "&";
            Log.e("param", "=====" + param);
            os.write(param.getBytes());
        }
        os.flush();
        os.close();
        // ---------------------

        // 결과 코드 담기
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "euc-kr"));
            String dataLine = null;
            while ((dataLine = br.readLine()) != null) {
                result.append(dataLine);
            }
            br.close();
        } else {
            Log.e("ERROR", "HTTP error code=" + responseCode);
        }
        return result.toString();
    }

}
