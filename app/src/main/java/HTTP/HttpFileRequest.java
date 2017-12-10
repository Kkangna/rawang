package HTTP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static Application.AppCommon.awsURL;

/**
 * Created by Administrator on 2017-12-07.
 */

public class HttpFileRequest extends AsyncTask<String, Integer, Bitmap> {
    private String TAG = getClass().getSimpleName();
    private URL url = null;
    private JSONObject values = null;
    private String packageName = "";
    private String fileName = "";

    public HttpFileRequest(String appPath, String fileName, String subUrl, JSONObject params){
        try {
            url = new URL(awsURL+subUrl);
            values = params;
            packageName = appPath;
            fileName = fileName;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        try {
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
            bufferedWriter.write(values.toString());
            bufferedWriter.flush();

            int code = connection.getResponseCode();
            Log.d(TAG, "File download server response code : "+ code);
            if (code == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(in);
                in.close();
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap data) {
        super.onPostExecute(data);
        storeImage(data);
    }

    private File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()+ "/Android/data/"+ packageName+ "/Files");
        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        // Create a media file name
        File mediaFile;
        String mImageName= fileName +".png";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        Log.d(TAG,"Output file path : "+mediaFile.getPath());// e.getMessage());
        return mediaFile;
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,"Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
}