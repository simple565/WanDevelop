package com.maureen.wandevelop.entity

import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

/**
 * 请求返回基础信息实体类
 */
@Serializable
data class BaseResponse<T>(
    val data: T?,
    val errorMsg: String,
    val errorCode: Int
) {
    val isSuccess: Boolean
        get() = errorCode == 0

    val isSuccessWithData: Boolean
        get() = isSuccess && null != data
}

@Serializable
data class BasePage<T>(
    val curPage: Int = 0,
    @Json(name = "datas")
    val dataList: List<T>,
    val offset: Int = 0,
    val over: Boolean = false,
    val pageCount: Int = 0,
    val size: Int = 0,
    val total: Int = 0
)