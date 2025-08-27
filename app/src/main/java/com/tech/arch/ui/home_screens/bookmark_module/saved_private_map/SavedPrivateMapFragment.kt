package com.tech.arch.ui.home_screens.bookmark_module.saved_private_map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tech.arch.R
import com.tech.arch.databinding.FragmentSavedPrivateMapBinding
import com.tech.arch.databinding.FragmentSavedPublicMapBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.home_screens.bookmark_module.BookmarkActivityVm
import dagger.hilt.android.AndroidEntryPoint
import org.checkerframework.checker.units.qual.A


@AndroidEntryPoint
class SavedPrivateMapFragment : BaseFragment<FragmentSavedPrivateMapBinding>() {

    private val viewModel : BookmarkActivityVm by viewModels()


    override fun onCreateView(view: View) {
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_saved_private_map
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

}