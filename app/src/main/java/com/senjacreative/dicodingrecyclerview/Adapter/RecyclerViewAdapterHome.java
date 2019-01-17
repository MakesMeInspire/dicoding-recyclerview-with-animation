package com.senjacreative.dicodingrecyclerview.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.senjacreative.dicodingrecyclerview.Detail;
import com.senjacreative.dicodingrecyclerview.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecyclerViewAdapterHome extends RecyclerView.Adapter<RecyclerViewAdapterHome.ViewHolder>{
    private Context mContext;
    //    final private RecyclerViewAdapterPopuler.ItemClickListener mClickListener;
    private ArrayList<String> mName, mPict, mLoc, mId, mDate, mDesc, mBg;

    Runnable runnable;
    int delayAnimate = 200;
    int itemCount;

    Handler controlDelay = new Handler();
    int bindPos, lastBindPos, lastGetDelay;

    private String Price, RealPrice, RupiahPrice;
    private int PriceLength, nonPpn, Ppn, finalPrice;

    public RecyclerViewAdapterHome(Context context, ArrayList<String> cosname, ArrayList<String> cosloc, ArrayList<String> cospict, ArrayList<String> cosid, ArrayList<String> cosdate, ArrayList<String> cosdesc, ArrayList<String> cosbg) {
//        this.mClickListener = mClickListener;
        this.mName = cosname;
        this.mPict = cospict;
        this.mLoc = cosloc;
        this.mId = cosid;
        this.mDate = cosdate;
        this.mDesc = cosdesc;
        this.mBg = cosbg;
        this.mContext = context;
    }

    @Override
    public RecyclerViewAdapterHome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.list_home, parent, false);
        return new RecyclerViewAdapterHome.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterHome.ViewHolder holder, final int position) {
        bindPos = position;

        //lastBindPos untuk mengetahui berapa item yang sudah di scroll down
        if (position > lastBindPos) {
            lastBindPos = bindPos;
        }

        holder.tv_cosName.setText(mName.get(position));
        holder.tv_cosLoc.setText(mLoc.get(position));
        Picasso.with(mContext).load(mPict.get(position)).placeholder(R.drawable.a_default).into(holder.iv_cosPict);

        Log.d("Cursor Posisi", "" + bindPos);
        Log.d("Bind Posisi", "" + lastBindPos);

        holder.cv_home.setVisibility(View.INVISIBLE);
        setAnimation(holder.cv_home);
        controlDelay();

        holder.cv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(mContext, Detail.class);

                Bundle c = new Bundle();
                c.putString("name", mName.get(position));
                c.putString("date", mDate.get(position));
                c.putString("loc", mLoc.get(position));
                c.putString("desc", mDesc.get(position));
                c.putString("bg", mBg.get(position));
                c.putString("pict", mPict.get(position));
                a.putExtras(c);

                mContext.startActivity(a);
                Activity now = (Activity)mContext;
                now.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    private void setAnimation(final View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
                if (view != null) {
                    view.startAnimation(animation);
                    view.setVisibility(View.VISIBLE);
                    runnable = this;
                    Log.d("Animate", "running");
                }
            }
        }, delayAnimate);
        delayAnimate += 200;
    }

    //Mengembalikan delay ke 0 jika animasi tidak berjalan
    void controlDelay() {
        controlDelay.postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable = this;
                if (lastGetDelay == delayAnimate) {
                    delayAnimate = 0;
                    controlDelay.removeCallbacks(runnable);
                    Log.d("ControlDelay", "OFF");
                } else {
                    controlDelay.postDelayed(runnable, 600);
                    Log.d("ControlDelay", "ON");
                    lastGetDelay = delayAnimate;
                }
            }
        }, 600);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView iv_cosPict;
        TextView tv_cosName, tv_cosLoc;
        CardView cv_home;


        public ViewHolder(final View itemView) {
            super(itemView);

            iv_cosPict = (ImageView) itemView.findViewById(R.id.iv_cosPict);
            tv_cosName = (TextView) itemView.findViewById(R.id.tv_cosName);
            tv_cosLoc = (TextView) itemView.findViewById(R.id.tv_cosLoc);
            cv_home = (CardView) itemView.findViewById(R.id.cv_home);

        }
    }
}
