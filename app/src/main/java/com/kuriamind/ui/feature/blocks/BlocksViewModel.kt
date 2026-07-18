package com.kuriamind.ui.feature.blocks

import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuriamind.domain.model.Block
import com.kuriamind.domain.repository.BlockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "BlocksViewModel"

@HiltViewModel
class BlocksViewModel @Inject constructor(
    private val repository: BlockRepository,
    private val application: Application,
) : ViewModel() {

    val blocks: StateFlow<List<Block>> = repository.observeAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        .also { Log.d(TAG, "blocks StateFlow initialized") }

    private val _installedApps = MutableStateFlow<Map<String, InstalledAppItem>>(emptyMap())
    val installedApps: StateFlow<Map<String, InstalledAppItem>> = _installedApps.asStateFlow()

    private val _blockToDelete = MutableStateFlow<Block?>(null)
    val blockToDelete: StateFlow<Block?> = _blockToDelete.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        Log.d(TAG, "BlocksViewModel created")
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        viewModelScope.launch {
            val pm = application.packageManager
            val apps = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                pm.getInstalledApplications(
                    PackageManager.ApplicationInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                pm.getInstalledApplications(PackageManager.GET_META_DATA)
            }

            _installedApps.value = apps
                .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
                .filter { it.packageName != "com.kuriamind" }
                .associate { info ->
                    info.packageName to InstalledAppItem(
                        packageName = info.packageName,
                        appName = pm.getApplicationLabel(info).toString(),
                        icon = info.loadIcon(pm),
                    )
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "BlocksViewModel cleared")
    }

    fun setBlockToDelete(block: Block?) {
        _blockToDelete.value = block
    }

    fun confirmDelete() {
        val block = _blockToDelete.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d(TAG, "Deleting block: ${block.id}")
                repository.deleteById(block.id)
                _blockToDelete.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleActive(block: Block) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Toggling block: ${block.id}")
                repository.toggleActive(block.id)
            } catch (_: Exception) {
            }
        }
    }
}
