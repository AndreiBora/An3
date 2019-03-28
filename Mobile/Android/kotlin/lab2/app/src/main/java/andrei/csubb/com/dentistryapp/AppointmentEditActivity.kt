package andrei.csubb.com.dentistryapp

import andrei.csubb.com.dentistryapp.Utils.convertDateToStringFormat
import andrei.csubb.com.dentistryapp.repository.AppointmentContract
import andrei.csubb.com.dentistryapp.repository.AppointmentDbManager
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_appointment_add.*
import kotlinx.android.synthetic.main.activity_appointment_edit.*
import org.jetbrains.anko.toast
import java.util.*

class AppointmentEditActivity : AppCompatActivity() {

    val dbManager = AppointmentDbManager(this)
    var appointmentId:Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_edit)
        supportActionBar!!.setTitle("Edit Appointment")

        val bundle: Bundle? = intent.extras
        bundle?.let {
            appointmentId =  it.getString(AppointmentAddActivity.EXTRA_ID)!!.toLongOrNull()
            if (appointmentId != 0L) {
                txtvEditName.setText(it.getString(AppointmentAddActivity.EXTRA_PATIENT_NAME))
                txtEditReason.setText(it.getString(AppointmentAddActivity.EXTRA_REASON))
                txtEditStartDate.setText(it.getString(AppointmentAddActivity.EXTRA_START_DATE))
                txtEditEndDate.setText(it.getString(AppointmentAddActivity.EXTRA_END_DATE))
            }
        }

        btnUpdate.setOnClickListener {
            val patientName:String = txtvEditName.text.toString()
            val reason:String = txtEditReason.text.toString()
            val startDate = txtEditStartDate.text.toString()
            val endDate = txtEditEndDate.text.toString()

            val data = Intent()
            intent.putExtra(AppointmentAddActivity.EXTRA_ID, appointmentId.toString())
            intent.putExtra(AppointmentAddActivity.EXTRA_START_DATE,startDate)
            intent.putExtra(AppointmentAddActivity.EXTRA_END_DATE, endDate)
            intent.putExtra(AppointmentAddActivity.EXTRA_REASON,reason)
            intent.putExtra(AppointmentAddActivity.EXTRA_PATIENT_NAME,patientName)
            setResult(Activity.RESULT_OK,intent)
            finish()
        }


    }
}


