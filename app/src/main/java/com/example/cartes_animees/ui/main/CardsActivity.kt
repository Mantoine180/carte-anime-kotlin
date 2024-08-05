package com.example.cartes_animees.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cartes_animees.R

class CardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        //val cardsRecyclerView: RecyclerView = findViewById(R.id.cardsRecyclerView)
        // Set up the RecyclerView with CardsAdapter (not shown here for brevity)

        // Assume CardsAdapter handles displaying cards and triggering CardDetailActivity
    }
}
