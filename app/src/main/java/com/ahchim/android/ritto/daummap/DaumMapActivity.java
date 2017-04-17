package com.ahchim.android.ritto.daummap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ahchim.android.ritto.PermissionControl;
import com.ahchim.android.ritto.R;
import com.ahchim.android.ritto.daummap.search.Item;
import com.ahchim.android.ritto.daummap.search.OnFinishSearchListener;
import com.ahchim.android.ritto.daummap.search.Searcher;

import net.daum.mf.map.api.CameraUpdateFactory;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapPointBounds;
import net.daum.mf.map.api.MapReverseGeoCoder; //기능이 제대로 동작하지 않고, 트래픽을 많이 먹으므로 기능을 삭제함.
import net.daum.mf.map.api.MapView;

import java.util.HashMap;
import java.util.List;


public class DaumMapActivity extends AppCompatActivity implements MapView.MapViewEventListener,
        MapView.CurrentLocationEventListener {

    private static final String DAUM_MAP_API_KEY = "521cddce9ca7dc1364a1f9ff00f13038";
    private final int REQ_PERMISSION = 100; // 권한요청코드

    EditText etSearch;
    Button btnSearch;
    MapView mapView;
    private HashMap<Integer, Item> mTagItemMap = new HashMap<Integer, Item>();

    MapPoint mCurrentLocation = null;
    MapPoint.GeoCoordinate geoCoordinate;

    BroadcastReceiver mReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkPermission();

        setContentView(R.layout.activity_daum_map);

        mapView = new MapView(this);
        mapView.setDaumMapApiKey(DAUM_MAP_API_KEY);

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this);
        mapView.setCurrentLocationEventListener(this);

        etSearch = (EditText) findViewById(R.id.etSearch);
        btnSearch = (Button) findViewById(R.id.btnSearch);

        //브로드캐스트의 액션을 등록하기 위한 인텐트 필터
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.location.PROVIDERS_CHANGED");

        //동적리시버 구현
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(isGPSAvailable()){
                    showToast("켜졌지?");
                }else {
                    showToast("안켜졌을걸?");
                }
            }
        };
        //리시버 등록
        registerReceiver(mReceiver, intentFilter);

        if(isGPSAvailable()==false){
            showToast("GPS가 켜져있지 않아요 아죠씨!");
        }

        Log.e("온크리에이트 끝", "=======================");
