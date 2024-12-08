package com.example.mytanamore.ScanTanaman

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mytanamore.R
import com.example.mytanamore.databinding.ActivityDetailTanamanBinding
import com.example.mytanamore.response.DiseaseInfo
import com.example.mytanamore.response.PlantInfo

class DetailTanamanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailTanamanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTanamanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val result = intent.getStringExtra("ANALYSIS_RESULT")
        val confidence = intent.getStringExtra("CONFIDENCE")
        val plantInfo: PlantInfo? = intent.getParcelableExtra("PLANT_INFO")

        // Menampilkan hasil analisis
        binding.tvResult.text = result ?: "No result"
        binding.tvConfidence.text = "Confidence: $confidence"

        // Memeriksa dan menampilkan informasi tanaman jika tersedia
        plantInfo?.let {
            Glide.with(this)
                .load(it.imageUrl)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.analyzedImage)
            binding.tvPlantName.text = it.plantName ?: "Unknown Plant"
            binding.tvScientificName.text = "Scientific Name: ${it.scientificName ?: "N/A"}"
            binding.tvOriginPlace.text = "Origin: ${it.originPlace ?: "Unknown"}"
            binding.tvClimate.text = "Climate: ${it.climate ?: "Unknown"}"
            binding.tvCommonDisease.text = "Common Disease: ${it.commonDisease ?: "None"}"
            binding.tvDescription.text = it.description ?: "No description available"
        } ?: run {
            binding.tvPlantName.text = "No plant information"
        }

        // Tombol kembali
        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }
}
