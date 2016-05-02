package com.example.activities;

import android.app.Activity;
import android.os.Bundle;

import com.example.R;

import net.sqlcipher.database.SQLiteDatabase;


public class SugarActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SQLiteDatabase.loadLibs(this);
    }
}
