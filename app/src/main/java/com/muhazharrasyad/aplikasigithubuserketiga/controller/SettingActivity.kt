package com.muhazharrasyad.aplikasigithubuserketiga.controller

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.muhazharrasyad.aplikasigithubuserketiga.R
import com.muhazharrasyad.aplikasigithubuserketiga.databinding.ActivitySettingBinding
import com.muhazharrasyad.aplikasigithubuserketiga.receiver.ReminderReceiver
import android.content.SharedPreferences

class SettingActivity : AppCompatActivity() {
    private var binding: ActivitySettingBinding? = null
    private lateinit var reminderReceiver: ReminderReceiver
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val SHARED_PREFERENCES = "setting preferences"
        private const val DAILY = "daily"
        private const val TIME_REPEATING = "09:00"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        reminderReceiver = ReminderReceiver()
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)

        setSwitch()
        binding?.swReminder?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                reminderReceiver.setRepeatingAlarm(
                    this,
                    ReminderReceiver.TYPE_REPEATING,
                    TIME_REPEATING,
                    getString(R.string.message_reminder)
                )
            } else {
                reminderReceiver.cancelAlarm(this, ReminderReceiver.TYPE_REPEATING)
            }
            saveChange(isChecked)
        }

        // Action Bar Back
        val actionbar = supportActionBar
        actionbar?.title = "Setting Github User"
        actionbar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setSwitch() {
        binding?.swReminder?.isChecked = sharedPreferences.getBoolean(DAILY, false)
    }

    private fun saveChange(value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(DAILY, value)
        editor.apply()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}