package com.example.moodmetrics;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "Login.db";
    private static DBHelper instance;

    private DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users (username TEXT primary key, password TEXT)");
        MyDB.execSQL("create Table moodEntries (username TEXT, mood TEXT, date TEXT)");
        MyDB.execSQL("create Table bmiEntries (username TEXT, bmi REAL, date TEXT)");
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

    @SuppressLint("SimpleDateFormat")
    public void addMoodEntryToDB(String username, String mood, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("mood", mood);
        contentValues.put("date", dateString);
        MyDB.insert("moodEntries", null, contentValues);
    }

    public HashMap<String, Integer> fetchMoodEntries(String username){
        SQLiteDatabase MyDB = this.getReadableDatabase();
        HashMap<String, Integer> moodEntries;
        try (Cursor cursor = MyDB.rawQuery("Select * from moodEntries where username = ?", new String[]{username})) {
            moodEntries = new HashMap<>();
            if (cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                    @SuppressLint("Range") String mood = cursor.getString(cursor.getColumnIndex("mood"));
                    moodEntries.put(date, Integer.parseInt(mood));
                } while (cursor.moveToNext());
            }
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

    @SuppressLint("SimpleDateFormat")
    public void addBmiEntryToDB(String username, double bmi, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(date);
        Log.d("Database", "Inserting BMI entry: Username=" + username + ", BMI=" + bmi + ", Date=" + dateString);
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("bmi", bmi);
        contentValues.put("date", dateString);
        MyDB.insert("bmiEntries", null, contentValues);
    }

    public HashMap<String, Double> fetchBmiEntries(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from bmiEntries where username = ?", new String[]{username});
        HashMap<String, Double> bmiEntries = new HashMap<>();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") double bmi = cursor.getDouble(cursor.getColumnIndex("bmi"));
                bmiEntries.put(date, bmi);
            } while (cursor.moveToNext());
        }
        return bmiEntries;
    }

    public Cursor fetchLatestBmiEntry(String username) {
        SQLiteDatabase MyDB = this.getReadableDatabase();

        Cursor cursor = MyDB.rawQuery("SELECT * FROM bmiEntries WHERE username = ? ORDER BY date DESC LIMIT 1", new String[]{username});

        return cursor;
    }
}