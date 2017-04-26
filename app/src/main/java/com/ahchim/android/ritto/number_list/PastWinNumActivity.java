package com.ahchim.android.ritto.number_list;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ahchim.android.ritto.util.Loader;
import com.ahchim.android.ritto.R;
import com.ahchim.android.ritto.model.WinListPojo;
import com.ahchim.android.ritto.util.DeviceUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PastWinNumActivity extends AppCompatActivity implements Loader.CallBack {

    RecyclerView recyclerView;
    DatabaseReference reference;

    List<WinListPojo> roundList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("지난 당첨 번호");

        setContentView(R.layout.activity_past_win_num);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = database.getReference("round");

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("지금까지의 회차정보를 가져오는 중입니다...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {  //데이터 다 받아왔을때 콜백
                int count = 0;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    WinListPojo winListPojo = dataSnapshot1.getValue(WinListPojo.class);
                    roundList.add(count, winListPojo);
                    count++;
                    Log.e("roundList "," ,count" + roundList.size() + "----" +count);
                }
                setList(roundList);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void load() {
        String url="http://www.nlotto.co.kr/";
        String round = "750";
        Loader.getList(url,round,this);
    }


    private void setList(List<WinListPojo> winInfo){
        recyclerView = (RecyclerView) findViewById(R.id.recycler_win_list);
        RecyclerAdapter adapter = new RecyclerAdapter(this, winInfo);
        adapter.setView(recyclerView);
        // precache 적용
        PreCachingLayoutManager manager = new PreCachingLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        manager.setExtraLayoutSpace(DeviceUtils.getScreenHeight(this));

        recyclerView.setLayoutManager(manager);
    }


    @Override
    public void call(List list) {
        //setList((ArrayList<String>) list);
    }


}
