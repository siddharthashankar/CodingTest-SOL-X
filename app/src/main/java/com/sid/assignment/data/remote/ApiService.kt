package com.sid.assignment.data.remote

import com.sid.assignment.model.CatFactsListResponse
import com.sid.assignment.utils.Constants
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(Constants.RANDOM_URL)
    suspend fun getCatFacts(): Response<List<CatFactsListResponse>>

}
