package com.test.appsfactorytask.screen.searchartist.view

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.test.appsfactorytask.R
import com.test.appsfactorytask.common.util.hideKeyboard
import com.test.appsfactorytask.common.util.setVisible
import com.test.appsfactorytask.core.view.BaseFragment
import com.test.appsfactorytask.screen.searchartist.viewmodel.ArtistUiState
import com.test.appsfactorytask.screen.searchartist.viewmodel.SearchArtistViewModel

class SearchArtistFragment : BaseFragment(R.layout.fragment_search_artist) {

    private val viewModel: SearchArtistViewModel by viewModels { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvEmptyResult = view.findViewById<View>(R.id.tvEmptyResult)
        val rvArtists = view.findViewById<RecyclerView>(R.id.rvArtists).apply { setHasFixedSize(true) }
        val adapter = ArtistAdapter { artist ->
            SearchArtistFragmentDirections
                .actionSearchArtistFragmentToTopAlbumsFragment(artist)
                .let { action -> view.findNavController().navigate(action) }
        }.also {
            rvArtists.adapter = it
        }
        viewModel.uiState.asLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is ArtistUiState.Success -> {
                    hideProgressbar()
                    tvEmptyResult.setVisible(it.artists.isEmpty())
                    rvArtists.setVisible(it.artists.isEmpty().not())
                    adapter.submitList(it.artists)
                }
                is ArtistUiState.Error -> {
                    showError(it.error)
                }
                ArtistUiState.Loading -> {
                    showProgressbar()
                }
                ArtistUiState.Idle -> {
                    // ignore
                }
            }
        }
        view.findViewById<EditText>(R.id.etSearch).apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onSearchClick()
                } else {
                    false
                }
            }
            addTextChangedListener { viewModel.onTextChange(it.toString()) }
        }
        view.findViewById<View>(R.id.btSearch).setOnClickListener { onSearchClick() }
    }

    override fun onStart() {
        super.onStart()
        actionBar?.apply {
            title = getString(R.string.title_search_artist)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun onSearchClick(): Boolean {
        hideKeyboard()
        viewModel.onSearch()
        return true
    }
}