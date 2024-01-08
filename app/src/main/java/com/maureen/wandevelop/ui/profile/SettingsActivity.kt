package com.maureen.wandevelop.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.maureen.wandevelop.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    private fun toggleDarkMode(switch: SwitchCompat) {
        // switch.isChecked = !switch.isChecked
        AppCompatDelegate.setDefaultNightMode(if (switch.isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }
}