package com.bigpictureclue.rawang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import DB.DatabaseHandler;
import HTTP.HttpCommonUtil;
import HTTP.HttpPostRequest;
import VO.Ramen;

import static Application.AppCommon.INTENT_HTTP_RESULT;
import static Application.AppCommon.INTENT_HTTP_RESULT_EXTRA;
import static Application.AppCommon.SERVER_TABLE_RAMEN;
import static Application.AppCommon.URL_INSERT_USER;
import static Application.AppCommon.URL_SELECT;
import static DB.DatabaseCommonUtil.RAMEN_KEY_IMAGE;
import static DB.DatabaseCommonUtil.RAMEN_TABLE_NAME;

public class RamenListActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private BroadcastReceiver receiver = null;
    private DatabaseHandler db;

    private ImageView imageViewRamen1,imageViewRamen2,imageViewRamen3,imageViewRamen4;
    private TextView textViewRamen1,textViewRamen2,textViewRamen3,textViewRamen4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ramen_list);
        mContext = this;
        setReceiver();

        imageViewRamen1 = (ImageView) findViewById(R.id.imageview_test1);
        imageViewRamen2 = (ImageView) findViewById(R.id.imageview_test2);
        imageViewRamen3 = (ImageView) findViewById(R.id.imageview_test3);
        imageViewRamen4 = (ImageView) findViewById(R.id.imageview_test4);
        textViewRamen1 = (TextView) findViewById(R.id.textview_test1);
        textViewRamen2 = (TextView) findViewById(R.id.textview_test2);
        textViewRamen3 = (TextView) findViewById(R.id.textview_test3);
        textViewRamen4 = (TextView) findViewById(R.id.textview_test4);

        SharedPreferences sharedpreferences = getSharedPreferences("ramen_pref", Context.MODE_PRIVATE);
        int ramen_ver = sharedpreferences.getInt("version", 0);
        Toast.makeText(mContext, "version : " + ramen_ver, Toast.LENGTH_SHORT).show();

        //Get update ramenList : URL, Table name, option (where...)
        //HttpPostRequest getRamenList = new HttpPostRequest(mContext);
        //getRamenList.execute(URL_SELECT, SERVER_TABLE_RAMEN, "WHERE id > '"+ramen_ver+"'");

        db = new DatabaseHandler(mContext);
        if(ramen_ver == 0)
            init();

        ArrayList<Ramen> result = db.selectData(RAMEN_TABLE_NAME);
        imageViewRamen1.setImageBitmap(BitmapFactory.decodeByteArray( result.get(0).getImage(), 0, result.get(0).getImage().length));
        textViewRamen1.setText(result.get(0).getTitle() + " Time : " + result.get(0).getTimer());
        imageViewRamen2.setImageBitmap(BitmapFactory.decodeByteArray( result.get(1).getImage(), 0, result.get(1).getImage().length));
        textViewRamen2.setText(result.get(1).getTitle() + " Time : " + result.get(1).getTimer());
        imageViewRamen3.setImageBitmap(BitmapFactory.decodeByteArray( result.get(2).getImage(), 0, result.get(2).getImage().length));
        textViewRamen3.setText(result.get(2).getTitle() + " Time : " + result.get(2).getTimer());
        imageViewRamen4.setImageBitmap(BitmapFactory.decodeByteArray( result.get(3).getImage(), 0, result.get(3).getImage().length));
        textViewRamen4.setText(result.get(3).getTitle() + " Time : " + result.get(3).getTimer());
    }

    protected void init(){
        InputStream inputStream = mContext.getResources().openRawResource(R.raw.init);
        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while (( line = buffreader.readLine()) != null) {
                text.append(line);
            }
        } catch (IOException e) {
        }

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(text.toString());

            int i=0;
            for (; i < jsonArray.length(); i++) {
                JSONObject j = jsonArray.getJSONObject(i);

                InputStream ins = mContext.getResources().openRawResource(mContext.getResources().getIdentifier("ramen"+j.get("id"), "raw", mContext.getPackageName()));
                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                int size = 0;
                byte[] buffer = new byte[1024];
                while((size=ins.read(buffer,0,1024))>=0){
                    outputStream.write(buffer,0,size);
                }
                ins.close();
                buffer=outputStream.toByteArray();

                //id, title, timer, type, description, image
                Ramen row = new Ramen(j.getInt("id"),j.getString("title"), j.getInt("timer"), j.getInt("type"), j.getString("description"), buffer);
                db.insertRamenData(row);
            }
            SharedPreferences sharedpreferences = getSharedPreferences("ramen_pref", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
            prefsEditor.putInt("version", i);
            prefsEditor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver != null)
            unregisterReceiver(receiver);
    }

    protected void setReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_HTTP_RESULT);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String result = intent.getCharSequenceExtra(INTENT_HTTP_RESULT_EXTRA).toString();
                Log.d(TAG, "HTTP Response message : "+result);

            }
        };
        registerReceiver(receiver, intentFilter);
    }
}
