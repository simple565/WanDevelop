package com.maureen.wandevelop.feature.profile.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.maureen.wanandroid.UserPreferences
import com.maureen.wandevelop.R
import com.maureen.wandevelop.feature.setting.SettingAdapter
import com.maureen.wandevelop.databinding.FragmentProfileBinding
import com.maureen.wandevelop.entity.SettingItem
import com.maureen.wandevelop.entity.SettingType
import com.maureen.wandevelop.entity.UserDetailInfo
import com.maureen.wandevelop.ext.cacheSize
import com.maureen.wandevelop.ext.curVersionName
import com.maureen.wandevelop.ext.showAndRequest
import com.maureen.wandevelop.ext.startActivity
import com.maureen.wandevelop.base.ProgressDialog
import com.maureen.wandevelop.feature.profile.ProfileUiState
import com.maureen.wandevelop.feature.profile.ProfileViewModel
import com.maureen.wandevelop.feature.setting.DarkModeSetDialog
import com.maureen.wandevelop.feature.setting.NotificationActivity
import com.maureen.wandevelop.feature.setting.ReadRecordActivity
import com.maureen.wandevelop.util.DarkModeUtil
import com.maureen.wandevelop.util.UserPrefUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 我的页面
 */
class ProfileFragment : Fragment() {
    companion object {
        private const val TAG = "ProfileFragment"
    }
    private val viewModel: ProfileViewModel by viewModels()

    private lateinit var viewBinding: FragmentProfileBinding
    private val settingAdapter = SettingAdapter()

    private val badgeDrawable by lazy {
        BadgeDrawable.create(requireContext())
            .also { drawable-> drawable.backgroundColor = ContextCompat.getColor(requireContext(), R.color.red_500) }
    }

