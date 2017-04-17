package com.ahchim.android.ritto.daummap.search;

/**
 * Created by Gold on 2017. 4. 1..
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ahchim.android.ritto.R;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapReverseGeoCoder;
import net.daum.mf.map.api.MapView;

public class LocationDemoActivity extends FragmentActivity implements MapView.CurrentLocationEventListener, MapReverseGeoCoder.ReverseGeoCodingResultListener {

    private static final String LOG_TAG = "LocationDemoActivity";
    private final int MENU_LOCATION = Menu.FIRST;
    private final int MENU_REVERSE_GEO = Menu.FIRST + 1;

    private MapView mMapView;
    private MapReverseGeoCoder mReverseGeoCoder = null;
    private boolean isUsingCustomLocationMarker = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.demo_nested_mapview);

        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.setDaumMapApiKey("521cddce9ca7dc1364a1f9ff00f13038");
        mMapView.setCurrentLocationEventListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
        mMapView.setShowCurrentLocationMarker(false);
    }

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint currentLocation, float accuracyInMeters) {
        MapPoint.GeoCoordinate mapPointGeo = currentLocation.getMapPointGeoCoord();
        Log.i(LOG_TAG, String.format("MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)", mapPointGeo.latitude, mapPointGeo.longitude, accuracyInMeters));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_LOCATION, Menu.NONE, "Location");
        menu.add(0, MENU_REVERSE_GEO, Menu.NONE, "Reverse Geo-Coding");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        final int itemId = item.getItemId();

        switch (itemId) {
            case MENU_LOCATION: {
                String[] mapMoveMenuItems = {
                        "User Location On",
                        "User Location On, MapMoving Off",
                        "User Location+Heading On",
                        "User Location+Heading On, MapMoving Off",
                        "Off",
                        (isUsingCustomLocationMarker ? "Default" : "Custom") + " Location Marker",
                        "Show Location Marker",
                        "Hide Location Marker"
                };
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Location");
                dialog.setItems(mapMoveMenuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // User Location On
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                            }
                            break;
                            case 1: // User Location On, MapMoving Off
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeadingWithoutMapMoving);
                            }
                            break;
                            case 2: // User Location+Heading On
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading);
                            }
                            break;
                            case 3: // User Location+Heading On, MapMoving Off
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeadingWithoutMapMoving);
                            }
                            break;
                            case 4: // Off
                            {
                                mMapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                                mMapView.setShowCurrentLocationMarker(false);
                            }
                            break;
                            case 5: // Default/Custom Location Marker
                            {
                                if (isUsingCustomLocationMarker) {
                                    mMapView.setCurrentLocationRadius(0);
                                    mMapView.setDefaultCurrentLocationMarker();
                                } else {
                                    mMapView.setCurrentLocationRadius(100); // meter
                                    mMapView.setCurrentLocationRadiusFillColor(Color.argb(77, 255, 255, 0));
                                    mMapView.setCurrentLocationRadiusStrokeColor(Color.argb(77, 255, 165, 0));

                                    MapPOIItem.ImageOffset trackingImageAnchorPointOffset = new MapPOIItem.ImageOffset(16, 16); // 좌하단(0,0) 기준 앵커포인트 오프셋
                                    MapPOIItem.ImageOffset directionImageAnchorPointOffset = new MapPOIItem.ImageOffset(65, 65);
                                    MapPOIItem.ImageOffset offImageAnchorPointOffset = new MapPOIItem.ImageOffset(15, 15);
                                    //mMapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.custom_map_present_tracking, trackingImageAnchorPointOffset);
                                    //mMapView.setCustomCurrentLocationMarkerDirectionImage(R.drawable.custom_map_present_direction, directionImageAnchorPointOffset);
                                    //mMapView.setCustomCurrentLocationMarkerImage(R.drawable.custom_map_present, offImageAnchorPointOffset);
                                }
                                isUsingCustomLocationMarker = !isUsingCustomLocationMarker;
                            }
                            break;
                            case 6: // Show Location Marker
                            {
                                mMapView.setShowCurrentLocationMarker(true);
                            }
                            break;
                            case 7: // Hide Location Marker
                            {
                                if (mMapView.isShowingCurrentLocationMarker()) {
                                    mMapView.setShowCurrentLocationMarker(false);
                                }
                            }
                            break;
                        }
                    }

                });
                dialog.show();
                return true;
            }

            case MENU_REVERSE_GEO: {
                mReverseGeoCoder = new MapReverseGeoCoder("a14b32c93ef72e751b9a37ceea05fd95", mMapView.getMapCenterPoint(), LocationDemoActivity.this, LocationDemoActivity.this);
                mReverseGeoCoder.startFindingAddress();
                return true;
            }
        }

        return onOptionsItemSelected(item);

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
    public void onReverseGeoCoderFoundAddress(MapReverseGeoCoder mapReverseGeoCoder, String s) {
        mapReverseGeoCoder.toString();
        onFinishReverseGeoCoding(s);
    }

    @Override
    public void onReverseGeoCoderFailedToFindAddress(MapReverseGeoCoder mapReverseGeoCoder) {
        onFinishReverseGeoCoding("Fail");
    }

    private void onFinishReverseGeoCoding(String result) {
        Toast.makeText(LocationDemoActivity.this, "Reverse Geo-coding : " + result, Toast.LENGTH_SHORT).show();
    }
}
