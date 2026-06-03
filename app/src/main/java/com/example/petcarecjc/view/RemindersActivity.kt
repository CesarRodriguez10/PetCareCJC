package com.example.petcarecjc.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.petcarecjc.R
import com.example.petcarecjc.model.Reminder
import com.example.petcarecjc.model.ReminderType
import com.example.petcarecjc.viewmodel.ReminderViewModel
import java.text.SimpleDateFormat
import java.util.*

class RemindersActivity : ComponentActivity() {

    private val viewModel: ReminderViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val petId   = intent.getStringExtra("petId")   ?: ""
        val petName = intent.getStringExtra("petName") ?: ""
        setContent {
            RemindersScreen(petId = petId, petName = petName, viewModel = viewModel, onBack = { finish() })
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RemindersScreen(petId: String, petName: String, viewModel: ReminderViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val reminders by viewModel.reminders.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var reminderToEdit by remember { mutableStateOf<Reminder?>(null) }

    LaunchedEffect(petId) {
        if (petId.isNotEmpty()) viewModel.loadRemindersForPet(petId)
        else viewModel.loadReminders()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {

        Surface(color = MaterialTheme.colorScheme.primary, shadowElevation = 4.dp) {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onBack) {
                    Text(stringResource(R.string.reminder_back), color = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text("🔔 ${stringResource(R.string.btn_reminders)}", color = Color.White,
                    fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.weight(1f))
                TextButton(onClick = { reminderToEdit = null; showDialog = true }) {
                    Text(stringResource(R.string.reminder_new_btn), color = Color.White)
                }
            }
        }

        if (petName.isNotEmpty()) {
            Text(text = stringResource(R.string.reminder_pet_label, petName),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        }

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (reminders.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                    contentAlignment = Alignment.Center) {
                    Text(stringResource(R.string.reminder_none), textAlign = TextAlign.Center, color = Color.Gray)
                }
            } else {
                reminders.forEach { reminder ->
                    ReminderCard(
                        reminder = reminder,
                        onDelete = { viewModel.deleteReminder(context, reminder) },
                        onToggle = { active -> viewModel.toggleReminder(context, reminder, active) },
                        onEdit   = { reminderToEdit = reminder; showDialog = true }
                    )
                }
            }
        }
    }

    if (showDialog) {
        ReminderDialog(
            petId    = petId,
            petName  = petName,
            existing = reminderToEdit,
            onDismiss = { showDialog = false },
            onConfirm = { reminder ->
                if (reminderToEdit != null) {
                    viewModel.updateReminder(context, reminder) { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                } else {
                    viewModel.addReminder(context, reminder) { msg ->
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
                showDialog = false
            }
        )
    }
}

@Composable
fun ReminderCard(reminder: Reminder, onDelete: () -> Unit, onToggle: (Boolean) -> Unit, onEdit: () -> Unit) {
    val typeLabel = when (reminder.type) {
        ReminderType.VACUNA.name       -> stringResource(R.string.type_vaccine)
        ReminderType.MEDICAMENTO.name  -> stringResource(R.string.type_medication)
        ReminderType.CITA_VET.name     -> stringResource(R.string.type_vet)
        ReminderType.ALIMENTACION.name -> stringResource(R.string.type_food)
        ReminderType.CUMPLEANOS.name   -> stringResource(R.string.type_birthday)
        else -> reminder.type
    }

    val timeLabel = if (reminder.isDaily) {
        stringResource(R.string.reminder_daily_label,
            "%02d:%02d".format(reminder.dailyHour, reminder.dailyMinute))
    } else {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(reminder.triggerMillis))
    }

    Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (reminder.active) Color.White else Color(0xFFEEEEEE))) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(typeLabel, fontWeight = FontWeight.Bold, fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary)
                Text(reminder.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(reminder.message, color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text("⏰ $timeLabel", fontSize = 12.sp, color = Color(0xFF009688))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Switch(checked = reminder.active, onCheckedChange = { onToggle(it) })
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = stringResource(R.string.edit),
                        tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete),
                        tint = Color.Red)
                }
            }
        }
    }
}

