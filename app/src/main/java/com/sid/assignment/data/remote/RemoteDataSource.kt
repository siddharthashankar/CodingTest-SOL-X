package com.sid.assignment.data.remote

import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getCatFacts() = apiService.getCatFacts()

}