package com.example.moodmetrics;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users (username TEXT primary key, password TEXT)");
    }

    public Boolean insertData(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        return result != -1;
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public Boolean checkusernamePassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop Table if exists users");
    }

    @SuppressLint("Range")
    public String getPassword(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if(cursor.moveToFirst()) {
            return cursor.getString(cursor.getColumnIndex("password"));
        }
        return null; // or you can throw an exception if you prefer
    }

    public Boolean addMoodEntryToDB(String username, String mood, String date) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("mood", mood);
        contentValues.put("date", date);
        long result = MyDB.insert("moodEntries", null, contentValues);
        return result != -1;
    }

    public HashMap<String, Integer> fetchMoodEntries(String username){
        // fetch the last 90 days of mood entries and return a hashmap of the dates and moods

           SQLiteDatabase MyDB = this.getReadableDatabase();
           Cursor cursor = MyDB.rawQuery("Select * from moodEntries where username = ?", new String[]{username});
           HashMap<String, Integer> moodEntries = new HashMap<>();
           if(cursor.moveToFirst()) {
               do {
                   String date = cursor.getString(cursor.getColumnIndex("date"));
                   String mood = cursor.getString(cursor.getColumnIndex("mood"));
                   moodEntries.put(date, Integer.parseInt(mood));
               } while (cursor.moveToNext());
           }
           return moodEntries;

    }

    private HashMap<String, Integer> generateMockMoodEntries() {
        HashMap<String, Integer> mockData = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();

        Random random = new Random();

        for (int i = 0; i < 20; i++) {
            Calendar tempCal = (Calendar) cal.clone(); // Clone the original calendar
            tempCal.add(Calendar.DATE, -19 + i); // Subtract 19 days once, then increment each day
            String date = sdf.format(tempCal.getTime());
            int moodValue = 1 + random.nextInt(5);
            mockData.put(date, moodValue);
        }


        return mockData;
    }
}