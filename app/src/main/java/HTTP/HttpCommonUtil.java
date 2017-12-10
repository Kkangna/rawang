package HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static Application.AppCommon.URL_DOWNLOAD;
import static Application.AppCommon.URL_INSERT_USER;
import static Application.AppCommon.URL_SELECT;
import static Application.AppCommon.awsURL;

/**
 * Created by Administrator on 2017-12-06.
 */



public class HttpCommonUtil {

    public static final int READ_TIMEOUT = 5000;
    public static final int CONNECTION_TIMEOUT = 5000;

    //Cast server response stream data to String type
    //PARAM - Http connection
    //RETURN - Response string
    public static String getHttpResponse(HttpURLConnection connection) throws IOException{
        String inputLine;

        InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
        BufferedReader reader = new BufferedReader(streamReader);
        StringBuilder stringBuilder = new StringBuilder();
        //Check if the line we are reading is not null
        while((inputLine = reader.readLine()) != null){
            stringBuilder.append(inputLine);
        }
        reader.close();
        streamReader.close();

        return stringBuilder.toString();
    }

    public static JSONObject setJSONObject(String...params) {
        JSONObject values = new JSONObject();
        try {
            if(params[0].compareTo(URL_INSERT_USER) == 0) {
                values.put("id", params[1]);
                values.put("type", params[2]);
                values.put("email", params[3]);
                values.put("nickname", params[4]);
            }else if(params[0].compareTo(URL_SELECT) == 0) {
                values.put("table", params[1]);
                values.put("option", params[2]);
            } else if (params[0].compareTo(URL_DOWNLOAD) == 0){
                values.put("category", params[1]);
                values.put("id", params[2]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return values;
    }
}
