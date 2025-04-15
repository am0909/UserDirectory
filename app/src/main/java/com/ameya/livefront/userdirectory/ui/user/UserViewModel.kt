package com.ameya.livefront.userdirectory.ui.user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ameya.livefront.userdirectory.domain.repository.UserRepository
import com.ameya.livefront.userdirectory.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
): ViewModel()  {
    var state by mutableStateOf(UserState())
        private set

    init {
        getUserList("", true)
    }

    fun onEvent(event: UserEvent) {
        when(event) {
            UserEvent.OnRefresh -> {
                getUserList("", true)
            }

            is UserEvent.OnSearchQueryChange -> {
                getUserList(query = event.query, requestRemote = false)
            }
        }
    }

    private fun getUserList(query: String, requestRemote: Boolean) {
        viewModelScope.launch {
            repository.getUserList(query = query, requestRemote = requestRemote).collect { result ->
                when(result) {
                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {
                        state = state.copy(isLoading = true)
                    }
                    is Resource.Success -> {
                        result.data?.let {
                            state = state.copy(userList = it)
                        }
                    }
                }
            }
        }
    }
}