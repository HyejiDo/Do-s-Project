package com.example.hjdo.doslist.setting

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceActivity

import com.example.hjdo.doslist.R

class SettingsActivity : PreferenceActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.pref_settings)


        val intent = intent

    }
}
