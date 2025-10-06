package com.maureen.wandevelop.feature.profile

import com.maureen.wandevelop.core.network.NetworkRequest
import com.maureen.wandevelop.network.WanAndroidService
import com.maureen.wandevelop.network.entity.ProfileInfo
import com.maureen.wandevelop.util.UserPrefUtil
import com.maureen.wandevelop.util.UserPrefUtil.KEY_USER_COLLECT_ID
import com.maureen.wandevelop.util.UserPrefUtil.KEY_USER_INFO
import com.maureen.wandevelop.util.UserPrefUtil.KEY_USER_UNREAD_MSG_COUNT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json

/**
 * Function: 账户相关
 * @author lianml
 * Create 2021-05-02
 */
class ProfileRepository {

    suspend fun getUserDetailInfo() = NetworkRequest.requestSafely { WanAndroidService.instance.userDetailInfo() }

    suspend fun getUnreadMessageCount() = NetworkRequest.requestSafely { WanAndroidService.instance.unreadMessageCount() }

    suspend fun signUp(username: String, password: String, passwordAgain: String) = NetworkRequest.requestSafely { WanAndroidService.instance.register(username, password, passwordAgain) }

    suspend fun signIn(username: String, password: String) = NetworkRequest.requestSafely { WanAndroidService.instance.login(username, password) }

    suspend fun saveProfileInfo(profileInfo: ProfileInfo) {
        UserPrefUtil.setPreference(KEY_USER_INFO, Json.encodeToString(profileInfo))
        UserPrefUtil.setPreference(KEY_USER_UNREAD_MSG_COUNT, profileInfo.unreadMsgCount.toString())
    }

    suspend fun saveUserCollectIds(collectIds: List<Long>) {
        UserPrefUtil.setPreference(KEY_USER_COLLECT_ID, collectIds.joinToString(","))
    }

    suspend fun updateUnreadMsgCount(count: Int = 0) {
        UserPrefUtil.setPreference(KEY_USER_UNREAD_MSG_COUNT, count.toString())
    }

    suspend fun hasProfileCache(): Boolean {
        return UserPrefUtil.getPreference(KEY_USER_INFO) != null
    }

    fun getProfileInfoFromCache(): Flow<ProfileInfo> {
        return combine(UserPrefUtil.getPreferenceFlow(KEY_USER_INFO, ""), UserPrefUtil.getPreferenceFlow(KEY_USER_UNREAD_MSG_COUNT)) { userJson, userUnread ->
            val unreadMsgCount = userUnread?.toInt() ?: 0
            return@combine if (userJson.isNullOrBlank()) {
                ProfileInfo(unreadMsgCount = unreadMsgCount)
            } else {
                Json.decodeFromString<ProfileInfo>(userJson).copy(unreadMsgCount = unreadMsgCount)
            }
        }.flowOn(Dispatchers.IO)
    }
}