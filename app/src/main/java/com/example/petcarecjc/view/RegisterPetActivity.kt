package com.example.petcarecjc

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.petcarecjc.model.Pet
import com.example.petcarecjc.viewmodel.PetViewModel

class RegisterPetActivity : ComponentActivity() {

    private val viewModel: PetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val context = LocalContext.current
            val status by viewModel.status.collectAsState()

            RegisterPetScreen { pet ->
                viewModel.savePet(pet) { message ->
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }

            LaunchedEffect(status) {
                if (status.isNotEmpty()) {
                    Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

@Composable
fun RegisterPetScreen(onSave: (Pet) -> Unit) {

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Agrega tu mascota",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Tipo (Perro, Gato...)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Género") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                val pet = Pet(
                    nombre = name,
                    tipo = type,
                    genero = gender,
                    descripcion = description
                )
                onSave(pet)
                name = ""
                type = ""
                gender = ""
                description = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }
    }
}