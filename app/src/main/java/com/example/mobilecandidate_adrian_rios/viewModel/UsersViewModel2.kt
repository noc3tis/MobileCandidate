package com.example.mobilecandidate_adrian_rios.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import retrofit2.Call
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mobilecandidate_adrian_rios.di.AppModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class UsersViewModel : ViewModel() {

    var listaUsuarios by mutableStateOf<List<UserData>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf <String?>(null)
        private set

    init {
        performSearch()
    }

    fun performSearch(gender: String? = null){
        viewModelScope.launch(Dispatchers.IO){
            isLoading = true
            errorMessage = null

                try{
                    val response = AppModule.instance.getUser(results = 50,gender)

                    if(response.isSuccessful){
                        val body = response.body()
                        if(body != null){
                            listaUsuarios = body.results
                        }else{
                            errorMessage = "La respuesta llego vacía"
                        }
                    }else{
                        errorMessage = "Error en la petición: ${response.code()}"
                    }
                } catch (e: Exception) {
                    errorMessage = "Error de conexión: ${e.localizedMessage}"
                } finally {
                    isLoading = false
                }
        }
    }
}