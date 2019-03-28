package andrei.csubb.com.dentistryapp.repository

import android.provider.BaseColumns

object AppointmentContract {
    const val DB_NAME = "AppointmentsDB"
    const val DB_VERSION = 1

    object AppointmentEntry : BaseColumns {
        const val DB_TABLE = "Appointments"
        const val COLUMN_START_DATE = "StartDate"
        const val COLUMN_END_DATE = "EndDate"
        const val REASON = "Reason"
        const val PATIENT_NAME = "PatientName"
    }
}

