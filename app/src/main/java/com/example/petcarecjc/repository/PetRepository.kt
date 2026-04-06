package com.example.petcarecjc.repository

import com.example.petcarecjc.model.Pet
import com.google.firebase.firestore.FirebaseFirestore

class PetRepository {

    private val db = FirebaseFirestore.getInstance()

    fun savePet(pet: Pet, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val id = db.collection("pets").document().id
        pet.id = id

        db.collection("pets").document(id).set(pet)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }


}
