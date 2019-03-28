package andrei.csubb.com.dentistryapp.repository

import andrei.csubb.com.dentistryapp.Utils.logd
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_CREATE_ENTRIES =
        """
            CREATE TABLE IF NOT EXISTS ${AppointmentContract.AppointmentEntry.DB_TABLE}(
                ${BaseColumns._ID} INTEGER PRIMARY KEY,
                ${AppointmentContract.AppointmentEntry.COLUMN_START_DATE} TEXT,
                ${AppointmentContract.AppointmentEntry.COLUMN_END_DATE} TEXT,
                ${AppointmentContract.AppointmentEntry.REASON} TEXT,
                ${AppointmentContract.AppointmentEntry.PATIENT_NAME} TEXT );
        """

private const val SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS ${AppointmentContract.AppointmentEntry.DB_TABLE}"

class DatabaseHeper(context:Context): SQLiteOpenHelper(context,AppointmentContract.DB_NAME, null,AppointmentContract.DB_VERSION ){
    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_ENTRIES)
        logd("Database was created")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(SQL_DELETE_ENTRIES)
        logd("Database was deleted")
    }

}