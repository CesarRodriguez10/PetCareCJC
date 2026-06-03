package com.example.petcarecjc.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.petcarecjc.R
import com.example.petcarecjc.model.Pet
import com.example.petcarecjc.viewmodel.PetViewModel
import kotlinx.coroutines.delay
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.rememberAsyncImagePainter

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

@Composable
fun RegisterPetScreen(onSave: (Pet) -> Unit, onBack: () -> Unit) {

    var name        by remember { mutableStateOf("") }
    var type        by remember { mutableStateOf("") }
    var breed       by remember { mutableStateOf("") }
    var gender      by remember { mutableStateOf("") }
    var age         by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var vaccines    by remember { mutableStateOf("") }
    var diseases    by remember { mutableStateOf("") }
    var medications by remember { mutableStateOf("") }
    var allergies   by remember { mutableStateOf("") }
    var lastVisit   by remember { mutableStateOf("") }
    var showSavedCard by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    var nameError      by remember { mutableStateOf("") }
    var typeError      by remember { mutableStateOf("") }
    var genderError    by remember { mutableStateOf("") }
    var ageError       by remember { mutableStateOf("") }
    var lastVisitError by remember { mutableStateOf("") }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            selectedImageUri = it
        }
    }

    LaunchedEffect(showSavedCard) {
        if (showSavedCard) { delay(3000); showSavedCard = false }
    }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {

        Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            TextButton(onClick = onBack) { Text(stringResource(R.string.back)) }
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = if (selectedImageUri != null) rememberAsyncImagePainter(selectedImageUri)
                else painterResource(id = R.drawable.jager),
                contentDescription = stringResource(R.string.pet_image),
                modifier = Modifier.fillMaxWidth().height(250.dp)
                    .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
                contentScale = ContentScale.Crop
            )
            Button(
                onClick = { launcher.launch(arrayOf("image/*")) },
                modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                )
            ) {
                Text(text = stringResource(R.string.change_photo),
                    color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {

            Text(text = stringResource(R.string.add_pet), style = MaterialTheme.typography.headlineMedium)

            if (showSavedCard) {
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                    shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = stringResource(R.string.pets_saved))
                        TextButton(onClick = { showSavedCard = false }) { Text("🐾") }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = stringResource(R.string.personal_info),
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            CustomTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = if (it.isBlank()) context.getString(R.string.val_name_required) else ""
                },
                label = stringResource(R.string.name),
                error = nameError
            )

            CustomTextField(
                value = type,
                onValueChange = {
                    type = it
                    typeError = if (it.isBlank()) context.getString(R.string.val_type_required) else ""
                },
                label = stringResource(R.string.type),
                error = typeError
            )

            CustomTextField(
                value = breed,
                onValueChange = { breed = it },
                label = stringResource(R.string.breed)
            )

            CustomTextField(
                value = gender,
                onValueChange = {
                    gender = it
                    genderError = if (it.isBlank()) context.getString(R.string.val_gender_required) else ""
                },
                label = stringResource(R.string.gender),
                error = genderError
            )

            CustomTextField(
                value = age,
                onValueChange = {
                    if (it.all { c -> c.isDigit() }) {
                        age = it
                        ageError = when {
                            it.isBlank()    -> context.getString(R.string.val_age_required)
                            it.toInt() > 50 -> context.getString(R.string.val_age_max)
                            else            -> ""
                        }
                    }
                },
                label = stringResource(R.string.age),
                error = ageError,
                keyboardType = KeyboardType.Number
            )

            CustomTextField(
                value = description,
                onValueChange = { description = it },
                label = stringResource(R.string.description)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(text = stringResource(R.string.health_info),
                fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            CustomTextField(value = vaccines,   onValueChange = { vaccines = it },   label = stringResource(R.string.vaccines))
            CustomTextField(value = diseases,   onValueChange = { diseases = it },   label = stringResource(R.string.diseases))
            CustomTextField(value = medications,onValueChange = { medications = it },label = stringResource(R.string.medications))
            CustomTextField(value = allergies,  onValueChange = { allergies = it },  label = stringResource(R.string.allergies))

            CustomTextField(
                value = lastVisit,
                onValueChange = {
                    if (it.length <= 10 && it.all { c -> c.isDigit() || c == '/' }) {
                        lastVisit = it
                        lastVisitError = when {
                            it.isNotBlank() && !Regex("""\d{2}/\d{2}/\d{4}""").matches(it) ->
                                context.getString(R.string.val_date_format)
                            else -> ""
                        }
                    }
                },
                label = stringResource(R.string.last_visit),
                error = lastVisitError,
                placeholder = stringResource(R.string.val_date_placeholder)
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    nameError   = if (name.isBlank())   context.getString(R.string.val_name_required)   else ""
                    typeError   = if (type.isBlank())   context.getString(R.string.val_type_required)   else ""
                    genderError = if (gender.isBlank()) context.getString(R.string.val_gender_required) else ""
                    ageError    = if (age.isBlank())    context.getString(R.string.val_age_required)    else ""
                    lastVisitError = if (lastVisit.isNotBlank() &&
                        !Regex("""\d{2}/\d{2}/\d{4}""").matches(lastVisit))
                        context.getString(R.string.val_date_format) else ""

                    if (nameError.isNotEmpty() || typeError.isNotEmpty() ||
                        genderError.isNotEmpty() || ageError.isNotEmpty() ||
                        lastVisitError.isNotEmpty()) {
                        Toast.makeText(context,
                            context.getString(R.string.val_fix_errors), Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    onSave(Pet(
                        nombre = name, tipo = type, raza = breed, genero = gender, edad = age,
                        descripcion = description, fotoUri = selectedImageUri?.toString() ?: "",
                        vacunas = vaccines, enfermedades = diseases, medicamentos = medications,
                        alergias = allergies, ultimaConsulta = lastVisit
                    ))

                    showSavedCard = true
                    name = ""; type = ""; breed = ""; gender = ""; age = ""
                    description = ""; vaccines = ""; diseases = ""
                    medications = ""; allergies = ""; lastVisit = ""
                    nameError = ""; typeError = ""; genderError = ""; ageError = ""; lastVisitError = ""
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = stringResource(R.string.save_pet), style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String = "",
    placeholder: String = "",
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value           = value,
            onValueChange   = onValueChange,
            label           = { Text(label) },
            placeholder     = if (placeholder.isNotEmpty()) {{ Text(placeholder, color = Color.Gray) }} else null,
            isError         = error.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            modifier        = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape           = RoundedCornerShape(12.dp)
        )
        if (error.isNotEmpty()) {
            Text(text = error, color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp))
        }
    }
}

//comentario