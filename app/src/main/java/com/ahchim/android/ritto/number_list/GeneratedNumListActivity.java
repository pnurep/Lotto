package com.ahchim.android.ritto.number_list;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;

import com.ahchim.android.ritto.R;
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

        GenNumList_Adapter adapter = new GenNumList_Adapter(this, searchResult, R.layout.divider_layout);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("클릭리스너","들어오니? ===================");
                CheckBox check = (CheckBox) view.findViewById(R.id.check);
                check.setChecked(true);
                Log.e("",""+check.isChecked());
            }
        });

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
