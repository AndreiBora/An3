package andrei.csubb.com.dentistryapp.model

import andrei.csubb.com.dentistryapp.Utils.convertDateToStringFormat
import org.json.JSONObject
import java.util.*

data class Appointment(val id :Long? = null, var startDate: Date,var endDate:Date,var reason:String, var patientName:String){

    fun toJson():String{
        val jsonObject = JSONObject()
        jsonObject.apply {
            id.let {
                this.put("id", id)
            }
            put("startDate", convertDateToStringFormat(startDate))
            put("endDate", convertDateToStringFormat(endDate))
            put("reason", reason)
            put("patientName", patientName)
        }
        return jsonObject.toString()
    }
}