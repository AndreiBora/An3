package andrei.csubb.com.dentistryapp

import andrei.csubb.com.dentistryapp.Utils.convertDateToStringFormat
import andrei.csubb.com.dentistryapp.Utils.convertStringToDate
import andrei.csubb.com.dentistryapp.Utils.logd
import andrei.csubb.com.dentistryapp.model.Appointment
import andrei.csubb.com.dentistryapp.repository.AppointmentContract
import andrei.csubb.com.dentistryapp.repository.AppointmentDbManager
import andrei.csubb.com.dentistryapp.service.AppointmentService
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item.view.*
import org.jetbrains.anko.toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private var dbManager = AppointmentDbManager(this)
    private var listAppointments = ArrayList<Appointment>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        appointmentListView.setHasFixedSize(true)
        appointmentListView.layoutManager = LinearLayoutManager(this)

        loadData()

        btnAddAppointment.setOnClickListener {
            val intent = Intent(MainActivity@ this, AppointmentAddActivity::class.java)
            startActivityForResult(intent, ADD_APPOINTMENT_REQUEST)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        dbManager.close()
        super.onDestroy()
    }

    fun loadData() {
        val cursor = dbManager.getAll()
        listAppointments.clear()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID))
                val startDateStr = cursor.getString(cursor.getColumnIndex(AppointmentContract.AppointmentEntry.COLUMN_START_DATE))
                val endDateStr = cursor.getString(cursor.getColumnIndex(AppointmentContract.AppointmentEntry.COLUMN_END_DATE))
                val reason = cursor.getString(cursor.getColumnIndex(AppointmentContract.AppointmentEntry.REASON))
                val patientName = cursor.getString(cursor.getColumnIndex(AppointmentContract.AppointmentEntry.PATIENT_NAME))
                //parsing the date
                //only for api 26
                //val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.getDefault())
                var startDate = convertStringToDate(startDateStr)
                var endDate = convertStringToDate(endDateStr)
                listAppointments.add(Appointment(id, startDate, endDate, reason, patientName))
            } while (cursor.moveToNext())
        }
        logd("Loading data:${listAppointments.toString()}")
        appointmentListView.adapter = AppointmentAdapter(listAppointments)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_APPOINTMENT_REQUEST && resultCode == Activity.RESULT_OK) {
            val startDate = data!!.getStringExtra(AppointmentAddActivity.EXTRA_START_DATE)
            val endDate = data!!.getStringExtra(AppointmentAddActivity.EXTRA_END_DATE)
            val reason = data!!.getStringExtra(AppointmentAddActivity.EXTRA_REASON)
            val patientName = data!!.getStringExtra(AppointmentAddActivity.EXTRA_PATIENT_NAME)

            val newApp = Appointment(null,
                    convertStringToDate(startDate),
                    convertStringToDate(endDate),
                    reason,
                    patientName
            )

            val values = ContentValues().apply {
                put(AppointmentContract.AppointmentEntry.COLUMN_START_DATE, startDate)
                put(AppointmentContract.AppointmentEntry.COLUMN_END_DATE, endDate)
                put(AppointmentContract.AppointmentEntry.REASON, reason)
                put(AppointmentContract.AppointmentEntry.PATIENT_NAME, patientName)
            }

            AppointmentService.addAppointment(newApp,dbManager,values,listAppointments,appointmentListView)


        } else if (requestCode == EDIT_APPOINTMENT_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data!!.getStringExtra(AppointmentAddActivity.EXTRA_ID).toLong()
            val startDate = data!!.getStringExtra(AppointmentAddActivity.EXTRA_START_DATE)
            val endDate = data!!.getStringExtra(AppointmentAddActivity.EXTRA_END_DATE)
            val reason = data!!.getStringExtra(AppointmentAddActivity.EXTRA_REASON)
            val patientName = data!!.getStringExtra(AppointmentAddActivity.EXTRA_PATIENT_NAME)

            val values = ContentValues().apply {
                put(BaseColumns._ID,id.toString())
                put(AppointmentContract.AppointmentEntry.COLUMN_START_DATE, startDate)
                put(AppointmentContract.AppointmentEntry.COLUMN_END_DATE, endDate)
                put(AppointmentContract.AppointmentEntry.REASON, reason)
                put(AppointmentContract.AppointmentEntry.PATIENT_NAME, patientName)
            }

            val newApp = Appointment(id,
                    convertStringToDate(startDate),
                    convertStringToDate(endDate),
                    reason,
                    patientName
            )
            AppointmentService.editAppointment(newApp,dbManager,values,listAppointments,appointmentListView)


        } else {
            toast("Appointment was not created")
        }
    }


    inner class AppointmentAdapter(
            private val appointments: List<Appointment>
    ) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            Log.d("size", appointments.size.toString())
            return appointments.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val appointment = appointments[position]
            Log.d("Adapter", "appointment" + appointment.toString())
            val startDateStr: String = convertDateToStringFormat(appointment.startDate)
            val startTime: String = SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(appointment.startDate)
            val endTime: String = SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(appointment.endDate)
            holder.appStartView.text = startDateStr
            holder.appEndView.text = startTime
            holder.endTimeView.text = endTime
            holder.reasonView.text = appointment.reason
            holder.patientNameView.text = appointment.patientName
            holder.editAppView.setOnClickListener {
                updateAppointment(appointment, it)
            }
            holder.deleteAppView.setOnClickListener {
                //Alert dialog
                val builder = AlertDialog.Builder(it.context)
                builder.setTitle("Delete appointment")
                builder.setMessage("Are you sure you want to delete the message?")
                builder.setPositiveButton("Yes") { _, _ ->
                    AppointmentService.deleteAppointment(appointment.id!!,dbManager,listAppointments,appointmentListView)
                    val selectionArgs = arrayOf(appointment.id.toString())
                    val selection = "_ID = ?"
                    val nrRowsDeleted = dbManager.delete(selection, selectionArgs)
                    listAppointments.remove(appointment)

                    if (nrRowsDeleted > 0) {
                        appointmentListView.adapter!!.notifyDataSetChanged();
                        toast("Appointment successfully deleted")
                    } else {
                        toast("Failed to deleted the appointment")
                    }
                }
                builder.setNegativeButton("No") { _, _ ->
                    toast("The appointment was not deleted")
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()
            }

        }


        private fun updateAppointment(appointment: Appointment, view: View) {
            val intent = Intent(this@MainActivity, AppointmentEditActivity::class.java)
                    .apply {
                        putExtra(AppointmentAddActivity.EXTRA_ID, appointment.id.toString())
                        putExtra(AppointmentAddActivity.EXTRA_START_DATE, convertDateToStringFormat(appointment.startDate))
                        putExtra(AppointmentAddActivity.EXTRA_END_DATE, convertDateToStringFormat(appointment.endDate))
                        putExtra(AppointmentAddActivity.EXTRA_REASON, appointment.reason)
                        putExtra(AppointmentAddActivity.EXTRA_PATIENT_NAME, appointment.patientName)
                    }
            startActivityForResult(intent, EDIT_APPOINTMENT_REQUEST)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val appStartView: TextView = view.appStartTextView
            val appEndView: TextView = view.appStartTimeTextView
            val reasonView: TextView = view.reasonTextView
            val patientNameView: TextView = view.patientNameTextView
            val editAppView: ImageView = view.editAppView
            val deleteAppView: ImageView = view.deleteAppView
            val endTimeView: TextView = view.endTimeTextV
        }
    }

    companion object {
        const val ADD_APPOINTMENT_REQUEST = 1
        const val EDIT_APPOINTMENT_REQUEST = 2
    }

}
