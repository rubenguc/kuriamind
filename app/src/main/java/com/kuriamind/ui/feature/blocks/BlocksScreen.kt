package com.kuriamind.ui.feature.blocks

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kuriamind.ui.feature.blocks.components.BlockItem
import com.kuriamind.ui.feature.blocks.components.ConfirmDeleteDialog

private const val TAG = "BlocksScreen"

@Composable
fun BlocksScreen(
    onAddBlock: () -> Unit,
    onEditBlock: (String) -> Unit,
    viewModel: BlocksViewModel = hiltViewModel(),
) {
    val blocks by viewModel.blocks.collectAsStateWithLifecycle()
    val installedApps by viewModel.installedApps.collectAsStateWithLifecycle()
    val blockToDelete by viewModel.blockToDelete.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(blocks.size) {
        Log.d(TAG, "Composed with ${blocks.size} blocks")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0D1117)),
        ) {
            // Header
            Text(
                text = "Blocks",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 8.dp),
            )

            if (blocks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Outlined.Inbox,
                            contentDescription = null,
                            tint = Color(0xFF444444),
                            modifier = Modifier.size(64.dp),
                        )
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = "No blocks yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color(0xFF666666),
                            fontWeight = FontWeight.SemiBold,
                        )
                        Spacer(Modifier.height(6.dp))
                        Text(
                            text = "Tap + to create your first focus block",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF444444),
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 20.dp, end = 20.dp, top = 8.dp, bottom = 88.dp,
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(blocks, key = { it.id }) { block ->
                        BlockItem(
                            block = block,
                            installedApps = installedApps,
                            onEdit = { onEditBlock(block.id) },
                            onDelete = { viewModel.setBlockToDelete(block) },
                            onToggleActive = { viewModel.toggleActive(block) },
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = onAddBlock,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            containerColor = Color(0xFF1D71B8),
            contentColor = Color.White
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add block")
        }

        blockToDelete?.let { block ->
            ConfirmDeleteDialog(
                blockName = block.name,
                isLoading = isLoading,
                onConfirm = { viewModel.confirmDelete() },
                onDismiss = { viewModel.setBlockToDelete(null) },
            )
        }
    }
}
