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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.maureen.wandevelop.R
import com.maureen.wandevelop.base.view.ProgressDialog
import com.maureen.wandevelop.databinding.FragmentProfileBinding
import com.maureen.wandevelop.entity.ProfileInfo
import com.maureen.wandevelop.entity.SettingItem
import com.maureen.wandevelop.entity.SettingType
import com.maureen.wandevelop.entity.UserDetailInfo
import com.maureen.wandevelop.ext.cacheSize
import com.maureen.wandevelop.ext.showAndRequest
import com.maureen.wandevelop.ext.startActivity
import com.maureen.wandevelop.feature.profile.ProfileViewModel
import com.maureen.wandevelop.feature.setting.DarkModeSetDialog
import com.maureen.wandevelop.feature.setting.NotificationActivity
import com.maureen.wandevelop.feature.setting.ReadRecordActivity
import com.maureen.wandevelop.feature.setting.SettingAdapter
import com.maureen.wandevelop.util.DarkModeUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
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
        initSettings()
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        viewModel.loadProfile()
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
        repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.profileInfoFlow.collectLatest {
                updateUserInfo(it.userDetailInfo)
                updateUnreadBadge(it.unreadCount)
                updateSettings(it)
            }
        }
    }

    private fun initSettings() {
        val list = mutableListOf<SettingItem>()
        list.add(SettingItem(type = SettingType.ROUTE, name = getString(R.string.nav_my_bookmark), action = { requireContext().startActivity(
            ReadRecordActivity::class.java) }))
        list.add(SettingItem(type = SettingType.ROUTE, name = getString(R.string.nav_read_later), action = { requireContext().startActivity(
            ReadRecordActivity::class.java) }))
        list.add(SettingItem(type = SettingType.ROUTE, name = getString(R.string.nav_my_share), action = { requireContext().startActivity(
            ReadRecordActivity::class.java) }))
        list.add(SettingItem(type = SettingType.ROUTE, name = getString(R.string.nav_read_record), action = { requireContext().startActivity(
            ReadRecordActivity::class.java) }))
        list.add(SettingItem(type = SettingType.ROUTE, name = getString(R.string.nav_todo), action = { requireContext().startActivity(
            ReadRecordActivity::class.java) }))
        list.add(SettingItem.EMPTY)
        list.add(
            SettingItem(
                type = SettingType.ROUTE,
                name = getString(R.string.nav_dark_mode_set),
                value = DarkModeUtil.convertDarkModeName(requireContext(), AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM),
                action = this::showDarkModeSetDialog
            )
        )
        list.add(SettingItem(type= SettingType.ACTION, name = getString(R.string.nav_clear_cache), value = requireContext().cacheSize, action = this::clearCache))
        list.add(SettingItem(type= SettingType.ROUTE, name = getString(R.string.nav_about_us)))
        list.add(SettingItem.EMPTY)
        list.add(SettingItem(type= SettingType.ACTION, name = getString(R.string.nav_logout), warn = true, action = this::logout))
        settingAdapter.items = list
        settingAdapter.notifyItemRangeChanged(0, list.size)
    }

    private fun clearCache() = lifecycleScope.launch {
        val findResult = settingAdapter.findItemByName(getString(R.string.nav_clear_cache)) ?: return@launch
        withContext(Dispatchers.IO) {
            Log.d(TAG, "clearCache: delete internal result ${requireContext().cacheDir?.deleteRecursively()}")
            Log.d(TAG, "clearCache: delete external result ${requireContext().externalCacheDir?.deleteRecursively()}")
        }
        findResult.second.value = requireContext().cacheSize
        settingAdapter.notifyItemChanged(findResult.first)
    }

    private fun showDarkModeSetDialog() = lifecycleScope.launch {
        val findResult = settingAdapter.findItemByName(getString(R.string.nav_dark_mode_set)) ?: return@launch
        val mode = DarkModeSetDialog().showAndRequest(childFragmentManager, DarkModeSetDialog.TAG, DarkModeSetDialog.REQUEST_KEY, viewLifecycleOwner)
            .firstOrNull()?.getInt(DarkModeSetDialog.RESULT_MODE)?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        findResult.second.value = DarkModeUtil.convertDarkModeName(requireContext(), mode)
        settingAdapter.notifyItemChanged(findResult.first)
        DarkModeUtil.setDarkMode(mode)
    }

    private fun logout() = lifecycleScope.launch {
        if (!viewModel.alreadyLogin) {
            Toast.makeText(requireContext(), "用户未登录", Toast.LENGTH_SHORT).show()
            return@launch
        }
        val progressDialog = ProgressDialog.newInstance("退出登录中……")
        progressDialog.show(childFragmentManager, ProgressDialog.TAG)
        viewModel.logoutAndClear()
        progressDialog.dismissAllowingStateLoss()
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

    private fun updateSettings(profileInfo: ProfileInfo) {
        settingAdapter.findItemByName(getString(R.string.nav_dark_mode_set))?.let {
            it.second.value = profileInfo.darkModeState
            settingAdapter.notifyItemChanged(it.first)
        }
        settingAdapter.findItemByName(getString(R.string.nav_clear_cache))?.let {
            it.second.value = profileInfo.cacheSize
            settingAdapter.notifyItemChanged(it.first)
        }
    }

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    private fun updateUnreadBadge(msgCount: Int) {
        if (msgCount > 0) {
            BadgeUtils.attachBadgeDrawable(badgeDrawable, viewBinding.toolbar, R.id.menu_notification)
        } else {
            BadgeUtils.detachBadgeDrawable(badgeDrawable, viewBinding.toolbar, R.id.menu_notification)
        }
    }
}