package co.iskv.crypto.presentation.base

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import moxy.MvpPresenter
import moxy.MvpView

open class BasePresenter<T: MvpView> : MvpPresenter<T>() {

    private val destroyCompositeDisposable = CompositeDisposable()
    private var detachCompositeDisposable: CompositeDisposable? = null

    override fun attachView(view: T) {
        super.attachView(view)
        detachCompositeDisposable = CompositeDisposable()
    }

    override fun detachView(view: T) {
        detachCompositeDisposable?.dispose()
        super.detachView(view)
    }

    override fun onDestroy() {
        destroyCompositeDisposable.dispose()
        super.onDestroy()
    }

    fun Disposable.disposeOnDestroy() {
        destroyCompositeDisposable.add(this)
    }

    fun Disposable.disposeOnDetach() {
        detachCompositeDisposable?.add(this)
    }

}