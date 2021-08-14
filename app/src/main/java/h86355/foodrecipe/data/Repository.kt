package h86355.foodrecipe.data

import dagger.hilt.android.scopes.ActivityRetainedScoped
import h86355.foodrecipe.data.local.LocalDataSource
import h86355.foodrecipe.data.remote.RemoteDataSource
import javax.inject.Inject

@ActivityRetainedScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
) {

    val remote = remoteDataSource
    val local = localDataSource

}