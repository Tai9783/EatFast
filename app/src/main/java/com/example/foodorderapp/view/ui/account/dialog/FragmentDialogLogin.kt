package com.example.foodorderapp.view.ui.account.dialog

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.example.foodorderapp.R

private var foodBounceAnimator: ObjectAnimator? = null
private var splashAnimatorSet: AnimatorSet? = null
private val dotAnimators = mutableListOf<ObjectAnimator>()
class FragmentDialogLogin : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dialog_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //bỏ nền trắng thừa các góc của hộp thoại
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val imgAnimation= view.findViewById<ImageView>(R.id.imgAnimation)
        val splashEffectView= view.findViewById<View>(R.id.splashEffectView)
        val dot1= view.findViewById<View>(R.id.dot1)
        val dot2= view.findViewById<View>(R.id.dot2)
        val dot3= view.findViewById<View>(R.id.dot3)
        isCancelable= false
        startFoodBounceAnimation(imgAnimation,splashEffectView)
        startLoadingDotsAnimation(dot1,dot2,dot3)
    }

    private fun startLoadingDotsAnimation(dot1: View, dot2: View, dot3: View) {
        val dots = listOf(dot1, dot2,dot3)
        val delayStep = 200L // Độ trễ giữa các chấm

        dots.forEachIndexed { index, dot ->
            val animator = ObjectAnimator.ofFloat(dot, "translationY", 0f, -10f, 0f).apply {
                duration = 600 // Thời gian nhảy của mỗi chấm
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.RESTART
                interpolator = AccelerateDecelerateInterpolator()
                startDelay = delayStep * index // Tạo hiệu ứng nhảy nối tiếp
            }
            dotAnimators.add(animator)
            animator.start()
        }
    }

    private fun startFoodBounceAnimation(imgAnimation: ImageView, splashEffectView: View) {

        foodBounceAnimator = ObjectAnimator.ofFloat(imgAnimation, "translationY", 0f, 20f, 0f).apply {
            duration = 1000 // 1 giây cho một chu kỳ nhảy
            repeatCount = ObjectAnimator.INFINITE // Lặp vô hạn
            repeatMode = ObjectAnimator.RESTART // Bắt đầu lại từ đầu sau mỗi chu kỳ
            interpolator = AccelerateDecelerateInterpolator() // Tăng tốc khi xuống, giảm tốc khi lên

            // Lắng nghe sự kiện để kích hoạt hiệu ứng tóe nước khi chạm đáy
            addUpdateListener { animator ->
                val animatedValue = animator.animatedValue as Float
                // Khi icon gần chạm đáy (translationY gần 20f)
                if (animatedValue > 18f && splashAnimatorSet?.isRunning != true) {
                    startSplashEffect(splashEffectView)
                }
            }
        }
        foodBounceAnimator?.start()
    }

    private fun startSplashEffect(splashEffectView: View) {
        // Animation cho hiệu ứng tóe nước
        val splashView = splashEffectView

        // Animation cho alpha (hiện lên và biến mất)
        val alphaAnimator = ObjectAnimator.ofFloat(splashView, "alpha", 0f, 1f, 0f).apply {
            duration = 500 // Nhanh chóng hiện lên và biến mất
            interpolator = DecelerateInterpolator()
        }

        // Animation cho scale (phình ra một chút)
        val scaleXAnimator = ObjectAnimator.ofFloat(splashView, "scaleX", 0.5f, 1.2f, 1f).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
        }
        val scaleYAnimator = ObjectAnimator.ofFloat(splashView, "scaleY", 0.5f, 1.2f, 1f).apply {
            duration = 500
            interpolator = DecelerateInterpolator()
        }

        splashAnimatorSet = AnimatorSet().apply {
            playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator)
            start()
        }


    }
    override fun onStop() {
        super.onStop()
        // Dừng tất cả các animation khi Fragment không còn hiển thị
        foodBounceAnimator?.cancel()
        splashAnimatorSet?.cancel()
        dotAnimators.forEach { it.cancel() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}