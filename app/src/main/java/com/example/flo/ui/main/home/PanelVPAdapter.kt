package com.example.flo.ui.main.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PanelVPAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val fragmentlist: ArrayList<Fragment> = ArrayList()

    //데이터를 전달할 때, 몇 개의 데이터를 전달할 것인지 써주는 함수
    override fun getItemCount(): Int = fragmentlist.size

    //fragment를 생성하는 함수
    override fun createFragment(position: Int): Fragment = fragmentlist[position]

    fun addFragment(fragment: Fragment) {
        fragmentlist.add(fragment)
        notifyItemInserted(fragmentlist.size-1) //list 안에 새로운 값이 추가가 되었을 때 viewpager에 값이 추가가 된 것을 알려줌
    }

}