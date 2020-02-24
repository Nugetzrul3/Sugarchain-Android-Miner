package com.nugetzrul3.sugarchainandroidminer

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    internal var mysharedPref: SharedPreferences

    init {
        mysharedPref = context.getSharedPreferences("filename", Context.MODE_PRIVATE)
    }

    fun setNightModeState(state: Boolean?) {
        val editor = mysharedPref.edit()
        editor.putBoolean("NightMode", state!!)
        editor.apply()
    }

    fun loadNightModestate(): Boolean? {
        return mysharedPref.getBoolean("NightMode", false)
    }
}