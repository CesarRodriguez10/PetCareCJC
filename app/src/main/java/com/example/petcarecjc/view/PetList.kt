package com.example.petcarecjc.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.petcarecjc.R
import com.example.petcarecjc.model.Pet

@Composable
fun PetListScreen(
    pets: List<Pet>,
    onDetailClick: (Pet) -> Unit,
    onBack: () -> Unit
) {

    var search by remember { mutableStateOf("") }

    Column {

        TextButton(onClick = onBack) {
            Text(
                stringResource(R.string.back)
            )
        }

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            placeholder = { Text(
                stringResource(R.string.search_pet)
            ) }
        )

        Text(
            stringResource(R.string.my_pets)
        )

        LazyColumn {
            items(pets) { pet ->
                PetItem(pet, onDetailClick)
            }
        }
    }
}

@Composable
fun PetItem(pet: Pet, onDetailClick: (Pet) -> Unit) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            stringResource(
                R.string.pet_item_format,
                pet.nombre,
                pet.tipo
            )
        )

        Button(onClick = { onDetailClick(pet) }) {
            Text(
                stringResource(R.string.detail)
            )
        }
    }
}

//comentario