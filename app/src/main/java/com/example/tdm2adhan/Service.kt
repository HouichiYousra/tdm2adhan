package com.example.tdm2adhan
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.azan.Azan
import com.azan.Method
import com.azan.astrologicalCalc.Location
import com.azan.astrologicalCalc.SimpleDate
import java.text.SimpleDateFormat
import java.util.*


class Service : Service() {
    val CHANNEL_ID="11"

    var mMediaPlayer: MediaPlayer? = null
    var mAudioManager: AudioManager? = null
    val today = SimpleDate(GregorianCalendar())
    val location = Location(30.045411, 31.236735, 2.0, 0)
    val azan = Azan(location, Method.EGYPT_SURVEY)
    val prayerTimes = azan.getPrayerTimes(today)
    val awquatSalat=arrayOf(prayerTimes.fajr(),prayerTimes.thuhr(),prayerTimes.assr(),prayerTimes.maghrib(),prayerTimes.ishaa(),"06:54:00")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        startTimer()
        return START_STICKY
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel"
            val descriptionText = "channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        val broadcastIntent = Intent(this, Receiver::class.java)
        sendBroadcast(broadcastIntent)
    }

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    fun startTimer() {
        timer = Timer()
        initializeTimerTask()
        timer!!.scheduleAtFixedRate(timerTask, 1000, 1000) //
    }

    fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun run() {
                val sdf = SimpleDateFormat("hh:mm:ss")
                val currentDate = sdf.format(Date())
                Log.i("time now",currentDate)

                mAudioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                mMediaPlayer = MediaPlayer.create(applicationContext, R.raw.adhan)
                if (awquatSalat.contains(currentDate)){
                    Log.i("SALAT"," GO TO SALAT")

                    var builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("Adhan")
                        .setContentText( "GO TO SALAT"
                        )
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    createNotificationChannel()

                    with(NotificationManagerCompat.from(applicationContext)) {
                        notify(11, builder.build())
                    }
                    mMediaPlayer!!.start()
                    mMediaPlayer!!.setOnCompletionListener({ mMediaPlayer?.release() })
                }
            }
        }
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}

