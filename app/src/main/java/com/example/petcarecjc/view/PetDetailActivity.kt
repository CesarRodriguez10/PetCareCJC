package com.example.petcarecjc.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.petcarecjc.R
import com.example.petcarecjc.model.Pet
import com.example.petcarecjc.viewmodel.PetViewModel
import androidx.compose.ui.res.stringResource

class PetDetailActivity : ComponentActivity() {

    private val viewModel: PetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadPets()

        setContent {

            val pets by viewModel.pets.collectAsState()

            var selectedPet by remember {
                mutableStateOf<Pet?>(null)
            }
            var editingPet by remember {
                mutableStateOf<Pet?>(null)
            }

            if (selectedPet == null) {

                PetListScreen(
                    pets = pets,

                    onDetailClick = {
                        selectedPet = it
                    },

                    onEditClick = { pet ->
                        editingPet = pet
                    },

                    onDeleteClick = { pet ->
                        viewModel.deletePet(pet.id)
                    },

                    onBack = {
                        finish()
                    }
                )

            } else {

                SinglePetDetailScreen(
                    pet = selectedPet!!,

                    onBack = {
                        selectedPet = null
                    }
                )
            }
            editingPet?.let { pet ->

                var newName by remember { mutableStateOf(pet.nombre) }
                var newType by remember { mutableStateOf(pet.tipo) }
                var newBreed by remember { mutableStateOf(pet.raza) }
                var newGender by remember { mutableStateOf(pet.genero) }
                var newAge by remember { mutableStateOf(pet.edad) }
                var newDescription by remember { mutableStateOf(pet.descripcion) }

                var newVaccines by remember { mutableStateOf(pet.vacunas) }
                var newDiseases by remember { mutableStateOf(pet.enfermedades) }
                var newMedications by remember { mutableStateOf(pet.medicamentos) }
                var newAllergies by remember { mutableStateOf(pet.alergias) }
                var newLastVisit by remember { mutableStateOf(pet.ultimaConsulta) }

                AlertDialog(

                    onDismissRequest = {
                        editingPet = null
                    },

                    title = {
                        Text(stringResource(R.string.edit_pet))
                    },

                    text = {

                        Column(
                            modifier = Modifier.verticalScroll(
                                rememberScrollState()
                            )
                        ) {

                            OutlinedTextField(
                                value = newName,
                                onValueChange = { newName = it },
                                label = { Text(stringResource(R.string.name)) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newType,
                                onValueChange = { newType = it },
                                label = { Text(stringResource(R.string.type)) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newBreed,
                                onValueChange = { newBreed = it },
                                label = { Text(stringResource(R.string.breed)) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newGender,
                                onValueChange = { newGender = it },
                                label = { Text(stringResource(R.string.gender)) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newAge,
                                onValueChange = { newAge = it },
                                label = { Text(stringResource(R.string.age)) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newDescription,
                                onValueChange = { newDescription = it },
                                label = { Text(stringResource(R.string.description)) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newVaccines,
                                onValueChange = { newVaccines = it },
                                label = { Text(stringResource(R.string.vaccines)) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newDiseases,
                                onValueChange = { newDiseases = it },
                                label = { Text(stringResource(R.string.diseases)) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newMedications,
                                onValueChange = { newMedications = it },
                                label = { Text(stringResource(R.string.medications)) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newAllergies,
                                onValueChange = { newAllergies = it },
                                label = { Text(stringResource(R.string.allergies)) }
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newLastVisit,
                                onValueChange = { newLastVisit = it },
                                label = { Text(stringResource(R.string.last_visit)) }
                            )
                        }
                    },

                    confirmButton = {

                        Button(
                            onClick = {

                                val updatedPet = pet.copy(
                                    nombre = newName,
                                    tipo = newType,
                                    raza = newBreed,
                                    genero = newGender,
                                    edad = newAge,
                                    descripcion = newDescription,
                                    vacunas = newVaccines,
                                    enfermedades = newDiseases,
                                    medicamentos = newMedications,
                                    alergias = newAllergies,
                                    ultimaConsulta = newLastVisit
                                )

                                viewModel.updatePet(updatedPet)

                                editingPet = null
                            }
                        ) {

                            Text(stringResource(R.string.save))
                        }
                    },

                    dismissButton = {

                        OutlinedButton(
                            onClick = {
                                editingPet = null
                            }
                        ) {

                            Text(stringResource(R.string.cancel))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PetListScreen(
    pets: List<Pet>,
    onDetailClick: (Pet) -> Unit,
    onEditClick: (Pet) -> Unit,
    onDeleteClick: (Pet) -> Unit,
    onBack: () -> Unit
)

{

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        TextButton(onClick = onBack) {
            Text(stringResource(R.string.back))
        }

        Text(
            text = stringResource(R.string.my_pets),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (pets.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(stringResource(R.string.no_pets_registered))
            }

        } else {

            pets.forEach { pet ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),

                    shape = RoundedCornerShape(20.dp),

                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {

                    Column {

                        Image(
                            painter = painterResource(R.drawable.jager),
                            contentDescription = null,

                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),

                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = pet.nombre,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )

                            Text(
                                text = stringResource(
                                    R.string.pet_type_breed,
                                    pet.tipo,
                                    pet.raza
                                ),
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = stringResource(
                                    R.string.pet_type_breed,
                                    pet.tipo,
                                    pet.raza
                                )
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {

                                Button(
                                    onClick = {
                                        onDetailClick(pet)
                                    }
                                ) {
                                    Text(stringResource(R.string.view))
                                }

                                Button(
                                    onClick = {

                                        val updatedPet = pet.copy(
                                            nombre = pet.nombre
                                        )

                                        onEditClick(updatedPet)
                                    },

                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF009688)
                                    )
                                ) {
                                    Text(stringResource(R.string.edit))
                                }

                                Button(
                                    onClick = {
                                        onDeleteClick(pet)
                                    },

                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.Red
                                    )
                                ) {
                                    Text(stringResource(R.string.delete))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SinglePetDetailScreen(
    pet: Pet,
    onBack: () -> Unit
) {

    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scroll)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {

            Image(
                painter = painterResource(R.drawable.jager),
                contentDescription = null,

                modifier = Modifier.fillMaxSize(),

                contentScale = ContentScale.Crop
            )

            TextButton(onClick = onBack) {

                Text(
                    stringResource(R.string.back),
                    color = Color.White
                )
            }
        }

        Column(
            modifier = Modifier.padding(24.dp)
        ) {

            Text(
                pet.nombre,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "${pet.tipo} • ${pet.raza} • ${pet.genero}",
                color = Color.Gray
            )

            Text(
                "${stringResource(R.string.age)}: ${
                    pet.edad.ifEmpty { stringResource(R.string.not_specified) }
                }"
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            DetailSectionTitle(
                stringResource(R.string.personal_info)
            )

            DetailItem(
                stringResource(R.string.description),

                pet.descripcion.ifEmpty {
                    stringResource(R.string.no_description)
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            DetailSectionTitle(
                stringResource(R.string.health_info)
            )

            DetailItem(
                stringResource(R.string.vaccines),
                pet.vacunas
            )

            DetailItem(
                stringResource(R.string.diseases),
                pet.enfermedades
            )

            DetailItem(
                stringResource(R.string.medications),
                pet.medicamentos
            )

            DetailItem(
                stringResource(R.string.allergies),
                pet.alergias
            )

            DetailItem(
                stringResource(R.string.last_visit),
                pet.ultimaConsulta
            )
        }
    }
}

@Composable
fun DetailSectionTitle(title: String) {

    Text(
        title,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
}

@Composable
fun DetailItem(
    label: String,
    value: String
) {

    Column(
        modifier = Modifier.padding(vertical = 6.dp)
    ) {

        Text(
            label,
            fontWeight = FontWeight.Bold
        )

        Text(
            value.ifEmpty {
                stringResource(R.string.not_registered)
            }
        )
    }
}

//comentario