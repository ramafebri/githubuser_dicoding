package com.example.consumerapp.view.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.consumerapp.R
import com.example.consumerapp.databinding.ActivityAlarmBinding
import com.example.consumerapp.notification.AlarmReceiver

class AlarmActivity : AppCompatActivity(){
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var binding: ActivityAlarmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        alarmReceiver = AlarmReceiver()

        binding.switchAlarm.isChecked = alarmReceiver.isAlarmSet(this)
        binding.switchAlarm.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val repeatTime = "09:00"
                val repeatMessage = getString(R.string.alarm_message)
                val messageToast = getString(R.string.alarm_setup)
                alarmReceiver.setRepeatingAlarm(this, repeatTime, repeatMessage, messageToast)
            } else {
                val messageToast = getString(R.string.alarm_cancel)
                alarmReceiver.cancelAlarm(this, messageToast)
            }
        }
    }
}