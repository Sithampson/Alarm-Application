package com.example.alarmapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.alarmapplication.data.DatabaseHelper
import com.example.alarmapplication.model.Alarm
import java.util.*

class AddEditAlarmActivity : AppCompatActivity(), View.OnClickListener {
    private var alarmManager: AlarmManager? = null
    private var alarmTimePicker: TimePicker? = null
    private var labelEditText: TextView? = null

    private var sundaytogglebutton: ToggleButton? = null
    private var mondaytogglebutton: ToggleButton? = null
    private var tuesdaytogglebutton: ToggleButton? = null
    private var wednesdaytogglebutton: ToggleButton? = null
    private var thursdaytogglebutton: ToggleButton? = null
    private var fridaytogglebutton: ToggleButton? = null
    private var saturdaytogglebutton: ToggleButton? = null

    private lateinit var alarmIntent: PendingIntent
    private var modeIntent: Intent? = null

    private var savebutton: Button? = null
    private var deletebutton: Button? = null

    private var alarmId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_alarm)

        alarmTimePicker = findViewById(R.id.alarmTimePicker)
        labelEditText = findViewById(R.id.labelText)
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        sundaytogglebutton = findViewById(R.id.sunday_togglebutton)
        mondaytogglebutton = findViewById(R.id.monday_togglebutton)
        tuesdaytogglebutton = findViewById(R.id.tuesday_togglebutton)
        wednesdaytogglebutton = findViewById(R.id.wednesday_togglebutton)
        thursdaytogglebutton = findViewById(R.id.thursday_togglebutton)
        fridaytogglebutton = findViewById(R.id.friday_togglebutton)
        saturdaytogglebutton = findViewById(R.id.saturday_togglebutton)

        savebutton = findViewById(R.id.save_alarmbutton)
        deletebutton = findViewById(R.id.delete_alarmbutton)

        modeIntent = intent
        if(modeIntent!!.getStringExtra("Intent") == "Edit"){
            setTimePickerTime(modeIntent!!.getLongExtra("Time", 0))
            setDaysChecked(modeIntent!!.getBooleanArrayExtra("Days"))
            labelEditText?.text = modeIntent!!.getStringExtra("Label")
            alarmId = modeIntent!!.getLongExtra("ID", 0)
        }

        savebutton?.setOnClickListener(this)
        deletebutton?.setOnClickListener(this)

    }


    private fun setDaysChecked(days: BooleanArray?) {
        mondaytogglebutton!!.isChecked = days!![Alarm.MON]
        tuesdaytogglebutton!!.isChecked = days[Alarm.TUES]
        wednesdaytogglebutton!!.isChecked = days[Alarm.WED]
        thursdaytogglebutton!!.isChecked = days[Alarm.THURS]
        fridaytogglebutton!!.isChecked = days[Alarm.FRI]
        saturdaytogglebutton!!.isChecked = days[Alarm.SAT]
        sundaytogglebutton!!.isChecked = days[Alarm.SUN]

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.save_alarmbutton -> {
                val alarm = Alarm()

                val time = Calendar.getInstance()
                time.set(Calendar.MINUTE, alarmTimePicker!!.minute)
                time.set(Calendar.HOUR_OF_DAY, alarmTimePicker!!.hour)
                time.set(Calendar.SECOND, 0)
                time.set(Calendar.MILLISECOND, 0)
                alarm.setTime(time.timeInMillis)

                alarm.setLabel(labelEditText!!.text.toString())

                alarm.setDay(Alarm.SUN, sundaytogglebutton!!.isChecked)
                alarm.setDay(Alarm.MON, mondaytogglebutton!!.isChecked)
                alarm.setDay(Alarm.TUES, tuesdaytogglebutton!!.isChecked)
                alarm.setDay(Alarm.WED, wednesdaytogglebutton!!.isChecked)
                alarm.setDay(Alarm.THURS, thursdaytogglebutton!!.isChecked)
                alarm.setDay(Alarm.FRI, fridaytogglebutton!!.isChecked)
                alarm.setDay(Alarm.SAT, saturdaytogglebutton!!.isChecked)
                alarm.setisEnabled(true)

                val db = DatabaseHelper(this)
                var success: Long = 0
                if(modeIntent!!.getStringExtra("Intent") == "Add") {
                    success = db.addAlarm(alarm)
                }
                else if(modeIntent!!.getStringExtra("Intent") == "Edit"){
                    success = db.updateAlarm(alarm, alarmId)
                }

                if (success > 0) {
                    val id = db.getLastEntryID()
                    alarm.setId(id)
                    Toast.makeText(this, R.string.update_success, Toast.LENGTH_SHORT).show()

                    alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                        intent.putExtra("Alarm_label", alarm.getLabel())
                        intent.putExtra("Alarm_id", alarm.getId())
                        PendingIntent.getBroadcast(this, alarm.getId().toInt(), intent, FLAG_UPDATE_CURRENT)
                    }


                    val dayOfWeek = time.get(Calendar.DAY_OF_WEEK)
                    val allDays = alarm.getDay()

                    if(!allDays!![0] && !allDays[1] && !allDays[2] && !allDays[3] && !allDays[4] && !allDays[5] && !allDays[6]){
                        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, time.timeInMillis, alarmIntent)
                    }
                    else if(allDays[dayOfWeek-1]){
                        time.set(Calendar.DAY_OF_WEEK, dayOfWeek)

                        alarmManager?.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            time.timeInMillis,
                            AlarmManager.INTERVAL_DAY * 7,
                            alarmIntent
                        )
                    }

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                    finish()
                } else {
                    Toast.makeText(this, R.string.update_failed, Toast.LENGTH_SHORT).show()
                }

            }

            R.id.delete_alarmbutton ->{
                val alarm = Alarm()
                val db = DatabaseHelper(this)
                val res = db.deleteAlarm(alarmId)
                alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                    PendingIntent.getBroadcast(this, alarm.getId().toInt(), intent, FLAG_UPDATE_CURRENT)
                }
                if(res > 0){
                    alarmManager!!.cancel(alarmIntent)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "Alarm deleted successfully", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(this, "Error deleting alarm", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setTimePickerTime(time: Long) {
        val c = Calendar.getInstance()
        c.timeInMillis = time
        alarmTimePicker?.minute = c.get(Calendar.MINUTE)
        alarmTimePicker?.hour = c.get(Calendar.HOUR_OF_DAY)
    }

}