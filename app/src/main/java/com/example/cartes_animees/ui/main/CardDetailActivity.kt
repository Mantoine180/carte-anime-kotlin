package com.example.cartes_animees.ui.main

import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartes_animees.R
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CardDetailActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private var mediaPlayer: MediaPlayer? = null
    private val TAG = "CardDetailActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)

        val animationId = intent.getIntExtra("animation_id", -1)
        val animationLibelle = intent.getStringExtra("animation_libelle") ?: ""
        val animationLibelleTextView: TextView = findViewById(R.id.tvAnimationLibelle)
        val imageButton: Button = findViewById(R.id.btnImage)
        val soundButton: Button = findViewById(R.id.btnSound)
        val imageView: ImageView = findViewById(R.id.ivAnimationImage)

        animationLibelleTextView.text = animationLibelle

        if (animationId != -1) {
            fetchAnimationDetails(animationId)
        }

        imageButton.setOnClickListener {
            val imageUrl = "http://10.0.2.2:8080/animations/$animationId/image"
            fetchImage(imageUrl, imageView)
        }

        soundButton.setOnClickListener {
            val soundUrl = "http://10.0.2.2:8080/animations/$animationId/sound"
            fetchAndPlayAudio(soundUrl)
        }
    }

    private fun fetchAnimationDetails(animationId: Int) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/animations/$animationId")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to fetch animation details", e)
                runOnUiThread {
                    Toast.makeText(this@CardDetailActivity, "Erreur lors de la récupération des détails de l'animation : ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d(TAG, "Animation details fetched successfully: $responseBody")
                    // Parse response and update UI (if necessary)
                } else {
                    Log.e(TAG, "Failed to fetch animation details, response code: ${response.code}")
                }
            }
        })
    }

    private fun fetchImage(url: String, imageView: ImageView) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to fetch image", e)
                runOnUiThread {
                    Toast.makeText(this@CardDetailActivity, "Erreur lors de la récupération de l'image : ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val bytes = response.body?.bytes()
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes?.size ?: 0)
                    Log.d(TAG, "Image fetched successfully")
                    runOnUiThread {
                        imageView.setImageBitmap(bitmap)
                    }
                } else {
                    Log.e(TAG, "Failed to fetch image, response code: ${response.code}")
                    runOnUiThread {
                        Toast.makeText(this@CardDetailActivity, "Erreur lors de la récupération de l'image", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun fetchAndPlayAudio(url: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $token")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Failed to fetch audio", e)
                runOnUiThread {
                    Toast.makeText(this@CardDetailActivity, "Erreur lors de la récupération du son : ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val bytes = response.body?.bytes()
                    val file = File(cacheDir, "temp_audio.mp3")
                    val fos = FileOutputStream(file)
                    fos.write(bytes)
                    fos.close()
                    Log.d(TAG, "Audio fetched successfully")

                    runOnUiThread {
                        playAudio(file.path)
                    }
                } else {
                    Log.e(TAG, "Failed to fetch audio, response code: ${response.code}")
                    runOnUiThread {
                        Toast.makeText(this@CardDetailActivity, "Erreur lors de la récupération du son", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun playAudio(filePath: String) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(filePath)
            setOnPreparedListener { start() }
            setOnCompletionListener { release() }
            setOnErrorListener { _, _, _ ->
                Toast.makeText(this@CardDetailActivity, "Erreur lors de la lecture du son", Toast.LENGTH_LONG).show()
                release()
                true
            }
            prepareAsync()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
