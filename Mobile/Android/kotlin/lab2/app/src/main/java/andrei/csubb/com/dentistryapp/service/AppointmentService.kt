package andrei.csubb.com.dentistryapp.service

import andrei.csubb.com.dentistryapp.MainActivity
import andrei.csubb.com.dentistryapp.Utils.convertStringToDate
import andrei.csubb.com.dentistryapp.Utils.logd
import andrei.csubb.com.dentistryapp.model.Appointment
import andrei.csubb.com.dentistryapp.repository.AppointmentDbManager
import android.content.ContentValues
import android.provider.BaseColumns
import android.support.annotation.MainThread
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import java.io.IOException

object AppointmentService {
    val client = OkHttpClient.Builder()
            //.readTimeout(10, TimeUnit.SECONDS)
            .build()
    const val URL = "http://023b5d6d.ngrok.io"




    fun deleteAppointment(id: Long,dbManager: AppointmentDbManager,listAppointments: ArrayList<Appointment>,appointmentListView: RecyclerView) {
        val delRequest = Request.Builder().url("$URL/api/appointment/$id").delete().build()
        val call = client.newCall(delRequest)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                logd("Delete request failed")
            }

            override fun onResponse(call: Call, response: Response) {
                val selectionArgs = arrayOf(id.toString())
                val selection = "_ID = ?"
                val nrRowsDeleted = dbManager.delete(selection, selectionArgs)
                for(app in listAppointments){
                    if(app.id == id){
                        listAppointments.remove(app)
                    }
                }
                if (nrRowsDeleted > 0) {
                    GlobalScope.launch(Dispatchers.Main) {
                        appointmentListView.adapter!!.notifyDataSetChanged();
                        logd("Appointment successfully deleted")
                    }
                } else {
                    logd("Fail to delete appointment")
                }
            }

        })
    }

    fun addAppointment(appointment: Appointment, dbManager: AppointmentDbManager, values: ContentValues, listAppointments: ArrayList<Appointment>, appointmentListView: RecyclerView){

        val json = appointment.toJson()
        val body:RequestBody = RequestBody.create(MediaType.parse("application/json"),json)
        val request = Request.Builder()
                .url("$URL/api/appointment")
                .post(body)
                .build()
        val cal = client.newCall(request)
        cal.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                logd("failure")
            }

            override fun onResponse(call: Call, response: Response) {
                val obj = JSONObject(response.body()!!.string())
                val id = obj.getLong("id")
                val patientName  = obj.getString("patientName")
                val reason = obj.getString("reason")
                val startDate = convertStringToDate(obj.getString("startDate"))
                val endDate = convertStringToDate(obj.getString("endDate"))
                val app = Appointment(id,startDate,endDate,reason,patientName)

                //insert to DB
                values.put(BaseColumns._ID,id.toString())
                dbManager.insert(values)

                GlobalScope.launch(Dispatchers.Main) {
                    listAppointments.add(app)
                    appointmentListView.adapter!!.notifyDataSetChanged()
                    logd("success")
                }

            }

        })
    }
    fun editAppointment(appointment: Appointment, dbManager: AppointmentDbManager, values: ContentValues, listAppointments: ArrayList<Appointment>, appointmentListView: RecyclerView){

        val json = appointment.toJson()
        val body:RequestBody = RequestBody.create(MediaType.parse("application/json"),json)
        val request = Request.Builder()
                .url("$URL/api/appointment/${appointment.id}")
                .put(body)
                .build()
        val cal = client.newCall(request)
        cal.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                logd("failure")
            }

            override fun onResponse(call: Call, response: Response) {
                val selection = "_ID = ?"
                val selectionArgs = arrayOf(appointment.id.toString())
                val res = dbManager.update(values, selection, selectionArgs)

                GlobalScope.launch(Dispatchers.Main) {
                    listAppointments.filter {
                        it.id == appointment.id
                    }.first().apply {
                        this.patientName = appointment.patientName
                        this.startDate = appointment.startDate
                        this.endDate = appointment.endDate
                        this.reason = appointment.reason
                    }
                    appointmentListView.adapter!!.notifyDataSetChanged()
                    logd("success")
                }

            }

        })
    }
}