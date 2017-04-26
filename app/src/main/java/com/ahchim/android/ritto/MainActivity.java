package com.ahchim.android.ritto;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ahchim.android.ritto.generate_number.AutoGenActivity;
import com.ahchim.android.ritto.generate_number.DirectNumSelectActivity;
import com.ahchim.android.ritto.interfaces.LottoService;
import com.ahchim.android.ritto.interfaces.WinNumberListService;
import com.ahchim.android.ritto.maps.MapsActivity;
import com.ahchim.android.ritto.maps.NationStoreActivity;
import com.ahchim.android.ritto.model.WinListPojo;
import com.ahchim.android.ritto.number_list.GeneratedNumListActivity;
import com.ahchim.android.ritto.number_list.PastWinNumActivity;
import com.ahchim.android.ritto.qrCodeReader.FullScannerFragmentActivity;
import com.github.aakira.expandablelayout.ExpandableWeightLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Class<?> mClss;
    private static final int ZXING_CAMERA_PERMISSION = 1;
    private ExpandableWeightLayout mExpandLayout;

    LinearLayout ll_main_1, ll_ex_container;
    //RelativeLayout progress_layout;

    Button btnGenerate, btnList, btnStore;
    TextView lottoNumber1, lottoNumber2, lottoNumber3, lottoNumber4, lottoNumber5, lottoNumber6,
            lottoNumberPlus, txtLottoTimes, txtLottoDate;

    Retrofit retrofit;
    WinNumberListService winNumberListService;

    ProgressDialog pd;

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
        mExpandLayout = (ExpandableWeightLayout) findViewById(R.id.expandableLayout);
        ll_main_1 = (LinearLayout) findViewById(R.id.ll_main_1);
        ll_ex_container = (LinearLayout) findViewById(R.id.ll_ex_container);

        btnGenerate = (Button) findViewById(R.id.btnGenerate);
        btnList = (Button) findViewById(R.id.btnList);
        btnStore = (Button) findViewById(R.id.btnStore);

        btnGenerate.setOnClickListener(this);
        btnList.setOnClickListener(this);
        btnStore.setOnClickListener(this);

        lottoNumber1 = (TextView) findViewById(R.id.lottoNumber1);
        lottoNumber2 = (TextView) findViewById(R.id.lottoNumber2);
        lottoNumber3 = (TextView) findViewById(R.id.lottoNumber3);
        lottoNumber4 = (TextView) findViewById(R.id.lottoNumber4);
        lottoNumber5 = (TextView) findViewById(R.id.lottoNumber5);
        lottoNumber6 = (TextView) findViewById(R.id.lottoNumber6);
        lottoNumberPlus = (TextView) findViewById(R.id.lottoNumberPlus);
        txtLottoTimes = (TextView) findViewById(R.id.txtLottoTimes);
        txtLottoDate = (TextView) findViewById(R.id.txtLottoDate);

        retrofit = new Retrofit.Builder().baseUrl(LottoService.LOTTO_URL).addConverterFactory(GsonConverterFactory.create()).build();
        winNumberListService = retrofit.create(WinNumberListService.class);

