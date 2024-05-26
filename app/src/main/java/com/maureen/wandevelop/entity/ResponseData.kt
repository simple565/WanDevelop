package com.maureen.wandevelop.entity

import androidx.annotation.Keep
import com.squareup.moshi.Json

/**
 * 请求返回基础信息实体类
 */
@Keep
data class BaseResponse<T>(
    val data: T?,
    val errorMsg: String,
    val errorCode: Int
) {
    val isSuccess: Boolean
        get() {
            return errorCode == 0
        }
}

@Keep
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