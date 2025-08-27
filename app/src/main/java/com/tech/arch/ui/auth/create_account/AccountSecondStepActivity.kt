package com.tech.arch.ui.auth.create_account

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.tech.arch.BR
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.model.AccountType
import com.tech.arch.data.model.Certification
import com.tech.arch.databinding.ActivityAccountSecondStepBinding
import com.tech.arch.databinding.ItemLayoutAccountTypeBinding
import com.tech.arch.databinding.ItemLayoutCertificationDataBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import com.tech.arch.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSecondStepActivity : BaseActivity<ActivityAccountSecondStepBinding>() {
    private val viewModel: CreateAccountActivityVm by viewModels()

    private lateinit var certificateAdapter: SimpleRecyclerViewAdapter<Certification, ItemLayoutCertificationDataBinding>
    private var selectedCertsForApi: List<String> = emptyList()
    private var selectedCertsForUi: String = ""
    private var serviceOffered :String = ""
    var isNewMemberAllowed: Boolean = false

    private lateinit var mapAdapter: SimpleRecyclerViewAdapter<AccountType, ItemLayoutAccountTypeBinding>
    private var interactionList = ArrayList<AccountType>()

//    private lateinit var groupAdapter: SimpleRecyclerViewAdapter<AccountType, ItemLayoutAccountTypeBinding>
//    private var groupTypeList  = ArrayList<AccountType>()


    private lateinit var businessTypeAdapter: SimpleRecyclerViewAdapter<AccountType, ItemLayoutAccountTypeBinding>
    private var businessList = ArrayList<AccountType>()


    private var certificateList = ArrayList<Certification>()
    override fun getLayoutResource(): Int {
        return R.layout.activity_account_second_step
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        ImageUtils.getStatusBarColor(this)

        initView()
        initOnClick()

        binding.radioNewMember.setOnCheckedChangeListener { _, checkedId ->
            isNewMemberAllowed = (checkedId == R.id.rbYes)
        }
    }

    private fun initView() {
        val accountType = Constants.accountType
        when (accountType) {
            "Individual User" -> {
                binding.consIndividual.visibility = View.VISIBLE
                binding.consBusiness.visibility = View.GONE
                binding.consGroup.visibility = View.GONE
            }

            "Group" -> {
                binding.consIndividual.visibility = View.GONE
                binding.consBusiness.visibility = View.GONE
                binding.consGroup.visibility = View.VISIBLE
            }

            "Business" -> {
                binding.consIndividual.visibility = View.GONE
                binding.consBusiness.visibility = View.VISIBLE
                binding.consGroup.visibility = View.GONE
            }
        }

        getCertificateList()
        getMapList()
        getGroupTypeList()
        getBusinessTypeList()
        initAdapter()

        binding.cbRentals.setOnCheckedChangeListener { _, _ -> updateServices() }
        binding.cbLessons.setOnCheckedChangeListener { _, _ -> updateServices() }
        binding.cbTours.setOnCheckedChangeListener { _, _ -> updateServices() }
    }


    private fun updateServices() {
        val selected = mutableListOf<String>()

        if (binding.cbRentals.isChecked) selected.add("Rentals")
        if (binding.cbLessons.isChecked) selected.add("Lessons")
        if (binding.cbTours.isChecked) selected.add("Tours")

        serviceOffered = selected.joinToString(", ")
    }

    private fun getBusinessTypeList() {
        businessList.add(AccountType("Dive Shop"))
        businessList.add(AccountType("School"))
        businessList.add(AccountType("Tour Operator"))
    }

    private fun getGroupTypeList() {
//        groupTypeList.add(AccountType("Club"))
//        groupTypeList.add(AccountType("Nonprofit"))
//        groupTypeList.add(AccountType("Research"))
    }


    private fun getMapList() {
        interactionList.add(AccountType("creating maps"))
        interactionList.add(AccountType("using already created maps"))
        interactionList.add(AccountType("both"))
    }


    private fun initAdapter() {
        certificateAdapter = SimpleRecyclerViewAdapter(
            R.layout.item_layout_certification_data,
            BR.bean
        ) { v, m, pos ->
            when (v.id) {
                R.id.consMain -> {
                    m.isSelected = !m.isSelected
                    certificateAdapter.notifyItemChanged(pos)

                    // Get selected items as List<String> for API
                    selectedCertsForApi = certificateList
                        .filter { it.isSelected }
                        .map { it.certificate }

                    // Get selected items as comma string for UI
                    selectedCertsForUi = selectedCertsForApi.joinToString(", ")

                    // Show in UI
                    binding.tvCertification.setText(selectedCertsForUi)

                    // Save for API

                }
            }

        }
        binding.rvCertification.adapter = certificateAdapter
        certificateAdapter.list = certificateList
        certificateAdapter.notifyDataSetChanged()


        mapAdapter =
            SimpleRecyclerViewAdapter(R.layout.item_layout_account_type, BR.bean) { v, m, pos ->
                when (v.id) {
                    R.id.consMain -> {
                        binding.tvMapInteraction.setText(m.account)
                        binding.rvMapInteraction.visibility = View.GONE
                    }
                }
            }
        binding.rvMapInteraction.adapter = mapAdapter
        mapAdapter.list = interactionList
        mapAdapter.notifyDataSetChanged()


        businessTypeAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_account_type,  BR.bean){v,m,pos ->
            when(v.id){
                R.id.consMain ->{
                    binding.tvBusinessType.setText(m.account)
                    binding.rvBusinessType.visibility = View.GONE
                }
            }
        }
        binding.rvBusinessType.adapter = businessTypeAdapter
        businessTypeAdapter.list = businessList
        businessTypeAdapter.notifyDataSetChanged()


    }

    private fun getCertificateList() {
        certificateList.addAll(
            listOf(
                Certification("Open Water Diver"),
                Certification("Advanced Open Water Diver"),
                Certification("Rescue Diver"),
                Certification("Enriched Air (Nitrox)"),
                Certification("Underwater Photographer"),
                Certification("Dry Suit"),
                Certification("Deep Diver"),
                Certification("Underwater Navigator"),
                Certification("Peak Performance Buoyancy"),
                Certification("Wreck Diver"),
                Certification("Night Diver"),
                Certification("Search and Recovery"),
                Certification("Equipment Specialist"),
                Certification("Diver Propulsion Vehicle"),
                Certification("Drift Diver"),
                Certification("Boat Diver"),
                Certification("Divemaster"),
                Certification("Emergency Oxygen Provider"),
                Certification("Emergency First Response - CPR & AED"),
                Certification("Dive Against Debris"),
                Certification("Emergency First Response - Care for Children"),
                Certification("Sidemount Rec Diver"),
                Certification("Underwater Naturalist"),
                Certification("Tec CCR Instructor"),
                Certification("Adaptive Techniques"),
                Certification("Advanced Freediver Instructor"),
                Certification("Freediver Instructor"),
                Certification("Fish Identification"),
                Certification("Advanced Public Safety Diver"),
                Certification("Full Face Mask Diver"),
                Certification("Ice Diver"),
                Certification("Public Safety Diver"),
                Certification("Course Director"),
                Certification("AWARE Shark Conservation"),
                Certification("Delayed Surface Marker Buoy (DSMB) Diver"),
                Certification("Rebreather Diver"),
                Certification("Assistant Instructor"),
                Certification("Advanced Rebreather Diver"),
                Certification("Advanced Freediver"),
                Certification("Coral Reef Conservation"),
                Certification("Basic Freediver"),
                Certification("Cavern Diver"),
                Certification("Emergency First Response Instructor")
            )
        )
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this, Observer {
            when (it?.id) {
                R.id.tvNextBtn -> {
                    when (RegistrationDataHolder.accountType) {
                        "Individual User" -> {
                            val diveHistory = binding.tvDiveHistory.text.toString().trim()
                            val areaInterested = binding.tvAreaInterested.text.toString().trim()
                            val mapInteraction = binding.tvMapInteraction.text.toString().trim()
                            val isTermsChecked = binding.tvCheckBox.isChecked
                            when {
                                areaInterested.isEmpty() -> {
                                    showToast("Please enter area you interested in")
                                }

                                mapInteraction.isEmpty() -> {
                                    showToast("Please select map interaction")
                                }

                                !isTermsChecked -> showToast("Please agree to the terms and privacy policy")

                                else -> {
                                    RegistrationDataHolder.interestedArea = areaInterested
                                    RegistrationDataHolder.mapInteraction = mapInteraction
                                    RegistrationDataHolder.termsAccepted = isTermsChecked
                                    RegistrationDataHolder.diveHistory = diveHistory
                                    RegistrationDataHolder.certifications = selectedCertsForUi


                                    val intent = Intent(this, AccountLastStepActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }

                        "Group" -> {
                            val groupName = binding.tvGroupName.text.toString().trim()
                            val contactEmail = binding.tvContactEmailGroup.text.toString().trim()
                            val typeOfGroup = binding.tvTypeOfGroup.text.toString().trim()
                            val numberOfMembers = binding.tvNumberOfMember.text.toString().trim()
                            val hostedEvents = binding.tvHostedEventsGroup.text.toString().trim()
                            val groupMembers = binding.tvGroupMembers.text.toString().trim()
                            val website = binding.tvWebsiteGroup.text.toString().trim()
                            val socialMedia = binding.tvSocialMedia.text.toString()
                            val isTermsChecked = binding.tvCheckBoxGroup.isChecked

                            when {
                                groupName.isEmpty() -> {
                                    showToast("Please enter group name")
                                }

                                typeOfGroup.isEmpty() -> {
                                    showToast("Please enter type of group")
                                }

                                !isTermsChecked -> showToast("Please agree to the terms and privacy policy")


                                else -> {
                                    RegistrationDataHolder.groupName = groupName
                                    RegistrationDataHolder.groupContactEmail = contactEmail
                                    RegistrationDataHolder.groupType = typeOfGroup
                                    RegistrationDataHolder.groupHostedEvent = hostedEvents
                                    RegistrationDataHolder.numberOfMembers = numberOfMembers.toInt()
                                    RegistrationDataHolder.groupWebsite = website
                                    RegistrationDataHolder.openToNewMembers = isNewMemberAllowed
                                    RegistrationDataHolder.termsAccepted = isTermsChecked


                                    val intent = Intent(this, AccountLastStepActivity::class.java)
                                    startActivity(intent)
                                }
                            }

                        }

                        "Business" -> {
                            val businessName = binding.tvBusinessName.text.toString().trim()
                            val businessType = binding.tvBusinessType.text.toString().trim()
                            val businessPhone = binding.tvPhoneNumber.text.toString().trim()
                            val publicEmail = binding.tvPublicContactEmail.text.toString().trim()
                            val website = binding.tvWebsite.text.toString().trim()
                            val socialMedia = binding.tvSocialMedia.text.toString().trim()
                            val address = binding.tvAddress.text.toString().trim()
                            val isTermsChecked = binding.tvCheckBoxBusiness.isChecked


                            when {
                                businessName.isEmpty() -> {
                                    showToast("Please enter business name")
                                }

                                businessType.isEmpty() -> {
                                    showToast("Please select business type")
                                }

                                businessPhone.isEmpty() -> {
                                    showToast("Please enter  business phone no.")
                                }

                                publicEmail.isEmpty() -> {
                                    showToast("Please enter public contact email")
                                }

                                website.isEmpty() -> {
                                    showToast("Please enter website")
                                }

                                socialMedia.isEmpty() -> {
                                    showToast("Please enter social media link")
                                }

                                address.isEmpty() -> {
                                    showToast("Please enter address")
                                }

                                !isTermsChecked -> showToast("Please agree to the terms and privacy policy")

                                else -> {
                                    RegistrationDataHolder.businessName = businessName
                                    RegistrationDataHolder.businessType = businessType
                                    RegistrationDataHolder.businessPhone = businessPhone
                                    RegistrationDataHolder.publicEmail = publicEmail
                                    RegistrationDataHolder.website = website
                                    RegistrationDataHolder.businessSocialMedia = socialMedia
                                    RegistrationDataHolder.servicesOffered = serviceOffered
                                    RegistrationDataHolder.hostedEvents = binding.tvHostedEvents.text.toString()
                                    RegistrationDataHolder.businessRole = binding.tvRoleOfBusiness.text.toString()
                                    RegistrationDataHolder.termsAccepted = isTermsChecked
                                    RegistrationDataHolder.hoursOfOperation = binding.tvHourOfOperation.text.toString()
                                    RegistrationDataHolder.address = address

                                    val intent = Intent(this, AccountLastStepActivity::class.java)
                                    startActivity(intent)

                                }
                            }


                        }
                    }

                    Log.i("DATA", RegistrationDataHolder.logData())
//                    val intent = Intent(this , AccountLastStepActivity::class.java)
//                    startActivity(intent)
                }

                R.id.tvMapInteraction -> {
                    binding.rvMapInteraction.visibility =
                        if (binding.rvMapInteraction.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }

                R.id.consLayout -> {
                    binding.rvCertification.visibility = View.GONE
                    binding.rvMapInteraction.visibility = View.GONE
                    binding.rvGroupType.visibility = View.GONE
                }

                R.id.tvCertification -> {
                    binding.rvCertification.visibility =
                        if (binding.rvCertification.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }

                R.id.tvBusinessType -> {
                    binding.rvBusinessType.visibility =
                        if (binding.rvBusinessType.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }

            }
        })
    }
}