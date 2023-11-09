package com.example.moodmetrics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView
        .OnNavigationItemSelectedListener{
    BottomNavigationView bottomNavigationView;
    Button bLogout;
    SharedPreferences prf;

    private String username;

    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prf = getSharedPreferences("user_details",MODE_PRIVATE);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        name = findViewById(R.id.name);
        username = prf.getString("username", null);
        name.setText(username);
        bLogout = findViewById(R.id.logoutButton);

        homeFragment = new HomeFragment(username);
        metricsFragment = new MetricsFragment();
        moodFragment = new MoodFragment(username);
        articlesFragment = new ArticlesFragment();

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home_nav);

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    HomeFragment homeFragment;
    MetricsFragment metricsFragment;
    MoodFragment moodFragment;
    ArticlesFragment articlesFragment;



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home_nav) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, homeFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.metrics_nav) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, metricsFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.mood_nav) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, moodFragment)
                    .commit();
            return true;
        } else if (item.getItemId() == R.id.articles_nav) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.flFragment, articlesFragment)
                    .commit();
            return true;
        } else {
            return false;
        }
    }

    public String getUsername() {
        return username;
    }
}
