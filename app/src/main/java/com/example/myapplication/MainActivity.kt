package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.json.JSONObject
import java.io.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeButtonText()
        saveConfig()
        setText()






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


        val save_button: Button = findViewById(R.id.button3)
        save_button.setOnClickListener {
            val changeTextView2: TextView = findViewById(R.id.textView6)

            var Settings = JSONObject()
            Settings.put("URL", Pooltxt.text)
            Settings.put("User", Usertxt.text)
            Settings.put("Passwd", Passwdtxt.text)
            Settings.put("CPU", thrdstxt.text)

            changeTextView2.setText("Pool:" + Settings.get("URL").toString() + ", Username:" + Settings.get("User").toString() + ", Password: " + Settings.get("Passwd").toString() + ", Threads: " + Settings.get("CPU").toString())



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
}








