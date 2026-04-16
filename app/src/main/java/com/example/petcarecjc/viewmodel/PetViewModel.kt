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
}