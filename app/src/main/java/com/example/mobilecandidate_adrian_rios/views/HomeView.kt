package com.example.mobilecandidate_adrian_rios.views

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mobilecandidate_adrian_rios.viewModel.UserData
import com.example.mobilecandidate_adrian_rios.viewModel.UsersViewModel
import kotlinx.coroutines.flow.distinctUntilChanged

/*
 * Pantalla principal de la aplicación.
 *
 * Permite:
 * - Ver usuarios.
 * - Filtrar por género.
 * - Restablecer filtro.
 * - Abrir favoritos.
 * - Eliminar usuarios.
 * - Agregar usuarios a favoritos.
 * - Abrir el detalle de un usuario.
 * - Cargar más usuarios al llegar al final.
 */
@Composable
fun HomeView(
    navController: NavController,
    viewModel: UsersViewModel
) {

    Scaffold(

        topBar = {

            Column(

                modifier = Modifier
                    .background(Color(0xFF1E293B))
                    .padding(16.dp)
            ) {

                Spacer(
                    modifier = Modifier.height(30.dp)
                )

                Text(
                    text = "Lista de usuarios:",

                    color = Color.White,

                    style =
                        MaterialTheme.typography.titleLarge,

                    fontFamily =
                        FontFamily.Monospace
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                /*
                 * Botones de filtrado y favoritos.
                 */
                Row {

                    /*
                     * Filtrar hombres.
                     */
                    Button(

                        onClick = {
                            viewModel.filtrarPorGenero(
                                "male"
                            )
                        },

                        modifier =
                            Modifier.weight(0.5f),

                        shape =
                            RoundedCornerShape(10.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFF4DA8DA)
                            )
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Male,

                            contentDescription =
                                "Hombres",

                            modifier =
                                Modifier.size(20.dp),

                            tint = Color.White
                        )
                    }

                    /*
                     * Filtrar mujeres.
                     */
                    Button(

                        onClick = {
                            viewModel.filtrarPorGenero(
                                "female"
                            )
                        },

                        modifier =
                            Modifier.weight(0.5f),

                        shape =
                            RoundedCornerShape(10.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFFF472B6)
                            )
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Female,

                            contentDescription =
                                "Mujeres",

                            modifier =
                                Modifier.size(20.dp),

                            tint = Color.White
                        )
                    }

                    /*
                     * Quitar filtro.
                     */
                    Button(

                        onClick = {
                            viewModel.sinGenero()
                        },

                        modifier =
                            Modifier.weight(0.5f),

                        shape =
                            RoundedCornerShape(10.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFFF87171)
                            )
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Replay,

                            contentDescription =
                                "Reiniciar",

                            modifier =
                                Modifier.size(20.dp),

                            tint = Color.White
                        )
                    }

                    /*
                     * Abrir favoritos.
                     */
                    Button(

                        onClick = {
                            navController.navigate(
                                "Favorites"
                            )
                        },

                        modifier =
                            Modifier.weight(0.5f),

                        shape =
                            RoundedCornerShape(10.dp),

                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor =
                                    Color(0xFFFFEE8C)
                            )
                    ) {

                        Icon(
                            imageVector =
                                Icons.Filled.Star,

                            contentDescription =
                                "Favoritos",

                            modifier =
                                Modifier.size(20.dp),

                            tint = Color.White
                        )
                    }
                }
            }
        }

    ) { paddingValues ->

        Column(

            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF0F0F0))
        ) {

            /*
             * Lista que actualmente debe mostrarse.
             */
            val lista = viewModel.listaUsuarios

            /*
             * Estado de desplazamiento de LazyColumn.
             */
            val listState = rememberLazyListState()

            /*
             * Detecta cuándo el usuario llega al final.
             *
             * lista.size forma parte de la clave para que el efecto
             * se vuelva a ejecutar cuando llegan nuevos usuarios.
             */
            LaunchedEffect(
                listState,
                lista.size
            ) {

                snapshotFlow {

                    listState
                        .layoutInfo
                        .visibleItemsInfo
                        .lastOrNull()
                        ?.index
                }

                    .distinctUntilChanged()

                    .collect { ultimoElementoVisible ->

                        /*
                         * Si el último elemento visible corresponde
                         * al último usuario de la lista, se solicitan
                         * otros 10 usuarios.
                         */
                        if (

                            ultimoElementoVisible != null &&

                            ultimoElementoVisible >=
                            lista.lastIndex &&

                            !viewModel.isLoading &&

                            !viewModel.isLoadingMore &&

                            lista.isNotEmpty()

                        ) {

                            viewModel.cargarMasUsuarios()
                        }
                    }
            }

            /*
             * Estado inicial de carga.
             */
            if (viewModel.isLoading) {

                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator()
                }

                /*
                 * Error de conexión o respuesta vacía.
                 */
            } else if (
                viewModel.errorMessage != null &&
                lista.isEmpty()
            ) {

                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            viewModel.errorMessage
                                ?: "Ocurrió un error."
                    )
                }

                /*
                 * No existen usuarios para mostrar.
                 */
            } else if (lista.isEmpty()) {

                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            "No se encontraron usuarios."
                    )
                }

            } else {

                /*
                 * Lista principal.
                 */
                LazyColumn(

                    state = listState,

                    contentPadding =
                        PaddingValues(16.dp)
                ) {

                    items(

                        items = lista,

                        /*
                         * El correo permite identificar
                         * cada elemento de manera estable.
                         */
                        key = { usuario ->
                            usuario.email
                        }

                    ) { usuario ->

                        /*
                         * Estado utilizado para el gesto
                         * de deslizar el usuario.
                         */
                        val dismissState =
                            rememberSwipeToDismissBoxState(

                                confirmValueChange = {
                                        dismissValue ->

                                    when (
                                        dismissValue
                                    ) {

                                        /*
                                         * Deslizar hacia la izquierda:
                                         * elimina el usuario.
                                         */
                                        SwipeToDismissBoxValue.EndToStart -> {

                                            viewModel.eliminarUsuario(
                                                usuario
                                            )

                                            true
                                        }

                                        /*
                                         * Deslizar hacia la derecha:
                                         * agrega a favoritos.
                                         *
                                         * false evita que el elemento
                                         * desaparezca de la lista.
                                         */
                                        SwipeToDismissBoxValue.StartToEnd -> {

                                            viewModel.agregarAFavoritos(
                                                usuario
                                            )

                                            false
                                        }

                                        else -> false
                                    }
                                }
                            )

                        SwipeToDismissBox(

                            state = dismissState,

                            /*
                             * Fondo que aparece durante el gesto.
                             */
                            backgroundContent = {

                                val color by
                                animateColorAsState(

                                    targetValue =
                                        when (
                                            dismissState.targetValue
                                        ) {

                                            SwipeToDismissBoxValue.EndToStart ->
                                                Color(0xFFF87171)

                                            SwipeToDismissBoxValue.StartToEnd ->
                                                Color(0xFFFFEE8C)

                                            else ->
                                                Color.Transparent
                                        }
                                )

                                val alignment =
                                    when (
                                        dismissState.dismissDirection
                                    ) {

                                        SwipeToDismissBoxValue.StartToEnd ->
                                            Alignment.CenterStart

                                        SwipeToDismissBoxValue.EndToStart ->
                                            Alignment.CenterEnd

                                        else ->
                                            Alignment.Center
                                    }

                                val icon =
                                    when (
                                        dismissState.dismissDirection
                                    ) {

                                        SwipeToDismissBoxValue.StartToEnd ->
                                            Icons.Filled.Star

                                        SwipeToDismissBoxValue.EndToStart ->
                                            Icons.Filled.Delete

                                        else ->
                                            null
                                    }

                                Box(

                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(
                                            vertical = 8.dp,
                                            horizontal = 16.dp
                                        )
                                        .background(
                                            color,
                                            RoundedCornerShape(16.dp)
                                        )
                                        .padding(
                                            horizontal = 20.dp
                                        ),

                                    contentAlignment =
                                        alignment
                                ) {

                                    icon?.let {

                                        Icon(
                                            imageVector = it,

                                            contentDescription =
                                                null,

                                            tint =
                                                Color.White,

                                            modifier =
                                                Modifier.size(30.dp)
                                        )
                                    }
                                }
                            }

                        ) {

                            /*
                             * Al tocar la tarjeta se abre el detalle.
                             */
                            Box(

                                modifier =
                                    Modifier.clickable {

                                        viewModel
                                            .seleccionarUsuario(
                                                usuario
                                            )

                                        navController.navigate(
                                            "UserDetail"
                                        )
                                    }
                            ) {

                                UserCard(usuario)
                            }
                        }
                    }
                }
            }
        }
    }
}

/*
 * Tarjeta utilizada para mostrar cada usuario.
 *
 * Requisito:
 * - Nombre
 * - Correo
 * - Género
 */
@Composable
fun UserCard(usuario: UserData) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 8.dp,
                horizontal = 16.dp
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),

        colors =
            CardDefaults.cardColors(
                containerColor = Color.White
            ),

        shape =
            RoundedCornerShape(16.dp)
    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            Text(
                text =
                    "${usuario.name.title} " +
                            "${usuario.name.first} " +
                            "${usuario.name.last}",

                style =
                    MaterialTheme.typography.titleSmall,

                color = Color.Gray,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text = usuario.email,

                style =
                    MaterialTheme.typography.titleSmall,

                color = Color.Gray,

                fontWeight =
                    FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            Text(
                text = usuario.gender,

                style =
                    MaterialTheme.typography.titleSmall,

                color = Color.Gray,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}