package com.maureen.wandevelop.ui.profile

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.maureen.wandevelop.R
import com.maureen.wandevelop.databinding.ActivityLoginBinding

/**
 * Function:登录和注册页面
 *
 * @author lianml
 * Create 2021-07-05
 */
class SignInOrUpActivity : AppCompatActivity() {
    private val viewModel: ProfileViewModel by viewModels()
    private val viewBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.login_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
    }
}