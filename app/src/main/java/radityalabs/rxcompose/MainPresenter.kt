package radityalabs.rxcompose

import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

fun <T> observableIo(): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun <T> loading(showLoading: (Boolean) -> Unit): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.doOnSubscribe { showLoading(true) }
                .doOnComplete { showLoading(false) }
    }
}

fun <T> before(block: () -> Unit): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.doOnSubscribe { block() }
    }
}

fun <T> after(block: () -> Unit): ObservableTransformer<T, T> {
    return ObservableTransformer { upstream ->
        upstream.doOnComplete { block() }
    }
}

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter {

    private val disposable = CompositeDisposable()

    private fun before() {
        Observable.just(Random().nextInt(100).toDouble())
                .compose(observableIo())
                .subscribe { number ->
                    view.showBeforeWork(number)
                }
    }

    private fun after() {
        Observable.just(Random().nextInt(100).toDouble())
                .compose(observableIo())
                .subscribe { number ->
                    view.showAfterWork(number)
                }
    }

    override fun doSomeWork() {
        val d = Observable.create(ObservableOnSubscribe<String> { e ->
            for (i in 0 until 10) {
                val number = Math.pow(Random().nextInt(100).toDouble(), Random().nextInt(100).toDouble())
                e.onNext(number.toString())
            }
            e.onComplete()
        }).compose(loading { isShow ->
            if (isShow) {
                view.showLoading()
            } else {
                view.hideLoading()
            }
        }).compose(before { before()
        }).compose(after { after()
        }).compose(observableIo())
                .subscribe { number ->
                    view.showCompleteWork(number)
                }
        disposable.add(d)
    }

    override fun onDetach() {
        disposable.clear()
    }
}

interface MainContract {
    interface View {
        fun showCompleteWork(status: String)

        fun showLoading()

        fun hideLoading()

        fun showAfterWork(number : Double)

        fun showBeforeWork(number : Double)
    }

    interface Presenter : BasePresenter {
        fun doSomeWork()
    }
}

interface BasePresenter {
    fun onDetach()
}
