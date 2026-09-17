package com.example.mobilecandidate_adrian_rios.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobilecandidate_adrian_rios.data.UserEntity
import com.example.mobilecandidate_adrian_rios.di.AppModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
 * ViewModel principal de la aplicación.
 *
 * Se encarga de:
 * - Obtener usuarios de la API.
 * - Mantener la lista de usuarios.
 * - Filtrar por género.
 * - Eliminar usuarios.
 * - Administrar favoritos.
 * - Mantener el usuario seleccionado.
 * - Controlar estados de carga.
 * - Controlar errores.
 *
 * El ViewModel permite separar la lógica de negocio
 * de las interfaces Compose.
 */
class UsersViewModel : ViewModel() {

    /*
     * Lista completa obtenida desde la API.
     *
     * Es privada porque las vistas no necesitan modificarla directamente.
     */
    private var listaCompleta: List<UserData> =
        emptyList()

    /*
     * Género actualmente utilizado como filtro.
     *
     * null significa que no existe ningún filtro.
     */
    private var generoFiltroActivo: String? = null

    /*
     * Lista que realmente se muestra en HomeView.
     *
     * private set evita que una vista pueda modificarla directamente.
     */
    var listaUsuarios: List<UserData> by mutableStateOf(
        emptyList()
    )
        private set

    /*
     * Favoritos almacenados en Room.
     */
    var listaFavoritos: List<UserEntity> by mutableStateOf(
        emptyList()
    )
        private set

    /*
     * Usuario seleccionado para mostrar su información detallada.
     */
    var usuarioSeleccionado: UserData? by mutableStateOf(null)
        private set

    /*
     * Indica si se están cargando los primeros usuarios.
     */
    var isLoading: Boolean by mutableStateOf(false)
        private set

    /*
     * Indica si se están cargando usuarios adicionales.
     */
    var isLoadingMore: Boolean by mutableStateOf(false)
        private set

    /*
     * Mensaje de error que puede ser mostrado por la interfaz.
     */
    var errorMessage: String? by mutableStateOf(null)
        private set

    /*
     * Seed fija.
     *
     * Permite solicitar una secuencia consistente de resultados.
     */
    private val seed = "adrian_app"

    /*
     * Después de cargar la primera página de 50 usuarios,
     * se comienza desde la página 6 solicitando grupos de 10.
     */
    private var paginaDe10 = 6

    /*
     * El ViewModel comienza observando favoritos
     * y cargando los primeros usuarios.
     */
    init {

        observarFavoritos()

        cargarUsuariosIniciales()
    }

    /*
     * Observa continuamente los favoritos guardados en Room.
     *
     * Cuando cambia la base de datos, listaFavoritos
     * se actualiza automáticamente.
     */
    private fun observarFavoritos() {

        viewModelScope.launch {

            AppModule.userDao
                .obtenerFavoritos()
                .collect { favoritos ->

                    listaFavoritos = favoritos
                }
        }
    }

    /*
     * Guarda temporalmente el usuario seleccionado.
     *
     * Posteriormente UserDetailView utiliza este usuario.
     */
    fun seleccionarUsuario(usuario: UserData) {

        usuarioSeleccionado = usuario
    }

    /*
     * Busca un usuario dentro de la lista completa
     * utilizando su correo como identificador.
     */
    fun obtenerUsuarioPorEmail(email: String): UserData? {

        return listaCompleta.find { usuario ->

            usuario.email == email
        }
    }

    /*
     * Convierte UserData en UserEntity y lo guarda
     * dentro de Room como favorito.
     */
    fun agregarAFavoritos(usuario: UserData) {

        viewModelScope.launch(Dispatchers.IO) {

            val favorito = UserEntity(

                email = usuario.email,

                title = usuario.name.title,

                firstName = usuario.name.first,

                lastName = usuario.name.last,

                gender = usuario.gender
            )

            AppModule.userDao.insertarFavorito(favorito)
        }
    }

    /*
     * Elimina un favorito de la base de datos.
     */
    fun eliminarDeFavoritos(usuario: UserEntity) {

        viewModelScope.launch(Dispatchers.IO) {

            AppModule.userDao.eliminarFavorito(usuario)
        }
    }

    /*
     * Obtiene los primeros 50 usuarios.
     */
    fun cargarUsuariosIniciales() {

        viewModelScope.launch(Dispatchers.IO) {

            isLoading = true

            errorMessage = null

            /*
             * Al realizar una nueva carga inicial,
             * se elimina cualquier filtro anterior.
             */
            generoFiltroActivo = null

            /*
             * Después de los primeros 50,
             * las siguientes peticiones comienzan en página 6.
             */
            paginaDe10 = 6

            try {

                val response =
                    AppModule.instance.getUser(
                        results = 50,
                        page = 1,
                        seed = seed
                    )

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {

                        listaCompleta = body.results

                        aplicarFiltroLocal()

                    } else {

                        errorMessage =
                            "La respuesta llegó vacía"
                    }

                } else {

                    errorMessage =
                        "Error en la petición: ${response.code()}"
                }

            } catch (e: Exception) {

                errorMessage =
                    "Error de conexión: ${e.localizedMessage}"

            } finally {

                isLoading = false
            }
        }
    }

    /*
     * Carga 10 usuarios adicionales.
     *
     * Se utiliza cuando el usuario llega al final de la lista.
     */
    fun cargarMasUsuarios() {

        /*
         * Evita lanzar dos peticiones simultáneas.
         */
        if (isLoadingMore) return

        viewModelScope.launch(Dispatchers.IO) {

            isLoadingMore = true

            try {

                val response =
                    AppModule.instance.getUser(
                        results = 10,
                        page = paginaDe10,
                        seed = seed
                    )

                if (response.isSuccessful) {

                    val body = response.body()

                    if (body != null) {

                        /*
                         * Los nuevos usuarios se agregan
                         * a los usuarios que ya teníamos.
                         */
                        listaCompleta =
                            listaCompleta + body.results

                        paginaDe10++

                        /*
                         * Se vuelve a aplicar el filtro actual.
                         */
                        aplicarFiltroLocal()

                    } else {

                        errorMessage =
                            "No se recibieron usuarios adicionales."
                    }

                } else {

                    errorMessage =
                        "Error al cargar más usuarios: ${response.code()}"
                }

            } catch (e: Exception) {

                errorMessage =
                    "Error al cargar más usuarios: ${e.localizedMessage}"

            } finally {

                isLoadingMore = false
            }
        }
    }

    /*
     * Activa un filtro por género.
     *
     * Ejemplos:
     * "male"
     * "female"
     */
    fun filtrarPorGenero(gender: String?) {

        generoFiltroActivo = gender

        aplicarFiltroLocal()
    }

    /*
     * Elimina el filtro de género.
     */
    fun sinGenero() {

        generoFiltroActivo = null

        aplicarFiltroLocal()
    }

    /*
     * Elimina individualmente un usuario de la lista.
     */
    fun eliminarUsuario(usuario: UserData) {

        listaCompleta =
            listaCompleta.filter { item ->

                item.email != usuario.email
            }

        aplicarFiltroLocal()
    }

    /*
     * Actualiza listaUsuarios de acuerdo con el filtro activo.
     */
    private fun aplicarFiltroLocal() {

        val filtro = generoFiltroActivo

        if (filtro == null) {

            listaUsuarios = listaCompleta

        } else {

            listaUsuarios =
                listaCompleta.filter { user ->

                    user.gender.equals(
                        filtro,
                        ignoreCase = true
                    )
                }
        }
    }
}