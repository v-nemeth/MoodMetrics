package com.example.moodmetrics;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ArticlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticlesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArticlesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArticlesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticlesFragment newInstance(String param1, String param2) {
        ArticlesFragment fragment = new ArticlesFragment();
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
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_articles, container, false);

        // Find the ImageView by ID
        ImageView articleImage1 = rootView.findViewById(R.id.articleImage1);
        ImageView articleImage2 = rootView.findViewById(R.id.articleImage2);
        TextView articleTitle1 = rootView.findViewById(R.id.Title_1);
        TextView articleTitle2 = rootView.findViewById(R.id.Title_2);
        TextView articleContent1 = rootView.findViewById(R.id.Content_1);
        TextView articleContent2 = rootView.findViewById(R.id.Content_2);

        // Create a common OnClickListener for both article images and text
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click based on the clicked view
                if (v == articleImage1 || v == articleTitle1 || v == articleContent1) {
                    // Handle the click for the first article
                    Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                    startActivity(intent);
                } else if (v == articleImage2 || v == articleTitle2 || v == articleContent2) {
                    // Handle the click for the second article
                    // Can go to the new content pages if you want to.
                    Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                    startActivity(intent);
                }
            }
        };

        // Set OnClickListener for the first article
        articleImage1.setOnClickListener(clickListener);
        articleTitle1.setOnClickListener(clickListener);
        articleContent1.setOnClickListener(clickListener);

        // Set OnClickListener for the second article
        articleImage2.setOnClickListener(clickListener);
        articleTitle2.setOnClickListener(clickListener);
        articleContent2.setOnClickListener(clickListener);

        return rootView;
    }
}