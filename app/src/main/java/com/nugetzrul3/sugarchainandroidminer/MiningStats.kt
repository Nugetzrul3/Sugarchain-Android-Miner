package com.nugetzrul3.sugarchainandroidminer

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import khttp.*
import kotlinx.android.synthetic.main.activity_mining_stats.*
import org.w3c.dom.Text
import java.io.IOException
import java.util.*

class MiningStats : AppCompatActivity() {

    protected lateinit var sharedpref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedpref = SharedPref(this)
        if (sharedpref.loadNightModestate() == true) {
            setTheme(R.style.DarkTheme)
        }
        else setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mining_stats)

        val toolbar: Toolbar = findViewById(R.id.statstoolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        getmininginfo()

    }

    fun getmininginfo() {
        Timer().scheduleAtFixedRate(object:TimerTask() {
            override fun run() {
                Thread (Runnable {
                    var info1 = khttp.get("https://1pool.sugarchain.org/api/worker_stats?"+getIntent().getStringExtra("walletaddress")).jsonObject.getString("miner")
                    var info2 = khttp.get("https://1pool.sugarchain.org/api/worker_stats?"+getIntent().getStringExtra("walletaddress")).jsonObject.getString("balance")
                    var info3 = khttp.get("https://1pool.sugarchain.org/api/worker_stats?"+getIntent().getStringExtra("walletaddress")).jsonObject.getString("paid")
                    var info4 = khttp.get("https://1pool.sugarchain.org/api/worker_stats?"+getIntent().getStringExtra("walletaddress")).jsonObject.getString("totalHash")
                    var info5 = khttp.get("https://1pool.sugarchain.org/api/worker_stats?"+getIntent().getStringExtra("walletaddress")).jsonObject.getString("totalShares")
                    this@MiningStats.runOnUiThread(java.lang.Runnable {
                        val walletaddress: TextView = findViewById(R.id.testprintwalletaddress)
                        walletaddress.setText(info1.toString() + "\n" + info2.toString() + "\n" + info3.toString() + "\n" + info4.toString() + "\n" + info5.toString())
                    })
                }).start()
            }
        }, 0, 5000)


    }

    /*fun setIntervalformininginfo() {
        android.os.Handler().postDelayed(
            Runnable {
                getmininginfo()
            },
        1000)
    }*/
}
