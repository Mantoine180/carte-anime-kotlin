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
            logout()
        }

        val seriesButton: Button = findViewById(R.id.btnSeries)
        seriesButton.setOnClickListener {
            startActivity(Intent(this, SeriesActivity::class.java))
        }
    }

    private fun logout() {
        // Efface les préférences partagées
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        // Redirige vers l'écran de connexion
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
