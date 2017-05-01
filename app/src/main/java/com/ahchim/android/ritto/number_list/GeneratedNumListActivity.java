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
import android.widget.CompoundButton;
import android.widget.ListView;

import com.ahchim.android.ritto.R;
import com.ahchim.android.ritto.model.SavedNumber;

import java.util.ArrayList;
import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class GeneratedNumListActivity extends AppCompatActivity {

    ListView listView;
    Realm realm;
    RealmResults<SavedNumber> results;

    ArrayList<SavedNumber> searchResult;
    ArrayList<SavedNumber> deleteTemp;
    ArrayList<Integer> temp;

    GenNumList_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("생성 번호 목록");

        setContentView(R.layout.activity_generated_num_list);

        searchResult = new ArrayList<>();
        deleteTemp = new ArrayList<>();
        temp = new ArrayList<>();

        realm = Realm.getDefaultInstance();
        importSaveData();

        listView = (ListView) findViewById(R.id.list1);

        adapter = new GenNumList_Adapter(this, searchResult, R.layout.divider_layout);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                Log.e("클릭리스너","들어오니? ===================");
                final CheckBox check = (CheckBox) view.findViewById(R.id.check);
                check.setFocusable(false);
                check.setClickable(false);
                //check.setChecked(listView.isItemChecked(position));

                if (check.isChecked()){
                    check.setChecked(false);
                    temp.remove((Object)position);
                    Log.e("temp","" + temp);
                }else {
                    check.setChecked(true);
                    temp.add(position);
                    Log.e("temp","" + temp);
                }
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
                Log.e("searchResult","결과================" + searchResult);
            }
        });
    }

    //선택번호삭제
    public void deleteItem() {
        for(int i : temp){
            searchResult.remove(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gen_num_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.gen_num_delete) {
            deleteItem();
            adapter.notifyDataSetChanged();

            CheckBox checkBox;
            for(int i=0; i<listView.getChildCount(); i++){
                checkBox = (CheckBox) listView.getChildAt(i).findViewById(R.id.check);
                checkBox.setChecked(false);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
