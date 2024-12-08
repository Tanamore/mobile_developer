package com.example.mytanamore

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.mytanamore.Ensiklopedia.EnsiklopediaFragment
import com.example.mytanamore.ScanPenyakit.ScanPenyakitFragment
import com.example.mytanamore.ScanTanaman.ScanTanamanActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Buat intent untuk membuka ScanTanamanActivity
        val intent = Intent(this, ScanTanamanActivity::class.java)
        startActivity(intent)

        // Tutup MainActivity agar tidak berada di tumpukan aktivitas
        finish()
    }
}