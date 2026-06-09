package com.gestorplus.appgestor.owner.schedule.data.mapper

import com.gestorplus.appgestor.core.data.local.entity.*
import com.gestorplus.appgestor.owner.schedule.domain.model.*

class ScheduleMapper {
    fun toDomain(entity: WorkingShiftEntity) = WorkingShift(
        dayOfWeek = entity.dayOfWeek,
        morningStart = entity.morningStart,
        morningEnd = entity.morningEnd,
        afternoonStart = entity.afternoonStart,
        afternoonEnd = entity.afternoonEnd
    )
    fun toEntity(domain: WorkingShift) = WorkingShiftEntity(
        dayOfWeek = domain.dayOfWeek,
        morningStart = domain.morningStart,
        morningEnd = domain.morningEnd,
        afternoonStart = domain.afternoonStart,
        afternoonEnd = domain.afternoonEnd
    )
    fun toDomain(entity: MasterScheduleEntity) = MasterSchedule(
        startTime = entity.startTime,
        endTime = entity.endTime,
        enabledDays = entity.enabledDays.split(",").mapNotNull { it.toIntOrNull() }
    )
    fun toEntity(domain: MasterSchedule) = MasterScheduleEntity(
        id = 1,
        startTime = domain.startTime,
        endTime = domain.endTime,
        enabledDays = domain.enabledDays.joinToString(",")
    )
    fun toDomain(entity: AutoLunchEntity) = AutoLunchConfig(
        enabled = entity.enabled,
        startTime = entity.startTime,
        endTime = entity.endTime
    )
    fun toEntity(domain: AutoLunchConfig) = AutoLunchEntity(
        id = 1,
        enabled = domain.enabled,
        startTime = domain.startTime,
        endTime = domain.endTime
    )
    fun toDomain(entity: TimingDefaultsEntity) = TimingDefaults(
        defaultDurationMinutes = entity.durationMinutes,
        defaultBufferMinutes = entity.bufferMinutes
    )
    fun toEntity(domain: TimingDefaults) = TimingDefaultsEntity(
        id = 1,
        durationMinutes = domain.defaultDurationMinutes,
        bufferMinutes = domain.defaultBufferMinutes
    )
    fun toDomain(entity: ScheduleExceptionEntity) = ScheduleException(
        id = entity.id,
        date = entity.date,
        isOpen = entity.isOpen,
        customMorningStart = entity.customMorningStart,
        customMorningEnd = entity.customMorningEnd,
        customAfternoonStart = entity.customAfternoonStart,
        customAfternoonEnd = entity.customAfternoonEnd,
        reason = entity.reason
    )
    fun toEntity(domain: ScheduleException) = ScheduleExceptionEntity(
        id = domain.id,
        date = domain.date,
        isOpen = domain.isOpen,
        customMorningStart = domain.customMorningStart,
        customMorningEnd = domain.customMorningEnd,
        customAfternoonStart = domain.customAfternoonStart,
        customAfternoonEnd = domain.customAfternoonEnd,
        reason = domain.reason
    )
}