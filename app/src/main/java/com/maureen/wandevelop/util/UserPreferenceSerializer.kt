package com.maureen.wandevelop.util

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.maureen.wanandroid.UserPreferences
import java.io.InputStream
import java.io.OutputStream

/**
 * @author lianml
 * @date 2024/2/10
 */
object UserPreferenceSerializer : Serializer<UserPreferences> {
    // 默认值
    override val defaultValue: UserPreferences = UserPreferences.getDefaultInstance()

    // 如何从文件里读取Protobuf内容
    override suspend fun readFrom(input: InputStream): UserPreferences {
        return try {
            // readFrom is already called on the data store background thread
            UserPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    // 将Protobuf写入到文件
    override suspend fun writeTo(t: UserPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}
