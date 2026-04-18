package com.example.petcarecjc.model

data class Pet(
    var id: String = "",
    var nombre: String = "",
    var tipo: String = "",
    var raza: String = "",
    var genero: String = "",
    var edad: String = "",
    var descripcion: String = "",
    var fotoUri: String = "",
    // Información de salud
    var vacunas: String = "",
    var enfermedades: String = "",
    var medicamentos: String = "",
    var alergias: String = "",
    var ultimaConsulta: String = ""
)