package com.gestorplus.appgestor.owner.schedule.data.datasource.service

import com.gestorplus.appgestor.data.datasource.FirebaseManager
import com.gestorplus.appgestor.owner.schedule.domain.model.*
import kotlinx.serialization.json.Json

class ScheduleRemoteService(
    private val firebaseManager: FirebaseManager
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun saveWorkingShifts(uid: String, shifts: List<WorkingShift>) {
        val map = shifts.associate { it.dayOfWeek.toString() to it }
        firebaseManager.saveData("workspaces/$uid/schedule/shifts", json.encodeToString(map))
    }

    suspend fun getWorkingShifts(uid: String): List<WorkingShift>? {
        val data = firebaseManager.getData("workspaces/$uid/schedule/shifts")
        return (data as? Map<String, Map<String, Any>>?)?.values?.mapNotNull { map ->
            try {
                WorkingShift(
                    dayOfWeek = (map["dayOfWeek"] as? Number)?.toInt() ?: return@mapNotNull null,
                    morningStart = map["morningStart"] as? String,
                    morningEnd = map["morningEnd"] as? String,
                    afternoonStart = map["afternoonStart"] as? String,
                    afternoonEnd = map["afternoonEnd"] as? String
                )
            } catch (e: Exception) { null }
        }
    }

    suspend fun saveMasterSchedule(uid: String, schedule: MasterSchedule) {
        firebaseManager.saveData("workspaces/$uid/schedule/master", json.encodeToString(schedule))
    }

    suspend fun getMasterSchedule(uid: String): MasterSchedule? {
        val data = firebaseManager.getData("workspaces/$uid/schedule/master")
        return (data as? Map<String, Any>)?.let {
            MasterSchedule(
                startTime = it["startTime"] as? String ?: "09:00 AM",
                endTime = it["endTime"] as? String ?: "05:00 PM",
                enabledDays = (it["enabledDays"] as? List<*>)?.mapNotNull { (it as? Number)?.toInt() } ?: listOf(1,2,3,4,5)
            )
        }
    }

    suspend fun saveAutoLunch(uid: String, config: AutoLunchConfig) {
        firebaseManager.saveData("workspaces/$uid/schedule/autoLunch", json.encodeToString(config))
    }

    suspend fun getAutoLunch(uid: String): AutoLunchConfig? {
        val data = firebaseManager.getData("workspaces/$uid/schedule/autoLunch")
        return (data as? Map<String, Any>)?.let {
            AutoLunchConfig(
                enabled = it["enabled"] as? Boolean ?: true,
                startTime = it["startTime"] as? String ?: "12:00 PM",
                endTime = it["endTime"] as? String ?: "01:00 PM"
            )
        }
    }

    suspend fun saveTimingDefaults(uid: String, defaults: TimingDefaults) {
        firebaseManager.saveData("workspaces/$uid/schedule/timingDefaults", json.encodeToString(defaults))
    }

    suspend fun getTimingDefaults(uid: String): TimingDefaults? {
        val data = firebaseManager.getData("workspaces/$uid/schedule/timingDefaults")
        return (data as? Map<String, Any>)?.let {
            TimingDefaults(
                defaultDurationMinutes = (it["defaultDurationMinutes"] as? Number)?.toInt() ?: 60,
                defaultBufferMinutes = (it["defaultBufferMinutes"] as? Number)?.toInt() ?: 15
            )
        }
    }

    suspend fun saveException(uid: String, exception: ScheduleException) {
        val path = "workspaces/$uid/schedule/exceptions/${exception.id}"
        firebaseManager.saveData(path, json.encodeToString(exception))
    }

    suspend fun deleteException(uid: String, exceptionId: String) {
        val path = "workspaces/$uid/schedule/exceptions/$exceptionId"
        firebaseManager.saveData(path, null)
    }

    suspend fun getExceptions(uid: String): List<ScheduleException> {
        val data = firebaseManager.getData("workspaces/$uid/schedule/exceptions")
        return (data as? Map<String, Map<String, Any>>?)?.values?.mapNotNull { map ->
            try {
                ScheduleException(
                    id = map["id"] as? String ?: "",
                    date = map["date"] as? String ?: "",
                    isOpen = map["isOpen"] as? Boolean ?: true,
                    customMorningStart = map["customMorningStart"] as? String,
                    customMorningEnd = map["customMorningEnd"] as? String,
                    customAfternoonStart = map["customAfternoonStart"] as? String,
                    customAfternoonEnd = map["customAfternoonEnd"] as? String,
                    reason = map["reason"] as? String
                )
            } catch (e: Exception) { null }
        } ?: emptyList()
    }
}