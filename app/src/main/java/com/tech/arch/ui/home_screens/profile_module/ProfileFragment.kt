package com.tech.arch.ui.home_screens.profile_module

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tech.arch.BR
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.api.SimpleApiResponse
import com.tech.arch.data.model.BookmarkData
import com.tech.arch.data.model.GetProfileApiResponse
import com.tech.arch.data.model.ProfileModel
import com.tech.arch.databinding.FragmentProfileBinding
import com.tech.arch.databinding.ItemLayoutBookmarksBinding
import com.tech.arch.databinding.ItemLayoutCertificationBinding
import com.tech.arch.databinding.ItemLayoutDeleteAccountBinding
import com.tech.arch.databinding.ItemLayoutLogutPopupBinding
import com.tech.arch.ui.auth.login_module.LoginActivity
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import com.tech.arch.utils.BaseCustomDialog
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() , BaseCustomDialog.Listener{

    private val viewModel : ProfileFragmentVm by viewModels()
 //   private lateinit var profileAdapter: SimpleRecyclerViewAdapter<ProfileModel, ItemLayoutProfileBinding>
 private lateinit var  certificationAdapter : SimpleRecyclerViewAdapter<BookmarkData, ItemLayoutCertificationBinding>
    private lateinit var  diveAdapter : SimpleRecyclerViewAdapter<BookmarkData, ItemLayoutCertificationBinding>
    private lateinit var logoutPopup: BaseCustomDialog<ItemLayoutLogutPopupBinding>
    private lateinit var deletePopup: BaseCustomDialog<ItemLayoutDeleteAccountBinding>
    private var publicMapList = arrayListOf<BookmarkData>()
    private var profileList = ArrayList<ProfileModel>()
    override fun onCreateView(view: View) {
        initOnClick()
        getList()
        getListData()
        initPopup()
        viewModel.getProfile(Constants.GET_PROFILE)
        inirAdapter()
    //    initAdapter()
        setObserver()
    }
    private fun getListData() {
        publicMapList.add(BookmarkData("Carlos, Ward"))
        publicMapList.add(BookmarkData("Rachel, Williamson"))
        publicMapList.add(BookmarkData("Johny, Kelly"))
    }
    private fun inirAdapter() {
        certificationAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_certification,BR.bean){v,m,pos ->

        }
        binding.rvCertification.adapter = certificationAdapter
        certificationAdapter.list = publicMapList
        certificationAdapter.notifyDataSetChanged()


        diveAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_certification,BR.bean){v,m,pos ->

        }
        binding.rvDiveHistory.adapter = diveAdapter
        diveAdapter.list = publicMapList
        diveAdapter.notifyDataSetChanged()
    }

    private fun setObserver() {
        viewModel.obrCommom.observe(viewLifecycleOwner, Observer {
            when(it?.status){
                Status.LOADING ->{
                    progressDialogAvl.isLoading(true)
                }
                Status.SUCCESS ->{
                    hideLoading()
                    when(it.message){
                        "logout" ->{
                            val myDataModel : SimpleApiResponse ? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                sharedPrefManager.clear()
                                val intent = Intent(requireContext(), LoginActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        "deleteAccount" ->{
                            val myDataModel : SimpleApiResponse ? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                sharedPrefManager.clear()
                                showToast("Account delete successfully")
                                val intent = Intent(requireContext(), LoginActivity::class.java)
                                startActivity(intent)
                            }
                        }
                        "getProfile" ->{
                            val myDataModel : GetProfileApiResponse? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if(myDataModel.userExists != null){
                                    Log.i("data", "setObserver: ${myDataModel.userExists}")
                                    binding.bean = myDataModel.userExists

                                }
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

    private fun initPopup() {
        logoutPopup = BaseCustomDialog(requireContext(), R.layout.item_layout_logut_popup, this)
        deletePopup  = BaseCustomDialog(requireContext(), R.layout.item_layout_delete_account,this)


    }

  //  private fun initAdapter() {
//        profileAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_profile,BR.bean){v,m,pos ->
//            when(m.title){
//                "Edit your profile" ->{
//                    val intent = Intent(requireContext() , EditYourProfile :: class.java)
//                    startActivity(intent)
//                }
//                "Change password" ->{
//                    val intent  = Intent(requireContext() , ChangePasswordActivity::class.java)
//                    startActivity(intent)
//                }
//                "Logout" ->{
//                    logoutPopup.show()
//                }
//                "Delete account" ->{
//                    deletePopup.show()
//                }
//            }
//
//        }
//        binding.rvProfile.adapter = profileAdapter
//        profileAdapter.list = profileList
//        profileAdapter.notifyDataSetChanged()
//    }

    private fun getList() {
        profileList.add(ProfileModel(R.drawable.iv_person,"Edit your profile"))
        profileList.add(ProfileModel(R.drawable.iv_password,"Change password"))
        profileList.add(ProfileModel(R.drawable.iv_privacy,"Privacy policy"))
        profileList.add(ProfileModel(R.drawable.iv_privacy,"Terms & conditions"))
        profileList.add(ProfileModel(R.drawable.iv_delete,"Delete account"))
        profileList.add(ProfileModel(R.drawable.iv_logout,"Logout"))
    }

    private fun initOnClick() {

    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_profile
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onViewClick(view: View?) {

        when(view?.id){
            R.id.tvConfirm ->{
                viewModel.logout(Constants.LOGOUT)
            }
            R.id.tvCancel->{
                logoutPopup.dismiss()
            }
            R.id.tvDeleteConfirm ->{
                viewModel.deleteAccount(Constants.DELETE_ACCOUNT)
            }
            R.id.tvDeleteCancel ->{
                deletePopup.dismiss()
            }
        }
    }

    override fun onResume() {
        viewModel.getProfile(Constants.GET_PROFILE)
        super.onResume()
    }
}