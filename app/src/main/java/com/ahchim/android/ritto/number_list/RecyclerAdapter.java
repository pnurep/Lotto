package com.ahchim.android.ritto.number_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahchim.android.ritto.R;
import com.ahchim.android.ritto.model.WinListPojo;

import java.util.List;

/**
 * Created by Gold on 2017. 4. 19..
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    Context context;
    List<WinListPojo> roundInfo;

    public RecyclerAdapter(Context context, List<WinListPojo> info){
        this.context = context;
        roundInfo = info;
    }

    public void setView(@NonNull RecyclerView listView){
        listView.setAdapter(this);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.winlist, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.setRound(roundInfo.get(position).getDrwNo());
        holder.setWhen(roundInfo.get(position).getDrwNoDate());
        holder.setTv_winPrice(roundInfo.get(position).getFirstWinamnt());
        holder.makeNum(roundInfo.get(position).getDrwtNo1(), holder.win_num1);
        holder.makeNum(roundInfo.get(position).getDrwtNo2(), holder.win_num2);
        holder.makeNum(roundInfo.get(position).getDrwtNo3(), holder.win_num3);
        holder.makeNum(roundInfo.get(position).getDrwtNo4(), holder.win_num4);
        holder.makeNum(roundInfo.get(position).getDrwtNo5(), holder.win_num5);
        holder.makeNum(roundInfo.get(position).getDrwtNo6(), holder.win_num6);
        holder.makeNum(roundInfo.get(position).getBnusNo(), holder.win_num7);

        Log.i("ITEM","Position================"+position);
    }

    @Override
    public int getItemCount() {
        Log.i("size","" + roundInfo.size());
        return roundInfo.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView tv_round, tv_when, tv_winPrice, win_num1, win_num2, win_num3, win_num4, win_num5, win_num6, win_num7;
        //private LinearLayout ll_winlist_inner_container;
        public Holder(View itemView) {
            super(itemView);
            tv_round = (TextView)itemView.findViewById(R.id.tv_round);
            tv_when = (TextView)itemView.findViewById(R.id.tv_when);
            tv_winPrice = (TextView)itemView.findViewById(R.id.tv_winPrice);
            win_num1 = (TextView)itemView.findViewById(R.id.win_num1);
            win_num2 = (TextView)itemView.findViewById(R.id.win_num2);
            win_num3 = (TextView)itemView.findViewById(R.id.win_num3);
            win_num4 = (TextView)itemView.findViewById(R.id.win_num4);
            win_num5 = (TextView)itemView.findViewById(R.id.win_num5);
            win_num6 = (TextView)itemView.findViewById(R.id.win_num6);
            win_num7 = (TextView)itemView.findViewById(R.id.win_num7);

        }

        public void setRound(String round){
            this.tv_round.setText(round + "회");
        }

        public void setWhen(String when){
            this.tv_when.setText(when);
        }

        public void setTv_winPrice(String winPrice){
            if(!winPrice.equals("0")){
                this.tv_winPrice.setText(winPrice + "원");
            }
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
}
