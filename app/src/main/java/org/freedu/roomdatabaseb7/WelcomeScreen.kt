package org.freedu.roomdatabaseb7

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import org.freedu.roomdatabaseb7.databinding.ActivityWelcomeScreenBinding

class WelcomeScreen : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeScreenBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWelcomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Now you can access views directly by their ID (camelCase)
        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            Toast.makeText(this@WelcomeScreen, "Navigating to Login...", Toast.LENGTH_SHORT).show()
        }

        binding.btnSupport.setOnClickListener {

            binding.tvSubtitle.text = "Support team notified."
        }
    }
}