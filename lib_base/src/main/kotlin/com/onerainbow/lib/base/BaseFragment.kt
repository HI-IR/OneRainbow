package com.onerainbow.lib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.therouter.TheRouter

/**
 * description ： Fragment的基础类
 * author : HI-IR
 * email : qq2420226433@outlook.com
 * date : 2025/7/13 10:17
 */
abstract class BaseFragment<VB: ViewBinding>: Fragment() {

    protected abstract fun getViewBinding():VB


    //关于binding的操作
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = getViewBinding()
        return _binding!!.root
    }


    //在这个函数里面写有业务逻辑
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TheRouter.inject(this)
        observeData()
        initEvent()

    }

    abstract fun initEvent()

    abstract fun observeData()


    override fun onDestroyView() {
        super.onDestroyView()
        //保证binding变量的有效生命周期是在onCreateView()函数和onDestroyView()函数之间
        _binding = null//置空_binding
    }



}