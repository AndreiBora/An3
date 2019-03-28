package andrei.csubb.com.dentistryapp

import andrei.csubb.com.dentistryapp.Utils.DURATIONS
import andrei.csubb.com.dentistryapp.Utils.convertDateToStringFormat
import andrei.csubb.com.dentistryapp.model.Appointment
import andrei.csubb.com.dentistryapp.repository.AppointmentContract
import andrei.csubb.com.dentistryapp.repository.AppointmentDbManager
import andrei.csubb.com.dentistryapp.service.AppointmentService
import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.activity_appointment_add.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*

class AppointmentAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_add)
        var calendar:Calendar = Calendar.getInstance()

        dateImgV.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { view, yearSelected, monthSelected, dayOfMonth ->
                        displaySelectedDate.text = "$yearSelected/$monthSelected/$dayOfMonth"
                        calendar.set(Calendar.YEAR,yearSelected)
                        calendar.set(Calendar.MONTH,monthSelected)
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
                    },
                    year,
                    month,
                    day
            ).show()
        }
        timeImgV.setOnClickListener{
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay)
                calendar.set(Calendar.MINUTE,minute)
                displaySelectedTime.text = SimpleDateFormat("HH:mm").format(calendar.time)
            }
            TimePickerDialog(this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true).show()
        }

        val durationAdapter = ArrayAdapter<Int>(this,
                android.R.layout.simple_list_item_1,
                DURATIONS
                )
        durationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        durationSpinner.adapter = durationAdapter

        btnAdd.setOnClickListener{
            val patientName:String = txtvAddName.text.toString()
            val reason:String = txtAddReason.text.toString()
            val appointmentDate = calendar.time
            val duration:Int = durationSpinner.selectedItem.toString().toInt()
            calendar.add(Calendar.MINUTE,duration)
            val endDateAppointment = calendar.time

            val intent = Intent()
            intent.putExtra(EXTRA_START_DATE,convertDateToStringFormat(appointmentDate))
            intent.putExtra(EXTRA_END_DATE, convertDateToStringFormat(endDateAppointment))
            intent.putExtra(EXTRA_REASON,reason)
            intent.putExtra(EXTRA_PATIENT_NAME,patientName)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }
    }

    companion object {
        const val EXTRA_ID = "andrei.csubb.com.dentistryapp.EXTRA_ID"
        const val EXTRA_START_DATE = "andrei.csubb.com.dentistryapp.EXTRA_START_DATE"
        const val EXTRA_END_DATE = "andrei.csubb.com.dentistryapp.EXTRA_END_DATE"
        const val EXTRA_REASON = "andrei.csubb.com.dentistryapp.EXTRA_REASON"
        const val EXTRA_PATIENT_NAME = "andrei.csubb.com.dentistryapp.EXTRA_PATIENT_NAME"

    }

}
