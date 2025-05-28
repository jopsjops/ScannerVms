package com.example.scanner

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*

class ScanFormActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scanner_form)

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)

        // Scanner configuration
        codeScanner.camera = CodeScanner.CAMERA_BACK
        codeScanner.formats = CodeScanner.ALL_FORMATS
        codeScanner.autoFocusMode = AutoFocusMode.SAFE
        codeScanner.scanMode = ScanMode.SINGLE
        codeScanner.isAutoFocusEnabled = true
        codeScanner.isFlashEnabled = false

        // Decode callback
        codeScanner.decodeCallback = DecodeCallback { result ->
            runOnUiThread {
                Log.d("ScanResult", "Scanned text: ${result.text}")

                val scanResult = result.text
                val regex = Regex("""(.+)\s+\[(.+?)\]\s+(.+)""")
                val matchResult = regex.find(scanResult)

                if (matchResult != null) {
                    val rawName = matchResult.groupValues[1].trim()
                    val studentId = matchResult.groupValues[2].trim()
                    val studentProgram = matchResult.groupValues[3].trim()

                    val studentName = rawName.lowercase().split(" ").joinToString(" ") { word ->
                        val cleanWord = word.replace(",", "")
                        val capitalized = cleanWord.replaceFirstChar { it.uppercaseChar() }
                        if (word.endsWith(",")) "$capitalized," else capitalized
                    }

                    // Launch form activity with parsed data
                    val intent = Intent(this, ViolationFormActivity::class.java)
                    intent.putExtra("studentName", studentName)
                    intent.putExtra("studentId", studentId)
                    intent.putExtra("studentProgram", studentProgram)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Invalid QR code format", Toast.LENGTH_LONG).show()
                }
            }
        }

        // Camera error handling
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        checkPermission(android.Manifest.permission.CAMERA, 200)
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    private fun checkPermission(permission: String, reqCode: Int) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), reqCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 200) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
