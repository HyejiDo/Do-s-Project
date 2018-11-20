package com.example.hjdo.doslist.setting;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.example.hjdo.doslist.R;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_settings);


        Intent intent = getIntent();

    }
}
