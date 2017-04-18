package com.ahchim.android.ritto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import static android.text.InputType.TYPE_CLASS_NUMBER;

public class AutoGenActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSelect1, btnSelect2, btnGen, btnSave;
    public static int REQUEST_CODE_1 = 100;
    public static int REQUEST_CODE_2 = 200;

    public static ArrayList<Integer> selectedNumber = new ArrayList<>();
    public static ArrayList<Integer> exceptNumber = new ArrayList<>();
    ArrayList<Integer> generatedNumber;

    ArrayList<ArrayList<Integer>> allGeneratedNumber = new ArrayList<>();
    ArrayList<ArrayList<Integer>> goToSaveNumber = new ArrayList<>();

    Ascending ascending;

    LinearLayout ll_inner_container;
    LinearLayout ll_inner_container_except;

    ListView listView;
    EditText howManyNum;

    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("로또 번호 생성");
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setContentView(R.layout.activity_auto_gen);

        ll_inner_container = (LinearLayout) findViewById(R.id.ll_inner_container);
        ll_inner_container_except = (LinearLayout) findViewById(R.id.ll_inner_container_except);

        listView = (ListView) findViewById(R.id.listView);

        btnSelect1 = (Button) findViewById(R.id.btnSelect);
        btnSelect2 = (Button) findViewById(R.id.btnSelect2);
        btnGen = (Button) findViewById(R.id.btnGen1);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSelect1.setOnClickListener(this);
        btnSelect2.setOnClickListener(this);
        btnGen.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        howManyNum = (EditText) findViewById(R.id.howManyNum);
        howManyNum.setInputType(TYPE_CLASS_NUMBER);
        howManyNum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)}); //글자수 1자로 제한

        ascending = new Ascending();

        realm = Realm.getDefaultInstance();

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnSelect : //포함하고 싶은 숫자
                intent = new Intent(AutoGenActivity.this, DirectNumSelectActivity.class);
                intent.putExtra("REQUEST_CODE", REQUEST_CODE_1);
                startActivityForResult(intent, REQUEST_CODE_1);
                break;
            case R.id.btnSelect2 : //제외하고 싶은 숫자
                intent = new Intent(AutoGenActivity.this, DirectNumSelectActivity.class);
                intent.putExtra("REQUEST_CODE", REQUEST_CODE_2);
                startActivityForResult(intent, REQUEST_CODE_2);
                break;

            case R.id.btnSave :
                if(goToSaveNumber.size() > 0){
                    saveGenNum(goToSaveNumber);
                } else {
                    if(allGeneratedNumber.size() <= 0){
                        Toast.makeText(this, "번호를 생성해 주세요!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "번호를 선택해 주세요!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.btnGen1 :
                goToSaveNumber.clear();
                int howMany = 0;

                //문자 입력 예방 예외처리
                try{
                    howMany = Integer.parseInt(howManyNum.getText().toString().trim());
                }catch (Exception e){
                    e.printStackTrace();
                }

                if( howMany > 0 && howMany < 6){
                    generateRandomNumber(selectedNumber, exceptNumber, howMany-1);
                    final ListViewAdapter adapter = new ListViewAdapter(this, R.layout.list_view_item_auto_gen, howMany , allGeneratedNumber);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);

                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            for(int i=listView.getAdapter().getCount()-1; i>=0; i--){
                                Log.e("ListView","===========================" + listView);
                                CheckBox check = (CheckBox) listView.findViewById(i);
                                check.setVisibility(View.VISIBLE);
                                check.setFocusable(false);
                                check.setClickable(false);
                                check.setChecked(((ListView)parent).isItemChecked(position));
                            }
                            return false;
                        }
                    });

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Log.e("클릭리스너","들어오니? ===================");
                            CheckBox check = (CheckBox) view.findViewById(position);
                            if(check.getVisibility() == View.VISIBLE){
                                if (check.isChecked()){
                                    check.setChecked(false);
                                    goToSaveNumber.remove(allGeneratedNumber.get(position));
                                    Log.e("position","======================" + position);
                                    Log.e("goToSaveNumber","======================" + goToSaveNumber);
                                } else {
                                    check.setChecked(true);
                                    goToSaveNumber.add(allGeneratedNumber.get(position));
                                    Log.e("position","======================" + position);
                                    Log.e("goToSaveNumber","======================" + goToSaveNumber);
                                }
                            }
                        }
                    });

                } else {
                    Toast.makeText(this, "0 이상 5이하의 정수를 입력하세요!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                //NavUtils.navigateUpFromSameTask(this); : A -> B -> C -> D 의 navi 버튼 클릭 -> A // 아래 깔려있던 액티비티 전부 destroy
                finish();
                return true; // 무슨역할인지는 모름. 아무역할도 아닐수 있음
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            ll_inner_container.removeAllViewsInLayout();
            selectedNumber.clear();
            selectedNumber = (ArrayList<Integer>) data.getSerializableExtra("result");
            Log.e("잘 받았디?","=================" + selectedNumber);
            Collections.sort(selectedNumber, ascending);
            Log.e("정렬됬디?","=================" + selectedNumber);

            inflateNumber(selectedNumber, requestCode);

        } else if(requestCode == 200 && resultCode == Activity.RESULT_OK) {
            ll_inner_container_except.removeAllViewsInLayout();
            exceptNumber.clear();
            exceptNumber = (ArrayList<Integer>) data.getSerializableExtra("result");
            Log.e("잘 받았디?","=================" + exceptNumber);
            Collections.sort(selectedNumber, ascending);
            Log.e("정렬됬디?","=================" + exceptNumber);

            inflateNumber(exceptNumber, requestCode);
        }
    }

    //포함하고싶은숫자, 제외하고싶은숫자 동적뷰 만들기
    public void inflateNumber(ArrayList<Integer> arrayList, int requestCode ){

        View v = View.inflate(this, R.layout.num_select_list, null);
        LinearLayout ll_container = (LinearLayout) v.findViewById(R.id.ll_select);
        ll_container.setGravity(Gravity.LEFT);

        for(int i=0; i < arrayList.size(); i++){
            TextView lottoNum = new TextView(this);
            lottoNum.setId(arrayList.get(i));
            lottoNum.setText(arrayList.get(i) + "");

            Log.e("어레이리스트 "," 값 ==================" + arrayList);
            Log.e("어레이리스트의 "," 인덱스==================" + arrayList.get(i));

            if(lottoNum.getId() < 11){
                lottoNum.setBackgroundResource(R.mipmap.ball_one);
            } else if(lottoNum.getId() < 21) {
                lottoNum.setBackgroundResource(R.mipmap.ball_two);
            } else if (lottoNum.getId() < 31) {
                lottoNum.setBackgroundResource(R.mipmap.ball_three);
            } else if (lottoNum.getId() < 41) {
                lottoNum.setBackgroundResource(R.mipmap.ball_four);
            } else {
                lottoNum.setBackgroundResource(R.mipmap.ball_five);
            }

            lottoNum.setTextSize(20);
            lottoNum.setTextColor(Color.WHITE);
            lottoNum.setTypeface(Typeface.DEFAULT_BOLD);
            lottoNum.setGravity(Gravity.CENTER);
            lottoNum.setPaintFlags(0);

            ll_container.addView(lottoNum);
        }

        if (requestCode == 100){
            ll_inner_container.setGravity(Gravity.LEFT);
            ll_inner_container.addView(v);
        } else if (requestCode == 200) {
            ll_inner_container_except.setGravity(Gravity.LEFT);
            ll_inner_container_except.addView(v);
        }
    }


    public void generateRandomNumber(ArrayList<Integer> selectedNumber, ArrayList<Integer> exceptNumber, int howMany){

        if(allGeneratedNumber != null){
            allGeneratedNumber.clear();
        }

        for (int p = 0; p <= howMany; p++){

            ArrayList<Integer> ranNumber = new ArrayList<>();
            for(int j=1; j<=45; j++){
                ranNumber.add(j);
            }

            //1부터 45까지의 중복없는 숫자를 랜덤하게 섞고,
            Collections.shuffle(ranNumber);
            Log.e("ranNumber","=================" + ranNumber);
            Log.e("selectedNumber","=================" + selectedNumber);
            Log.e("exceptNumber","=================" + exceptNumber);

            //ranNumber에서 포함하고싶은 숫자 제거
            for(int i=0; i<selectedNumber.size(); i++){
                ranNumber.remove(selectedNumber.get(i));
            }
            Log.e("ranNumber, 포함지움","=================" + ranNumber);

            //ranNumber에서 제외하고싶은 숫자 제거
            for(int i=0; i<exceptNumber.size(); i++){
                ranNumber.remove(exceptNumber.get(i));
            }
            Log.e("ranNumber, 제외지움","=================" + ranNumber);

            generatedNumber = new ArrayList<>();

            //만든수에 포함하고싶은숫자 넣어준다.
            for(int k=0; k<6-selectedNumber.size(); k++){
                generatedNumber.add(k, ranNumber.get(k));
            }
            generatedNumber.addAll(selectedNumber);
            Log.e("generatedNumber","=================" + generatedNumber);

            //그 후 정렬
            Collections.sort(generatedNumber, ascending);
            Log.e("Sorted generatedNumber","=================" + generatedNumber);

            allGeneratedNumber.add(p, generatedNumber);
            Log.e("allGeneratedNumber","=================" + allGeneratedNumber);

        }
    }

    //리스트뷰 어댑터클래스
    public class ListViewAdapter extends BaseAdapter{

        Context context;
        LayoutInflater inflater;
        int layout;
        int howMany;
        ArrayList<ArrayList<Integer>> allGeneratedNum = new ArrayList<>();

        public ListViewAdapter(Context context, int layout, int howMany, ArrayList<ArrayList<Integer>> allGeneratedNum){
            this.context = context;
            this.layout = layout;
            this.howMany = howMany;
            this.allGeneratedNum = allGeneratedNum;
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        // 깊은 깨달음! -> getCount() 함수의 리턴값으로 정해준 수가 -> getView()를 그 수만큼 실행시킨다!!!
        @Override
        public int getCount() {
            return howMany;
        }

        @Override
        public Object getItem(int position) {
            return allGeneratedNum.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = inflater.inflate(layout, parent, false);
            }

            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);
            //checkBox.setOnCheckedChangeListener(AutoGenActivity.this);
            checkBox.setId(position);

            TextView num1 = (TextView) convertView.findViewById(R.id.num1);
            TextView num2 = (TextView) convertView.findViewById(R.id.num2);
            TextView num3 = (TextView) convertView.findViewById(R.id.num3);
            TextView num4 = (TextView) convertView.findViewById(R.id.num4);
            TextView num5 = (TextView) convertView.findViewById(R.id.num5);
            TextView num6 = (TextView) convertView.findViewById(R.id.num6);

            //allGeneratedNum.get(position).get(0) -> 이게 중요해.........
            num1.setText(allGeneratedNum.get(position).get(0) + "");
            num2.setText(allGeneratedNum.get(position).get(1) + "");
            num3.setText(allGeneratedNum.get(position).get(2) + "");
            num4.setText(allGeneratedNum.get(position).get(3) + "");
            num5.setText(allGeneratedNum.get(position).get(4) + "");
            num6.setText(allGeneratedNum.get(position).get(5) + "");

            return convertView;
        }
    }

    //만든번호 저장
    public void saveGenNum(ArrayList<ArrayList<Integer>> arrayList){

        if(allGeneratedNumber != null){
            realm.beginTransaction();

            for(ArrayList<Integer> item : arrayList){
                Log.e("item","================" + item);

                SavedNumber savedNumber = realm.createObject(SavedNumber.class);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                String currentDateTimeString = dateFormat.format(new Date());

                savedNumber.setDate(currentDateTimeString);
                savedNumber.setNum1(item.get(0));
                savedNumber.setNum2(item.get(1));
                savedNumber.setNum3(item.get(2));
                savedNumber.setNum4(item.get(3));
                savedNumber.setNum5(item.get(4));
                savedNumber.setNum6(item.get(5));

                Log.e("현재날짜", "======================" + currentDateTimeString);
                Log.e("getNumber","================" + savedNumber.getNum1());
                Log.e("getNumber","================" + savedNumber.getNum2());
                Log.e("getNumber","================" + savedNumber.getNum3());
                Log.e("getNumber","================" + savedNumber.getNum4());
                Log.e("getNumber","================" + savedNumber.getNum5());
                Log.e("getNumber","================" + savedNumber.getNum6());
            }
            realm.commitTransaction();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
        selectedNumber.clear();
        exceptNumber.clear();
    }

    //제네레이트 된 번호들을 모은다. 최대 5개    -->  개 뻘짓의 산물
//    public class GeneratedItemNumber {
//        ArrayList<Integer> item;
//
//        public GeneratedItemNumber(ArrayList<Integer> items) {
//            item = new ArrayList<>();
//            item = items;
//        }
//    }

}
