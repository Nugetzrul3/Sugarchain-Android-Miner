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

        checkTextLength()

    }

    fun checkTextLength() {
        var walletlength = (getIntent().getStringExtra("walletaddress"))

        if (walletlength.length == 0) {
            val errorSay: TextView = findViewById(R.id.yourminer)
            errorSay.setText("No wallet specified")
        } else if (walletlength.length < 45) {
            val errorSay: TextView = findViewById(R.id.yourminer)
            errorSay.setText("Not a valid wallet! Please check your address")
        } else if (walletlength.length == 45) {
            getmininginfo()
        }
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
                        val walletaddress: TextView = findViewById(R.id.yourminer)
                        val balance: TextView = findViewById(R.id.yourbalance)
                        val paid: TextView = findViewById(R.id.yourpaid)
                        val hashrate: TextView = findViewById(R.id.yourhashrate)
                        val shares: TextView = findViewById(R.id.yourtotalshares)
                        walletaddress.setText("Your Address: " + info1.toString())
                        balance.setText("Your Pool balance: " + info2.toString())
                        paid.setText("Paid out: " + info3.toString())
                        hashrate.setText("Your hashrate: " + info4.toString())
                        shares.setText("Your shares: " + info5.toString())
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
