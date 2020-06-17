package com.nugetzrul3.sugarchainandroidminer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import khttp.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.math.roundToInt

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

        checkWalletLength()
    }

    fun checkWalletLength() {
        var walletlength = (getIntent().getStringExtra("walletaddress"))

        if (walletlength.length == 0) {
            val errorSay: TextView = findViewById(R.id.yourminer)
            errorSay.setText("No wallet specified")
        } else if (walletlength.length < 45 || walletlength.length > 45) {
            val errorSay: TextView = findViewById(R.id.yourminer)
            errorSay.setText("Not a valid wallet! Please check your address")
        } else if (walletlength.length == 45) {
            nomporyiimp()
        }
    }

    fun nomporyiimp() {
        val radioButtonNomp: RadioButton = findViewById(R.id.selectNomp)
        val radioButtonYiimp: RadioButton = findViewById(R.id.selectYIIMP)
        if (radioButtonNomp.isChecked) {
            getmininginfoNOMP()
        }
        else if (radioButtonYiimp.isChecked) {
            getmininginfoYIIMP()
        }
        else {
            val errorSay: TextView = findViewById(R.id.yourminer)
            errorSay.setText("Please specify which type of pool you are using")
        }
    }

    fun getmininginfoNOMP() {
        Timer().scheduleAtFixedRate(object:TimerTask() {
            override fun run() {
                Thread (Runnable {
                    var info1 = get("http://www.lepool.com.cn:8080/api/worker_stats?"+getIntent().getStringExtra("walletaddress")).jsonObject.getString("miner")
                    var info2 = get("http://www.lepool.com.cn:8080/api/worker_stats?"+getIntent().getStringExtra("walletaddress")).jsonObject.getString("immature")
                    var info3 = get("http://www.lepool.com.cn:8080/api/worker_stats?"+getIntent().getStringExtra("walletaddress")).jsonObject.getString("paid")
                    var info4 = get("http://www.lepool.com.cn:8080/api/worker_stats?"+getIntent().getStringExtra("walletaddress")).jsonObject.getString("totalHash")
                    var info5 = get("http://www.lepool.com.cn:8080/api/worker_stats?"+getIntent().getStringExtra("walletaddress")).jsonObject.getString("totalShares")
                    this@MiningStats.runOnUiThread(java.lang.Runnable {
                        val walletaddress: TextView = findViewById(R.id.yourminer)
                        val balance: TextView = findViewById(R.id.yourimmature)
                        val paid: TextView = findViewById(R.id.yourpaid)
                        val hashrate: TextView = findViewById(R.id.yourhashrate)
                        val shares: TextView = findViewById(R.id.yourtotalshares)
                        walletaddress.setText("Your Address: " + info1.toString())
                        balance.setText("Your Immature Balance: " + info2.toString() + "SUGAR")
                        paid.setText("Paid out: " + info3.toString() + "SUGAR")
                        hashrate.setText("Your hashrate: " + (info4.toFloat().roundToInt()/1000).toString() + " KH/s")
                        shares.setText("Your shares: " + info5.toString())
                    }
                    )
                }).start()
            }
        }, 0, 5000)

    }

    fun getmininginfoYIIMP() {
        Timer().scheduleAtFixedRate(object:TimerTask() {
            override fun run() {
                Thread (Runnable {
                    var info2 = get("http://miningmadness.com/api/wallet?address="+getIntent().getStringExtra("walletaddress")).jsonObject.getString("unpaid")
                    var info3 = get("http://miningmadness.com/api/wallet?address="+getIntent().getStringExtra("walletaddress")).jsonObject.getString("paid24h")
                    var info4 = get("http://miningmadness.com/api/wallet?address="+getIntent().getStringExtra("walletaddress")).jsonObject.getString("totalHash")
                    var info5 = get("http://miningmadness.com/api/wallet?address="+getIntent().getStringExtra("walletaddress")).jsonObject.getString("totalShares")
                    this@MiningStats.runOnUiThread(java.lang.Runnable {
                        val walletaddress: TextView = findViewById(R.id.yourminer)
                        val balance: TextView = findViewById(R.id.yourimmature)
                        val paid: TextView = findViewById(R.id.yourpaid)
                        val hashrate: TextView = findViewById(R.id.yourhashrate)
                        val shares: TextView = findViewById(R.id.yourtotalshares)
                        walletaddress.setText("Your Address: " + getIntent().getStringExtra("walletaddress"))
                        balance.setText("Your Immature Balance: " + info2.toString() + "SUGAR")
                        paid.setText("Paid out: " + info3.toString() + "SUGAR")
                        hashrate.setText("Your hashrate: " + (info4.toFloat().roundToInt()/1000).toString() + " KH/s")
                        shares.setText("Your shares: " + info5.toString())
                    }
                    )
                }).start()
            }
        }, 0, 5000)

    }

}
