package com.example.tdm2adhan


import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.azan.Azan
import com.azan.Method
import com.azan.astrologicalCalc.Location
import com.azan.astrologicalCalc.SimpleDate
import java.util.*


class MainActivity : AppCompatActivity() {

    var mServiceIntent: Intent? = null
    private var mSensorService: Service? = Service()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mServiceIntent = Intent(applicationContext, mSensorService!!.javaClass)
        if (!isMyServiceRunning(mSensorService!!.javaClass)) {
            startService(mServiceIntent)
        }

    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager =
            getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                Log.i("isMyServiceRunning?", true.toString() + "")
                return true
            }
        }
        Log.i("isMyServiceRunning?", false.toString() + "")
        return false
    }


    override fun onDestroy() {
        stopService(mServiceIntent)
        Log.i("MAINACT", "onDestroy!")
        super.onDestroy()
    }

}
