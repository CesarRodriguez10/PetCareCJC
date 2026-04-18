package com.example.petcarecjc.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    onDetailClick = { pet ->
                        selectedPet = pet
                    },
                    onBack = {
                        finish() // Cierra esta actividad y vuelve a la anterior (MainActivity)
                    }
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
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Encabezado con Imagen
        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            Image(
                painter = painterResource(id = R.drawable.jager),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Botón Volver Estándar (Reutilizando el diseño de PetList)
            Surface(
                color = Color.Black.copy(alpha = 0.4f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
            ) {
                TextButton(
                    onClick = onBack,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Volver",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(24.dp)) {
            // Nombre y Datos Básicos
            Text(
                text = pet.nombre,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF009688)
            )
            
            Text(
                text = "${pet.tipo} • ${pet.raza} • ${pet.genero}",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "Edad: ${if(pet.edad.isEmpty()) "No especificada" else pet.edad}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            // SECCIÓN: INFORMACIÓN PERSONAL
            DetailSectionTitle("Información Personal")
            DetailItem("Descripción", pet.descripcion.ifEmpty { "Sin descripción disponible." })
            
            Spacer(modifier = Modifier.height(24.dp))

            // SECCIÓN: SALUD
            DetailSectionTitle("Información de Salud")
            DetailItem("Vacunas", pet.vacunas)
            DetailItem("Enfermedades", pet.enfermedades)
            DetailItem("Medicamentos actuales", pet.medicamentos)
            DetailItem("Alergias", pet.alergias)
            DetailItem("Última consulta veterinaria", pet.ultimaConsulta)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun DetailSectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF009688),
        modifier = Modifier.padding(bottom = 12.dp)
    )
}

@Composable
fun DetailItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Text(
            text = value.ifEmpty { "No registrado" },
            fontSize = 16.sp,
            color = Color.DarkGray
        )
    }
}
