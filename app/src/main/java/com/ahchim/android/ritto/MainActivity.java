package com.ahchim.android.ritto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ahchim.android.ritto.daummap.DaumMapActivity;
import com.ahchim.android.ritto.qrCodeReader.FullScannerFragmentActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Class<?> mClss;
    private static final int ZXING_CAMERA_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "QR코드 촬영 여기에", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                launchFullFragmentActivity(view);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //네이버 지도 로딩
    public void moveToMap(View v){
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("Map", "Around");
        startActivity(intent);
    }

    public void moveToNation() {
        Intent intent = new Intent(MainActivity.this, NationStoreActivity.class);
        startActivity(intent);
    }

    public void genNumberMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        final MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.generate_number, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.action_random_number :
                        intent = new Intent(MainActivity.this, AutoGenActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_input_number :
                        intent = new Intent(MainActivity.this, DirectNumSelectActivity.class);
                        intent.putExtra("REQUEST_CODE", 300);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    public void listNumberMenu(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.list_number, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.action_generated_numbers :
                        intent = new Intent(MainActivity.this, GeneratedNumListActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_past_winning_numbers :
                        intent = new Intent(MainActivity.this, PastWinNumActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.action_purchased_numbers :
                        intent = new Intent(MainActivity.this, PurchasedNumActivity.class);
                        startActivity(intent);
                        break;
                }

                return false;
            }
        });

        popup.show();
    }

    public void showStoreMenu(final View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.show_store, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_store_around :
                        moveToMap(v);
                        break;
                    case R.id.action_store_nationwide :
                        moveToNation();
                        break;
                }
                return false;
            }
        });
        popup.show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launchActivity(Class<?> clss) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            mClss = clss;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, ZXING_CAMERA_PERMISSION);
        } else {
            Intent intent = new Intent(this, clss);
            startActivity(intent);
        }
    }

    public void launchFullFragmentActivity(View v) {
        launchActivity(FullScannerFragmentActivity.class);
    }


}
