package com.example.moodmetrics;

import android.annotation.SuppressLint;
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

    private final String username;
    private String mood;
    private Button submit;

    public MoodFragment(String u) {
        this.username = u;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mood, container, false);

        submit   = (Button)   view.findViewById(R.id.submit);
        TextView saddest  = (TextView) view.findViewById(R.id.saddestSmiley);
        TextView sad      = (TextView) view.findViewById(R.id.sadSmiley);
        TextView neutral  = (TextView) view.findViewById(R.id.neutralSmiley);
        TextView happy    = (TextView) view.findViewById(R.id.happySmiley);
        TextView happiest = (TextView) view.findViewById(R.id.happiestSmiley);
        TextView dateView = (TextView) view.findViewById(R.id.date);

        dateView.setText(new SimpleDateFormat("dd. MMMM yyyy - hh:mm").format(new Date()));

        setMoodButtonListener(saddest, "1");
        setMoodButtonListener(sad, "2");
        setMoodButtonListener(neutral, "3");
        setMoodButtonListener(happy, "4");
        setMoodButtonListener(happiest, "5");

        submit.setOnClickListener(v -> {
            DBHelper dbHelper = DBHelper.getInstance(getContext());
            Date date = new Date();
            dbHelper.addMoodEntryToDB(username, mood, date);
            submit.setEnabled(false);
        });

        return view;
    }

    private void setMoodButtonListener(TextView b, String moodValue) {
        b.setOnClickListener(v -> {
            mood = moodValue;
            submit.setEnabled(true);
        });
    }


}