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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.petcarecjc.R
import com.example.petcarecjc.model.Pet
import com.example.petcarecjc.viewmodel.PetViewModel

class PetDetailActivity : ComponentActivity() {

    private val viewModel: PetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadPets()

        setContent {
            val pets by viewModel.pets.collectAsState()
            var selectedPet by remember { mutableStateOf<Pet?>(null) }

            if (selectedPet == null) {
                PetListScreen(
                    pets = pets,
                    onDetailClick = { selectedPet = it },
                    onBack = { finish() }
                )
            } else {
                SinglePetDetailScreen(
                    pet = selectedPet!!,
                    onBack = { selectedPet = null }
                )
            }
        }
    }
}

@Composable
fun SinglePetDetailScreen(pet: Pet, onBack: () -> Unit) {
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
                Text(stringResource(R.string.back), color = Color.White)
            }
        }

        Column(modifier = Modifier.padding(24.dp)) {

            Text(pet.nombre, fontSize = 32.sp, fontWeight = FontWeight.Bold)

            Text(
                "${pet.tipo} • ${pet.raza} • ${pet.genero}",
                color = Color.Gray
            )

            Text(
                "${stringResource(R.string.age)}: ${
                    if (pet.edad.isEmpty())
                        stringResource(R.string.not_specified)
                    else pet.edad
                }"
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            DetailSectionTitle(stringResource(R.string.personal_info))
            DetailItem(
                stringResource(R.string.description),
                pet.descripcion.ifEmpty {
                    stringResource(R.string.no_description)
                }
            )

            Spacer(Modifier.height(20.dp))

            DetailSectionTitle(stringResource(R.string.health_info))
            DetailItem("Vacunas", pet.vacunas)
            DetailItem("Enfermedades", pet.enfermedades)
        }
    }
}

@Composable
fun DetailSectionTitle(title: String) {
    Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
}

@Composable
fun DetailItem(label: String, value: String) {
    Column {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value.ifEmpty { "No registrado" })
    }
}