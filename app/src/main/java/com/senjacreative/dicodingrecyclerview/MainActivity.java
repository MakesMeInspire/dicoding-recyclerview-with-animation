package com.senjacreative.dicodingrecyclerview;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.senjacreative.dicodingrecyclerview.Adapter.RecyclerViewAdapterHome;
import com.senjacreative.dicodingrecyclerview.Config.Link;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    SwipeRefreshLayout srl_home;
    NestedScrollView nsv_home;
    RecyclerView rv_home;
    RecyclerView.LayoutManager rml_home;
    RecyclerViewAdapterHome ra_home;
    ArrayList<String> list_cosName, list_cosPict, list_cosLoc, list_id, list_date, list_desc, list_bg;

    String BackgroundAPI="0";
    JSONArray resultJson = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        srl_home.setRefreshing(true);
        getBaseCosplayer();
        srl_home.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list_cosName.clear();
                list_cosPict.clear();
                list_cosLoc.clear();
                list_id.clear();
                list_bg.clear();
                list_date.clear();
                list_desc.clear();

                ra_home = new RecyclerViewAdapterHome(MainActivity.this, list_cosName, list_cosLoc, list_cosPict, list_id, list_date, list_desc, list_bg);
                rv_home.setAdapter(ra_home);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getBaseCosplayer();
                    }
                },1500);
            }
        });
    }

    void init(){
        srl_home = (SwipeRefreshLayout) findViewById(R.id.srl_home);
        nsv_home = (NestedScrollView) findViewById(R.id.nsv_home);
        rv_home = (RecyclerView) findViewById(R.id.rv_home);

        rv_home.setHasFixedSize(true);
        rml_home = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_home.setLayoutManager(rml_home);

        list_cosLoc = new ArrayList<String>();
        list_cosPict = new ArrayList<String>();
        list_cosName = new ArrayList<String>();
        list_id = new ArrayList<String>();
        list_date = new ArrayList<String>();
        list_desc = new ArrayList<String>();
        list_bg = new ArrayList<String>();
    }

    void getBaseCosplayer(){
        class gtBsCos extends AsyncTask<String, Void, String>{
            @Override
            protected String doInBackground(String... strings) {
                BackgroundAPI="0";
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("status", "1"));

                InputStream is = null;
                String line;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(Link.getBase);
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();
                    is = entity.getContent();
                    Log.e("pass 1", "connection success ");
                } catch (Exception e) {
                    Log.e("Fail 1", e.toString());
                }
                try {
                    BufferedReader reader = new BufferedReader
                            (new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    is.close();
                    BackgroundAPI = sb.toString();
                } catch (Exception e) {
                    Log.e("Fail 2", e.toString());
                }
                return BackgroundAPI;
            }

            @Override
            protected void onPostExecute(String s) {
                Log.d("DATA COSPLAYER", " => "+s);
                if (s.length()>5){
                    saveCosplayer();
                }else {
                    Toast.makeText(MainActivity.this, "Periksa Koneksi Anda!!!", Toast.LENGTH_SHORT).show();
                    srl_home.setRefreshing(false);
                }
            }
        }
        gtBsCos g = new gtBsCos();
        g.execute();
    }

    void saveCosplayer(){
        try {
            JSONObject jsonObj = new JSONObject(BackgroundAPI);
            resultJson = jsonObj.getJSONArray("result");

            for (int i = 0; i < resultJson.length(); i++) {
                JSONObject c = resultJson.getJSONObject(i);

                list_cosName.add(c.getString("name"));
                list_cosLoc.add(c.getString("loc"));
                list_cosPict.add(c.getString("pict"));
                list_id.add(c.getString("id"));
                list_bg.add(c.getString("background"));
                list_desc.add(c.getString("descrip"));
                list_date.add(c.getString("date"));

            }
            if (list_cosName.size() > 0) {
                ra_home = new RecyclerViewAdapterHome(this, list_cosName, list_cosLoc, list_cosPict, list_id, list_date, list_desc, list_bg);
                rv_home.setAdapter(ra_home);
                srl_home.setRefreshing(false);

            } else {
                list_cosName.clear();
                list_cosLoc.clear();
                list_cosPict.clear();

                ra_home = new RecyclerViewAdapterHome(this, list_cosName, list_cosLoc, list_cosPict, list_id, list_date, list_desc, list_bg);
                rv_home.setAdapter(ra_home);
                srl_home.setRefreshing(false);

            }

            // Stop refresh animation
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about_me) {
            aboutMe();
        }

        return super.onOptionsItemSelected(item);
    }

    void aboutMe(){
        final SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.NORMAL_TYPE);
        pDialog.setTitleText("About Me.");
        pDialog.setContentText("Muhammad Iqbal Najih");
        pDialog.setConfirmText("Ok");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                pDialog.dismissWithAnimation();
            }
        });
        pDialog.setCancelable(false);
        pDialog.show();
    }
}
