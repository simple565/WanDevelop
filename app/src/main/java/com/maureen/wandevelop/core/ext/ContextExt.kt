package com.maureen.wandevelop.core.ext

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.NameNotFoundException
import android.text.format.Formatter
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

/**
 * Function:
 * Date:   2021/7/9
 * @author lianml
 */
fun Context.startActivity(target: Class<*>) {
    this.startActivity(Intent(this, target))
}

val Context.preferenceStore: DataStore<Preferences> by preferencesDataStore(name = "userPreference")

val Context.curVersionName: String
    get() {
        return try {
            this.packageManager.getPackageInfo(this.packageName, 0)?.versionName ?: ""
        } catch (e: NameNotFoundException) {
            Log.e("ContextEx", "curVersionName: ", e)
            ""
        }
    }

val Context.cacheSize: String
    get() {
        var size = 0L
        this.cacheDir.listFiles()?.forEach {
            size += it.length()
        }
        this.externalCacheDir?.listFiles()?.forEach {
            size += it.length()
        }
        return Formatter.formatFileSize(this, size).uppercase()
    }

val Context.screenWidth: Int
    get() {
        return this.resources.displayMetrics.widthPixels
    }

val Context.screenHeight: Int
    get() {
        return this.resources.displayMetrics.heightPixels
    }