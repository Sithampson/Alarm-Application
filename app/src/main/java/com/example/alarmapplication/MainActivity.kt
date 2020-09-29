package com.example.alarmapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapplication.adapter.AlarmRowAdapter
import com.example.alarmapplication.data.DatabaseHelper
import com.example.alarmapplication.model.Alarm
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var fab: FloatingActionButton? = null
    private var alarmRowAdapter: AlarmRowAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.main_recyclerview)
        fab = findViewById(R.id.main_fab)
        val db = DatabaseHelper(this)

        fab?.setOnClickListener {
            val intent = Intent(this, AddEditAlarmActivity::class.java)
            intent.putExtra("Intent", "Add")
            startActivity(intent)
        }

        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(this)

        val list: ArrayList<Alarm>? = ArrayList()
        val cursor = db.getallAlarms()

        if(cursor.count == 0){
            fab?.setOnClickListener {
                val intent = Intent(this, AddEditAlarmActivity::class.java)
                intent.putExtra("Intent", "Add")
                startActivity(intent)
                finish()
            }
        }
        else {
            list?.clear()
            while (cursor.moveToNext()) {
                val allDays = BooleanArray(size = 7)

                val id = cursor.getLong(0)
                val time = cursor.getLong(1)
                val label = cursor.getString(2)
                allDays[0] = cursor.getInt(3) == 1
                allDays[1] = cursor.getInt(4) == 1
                allDays[2] = cursor.getInt(5) == 1
                allDays[3] = cursor.getInt(6) == 1
                allDays[4] = cursor.getInt(7) == 1
                allDays[5] = cursor.getInt(8) == 1
                allDays[6] = cursor.getInt(9) == 1

                val enabled = cursor.getInt(10) == 1

                list!!.add(Alarm(id, label,  time, allDays, enabled))
            }

            alarmRowAdapter = AlarmRowAdapter(this, list)
            recyclerView?.adapter = alarmRowAdapter
            alarmRowAdapter?.notifyDataSetChanged()
        }
    }

}