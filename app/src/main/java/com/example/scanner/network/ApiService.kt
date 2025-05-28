package com.example.scanner.network

import com.example.scanner.model.ViolationData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("submit_violation.php")
    fun submitViolation(@Body violationData: ViolationData): Call<Void>
}
