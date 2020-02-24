package com.nugetzrul3.sugarchainandroidminer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Switch
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatDelegate

class SettingsPage : AppCompatActivity() {
    protected lateinit var sharedpref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedpref = SharedPref(this)
        if (sharedpref.loadNightModestate() == true) {
            setTheme(R.style.DarkTheme)
        }
        else setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_page)

        val settingstoolbar: Toolbar = findViewById(R.id.settingstoolbar)
        setSupportActionBar(settingstoolbar)

        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
    }


}
