package HTTP;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static Application.AppCommon.INTENT_HTTP_RESULT;
import static Application.AppCommon.INTENT_HTTP_RESULT_EXTRA;
import static Application.AppCommon.awsURL;
import static HTTP.HttpCommonUtil.getHttpResponse;

/**
 * Created by Administrator on 2017-12-07.
 */

public class HttpPostRequest extends AsyncTask<String, Void, String> {
    private String TAG = getClass().getSimpleName();
    private Context mContext;

    public HttpPostRequest(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected String doInBackground(String... params){
        String result = "";
        try {
            URL url = new URL(awsURL+params[0]);

            Log.d(TAG, "HttpPostRequest params : "+ params);
            //Create a connection
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            //Set methods and timeouts
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setUseCaches(false);
            OutputStream outputStream = connection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(HttpCommonUtil.setJSONObject(params).toString());
            bufferedWriter.flush();

            int code = connection.getResponseCode();
            Log.d(TAG, "HttpPostRequest server response code : "+ code);
            if(code == HttpURLConnection.HTTP_OK)
                result = getHttpResponse(connection);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        Log.d(TAG, "HttpPostRequest response message : "+ result);
        Intent intent = new Intent();
        intent.setAction(INTENT_HTTP_RESULT);
        intent.putExtra(INTENT_HTTP_RESULT_EXTRA,result);
        mContext.sendBroadcast(intent);
    }
}
