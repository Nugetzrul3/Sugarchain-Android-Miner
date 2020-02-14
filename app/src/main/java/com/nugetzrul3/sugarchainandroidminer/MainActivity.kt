package com.nugetzrul3.sugarchainandroidminer

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import org.json.JSONObject
import java.io.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sugartoolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(sugartoolbar)

        changeButtonText()
        setText()
        clearLog()






    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.getItemId()) {
            R.id.mygithub -> {
                var parse1 = Uri.parse("https://github.com/Nugetzrul3")
                startActivity(Intent(Intent.ACTION_VIEW, parse1))
                return true
                }
            R.id.website -> {
                var parse2 = Uri.parse("https://sugarchain.org")
                startActivity(Intent(Intent.ACTION_VIEW, parse2))
                return true
            }
            R.id.Sugargithub -> {
                var parse3 = Uri.parse("https://github.com/sugarchain-project")
                startActivity(Intent(Intent.ACTION_VIEW, parse3))
                return true
            }
            R.id.Donate -> {
                var parse4 = Uri.parse("https://sugarchain-blockbook.ilmango.work/address/sugar1qtl7u435t4jly2hdaa7hrcv5qkpvwa0spd9zzc7")
                startActivity(Intent(Intent.ACTION_VIEW, parse4))
            }
            R.id.settings -> {

            }
            }


        return false
    }

    private var doublebackpressedonce = false

    override fun onBackPressed() {
        if (doublebackpressedonce) {
            super.onBackPressed()
            return
        }
        this.doublebackpressedonce = true
        Toast.makeText(this, "Click back again to Exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed(Runnable { doublebackpressedonce = false }, 1500)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(!hasFocus)
        saveConfig()
    }

    private fun changeButtonText() {
        val start_button: Button = findViewById(R.id.button)
        start_button.setText("Start")
        start_button.setOnClickListener {
            val changeTextView: TextView = findViewById(R.id.textView6)

            fun stoporstart() {
            if (start_button.text == "Start") {
                start_button.setText("Stop")
                repeat(10) {
                    changeTextView.setText("The Process has started")
                }
            }
            else if (start_button.text == "Stop") {
                start_button.setText("Start")
                changeTextView.setText("The Process has stopped")
            }
            }
            stoporstart()

        }
    }



    fun saveConfig() {

        var Pooltxt = findViewById(R.id.editText) as EditText
        var Usertxt = findViewById(R.id.editText2) as EditText
        var Passwdtxt = findViewById(R.id.editText3) as EditText
        var thrdstxt = findViewById(R.id.editText5) as EditText



        var Settings = JSONObject()
        Settings.put("URL", Pooltxt.text)
        Settings.put("User", Usertxt.text)
        Settings.put("Passwd", Passwdtxt.text)
        Settings.put("CPU", thrdstxt.text)




        var context = applicationContext.filesDir.path
        var file = File(context + "config.json")
        if(!file.exists()){
            file.createNewFile()
            file.writeText(Settings.toString())
        }
        else if(file.exists()){
            file.writeText(Settings.toString())
        }

    }

    fun setText() {
        var json: String

        val serverset: EditText = findViewById(R.id.editText)
        val userrset: EditText = findViewById(R.id.editText2)
        val passwdset: EditText = findViewById(R.id.editText3)
        val thrdsset: EditText = findViewById(R.id.editText5)

        try {

            var context = applicationContext.filesDir.path
            val path = context + "config.json"

            val inputStream: InputStream =
                FileInputStream("$path")
            json = inputStream.bufferedReader().use { it.readText() }


            var jsobobj = JSONObject(json)

            for (i in 0..jsobobj.length() - 1) {
                var server = jsobobj.get("URL")
                var username = jsobobj.get("User")
                var password = jsobobj.get("Passwd")
                var threads = jsobobj.get("CPU")

                serverset.setText(server.toString())
                userrset.setText(username.toString())
                passwdset.setText(password.toString())
                thrdsset.setText(threads.toString())


            }
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }

    fun clearLog() {
        val logclear: TextView = findViewById(R.id.textView6)
        val clear_button: Button = findViewById(R.id.button3)

        clear_button.setOnClickListener{
            logclear.setText("")
        }
    }


    /*fun DarkModeActivate(view: View) {
        val switch: Switch = findViewById(R.id.switch1)

    }
    To be added later
     */


}








