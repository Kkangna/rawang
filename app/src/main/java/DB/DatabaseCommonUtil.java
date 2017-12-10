package DB;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017-12-07.
 */

public class DatabaseCommonUtil {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "rawang";
    public static final String RAMEN_TABLE_NAME = "ramen";
    public static final String RAMEN_KEY_ID = "id";
    public static final String RAMEN_KEY_TITLE= "title";
    public static final String RAMEN_KEY_TIMER= "timer";
    public static final String RAMEN_KEY_TYPE= "type";
    public static final String RAMEN_KEY_DESCRIPTION = "description";
    public static final String RAMEN_KEY_IMAGE = "image";

    public static final String CREATE_RAMEN_TABLE = "CREATE TABLE " +RAMEN_TABLE_NAME+" ("+
            RAMEN_KEY_ID+     " INTEGER NOT NULL," +
            RAMEN_KEY_TITLE+ " TEXT," +
            RAMEN_KEY_TIMER+ " INTEGER," +
            RAMEN_KEY_TYPE+  " INTEGER," +
            RAMEN_KEY_DESCRIPTION+  " TEXT," +
            RAMEN_KEY_IMAGE+ " BLOB)";

}
