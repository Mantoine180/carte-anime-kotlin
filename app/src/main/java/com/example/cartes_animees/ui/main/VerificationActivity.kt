package com.example.cartes_animees.ui.main// com.example.cartes_animees.ui.main.VerificationActivity.kt
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.cartes_animees.R
import okhttp3.*
import okio.IOException

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
            verifyCode(username, code)
        }
    }

    private fun verifyCode(username: String, code: String) {
        val requestBody = FormBody.Builder()
            .add("username", username)
            .add("token", code)
            .build()

        val request = Request.Builder()
            .url("http://yourapi.com/api/auth/verify")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    // Gérer l'erreur, par exemple, afficher un Toast
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        // Gérer la réponse positive, par exemple, lancer l'activité principale
                    } else {
                        // Gérer l'erreur de réponse, par exemple, afficher un message d'erreur
                    }
                }
            }
        })
    }
}
