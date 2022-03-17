package co.iskv.crypto.ui.splash

import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.animation.doOnEnd
import co.iskv.crypto.R
import co.iskv.crypto.databinding.FragmentSplashBinding
import co.iskv.crypto.presentation.splash.SplashPresenter
import co.iskv.crypto.presentation.splash.SplashView
import co.iskv.crypto.ui.base.BaseFragment
import moxy.ktx.moxyPresenter

class SplashFragment : BaseFragment<FragmentSplashBinding>(R.layout.fragment_splash), SplashView {

    companion object {
        fun newInstance() = SplashFragment()
    }

    override val bindingProvider: (View) -> FragmentSplashBinding
        get() = FragmentSplashBinding::bind

    private val presenter by moxyPresenter { scope.getInstance(SplashPresenter::class.java) }

    private val textAnimation = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 3500L
        repeatCount = ValueAnimator.RESTART
        repeatMode = ValueAnimator.REVERSE
        addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Float

            val startValue = ""
            val endValue = "GRAPH "

            val lengthDiff = endValue.length - startValue.length
            val currentDiff = (lengthDiff * animatedValue).toInt()
            val result = if (currentDiff > 0) {
                endValue.substring(0, startValue.length + currentDiff)
            } else {
                startValue.substring(0, startValue.length + currentDiff)
            }

            binding.tvTitle.text = result
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.graphAnim.addAnimatorUpdateListener {
            it.doOnEnd { presenter.onMenuOpen() }
        }
    }

    override fun onStart() {
        super.onStart()
        textAnimation.start()
    }

    override fun onPause() {
        super.onPause()
        textAnimation.cancel()
    }

}