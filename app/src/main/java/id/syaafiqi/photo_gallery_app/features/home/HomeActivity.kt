package id.syaafiqi.photo_gallery_app.features.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import id.syaafiqi.photo_gallery_app.R
import id.syaafiqi.photo_gallery_app.core.data.PagedRequest
import id.syaafiqi.photo_gallery_app.databinding.ActivityHomeBinding
import id.syaafiqi.photo_gallery_app.extensions.hide
import id.syaafiqi.photo_gallery_app.extensions.show
import io.reactivex.plugins.RxJavaPlugins


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val observer: HomeViewObserver = HomeViewObserver()
    private val presenter: HomePresenter = HomePresenter(observer)
    private lateinit var adapter: HomeAdapter

    private var paging: PagedRequest = PagedRequest()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActions()
        initView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)

        val search = menu?.findItem(R.id.nav_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = getString(R.string.search)
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                paging.keyword = if (p0.isNullOrBlank()) "" else p0
                paging.page = 1
                presenter.getPhotos(paging)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean = false
        })
        searchView.setOnCloseListener {
            paging.keyword = ""
            paging.page = 1
            presenter.getPhotos(paging)
            false
        }

        return super.onCreateOptionsMenu(menu)
    }

    private fun initView() {
        paging = PagedRequest()
        presenter.getPhotos(paging)
    }

    private fun setActions() {
        binding.photoRefresherPanel.setOnRefreshListener {
            initView()
        }

        binding.layoutSwitcherFab.setOnClickListener {
            when (binding.rvPhotos.layoutManager) {
                is GridLayoutManager -> setLinearRecyclerView()
                is LinearLayoutManager -> setGridRecyclerView()
            }
        }

    }

    private fun setLinearRecyclerView() {
        binding.layoutSwitcherFab.setImageResource(R.drawable.ic_view_module_24)
        binding.rvPhotos.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvPhotos.clearOnScrollListeners()
        binding.rvPhotos.addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = binding.rvPhotos.layoutManager as LinearLayoutManager
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                if (!observer.isLastPage && !observer.isLoadMore &&
                    visibleItemCount.plus(firstVisibleItemPosition) >= totalItemCount &&
                    firstVisibleItemPosition >= 0
                ) {
                    paging.page++
                    presenter.fetchPhotos(paging)
                }
            }
        })
    }

    private fun setGridRecyclerView() {
        binding.layoutSwitcherFab.setImageResource(R.drawable.ic_view_list_24)
        binding.rvPhotos.layoutManager = GridLayoutManager(binding.root.context, 2)
        binding.rvPhotos.clearOnScrollListeners()
        binding.rvPhotos.addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(
                recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                with(recyclerView.layoutManager as GridLayoutManager) {
                    if (childCount + findFirstVisibleItemPosition() == itemCount && !observer.isLastPage && !observer.isLoadMore) {
                        paging.page++
                        presenter.fetchPhotos(paging)
                    }
                }
            }
        })
        (binding.rvPhotos.layoutManager as GridLayoutManager).spanSizeLookup =
            object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    when (adapter.getItemViewType(position)) {
                        0 -> 2
                        else -> 1
                    }
            }
    }

    inner class HomeViewObserver : HomeView {
        override var isLastPage: Boolean = false
        override var isLoadMore: Boolean = false

        override fun onShowLoading() {
            binding.loadingState.root.show()
        }

        override fun onHideLoading() {
            binding.loadingState.root.hide()
            binding.photoRefresherPanel.isRefreshing = false
        }

        override fun onLoadSuccess(models: List<PhotosModel>) {
            binding.errorState.root.hide()

            when (models.size) {
                0 -> binding.emptyState.root.show()
                else -> {
                    adapter = HomeAdapter(ArrayList(models))
                    binding.rvPhotos.adapter = adapter
                    when (binding.rvPhotos.layoutManager) {
                        is GridLayoutManager -> setGridRecyclerView()
                        is LinearLayoutManager -> setLinearRecyclerView()
                        else -> setGridRecyclerView()
                    }
                }
            }
        }

        override fun onLoadFailed() {
            with(binding.errorState) {
                tryAgainBtn.setOnClickListener {
                    root.hide()
                    initView()
                }
                root.show()
            }
        }

        override fun onFetchSuccess(models: List<PhotosModel>) {
            adapter.addItems(ArrayList(models), isLastPage)
        }

        override fun onFetchFailed() {
            with(binding.errorState) {
                tryAgainBtn.setOnClickListener {
                    root.hide()
                    initView()
                }
                root.show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}