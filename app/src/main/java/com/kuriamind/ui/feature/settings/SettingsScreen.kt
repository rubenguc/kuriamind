package com.kuriamind.ui.feature.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kuriamind.KuriamindApplication

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val currentLang = KuriamindApplication.loadAppLanguage(context)
    var selectedLang by remember { mutableStateOf(currentLang) }
    var expanded by remember { mutableStateOf(false) }

    val languages = listOf(
        "en" to "English",
        "es" to "Español",
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1117))
            .systemBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // ── Header ────────────────────────────────────────────────
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "Configure your preferences",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
        )

        Spacer(Modifier.height(28.dp))

        // ── Language section ──────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Language",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.weight(1f),
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
            ) {
                OutlinedTextField(
                    value = languages.first { it.first == selectedLang }.second,
                    onValueChange = {},
                    readOnly = true,
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFF58A6FF),
                        unfocusedBorderColor = Color(0xFF444444),
                        cursorColor = Color.White,
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .menuAnchor()
                        .width(140.dp),
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                ) {
                    languages.forEach { (code, label) ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = label,
                                    color = if (code == selectedLang) Color(0xFF58A6FF) else Color.White,
                                    fontWeight = if (code == selectedLang) FontWeight.SemiBold else FontWeight.Normal,
                                )
                            },
                            onClick = {
                                expanded = false
                                if (selectedLang != code) {
                                    selectedLang = code
                                    KuriamindApplication.setAppLanguage(context, code)
                                    recreateApp(context)
                                }
                            },
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // ── Theme section ─────────────────────────────────────────
        Text(
            text = "Appearance",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
        Spacer(Modifier.height(12.dp))

        val themeModes = listOf(
            "system" to "System default",
            "light" to "Light",
            "dark" to "Dark",
        )
        val currentTheme = KuriamindApplication.loadAppTheme(context)
        var selectedTheme by remember { mutableStateOf(currentTheme) }

        themeModes.forEach { (mode, label) ->
            ThemeOption(
                label = label,
                selected = selectedTheme == mode,
                onClick = {
                    if (selectedTheme != mode) {
                        selectedTheme = mode
                        KuriamindApplication.setAppTheme(context, mode)
                        recreateApp(context)
                    }
                },
            )
            Spacer(Modifier.height(8.dp))
        }

        Spacer(Modifier.height(32.dp))

        // ── Divider ───────────────────────────────────────────────
        HorizontalDivider(
            color = Color(0xFF2D2D2D),
            thickness = 1.dp,
        )

        Spacer(Modifier.height(24.dp))

        // ── About section ─────────────────────────────────────────
        Text(
            text = "About this project",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
        Spacer(Modifier.height(12.dp))

        Text(
            text = "This is a personal open-source project. I built it because I got tired of apps that offer similar functionality but require a subscription or too many steps to set up. Kuria Mind is free, no ads, and always will be.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFCCCCCC),
            lineHeight = 22.sp,
        )

        Spacer(Modifier.height(12.dp))

        // GitHub link
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rubenguc/kuriamind"))
                    context.startActivity(intent)
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "github.com/rubenguc/kuriamind",
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline,
                ),
                color = Color(0xFF58A6FF),
            )
        }

        Spacer(Modifier.height(24.dp))

        // ── Credits section ───────────────────────────────────────
        Text(
            text = "Credits",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
        Spacer(Modifier.height(8.dp))

        Text(
            text = "Logo and design by",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFFCCCCCC),
        )
        Spacer(Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/migsstudio_19"))
                    context.startActivity(intent)
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "@migsstudio_19",
                style = MaterialTheme.typography.bodyMedium.copy(
                    textDecoration = TextDecoration.Underline,
                ),
                color = Color(0xFF58A6FF),
            )
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun ThemeOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor = if (selected) Color(0xFF1D71B8).copy(alpha = 0.1f) else Color(0xFF1A1A1A)
    val borderColor = if (selected) Color(0xFF58A6FF) else Color(0xFF333333)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.dp, borderColor),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.weight(1f),
            )
            if (selected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF58A6FF),
                    modifier = Modifier.size(20.dp),
                )
            }
        }
    }
}

private fun recreateApp(context: android.content.Context) {
    val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
    (context as? Activity)?.finish()
}
