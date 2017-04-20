package com.ahchim.android.ritto;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ahchim.android.ritto.model.storesearch.Channel;
import com.ahchim.android.ritto.model.storesearch.Item;
import com.ahchim.android.ritto.model.storesearch.StoreSearch;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    // GPS정보 업데이트 거리
    private static final long MIN_DISTANCE_GPS_DATA_UPDATE = 10;
    // GPS정보 업데이트 시간 1/1000
    private static final long MIN_TIME_UPDATE = 1000 * 60 * 1;

    protected LocationManager locationManager;

    //현재 gps사용 유무
    boolean isGPSEnabled = false;

    // 네트워크 사용유
    boolean isNetworkEnabled = false;

    //gps상태값
    boolean isGetLocation = false;

    Location location;
    double gps_lat;
    double gps_lng;

    private GoogleMap mMap;
    Intent intent;

    Retrofit retrofit;
    SearchService searchService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .baseUrl(SearchService.DAUM_SEARCH_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        searchService = retrofit.create(SearchService.class);



        int permissionChk = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionChk == PackageManager.PERMISSION_GRANTED) {
            intent = getIntent();
            getLocation();

        } else {
            Toast.makeText(this, "권한없음!", Toast.LENGTH_SHORT).show();
            finish();
        }

        setMap();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View view = getLayoutInflater().inflate(R.layout.snippet, null);

                TextView title = (TextView) view.findViewById(R.id.tv_snippet_sangho);
                TextView address = (TextView) view.findViewById(R.id.tv_snippet_addr);

                title.setText(marker.getTitle());

                if(marker.getSnippet() == "" || marker.getSnippet() == null){
                    address.setVisibility(View.GONE);
                } else {
                    address.setText(marker.getSnippet());
                }

                return view;
            }
        });


        String requestWord = intent.getStringExtra("Map");
        switch (requestWord) {
            case "Around":
                //위치정보를 가져올 수 있을때
                if(isGPSEnabled && isGetLocation()){
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(currentLocation).title("현재위치")).showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 16));
                    storeSrchRtrft();

                //위치정보를 가져올 수 없을때
                } else {
                    LatLng defaultLocation = new LatLng(37.515647, 127.021401);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 16));
                    showSettingAlert();
                }

                break;
            case "Nation":
                Bundle bundle = intent.getExtras();
                double latitude = Double.parseDouble(bundle.getString("latitude"));
                double longitude = Double.parseDouble(bundle.getString("longitude"));
                String sangho = bundle.getString("sangho");
                String address = bundle.getString("address");
                String phoneNum = bundle.getString("phoneNum");

                Log.e("위도 latitude", "============" + latitude);
                Log.e("경도 longitude", "============" + longitude);
                Log.e("sangho", "============" + sangho);
                Log.e("address", "============" + address);
                Log.e("phoneNum", "============" + phoneNum);

                LatLng store = new LatLng(latitude, longitude);
                MarkerOptions options = new MarkerOptions();
                options.position(store);
                options.title(sangho);
                options.snippet(address + "\n" + phoneNum);

                //options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pin_01));

                mMap.addMarker(options).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(store));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(store, 14));

                break;
        }
    }


    public void storeSrchRtrft() {

        String apiLocation = location.getLatitude() + "," + location.getLongitude() + "";
        Log.e("apiLocation","==========" + apiLocation);

        Map<String, String> getQuery = new HashMap<>();

        getQuery.put("apikey", "da734c36814e0d0404fe4a78ec9e394e");
        getQuery.put("query", "로또판매점");
        getQuery.put("apiLocation", apiLocation);
        getQuery.put("radius", "10000");

        ArrayList<String> keyset = new ArrayList<>(getQuery.keySet());
        ArrayList<String> encode = new ArrayList<>();

        for(String key : keyset){
            try {
                encode.add(URLEncoder.encode( getQuery.get(key), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        for(String a : encode){
            Log.e("encode","===========" + a);
        }


        // 다음 Search Api 사용, 현재위치중심으로 검색
        Call<StoreSearch> storeSearchData = searchService.getStoreData(encode.get(0), encode.get(1),  encode.get(2), encode.get(3));
        storeSearchData.enqueue(new Callback<StoreSearch>() {
            @Override
            public void onResponse(Call<StoreSearch> call, Response<StoreSearch> response) {
                Log.e("로또판매점 성공","=====================" + response.body().toString());

                for (Item item : response.body().getChannel().getItem()){
                    String title = item.getTitle();
                    String address = item.getAddress() + "\n";
                    String pNum = item.getPhone();
                    double latitude = Double.parseDouble(item.getLatitude());
                    double longitude = Double.parseDouble(item.getLongitude());


                    LatLng searchedStore = new LatLng(latitude, longitude);
                    MarkerOptions options = new MarkerOptions();
                    options.position(searchedStore);
                    options.title(title);

                    if(address == "") {
                        address = "";
                    } else if (pNum == ""){
                        pNum = "";
                    }

                    options.snippet(address + pNum);
                    mMap.addMarker(options);
                }

            }

            @Override
            public void onFailure(Call<StoreSearch> call, Throwable throwable) {
                Log.e("실패","==================" + throwable.getMessage());
            }
        });

    }



    public void setMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public Location getLocation() {

        try {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

            } else {
                this.isGetLocation = true;

                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_UPDATE,
                            MIN_DISTANCE_GPS_DATA_UPDATE, this);

                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            gps_lat = location.getLatitude();
                            gps_lng = location.getLongitude();
                        }
                    }
                }

                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_UPDATE,
                                MIN_DISTANCE_GPS_DATA_UPDATE,
                                this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                gps_lat = location.getLatitude();
                                gps_lng = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return location;
    }

    /**
     *
     *  GPS사용중지     */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(MapsActivity.this);
        }
    }

    /**
     *
     * @return 위도값
     */
    public double getLatitude() {
        if (location != null) {
            gps_lat = location.getLatitude();
        }
        return  gps_lat;
    }

    /**
     *
     * @return 경도값
     */
    public double getLongitude() {
        if(location != null) {
            gps_lng = location.getLongitude();
        }
        return gps_lng;
    }

    public boolean isGetLocation() {
        return this.isGetLocation;
    }


    public void showSettingAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("GPS 사용 유무 세팅");
        alertDialog.setMessage("GPS가 켜져있지 않습니다. \n  설정창으로 가시겠습니까?");

        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }



    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
