package Application;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017-11-29.
 */

public class AppCommon extends Application{


    public static final String awsURL = "s";


    public static final String SERVER_DB = "rawang";
    public static final String SERVER_TABLE_USER = "user";
    public static final String SERVER_TABLE_RAMEN = "ramen";

    public static final String URL_SELECT_ALLUSER = "/select-user";
    public static final String URL_DOWNLOAD = "/download";
    public static final String URL_SELECT = "/select";
    public static final String URL_INSERT_USER = "/insert/user";

    public static final String INTENT_HTTP_RESULT = "rawang.http.broadcast.result";
    public static final String INTENT_HTTP_RESULT_EXTRA = "result";
}
