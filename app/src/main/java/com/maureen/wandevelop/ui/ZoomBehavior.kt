package com.maureen.wandevelop.ui

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.imageview.ShapeableImageView


/**
 * Function:
 * @author lianml
 * Create 2021-07-17
 */
class ZoomBehavior(context: Context, attrs: AttributeSet?) :
    CoordinatorLayout.Behavior<ShapeableImageView>(context, attrs) {
    private var initFinish = false
    private var initChildX: Int = 0
    private var initChildY: Int = 0
    private var initParentX: Int = 0
    private var initParentY: Int = 0


    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: ShapeableImageView,
        dependency: View
    ): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: ShapeableImageView,
        dependency: View
    ): Boolean {
        if (!initFinish) {
            // 初始化
            initChildX = child.left
            initChildY = child.top
            initParentX = dependency.left
            initParentY = dependency.top
            initFinish = true
        } else {
            val translationY = initChildY + dependency.top
            Log.d("lianml", "lianml top ${dependency.top}")
            if (translationY > 0) {
                Log.d("lianml", "lianml hhhh $translationY")
                child.top = translationY
            }
        }
        return true
    }
}
