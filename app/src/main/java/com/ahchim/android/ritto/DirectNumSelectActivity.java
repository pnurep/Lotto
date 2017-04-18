package com.ahchim.android.ritto;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahchim.android.ritto.model.SavedNumber;
import com.ahchim.android.ritto.util.Ascending;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import io.realm.Realm;

public class DirectNumSelectActivity extends AppCompatActivity {

    Button btnSelect;

    int start = 1;
    int limit = 7;
    int switchLimit = 0;
    int a = 1;
    int numSelectCounter = 0;

    ArrayList<Integer> selectedNumberList;

    LinearLayout linearLayout, linearLayout1, ll_select_result;
    TextView lottoNum1, textView5;
    public static int REQUEST_CODE_DIRECT;

    Realm realm;
    Ascending ascending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        REQUEST_CODE_DIRECT = getIntent().getExtras().getInt("REQUEST_CODE");
        Log.e("REQUEST_CODE_DIRECT", "=======================" + REQUEST_CODE_DIRECT);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (REQUEST_CODE_DIRECT == 300) {
            actionBar.setTitle("번호 직접 입력");
            Log.e("REQUEST_CODE_DIRECT", "===================" + REQUEST_CODE_DIRECT);
        } else if (REQUEST_CODE_DIRECT == 100) {
            actionBar.setTitle("포함할 번호 선택");
            Log.e("REQUEST_CODE_DIRECT", "===================" + REQUEST_CODE_DIRECT);
        } else if (REQUEST_CODE_DIRECT == 200) {
            actionBar.setTitle("제외할 번호 선택");
            Log.e("REQUEST_CODE_DIRECT", "===================" + REQUEST_CODE_DIRECT);
        }

        setContentView(R.layout.activity_direct_num_select);

        selectedNumberList = new ArrayList<>();

        btnSelect = (Button) findViewById(R.id.btnSelect_direct);
        linearLayout = (LinearLayout) findViewById(R.id.direct_ll);
        ll_select_result = (LinearLayout) findViewById(R.id.ll_select_result);
        ll_select_result.setBackgroundColor(Color.YELLOW);
        ll_select_result.setGravity(Gravity.CENTER_HORIZONTAL);
        textView5 = (TextView) findViewById(R.id.textView5);

        if (REQUEST_CODE_DIRECT == 300) {
            btnSelect.setText("번호저장!");
            textView5.setText("  번호 6개를 선택해주세요!");
            switchLimit = 6;
        } else if (REQUEST_CODE_DIRECT == 100 || REQUEST_CODE_DIRECT == 200) {
            btnSelect.setText("선택완료");
            if(REQUEST_CODE_DIRECT == 100){
                textView5.setText("  포함할 번호를 선택하세요. (최대 5개)");
            } else {
                textView5.setText("  제외할 번호를 선택하세요. (최대 5개)");
            }
            ll_select_result.setVisibility(View.GONE);
            switchLimit = 5;
        }

        viewInit();
        setBallDoNotClick();

