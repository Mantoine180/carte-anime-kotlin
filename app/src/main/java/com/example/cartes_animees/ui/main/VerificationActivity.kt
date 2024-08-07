package com.example.cartes_animees.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartes_animees.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class VerificationActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        val verificationCodeEditText: EditText = findViewById(R.id.verificationCodeEditText)
        val verifyButton: Button = findViewById(R.id.verifyButton)
        val username = intent.getStringExtra("USERNAME") ?: ""

        verifyButton.setOnClickListener {
            val code = verificationCodeEditText.text.toString()
            if (code.isNotEmpty() && code.matches(Regex("^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}\$"))) {
                verifyCode(username, code)
            } else {
                Toast.makeText(this, "Veuillez entrer un code de vérification valide.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun verifyCode(username: String, code: String) {
        val json = """
            {
                "username": "$username",
                "token": "$code"
            }
        """.trimIndent()

        val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("http://10.0.2.2:8080/api/auth/verify")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@VerificationActivity, "Échec de la vérification : ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val jsonResponse = JSONObject(responseBody)
                        val token = jsonResponse.getString("token")

                        // Stocker le token dans SharedPreferences
                        val sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("auth_token", token)
                        editor.apply()

                        // Naviguer vers SeriesActivity
                        val intent = Intent(this@VerificationActivity, SeriesActivity::class.java)
                        startActivity(intent)
                        finish() // Terminez cette activité pour empêcher de revenir en arrière
                    } else {
                        Toast.makeText(this@VerificationActivity, "Échec de la vérification : ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}
