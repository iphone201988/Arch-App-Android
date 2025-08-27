package com.tech.arch.ui.home_screens.bookmark_module

import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import com.tech.arch.BR
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.model.BookmarkData
import com.tech.arch.data.model.GetBookMarkApiResponse
import com.tech.arch.databinding.ActivityBookmarkBinding
import com.tech.arch.databinding.ItemLayoutBookmarksBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import com.tech.arch.ui.home_screens.event_fragment.EventsPagerAdapter
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import com.tech.arch.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarkActivity : BaseActivity<ActivityBookmarkBinding>() {

//    private lateinit var  privateMapAdapter : SimpleRecyclerViewAdapter<GetBookMarkApiResponse.Bookmark,ItemLayoutBookmarksBinding>
//    private lateinit var  publicMapAdapter : SimpleRecyclerViewAdapter<GetBookMarkApiResponse.Bookmark,ItemLayoutBookmarksBinding>
    private var publicMapList = arrayListOf<BookmarkData>()

    private lateinit var bookMarkAdapter : BookMarkAdapter

    private val viewModel : BookmarkActivityVm by viewModels()

    override fun getLayoutResource(): Int {
        return R.layout.activity_bookmark
    }

    override fun getViewModel(): BaseViewModel {
         return viewModel
    }

    override fun onCreateView() {
        ImageUtils.getStatusBarColor(this)
        initOnClick()
      //  viewModel.getBookMarks(Constants.GET_BOOKMARK)
        initAdapter()
        initObserver()

    }

    private fun initObserver() {
        viewModel.obrCommon.observe(this , Observer {
            when(it?.status){
                Status.LOADING ->{
                    progressDialogAvl.isLoading(true)
                }
                Status.SUCCESS -> {
                    hideLoading()
                    when (it.message) {
                        "getBookMarks" -> {
                            val myDataModel: GetBookMarkApiResponse? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null) {
                                val currentUserId = sharedPrefManager.getCurrentUser()?.userExists?._id

                                val privateList = mutableListOf<GetBookMarkApiResponse.Bookmark>()
                                val publicList = mutableListOf<GetBookMarkApiResponse.Bookmark>()

                                myDataModel.bookmark?.forEach { bookmark ->
                                    if (bookmark?.userId == currentUserId) {
                                        if (bookmark != null) {
                                            privateList.add(bookmark)
                                        }
                                    } else {
                                        if (bookmark != null) {
                                            publicList.add(bookmark)
                                        }
                                    }
                                }

                                Log.i("private list", "initObserver: $privateList")
                                Log.i("public list", "initObserver:$publicList ")

//                                privateMapAdapter.list = privateList
//                                publicMapAdapter.list = publicList

                            }
                        }

                    }
                }
                Status.ERROR ->{
                    hideLoading()
                    showToast(it.message.toString())
                }
                else ->{
                }
            }
        })
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.ivBack ->{
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }



    private fun initAdapter() {
//        privateMapAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_bookmarks,BR.bean){v,m,pos ->
//
//        }
//        binding.rvPrivateMap.adapter = privateMapAdapter
//        privateMapAdapter.notifyDataSetChanged()
//
//
//        publicMapAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_bookmarks,BR.bean){v,m,pos ->
//
//        }
//        binding.rvPublicMaps.adapter = publicMapAdapter
//        publicMapAdapter.notifyDataSetChanged()



        bookMarkAdapter = BookMarkAdapter(this)
        binding.viewPager.adapter = bookMarkAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Saved Public Maps"
                1 -> "Private User Created Maps"
                else -> ""
            }
        }.attach()
    }
}