package com.bigpictureclue.rawang;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
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

    public static final int READ_TIMEOUT = 15000;
    public static final int CONNECTION_TIMEOUT = 15000;
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
                HttpSelectRequest selectRequest = new HttpSelectRequest();
                selectRequest.execute("user","nickname",edittextNickname.getText().toString());
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

    protected String getHttpResponse(HttpURLConnection connection) throws IOException{
        String inputLine;
        //Create a new InputStreamReader
        InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
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
        return stringBuilder.toString();
    }

    public class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        @Override
        protected String doInBackground(String... params){
            String result;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(awsURL+"select-user");
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                //Connect to our url
                connection.connect();
                result = getHttpResponse(connection);
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

    public class HttpPostRequest extends AsyncTask<String, Void, String> {
        public String REQUEST_METHOD = "POST";
        @Override
        protected String doInBackground(String... params){
            String result;
            String inputLine;
            try {
                //  ContentValues values = new ContentValues();
                JSONObject values = new JSONObject();
                values.put("id", editTextID.getText().toString());
                values.put("type", edittextType.getText().toString());
                values.put("email", edittextEmail.getText().toString());
                values.put("nickname", edittextNickname.getText().toString());

                //Create a URL object holding our url
                URL myUrl = new URL(awsURL+"insert-user");
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                //Set methods and timeouts
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches(false);
                OutputStream  outputStream = connection.getOutputStream();
                BufferedWriter  bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(values.toString());
                bufferedWriter.flush();

                int statusCode = connection.getResponseCode();
                result = "The status code is " + statusCode;
            }
            catch(IOException e){
                result = "IO EXCEPTION";
                e.printStackTrace();
            } catch (JSONException e) {
                result = "JSON EXCEPTION";
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            textViewResult.setText(result);
        }
    }

    public class HttpSelectRequest extends AsyncTask<String, Void, String> {
        public String REQUEST_METHOD = "POST";
        @Override
        protected String doInBackground(String... params){
            String result;
            String inputLine;
            try {
                //  ContentValues values = new ContentValues();
                JSONObject values = new JSONObject();
                values.put("table", params[0]);
                values.put("column", params[1]);
                values.put("filter", params[2]);

                //Create a URL object holding our url
                URL myUrl = new URL(awsURL+"select");
                //Create a connection
                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                //Set methods and timeouts
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("charset", "utf-8");
                connection.setUseCaches(false);
                OutputStream  outputStream = connection.getOutputStream();
                BufferedWriter  bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
                bufferedWriter.write(values.toString());
                bufferedWriter.flush();
                int statusCode = connection.getResponseCode();
                if(statusCode == 200)
                    result = getHttpResponse(connection);
                else
                    result = "FAIL";
            }
            catch(IOException e){
                result = "FAIL : IO EXCEPTION";
                e.printStackTrace();
            } catch (JSONException e) {
                result = "FAIL : JSON EXCEPTION";
                e.printStackTrace();
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            if(result.equals("[]")){
                HttpPostRequest postRequest = new HttpPostRequest();
                postRequest.execute();
            } else
                Toast.makeText(mContext, "닉네임이 중복되었습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
