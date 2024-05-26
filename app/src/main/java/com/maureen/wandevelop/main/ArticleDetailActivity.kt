package com.maureen.wandevelop.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.maureen.wandevelop.databinding.ActivityArticleDetailBinding

class ArticleDetailActivity : AppCompatActivity() {
    private val viewBinding by lazy {
        ActivityArticleDetailBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }
}