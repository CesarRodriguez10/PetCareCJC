package com.example.petcarecjc.model


enum class ReminderType {
    VACUNA,
    MEDICAMENTO,
    CITA_VET,
    ALIMENTACION,
    CUMPLEANOS
}

/**
 * Representa un recordatorio asociado a una mascota.
 *
 * @param id          ID único generado por Firestore
 * @param petId       ID de la mascota a la que pertenece
 * @param petName     Nombre de la mascota (para mostrar en la notificación)
 * @param type        Tipo de recordatorio [ReminderType]
 * @param title       Título de la notificación
 * @param message     Mensaje de la notificación
 * @param triggerMillis Timestamp en ms cuando debe dispararse (0 = diario)
 * @param isDaily     true = se repite cada día a dailyHour:dailyMinute
 * @param dailyHour   Hora del recordatorio diario (0-23)
 * @param dailyMinute Minuto del recordatorio diario (0-59)
 * @param active      Si está activo o pausado
 */
data class Reminder(
    var id: String         = "",
    var petId: String      = "",
    var petName: String    = "",
    var type: String       = ReminderType.VACUNA.name,
    var title: String      = "",
    var message: String    = "",
    var triggerMillis: Long = 0L,
    var isDaily: Boolean   = false,
    var dailyHour: Int     = 8,
    var dailyMinute: Int   = 0,
    var active: Boolean    = true
)