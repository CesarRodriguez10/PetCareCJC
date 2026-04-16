package com.example.petcarecjc.view


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            Image(
                painter = painterResource(id = R.drawable.jager),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            TextButton(
                onClick = onBack,
                modifier = Modifier.padding(16.dp).align(Alignment.TopStart)
            ) {
                Text("← Volver", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = pet.nombre,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF009688)
            )
            
            Text(
                text = "${pet.tipo} • ${pet.genero}",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Text(
                text = "Descripción",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (pet.descripcion.isEmpty()) "Sin descripción disponible." else pet.descripcion,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun PetDetailScreen() {
    Text(text = "Detalle de Mascota")
}
