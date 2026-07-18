package com.kuriamind.ui.feature.blocks

import android.app.Application
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuriamind.domain.model.Block
import com.kuriamind.domain.repository.BlockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

data class BlockFormState(
    val name: String = "",
    val blockedApps: List<String> = emptyList(),
    val blockApps: Boolean = true,
    val blockNotifications: Boolean = true,
    val addTimer: Boolean = false,
    val startTime: String = "",
    val endTime: String = "",
)

data class InstalledAppItem(
    val packageName: String,
    val appName: String,
    val icon: Drawable?,
)

@HiltViewModel
class BlockViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BlockRepository,
    private val application: Application,
) : ViewModel() {

    private val blockId: String? = savedStateHandle.get<String?>("blockId")?.takeIf { it.isNotBlank() }

    private val _formState = MutableStateFlow(BlockFormState())
    val formState: StateFlow<BlockFormState> = _formState.asStateFlow()

    private val _installedApps = MutableStateFlow<List<InstalledAppItem>>(emptyList())
    val installedApps: StateFlow<List<InstalledAppItem>> = _installedApps.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode.asStateFlow()

    val isEditing: Boolean get() = blockId != null

    init {
        loadInstalledApps()
        if (blockId != null) {
            loadBlock(blockId)
        }
    }

    private fun loadBlock(id: String) {
        viewModelScope.launch {
            val block = repository.getById(id) ?: return@launch
            _isEditMode.value = true
            _formState.value = BlockFormState(
                name = block.name,
                blockedApps = block.blockedApps,
                blockApps = block.blockApps,
                blockNotifications = block.blockNotifications,
                addTimer = block.startTime.isNotEmpty() || block.endTime.isNotEmpty(),
                startTime = block.startTime,
                endTime = block.endTime,
            )
        }
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
                .map { info ->
                    InstalledAppItem(
                        packageName = info.packageName,
                        appName = pm.getApplicationLabel(info).toString(),
                        icon = info.loadIcon(pm),
                    )
                }
                .sortedBy { it.appName }
        }
    }

    fun updateName(name: String) {
        _formState.value = _formState.value.copy(name = name)
    }

    fun updateBlockedApps(packages: List<String>) {
        _formState.value = _formState.value.copy(blockedApps = packages)
    }

    fun toggleBlockApps() {
        _formState.value = _formState.value.copy(blockApps = !_formState.value.blockApps)
    }

    fun toggleBlockNotifications() {
        _formState.value = _formState.value.copy(blockNotifications = !_formState.value.blockNotifications)
    }

    fun toggleTimer() {
        _formState.value = _formState.value.copy(addTimer = !_formState.value.addTimer)
    }

    fun updateStartTime(time: String) {
        _formState.value = _formState.value.copy(startTime = time)
    }

    fun updateEndTime(time: String) {
        _formState.value = _formState.value.copy(endTime = time)
    }

    fun save(onSuccess: () -> Unit) {
        val state = _formState.value
        if (state.name.isBlank()) {
            Log.w(TAG, "save() cancelled: name is blank")
            return
        }

        viewModelScope.launch {
            _isSaving.value = true
            try {
                val existingBlockId = blockId
                val targetId = existingBlockId ?: UUID.randomUUID().toString()
                val block = Block(
                    id = targetId,
                    name = state.name,
                    blockedApps = state.blockedApps,
                    blockApps = state.blockApps,
                    blockNotifications = state.blockNotifications,
                    isActive = if (existingBlockId != null) {
                        repository.getById(existingBlockId)?.isActive ?: true
                    } else {
                        true
                    },
                    startTime = if (state.addTimer) state.startTime else "",
                    endTime = if (state.addTimer) state.endTime else "",
                )

                Log.d(TAG, "save() starting: id=$targetId name=${block.name}")

                if (existingBlockId != null) {
                    repository.update(block)
                    Log.d(TAG, "save() updated existing block")
                } else {
                    repository.save(block)
                    Log.d(TAG, "save() saved new block")
                }

                Log.d(TAG, "save() calling onSuccess")
                onSuccess()
                Log.d(TAG, "save() onSuccess returned")
            } catch (e: Exception) {
                Log.e(TAG, "save() failed", e)
            } finally {
                _isSaving.value = false
            }
        }
    }

    companion object {
        private const val TAG = "BlockViewModel"
    }
}
