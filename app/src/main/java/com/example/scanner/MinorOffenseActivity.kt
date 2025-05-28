package com.example.scanner

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MinorOffenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_minor_form) // Make sure this layout exists

        findViewById<TextView>(R.id.title).text = "Offense: Minor"

        // Retrieve passed data
        val studentId = intent.getStringExtra("studentId") ?: "No ID provided"
        val studentName = intent.getStringExtra("studentName") ?: "No Name provided"
        val studentProgram = intent.getStringExtra("studentProgram") ?: "No Program provided"
        val date = intent.getStringExtra("date") ?: "No Date provided"
        val time = intent.getStringExtra("time") ?: "No Time provided"
        val department = intent.getStringExtra("department") ?: "No Department provided"
        val email = intent.getStringExtra("email") ?: "No Email provided"
        val personnelName = intent.getStringExtra("personnelName") ?: "No Personnel provided"

        // Display values
        findViewById<TextView>(R.id.student_id).text = "Student ID: $studentId"
        findViewById<TextView>(R.id.student_name).text = "Name: $studentName"
        findViewById<TextView>(R.id.student_program).text = "Program: $studentProgram"
        findViewById<TextView>(R.id.date).text = "Date: $date"
        findViewById<TextView>(R.id.time).text = "Time: $time"
        findViewById<TextView>(R.id.department).text = "Department: $department"
        findViewById<TextView>(R.id.email).text = "Email: $email"
        findViewById<TextView>(R.id.personnel_name).text = "Personnel: $personnelName"

        // Offense options
        val offenseSpinner: Spinner = findViewById(R.id.minorOff_spinner)
        val offenses = arrayOf(
            "Violating dress protocol", "Incomplete uniform", "Littering",
            "Loitering in hallways", "Class disturbance",
            "Shouting", "Eating in class", "Public affection",
            "Kissing", "Suggestive poses", "Inappropriate touching",
            "No ID card", "Using others' ID", "Caps indoors",
            "Noise in quiet areas", "Discourtesy", "Malicious calls",
            "Refusing ID check", "Blocking passageways", "Unauthorized charging",
            "Academic non-compliance"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, offenses)
        offenseSpinner.adapter = adapter

        // Buttons
        val submitButton: Button = findViewById(R.id.submit_button)
        val cancelButton: Button = findViewById(R.id.cancel_button)

        submitButton.setOnClickListener {
            val selectedOffense = offenseSpinner.selectedItem?.toString() ?: ""
            if (selectedOffense.isBlank()) {
                Toast.makeText(this, "Please select a minor offense", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, DetailsFormActivity::class.java).apply {
                    putExtra("studentId", studentId)
                    putExtra("studentName", studentName)
                    putExtra("studentProgram", studentProgram)
                    putExtra("department", department)
                    putExtra("minor", selectedOffense)
                    putExtra("date", date)
                    putExtra("time", time)
                    putExtra("email", email)
                    putExtra("personnelName", personnelName)
                }
                startActivity(intent)
            }
        }

        cancelButton.setOnClickListener {
            finish()
        }
    }
}