    private val signInOrUpLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        Log.d(TAG, "launcher: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            viewModel.loadUserDetail()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated: ")
        initView()
        observeData()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        viewModel.loadPreference()
        viewModel.loadUserDetail()
    }

    private fun initView() {
        Log.d(TAG, "initView: ")
        viewBinding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_notification) {
                requireContext().startActivity(NotificationActivity::class.java)
            }
            return@setOnMenuItemClickListener true
        }
        viewBinding.tvNickname.setOnClickListener {
            if (viewModel.alreadyLogin) {
                return@setOnClickListener
            }
            signInOrUpLauncher.launch(Intent(requireContext(), SignInOrUpActivity::class.java))
        }
        viewBinding.rvSettings.adapter = settingAdapter
        viewBinding.rvSettings.itemAnimator = null
    }
    private fun observeData() = lifecycleScope.launch {
        viewModel.uiState.collectLatest {
            when (it) {
                is ProfileUiState.LoadUserPreference -> {
                    initSettings(it.preferences)
                    updateUnreadBadge(it.unreadCount > 0)
                }
                is ProfileUiState.LoadUserInfoSuccess -> {
                    updateUserInfo(it.userInfo)
                    Log.d(TAG, "observeData: ${it.unreadCount}")
                    updateUnreadBadge(it.unreadCount > 0)
                }
                is ProfileUiState.LoadUserInfoFail -> {
                    Toast.makeText(requireContext(), it.msg, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }
    private fun initSettings(preferences: UserPreferences?) {
        Log.d(TAG, "initSettings: preference is null ${preferences == null}")
        val list = mutableListOf<SettingItem>()
        list.add(
            SettingItem(
                type = SettingType.ROUTE,
                name = getString(R.string.nav_dark_mode_set),
                value = DarkModeUtil.convertDarkModeName(requireContext(), preferences?.darkMode?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
                action = this::showDarkModeSetDialog
            )
        )
        list.add(SettingItem(type = SettingType.ROUTE, name = getString(R.string.nav_my_share), action = { requireContext().startActivity(
            ReadRecordActivity::class.java) }))
        list.add(SettingItem(type = SettingType.ROUTE, name = getString(R.string.nav_read_record), action = { requireContext().startActivity(
            ReadRecordActivity::class.java) }))
        list.add(SettingItem(type= SettingType.ACTION, name = getString(R.string.nav_clear_cache), value = requireContext().cacheSize, action = this::clearCache))
        list.add(SettingItem(type= SettingType.ACTION, name = getString(R.string.nav_check_update), value = requireContext().curVersionName, action = this::checkUpdate))
        list.add(SettingItem(type= SettingType.ROUTE, name = getString(R.string.nav_about_us)))
        list.add(SettingItem.EMPTY)
        list.add(SettingItem(type= SettingType.ACTION, name = getString(R.string.nav_logout), warn = true, action = this::logout))
        settingAdapter.items = list
        settingAdapter.notifyItemRangeChanged(0, list.size)
    }
    private fun clearCache() = lifecycleScope.launch {
        val findResult = settingAdapter.findItemByName(getString(R.string.nav_clear_cache))
        val item = findResult.second ?: return@launch
        withContext(Dispatchers.IO) {
            Log.d(TAG, "clearCache: delete internal result ${requireContext().cacheDir?.deleteRecursively()}")
            Log.d(TAG, "clearCache: delete external result ${requireContext().externalCacheDir?.deleteRecursively()}")
        }
        item.value = requireContext().cacheSize
        settingAdapter.notifyItemChanged(findResult.first)
    }
    private fun checkUpdate() = lifecycleScope.launch {

    }
    private fun showDarkModeSetDialog() = lifecycleScope.launch {
        val findResult = settingAdapter.findItemByName(getString(R.string.nav_dark_mode_set))
        val item = findResult.second ?: return@launch
        val mode = DarkModeSetDialog().showAndRequest(childFragmentManager, DarkModeSetDialog.TAG, DarkModeSetDialog.REQUEST_KEY, viewLifecycleOwner)
            .firstOrNull()?.getInt(DarkModeSetDialog.RESULT_MODE)?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        item.value = DarkModeUtil.convertDarkModeName(requireContext(), mode)
        Log.d(TAG, "showDarkModeSetDialog: select mode $mode name ${item.value}")
        withContext(Dispatchers.IO) {
            UserPrefUtil.setPreference(requireContext()) { builder -> builder.setDarkMode(mode) }
        }
        Log.d(TAG, "showDarkModeSetDialog: ${findResult.first}")
        settingAdapter.notifyItemChanged(findResult.first)
        // 模式转变时会引起activity重建，在manifest中配置了configChange后不会重建
        AppCompatDelegate.setDefaultNightMode(mode)
    }
    private fun logout() {
        val progressDialog = ProgressDialog.newInstance("退出登录中……")
        viewModel.logoutAndClear()
            .onStart { progressDialog.show(childFragmentManager, ProgressDialog.TAG) }
            .onCompletion {
                updateUserInfo(null)
                progressDialog.dismiss()
            }
            .launchIn(lifecycleScope)
    }
    private fun updateUserInfo(info: UserDetailInfo?) {
        with(viewBinding) {
            tvNickname.text = info?.userInfo?.userName?: getString(R.string.nav_sign_in_or_up)
            tvEmail.text = info?.userInfo?.email ?: ""
            tvEmail.isVisible = info?.userInfo?.email?.isNotEmpty() ?: false
            tvCoin.text = info?.coinInfo?.coinCount?.toString() ?: getString(R.string.prompt_default_value)
            tvRank.text = info?.coinInfo?.rank?.run { String.format("Lv.%s", this) } ?: getString(R.string.prompt_default_value)
            tvLevel.text = info?.coinInfo?.level?.toString()?.run { String.format("No.%s", this) } ?: getString(R.string.prompt_default_value)
        }
    }
    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    private fun updateUnreadBadge(show: Boolean) {
        if (show) {
            BadgeUtils.attachBadgeDrawable(badgeDrawable, viewBinding.toolbar, R.id.menu_notification)
        } else {
            BadgeUtils.detachBadgeDrawable(badgeDrawable, viewBinding.toolbar, R.id.menu_notification)
        }
    }
}