@Composable
fun ReminderDialog(petId: String, petName: String, existing: Reminder?,
                   onDismiss: () -> Unit, onConfirm: (Reminder) -> Unit) {
    val context   = LocalContext.current
    val isEditing = existing != null

    var selectedType by remember { mutableStateOf(
        if (existing != null) ReminderType.valueOf(existing.type) else ReminderType.VACUNA
    )}
    var customTitle  by remember { mutableStateOf(existing?.title   ?: "") }
    var customMsg    by remember { mutableStateOf(existing?.message ?: "") }
    var isDaily      by remember { mutableStateOf(existing?.isDaily ?: false) }
    var dailyHour    by remember { mutableIntStateOf(existing?.dailyHour   ?: 8) }
    var dailyMinute  by remember { mutableIntStateOf(existing?.dailyMinute ?: 0) }
    var dateMillis   by remember { mutableLongStateOf(
        if (existing != null && existing.triggerMillis > 0) existing.triggerMillis
        else System.currentTimeMillis() + 3_600_000L
    )}
    val dateLabel = remember(dateMillis) {
        SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(dateMillis))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (isEditing) stringResource(R.string.reminder_edit)
            else stringResource(R.string.reminder_new), fontWeight = FontWeight.Bold)
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)) {

                Text(stringResource(R.string.reminder_type), fontWeight = FontWeight.SemiBold)

                val types = listOf(
                    ReminderType.VACUNA       to stringResource(R.string.type_vaccine_opt),
                    ReminderType.CITA_VET     to stringResource(R.string.type_vet_opt),
                    ReminderType.ALIMENTACION to stringResource(R.string.type_food_opt),
                    ReminderType.CUMPLEANOS   to stringResource(R.string.type_birthday_opt)
                )
                types.forEach { (type, label) ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedType == type,
                            onClick  = { selectedType = type; isDaily = type == ReminderType.ALIMENTACION }
                        )
                        Text(label)
                    }
                }

                HorizontalDivider()

                OutlinedTextField(value = customTitle, onValueChange = { customTitle = it },
                    label = { Text(stringResource(R.string.reminder_title_hint)) },
                    modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = customMsg, onValueChange = { customMsg = it },
                    label = { Text(stringResource(R.string.reminder_detail_hint)) },
                    modifier = Modifier.fillMaxWidth())

                HorizontalDivider()

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isDaily, onCheckedChange = { isDaily = it })
                    Text(stringResource(R.string.reminder_daily))
                }

                if (isDaily) {
                    Text(stringResource(R.string.reminder_daily_hour), fontWeight = FontWeight.SemiBold)
                    Button(onClick = {
                        TimePickerDialog(context, { _, h, m ->
                            dailyHour = h; dailyMinute = m
                        }, dailyHour, dailyMinute, true).show()
                    }) { Text("⏰ %02d:%02d".format(dailyHour, dailyMinute)) }
                } else {
                    Text(stringResource(R.string.reminder_exact_date), fontWeight = FontWeight.SemiBold)
                    Button(onClick = {
                        val cal = Calendar.getInstance().apply { timeInMillis = dateMillis }
                        DatePickerDialog(context, { _, y, m, d ->
                            TimePickerDialog(context, { _, h, min ->
                                val c = Calendar.getInstance()
                                c.set(Calendar.YEAR, y); c.set(Calendar.MONTH, m)
                                c.set(Calendar.DAY_OF_MONTH, d); c.set(Calendar.HOUR_OF_DAY, h)
                                c.set(Calendar.MINUTE, min); c.set(Calendar.SECOND, 0)
                                c.set(Calendar.MILLISECOND, 0)
                                dateMillis = c.timeInMillis
                            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
                    }) { Text("📅 $dateLabel") }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val title = customTitle.ifEmpty {
                    when (selectedType) {
                        ReminderType.VACUNA       -> "💉 ${context.getString(R.string.type_vaccine)} $petName"
                        ReminderType.MEDICAMENTO  -> "💊 ${context.getString(R.string.type_medication)} $petName"
                        ReminderType.CITA_VET     -> "🏥 ${context.getString(R.string.type_vet)} $petName"
                        ReminderType.ALIMENTACION -> "🍖 $petName"
                        ReminderType.CUMPLEANOS   -> "🎂 $petName"
                    }
                }
                onConfirm(Reminder(
                    id = existing?.id ?: "", petId = petId, petName = petName,
                    type = selectedType.name, title = title,
                    message = customMsg.ifEmpty { title },
                    isDaily = isDaily, dailyHour = dailyHour, dailyMinute = dailyMinute,
                    triggerMillis = if (isDaily) 0L else dateMillis,
                    active = existing?.active ?: true
                ))
            }) {
                Text(if (isEditing) stringResource(R.string.reminder_save_changes)
                else stringResource(R.string.reminder_save_btn))
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}