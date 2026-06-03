package com.example.petcarecjc.repository

import android.util.Log
import com.example.petcarecjc.model.Reminder
import com.google.firebase.firestore.FirebaseFirestore

class ReminderRepository {

    private val db  = FirebaseFirestore.getInstance()
    private val col = db.collection("reminders")

    fun saveReminder(reminder: Reminder, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val id = col.document().id
        reminder.id = id
        col.document(id).set(reminder)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    // NUEVO: actualizar un recordatorio existente por su id
    fun updateReminder(reminder: Reminder, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        col.document(reminder.id).set(reminder)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun getReminders(onResult: (List<Reminder>) -> Unit) {
        col.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("FIRESTORE", "Error reminders", error)
                return@addSnapshotListener
            }
            val list = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Reminder::class.java)?.apply { id = doc.id }
            } ?: emptyList()
            onResult(list)
        }
    }

    fun getRemindersForPet(petId: String, onResult: (List<Reminder>) -> Unit) {
        col.whereEqualTo("petId", petId).addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            val list = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(Reminder::class.java)?.apply { id = doc.id }
            } ?: emptyList()
            onResult(list)
        }
    }

    fun deleteReminder(reminderId: String, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        col.document(reminderId).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun toggleActive(reminderId: String, active: Boolean, onSuccess: () -> Unit) {
        col.document(reminderId).update("active", active)
            .addOnSuccessListener { onSuccess() }
    }
}