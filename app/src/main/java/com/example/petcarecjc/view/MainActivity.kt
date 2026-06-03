package com.example.petcarecjc.view

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.petcarecjc.R
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(this,
                    getString(R.string.notif_enable_prompt), Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                Toast.makeText(this,
                    getString(R.string.notif_alarm_prompt), Toast.LENGTH_LONG).show()
                startActivity(Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:$packageName")
                })
            }
        }

        val username = intent.getStringExtra("USERNAME") ?: "Usuario"

        setContent {
            MainScreen(
                username         = username,
                onRegisterClick  = { startActivity(Intent(this, RegisterPetActivity::class.java)) },
                onViewPetsClick  = { startActivity(Intent(this, PetDetailActivity::class.java)) },
                onRemindersClick = { startActivity(Intent(this, RemindersActivity::class.java)) },
                onSignOutClick   = {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginUser::class.java))
                    finish()
                },
                onLanguageChange = { langCode -> setLocale(langCode) }
            )
        }
    }

    @Suppress("DEPRECATION")
    private fun setLocale(langCode: String) {
        val locale = Locale.forLanguageTag(langCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        recreate()
    }
}

@Composable
fun MainScreen(
    username: String,
    onRegisterClick: () -> Unit,
    onViewPetsClick: () -> Unit,
    onRemindersClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onLanguageChange: (String) -> Unit
) {
    var showLangMenu by remember { mutableStateOf(false) }

    Column(
        modifier            = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {
            TextButton(
                onClick  = { showLangMenu = true },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) { Text("🌐 Idioma / Language") }

            DropdownMenu(
                expanded         = showLangMenu,
                onDismissRequest = { showLangMenu = false }
            ) {
                DropdownMenuItem(
                    text    = { Text("🇨🇴 Español") },
                    onClick = { showLangMenu = false; onLanguageChange("es") }
                )
                DropdownMenuItem(
                    text    = { Text("🇺🇸 English") },
                    onClick = { showLangMenu = false; onLanguageChange("en") }
                )
            }
        }

        Text(
            text  = stringResource(R.string.welcome_user, username),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter            = painterResource(id = R.drawable.pets),
            contentDescription = stringResource(R.string.pet_image),
            modifier           = Modifier.size(220.dp).clip(RoundedCornerShape(20.dp))
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(text = stringResource(R.string.care_text), style = MaterialTheme.typography.headlineSmall)
        Text(text = stringResource(R.string.desc_text))

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
            Text(stringResource(R.string.btn_register))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = onViewPetsClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
            Text(stringResource(R.string.btn_view))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = onRemindersClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
            Text(" ${stringResource(R.string.btn_reminders)}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onSignOutClick, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.sign_out), color = Color.Red)
        }
    }
}