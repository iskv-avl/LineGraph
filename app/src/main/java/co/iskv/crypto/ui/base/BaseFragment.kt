package co.iskv.crypto.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import co.iskv.crypto.di.DI
import moxy.MvpAppCompatFragment
import toothpick.Scope
import toothpick.Toothpick

const val STATE_SCOPE_NAME = "state_scope_name"

abstract class BaseFragment<T : ViewBinding>(layoutRes: Int) : MvpAppCompatFragment(layoutRes),
    ScopeHolder, BackPressListener {

    abstract val bindingProvider: (View) -> T

    override lateinit var fragmentScopeName: String
    override lateinit var scope: Scope

    override val parentScopeName: String by lazy {
        (parentFragment as? BaseFragment<*>)?.fragmentScopeName
            ?: DI.APP_SCOPE
    }

    private var _binding: T? = null
    val binding: T get() = _binding!!

    private var instanceStateSaved: Boolean = false

    override fun installModules(scope: Scope) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        fragmentScopeName = savedInstanceState?.getString(STATE_SCOPE_NAME)
            ?: "${javaClass.simpleName}_${hashCode()}"

        if (Toothpick.isScopeOpen(fragmentScopeName)) {
            scope = Toothpick.openScope(fragmentScopeName)
        } else {
            scope = Toothpick.openScopes(parentScopeName, fragmentScopeName)
            installModules(scope)
        }

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = super.onCreateView(inflater, container, savedInstanceState)
        ?.also { _binding = bindingProvider.invoke(it) }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun needCloseScope(): Boolean =
        when {
            activity?.isChangingConfigurations == true -> false
            activity?.isFinishing == true -> true
            else -> isRealRemoving()
        }

    override fun onDestroy() {
        super.onDestroy()
        if (needCloseScope()) {
            Toothpick.closeScope(scope.name)
        }
    }

    override fun onResume() {
        super.onResume()
        instanceStateSaved = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        instanceStateSaved = true
        outState.putString(STATE_SCOPE_NAME, fragmentScopeName)
    }

    override fun isRealRemoving(): Boolean =
        (isRemoving && !instanceStateSaved) || // Because isRemoving == true for fragment in backstack on screen rotation
                ((parentFragment as? BaseFragment<*>)?.isRealRemoving() ?: false)

    override fun onBackPressed() {}

}