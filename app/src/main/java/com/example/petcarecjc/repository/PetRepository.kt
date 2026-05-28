package com.example.petcarecjc.repository

import android.util.Log
import com.example.petcarecjc.model.Pet
import com.google.firebase.firestore.FirebaseFirestore

class PetRepository {

    private val db = FirebaseFirestore.getInstance()

    fun savePet(pet: Pet, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val id = db.collection("pets").document().id
        pet.id = id

        Log.d("FIRESTORE", "Intentando guardar mascota con ID: $id")

        db.collection("pets").document(id).set(pet)
            .addOnSuccessListener {
                Log.d("FIRESTORE", "Mascota guardada exitosamente")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Error al guardar en Firestore", e)
                onError(e)
            }
    }

    fun getPets(onResult: (List<Pet>) -> Unit) {
        db.collection("pets").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("FIRESTORE", "Error al obtener mascotas", error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val pets = snapshot.documents.mapNotNull { document ->

                    val pet = document.toObject(Pet::class.java)

                    pet?.apply {
                        id = document.id
                    }
                }
                Log.d("FIRESTORE", "Mascotas recibidas: ${pets.size}")
                onResult(pets)
            }
        }
    }

    fun deletePet(
        petId: String,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        db.collection("pets")
            .document(petId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it)
            }
    }

    fun updatePet(
        pet: Pet,
        onSuccess: () -> Unit,
        onError: (Exception) -> Unit
    ) {

        db.collection("pets")
            .document(pet.id)
            .set(pet)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onError(it)
            }
    }
}
