package com.bigpictureclue.rawang;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import HTTP.HttpCommonUtil;
import HTTP.HttpPostRequest;

import static Application.AppCommon.INTENT_HTTP_RESULT;
import static Application.AppCommon.INTENT_HTTP_RESULT_EXTRA;
import static Application.AppCommon.SERVER_TABLE_USER;
import static Application.AppCommon.URL_INSERT_USER;
import static Application.AppCommon.URL_SELECT;

/**
 * Created by Administrator on 2017-11-28.
 */

public class AccountActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    private Context mContext;
    private BroadcastReceiver receiver = null;

    private Button buttonPostSend, buttonGetUser, buttonGetRamen;
    private EditText editTextID, edittextType, edittextEmail, edittextNickname;
    private TextView textViewResult;
    private ImageView imageViewRamen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mContext = this;

        buttonPostSend = (Button) findViewById(R.id.button_postSend);
        buttonGetUser = (Button) findViewById(R.id.button_getUser);
        buttonGetRamen = (Button) findViewById(R.id.button_getRamen);
        editTextID = (EditText) findViewById(R.id.edittext_id);
        edittextType = (EditText) findViewById(R.id.edittext_type);
        edittextEmail = (EditText) findViewById(R.id.edittext_email);
        edittextNickname = (EditText) findViewById(R.id.edittext_nickname);
        textViewResult = (TextView) findViewById(R.id.textview_result);
        imageViewRamen = (ImageView) findViewById(R.id.imageview_ramen);

        //Test checking nickname duplication, insert new user
        buttonPostSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Nickname duplication check
                // URL, Table name, option (where...)
                HttpPostRequest checkNickname = new HttpPostRequest(mContext);
                checkNickname.execute(URL_SELECT, SERVER_TABLE_USER, "WHERE nickname='"+edittextNickname.getText().toString()+"'");
            }
        });

        //Test get all user
        buttonGetUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpPostRequest getAllUser = new HttpPostRequest(mContext);
                getAllUser.execute(URL_SELECT, SERVER_TABLE_USER, "");
            }
        });

        buttonGetRamen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, RamenListActivity.class);
                startActivity(intent);
            }
        });

        setReceiver();
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
                if(result.compareTo("[]") == 0) { //No duplicated nickname
                    HttpPostRequest checkNickname = new HttpPostRequest(mContext);
                    checkNickname.execute(URL_INSERT_USER, editTextID.getText().toString(),edittextType.getText().toString(),edittextEmail.getText().toString(),edittextNickname.getText().toString());
                } else if (result.compareTo("Insert user OK") == 0){
                    Toast.makeText(context, "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "닉네임이 중복되었습니다.", Toast.LENGTH_SHORT).show();
                    textViewResult.setText(result);
                }
            }
        };
        registerReceiver(receiver, intentFilter);
    }
}
