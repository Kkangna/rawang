package DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import VO.Ramen;

import static DB.DatabaseCommonUtil.*;

/**
 * Created by Administrator on 2017-12-05.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private String TAG = getClass().getSimpleName();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RAMEN_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ RAMEN_TABLE_NAME );
        onCreate(db);
    }


    //Execute inserting a set of JSON data
    //PARAM - table : insert table name , row : a set of JSON data
    public void insertRamenData(Ramen row) {
        Log.d(TAG, "insertData called : " + row.getId());
        SQLiteDatabase db = this.getWritableDatabase();

        String sql                      =   "INSERT INTO ramen (id, title, timer, type, description, image) VALUES(?,?,?,?,?,?)";
        SQLiteStatement insertStmt      =   db.compileStatement(sql);
        insertStmt.clearBindings();
        insertStmt.bindLong(1, row.getId());
        insertStmt.bindString(2,row.getTitle());
        insertStmt.bindLong(3, row.getTimer());
        insertStmt.bindLong(4, row.getType());
        insertStmt.bindString(5,row.getDescription());
        insertStmt.bindBlob(6, row.getImage());
        insertStmt.executeInsert();
        db.close();
    }

    //Execute selecting all data of table
    //PARAM - table : select table name
    //RETURN - All selected data of table as JSONArray
    public ArrayList<Ramen> selectData(String table){
        Log.d(TAG, "selectData called : " + table);
        ArrayList<Ramen> result = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+table, null);
        try {
            result = setSelectedData(table, c);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "selectData result count : " + c.getCount());
        c.close();
        db.close();
        return result;
    }

    //Set the row data as inserting query
    //PARAM - table : insert table name , row : a set of JSON data
    //RETURN - insert query
    protected ContentValues setInsertQeury(String table, JSONObject row) throws JSONException{
        ContentValues values = new ContentValues();

        if(table.compareTo(RAMEN_TABLE_NAME) == 0) {
            values.put(RAMEN_KEY_ID, row.getInt(RAMEN_KEY_ID));
            values.put(RAMEN_KEY_TITLE, row.getString(RAMEN_KEY_TITLE));
            values.put(RAMEN_KEY_TIMER, row.getInt(RAMEN_KEY_TIMER));
            values.put(RAMEN_KEY_TYPE, row.getInt(RAMEN_KEY_TYPE));
            values.put(RAMEN_KEY_DESCRIPTION, row.getString(RAMEN_KEY_DESCRIPTION));
        } else
            Log.d(TAG, "Insert request table name is wrong: " + table);

        return values;
    }

    protected ArrayList<Ramen> setSelectedData(String table, Cursor c) throws JSONException{
        ArrayList<Ramen> result = new ArrayList<Ramen>();
        if (table.compareTo(RAMEN_TABLE_NAME) == 0) { //Case of ramen table
            if (c.moveToFirst()) {
                do {
                   Ramen row = new Ramen(c.getInt(0), c.getString(1), c.getInt(2), c.getInt(3), c.getString(4), c.getBlob(5));
                   result.add(row);
                } while (c.moveToNext());
            }
        }else
            Log.d(TAG, "Select request table name is wrong: " + table);

        Log.d(TAG, "JSONArray result : " + result.toString());
        return result;
    }
}
