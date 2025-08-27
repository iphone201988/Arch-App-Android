package com.tech.arch.ui.home_screens.event_fragment.events

import android.view.View
import androidx.fragment.app.viewModels
import com.tech.arch.BR
import com.tech.arch.R
import com.tech.arch.data.model.EventData
import com.tech.arch.databinding.FragmentEventDataBinding
import com.tech.arch.databinding.ItemLayoutEventDataBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EventDataFragment :BaseFragment<FragmentEventDataBinding>() {

    private val viewModel : EventDataFragmentVm by viewModels()
    private lateinit var eventAdapter: SimpleRecyclerViewAdapter<EventData,ItemLayoutEventDataBinding>
    private  var eventDataList = arrayListOf<EventData>()

    override fun onCreateView(view: View) {
        getEventData()
        initAdapter()
    }

    private fun getEventData() {
        eventDataList.add(EventData("Name of event","Host Username", "Location","23 Apr,2025"))
        eventDataList.add(EventData("Name of event","Host Username", "Location","23 Apr,2025"))
        eventDataList.add(EventData("Name of event","Host Username", "Location","23 Apr,2025"))
        eventDataList.add(EventData("Name of event","Host Username", "Location","23 Apr,2025"))
        eventDataList.add(EventData("Name of event","Host Username", "Location","23 Apr,2025"))
        eventDataList.add(EventData("Name of event","Host Username", "Location","23 Apr,2025"))
        eventDataList.add(EventData("Name of event","Host Username", "Location","23 Apr,2025"))
    }

    private fun initAdapter() {
        eventAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_event_data, BR.bean){v,m,pos ->

        }
        binding.rvEventData.adapter = eventAdapter
        eventAdapter.list = eventDataList
        eventAdapter.notifyDataSetChanged()
    }

    override fun getLayoutResource(): Int {
       return R.layout.fragment_event_data
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


}