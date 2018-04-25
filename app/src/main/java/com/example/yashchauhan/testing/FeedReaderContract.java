package com.example.yashchauhan.testing;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

import static com.example.yashchauhan.testing.FeedReaderContract.FeedEntry.TABLE_NAME;

/**
 * Created by yashchauhan on 14/04/18.
 */

public class FeedReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private FeedReaderContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_TITLE + " TEXT," +
                    FeedEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static class FeedReaderDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "FeedReader.db";
//        public SQLiteDatabase db;
        private Context context;
        public FeedReaderDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        public void onCreate(SQLiteDatabase db) {

            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
        long adddata(String title,String subtitle){
//             Gets the data repository in write mode
//            db.execSQL(SQL_CREATE_ENTRIES);

            SQLiteDatabase db = this.getWritableDatabase();
//            db = getWritableDatabase();
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(FeedEntry.COLUMN_NAME_TITLE, title);
            values.put(FeedEntry.COLUMN_NAME_SUBTITLE, subtitle);

            // Insert the new row, returning the primary key value of the new row
            long newRowId = db.insert(TABLE_NAME, null, values);
//            db.close();
            return newRowId;
        }

        public void clearDatabase() {
            String TABLE_NAME1= TABLE_NAME;
            SQLiteDatabase db = this.getWritableDatabase();
            String clearDBQuery = "DELETE FROM "+TABLE_NAME1;
            db.execSQL(clearDBQuery);
        }

        ArrayList getdata(){

            SQLiteDatabase db = this.getReadableDatabase();

//            // Define a projection that specifies which columns from the database
//// you will actually use after this query.
//            String[] projection = {
//                    BaseColumns._ID,
//                    FeedEntry.COLUMN_NAME_TITLE,
//                    FeedEntry.COLUMN_NAME_SUBTITLE
//            };
//
//// Filter results WHERE "title" = 'My Title'
//            String selection = FeedEntry.COLUMN_NAME_TITLE + " = ?";
//            String[] selectionArgs = { "My Title" };

// How you want the results sorted in the resulting Cursor
            String sortOrder =
                    FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

            Cursor cursor = db.query(
                    TABLE_NAME,   // The table to query
                    null,             // The array of columns to return (pass null to get all)
                    FeedEntry.COLUMN_NAME_TITLE,              // The columns for the WHERE clause
                    null,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null               // The sort order
            );
//            Cursor cursor1 = db.
            ArrayList<String> itemIds = new ArrayList<String>();
//            List itemIds = new ArrayList<>();
            while(cursor.moveToNext()) {
                String itemId = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_TITLE));
                itemIds.add(itemId);
                Log.d("Inside while loop",FeedEntry.COLUMN_NAME_TITLE);
            }
            cursor.moveToFirst();
            cursor.moveToPrevious();
            while(cursor.moveToNext()) {
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_NAME_SUBTITLE));
                itemIds.add(name);
                Log.d("Inside while loop",FeedEntry.COLUMN_NAME_TITLE);
            }
            cursor.close();
        return itemIds;
        }
    }
}


