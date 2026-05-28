package com.example.petcarecjc.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.*
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.petcarecjc.R
import com.example.petcarecjc.model.Pet
import com.example.petcarecjc.viewmodel.PetViewModel

class RegisterPetActivity : ComponentActivity() {

    private val viewModel: PetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current

            RegisterPetScreen(
                onSave = { pet ->
                    viewModel.savePet(pet) {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    }
                },
                onBack = { finish() }
            )
        }
    }
}
//registro de mascotas
@Composable
fun RegisterPetScreen(onSave: (Pet) -> Unit, onBack: () -> Unit) {

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    Column(Modifier.padding(20.dp)) {

        TextButton(onClick = onBack) {
            Text(stringResource(R.string.back))
        }

        Text(stringResource(R.string.add_pet))

        CustomTextField(name, { name = it }, stringResource(R.string.name))
        CustomTextField(type, { type = it }, "Tipo")

        Button(
            onClick = {
                onSave(Pet(nombre = name, tipo = type))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_pet))
        }
    }
}

@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(value, onValueChange, label = { Text(label) })
}