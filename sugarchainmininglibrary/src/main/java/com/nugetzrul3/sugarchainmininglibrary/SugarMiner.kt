package com.nugetzrul3.sugarchainmininglibrary

import android.os.Handler
import android.os.Bundle
import android.os.Message

class SugarMiner {
    companion object {
        private const val TAG: String = "SugarMiner"
        private var stringhandler: Handler? = null
        fun output(message: String) {
            val msg = Message()
            val bundle = Bundle()
            bundle.putString("log", message)
            msg.data = bundle
            if (stringhandler != null) {
                stringhandler!!.sendMessage(msg)
            }
        }

        init {
            System.loadLibrary("HelloWorld")
        }
    }

    enum class Algorithm {
        YESPOWER,
        YESPOWERSUGAR,
        YESPOWERLITB,
        YESPOWERIOTS,
        YESOPOWERMBC,
        YESPOWERITC,
        YESPOWERISO,
    }

    constructor() {}
    constructor(handler: Handler?) {
        stringhandler = handler
    }

    fun beginMiner(pool: String, username: String, pwd: String, threads: Int, algorithm: Algorithm?): Int {
        return when (algorithm) {
            Algorithm.YESPOWER -> startMining(pool, username, pwd, threads, 0)
            Algorithm.YESPOWERSUGAR -> startMining(pool, username, pwd, threads, 1)
            Algorithm.YESPOWERLITB -> startMining(pool, username, pwd, threads, 2)
            Algorithm.YESPOWERIOTS -> startMining(pool, username, pwd, threads, 3)
            Algorithm.YESOPOWERMBC -> startMining(pool, username, pwd, threads, 4)
            Algorithm.YESPOWERITC -> startMining(pool, username, pwd, threads, 5)
            Algorithm.YESPOWERISO -> startMining(pool, username, pwd, threads, 6)
            else -> -1
        }
    }


    private external fun startMining(pool: String, username: String, pwd: String, threads: Int, algorithm: Int): Int
    external fun stopMining(): Int

}
