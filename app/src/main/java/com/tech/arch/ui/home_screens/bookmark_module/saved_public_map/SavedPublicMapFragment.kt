package com.tech.arch.ui.home_screens.bookmark_module.saved_public_map

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tech.arch.BR
import com.tech.arch.R

import com.tech.arch.data.model.BookMarkDataaaa
import com.tech.arch.data.model.GetBookMarkApiResponse
import com.tech.arch.databinding.FragmentSavedPublicMapBinding
import com.tech.arch.databinding.ItemLayoutBookmarlkBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import com.tech.arch.ui.home_screens.bookmark_module.BookMarkAdapter
import com.tech.arch.ui.home_screens.bookmark_module.BookmarkActivityVm
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SavedPublicMapFragment : BaseFragment<FragmentSavedPublicMapBinding>() {

    private val viewModel : BookmarkActivityVm by viewModels()

   private lateinit var savedAdapter: SimpleRecyclerViewAdapter<BookMarkDataaaa,ItemLayoutBookmarlkBinding>

   private var bookmarkList = ArrayList<BookMarkDataaaa>()

    override fun onCreateView(view: View) {

        getBookMarkList()
        initAdaoter()
    }

    private fun initAdaoter() {
        savedAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_bookmarlk,BR.bean){v,m,pos ->

        }
        binding.rvSavedMaps.adapter = savedAdapter
        savedAdapter.list  = bookmarkList
        savedAdapter.notifyDataSetChanged()
    }

    private fun getBookMarkList() {
        bookmarkList.add(BookMarkDataaaa("Dive Site Name","Location","City", "State","12 miles away"," 80 / 100"))
        bookmarkList.add(BookMarkDataaaa("Dive Site Name","Location","City", "State","12 miles away"," 80 / 100"))
        bookmarkList.add(BookMarkDataaaa("Dive Site Name","Location","City", "State","12 miles away"," 80 / 100"))
        bookmarkList.add(BookMarkDataaaa("Dive Site Name","Location","City", "State","12 miles away"," 80 / 100"))
        bookmarkList.add(BookMarkDataaaa("Dive Site Name","Location","City", "State","12 miles away"," 80 / 100"))
        bookmarkList.add(BookMarkDataaaa("Dive Site Name","Location","City", "State","12 miles away"," 80 / 100"))
    }


    override fun getLayoutResource(): Int {
        return R.layout.fragment_saved_public_map
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


}