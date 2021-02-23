package com.maureen.wandevelop.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * Function:
 * @author lianml
 * Create 2021-02-11
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected abstract fun initView()
}