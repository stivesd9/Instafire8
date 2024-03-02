package com.example.instafire

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instafire.databinding.ActivityListProductCategoryBinding
import com.example.instafire.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

private const val TAG = "ListProductCategory"
class ListProductCategory : AppCompatActivity() {

    private var signedInUser: User? = null

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var binding: ActivityListProductCategoryBinding
    private lateinit var productAdapter: ProductAdapter

    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListProductCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestoreDb = FirebaseFirestore.getInstance()

        productAdapter = ProductAdapter()

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListProductCategory)
            adapter = productAdapter
        }

        searchButton = findViewById(R.id.searchButton)
        searchEditText = findViewById(R.id.searchEditText)

        searchButton.setOnClickListener {
            val searchQuery = searchEditText.text.toString().trim()
            if (searchQuery.isNotEmpty()) {
                searchProducts(searchQuery)
            }
        }

        fetchProducts()


    }

    private fun fetchProducts(){
        val collectionRef = firestoreDb.collection("smartphones")

        collectionRef.orderBy("category", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { userSnapshot  ->
                val datax = mutableListOf<Product>()
                for (documentSnapshot in userSnapshot.documents) {

                    val data = documentSnapshot.data

                    if (data != null && data.containsKey("title")) {
                        val value = data["title"].toString()
                        val valueDescription = data["description"].toString()
                        val valuePrice = data["price"].toString()
                        val valDescountPorcCat = data["discountPercentage"].toString()
                        val valRatingCat = data["rating"].toString()
                        val valStockCat = data["stock"].toString()
                        val valBrandCat = data["brand"].toString()
                        val imageUrlCat = data["imagen1"].toString()
                        val category = data["category"].toString()

                        if (valStockCat.isNotEmpty()) {
                            val product = Product(
                                value,
                                valueDescription,
                                valuePrice,
                                valDescountPorcCat,
                                valRatingCat,
                                valStockCat,
                                valBrandCat,
                                imageUrlCat,
                                category
                            )
                            datax.add(product)
                        }
                    }
                }
                showProducts(datax)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error al consultar la colección: ${exception.message}")
            }


    }

    private fun searchProducts(searchQuery: String) {
        val collectionRef = firestoreDb.collection("smartphones")

        collectionRef.orderBy("category", Query.Direction.ASCENDING).limit(30)
            .whereEqualTo("title", searchQuery)
            .get()
            .addOnSuccessListener { userSnapshot ->
                val dataz = mutableListOf<Product>()
                for (documentSnapshot in userSnapshot.documents) {
                    val data = documentSnapshot.data

                    if (data != null && data.containsKey("title")) {
                        val value = data["title"].toString()
                        val valueDescription = data["description"].toString()
                        val valuePrice = data["price"].toString()
                        val valDescountPorcCat = data["discountPercentage"].toString()
                        val valRatingCat = data["rating"].toString()
                        val valStockCat = data["stock"].toString()
                        val valBrandCat = data["brand"].toString()
                        val imageUrlCat = data["imagen1"].toString()
                        val category = data["category"].toString()

                        if (valStockCat.isNotEmpty()) {

                            val product = Product(
                            value,
                            valueDescription,
                            valuePrice,
                            valDescountPorcCat,
                            valRatingCat,
                            valStockCat,
                            valBrandCat,
                            imageUrlCat,
                            category)

                            dataz.add(product)
                        }
                    }
                }
                showProducts(dataz)
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error al consultar la colección: ${exception.message}")
            }
    }

    private fun showProducts(products: List<Product>) {
        productAdapter.setData(products)
    }

    data class Product(
        val title: String,
        val description: String,
        val price: String,
        val discountPercentage: String,
        val rating: String,
        val stock: String,
        val brand: String,
        val imagen1: String,
        val category: String
    )

    inner class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
        private var productList: List<Product> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_product, parent, false)
            return ProductViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            val product = productList[position]
            holder.bind(product)
        }

        override fun getItemCount(): Int = productList.size

        fun setData(products: List<Product>) {
            productList = products
            notifyDataSetChanged()
        }

        inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val productNameTextView: TextView = itemView.findViewById(R.id.tvUserCateg)
            private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionCateg)
            private val priceTextView: TextView = itemView.findViewById(R.id.priceCateg)
            private val discountPercentageTextView: TextView = itemView.findViewById(R.id.descountPorcCateg)
            private val ratingTextView: TextView = itemView.findViewById(R.id.ratingCateg)
            private val stockTextView: TextView = itemView.findViewById(R.id.stockCateg)
            private val brandTextView: TextView = itemView.findViewById(R.id.brancCateg)
            private val imageView: ImageView = itemView.findViewById(R.id.ivImageCat)
            private val CategListPro: TextView = itemView.findViewById(R.id.CategListPro)

            fun bind(product: Product) {
                productNameTextView.text = product.title
                descriptionTextView.text = product.description
                priceTextView.text = product.price
                discountPercentageTextView.text = product.discountPercentage
                ratingTextView.text = product.rating
                stockTextView.text = product.stock
                brandTextView.text = product.brand

                Glide.with(itemView.context)
                    .load(product.imagen1)
                    .into(imageView)

                CategListPro.text = product.category

        }   }
    }


}