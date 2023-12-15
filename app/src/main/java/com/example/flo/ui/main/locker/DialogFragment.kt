package com.example.flo.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.example.flo.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogFragment(private val lockerFragment: LockerFragment): BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog, container, false)
        val delete: LinearLayout = view.findViewById(R.id.dialog_delete_ll)
        delete.setOnClickListener {
            lockerFragment.binding.lockerSelectAllOnTv.visibility = View.GONE
            lockerFragment.binding.lockerSelectOnIv.visibility = View.GONE
            lockerFragment.binding.lockerSelectOffIv.visibility = View.VISIBLE
            lockerFragment.binding.lockerSelectAllOffTv.visibility = View.VISIBLE
            dismiss()
        }
        return view

    }
}