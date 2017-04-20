package com.ahchim.android.ritto;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * Created by Gold on 2017. 4. 11..
 */

public class LocationAdapter extends BaseAdapter {

    public final int VIEW_TYPE_LOCA = 0;
    public final int VIEW_TYPE_DETAIL = 1;
    public final int TYPE_COUNT = 2;

    Context mContext;
    List<String> locationArray;
    //ArrayList<ArrayList<String>> detail;
    LayoutInflater layoutInflater;
    int depthFlag = 0;

    public LocationAdapter(Context context, List<String> array, ArrayList<ArrayList<String>> detail, int Flag) {
        mContext = context;
        locationArray = array;
        //this.detail = detail;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        depthFlag = Flag;
        Log.e("adapter_detail", "==================" + detail);
        //Log.e("depthFlag", "==================" + depthFlag);

//        for (int i = 0; i < detail.size(); i++) {
////            Log.e("adapter_locationArray","==================" + locationArray.get(i));
//            Log.e("adapter_detail", "==================" + detail.get(i));
//        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        //return super.getItemViewType(position);
//
//        if(depthFlag < 3 ){
//            return VIEW_TYPE_LOCA;
//        } else {
//            return VIEW_TYPE_DETAIL;
//        }
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        //return super.getViewTypeCount();
//        return TYPE_COUNT;
//    }

    @Override
    public int getCount() {

        if (NationStoreActivity.LOCATION_DEPTH_FLAG < 3) {
            return locationArray.size();
        } else if (NationStoreActivity.LOCATION_DEPTH_FLAG == 3) {
            return NationStoreActivity.detailLoc.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (NationStoreActivity.LOCATION_DEPTH_FLAG < 3) {
            //Log.e("depthFlag < 3", "==================" + NationStoreActivity.LOCATION_DEPTH_FLAG);
            return getLocationView(position, convertView, parent);
        } else if (NationStoreActivity.LOCATION_DEPTH_FLAG == 3) {
            //Log.e("depthFlag == 3", "==================" + NationStoreActivity.LOCATION_DEPTH_FLAG);
            return getLocationDetailView(position, convertView, parent);
        }
        return convertView;
    }

    public View getLocationView(int position, View convertView, ViewGroup parent) {

        //Log.e("Normal - View - ENTER", "============================================");

        //TODO : ViewHolder 구현을 해야 성능이 보장될 것이다.
        convertView = layoutInflater.inflate(R.layout.location_item, parent, false);

//        Log.e("adapter_position", "==========" + position);
//        Log.e("locationArray", "==========" + locationArray);
//        Log.e("location_(position)", "==========" + locationArray.get(position));

        TextView tv_location = (TextView) convertView.findViewById(R.id.tv_location);
        tv_location.setText(locationArray.get(position));

        return convertView;
    }


    public View getLocationDetailView(int position, View convertView, ViewGroup parent) {

        //Log.e("DetailView - ENTER", "============================================");

        //TODO : ViewHolder 구현을 해야 성능이 보장될 것이다.
        convertView = layoutInflater.inflate(R.layout.location_item_detail, parent, false);

        TextView tv_sangho = (TextView) convertView.findViewById(R.id.tv_sangho);
        TextView tv_address = (TextView) convertView.findViewById(R.id.tv_address);
        TextView tv_phoneNum = (TextView) convertView.findViewById(R.id.tv_phoneNum);
        TextView tv_latitude = (TextView) convertView.findViewById(R.id.tv_latitude);
        TextView tv_longitude = (TextView) convertView.findViewById(R.id.tv_longitude);

        Log.e("detailLoc", "===" + NationStoreActivity.detailLoc);

        tv_sangho.setText(NationStoreActivity.detailLoc.get(position).get(0));
        tv_address.setText(NationStoreActivity.detailLoc.get(position).get(1));
        tv_phoneNum.setText(NationStoreActivity.detailLoc.get(position).get(2));
        tv_latitude.setText(NationStoreActivity.detailLoc.get(position).get(3));
        tv_longitude.setText(NationStoreActivity.detailLoc.get(position).get(4));

        return convertView;
    }


    public class ViewHolder{

    }


}



