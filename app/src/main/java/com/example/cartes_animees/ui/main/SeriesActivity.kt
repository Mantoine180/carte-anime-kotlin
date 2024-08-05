package com.example.cartes_animees.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.cartes_animees.R

class SeriesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)

        val seriesListView: ListView = findViewById(R.id.lvSeries)
        val seriesArray = resources.getStringArray(R.array.series_titles)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, seriesArray)
        seriesListView.adapter = adapter

        seriesListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, CardsActivity::class.java)
            intent.putExtra("series_name", seriesArray[position])
            startActivity(intent)
        }
    }
}

