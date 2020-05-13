package com.example.tdm2adhan

import android.content.BroadcastReceiver import android.content.Context
import android.content.Intent
import android.util.Log


class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        Log.i(
            Receiver::class.java.getSimpleName(),
            "Service Stopped"
        )
        context.startService(Intent(context, Service::class.java))
    }
}