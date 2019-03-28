//package andrei.csubb.com.dentistryapp.model
//
//import java.util.*
//import kotlin.collections.ArrayList
//
//object AppointmentContent{
//    val ITEMS: MutableList<Appointment> = ArrayList()
//    val DURATIONS: MutableList<Int> = ArrayList()
//
//    init{
//        ITEMS.add(Appointment(1,Date(),Date(),"carie mare","Vasilica"))
//        ITEMS.add(Appointment(2,Date(),Date(),"extractie","Tomita"))
//        DURATIONS.add(30)
//        DURATIONS.add(45)
//        DURATIONS.add(60)
//        DURATIONS.add(75)
//        DURATIONS.add(90)
//        DURATIONS.add(120)
//    }
//
//    fun getById(id:Int?):Appointment?{
//        for (app:Appointment in ITEMS) {
//            if (app.id == id){
//                return app
//            }
//        }
//        return null
//    }
//
//    fun update(appointment: Appointment){
//        for(app:Appointment in ITEMS){
//            if(app.id == appointment.id){
//                app.reason = appointment.reason
//                app.patientName = appointment.patientName
//                app.startDate = appointment.startDate
//                app.endDate = appointment.endDate
//            }
//        }
//
//    }
//
//    fun delete(id:Int?):Int?{
//        var pos:Int? = null
//        for((index,item) in ITEMS.withIndex()){
//            if(item.id == id){
//                pos = index
//                break
//            }
//        }
//        pos?.let { ITEMS.removeAt(it) }
//
//        return pos
//    }
//}