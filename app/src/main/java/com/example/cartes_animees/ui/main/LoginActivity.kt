package com.example.cartes_animees.ui.main

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cartes_animees.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.btnLogin)
        val togglePasswordButton: ImageButton = findViewById(R.id.togglePasswordButton)

        togglePasswordButton.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                togglePasswordButton.setImageResource(R.drawable.ic_visibility_off) // Remplacez par votre icône pour masquer le mot de passe
            } else {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                togglePasswordButton.setImageResource(R.drawable.ic_visibility) // Remplacez par votre icône pour montrer le mot de passe
            }
            // Remet le curseur à la fin du texte
            passwordEditText.setSelection(passwordEditText.text.length)
        }

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            performLogin(username, password)
        }
    }

    private fun performLogin(username: String, password: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            val json = """
                {
                    "username": "$username",
                    "password": "$password"
                }
            """.trimIndent()

            val requestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

            val request = Request.Builder()
                .url("http://10.0.2.2:8080/api/auth/signin")
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Échec de la connexion : ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        if (!response.isSuccessful) {
                            Toast.makeText(this@LoginActivity, "Échec de la connexion : ${response.message}", Toast.LENGTH_LONG).show()
                        } else {
                            // Connexion réussie, lancer com.example.cartes_animees.ui.main.VerificationActivity
                            val intent = Intent(this@LoginActivity, VerificationActivity::class.java)
                            intent.putExtra("USERNAME", username)
                            startActivity(intent)
                            finish() // Terminez cette activité pour l'empêcher de revenir en arrière
                        }
                    }
                }
            })
        } else {
            Toast.makeText(this, "Le nom d'utilisateur et le mot de passe ne peuvent pas être vides", Toast.LENGTH_LONG).show()
        }
    }
}
