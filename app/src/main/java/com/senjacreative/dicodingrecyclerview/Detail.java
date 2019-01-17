package com.senjacreative.dicodingrecyclerview;

import android.app.Activity;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Detail extends AppCompatActivity {
    CircleImageView civ_cosplayer;
    RelativeLayout rl_detail;
    TextView tv_dCosName,tv_dCosDate,tv_dCosLoc,tv_dCosDesc;
    ImageView iv_background;
    String cName, cDate, cLoc,cDesc, cBd, cPict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        getBundle();
        init();
        setContent();
    }

    void init(){
        civ_cosplayer = (CircleImageView) findViewById(R.id.civ_cosplayer);
        rl_detail = (RelativeLayout) findViewById(R.id.rl_detail);
        tv_dCosName = (TextView) findViewById(R.id.tv_dCosName);
        tv_dCosDate = (TextView) findViewById(R.id.tv_dCosDate);
        tv_dCosLoc = (TextView) findViewById(R.id.tv_dCosLoc);
        tv_dCosDesc = (TextView) findViewById(R.id.tv_dCosDesc);
        iv_background = (ImageView) findViewById(R.id.iv_background);
    }

    void getBundle(){
        Bundle b = this.getIntent().getExtras();
        if(b!=null){
            cName = b.getString("name");
            cDate = b.getString("date");
            cLoc = b.getString("loc");
            cDesc = b.getString("desc");
            cBd = b.getString("bg");
            cPict = b.getString("pict");
        }else {
            cName = "";
            cDate = "";
            cLoc = "";
            cDesc = "";
            cBd = "";
            cPict = "";
        }
    }

    void setContent(){
        Picasso.with(this).load(cPict).placeholder(R.drawable.ic_image_black_24dp).into(civ_cosplayer);
        Picasso.with(this).load(cBd).placeholder(R.drawable.ic_image_black_24dp).into(iv_background);
        tv_dCosName.setText(cName);
        tv_dCosDate.setText(cDate);
        tv_dCosDesc.setText("'' "+cDesc+" ''");
        tv_dCosLoc.setText(cLoc);
    }

    @Override
    public void onBackPressed() {
        finish();
        Activity now = (Activity)this;
        now.overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
    }
}
