package com.onerainbow.module.seek.Fragment


import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.onerainbow.module.seek.adapter.SingleAdapter
import com.onerainbow.module.seek.adapter.UserDataAdapter
import com.onerainbow.module.seek.databinding.FragmentUsersBinding
import com.onerainbow.module.seek.viewmodel.FinishSeekViewModel
import com.onerainbow.lib.base.BaseFragment

/**
 * description ： TODO:类的作用
 * author : summer_palace2
 * email : 2992203079qq.com
 * date : 2025/7/17 11:17
 */
class UsersFragment :BaseFragment<FragmentUsersBinding>() {
    private val finishSeekViewmodel: FinishSeekViewModel by lazy {
        ViewModelProvider(requireActivity())[FinishSeekViewModel::class.java]
    }
    private lateinit var userDataAdapter: UserDataAdapter
    private var keyword: String? = null
    override fun getViewBinding(): FragmentUsersBinding =FragmentUsersBinding.inflate(layoutInflater)

    override fun initEvent() {
        userDataAdapter =UserDataAdapter()
        binding.userRecycleview.layoutManager=LinearLayoutManager(context)
        binding.userRecycleview.adapter=userDataAdapter
        keyword?.let {
            finishSeekViewmodel.getUser(it)
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        keyword = arguments?.getString("keyword") ?: ""



    }
    override fun initViewModel() {
        finishSeekViewmodel.userDataLiveData.observe(viewLifecycleOwner){
            result ->
            userDataAdapter.submitList(result.result.artists)
        }

    }
    companion object {
        fun newInstance(keyword: String?): UsersFragment {
            val fragment = UsersFragment()
            fragment.arguments = Bundle().apply {
                putString("keyword", keyword)
            }
            return fragment
        }
    }
}