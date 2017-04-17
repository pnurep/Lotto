package com.ahchim.android.ritto;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PurchasedNumActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("구입 번호 목록 & 당첨확인");

        setContentView(R.layout.activity_purchased_num);
    }
}
