package com.test.medicinemates.medicine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.medicinemates.databinding.ItemMedicineBinding

class MedicineAdapter : RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {

    var data: List<MedicineModel> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    class MedicineViewHolder(val binding: ItemMedicineBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMedicineBinding.inflate(inflater, parent, false)

        return MedicineViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val item = data[position]

        with(holder.binding) {

            val medicineText = "${item.intakeTime} - ${item.name}"
            tvMedicineItem.text = medicineText
        }
    }
}