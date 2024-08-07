package com.example.cartes_animees.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartes_animees.R
import com.example.cartes_animees.model.Animation
import com.example.cartes_animees.model.Series
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class CardsActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        val seriesInfoTextView: TextView = findViewById(R.id.tvSeriesInfo)
        val animationsListView: ListView = findViewById(R.id.lvAnimations)
        val seriesId = intent.getIntExtra("series_id", -1)

        if (seriesId != -1) {
            val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
            val token = sharedPreferences.getString("auth_token", null)

            if (token != null) {
                fetchSeriesDetails(seriesId, token, seriesInfoTextView, animationsListView)
            } else {
                Toast.makeText(this, "Token non disponible", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "ID de série non valide", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchSeriesDetails(seriesId: Int, token: String, seriesInfoTextView: TextView, animationsListView: ListView) {
        val request = Request.Builder()
            .url("http://10.0.2.2:8080/series/$seriesId")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@CardsActivity, "Erreur lors de la récupération des informations de la série : ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val series = parseSeriesJson(responseBody)

                    runOnUiThread {
                        series?.let {
                            seriesInfoTextView.text = """
                                Série : ${it.libelle_serie}
                                Thème : ${it.theme}
                                Description : ${it.description}
                            """.trimIndent()

                            val adapter = AnimationAdapter(this@CardsActivity, it.animations)
                            animationsListView.adapter = adapter

                            animationsListView.setOnItemClickListener { parent, view, position, id ->
                                val animation = it.animations[position]
                                val intent = Intent(this@CardsActivity, CardDetailActivity::class.java)
                                intent.putExtra("animation_id", animation.id)
                                intent.putExtra("animation_libelle", animation.libelle_animation)
                                startActivity(intent)
                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@CardsActivity, "Erreur lors de la récupération des informations de la série : ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun parseSeriesJson(json: String?): Series? {
        json?.let {
            val seriesJson = JSONObject(it)
            val id = seriesJson.getInt("id")
            val libelleSerie = seriesJson.getString("libelle_serie")
            val theme = seriesJson.getString("theme")
            val description = seriesJson.getString("description")
            val animationsJsonArray = seriesJson.getJSONArray("animations")
            val animations = mutableListOf<Animation>()

            for (i in 0 until animationsJsonArray.length()) {
                val animationJson = animationsJsonArray.getJSONObject(i)
                val animationId = animationJson.getInt("id")
                val libelleAnimation = animationJson.getString("libelle_animation")
                animations.add(Animation(animationId, libelleAnimation))
            }

            return Series(id, libelleSerie, theme, description, animations)
        }
        return null
    }
}
