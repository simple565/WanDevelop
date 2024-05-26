package com.maureen.wandevelop.feature.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maureen.wandevelop.R
import com.maureen.wandevelop.databinding.AcitivityNotificationBinding
import com.maureen.wandevelop.databinding.AcitivityReadRecordBinding

class ReadRecordActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        AcitivityReadRecordBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        initView()
    }

    private fun initView() {
        with(viewBinding.toolbar) {
            setNavigationOnClickListener { finish() }
            setOnMenuItemClickListener {
                when(it.itemId) {
                    R.id.menu_clear -> clearRecord()
                    R.id.menu_search -> startSearch()
                }
                return@setOnMenuItemClickListener true
            }
        }
    }

    private fun clearRecord() {

    }

    private fun startSearch() {

    }
}