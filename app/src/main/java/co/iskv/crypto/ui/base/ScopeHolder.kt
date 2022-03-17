package co.iskv.crypto.ui.base

import toothpick.Scope

interface ScopeHolder {

    val parentScopeName: String

    var fragmentScopeName: String
    var scope: Scope

    fun installModules(scope: Scope) {}
    fun isRealRemoving(): Boolean

}