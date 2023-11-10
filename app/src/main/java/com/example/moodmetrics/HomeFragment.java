package com.example.moodmetrics;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    DBHelper DB;
    String username;
    public HomeFragment(String u) {
        this.username = u;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DB = DBHelper.getInstance(getContext());

        populateMoodGrid(DB.fetchMoodEntries(username), view);

        //BMI parts
        vBMI(view);

        return view;
    }

    private void vBMI(View view){
        Cursor cursor = DB.fetchLatestBmiEntry(username);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") double latestBmi = cursor.getDouble(cursor.getColumnIndex("bmi"));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
            TextView currentBmi = view.findViewById(R.id.currentBmi);
            TextView bmiInterpretation = view.findViewById(R.id.bmiInterpretation);
            currentBmi.setText("Your current BMI: "+latestBmi);
            bmiInterpretation.setText("Interpretation:"+ MetricsFragment.Interpretation(latestBmi));
            Log.d("Latest BMI", "BMI: " + latestBmi + ", Date: " + date);
        } else {
            Log.d("Latest BMI", "No BMI entry found");
        }
        cursor.close();
    }

    private void populateMoodGrid(HashMap<String, Integer> moodEntries, View view) {
        GridLayout gridLayout = view.findViewById(R.id.monthlyMoodGrid);
        ArrayList<Pair<String, Integer>> sortedMoodList = generateSortedMoodList(moodEntries);

        int i = 0;
        String prevMonth = "";
        for (Pair<String, Integer> moodEntry : sortedMoodList) {
            String date = moodEntry.first;
            int moodValue = moodEntry.second;

            if (i % 7 == 0) {
                addMonthHeaderToGrid(gridLayout, date, prevMonth);
                prevMonth = date.substring(5, 7);
            }

            addMoodViewToGrid(gridLayout, moodValue);
            i++;
        }
    }

    private void addMonthHeaderToGrid(GridLayout gridLayout, String date, String prevMonth) {
        TextView tv = new TextView(getContext());
        tv.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        String currentMonth = date.substring(5, 7);
        if (!currentMonth.equals(prevMonth)) {
            tv.setText(getMonthDisplayString(currentMonth));
            gridLayout.addView(tv);
        } else {
            gridLayout.addView(new TextView(getContext()));
        }
    }

    private String getMonthDisplayString(String month) {
        try {
            SimpleDateFormat monthParse = new SimpleDateFormat("MM");
            SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
            return monthDisplay.format(monthParse.parse(month));
        } catch (ParseException e) {
            e.printStackTrace();
            return month; // fallback to number if parsing fails
        }
    }

    private void addMoodViewToGrid(GridLayout gridLayout, int moodValue) {
        View moodView = new View(getContext());
        moodView.setLayoutParams(createMoodViewLayoutParams());
        moodView.setBackgroundColor(getMoodColor(moodValue));
        gridLayout.addView(moodView);
    }

    private GridLayout.LayoutParams createMoodViewLayoutParams() {
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = (int) getResources().getDimension(R.dimen.mood_view_size);
        params.height = (int) getResources().getDimension(R.dimen.mood_view_size);
        params.setMargins(4, 4, 4, 4);
        return params;
    }

    private int getMoodColor(int moodValue) {
        switch (moodValue) {
            case 1:
                return Color.parseColor("#fd4141");
            case 2:
                return Color.parseColor("#fd7575");
            case 3:
                return Color.parseColor("#ebedf0");
            case 4:
                return Color.parseColor("#9be9a8");
            case 5:
                return Color.parseColor("#40c463");
            default:
                return Color.GRAY;
        }
    }

    private ArrayList<Pair<String, Integer>> generateSortedMoodList(HashMap<String, Integer> moodEntries){
        // Create a list of all dates with default mood value
        ArrayList<Pair<String, Integer>> sortedMoods = new ArrayList<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        try {
            // Assuming the entries are within a known range, e.g., the last 90 days
            calendar.add(Calendar.DAY_OF_YEAR, -calculateRangeDays());
            Date startDate = calendar.getTime();
            Date endDate = new Date(); // today

            while (startDate.before(endDate) || startDate.equals(endDate)) {
                String dateString = dateFormat.format(startDate);
                // Default mood value is 0, or get from hashmap if present
                int moodValue = moodEntries.getOrDefault(dateString, 0);
                sortedMoods.add(new Pair<>(dateString, moodValue));
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                startDate = calendar.getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sortedMoods;
    }
    //This method calculates
    private int calculateRangeDays() {
        Calendar calendar = Calendar.getInstance();

        // Move back approximately 80 days
        calendar.add(Calendar.DAY_OF_YEAR, -80);

        // Adjust to the nearest previous Sunday
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        }

        // Calculate the number of days from this adjusted date to today
        Calendar today = Calendar.getInstance();
        int days = 0;
        while (calendar.before(today) || calendar.equals(today)) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            days++;
        }

        return days;
    }
}