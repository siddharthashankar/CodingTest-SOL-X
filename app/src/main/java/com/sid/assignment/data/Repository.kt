package com.sid.assignment.data

import com.sid.assignment.data.remote.RemoteDataSource
import com.sid.assignment.model.BaseApiResponse
import com.sid.assignment.model.CatFactsListResponse
import com.sid.assignment.utils.NetworkResult
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


@ViewModelScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseApiResponse() {

    suspend fun getCatFacts(): Flow<NetworkResult<List<CatFactsListResponse>>> {
        return flow<NetworkResult<List<CatFactsListResponse>>> {
            emit(safeApiCall { remoteDataSource.getCatFacts() })
        }.flowOn(Dispatchers.IO)
    }

}
