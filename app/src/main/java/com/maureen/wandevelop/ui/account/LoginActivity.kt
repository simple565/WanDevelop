package com.maureen.wandevelop.ui.account

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.maureen.wandevelop.R
import com.maureen.wandevelop.databinding.ActivityLoginBinding
import com.maureen.wandevelop.viewmodels.AccountViewModel

class LoginActivity : AppCompatActivity() {
    private val viewModel: AccountViewModel by viewModels()
    private val viewBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.login_fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_login, R.id.nav_register))
        with(viewBinding) {
            with(loginToolBar) {
                setSupportActionBar(this)
            }
            setupActionBarWithNavController(navController, appBarConfiguration)
        }
    }
}