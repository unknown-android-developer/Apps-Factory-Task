package com.test.appsfactorytask.screen.albums.view

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.test.appsfactorytask.R
import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.common.util.setVisible
import com.test.appsfactorytask.core.view.BaseFragment
import com.test.appsfactorytask.screen.albums.viewmodel.AlbumsViewModel
import com.test.appsfactorytask.common.viewmodel.AlbumUiState

class AlbumsFragment : BaseFragment(R.layout.fragment_albums) {

    private val viewModel: AlbumsViewModel by viewModels { viewModelFactory }

    private val itemDecoration: RecyclerView.ItemDecoration =
        object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                if (parent.isLastItem(view)) {
                    val fabSize = view.resources.getDimension(R.dimen.fab_search_size).toInt()
                    val margin = view.resources.getDimension(R.dimen.padding).toInt()
                    outRect.bottom = fabSize + margin
                }
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fabSearch = view.findViewById<FloatingActionButton>(R.id.fabSearch)
        val tvEmptyStorage = view.findViewById<View>(R.id.tvEmptyStorage)
        val btSearch = view.findViewById<View>(R.id.btSearch)
        val albumsAdapter = AlbumsAdapter(viewModel::onAlbumClick, ::onDeleteClick)
        view.findViewById<RecyclerView>(R.id.rvAlbums).apply {
            setHasFixedSize(true)
            adapter = albumsAdapter
            addItemDecoration(itemDecoration)
        }
        viewModel.uiState.asLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AlbumUiState.SuccessAlbums -> {
                    hideProgressbar()
                    albumsAdapter.submitList(it.albums)
                    tvEmptyStorage.setVisible(it.albums.isEmpty())
                    btSearch.setVisible(it.albums.isEmpty())
                }
                is AlbumUiState.SuccessTracks -> {
                    hideProgressbar()
                    AlbumsFragmentDirections
                        .actionAlbumsFragmentToTracksBottomSheetFragment(it.tracks)
                        .let { action -> view.findNavController().navigate(action) }
                }
                is AlbumUiState.Error -> {
                    showError(it.error)
                }
                AlbumUiState.Loading -> {
                    showProgressbar()
                }
            }
        }
        fabSearch.setOnClickListener { onSearchClick() }
        btSearch.setOnClickListener { onSearchClick() }
    }

    override fun onStart() {
        super.onStart()
        actionBar?.apply {
            title = getString(R.string.title_local_albums)
            setDisplayHomeAsUpEnabled(false)
            setDisplayShowHomeEnabled(false)
        }
    }

    private fun onDeleteClick(album: AlbumModel) {
        AlertDialog.Builder(requireContext()).apply {
            setMessage(R.string.delete_album)
            setPositiveButton(android.R.string.ok) { _, _ ->
                viewModel.onDeleteAlbum(album)
            }
            setNegativeButton(android.R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
        }.show()
    }

    private fun onSearchClick() {
        AlbumsFragmentDirections
            .actionAlbumsFragmentToSearchArtistFragment()
            .let { view?.findNavController()?.navigate(it) }
    }

    private fun RecyclerView.isLastItem(
        view: View
    ): Boolean = getChildLayoutPosition(view) == (adapter?.itemCount ?: 0) - 1
}