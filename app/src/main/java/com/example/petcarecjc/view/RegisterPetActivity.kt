package com.example.petcarecjc

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
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

}