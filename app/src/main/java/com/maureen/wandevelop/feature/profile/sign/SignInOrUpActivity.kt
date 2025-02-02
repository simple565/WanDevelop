package com.maureen.wandevelop.feature.profile.sign

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.maureen.wandevelop.databinding.ActivitySignInOrUpBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Function:登录和注册页面
 *
 * @author lianml
 * Create 2021-07-05
 */
class SignInOrUpActivity : AppCompatActivity() {
    private val viewModel: SignViewModel by viewModels()
    private val viewBinding by lazy {
        ActivitySignInOrUpBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        viewModel.uiState.onEach {
            Log.d("TAG", "onCreate: $it")
            if (it is SignUiState.Idle) {
                return@onEach
            }
            if (it is SignUiState.SignResult && it.result) {
                Toast.makeText(this, it.resultMsg, Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
        }.launchIn(lifecycleScope)
    }
}