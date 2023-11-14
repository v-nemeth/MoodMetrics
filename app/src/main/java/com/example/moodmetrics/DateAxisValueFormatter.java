package com.example.moodmetrics;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateAxisValueFormatter extends ValueFormatter {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        long millis = (long) value; // Ensure x-values are in milliseconds
        return sdf.format(new Date(millis));
    }
}

