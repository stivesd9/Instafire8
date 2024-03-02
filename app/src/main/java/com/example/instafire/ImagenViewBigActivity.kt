package com.example.instafire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.instafire.databinding.ActivityImagenViewBigBinding
import com.example.instafire.databinding.ActivityLoginBinding

class ImagenViewBigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImagenViewBigBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagenViewBigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUrl = intent.getStringExtra("imageUrl")

        val username = intent.getStringExtra("username")
        val description = intent.getStringExtra("description")
        val raiting = intent.getStringExtra("raiting")

        val ivBigImage: ImageView = findViewById(R.id.ivBigImage)
        Glide.with(this).load(imageUrl).into(ivBigImage)

        val etUsername: TextView = findViewById(R.id.usernameList)
        etUsername.setText(username)

        val etDescription: TextView = findViewById(R.id.userDescript)
        etDescription.setText(description)

        val etRaiting: TextView = findViewById(R.id.userRaiting)
        etRaiting.setText(raiting)




    }
}