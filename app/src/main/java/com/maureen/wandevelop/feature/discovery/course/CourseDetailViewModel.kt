package com.maureen.wandevelop.feature.discovery.course

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maureen.wandevelop.core.entity.DataLoadState
import com.maureen.wandevelop.feature.discovery.DiscoveryRepository
import com.maureen.wandevelop.network.entity.SystemNodeInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author lianml
 * @date 2025/10/4
 */
class CourseDetailViewModel : ViewModel() {
    companion object {
        private const val TAG = "CourseDetailViewModel"
    }

    private val discoveryRepository: DiscoveryRepository = DiscoveryRepository()
    private val _courseDetailState: MutableStateFlow<SystemNodeInfo?> = MutableStateFlow(null)
    private val _chapterExpandIdSetState: MutableStateFlow<Set<Int>> = MutableStateFlow(emptySet())

    val courseDetailState = combine(
        _courseDetailState,
        _chapterExpandIdSetState
    ) { course, expandIdSet ->
        return@combine DataLoadState(
            isLoading = false,
            dataList = if (course == null) emptyList() else listOf(course),
            operatedIdSet = expandIdSet
        )
    }.flowOn(Dispatchers.Default)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = DataLoadState(isLoading = true)
        )


    fun loadCourseDetail(parentId: Int, courseId: Int) = viewModelScope.launch(Dispatchers.IO) {
        // 请求体系数据，根据courseId过滤出当前课程数据
        val courseInfo = discoveryRepository.getSystemNodeList().takeIf { it.isSuccessWithData }?.run {
            this.data?.flatMap { listOf(it) + it.children }?.find { it.id == courseId }
        } ?: return@launch
        Log.d(TAG, "loadCourseCatalogue: ${courseInfo.name}")
        if (parentId == 0) {
            // 一级节点，需要根据二级节点id请求数据
            _courseDetailState.value = courseInfo
            val subMap = courseInfo.children.associateBy { it.id }.toMutableMap()
            courseInfo.children.forEach {
                subMap[it.id] = it.copy(articleList = discoveryRepository.getSystemNodeArticleList(it.id).data?.dataList?.reversed() ?: emptyList())
                Log.d(TAG, "loadCourseDetail: ${subMap[it.id]?.articleList}")
                _courseDetailState.value = courseInfo.copy(children = subMap.values.toList())
            }
        } else {
            // 非一级节点，根据courseId请求课程目录
            _courseDetailState.value = courseInfo.copy(
                articleList = discoveryRepository.getSystemNodeArticleList(courseId).data?.dataList?.reversed() ?: emptyList()
            )
        }
    }

    fun toggleExpand(chapterId: Int) {
        val current = _chapterExpandIdSetState.value
        if (current.contains(chapterId)) {
            _chapterExpandIdSetState.update { it - chapterId }
        } else {
            _chapterExpandIdSetState.update { it + chapterId }
        }
    }
}