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
import kotlinx.coroutines.delay
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import android.graphics.BitmapFactory

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
fun RegisterPetScreen(
    onSave: (Pet) -> Unit,
    onBack: () -> Unit
) {

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var breed by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var vaccines by remember { mutableStateOf("") }
    var diseases by remember { mutableStateOf("") }
    var medications by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }
    var lastVisit by remember { mutableStateOf("") }

    var showSavedCard by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        selectedImageUri = uri
    }
    val scrollState = rememberScrollState()

        TextButton(onClick = onBack) {
            Text(stringResource(R.string.back))
        }

        Text(stringResource(R.string.add_pet))

        Box(modifier = Modifier.fillMaxWidth()) {

                if (selectedImageUri != null) {

                    val inputStream = context.contentResolver.openInputStream(selectedImageUri!!)
                    val bitmap = BitmapFactory.decodeStream(inputStream)

                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Mascota",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 20.dp,
                                    bottomEnd = 20.dp
                                )
                            ),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Image(
                        painter = painterResource(id = R.drawable.jager),
                        contentDescription = "Mascota",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 20.dp,
                                    bottomEnd = 20.dp
                                )
                            ),
                        contentScale = ContentScale.Crop
                    )
                }

            
            Button(
                onClick = {
                    imagePicker.launch("image/*")
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(androidx.compose.ui.Alignment.BottomEnd),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f))
            ) {
                Text("Cambiar Foto", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        Button(
            onClick = {
                onSave(Pet(nombre = name, tipo = type))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Agrega tus mascotas",
                style = MaterialTheme.typography.headlineMedium
            )

            if (showSavedCard) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Tus mascotas fueron guardadas",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        TextButton(onClick = { showSavedCard = false }) {
                            Text("\uD83D\uDC3E")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Información Personal", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            CustomTextField(value = name, onValueChange = { name = it }, label = "Nombre de tu mascota")
            CustomTextField(value = type, onValueChange = { type = it }, label = "Tipo (Perro, Gato, etc)")
            CustomTextField(value = breed, onValueChange = { breed = it }, label = "Raza")
            CustomTextField(value = gender, onValueChange = { gender = it }, label = "Género")
            CustomTextField(value = age, onValueChange = { age = it }, label = "Edad / Fecha Nacimiento")
            CustomTextField(value = description, onValueChange = { description = it }, label = "Descripción")

            Spacer(modifier = Modifier.height(24.dp))

            Text("Información de Salud", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            CustomTextField(value = vaccines, onValueChange = { vaccines = it }, label = "Vacunas")
            CustomTextField(value = diseases, onValueChange = { diseases = it }, label = "Enfermedades / Condición")
            CustomTextField(value = medications, onValueChange = { medications = it }, label = "Medicamentos actuales")
            CustomTextField(value = allergies, onValueChange = { allergies = it }, label = "Alergias")
            CustomTextField(value = lastVisit, onValueChange = { lastVisit = it }, label = "Última consulta veterinario")

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    val pet = Pet(
                        nombre = name,
                        tipo = type,
                        raza = breed,
                        genero = gender,
                        edad = age,
                        descripcion = description,
                        vacunas = vaccines,
                        enfermedades = diseases,
                        medicamentos = medications,
                        alergias = allergies,
                        ultimaConsulta = lastVisit
                    )
                    onSave(pet)
                    showSavedCard = true


                    name = ""; type = ""; breed = ""; gender = ""; age = ""; description = ""
                    vaccines = ""; diseases = ""; medications = ""; allergies = ""; lastVisit = ""
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Guardar Mascota", style = MaterialTheme.typography.titleMedium)
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(value, onValueChange, label = { Text(label) })
}