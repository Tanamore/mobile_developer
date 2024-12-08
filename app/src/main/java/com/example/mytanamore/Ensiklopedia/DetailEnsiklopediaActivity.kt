package com.example.mytanamore.Ensiklopedia

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mytanamore.databinding.ActivityDetailEnsiklopediaBinding
import com.example.mytanamore.response.DataItem

class DetailEnsiklopediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEnsiklopediaBinding
    private lateinit var viewModel: EnsiklopediaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEnsiklopediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[EnsiklopediaViewModel::class.java]

        val dataItem: DataItem? = intent.getParcelableExtra("DATA_ITEM")
        dataItem?.let { data ->
            // Memuat gambar tanaman
            Glide.with(this)
                .load(data.imageUrl)
                .centerCrop()
                .into(binding.ivPlantImage)

            // Menampilkan nama tanaman dan nama ilmiah
            binding.tvPlantName.text = data.plantName ?: "Unknown Plant"
            binding.tvScientificName.text = "Scientific Name: ${data.scientificName ?: "-"}"

            val plantId = data.id ?: ""

            if (plantId.isNotEmpty()) {
                // Meminta detail ensiklopedia menggunakan plantId
                viewModel.fetchEnsiklopediaDetail(plantId)
                viewModel.ensiklopediaDetailState.observe(this) { state ->
                    when (state) {
                        is EnsiklopediaViewModel.EnsiklopediaDetailState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        is EnsiklopediaViewModel.EnsiklopediaDetailState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            val detail = state.ensiklopediaDetail
                            // Set nilai detail tanaman
                            binding.tvUses.text = "Uses: ${detail.uses ?: "-"}"
                            binding.tvOriginPlace.text = "Origin Place: ${detail.originPlace ?: "-"}"
                            binding.tvClimate.text = "Climate: ${detail.climate ?: "-"}"
                            binding.tvCommonDisease.text = "Common Disease: ${detail.commonDisease ?: "-"}"
                            binding.tvFertilizer.text = "Fertilizer: ${detail.fertilizer ?: "-"}"
                            binding.tvWateringFreq.text = "Watering Frequency: ${detail.wateringFrequency ?: "-"}"
                            binding.tvHarvestTime.text = "Harvest Time: ${detail.harvestTime ?: "-"}"
                            binding.tvDescription.text = detail.description ?: "No Description"
                        }

                        is EnsiklopediaViewModel.EnsiklopediaDetailState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                        }

                        null -> {
                            binding.progressBar.visibility = View.GONE
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Invalid plant ID", Toast.LENGTH_SHORT).show()
            }
        }

        binding.icBack.setOnClickListener {
            onBackPressed()
        }
    }
}