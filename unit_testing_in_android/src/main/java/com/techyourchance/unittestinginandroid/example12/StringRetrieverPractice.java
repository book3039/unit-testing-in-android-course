package com.techyourchance.unittestinginandroid.example12;

import android.content.Context;

public class StringRetrieverPractice {
    private final Context context;

    public StringRetrieverPractice(Context context) {
        this.context = context;
    }

    public String getString(int id) {
        return context.getString(id);
    }
}
