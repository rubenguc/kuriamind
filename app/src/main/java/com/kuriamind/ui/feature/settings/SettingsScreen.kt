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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val currentLang = KuriamindApplication.loadAppLanguage(context)
    var selectedLang by remember { mutableStateOf(currentLang) }

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
        Text(
            text = "Language",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
        )
        Spacer(Modifier.height(12.dp))

        LanguageOption(
            label = "English",
            selected = selectedLang == "en",
            onClick = {
                if (selectedLang != "en") {
                    selectedLang = "en"
                    KuriamindApplication.setAppLanguage(context, "en")
                    recreateApp(context)
                }
            },
        )
        Spacer(Modifier.height(8.dp))
        LanguageOption(
            label = "Español",
            selected = selectedLang == "es",
            onClick = {
                if (selectedLang != "es") {
                    selectedLang = "es"
                    KuriamindApplication.setAppLanguage(context, "es")
                    recreateApp(context)
                }
            },
        )

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
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/migueldelcarpio_25/"))
                    context.startActivity(intent)
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "@migueldelcarpio_25",
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
private fun LanguageOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor = if (selected) Color(0xFF58A6FF) else Color(0xFF333333)
    val bgColor = if (selected) Color(0xFF1D71B8).copy(alpha = 0.1f) else Color(0xFF1A1A1A)

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
