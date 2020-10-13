package com.example.alarmapplication.model

import java.io.Serializable

class Alarm(){
    constructor(id: Long, label: String, time: Long, allDays: BooleanArray, enabled: Boolean) : this() {
        this.id = id
        this.label = label
        this.time = time
        this.allDays = allDays
        this.isEnabled = enabled
    }

    private var id: Long = 0
    private var time: Long = 0
    private var label: String = ""
    private var allDays = BooleanArray(size = 7)
    private var isEnabled = false

    fun getId(): Long {
        return id
    }

    fun setId(id: Long){
        this.id = id
    }

    fun getTime(): Long{
        return time
    }

    fun setTime(time: Long) {
        this.time = time
    }

    fun getLabel(): String?{
        return label
    }

    fun setLabel(label: String) {
        this.label = label
    }

    fun getDay(): BooleanArray?{
        return allDays
    }

    fun setDay(day: Int, isChecked: Boolean) {
        allDays[day] = isChecked
    }

    fun getisEnabled(): Boolean{
        return isEnabled
    }

    fun setisEnabled(isEnabled: Boolean) {
        this.isEnabled = isEnabled
    }


    companion object{

        const val SUN = 0
        const val MON = 1
        const val TUES = 2
        const val WED = 3
        const val THURS = 4
        const val FRI = 5
        const val SAT = 6

    }

}