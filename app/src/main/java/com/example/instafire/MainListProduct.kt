package com.example.instafire

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instafire.databinding.ActivityMainListProductBinding
import com.example.instafire.models.Post
import com.google.firebase.firestore.FirebaseFirestore
import java.math.BigInteger
import java.security.MessageDigest

 class MainListProduct(val context: Context, val posts: List<Post>) :
    RecyclerView.Adapter<MainListProduct.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ActivityMainListProductBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    private lateinit var firestoreDb: FirebaseFirestore

    inner class ViewHolder(private val binding: ActivityMainListProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: Post) {
            val username = post.user?.username as String
            binding.tvUsername.text = post.user?.username
            binding.tvDescription.text = post.description
            Glide.with(context).load(post.imageUrl).into(binding.ivPost)
            Glide.with(context).load(getProfileImageUrl(username)).into(binding.ivProfileImage)
            binding.tvRelativeTime.text = DateUtils.getRelativeTimeSpanString(post.creationTimeMs)


            binding.btnDeletPosts.setOnClickListener {

                val position = adapterPosition

                if (position != RecyclerView.NO_POSITION) {

                    Log.d("TAG", "Posición del elemento actual en 'posts': $position")

                }

                firestoreDb = FirebaseFirestore.getInstance()
                val articulodocCollection = firestoreDb.collection("posts")

                articulodocCollection.limit(50)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {

                            val articulodoc = querySnapshot.documents[position]

                            val id = articulodoc.id

                            Log.d("TAG", "ID del primer documento en 'posts': $id")

                            val docFef = firestoreDb.collection("posts").document(id).delete()

                            docFef.addOnCompleteListener{ Log.d("TAG", "Posts eliminado")}

                        } else {
                            Log.d("TAG", "La colección 'posts' está vacía.")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error al obtener documentos de la colección 'posts'", e)
                    }

            }

            binding.btnViewImg.setOnClickListener {

                val imageUrl = post.imageUrl
                val intent = Intent(context, ImagenViewBigActivity::class.java)
                intent.putExtra("imageUrl", imageUrl)
                context.startActivity(intent)
            }

        }

        private fun getProfileImageUrl(username: String): String {
            val digest = MessageDigest.getInstance("MD5")
            val hash = digest.digest(username.toByteArray())
            val bigInt = BigInteger(hash)
            val hex = bigInt.abs().toString(16)
            return "https://www.gravatar.com/avatar/$hex?d=identicon"
        }
    }
}