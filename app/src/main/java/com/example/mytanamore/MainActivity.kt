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
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val fragmentManager = supportFragmentManager
        val ScanPenyakitFragment = ScanPenyakitFragment()
        val fragment = fragmentManager.findFragmentByTag(ScanPenyakitFragment::class.java.simpleName)
        if (fragment !is ScanPenyakitFragment) {
            fragmentManager
                .beginTransaction()
                .add(R.id.frame_container, ScanPenyakitFragment, ScanPenyakitFragment::class.java.simpleName)
                .commit()
        }
    }
}