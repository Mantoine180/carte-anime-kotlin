package com.example.cartes_animees.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartes_animees.R
import com.example.cartes_animees.model.Animation
import com.example.cartes_animees.model.Series
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class SeriesActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series)

        val seriesListView: ListView = findViewById(R.id.lvSeries)

        // Récupérer le token de SharedPreferences
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token != null) {
            fetchSeries(token, seriesListView)
        } else {
            Toast.makeText(this, "Token non disponible", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchSeries(token: String, seriesListView: ListView) {
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/series")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@SeriesActivity, "Erreur lors de la récupération des séries : ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val seriesList = parseSeriesJson(responseBody)

                    runOnUiThread {
                        val adapter = SeriesAdapter(this@SeriesActivity, seriesList)
                        seriesListView.adapter = adapter

                        seriesListView.setOnItemClickListener { parent, view, position, id ->
                            val intent = Intent(this@SeriesActivity, CardsActivity::class.java)
                            intent.putExtra("series_id", seriesList[position].id)
                            startActivity(intent)
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@SeriesActivity, "Erreur lors de la récupération des séries : ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun parseSeriesJson(json: String?): List<Series> {
        val seriesList = mutableListOf<Series>()
        json?.let {
            val jsonArray = JSONArray(it)
            for (i in 0 until jsonArray.length()) {
                val seriesJson = jsonArray.getJSONObject(i)
                val id = seriesJson.getInt("id")
                val libelleSerie = seriesJson.getString("libelle_serie")
                val theme = seriesJson.getString("theme")
                val description = seriesJson.getString("description")
                val animationsJsonArray = seriesJson.getJSONArray("animations")
                val animations = mutableListOf<Animation>()

                for (j in 0 until animationsJsonArray.length()) {
                    val animationJson = animationsJsonArray.getJSONObject(j)
                    val animationId = animationJson.getInt("id")
                    val libelleAnimation = animationJson.getString("libelle_animation")
                    animations.add(Animation(animationId, libelleAnimation))
                }

                val series = Series(id, libelleSerie, theme, description, animations)
                seriesList.add(series)
            }
        }
        return seriesList
    }
}
