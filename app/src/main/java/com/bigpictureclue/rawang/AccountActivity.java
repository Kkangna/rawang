package com.bigpictureclue.rawang;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static Application.AppCommon.awsURL;

/**
 * Created by Administrator on 2017-11-28.
 */

public class AccountActivity extends AppCompatActivity {

    private Button buttonPostSend, buttonGetUser;
    private EditText editTextID,edittextType, edittextEmail,edittextNickname;
    private TextView textViewResult;
    private Context mContext;
    private URL url = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        mContext = this;

        buttonPostSend =(Button)findViewById(R.id.button_postSend);
        buttonGetUser =(Button)findViewById(R.id.button_getUser);
        editTextID =(EditText)findViewById(R.id.edittext_id);
        edittextType =(EditText)findViewById(R.id.edittext_type);
        edittextEmail =(EditText)findViewById(R.id.edittext_email);
        edittextNickname =(EditText)findViewById(R.id.edittext_nickname);
        textViewResult =(TextView)findViewById(R.id.textview_result);

        buttonPostSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonGetUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HttpGetRequest getRequest = new HttpGetRequest();
                getRequest.execute();

            }
        });
    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        @Override
        protected String doInBackground(String... params){
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(awsURL+"persons");
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();

            }
            catch(IOException e){
                e.printStackTrace();
                result = null;
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            textViewResult.setText(result);
        }
    }
}