        realm = Realm.getDefaultInstance();
        ascending = new Ascending();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void viewInit() {
        for (int i = 0; i < 7; i++) {
            View v = View.inflate(this, R.layout.num_select_list, null);
            linearLayout1 = (LinearLayout) v.findViewById(R.id.ll_select);

            switch (a) {
                case 1:
                    break;
                case 2:
                    start = start + 7;
                    limit = limit + 7;
                    break;
                case 3:
                    start = start + 7;
                    limit = limit + 7;
                    break;
                case 4:
                    start = start + 7;
                    limit = limit + 7;
                    break;
                case 5:
                    start = start + 7;
                    limit = limit + 7;
                    break;
                case 6:
                    start = start + 7;
                    limit = limit + 7;
                    break;
                case 7:
                    start = start + 7;
                    limit = 45;
                    break;
            }

            for (int j = start; j <= limit; j++) {
                lottoNum1 = new TextView(this);
                lottoNum1.setId(j);
                lottoNum1.setBackgroundResource(R.mipmap.ball_unselect);
                lottoNum1.setText(j + "");
                lottoNum1.setTextSize(20);
                lottoNum1.setTextColor(Color.WHITE);
                lottoNum1.setTypeface(Typeface.DEFAULT_BOLD);
                lottoNum1.setGravity(Gravity.CENTER);
                lottoNum1.setPaintFlags(0);

                lottoNum1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(this, ((TextView)v).getText(), Toast.LENGTH_SHORT).show(); -> 이거 토스트 어케 찍냐...
                        Log.e("동적뷰", "값=================" + ((TextView) v).getText().toString()); //-> 기가 막힌다. lottoNum1.getText().toString()하면 마지막거의 내용만 출력됨
                        Log.e("동적뷰", "아이디값이 뭐니?==================" + v.getId());

                        switch (((TextView) v).getPaintFlags()) {
                            case 0:
                                colorDistinction(v);
                                break;
                            case 1:
                                v.setBackgroundResource(R.mipmap.ball_unselect);
                                ((TextView) v).setPaintFlags(0);
                                numSelectCounter = numSelectCounter - 1;
                                selectedNumberList.remove((Object) v.getId()); //오브젝트 타입으로 바꿔주어야 인덱스로 삭제하는게 아닌 값으로 삭제를 할 수 있다.
                                Log.e("selectedNumberList","====" + selectedNumberList);
                                ll_select_result.removeView(ll_select_result.findViewById(v.getId()));
                                break;
                            case 3 :
                                Toast.makeText(DirectNumSelectActivity.this, "이미 포함되거나 제외된 수 입니다", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                Log.e("돌아가니?", "====================" + j);
                int count = j;

                if (count % 7 == 0) {
                    a = a + 1;
                    Log.e("a는?", "=============" + a);
                }
                if (j == 43) {
                    linearLayout1.setPadding(133, 0, 0, 0);
                }
                if (j >= 43) {
                    linearLayout1.setGravity(Gravity.LEFT);
                }
                linearLayout1.addView(lottoNum1);
            }
            linearLayout.addView(v);
        }

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (REQUEST_CODE_DIRECT == 100 || REQUEST_CODE_DIRECT == 200) {
                    if (numSelectCounter == 0) {
                        Toast.makeText(DirectNumSelectActivity.this, "번호를 1개 이상 선택해 주십쇼!", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent returnIntent = new Intent(DirectNumSelectActivity.this, AutoGenActivity.class);
                        returnIntent.putIntegerArrayListExtra("result", selectedNumberList);
                        setResult(RESULT_OK, returnIntent);
                        Log.e("과연 어떤값이?", "=================" + selectedNumberList);
                        finish();
                    }
                } else if (REQUEST_CODE_DIRECT == 300) {
                    if (numSelectCounter < 6) {
                        Toast.makeText(DirectNumSelectActivity.this, "6개 다 채웠는감??", Toast.LENGTH_SHORT).show();
                    } else {

                        realm.beginTransaction();
                        SavedNumber savedNumber = realm.createObject(SavedNumber.class);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                        String currentDateTimeString = simpleDateFormat.format(new Date());

                        Collections.sort(selectedNumberList, ascending);
                        savedNumber.setDate(currentDateTimeString);
                        savedNumber.setNum1(selectedNumberList.get(0));
                        savedNumber.setNum2(selectedNumberList.get(1));
                        savedNumber.setNum3(selectedNumberList.get(2));
                        savedNumber.setNum4(selectedNumberList.get(3));
                        savedNumber.setNum5(selectedNumberList.get(4));
                        savedNumber.setNum6(selectedNumberList.get(5));

                        Toast.makeText(DirectNumSelectActivity.this, "번호가 저장되었습니다!", Toast.LENGTH_SHORT).show();

                        realm.commitTransaction();

                        //저장후 뷰들의 초기화
                        ll_select_result.removeAllViewsInLayout();
                        setUnclicked();
                        selectedNumberList.clear();
                        numSelectCounter = 0;
                        Log.e("selectedNumberList","저장후" + selectedNumberList);
                    }
                }
            }
        });
    }

    public void colorDistinction(View v) {
        if (numSelectCounter < switchLimit) {
            if (v.getId() < 11) {
                v.setBackgroundResource(R.mipmap.ball_one);
                v.setTag(R.mipmap.ball_one);
            } else if (v.getId() < 21) {
                v.setBackgroundResource(R.mipmap.ball_two);
                v.setTag(R.mipmap.ball_two);
            } else if (v.getId() < 31) {
                v.setBackgroundResource(R.mipmap.ball_three);
                v.setTag(R.mipmap.ball_three);
            } else if (v.getId() < 41) {
                v.setBackgroundResource(R.mipmap.ball_four);
                v.setTag(R.mipmap.ball_four);
            } else {
                v.setBackgroundResource(R.mipmap.ball_five);
                v.setTag(R.mipmap.ball_five);
            }
            ((TextView) v).setPaintFlags(1);
            numSelectCounter = numSelectCounter + 1;
            selectedNumberList.add(v.getId());

            TextView selectedTV = new TextView(DirectNumSelectActivity.this);
            selectedTV.setId(v.getId());
            selectedTV.setText(((TextView) v).getText().toString());
            selectedTV.setGravity(Gravity.CENTER);
            selectedTV.setTextSize(20);
            selectedTV.setTextColor(Color.WHITE);
            selectedTV.setBackgroundResource((Integer) v.getTag());
            selectedTV.setTypeface(Typeface.DEFAULT_BOLD);

            ll_select_result.addView(selectedTV);

        } else {
            Toast.makeText(DirectNumSelectActivity.this, switchLimit + "개까지만 선택 가능해요!", Toast.LENGTH_SHORT).show();
        }

    }

    public void setUnclicked() {
        for(int i=0; i<6; i++){
            TextView foundView = (TextView) linearLayout.findViewById(selectedNumberList.get(i));
            foundView.setBackgroundResource(R.mipmap.ball_unselect);
            foundView.setPaintFlags(0);
        }
    }

    public void setBallDoNotClick() {
        ArrayList<Integer> except1 = new ArrayList<>(AutoGenActivity.selectedNumber);
        ArrayList<Integer> except2 = new ArrayList<>(AutoGenActivity.exceptNumber);

        if(REQUEST_CODE_DIRECT == 100){
            if(except2.size() > 0){
                for(int i=0; i<except2.size(); i++){
                    TextView view = (TextView) linearLayout.findViewById(except2.get(i));
                    view.setBackgroundResource(R.mipmap.ball_unselect_with_x);
                    view.setPaintFlags(3);
                }
            }
        } else if (REQUEST_CODE_DIRECT == 200){
            if(except1.size() > 0){
                for(int i=0; i<except1.size(); i++){
                    TextView view = (TextView) linearLayout.findViewById(except1.get(i));
                    view.setBackgroundResource(R.mipmap.ball_unselect_with_x);
                    view.setPaintFlags(3);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
