package com.nugetzrul3.sugarchainandroidminer

import java.lang.StringBuilder
import java.util.concurrent.BlockingQueue


object Utils {
    fun rotateStringQueue(
        queue: BlockingQueue<String?>,
        next: String?
    ): String {
        while (!queue.offer(next)) {
            try {
                queue.take()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
        val logs = StringBuilder()
        for (element in queue) {
            logs.append(element)
        }
        return logs.toString()
    }
}