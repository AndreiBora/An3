package andrei.csubb.com.dentistryapp.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.NULL

class AppointmentDbManager(context: Context) {
    private val dbHelper = DatabaseHeper(context)
    private val db:SQLiteDatabase by lazy { dbHelper.writableDatabase }

    fun insert(values: ContentValues):Long{
        return db.insert(AppointmentContract.AppointmentEntry.DB_TABLE,"",values)
    }

    fun getAll(): Cursor {
        return db.rawQuery("select * from ${AppointmentContract.AppointmentEntry.DB_TABLE}",null)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        return db.delete(AppointmentContract.AppointmentEntry.DB_TABLE, selection, selectionArgs)
    }

    fun clear(){
        db.rawQuery("DELETE FROM ${AppointmentContract.AppointmentEntry.DB_TABLE}", null)
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        return db.update(AppointmentContract.AppointmentEntry.DB_TABLE, values, selection, selectionArgs)
    }

    fun close() {
        db.close()
    }

}