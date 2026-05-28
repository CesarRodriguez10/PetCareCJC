package com.example.petcarecjc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petcarecjc.model.Pet
import com.example.petcarecjc.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PetViewModel : ViewModel() {

    private val repository = PetRepository()
    private val _pets = MutableStateFlow<List<Pet>>(emptyList())
    val pets: StateFlow<List<Pet>> = _pets
    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    fun savePet(pet: Pet, onResult: (String) -> Unit) {
        repository.savePet(
            pet,
            onSuccess = {
                onResult("Guardado")
                _status.value = "Mascota guardada"
                loadPets()
            },
            onError = {
                onResult("Error: ${it.message}")
                _status.value = "Error: ${it.message}"
            }
        )
    }
    fun loadPets() {
        viewModelScope.launch {
            repository.getPets { list ->
                _pets.value = list
            }
        }
    }

    fun deletePet(petId: String) {

        repository.deletePet(
            petId = petId,

            onSuccess = {
                _status.value = "Mascota eliminada"
                loadPets()
            },

            onError = {
                _status.value = "Error al eliminar"
            }
        )
    }

    fun updatePet(pet: Pet) {

        repository.updatePet(
            pet = pet,

            onSuccess = {
                _status.value = "Mascota actualizada"
                loadPets()
            },

            onError = {
                _status.value = "Error al actualizar"
            }
        )
    }
}