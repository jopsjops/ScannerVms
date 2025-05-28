package com.example.scanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // On click for Scan button
        findViewById<View>(R.id.scan_button).setOnClickListener {
            // Implement your QR scan functionality here
            val intent = Intent(this, ScanFormActivity::class.java)
            startActivity(intent)
        }
    }
}