package com.example.petcarecjc.viewmodel

import androidx.lifecycle.ViewModel
import com.example.petcarecjc.model.Pet
import com.example.petcarecjc.repository.PetRepository

class PetViewModel : ViewModel() {

    private val repository = PetRepository()
//nuevo
    fun savePet(pet: Pet, onResult: (String) -> Unit) {
        repository.savePet(pet,
            onSuccess = { onResult("Guardado") },
            onError = { onResult("Error: ${it.message}") }
        )
    }
}
