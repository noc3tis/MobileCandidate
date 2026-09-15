package com.example.mobilecandidate_adrian_rios.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UsersViewModel : ViewModel() {

    var listaUsuarios by mutableStateOf<List<UserData>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    init {

    }
}