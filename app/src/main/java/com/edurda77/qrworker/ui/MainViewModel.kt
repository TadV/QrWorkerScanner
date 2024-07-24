package com.edurda77.qrworker.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.query
import com.edurda77.qrworker.domain.model.LocalUser
import com.edurda77.qrworker.domain.model.TechOperation
import com.edurda77.qrworker.domain.repository.WorkRepository
import com.edurda77.qrworker.domain.utils.Resource
import com.edurda77.qrworker.domain.utils.UNKNOWN_ERROR
import com.edurda77.qrworker.domain.utils.checkConflicts
import com.edurda77.qrworker.domain.utils.checkConflictsOperations
import com.edurda77.qrworker.domain.utils.compareLists
import com.edurda77.qrworker.domain.utils.filterLists
import com.edurda77.qrworker.domain.utils.getCurrentDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workRepository: WorkRepository
) : ViewModel() {
    private var _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()
    private val _shadowTechOperations = MutableStateFlow<List<TechOperation>>(emptyList())

    init {
        checkSavedUser()
        loadLocalTechOperations()
    }

    fun onEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            is MainEvent.TryAuthorization -> {
                _state.value.copy(
                    message = ""
                )
                    .updateStateUI()
                if (mainEvent.userNumber.length == 13) {
                    viewModelScope.launch {
                        when (val result = workRepository.getWorkerFIO(mainEvent.userNumber)) {
                            is Resource.Error -> {

                            }

                            is Resource.Success -> {
                                if (result.data != null) {
                                    println(result.data)

                                    result.data.dataServer?.let {
                                        _state.value.copy(
                                            appState = AppState.WorkScan(WorkState.ReadyScanState),
                                            user = it.workerCode,
                                            userName = it.workerFio,
                                        )
                                            .updateStateUI()
                                        workRepository.saveCurrentUser(LocalUser(it.workerCode, it.workerFio))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            is MainEvent.LogOut -> {
                viewModelScope.launch {
                    workRepository.logOut()

                    _state.value.copy(
                        appState = AppState.Authorization(AuthorizationState.EnterState),
                    )
                        .updateStateUI()
                }
            }

            is MainEvent.ChangeAppState -> {
                _state.value.copy(
                    appState = mainEvent.appState,
                )
                    .updateStateUI()
            }

            is MainEvent.ScanOpzs -> {
                _state.value.copy(
                    appState = AppState.WorkScan(WorkState.ReadyScanState),
                    opzs = mainEvent.code,
                    message = "",
                )
                    .updateStateUI()
                loadTechOperations()

            }

            MainEvent.UploadForce -> {
                viewModelScope.launch {
                    workRepository.uploadPerDayData()
                }
            }

            is MainEvent.SelectTechOperation -> {
                viewModelScope.launch {
                    when (val resultLoad =
                        workRepository.getCodeById(mainEvent.techOperation.id)) {
                        is Resource.Error -> {

                        }

                        is Resource.Success -> {
                            if (resultLoad.data != null) {
                                workRepository.deleteCodeById(mainEvent.techOperation.id)
                                //////////////
                            } else {
                                workRepository.insertQrCode(
                                    id = mainEvent.techOperation.id,
                                    techOperation = mainEvent.techOperation.techOperation,
                                    productionReport = mainEvent.techOperation.productionReport,
                                    timeScan = getCurrentDateTime(),
                                    user = _state.value.user
                                )
                                ///////
                            }
                        }
                    }
                    _shadowTechOperations.value = updateVisibleTechOperations(
                        techOperation = mainEvent.techOperation,
                        oldTechOperations = _shadowTechOperations.value
                    )

                    val queries = _state.value.query.split("/").map { it.trim() }
                    _state.value.copy(
                        techOperations = _shadowTechOperations.value,
                        visibleTechOperations = _shadowTechOperations.value.filterLists(queries),
                    )
                        .updateStateUI()
                }
            }

            MainEvent.UploadSelectedTechOperations -> {
                val selectedOperations =
                    _state.value.techOperations.filter { it.currentUser == _state.value.user }
                viewModelScope.launch {
                    when (workRepository.updateTechOperations(
                        techOperations = selectedOperations,
                    )) {
                        is Resource.Error -> {

                        }

                        is Resource.Success -> {
                            loadTechOperations()
                            /* val updatedList = _shadowTechOperations.value.toMutableList()
                             for (i in updatedList.indices) {
                                 if (updatedList[i].codeUser==_state.value.user) {
                                     updatedList[i] = updatedList[i].copy(isUploadedThisUser = true)
                                 }
                             }
                             _shadowTechOperations.value = updatedList
                             _state.value.copy(
                                 techOperations = _shadowTechOperations.value,
                             )
                                 .updateStateUI()*/
                        }
                    }
                }
            }

            MainEvent.GetApprovedTechOperationPrevDay -> {
                // Not implemented
            }

            MainEvent.GetApprovedTechOperationCurrentMonth -> {
                // Not implemented
            }

            is MainEvent.OnSearch -> {
                val queries = mainEvent.query.split("/").map { it.trim() }
                _state.value.copy(
                    query = mainEvent.query,
                    visibleTechOperations = _shadowTechOperations.value.filterLists(queries)
                )
                    .updateStateUI()

            }

            MainEvent.OnFilter -> {
                val currentQueries =  _state.value.filtersQueries
                _state.value.copy(
                    techOperations = _shadowTechOperations.value.compareLists(currentQueries)
                )
                    .updateStateUI()
            }

            is MainEvent.AddItemInFilteredList -> {
                val current = _state.value.filtersQueries.toMutableList()
                current.add("")
                _state.value.copy(
                    filtersQueries = current,
                )
                    .updateStateUI()
            }
            is MainEvent.DeleteItemFromFilterList -> {
                val current = _state.value.filtersQueries.toMutableList()
                current.removeAt(mainEvent.index)
                _state.value.copy(
                    filtersQueries = current,
                )
                    .updateStateUI()
            }
            is MainEvent.UpdateFilteredList -> {
                val current = _state.value.filtersQueries.toMutableList()
                Log.d("test werty", "view model index - ${mainEvent.index}, item ${mainEvent.query}")
                current[mainEvent.index] = mainEvent.query
                _state.value.copy(
                    filtersQueries = current,
                )
                    .updateStateUI()
            }
        }
    }

    private fun checkSavedUser() {
        runBlocking {
            when (val result = workRepository.getCurrentUser()) {
                is Resource.Error -> {

                }

                is Resource.Success -> {
                    result.data?.let {
                        Log.d("Load Saved User", it.toString())
                        _state.value.copy(
                            appState = AppState.WorkScan(WorkState.ReadyScanState),
                            user = it.userCode,
                            userName = it.userName
                        )
                            .updateStateUI()
                    }
                }
            }
        }
    }

    private fun loadLocalTechOperations() {
        viewModelScope.launch {
            workRepository.getAllLocalOperations().collect { result ->
                when (result) {
                    is Resource.Error -> {

                    }

                    is Resource.Success -> {
                        _state.value.copy(
                            localTechOperations = result.data ?: emptyList(),
                        )
                            .updateStateUI()
                    }
                }
            }
        }
    }

    private fun loadTechOperations() {
        viewModelScope.launch {
            Log.d("loadTechOperations Before", _state.value.toString())
            val result = workRepository.getTechOperations(
                currentUserCode = _state.value.user,
                currentUserName = _state.value.userName,
                numberOPZS = _state.value.opzs
            )
            when (result) {
                is Resource.Error -> {
                    Log.d("loadTechOperations Resource.Error", result.toString())
                    _shadowTechOperations.value = emptyList()

                    _state.value.copy(
                        message = result.message ?: UNKNOWN_ERROR,
                        techOperations = emptyList(),
                        visibleTechOperations = emptyList(),
                    )
                        .updateStateUI()
                }

                is Resource.Success -> {
                    Log.d("loadTechOperations Resource.Success", result.data.toString())
                    _shadowTechOperations.value = result.data ?: emptyList()

                    val queries = _state.value.query.split("/").map { it.trim() }
                    _state.value.copy(
                        visibleTechOperations = _shadowTechOperations.value.filterLists(queries),
                        // appState = AppState.WorkScan(WorkState.ReadyScanState),
                        techOperations = result.data ?: emptyList(),
                        isConflict = checkConflicts(
                            remoteOperations = result.data ?: emptyList(),
                            localOperations = _state.value.localTechOperations
                        ),
                        conflictedTechOperations = checkConflictsOperations(
                            remoteOperations = result.data ?: emptyList(),
                            localOperations = _state.value.localTechOperations
                        )
                    )
                        .updateStateUI()
                }
            }
        }
    }

    private fun updateVisibleTechOperations(
        techOperation: TechOperation,
        oldTechOperations: List<TechOperation>
    ): List<TechOperation> {
        val index =
            oldTechOperations.indexOf(oldTechOperations.firstOrNull { it.id == techOperation.id })
        Log.d("updateVisibleTechOperations", oldTechOperations.toString())
        Log.d("updateVisibleTechOperations", techOperation.toString())
        Log.d("updateVisibleTechOperations", index.toString())
        if (index >= 0) {
            val oldTechOperation = _state.value.techOperations[index]
            val newTechOperation =
                if (oldTechOperation.workerCode!!.isBlank()) oldTechOperation.copy(
                    workerCode = _state.value.user,
                    currentUser = _state.value.user
                ) else oldTechOperation.copy(
                    workerCode = "",
                    currentUser = _state.value.user
                )
            val oldOperations = _state.value.techOperations.toMutableList()
            oldOperations.removeAt(index)
            oldOperations.add(index, newTechOperation)
            return oldOperations
        }
        return oldTechOperations
    }


    private fun MainState.updateStateUI() {
        _state.update {
            this
        }
    }
}