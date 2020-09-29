package com.example.alarmapplication.adapter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapplication.AddEditAlarmActivity
import com.example.alarmapplication.AlarmReceiver
import com.example.alarmapplication.R
import com.example.alarmapplication.data.DatabaseHelper
import com.example.alarmapplication.model.Alarm
import java.text.SimpleDateFormat
import java.util.*

class AlarmRowAdapter(private val context: Context, alarmList: List<Alarm>?) : RecyclerView.Adapter<AlarmRowAdapter.ViewHolder>() {
    private val alarmList: List<Alarm> = alarmList!!
    private val TIME_FORMAT = SimpleDateFormat("h:mm", Locale.getDefault())
    private val AM_PM_FORMAT = SimpleDateFormat("a", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.alarm_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm: Alarm = alarmList[position]

        val formattedDate = TIME_FORMAT.format(alarm.getTime()) +" " + AM_PM_FORMAT.format(alarm.getTime())

        holder.timetext!!.text = formattedDate
        holder.labeltext!!.text = alarm.getLabel()

        val days: BooleanArray
        days = alarm.getDay()!!
        if(days[0])
            holder.daytext!!.append("Sun ")
        if(days[1])
            holder.daytext!!.append("Mon ")
        if(days[2])
            holder.daytext!!.append("Tue ")
        if(days[3])
            holder.daytext!!.append("Wed ")
        if(days[4])
            holder.daytext!!.append("Thu ")
        if(days[5])
            holder.daytext!!.append("Fri ")
        if(days[6])
            holder.daytext!!.append("Sat ")

        holder.enableswitch!!.isChecked = alarm.getisEnabled()
    }

    override fun getItemCount(): Int {
        return alarmList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var timetext: TextView? = null
        var daytext: TextView? = null
        var labeltext: TextView? = null
        var enableswitch: SwitchCompat? = null


        override fun onClick(v: View?) {
            val intent = Intent(context, AddEditAlarmActivity::class.java)
            intent.putExtra("Intent", "Edit")
            intent.putExtra("ID", alarmList[adapterPosition].getId())
            intent.putExtra("Label", alarmList[adapterPosition].getLabel())
            intent.putExtra("Days", alarmList[adapterPosition].getDay())
            intent.putExtra("Time", alarmList[adapterPosition].getTime())

            context.startActivity(intent)
        }

        init {
            val db = DatabaseHelper(context)
            itemView.setOnClickListener(this)
            timetext = itemView.findViewById(R.id.alarm_row_time)
            labeltext = itemView.findViewById(R.id.alarm_row_label)
            daytext = itemView.findViewById(R.id.alarm_row_days)
            enableswitch = itemView.findViewById(R.id.alarm_row_enable)


            enableswitch?.setOnCheckedChangeListener { _, isChecked ->

//                val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
//                    intent.putExtra("Alarm_label", alarmList[adapterPosition].getLabel())
//                    intent.putExtra("Alarm_id", alarmList[adapterPosition].getId())
//                    PendingIntent.getBroadcast(context, alarmList[adapterPosition].getId().toInt(), intent,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                    )
//                }
//
//                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//                val allDays: BooleanArray = alarmList[adapterPosition].getDay()!!
//                val c = Calendar.getInstance()
//
//                if(isChecked){
//                    val dayOfWeek = c.get(Calendar.DAY_OF_WEEK)
//                    c.timeInMillis = alarmList[adapterPosition].getTime()
//
//                    if(!allDays[0] && !allDays[1] && !allDays[2] && !allDays[3] && !allDays[4] && !allDays[5] && !allDays[6]){
//                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, alarmIntent)
//                    }
//
//                    else if(allDays[dayOfWeek-1]){
//                        c.set(Calendar.DAY_OF_WEEK, dayOfWeek)
//                        alarmManager.setRepeating(
//                            AlarmManager.RTC_WAKEUP,
//                            c.timeInMillis,
//                            AlarmManager.INTERVAL_DAY * 7,
//                            alarmIntent
//                        )
//                    }
//                }
//                else{
//                    alarmManager.cancel(alarmIntent)
//                }

                db.switchCompatEnable(isChecked, alarmList[adapterPosition].getId())
            }
        }

    }

}