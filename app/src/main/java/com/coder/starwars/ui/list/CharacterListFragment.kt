package com.coder.starwars.ui.list

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coder.starwars.util.Constants.TYPING_BUFFER
import com.coder.starwars.R
import com.coder.starwars.data.network.model.CharacterItemModel
import com.coder.starwars.di.ViewModelFactory
import com.coder.starwars.util.extensions.gone
import com.coder.starwars.util.extensions.visible
import com.coder.starwars.util.EndlessScrollListener
import com.coder.starwars.ui.base.BaseFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.actionbar_toolbar.*
import kotlinx.android.synthetic.main.fragment_character_list.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * List fragment containing the list of Characters.
 * It contains search button for searching Character.
 */
class CharacterListFragment : BaseFragment(), CharacterListAdapter.Interaction {

    override val layout = R.layout.fragment_character_list
    override val viewModelClass = CharacterListViewModel::class.java

    val disposable = CompositeDisposable()

    @Inject
    lateinit var factory: ViewModelFactory

    override fun provideViewModelFactory() = factory

    val viewModel: CharacterListViewModel by lazy { baseViewModel as CharacterListViewModel }

    private val adapter: CharacterListAdapter by lazy { CharacterListAdapter(this) }

    // Provision of Search functionality
    private var searchView: SearchView? = null
    private val searchListener = PublishSubject.create<String>()

    //Pagination
    private lateinit var endlessScrollListener: EndlessScrollListener

    //Navigator
    private var navigator: CharacterNavigator? = null

    // For handling Fragment Transactions
    companion object {
        val TAG = "CharacterListFragment"
        fun newInstance() = CharacterListFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CharacterNavigator)
            navigator = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpToolbar(toolbar)

        initViews()

        observerPaginationLoading()
        observeCharacterList()
        initSearch()
        viewModel.initialLoad()

        endlessScrollListener = initEndlessScroll()
    }

    private fun initViews() {
        rv_characters.setHasFixedSize(true)
        rv_characters.adapter = adapter

        srl_characters.setOnRefreshListener {
            refreshCharacters()
        }

        bt_show_all.setOnClickListener {
            refreshCharacters()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val myActionMenuItem = menu.findItem(R.id.item_search)
        searchView = myActionMenuItem.actionView as SearchView
        searchView?.queryHint = getString(R.string.enter_query)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchListener.onNext(newText)
                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        rv_characters.addOnScrollListener(endlessScrollListener)
    }

    // Refresh Characters in the list
    private fun refreshCharacters() {
        ll_no_data.gone()
        searchView?.setQuery("", false)
        searchView?.clearFocus()
        viewModel.refreshCharacters()
    }

    // Observer Pagination loading
    private fun observerPaginationLoading() {
        viewModel.paginationLoading.observe(this, Observer {
            if (it) {
                pb_loading.visible()
            } else pb_loading.gone()
        })
    }

    // Seach functionality
    private fun initSearch() {
        searchListener
            .debounce(TYPING_BUFFER, TimeUnit.MILLISECONDS)
            .subscribe {
                endlessScrollListener.resetState()
                viewModel.searchCharacter(it)
            }
            .addTo(disposable)
    }

    // Observer Character List
    private fun observeCharacterList() {
        viewModel.characters.observe(this, Observer { characters ->
            if (characters.isEmpty()) {
                ll_no_data.visible()
                rv_characters.gone()
            } else {
                adapter.swapData(characters)
                ll_no_data.gone()
                rv_characters.visible()
            }
        })
    }

    // EndlessScrolling , will be called each time visibleThreshold reaches
    private fun initEndlessScroll() = object : EndlessScrollListener(
        layoutManager = rv_characters.layoutManager as LinearLayoutManager,
        visibleThreshold = 2
    ) {
        override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
            viewModel.loadNextPage()
        }
    }

    // Hide loading
    override fun hideLoading() {
        srl_characters.isRefreshing = false
    }

    // Show loading
    override fun showLoading() {
        srl_characters.isRefreshing = true
    }

    interface CharacterNavigator {
        fun showCharacterDetails(character: CharacterItemModel)
    }

    // On Adapter Clicked
    override fun characterClicked(character: CharacterItemModel) {
        searchView?.clearFocus()
        navigator?.showCharacterDetails(character)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}