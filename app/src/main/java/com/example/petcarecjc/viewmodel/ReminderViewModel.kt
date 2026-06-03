package com.example.petcarecjc.viewmodel

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcarecjc.model.Reminder
import com.example.petcarecjc.model.ReminderType
import com.example.petcarecjc.notifications.NotificationHelper
import com.example.petcarecjc.notifications.NotificationScheduler
import com.example.petcarecjc.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReminderViewModel : ViewModel() {

    private val repository = ReminderRepository()

    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> = _reminders

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    init { loadReminders() }

    fun loadReminders() {
        viewModelScope.launch {
            repository.getReminders { list -> _reminders.value = list }
        }
    }

    fun loadRemindersForPet(petId: String) {
        viewModelScope.launch {
            repository.getRemindersForPet(petId) { list -> _reminders.value = list }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun addReminder(context: Context, reminder: Reminder, onResult: (String) -> Unit) {
        repository.saveReminder(reminder,
            onSuccess = {
                scheduleNotification(context, reminder)
                _status.value = "Recordatorio guardado"
                onResult("Recordatorio guardado ✅")
            },
            onError = {
                _status.value = "Error al guardar"
                onResult("Error: ${it.message}")
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun updateReminder(context: Context, reminder: Reminder, onResult: (String) -> Unit) {
        repository.updateReminder(reminder,
            onSuccess = {
                // Cancela la alarma anterior y programa la nueva
                NotificationScheduler.cancel(context, reminder.id.hashCode())
                scheduleNotification(context, reminder)
                _status.value = "Recordatorio actualizado"
                onResult("Recordatorio actualizado ")
            },
            onError = {
                _status.value = "Error al actualizar"
                onResult("Error: ${it.message}")
            }
        )
    }

    fun deleteReminder(context: Context, reminder: Reminder) {
        NotificationScheduler.cancel(context, reminder.id.hashCode())
        repository.deleteReminder(reminder.id,
            onSuccess = { _status.value = "Recordatorio eliminado" },
            onError   = { _status.value = "Error al eliminar" }
        )
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun toggleReminder(context: Context, reminder: Reminder, active: Boolean) {
        if (active) scheduleNotification(context, reminder)
        else NotificationScheduler.cancel(context, reminder.id.hashCode())
        repository.toggleActive(reminder.id, active) {
            _status.value = if (active) "Recordatorio activado" else "Recordatorio pausado"
        }
    }


    @RequiresApi(Build.VERSION_CODES.S)
    private fun scheduleNotification(context: Context, reminder: Reminder) {
        val notifId = reminder.id.hashCode()
        val channel = channelFor(reminder.type)
        if (reminder.isDaily) {
            NotificationScheduler.scheduleDaily(context, notifId, channel,
                reminder.title, reminder.message, reminder.dailyHour, reminder.dailyMinute)
        } else {
            NotificationScheduler.schedule(context, notifId, channel,
                reminder.title, reminder.message, reminder.triggerMillis)
        }
    }

    private fun channelFor(type: String) = when (type) {
        ReminderType.VACUNA.name,
        ReminderType.MEDICAMENTO.name  -> NotificationHelper.CHANNEL_VACUNAS
        ReminderType.CITA_VET.name     -> NotificationHelper.CHANNEL_CITAS
        ReminderType.ALIMENTACION.name -> NotificationHelper.CHANNEL_ALIMENTACION
        ReminderType.CUMPLEANOS.name   -> NotificationHelper.CHANNEL_CUMPLE
        else                           -> NotificationHelper.CHANNEL_CITAS
    }
}