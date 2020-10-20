package com.example.githubuser.view.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.example.githubuser.notification.AlarmReceiver
import com.example.githubuser.R

class AlarmActivity : AppCompatActivity(){
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        alarmReceiver = AlarmReceiver()

        val toggle: SwitchCompat = findViewById(R.id.switch_alarm)
        toggle.isChecked = alarmReceiver.isAlarmSet(this)
        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val repeatTime = "09:00"
                val repeatMessage = resources.getString(R.string.alarm_message)
                val messageToast = resources.getString(R.string.alarm_setup)
                alarmReceiver.setRepeatingAlarm(this, repeatTime, repeatMessage, messageToast)
            } else {
                val messageToast = resources.getString(R.string.alarm_cancel)
                alarmReceiver.cancelAlarm(this, messageToast)
            }
        }
    }
}