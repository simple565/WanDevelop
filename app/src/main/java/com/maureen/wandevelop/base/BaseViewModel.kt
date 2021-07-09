package com.maureen.wandevelop.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonIOException
import com.google.gson.JsonParseException
import com.maureen.wandevelop.MyApplication
import com.maureen.wandevelop.R
import com.maureen.wandevelop.ext.showToast
import com.maureen.wandevelop.network.ApiException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * Function:
 * Date:   2021/7/8
 * @author lianml
 */
open class BaseViewModel : ViewModel() {

    /**
     * 创建并执行协程
     * @param context 线程
     * @param block 协程中执行
     * @param error 错误时执行
     * @param cancel 取消时只需
     * @param showErrorToast 是否弹出错误吐司
     * @return Job
     */
    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend (CoroutineScope) -> Unit,
        error: (suspend (Exception) -> Unit?)? = null,
        cancel: (suspend (Exception) -> Unit?)? = null,
        showErrorToast: Boolean = true
    ): Job {
        return viewModelScope.launch(context) {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        onError(e, showErrorToast)
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    /**
     * 取消协程
     * @param job 协程job
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 统一处理错误
     * @param e 异常
     * @param showErrorToast 是否显示错误吐司
     */
    private fun onError(e: Exception, showErrorToast: Boolean) {
        when (e) {
            is ApiException -> {
                when (e.code) {
                    -1001 -> {
                        // 登录失效，清除用户信息、清除cookie/token
                    }
                    // 其他api错误
                    -1 -> if (showErrorToast) MyApplication.instance.showToast(e.message)
                    // 其他错误
                    else -> if (showErrorToast) MyApplication.instance.showToast(e.message)
                }
            }
            // 网络请求失败
            is ConnectException,
            is SocketTimeoutException,
            is UnknownHostException,
            is HttpException,
            is SSLHandshakeException ->
                if (showErrorToast) MyApplication.instance.showToast(R.string.network_request_failed)
            // 数据解析错误
            is JsonIOException,
            is JsonParseException ->
                if (showErrorToast) MyApplication.instance.showToast(R.string.api_data_parse_error)
            // 其他错误
            else ->
                if (showErrorToast) MyApplication.instance.showToast(e.message ?: return)
        }
    }
}