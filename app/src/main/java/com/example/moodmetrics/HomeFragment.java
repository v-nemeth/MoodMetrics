package com.example.moodmetrics;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DBHelper DB;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        DB = new DBHelper(getContext());

        populateMoodGrid(DB.fetchMoodEntries("username"), view);


        // Inflate the layout for this fragment
        return view;
    }

    private void populateMoodGrid(HashMap<String, Integer> moodEntries, View view){
        GridLayout gridLayout = view.findViewById(R.id.monthlyMoodGrid);

        int i = 0;
        for (Map.Entry<String, Integer> entry : moodEntries.entrySet()) {
            if (i%7 == 0){
                //TODO IMPLEMENT empty column
            }

            Integer moodValue = entry.getValue();

            View moodView = new View(getContext());
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = (int) getResources().getDimension(R.dimen.mood_view_size);
            params.height = (int) getResources().getDimension(R.dimen.mood_view_size);
            params.setMargins(4, 4, 4, 4);
            moodView.setLayoutParams(params);

            // Set color based on moodValue, you can customize this part
            switch (moodValue) {
                case 1:
                    moodView.setBackgroundColor(Color.parseColor("#ebedf0"));
                    break;
                case 2:
                    moodView.setBackgroundColor(Color.parseColor("#fd7575"));
                    break;
                case 3:
                    moodView.setBackgroundColor(Color.parseColor("#40c463"));
                    break;
                case 4:
                    moodView.setBackgroundColor(Color.parseColor("#9be9a8"));
                    break;
                case 5:
                    moodView.setBackgroundColor(Color.parseColor("#ffffff")); // Example for mood 5
                    break;
                default:
                    moodView.setBackgroundColor(Color.GRAY);
                    break;
            }

            gridLayout.addView(moodView);
            i++;
        }
    }
}