//        Log.e("온크리에이트 : 경도","================" + mCurrentLocation.getMapPointGeoCoord().latitude);
//        Log.e("온크리에이트 : 경도","================" + mCurrentLocation.getMapPointGeoCoord().longitude);
    }

    //permission check
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionControl.checkPermission(this, REQ_PERMISSION)) {
                return;
            }
        } else {
            finish();
        }
    }

    public void onClickSearchButton(View v) {
        String query = etSearch.getText().toString();
        if (query == null || query.length() == 0) {
            Toast.makeText(getApplicationContext(), "검색어를 입력하세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        hideSoftKeyboard();

        if (isGPSAvailable()) {
            if (mCurrentLocation != null) {
                geoCoordinate = mCurrentLocation.getMapPointGeoCoord();
            } else {
                Toast.makeText(this, "아직 위치정보를 잡지 못했습니다! 잠시후 시도해 주세요", Toast.LENGTH_LONG);
            }
        } else {
            geoCoordinate = mapView.getMapCenterPoint().getMapPointGeoCoord();
        }

        double latitude = geoCoordinate.latitude; //위도
        double longtitude = geoCoordinate.longitude; //경도
        int radius = 10000; // 중심좌표부터의 반경거리 특정지역을 중심으로 검색하려고 할 경우 사용한다. meter 단위(0~10000);
        int page = 1; // 페이지번호 (1~3) 한페이지에 15개

        Searcher searcher = new Searcher();
        mapView.removeAllPOIItems(); // 기존 검색결과 삭제

        searcher.searchKeyword(getApplicationContext(), query, latitude, longtitude, radius, page, DAUM_MAP_API_KEY, new OnFinishSearchListener() {
            @Override
            public void onSuccess(List<Item> itemList) {
                int j = 0;
                showResult(itemList); // 검색 결과 보여줌
                Log.e("쇼리절트", "=====================================" + j);
                j++;
            }

            @Override
            public void onFail() {
                showToast("Api키의 제한 트래픽이 초과되었습니다");
            }
        });
    }

    public void setToMyCurrentLocation() {
        // onCurrentLocationUpdate에서 받아온 현재 위치값 == mCurrentLoaction 을 사용한다.
        // 최소 1회는 gps를 통해 현재위치값을 받아 화면을 그리고
        // 다음부터는 mCurrentLocation에 저장된 위치값으로 현재위치를 찍는다.
        // mCurrentLocation값은 onCurrentLocationUpdate콜백함수가 3~5초 정도의 주기적인 간격으로 업데이트 한다.

        if (isGPSAvailable() && mCurrentLocation != null) {
            mapView.moveCamera(CameraUpdateFactory.newMapPoint(mCurrentLocation));
            Log.e("위치위치1", "=======================");
            Log.e("mCurrentLocation", "=======================" + mCurrentLocation);
        } else {
            //showToast("GPS 안켜져있다니까요 아죠씨?");
//            mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
//            mapView.moveCamera(CameraUpdateFactory.newMapPoint(mCurrentLocation));
            Log.e("위치위치2", "=======================");
            Log.e("mCurrentLocation", "=======================" + mCurrentLocation);

        }
    }

    public boolean isGPSAvailable() {
        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    //스레드 안에서 ui변경 불가하기 때문에..
    private void showToast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DaumMapActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Create DaumMap Menu Button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuItem menuItem;
        menuItem = menu.add(0,R.id.current_location,0,"현재위치로");
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_NEVER);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case R.id.current_location :
                setToMyCurrentLocation();
                break;
        }
        return true;
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }

    private void showResult(List<Item> itemList) {
        MapPointBounds mapPointBounds = new MapPointBounds();

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);

            MapPOIItem poiItem = new MapPOIItem();
            poiItem.setItemName(item.title);
            poiItem.setTag(i);
            MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(item.latitude, item.longitude);
            poiItem.setMapPoint(mapPoint);
            mapPointBounds.add(mapPoint);

            //커스텀 마커를 사용할때
//            poiItem.setMarkerType(MapPOIItem.MarkerType.CustomImage);
//            poiItem.setCustomImageResourceId(R.drawable.map_pin_blue);
//            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.CustomImage);
//            poiItem.setCustomSelectedImageResourceId(R.drawable.map_pin_red);

            poiItem.setMarkerType(MapPOIItem.MarkerType.BluePin);
            poiItem.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin);

            poiItem.setCustomImageAutoscale(false);
            poiItem.setCustomImageAnchor(0.5f, 1.0f);

            mapView.addPOIItem(poiItem);
            mTagItemMap.put(poiItem.getTag(), item);
        }

        mapView.moveCamera(CameraUpdateFactory.newMapPointBounds(mapPointBounds));

        MapPOIItem[] poiItems = mapView.getPOIItems();
        if (poiItems.length > 0) {
            mapView.selectPOIItem(poiItems[0], false);
        }
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

        MapPoint myPoint = MapPoint.mapPointWithGeoCoord(37.53737528, 127.00557633);

        MapPOIItem marker = new MapPOIItem();
        marker.setItemName("Default Marker");
        marker.setTag(0);
        marker.setMapPoint(myPoint);
        marker.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

        mapView.addPOIItem(marker);
        setToMyCurrentLocation();
    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {
//        double latitude = mapView.getMapCenterPoint().getMapPointGeoCoord().latitude;
//        double longtitude = mapView.getMapCenterPoint().getMapPointGeoCoord().longitude;

        Log.e("온커런트로케이션업뎃 : 경도","================" + mapPoint.getMapPointGeoCoord().latitude);
        Log.e("온커런트로케이션업뎃 : 경도","================" + mapPoint.getMapPointGeoCoord().longitude);

        mCurrentLocation = mapPoint;

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    protected void onDestroy() {
        //리시버해제... 여기서 해제해도 될런지는 모르겠지만..
        unregisterReceiver(mReceiver);
        super.onDestroy();
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mapView.setShowCurrentLocationMarker(false);
    }

}