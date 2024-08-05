package com.example.cartes_animees.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cartes_animees.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val connectButton: Button = findViewById(R.id.btnConnect)
        connectButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        val logoutButton: Button = findViewById(R.id.btnLogout)
        logoutButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val seriesButton: Button = findViewById(R.id.btnSeries)
        seriesButton.setOnClickListener {
            startActivity(Intent(this, SeriesActivity::class.java))
        }
    }
}

