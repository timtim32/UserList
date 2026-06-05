package com.timtim.userlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timtim.userlist.data.model.User
import com.timtim.userlist.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class UserListUiState {
    object Loading : UserListUiState()
    data class Success(val users: List<User>) : UserListUiState()
    data class Error(val message: String) : UserListUiState()
}

class UserListViewModel(private val repository: UserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UserListUiState>(UserListUiState.Loading)
    val uiState: StateFlow<UserListUiState> = _uiState.asStateFlow()

    private val _selectedUser = MutableStateFlow<User?>(null)
    val selectedUser: StateFlow<User?> = _selectedUser.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            _uiState.value = UserListUiState.Loading
            try {
                val users = repository.getUsers()
                if (users.isEmpty()) {
                    _uiState.value = UserListUiState.Error("No users found")
                } else {
                    _uiState.value = UserListUiState.Success(users)
                }
            } catch (e: Exception) {
                _uiState.value = UserListUiState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun selectUser(user: User) {
        _selectedUser.value = user
    }
}
