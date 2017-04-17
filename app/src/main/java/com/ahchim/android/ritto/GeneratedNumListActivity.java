package com.ahchim.android.ritto;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.ahchim.android.ritto.model.SavedNumber;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class GeneratedNumListActivity extends AppCompatActivity {

    ListView listView;
    Realm realm;
    RealmResults<SavedNumber> results;

    ArrayList<SavedNumber> searchResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("생성 번호 목록");

        setContentView(R.layout.activity_generated_num_list);

        realm = Realm.getDefaultInstance();
        importSaveData();

        listView = (ListView) findViewById(R.id.list1);
        GenNumList_Adapter adapter = new GenNumList_Adapter(this, searchResult, R.layout.divider_layout, R.layout.list_view_item_auto_gen);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

    }


    public void importSaveData() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                results = realm.where(SavedNumber.class).findAll();
                Log.e("렘 검색결과", "===========================" + results);

                for (int i = 0; i < results.size(); i++) {
                    searchResult.add(results.get(i));
                }
            }
        });
    }
}
