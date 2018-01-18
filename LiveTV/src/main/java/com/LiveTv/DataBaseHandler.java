package com.LiveTv;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by creativeinfoway2 on 26/09/16.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;      // database version
    private static final String DATABASE_NAME = "FileDB";   // databse name

    private static final String TABLE_NAME = "Recent ";     // table 1
    private static final String KEY_CHANNEL_NAME = "channel_name";   // table 1 data
    private static final String KEY_CHANNEL_IMAGE = "channel_image";
    private static final String KEY_CHANNEL_LINK = "channel_link";
    private static final String KEY_ID = "id";


    private static final String TABLE_FAVOURITE = "Favourite";   // table 2
    private static final String KEY_CHANNEL_NAME_FAVOURITE = "channel_name_favourite ";  // table 2 data
    private static final String KEY_CHANNEL_IMAGE_FAVOURITE = "channel_image_favourite";

    String columnRecent[] = {KEY_CHANNEL_NAME, KEY_CHANNEL_IMAGE,KEY_CHANNEL_LINK};
    String columnFavourite[] = {KEY_CHANNEL_NAME_FAVOURITE, KEY_CHANNEL_IMAGE_FAVOURITE,KEY_CHANNEL_LINK};

    ArrayList<String> recentItem = new ArrayList<>();

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        createDb();
    }

    private void createDb() {
        SQLiteDatabase db = this.getWritableDatabase();
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_file_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CHANNEL_NAME + " TEXT," + KEY_CHANNEL_IMAGE + " TEXT," + KEY_CHANNEL_LINK + " TEXT);";
        String CREATE_file_SUBCATEGORY = "CREATE TABLE IF NOT EXISTS " + TABLE_FAVOURITE + " (" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_CHANNEL_NAME_FAVOURITE + " TEXT," + KEY_CHANNEL_IMAGE_FAVOURITE + " TEXT," + KEY_CHANNEL_LINK + " TEXT);";

        db.execSQL(CREATE_file_TABLE);
        db.execSQL(CREATE_file_SUBCATEGORY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_FAVOURITE);

        onCreate(db);
    }

    public void addRecentItem(String name, String image,String link) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CHANNEL_NAME, name);
        values.put(KEY_CHANNEL_IMAGE, image);
        values.put(KEY_CHANNEL_LINK,link);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<DataContainer> getRecentItem() {
        ArrayList<DataContainer> FileList = new ArrayList<DataContainer>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataContainer dataContainer = new DataContainer();
                dataContainer.title = cursor.getString(1);
                dataContainer.image = cursor.getString(2);
                dataContainer.link = cursor.getString(3);
                FileList.add(dataContainer);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return FileList;
    }

    public boolean dataAddedAlreadyorNot(String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, columnRecent, KEY_CHANNEL_NAME + " =?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                return true;
            } while (cursor.moveToNext());
        } else {
            return false;
        }
    }

    public void addChannelToFavourite(String channelAddToFavourite, String image,String link) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_CHANNEL_NAME_FAVOURITE, channelAddToFavourite);
        values.put(KEY_CHANNEL_IMAGE_FAVOURITE, image);
        values.put(KEY_CHANNEL_LINK, link);
        db.insert(TABLE_FAVOURITE, null, values);
        db.close();
    }

    public ArrayList<DataContainer> getFavouriteItem() {

        ArrayList<DataContainer> FileList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVOURITE;
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DataContainer dataContainer = new DataContainer();
                dataContainer.title = cursor.getString(1);
                dataContainer.image = cursor.getString(2);
                dataContainer.link = cursor.getString(3);
                FileList.add(dataContainer);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return FileList;
    }

    public void delete(String channelName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVOURITE, KEY_CHANNEL_NAME_FAVOURITE + " =? " ,new String[]{channelName});
        db.close();
    }

    public boolean channelAlereadyAddedToFavouriteorNot(String name) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_FAVOURITE, columnFavourite, KEY_CHANNEL_NAME_FAVOURITE + " =?", new String[]{name}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                return true;

            } while (cursor.moveToNext());
        } else {
            return false;

        }

    }
}
