package com.test.medicinemates.addmedicine

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.test.medicinemates.R
import com.test.medicinemates.medicine.ERecurringType


class EnumAdapter(
    private val mContext: Context,
    private var mLayoutResourceId: Int,
    private var mItems: Array<ERecurringType>,
) : ArrayAdapter<ERecurringType?>(mContext, mLayoutResourceId, mItems) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val enumItem = getItem(position)

        var row = convertView

        if (row == null) {
            val inflater = LayoutInflater.from(mContext)
            row = inflater.inflate(mLayoutResourceId, parent, false)
        }

        val vm = row!!.findViewById(R.id.tv_spinner) as TextView
        vm.text = (enumItem!!.description)

        return row!!
    }
}
