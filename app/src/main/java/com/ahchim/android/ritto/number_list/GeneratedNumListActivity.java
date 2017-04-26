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
import android.widget.ListView;

import com.ahchim.android.ritto.R;
import com.ahchim.android.ritto.model.SavedNumber;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class GeneratedNumListActivity extends AppCompatActivity implements ListView.MultiChoiceModeListener, ListView.OnItemClickListener {

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
        listView.setMultiChoiceModeListener(this);
        listView.setOnItemClickListener(this);
        GenNumList_Adapter adapter = new GenNumList_Adapter(this, searchResult, R.layout.divider_layout);
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


    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        Log.e("ActionMode","==================" + mode);
        Log.e("position","==================" + position);
        Log.e("id","==================" + id);
        Log.e("checked","==================" + checked);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Log.e("ActionMode","==================" + mode);
        Log.e("menu","==================" + menu);
        return false;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        Log.e("ActionMode","==================" + mode);
        Log.e("menu","==================" + menu);
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        Log.e("ActionMode","==================" + mode);
        Log.e("MenuItem","==================" + item);
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        Log.e("ActionMode","==================" + mode);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
