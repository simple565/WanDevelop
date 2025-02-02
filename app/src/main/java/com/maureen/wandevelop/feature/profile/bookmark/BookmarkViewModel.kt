package com.maureen.wandevelop.feature.profile.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ItemSnapshotList
import androidx.paging.cachedIn
import com.maureen.wandevelop.entity.Collection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BookmarkViewModel : ViewModel() {
    companion object {
        private const val TAG = "BookmarkViewModel"
    }

    private val repository = BookmarkRepository()

    val collectionFlow = repository.loadCollectionList().flow.flowOn(Dispatchers.IO).cachedIn(viewModelScope)

    fun loadReadLater() = viewModelScope.launch(Dispatchers.IO) {
        /*repository.loadReadLater().map {

        }.also {
            Log.d(TAG, "loadReadLater: ${it.size}")
        }*/
    }

    suspend fun cancelCollect(snapshotList: ItemSnapshotList<Collection>, collection: Collection) = withContext(Dispatchers.IO) {
        val result = repository.cancelBookmark(collection.id, collection.originId)
        Log.d(TAG, "cancelCollect: $result")
        return@withContext if (result.isSuccess) {
            Log.d(TAG, "cancelCollect: ${snapshotList.indexOf(collection)}")
            snapshotList.indexOf(collection)
        } else {
            -1
        }
    }
}