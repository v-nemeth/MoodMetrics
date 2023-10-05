package com.example.moodmetrics.ui.mood;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MoodViewModel extends ViewModel{

    private final MutableLiveData<String> mText;

    public MoodViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is mood fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}