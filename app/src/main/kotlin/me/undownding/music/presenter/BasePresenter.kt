package me.undownding.music.presenter

import android.view.View
import butterknife.ButterKnife
import com.trello.rxlifecycle.components.support.RxFragment

/**
 * Created by undownding on 16-6-12.
 */
abstract class BasePresenter<T>() {

    abstract fun getFragment(): RxFragment

    abstract fun requestData()

    abstract fun onItemClick(item: T)

    open fun bind(view: View): View {
        ButterKnife.bind(getFragment(), view)
        return view
    }
}