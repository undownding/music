package me.undownding.music.fragment

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.baidu.music.model.Album
import com.trello.rxlifecycle.components.support.RxAppCompatActivity
import me.undownding.binding.BindingAdapter
import me.undownding.music.BR
import me.undownding.music.R
import me.undownding.music.presenter.AlbumListPresenter
import me.undownding.music.presenter.BasePresenter

/**
 * Created by undownding on 16-6-11.
 */
class AlbumListFragment: BaseFragment<Album>() {

    private val layoutId = R.layout.item_list_album
    private val brId = BR.item

    @BindView(android.R.id.list)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    val presenter by lazy { AlbumListPresenter(this) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return presenter.bind(inflater?.inflate(R.layout.fragment_list, container, false)!!)
    }

    override fun onCreateAdapter(): BindingAdapter<Album> {
        return object : BindingAdapter<Album>(list, layoutId, brId) {
            override fun onClick(view: View?, item: Album?, position: Int) {
                presenter.onItemClick(item!!)
            }
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            (activity as RxAppCompatActivity).setSupportActionBar(toolbar)
            activity.title = "μ'sic"
        }
    }

    override fun getPresenter(): BasePresenter<Album> {
        return presenter
    }

}