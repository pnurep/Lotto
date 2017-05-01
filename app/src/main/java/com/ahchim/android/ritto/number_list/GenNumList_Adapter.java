package com.ahchim.android.ritto.number_list;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ahchim.android.ritto.R;
import com.ahchim.android.ritto.model.SavedNumber;

import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Gold on 2017. 4. 10..
 */

public class GenNumList_Adapter extends BaseAdapter {

    public static final int VIEW_TYPE_DATE = 0;
    public static final int VIEW_TYPE_NUM = 1;
    public static final int TYPE_MAX_COUNT = VIEW_TYPE_DATE + 1;

    Context mContext;
    ArrayList<SavedNumber> results;
    LayoutInflater inflater;
    int divide_layout;
    String date = "";

    Realm realm;


    public GenNumList_Adapter(Context context, ArrayList result, int layout1) {
        mContext = context;
        this.results = result;
        divide_layout = layout1;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        realm = Realm.getDefaultInstance();
    }

    @Override
    public int getCount() {
        return results.size();
    }

//    @Override
//    public int getViewTypeCount() {
//        //return super.getViewTypeCount();
//        return  getCount();
//    }

//    @Override
//    public int getItemViewType(int position) {
//        //return super.getItemViewType(position);
////        if(date.equals(results.get(position).getDate())){
////            Log.e("type : num / getDate","=====================" + results.get(position).getDate());
////            return VIEW_TYPE_NUM;
////        } else {
////            Log.e("type : date / getDate","=====================" + results.get(position).getDate());
////            return VIEW_TYPE_DATE;
////        }
//        return position;
//    }

    @Override
    public Object getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getDateView(position, convertView, parent);
    }

    private View getDateView(int position, View convertView, ViewGroup parent) {

        realm.beginTransaction();

        if (convertView == null) {
            convertView = inflater.inflate(divide_layout, parent, false);
        } else {

        }

        CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.check);
        TextView divider = (TextView) convertView.findViewById(R.id.tv_divider);
        TextView num1 = (TextView) convertView.findViewById(R.id.num1_adapter);
        TextView num2 = (TextView) convertView.findViewById(R.id.num2_adapter);
        TextView num3 = (TextView) convertView.findViewById(R.id.num3_adapter);
        TextView num4 = (TextView) convertView.findViewById(R.id.num4_adapter);
        TextView num5 = (TextView) convertView.findViewById(R.id.num5_adapter);
        TextView num6 = (TextView) convertView.findViewById(R.id.num6_adapter);

        Log.e("visibility", "=======================" + results.get(position).getShow());
        if (results.get(position).getShow() == SavedNumber.NONE) {
            results.get(position).setShow(date.equals(results.get(position).getDate()) ? View.GONE : View.VISIBLE);
            //divider.setVisibility(((results.get(position).getShow() == View.VISIBLE ) ? View.VISIBLE : View.GONE)); -> 왜 안에서 하면 동작을 안할까
        }
        if(results.get(position).getShow() == View.VISIBLE){
            divider.setVisibility(View.VISIBLE);
        }else{
            divider.setVisibility(View.GONE);
        }

        divider.setText(results.get(position).getDate());

//        Log.e("date", "=======================" + date);
//        Log.e("getdate", "=======================" + results.get(position).getDate());
//        Log.e("visibility", "=======================" + results.get(position).getShow());

        //Log.e("position", "=======================" + position);

        //checkBox.setId(position);
        //checkBox.setVisibility(View.GONE);


        makeNum(results.get(position).getNum1() + "", num1);
        makeNum(results.get(position).getNum2() + "", num2);
        makeNum(results.get(position).getNum3() + "", num3);
        makeNum(results.get(position).getNum4() + "", num4);
        makeNum(results.get(position).getNum5() + "", num5);
        makeNum(results.get(position).getNum6() + "", num6);

        date = results.get(position).getDate();

        realm.commitTransaction();

        return convertView;
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
}
