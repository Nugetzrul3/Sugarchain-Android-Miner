package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.method.LinkMovementMethod
import android.widget.*
import androidx.appcompat.app.ActionBar
import org.json.JSONObject
import java.io.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainline: TextView = findViewById(R.id.textView7)

        mainline.setMovementMethod(LinkMovementMethod.getInstance())

        changeButtonText()
        setText()
        clearLog()






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
                changeTextView.setText("The Process has started")
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


}








