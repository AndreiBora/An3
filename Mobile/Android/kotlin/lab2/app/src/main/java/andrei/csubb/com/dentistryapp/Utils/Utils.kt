package andrei.csubb.com.dentistryapp.Utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

fun Any.logd(message: Any? = "no message!") {
    Log.d(this.javaClass.simpleName, message.toString())
}

fun convertDateToStringFormat(date:Date): String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault()).format(date)
}

fun convertStringToDate(dateStr:String):Date{
    return SimpleDateFormat("yyyy-MM-dd HH:mm").parse(dateStr)
}