package com.example.alarmapplication.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.alarmapplication.model.Alarm

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {

        val createAlarmsTable = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TIME + " INT NOT NULL, " +
                COL_LABEL + " TEXT, " +
                COL_SUN + " INTEGER NOT NULL, " +
                COL_MON + " INTEGER NOT NULL, " +
                COL_TUES + " INTEGER NOT NULL, " +
                COL_WED + " INTEGER NOT NULL, " +
                COL_THURS + " INTEGER NOT NULL, " +
                COL_FRI + " INTEGER NOT NULL, " +
                COL_SAT + " INTEGER NOT NULL, " +
                COL_IS_ENABLED + " INTEGER NOT NULL, " +
                COL_URI_NOTIFICATION + " TEXT NOT NULL " +
                ");"
        sqLiteDatabase.execSQL(createAlarmsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)

    }

    fun addAlarm(alarm: Alarm): Long {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_TIME, alarm.getTime())
        cv.put(COL_LABEL, alarm.getLabel())

        val days: BooleanArray = alarm.getDay()!!
        cv.put(COL_MON, if (days[Alarm.MON]) 1 else 0)
        cv.put(COL_TUES, if (days[Alarm.TUES]) 1 else 0)
        cv.put(COL_WED, if (days[Alarm.WED]) 1 else 0)
        cv.put(COL_THURS, if (days[Alarm.THURS]) 1 else 0)
        cv.put(COL_FRI, if (days[Alarm.FRI]) 1 else 0)
        cv.put(COL_SAT, if (days[Alarm.SAT]) 1 else 0)
        cv.put(COL_SUN, if (days[Alarm.SUN]) 1 else 0)

        cv.put(COL_IS_ENABLED, alarm.getisEnabled())
        cv.put(COL_URI_NOTIFICATION, alarm.getUriNotification())

        val res = db.insert(TABLE_NAME, null, cv)
        db.close()
        return res
    }

    fun getallAlarms(): Cursor{
        val db = this.readableDatabase
        val query = "SELECT * from $TABLE_NAME"
        return  db.rawQuery(query, null)
    }

    fun updateAlarm(alarm: Alarm, id: Long): Long{
        val db = writableDatabase

        val cv = ContentValues()
        cv.put(COL_TIME, alarm.getTime())
        cv.put(COL_LABEL, alarm.getLabel())

        val days: BooleanArray = alarm.getDay()!!
        cv.put(COL_MON, if (days[Alarm.MON]) 1 else 0)
        cv.put(COL_TUES, if (days[Alarm.TUES]) 1 else 0)
        cv.put(COL_WED, if (days[Alarm.WED]) 1 else 0)
        cv.put(COL_THURS, if (days[Alarm.THURS]) 1 else 0)
        cv.put(COL_FRI, if (days[Alarm.FRI]) 1 else 0)
        cv.put(COL_SAT, if (days[Alarm.SAT]) 1 else 0)
        cv.put(COL_SUN, if (days[Alarm.SUN]) 1 else 0)

        cv.put(COL_IS_ENABLED, alarm.getisEnabled())
        cv.put(COL_URI_NOTIFICATION, alarm.getUriNotification())

        val res = db.update(TABLE_NAME, cv, "$ID = $id", null)
        db.close()
        return res.toLong()

    }

    fun switchCompatEnable(checked: Boolean, id: Long){
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(COL_IS_ENABLED, checked)
        db.update(TABLE_NAME, cv, "$ID = $id", null)
        db.close()
    }

    fun getEnabledAlarms(): Cursor{
        val db = this.readableDatabase
        val query = "SELECT * from $TABLE_NAME where $COL_IS_ENABLED = 1"
        return db.rawQuery(query, null)
    }

    fun getLastEntryID(): Long{
        val db = this.readableDatabase
        val query = "SELECT * from $TABLE_NAME ORDER BY $ID DESC LIMIT 1"
        val cursor: Cursor = db.rawQuery(query, null)
        var vcount = 0
        while (cursor.moveToNext()) {
            vcount = cursor.getInt(0)
        }
        return vcount.toLong()
    }

    fun deleteAlarm(id: Long): Int {
        val where = "$ID=?"
        val whereArgs = arrayOf(java.lang.Long.toString(id))
        return writableDatabase.delete(TABLE_NAME, where, whereArgs)
    }

    companion object {
        private const val DATABASE_NAME = "alarms.db"
        private const val TABLE_NAME = "alarms"
        const val ID = "id"
        const val COL_TIME = "time"
        const val COL_LABEL = "label"
        const val COL_MON = "monday"
        const val COL_TUES = "tuesday"
        const val COL_WED = "wednesday"
        const val COL_THURS = "thursday"
        const val COL_FRI = "friday"
        const val COL_SAT = "saturday"
        const val COL_SUN = "sunday"
        const val COL_IS_ENABLED = "is_enabled"
        const val COL_URI_NOTIFICATION = "uri_notification"

    }
}
