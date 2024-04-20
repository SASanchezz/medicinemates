package com.test.medicinemates

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.medicinemates.databinding.FragmentCalendarBinding
import com.test.medicinemates.medicine.MedicineAdapter
import com.test.medicinemates.medicine.MedicineService
import com.test.medicinemates.utilis.DateUtils
import kotlinx.coroutines.launch


class FragmentCalendar : Fragment() {
    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private val medicineService = MedicineService()
    private lateinit var adapter: MedicineAdapter

    private var chosenDate: String = DateUtils.getCurrentDate()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvCurrentDate.text = DateUtils.getCurrentDate()

        binding.cvCalendar
            .setOnDateChangeListener { _, year, month, dayOfMonth ->
                onDateChanged(year, month, dayOfMonth)
            }

        binding.bAdd.setOnClickListener {
            buttonAddClick()
        }

        binding.bChatBot.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentCalendar_to_fragmentChat)
        }

        val manager = LinearLayoutManager(activity)
        adapter = MedicineAdapter()
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.data = medicineService.getDayMedicine(chosenDate)
        }

        binding.rvCurrentDay.layoutManager = manager
        binding.rvCurrentDay.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onDateChanged(year: Int, month: Int, dayOfMonth: Int) {
        this.chosenDate = DateUtils.getDateString(dayOfMonth, month + 1, year)
        binding.tvCurrentDate.text = this.chosenDate

        viewLifecycleOwner.lifecycleScope.launch {
            adapter.data = medicineService.getDayMedicine(chosenDate)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun buttonAddClick() {
        val bundle = Bundle()
        bundle.putString("intakeDate", binding.tvCurrentDate.text as String)
        findNavController().navigate(R.id.action_fragmentCalendar_to_fragmentAddMedicine, bundle)
    }

}