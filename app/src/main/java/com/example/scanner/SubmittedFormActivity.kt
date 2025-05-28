package com.example.scanner

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SubmittedFormActivity : AppCompatActivity() {

    private lateinit var studentIdTextView: TextView
    private lateinit var studentNameTextView: TextView
    private lateinit var studentProgramTextView: TextView
    private lateinit var departmentTextView: TextView
    private lateinit var majorOffenseTextView: TextView
    private lateinit var minorOffenseTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var doneButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sumbitted_form) // Ensure this layout file exists

        // Initialize the views
        studentIdTextView = findViewById(R.id.student_id)
        studentNameTextView = findViewById(R.id.student_name)
        studentProgramTextView = findViewById(R.id.student_program)
        departmentTextView = findViewById(R.id.department)
        majorOffenseTextView = findViewById(R.id.major_spinner)
        minorOffenseTextView = findViewById(R.id.minor_spinner)
        dateTextView = findViewById(R.id.date)
        statusTextView = findViewById(R.id.status_spinner)
        doneButton = findViewById(R.id.done_button)

        // Get the data passed from DetailsFormActivity
        val studentId = intent.getStringExtra("studentId")
        val studentName = intent.getStringExtra("studentName")
        val studentProgram = intent.getStringExtra("studentProgram")
        val department = intent.getStringExtra("department")
        val majorOffense = intent.getStringExtra("major")
        val minorOffense = intent.getStringExtra("minor")
        val date = intent.getStringExtra("date")
        val status = intent.getStringExtra("status") // Get the offense status

        // Populate the TextViews with the data
        studentIdTextView.text = "Student ID: $studentId"
        studentNameTextView.text = "Name: $studentName"
        studentProgramTextView.text = "Program: $studentProgram"
        departmentTextView.text = "Department: $department"
        dateTextView.text = "Date: $date"
        statusTextView.text = "Status: $status" // Display the offense status

        // Determine which offense to display
        if (!majorOffense.isNullOrBlank()) {
            majorOffenseTextView.text = "Major Offense: $majorOffense"
            majorOffenseTextView.visibility = View.VISIBLE
            minorOffenseTextView.visibility = View.GONE // Hide minor if major exists
        } else if (!minorOffense.isNullOrBlank()) {
            minorOffenseTextView.text = "Minor Offense: $minorOffense"
            minorOffenseTextView.visibility = View.VISIBLE
        } else {
            majorOffenseTextView.visibility = View.GONE // Hide major if neither exists
            minorOffenseTextView.visibility = View.GONE // Hide minor if neither exists
        }

        // Set up the done button to return to the main activity
        doneButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish() // Close the current activity
        }
    }
}
