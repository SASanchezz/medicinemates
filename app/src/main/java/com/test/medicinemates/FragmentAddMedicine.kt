package com.test.medicinemates

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.test.medicinemates.R.id.action_fragmentAddMedicine_to_fragmentCalendar
import com.test.medicinemates.R.layout.spinner_layout
import com.test.medicinemates.addmedicine.EnumAdapter
import com.test.medicinemates.connections.Db
import com.test.medicinemates.databinding.FragmentAddMedicineBinding
import com.test.medicinemates.medicine.ERecurringType
import com.test.medicinemates.medicine.MedicineModel
import com.test.medicinemates.notificationmanager.AlarmReceiver
import com.test.medicinemates.utilis.DateUtils
import kotlinx.coroutines.launch
import java.util.Calendar


class FragmentAddMedicine : Fragment() {

    private var _binding: FragmentAddMedicineBinding? = null
    private val binding get() = _binding!!

    private lateinit var daysOfWeekLayout: LinearLayout

    private var initialIntakeDate: String? = null
    private val startIntakeCalendar = Calendar.getInstance()
    private val endIntakeCalendar = Calendar.getInstance()
    private lateinit var daysOfWeekCheckboxes: Array<CheckBox>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddMedicineBinding.inflate(inflater, container, false)

        this.initialIntakeDate = arguments?.getString("intakeDate")
        binding.etStartIntakeDate.setText(this.initialIntakeDate)
        daysOfWeekLayout = LinearLayout(requireContext())
        daysOfWeekLayout.id = View.generateViewId()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bCancelNewMedicine.setOnClickListener {
            findNavController().navigate(action_fragmentAddMedicine_to_fragmentCalendar)
        }

        binding.bSaveNewMedicine.setOnClickListener(this.buttonSaveNewMedicine)
        binding.etStartIntakeDate.setOnClickListener(this.startIntakeDateClickListener)
        binding.etEndIntakeDate.setOnClickListener(this.endIntakeDateClickListener)
        binding.etIntakeTime.setOnClickListener(this.intakeTimeClickListener)

        val adapter = EnumAdapter(
            requireActivity(),
            spinner_layout,
            ERecurringType.values()
        )
        binding.etIntakeRegularity.adapter = adapter

        binding.etIntakeRegularity.onItemSelectedListener = this.itemSelectedListener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setNotificationAlarm() {
        val alarmManager = requireActivity().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val requestCode: Long = Calendar.getInstance().timeInMillis + 1

        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startIntakeCalendar.timeInMillis,
            AlarmManager.INTERVAL_FIFTEEN_MINUTES / 5,
            pendingIntent
        )
    }

    private fun fieldsAreValid(): Boolean {
        if (binding.etMedicineName.text.isEmpty()) {
            binding.etMedicineName.error = "Введіть назву препарату"
            return false
        }
        if (binding.etStartIntakeDate.text.isEmpty()) {
            binding.etStartIntakeDate.error = "Введіть дату початку прийому"
            return false
        }

        if (binding.etIntakeTime.text.isEmpty()) {
            binding.etIntakeTime.error = "Введіть час прийому"
            return false
        }

        return true
    }

    private val buttonSaveNewMedicine = View.OnClickListener {
        if (!fieldsAreValid()) {
            return@OnClickListener
        }
        val endDate = if (binding.etEndIntakeDate.text.isEmpty()) null else binding.etEndIntakeDate.text.toString()
        val weekDays: String? = if (binding.etIntakeRegularity.selectedItem != ERecurringType.ON_WEEK) null else
            daysOfWeekCheckboxes.mapIndexed { index, checkBox ->
                if (checkBox.isChecked) index else null
            }.filterNotNull().joinToString(separator = ",")

        val newMedicine = MedicineModel(
            binding.etMedicineName.text.toString(),
            binding.etStartIntakeDate.text.toString(),
            endDate,
            binding.etIntakeTime.text.toString(),
            binding.etIntakeRegularity.selectedItem as ERecurringType,
            weekDays
        )
        viewLifecycleOwner.lifecycleScope.launch {
            Db.medicineRepository.insertNewMedicine(newMedicine.toMedicineDbEntity())
        }
        setNotificationAlarm()

        findNavController().navigate(action_fragmentAddMedicine_to_fragmentCalendar)
    }

    private val itemSelectedListener: AdapterView.OnItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            val selectedItem = parent?.getItemAtPosition(position) as ERecurringType
            binding.etIntakeRegularity.setSelection(selectedItem.ordinal)


            when (selectedItem) {
                ERecurringType.EVERY_OTHER_DAY -> {
                    binding.addMedicineFragment.removeView(daysOfWeekLayout)
                }
                ERecurringType.ON_WEEK -> {
                    val daysOfWeek = arrayOf("Нд", "Пн", "Вт", "Ср", "Чт", "Пт", "Сб")

                    val constraintSet = ConstraintSet()
                    daysOfWeekLayout.id = View.generateViewId()

                    daysOfWeekCheckboxes = daysOfWeek.map { day ->
                        val checkBox = CheckBox(requireContext())
                        checkBox.text = day
                        checkBox.textSize = 20f
                        daysOfWeekLayout.addView(checkBox)

                        checkBox
                    }.toTypedArray()

                    binding.addMedicineFragment.addView(daysOfWeekLayout)

                    constraintSet.clone(binding.addMedicineFragment);
                    constraintSet.connect(daysOfWeekLayout.id, ConstraintSet.TOP, binding.etIntakeRegularity.id, ConstraintSet.BOTTOM)
                    constraintSet.connect(daysOfWeekLayout.id, ConstraintSet.START, binding.addMedicineFragment.id, ConstraintSet.START)
                    constraintSet.connect(daysOfWeekLayout.id, ConstraintSet.END, binding.addMedicineFragment.id, ConstraintSet.END)
                    constraintSet.applyTo(binding.addMedicineFragment)
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            binding.etIntakeRegularity.setSelection(0)
        }
    }

    private val intakeTimeClickListener: View.OnClickListener = View.OnClickListener {
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                this.startIntakeCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                this.startIntakeCalendar.set(Calendar.MINUTE, minute)
                binding.etIntakeTime.setText(DateUtils.getTimeString(hourOfDay, minute))
            },
            this.startIntakeCalendar.get(Calendar.HOUR_OF_DAY),
            this.startIntakeCalendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private val startIntakeDateClickListener: View.OnClickListener = View.OnClickListener {
        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                this.startIntakeCalendar.set(Calendar.YEAR, year)
                this.startIntakeCalendar.set(Calendar.MONTH, monthOfYear)
                this.startIntakeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.etStartIntakeDate.setText(DateUtils.getDateString(dayOfMonth, monthOfYear+1, year))
            },
            this.startIntakeCalendar.get(Calendar.YEAR),
            this.startIntakeCalendar.get(Calendar.MONTH),
            this.startIntakeCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private val endIntakeDateClickListener: View.OnClickListener = View.OnClickListener {
        DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                this.endIntakeCalendar.set(Calendar.YEAR, year)
                this.endIntakeCalendar.set(Calendar.MONTH, monthOfYear)
                this.endIntakeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                binding.etEndIntakeDate.setText(DateUtils.getDateString(dayOfMonth, monthOfYear+1, year))
            },
            this.endIntakeCalendar.get(Calendar.YEAR),
            this.endIntakeCalendar.get(Calendar.MONTH),
            this.endIntakeCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }


}