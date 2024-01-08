package com.maureen.wandevelop.ext

import android.content.ComponentName
import android.content.Context
import android.content.Intent
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")