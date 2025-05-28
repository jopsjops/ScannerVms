package com.example.scanner

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class ViolationFormActivity : AppCompatActivity() {
    private lateinit var studentIdTextView: TextView
    private lateinit var studentNameTextView: TextView
    private lateinit var studentProgramTextView: TextView
    private lateinit var departmentTextView: TextView
    private lateinit var dateEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var personnelNameEditText: EditText
    private lateinit var selectMajorOffenseButton: Button
    private lateinit var selectMinorOffenseButton: Button
    private lateinit var cancelButton: Button

    private var detectedDepartment: String = "Unknown Department"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_violation_form)

        // Initialize views
        studentIdTextView = findViewById(R.id.student_id)
        studentNameTextView = findViewById(R.id.student_name)
        studentProgramTextView = findViewById(R.id.student_program)
        departmentTextView = findViewById(R.id.department)
        dateEditText = findViewById(R.id.date_edit_text)
        timeEditText = findViewById(R.id.time_edit_text)
        emailEditText = findViewById(R.id.email_edit_text)
        personnelNameEditText = findViewById(R.id.personnel_name_edit_text)
        selectMajorOffenseButton = findViewById(R.id.select_major_offense_button)
        selectMinorOffenseButton = findViewById(R.id.select_minor_offense_button)
        cancelButton = findViewById(R.id.cancel_button)

        // Retrieve student information from the previous activity
        val studentId = intent.getStringExtra("studentId") ?: "Unknown ID"
        val studentName = intent.getStringExtra("studentName") ?: "Unknown Name"
        val studentProgram = intent.getStringExtra("studentProgram") ?: "Unknown Program"

        // Set the student information in the TextViews
        studentIdTextView.text = "Student ID: $studentId"
        studentNameTextView.text = "Student Name: $studentName"
        studentProgramTextView.text = "Student Program: $studentProgram"

        // Automatically set the department based on program
        detectedDepartment = getDepartmentFromProgram(studentProgram)
        departmentTextView.text = "Department: $detectedDepartment"

        // Automatically set the current date
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateEditText.setText(dateFormat.format(calendar.time))

        // Set up the date picker
        dateEditText.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Date")
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                dateEditText.setText(sdf.format(Date(selection)))
            }

            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }

        // Automatically set the current time in 12-hour format with AM/PM
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.US)
        timeFormat.timeZone = TimeZone.getTimeZone("Asia/Manila")
        timeEditText.setText(timeFormat.format(calendar.time))

        // Handle Major Offense selection
        selectMajorOffenseButton.setOnClickListener {
            val selectedDate = dateEditText.text.toString()
            val selectedTime = timeEditText.text.toString()
            val email = emailEditText.text.toString()
            val personnelName = personnelNameEditText.text.toString()

            when {
                detectedDepartment == "Unknown Department" -> {
                    Toast.makeText(this, "Department could not be determined.", Toast.LENGTH_SHORT).show()
                }
                selectedDate.isEmpty() -> {
                    Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                }
                selectedTime.isEmpty() -> {
                    Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show()
                }
                email.isEmpty() -> {
                    Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
                }
                personnelName.isEmpty() -> {
                    Toast.makeText(this, "Please enter personnel name", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val intent = Intent(this, MajorOffenseActivity::class.java).apply {
                        putExtra("studentId", studentId)
                        putExtra("studentName", studentName)
                        putExtra("studentProgram", studentProgram)
                        putExtra("date", selectedDate)
                        putExtra("time", selectedTime)
                        putExtra("department", detectedDepartment)
                        putExtra("email", email)
                        putExtra("personnelName", personnelName)
                    }
                    startActivity(intent)
                }
            }
        }

        // Handle Minor Offense selection
        selectMinorOffenseButton.setOnClickListener {
            val selectedDate = dateEditText.text.toString()
            val selectedTime = timeEditText.text.toString()
            val email = emailEditText.text.toString()
            val personnelName = personnelNameEditText.text.toString()

            when {
                detectedDepartment == "Unknown Department" -> {
                    Toast.makeText(this, "Department could not be determined.", Toast.LENGTH_SHORT).show()
                }
                selectedDate.isEmpty() -> {
                    Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
                }
                selectedTime.isEmpty() -> {
                    Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show()
                }
                email.isEmpty() -> {
                    Toast.makeText(this, "Please enter an email", Toast.LENGTH_SHORT).show()
                }
                personnelName.isEmpty() -> {
                    Toast.makeText(this, "Please enter personnel name", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val intent = Intent(this, MinorOffenseActivity::class.java).apply {
                        putExtra("studentId", studentId)
                        putExtra("studentName", studentName)
                        putExtra("studentProgram", studentProgram)
                        putExtra("date", selectedDate)
                        putExtra("time", selectedTime)
                        putExtra("department", detectedDepartment)
                        putExtra("email", email)
                        putExtra("personnelName", personnelName)
                    }
                    startActivity(intent)
                }
            }
        }

        // Cancel button
        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun getDepartmentFromProgram(program: String): String {
        return when {
            program.contains("BSN", ignoreCase = true) -> "CON"
            program.contains("BSECE", ignoreCase = true) -> "COE"
            program.contains("BSIT", ignoreCase = true) -> "CCS"
            program.contains("BSCS", ignoreCase = true) -> "CCS"
            program.contains("AB Psych", ignoreCase = true) -> "CAS"
            program.contains("BSED MATH", ignoreCase = true) -> "COED"
            program.contains("BSED FIL", ignoreCase = true) -> "COED"
            program.contains("BSED ENG", ignoreCase = true) -> "COED"
            program.contains("BEED", ignoreCase = true) -> "COED"
            program.contains("BSHM", ignoreCase = true) -> "CIHM"
            program.contains("BSA", ignoreCase = true) -> "CBA"
            program.contains("BSBA", ignoreCase = true) -> "CBA"
            program.contains("BSENT", ignoreCase = true) -> "CBA"
            else -> "Unknown Department"
        }
    }
}