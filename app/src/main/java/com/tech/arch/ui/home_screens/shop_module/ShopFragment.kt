package com.tech.arch.ui.home_screens.shop_module

import android.view.View
import androidx.fragment.app.viewModels
import com.tech.arch.BR
import com.tech.arch.R
import com.tech.arch.data.model.SubscriptionData
import com.tech.arch.databinding.FragmentShopBinding
import com.tech.arch.databinding.ItemLayoutSubscriptionBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShopFragment : BaseFragment<FragmentShopBinding>() {

    private val viewModel : ShopFragmentVm  by viewModels()
    private lateinit var subscriptionAdapter : SimpleRecyclerViewAdapter<SubscriptionData, ItemLayoutSubscriptionBinding>
    private var subscriptionList = arrayListOf<SubscriptionData>()

    override fun onCreateView(view: View) {
        initOnClick()
        getList()
        initAdapter()
    }

    private fun getList() {
        subscriptionList.add(
            SubscriptionData("Monthly \n"+ "Plan","$3.99","/month","Pay as you go - cancel anytime\n" +
                    "$3.99 per month ($47.88 per year)")
        )
        subscriptionList.add(
            SubscriptionData("Yearly \n"+ "Plan","$35.88","/year","Pay once and save.\n" +
                    "Only \$2.99 per month billed annually.\n" +
                    "(35.88 per year)")
        )
    }

    private fun initAdapter() {
        subscriptionAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_subscription,BR.bean){v,m,pos ->

        }
        binding.rvSubscriptionPlans.adapter = subscriptionAdapter
        subscriptionAdapter.list = subscriptionList
        subscriptionAdapter.notifyDataSetChanged()
    }

    private fun initOnClick() {

    }

    override fun getLayoutResource(): Int {
       return  R.layout.fragment_shop
    }

    override fun getViewModel(): BaseViewModel {
         return viewModel
    }

}