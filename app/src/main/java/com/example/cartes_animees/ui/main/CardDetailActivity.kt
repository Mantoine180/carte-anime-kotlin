package com.example.cartes_animees.ui.main

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cartes_animees.R

class CardDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        val cardName = intent.getStringExtra("card_name")
        val cardNameTextView: TextView = findViewById(R.id.tvCardName)
        cardNameTextView.text = cardName

        val backButton: Button = findViewById(R.id.btnReturn)
        backButton.setOnClickListener { finish() }
    }
}
