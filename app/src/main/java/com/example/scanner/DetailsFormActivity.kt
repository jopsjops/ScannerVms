package com.example.scanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.scanner.model.ViolationData
import com.example.scanner.network.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFormActivity : AppCompatActivity() {

    private lateinit var studentIdTextView: TextView
    private lateinit var studentNameTextView: TextView
    private lateinit var studentProgramTextView: TextView
    private lateinit var departmentTextView: TextView
    private lateinit var majorOffenseTextView: TextView
    private lateinit var minorOffenseTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var submitButton: Button
    private lateinit var cancelButton: Button
    private lateinit var emailTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var personnelTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_form)

        // Views
        studentIdTextView = findViewById(R.id.student_id)
        studentNameTextView = findViewById(R.id.student_name)
        studentProgramTextView = findViewById(R.id.student_program)
        departmentTextView = findViewById(R.id.department)
        majorOffenseTextView = findViewById(R.id.major_spinner)
        minorOffenseTextView = findViewById(R.id.minor_spinner)
        dateTextView = findViewById(R.id.date)
        submitButton = findViewById(R.id.submit_button)
        cancelButton = findViewById(R.id.cancel_button)
        emailTextView = findViewById(R.id.email)
        timeTextView = findViewById(R.id.time)
        personnelTextView = findViewById(R.id.personnel_name)

        // Data passed from previous activity
        val studentId = intent.getStringExtra("studentId") ?: ""
        val studentName = intent.getStringExtra("studentName") ?: ""
        val studentProgram = intent.getStringExtra("studentProgram") ?: ""
        val department = intent.getStringExtra("department") ?: ""
        val date = intent.getStringExtra("date") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val time = intent.getStringExtra("time") ?: ""
        val personnelName = intent.getStringExtra("personnelName") ?: ""

        // Populate the TextViews with the data received
        studentIdTextView.text = "Student ID: $studentId"
        studentNameTextView.text = "Name: $studentName"
        studentProgramTextView.text = "Program: $studentProgram"
        departmentTextView.text = "Department: $department"
        dateTextView.text = "Date: $date"
        emailTextView.text = "Email: $email"
        timeTextView.text = "Time: $time"
        personnelTextView.text = "Personnel: $personnelName"

        // Display only major or minor offense
        val majorOffense = intent.getStringExtra("major") ?: ""
        val minorOffense = intent.getStringExtra("minor") ?: ""

        if (majorOffense.isNotBlank()) {
            majorOffenseTextView.text = "Major Offense: $majorOffense"
            minorOffenseTextView.visibility = TextView.GONE
        } else {
            minorOffenseTextView.text = "Minor Offense: $minorOffense"
            majorOffenseTextView.visibility = TextView.GONE
        }

        submitButton.setOnClickListener {
            // Validate that all required data is present
            if (studentId.isNotBlank() && studentName.isNotBlank() && studentProgram.isNotBlank() &&
                department.isNotBlank() && date.isNotBlank()) {

                // Determine violation based on passed data
                val violation = if (majorOffense.isNotBlank()) majorOffense else minorOffense
                val offenseType = if (majorOffense.isNotBlank()) "Major" else "Minor"

                val violationData = ViolationData(
                    studentId = studentId,
                    studentName = studentName,
                    department = department,
                    program = studentProgram,
                    violation = violation,
                    offense = offenseType,
                    date = date,
                    email = email,
                    time = time,
                    personnelName = personnelName
                )

                Log.d("DetailsFormActivity", "Sending data: $violationData")

                // Make the network request to submit data
                RetrofitInstance.api.submitViolation(violationData)
                    .enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.d("API Response", "Success: ${response.isSuccessful}, Code: ${response.code()}")

                            if (response.isSuccessful) {
                                Toast.makeText(
                                    this@DetailsFormActivity,
                                    "Data submitted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // Redirect to SubmittedFormActivity
                                val intent = Intent(this@DetailsFormActivity, SubmittedFormActivity::class.java).apply {
                                    putExtra("studentId", studentId)
                                    putExtra("studentName", studentName)
                                    putExtra("studentProgram", studentProgram)
                                    putExtra("department", department)
                                    putExtra("violation", violation)
                                    putExtra("offense", offenseType)
                                    putExtra("date", date)
                                    putExtra("time", time)
                                    putExtra("personnelName", personnelName)
                                }
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@DetailsFormActivity,
                                    "Failed to submit data. Code: ${response.code()}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.e(
                                    "DetailsFormActivity",
                                    "Failed to submit data. Response code: ${response.code()}"
                                )
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Toast.makeText(
                                this@DetailsFormActivity,
                                "Network error: ${t.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("DetailsFormActivity", "Network error: ${t.message}", t)
                        }
                    })
            } else {
                Toast.makeText(this, "Please ensure all fields are filled", Toast.LENGTH_SHORT).show()
            }
        }

        // Cancel button functionality
        cancelButton.setOnClickListener {
            finish() // Close the activity if cancel is clicked
        }
    }
}
