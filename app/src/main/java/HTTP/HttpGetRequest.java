package HTTP;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static Application.AppCommon.awsURL;
import static HTTP.HttpCommonUtil.CONNECTION_TIMEOUT;
import static HTTP.HttpCommonUtil.READ_TIMEOUT;
import static HTTP.HttpCommonUtil.getHttpResponse;

/**
 * Created by Administrator on 2017-12-07.
 */

//Http get Request
public class HttpGetRequest extends AsyncTask<String, Void, String> {
    private String TAG = getClass().getSimpleName();
    private URL url;

    public HttpGetRequest(String subUrl){
        try {
            url = new URL(awsURL+subUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected String doInBackground(String... params){
        String result = "";
        try {
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.connect();
            int code = connection.getResponseCode();
            Log.d(TAG, "Server response code : "+ code);
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
        Log.d(TAG, "HttpGetRequest response message : "+ result);
    }
}