package com.maureen.wandevelop.ext

import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.callbackFlow

/**
 * Fragment 扩展
 * @author lianml
 * @date 2024/2/16
 */
fun DialogFragment.showAndRequest(
    fragmentManager: FragmentManager,
    tag: String,
    requestKey: String,
    lifecycleOwner: LifecycleOwner
) = callbackFlow {
    fragmentManager.setFragmentResultListener(requestKey, lifecycleOwner) { _, result ->
        trySendBlocking(result)
    }
    this@showAndRequest.show(fragmentManager, tag)
    awaitClose {
        this@showAndRequest.dismissAllowingStateLoss()
        Log.d(tag, "showAndRequest: close")
    }
}

fun Fragment.showAndRequest(
    fragmentManager: FragmentManager,
    requestKey: String,
    lifecycleOwner: LifecycleOwner
) = callbackFlow {
    fragmentManager.setFragmentResultListener(requestKey, lifecycleOwner) { _, result ->
        trySendBlocking(result)
    }
    fragmentManager.beginTransaction().show(this@showAndRequest).commitAllowingStateLoss()
    awaitClose {
        fragmentManager.beginTransaction().remove(this@showAndRequest).commitAllowingStateLoss()
        Log.d("showAndRequest", "close")
    }
}