//        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);

        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
        pd.setMessage("불러오는중...");
        pd.show();

        getLatestWinNumber();

    }

    public void getLatestWinNumber() {

        Call<WinListPojo> getLatestWinData = winNumberListService.getLatestWinData();
        getLatestWinData.enqueue(new Callback<WinListPojo>() {
            @Override
            public void onResponse(Call<WinListPojo> call, Response<WinListPojo> response) {
                Log.e("성공","=====" + response.body());

                makeNum(response.body().getDrwtNo1(), lottoNumber1);
                makeNum(response.body().getDrwtNo2(), lottoNumber2);
                makeNum(response.body().getDrwtNo3(), lottoNumber3);
                makeNum(response.body().getDrwtNo4(), lottoNumber4);
                makeNum(response.body().getDrwtNo5(), lottoNumber5);
                makeNum(response.body().getDrwtNo6(), lottoNumber6);
                makeNum(response.body().getBnusNo(), lottoNumberPlus);

                txtLottoTimes.setText(response.body().getDrwNo() + "회차");
                txtLottoDate.setText(response.body().getDrwNoDate());

//                progress_layout.setVisibility(View.GONE);
                pd.dismiss();
            }

            @Override
            public void onFailure(Call<WinListPojo> call, Throwable throwable) {
                Log.e("실패","=====" + "aa");
//                progress_layout.setVisibility(View.GONE);
                pd.dismiss();
            }
        });

        //pd.dismiss();

    }


    public void makeNum(String num, TextView tv){

        tv.setText(num);
        tv.setClickable(false);

        int tempNum = Integer.parseInt(num);

        if (tempNum < 11) {
            tv.setBackgroundResource(R.mipmap.ball_one);
            tv.setTag(R.mipmap.ball_one);
        } else if (tempNum < 21) {
            tv.setBackgroundResource(R.mipmap.ball_two);
            tv.setTag(R.mipmap.ball_two);
        } else if (tempNum < 31) {
            tv.setBackgroundResource(R.mipmap.ball_three);
            tv.setTag(R.mipmap.ball_three);
        } else if (tempNum < 41) {
            tv.setBackgroundResource(R.mipmap.ball_four);
            tv.setTag(R.mipmap.ball_four);
        } else {
            tv.setBackgroundResource(R.mipmap.ball_five);
            tv.setTag(R.mipmap.ball_five);
        }
    }


    //맵액티비티 로딩
    public void moveToMap(){
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        intent.putExtra("Map", "Around");
        startActivity(intent);
    }

    // 전국판매점지도
    public void moveToNation() {
        Intent intent = new Intent(MainActivity.this, NationStoreActivity.class);
        startActivity(intent);
    }

    public void moveToAutoGen(){
        Intent intent = new Intent(MainActivity.this, AutoGenActivity.class);
        startActivity(intent);
    }

    public void moveToDirectGen(){
        Intent intent = new Intent(MainActivity.this, DirectNumSelectActivity.class);
        intent.putExtra("REQUEST_CODE", 300);
        startActivity(intent);
    }

    public void moveToGeneratedNumList(){
        Intent intent = new Intent(MainActivity.this, GeneratedNumListActivity.class);
        startActivity(intent);
    }

    public void moveToPastWinNum(){
        Intent intent = new Intent(MainActivity.this, PastWinNumActivity.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(mExpandLayout.isExpanded()){
            mExpandLayout.toggle();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //메뉴아이템
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //네비게이션 메뉴 아이템
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_random_number :
                moveToAutoGen();
                break;
            case R.id.action_input_number :
                moveToDirectGen();
                break;
            case R.id.action_generated_numbers :
                moveToGeneratedNumList();
                break;
            case R.id.action_past_winning_numbers :
                moveToPastWinNum();
                break;
            case R.id.action_store_around :
                moveToMap();
                break;
            case R.id.action_store_nationwide :
                moveToNation();
                break;
        }



//        int id = item.getItemId();
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGenerate :
                mExpandLayout.toggle();
                TextView tv1 = new TextView(this);
                tv1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                tv1.setGravity(Gravity.CENTER_VERTICAL);
                tv1.setTextColor(Color.WHITE);
                tv1.setTypeface(Typeface.DEFAULT_BOLD);
                tv1.setTextSize(19);
                tv1.setText("번호 자동생성");
                tv1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToAutoGen();
                    }
                });

                TextView tv2 = new TextView(this);
                tv2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                tv2.setGravity(Gravity.CENTER_VERTICAL);
                tv2.setTextColor(Color.WHITE);
                tv2.setTypeface(Typeface.DEFAULT_BOLD);
                tv2.setTextSize(19);
                tv2.setText("번호 직접입력");
                tv2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToDirectGen();
                    }
                });

                ll_ex_container.removeAllViewsInLayout();
                ll_ex_container.addView(tv1);
                ll_ex_container.addView(tv2);
                break;

            case R.id.btnList :
                mExpandLayout.toggle();
                TextView tv3 = new TextView(this);
                tv3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                tv3.setGravity(Gravity.CENTER_VERTICAL);
                tv3.setTextColor(Color.WHITE);
                tv3.setTypeface(Typeface.DEFAULT_BOLD);
                tv3.setTextSize(19);
                tv3.setText("만든번호목록");
                tv3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToGeneratedNumList();
                    }
                });


                TextView tv4 = new TextView(this);
                tv4.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                tv4.setGravity(Gravity.CENTER_VERTICAL);
                tv4.setTextColor(Color.WHITE);
                tv4.setTypeface(Typeface.DEFAULT_BOLD);
                tv4.setTextSize(19);
                tv4.setText("지난당첨번호");
                tv4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToPastWinNum();
                    }
                });

                ll_ex_container.removeAllViewsInLayout();
                ll_ex_container.addView(tv3);
                ll_ex_container.addView(tv4);
                break;

            case R.id.btnStore :
                mExpandLayout.toggle();
                TextView tv5 = new TextView(this);
                tv5.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                tv5.setGravity(Gravity.CENTER_VERTICAL);
                tv5.setTextColor(Color.WHITE);
                tv5.setTypeface(Typeface.DEFAULT_BOLD);
                tv5.setTextSize(19);
                tv5.setText("주변 판매점");
                tv5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToMap();
                    }
                });

                TextView tv6 = new TextView(this);
                tv6.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
                tv6.setGravity(Gravity.CENTER_VERTICAL);
                tv6.setTextColor(Color.WHITE);
                tv6.setTypeface(Typeface.DEFAULT_BOLD);
                tv6.setTextSize(19);
                tv6.setText("전국 판매점");
                ll_ex_container.removeAllViewsInLayout();
                ll_ex_container.addView(tv5);
                ll_ex_container.addView(tv6);
                tv6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToNation();
                    }
                });

                break;
        }
    }
}
