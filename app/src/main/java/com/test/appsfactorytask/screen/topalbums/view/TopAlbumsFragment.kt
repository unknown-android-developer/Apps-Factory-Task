package com.test.appsfactorytask.screen.topalbums.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.test.appsfactorytask.R
import com.test.appsfactorytask.common.model.AlbumModel
import com.test.appsfactorytask.core.view.BaseFragment
import com.test.appsfactorytask.common.viewmodel.AlbumUiState
import com.test.appsfactorytask.screen.topalbums.viewmodel.TopAlbumsViewModel

class TopAlbumsFragment : BaseFragment(R.layout.fragment_top_albums) {

    private val viewModel: TopAlbumsViewModel by viewModels { viewModelFactory }

    private val args: TopAlbumsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actionBar?.title = getString(R.string.title_top_albums)
        val rvTopAlbums = view.findViewById<RecyclerView>(R.id.rvAlbums).apply { setHasFixedSize(true) }
        val topAlbumsAdapter = TopAlbumsAdapter(viewModel::onAlbumClick, ::onSaveClick)
        rvTopAlbums.adapter = topAlbumsAdapter
        viewModel.uiState.asLiveData().observe(viewLifecycleOwner) {
            when (it) {
                is AlbumUiState.SuccessAlbums -> {
                    hideProgressbar()
                    topAlbumsAdapter.submitList(it.albums)
                }
                is AlbumUiState.SuccessTracks -> {
                    hideProgressbar()
                    TopAlbumsFragmentDirections
                        .actionTopAlbumsFragmentToTracksBottomSheetFragment(it.tracks)
                        .let { action ->
                            view.findNavController().navigate(action)
                        }
                }
                is AlbumUiState.Error -> {
                    hideProgressbar()
                    showError(it.error)
                }
                AlbumUiState.Loading -> {
                    showProgressbar()
                }
            }
        }
        viewModel.onArtist(args.artist)
    }

    private fun onSaveClick(album: AlbumModel) {
        if (album.isSaved) {
            AlertDialog.Builder(requireContext()).apply {
                setMessage(R.string.delete_album)
                setPositiveButton(android.R.string.ok) { _, _ ->
                    viewModel.onSaveClick(album)
                }
                setNegativeButton(android.R.string.cancel) { dialog, _ ->
                    dialog.dismiss()
                }
            }.show()
        } else {
            viewModel.onSaveClick(album)
        }
    }
}