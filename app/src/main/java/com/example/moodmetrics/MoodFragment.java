package com.example.moodmetrics;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MoodFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView sad, saddest, neutral, happy, happiest;

    private Button submit;

    private String username;


    public MoodFragment(String u) {
        this.username = u;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood, container, false);

        final String[] mood = new String[1];
        saddest = (TextView) view.findViewById(R.id.saddestSmiley);
        sad     = (TextView) view.findViewById(R.id.sadSmiley);
        neutral = (TextView) view.findViewById(R.id.neutralSmiley);
        happy   = (TextView) view.findViewById(R.id.happySmiley);
        happiest= (TextView) view.findViewById(R.id.happiestSmiley);
        submit  = (Button)   view.findViewById(R.id.submit);


        saddest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood[0] = "1";
                submit.setEnabled(true);
            }
        });
        sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood[0] = "2";
                submit.setEnabled(true);
            }
        });
        neutral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood[0] = "3";
                submit.setEnabled(true);
            }
        });
        happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood[0] = "4";
                submit.setEnabled(true);
            }
        });
        happiest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mood[0] = "5";
                submit.setEnabled(true);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBHelper dbHelper = new DBHelper(getContext());
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                dbHelper.addMoodEntryToDB(username, mood[0], date.toString());
                submit.setEnabled(false);
            }
        });

        return view;
    }
}