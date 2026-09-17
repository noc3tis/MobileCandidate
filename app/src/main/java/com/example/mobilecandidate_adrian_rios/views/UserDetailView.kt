package com.example.mobilecandidate_adrian_rios.views

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.location.Geocoder
import android.net.Uri
import android.os.Environment
import android.provider.ContactsContract
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.mobilecandidate_adrian_rios.viewModel.UserData
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

/*
 * Pantalla con la información completa de un usuario.
 *
 * Incluye:
 * - Imagen.
 * - Nombre.
 * - Correo.
 * - Teléfono.
 * - Dirección.
 * - Contactos.
 * - Compartir.
 * - PDF.
 * - Distancia.
 * - Google Maps.
 * - Validación de credenciales.
 * - Cámara.
 */
@Composable
fun UserDetailView(
    navController: NavController,
    usuario: UserData
) {

    val context = LocalContext.current

    /*
     * Cliente utilizado para obtener la ubicación actual
     * del dispositivo.
     */
    val fusedLocationClient =
        LocationServices.getFusedLocationProviderClient(
            context
        )

    /*
     * Distancia calculada entre el dispositivo y el usuario.
     */
    var distanciaKm by remember {
        mutableStateOf<Double?>(null)
    }

    /*
     * Campos del formulario de credenciales.
     */
    var correo by remember {
        mutableStateOf("")
    }

    var contrasena by remember {
        mutableStateOf("")
    }

    /*
     * Mensaje que se muestra después de validar.
     */
    var mensajeValidacion by remember {
        mutableStateOf("")
    }

    /*
     * Bitmap de la fotografía tomada.
     *
     * Se mantiene en memoria mientras esta pantalla exista.
     */
    var fotoTomada by remember {
        mutableStateOf<Bitmap?>(null)
    }

    /*
     * Launcher para abrir la cámara.
     *
     * TakePicturePreview devuelve un Bitmap de la fotografía.
     */
    val cameraLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->

            if (bitmap != null) {

                fotoTomada = bitmap
            }
        }

    /*
     * Launcher utilizado para solicitar permisos de ubicación.
     */
    val locationPermissionLauncher =
        rememberLauncherForActivityResult(

            ActivityResultContracts.RequestMultiplePermissions()

        ) { permisos ->

            /*
             * Se acepta cualquiera de los dos permisos:
             * ubicación precisa o aproximada.
             */
            val permisoAceptado =
                permisos[
                    Manifest.permission.ACCESS_FINE_LOCATION
                ] == true ||

                        permisos[
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ] == true

            if (permisoAceptado) {

                /*
                 * Obtenemos la última ubicación conocida.
                 */
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->

                        if (location != null) {

                            /*
                             * En lugar de utilizar directamente
                             * las coordenadas de RandomUser, se geocodifica
                             * la dirección del usuario para mantener
                             * consistencia con Google Maps.
                             */
                            obtenerCoordenadasDeDireccion(
                                context,
                                usuario
                            ) { latitudUsuario,
                                longitudUsuario ->

                                distanciaKm =
                                    calcularDistanciaKm(

                                        location.latitude,

                                        location.longitude,

                                        latitudUsuario,

                                        longitudUsuario
                                    )
                            }
                        } else {

                            Toast.makeText(
                                context,
                                "No se pudo obtener la ubicación actual.",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

    Scaffold(

        topBar = {

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF1E293B)
                    )
                    .padding(
                        top = 30.dp,
                        start = 8.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                /*
                 * Regresar a la pantalla anterior.
                 */
                IconButton(

                    onClick = {
                        navController.popBackStack()
                    }
                ) {

                    Icon(

                        imageVector =
                            Icons.Filled.ArrowBack,

                        contentDescription =
                            "Regresar",

                        tint = Color.White
                    )
                }

                Text(

                    text =
                        "Información del usuario",

                    color = Color.White,

                    style =
                        MaterialTheme.typography.titleLarge
                )
            }
        }

    ) { paddingValues ->

        /*
         * Column desplazable porque esta pantalla contiene
         * una gran cantidad de información y controles.
         */
        Column(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFFF0F0F0)
                )
                .padding(paddingValues)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(16.dp),

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            /*
             * Imagen obtenida desde RandomUser.
             */
            AsyncImage(

                model =
                    usuario.picture.large,

                contentDescription =
                    "Foto del usuario",

                modifier = Modifier
                    .size(180.dp)
                    .clip(
                        RoundedCornerShape(20.dp)
                    ),

                contentScale =
                    ContentScale.Crop
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            /*
             * Nombre completo.
             */
            Text(

                text =
                    "${usuario.name.title} " +
                            "${usuario.name.first} " +
                            "${usuario.name.last}",

                style =
                    MaterialTheme.typography.headlineSmall,

                color =
                    Color(0xFF1E293B)
            )

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            /*
             * Correo.
             */
            UserInfoCard(

                icon =
                    Icons.Filled.Email,

                title =
                    "Correo",

                value =
                    usuario.email
            )

            /*
             * Teléfono.
             */
            UserInfoCard(

                icon =
                    Icons.Filled.Phone,

                title =
                    "Teléfono",

                value =
                    usuario.phone
            )

            /*
             * Guarda el usuario mediante el sistema de contactos
             * del dispositivo.
             */
            Button(

                onClick = {

                    guardarEnContactos(
                        context,
                        usuario
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {

                Text(
                    "Guardar en contactos"
                )
            }

            /*
             * Comparte nombre, teléfono y correo.
             *
             * Android mostrará las aplicaciones compatibles,
             * incluyendo WhatsApp si está instalado.
             */
            Button(

                onClick = {

                    compartirContacto(
                        context,
                        usuario
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {

                Text(
                    "Compartir por WhatsApp"
                )
            }

            /*
             * Genera el PDF.
             */
            Button(

                onClick = {

                    guardarContactoComoPdf(
                        context,
                        usuario
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {

                Text(
                    "Guardar como PDF"
                )
            }

            /*
             * Dirección del usuario.
             */
            UserInfoCard(

                icon =
                    Icons.Filled.LocationOn,

                title =
                    "Dirección",

                value =
                    "${usuario.location.street.number} " +
                            "${usuario.location.street.name}\n" +
                            "${usuario.location.city}, " +
                            "${usuario.location.state}\n" +
                            usuario.location.country
            )

            /*
             * Calcula la distancia.
             */
            Button(

                onClick = {

                    val tienePermiso =

                        ContextCompat.checkSelfPermission(

                            context,

                            Manifest.permission.ACCESS_FINE_LOCATION

                        ) ==
                                PackageManager.PERMISSION_GRANTED ||

                                ContextCompat.checkSelfPermission(

                                    context,

                                    Manifest.permission.ACCESS_COARSE_LOCATION

                                ) ==
                                PackageManager.PERMISSION_GRANTED

                    if (tienePermiso) {

                        /*
                         * El permiso ya existe.
                         */
                        fusedLocationClient.lastLocation
                            .addOnSuccessListener { location ->

                                if (location != null) {

                                    /*
                                     * Geocodifica la dirección
                                     * del usuario.
                                     */
                                    obtenerCoordenadasDeDireccion(

                                        context,

                                        usuario

                                    ) { latitudUsuario,
                                        longitudUsuario ->

                                        distanciaKm =
                                            calcularDistanciaKm(

                                                location.latitude,

                                                location.longitude,

                                                latitudUsuario,

                                                longitudUsuario
                                            )
                                    }

                                } else {

                                    Toast.makeText(

                                        context,

                                        "No se pudo obtener la ubicación actual.",

                                        Toast.LENGTH_LONG

                                    ).show()
                                }
                            }

                    } else {

                        /*
                         * Se solicita permiso al usuario.
                         */
                        locationPermissionLauncher.launch(

                            arrayOf(

                                Manifest.permission
                                    .ACCESS_FINE_LOCATION,

                                Manifest.permission
                                    .ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {

                Text(
                    "Calcular distancia"
                )
            }

            /*
             * Muestra la distancia calculada.
             */
            if (distanciaKm != null) {

                Text(

                    text =
                        "Distancia: %.2f km"
                            .format(distanciaKm),

                    modifier =
                        Modifier.padding(
                            top = 12.dp
                        )
                )
            }

            /*
             * Abre Google Maps.
             */
            Button(

                onClick = {

                    abrirGoogleMaps(
                        context,
                        usuario
                    )
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {

                Text(
                    "Ver ubicación en Google Maps"
                )
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            /*
             * ------------------------------------------------
             * FORMULARIO DE CREDENCIALES
             * ------------------------------------------------
             */
            Text(

                text =
                    "Validar credenciales",

                style =
                    MaterialTheme.typography.headlineSmall,

                color =
                    Color(0xFF1E293B)
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * Campo de correo.
             */
            OutlinedTextField(

                value =
                    correo,

                onValueChange = {

                    correo = it

                    /*
                     * Se limpia el mensaje al modificar
                     * el contenido.
                     */
                    mensajeValidacion = ""
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text(
                        "Correo electrónico"
                    )
                },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Email
                    ),

                singleLine = true
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            /*
             * Campo de contraseña.
             */
            OutlinedTextField(

                value =
                    contrasena,

                onValueChange = {

                    contrasena = it

                    mensajeValidacion = ""
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {
                    Text(
                        "Contraseña"
                    )
                },

                /*
                 * Oculta la contraseña.
                 */
                visualTransformation =
                    PasswordVisualTransformation(),

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Password
                    ),

                singleLine = true
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * Botón de validación.
             */
            Button(

                onClick = {

                    /*
                     * Validación del correo.
                     */
                    val correoValido =

                        android.util.Patterns
                            .EMAIL_ADDRESS
                            .matcher(correo)
                            .matches()

                    /*
                     * La contraseña debe tener
                     * mínimo 6 caracteres.
                     */
                    val contrasenaValida =
                        contrasena.length >= 6

                    /*
                     * Determinamos el mensaje que se mostrará.
                     */
                    mensajeValidacion = when {

                        correo.isBlank() ->
                            "Ingresa un correo electrónico"

                        !correoValido ->
                            "El correo electrónico no es válido"

                        contrasena.isBlank() ->
                            "Ingresa una contraseña"

                        !contrasenaValida ->
                            "La contraseña debe tener al menos 6 caracteres"

                        else ->
                            "Credenciales válidas"
                    }
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "Validar credenciales"
                )
            }

            /*
             * Muestra el resultado de la validación.
             */
            if (mensajeValidacion.isNotEmpty()) {

                Text(

                    text =
                        mensajeValidacion,

                    modifier =
                        Modifier.padding(
                            top = 12.dp
                        ),

                    color =

                        if (
                            mensajeValidacion ==
                            "Credenciales válidas"
                        ) {

                            Color(0xFF2E7D32)

                        } else {

                            Color.Red
                        }
                )
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )

            /*
             * ------------------------------------------------
             * CÁMARA
             * ------------------------------------------------
             */
            Text(

                text =
                    "Tomar fotografía",

                style =
                    MaterialTheme.typography.headlineSmall,

                color =
                    Color(0xFF1E293B)
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            /*
             * Abre la cámara del dispositivo.
             */
            Button(

                onClick = {

                    cameraLauncher.launch(null)
                },

                modifier =
                    Modifier.fillMaxWidth()
            ) {

                Text(
                    "Tomar foto"
                )
            }

            /*
             * Si existe una fotografía,
             * se muestra debajo del botón.
             */
            if (fotoTomada != null) {

                Spacer(
                    modifier =
                        Modifier.height(16.dp)
                )

                Image(

                    bitmap =
                        fotoTomada!!.asImageBitmap(),

                    contentDescription =
                        "Fotografía tomada",

                    modifier = Modifier
                        .size(220.dp)
                        .clip(
                            RoundedCornerShape(20.dp)
                        ),

                    contentScale =
                        ContentScale.Crop
                )
            }
        }
    }
}

/*
 * Tarjeta reutilizable para mostrar información.
 */
@Composable
fun UserInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),

        colors =
            CardDefaults.cardColors(
                containerColor =
                    Color.White
            ),

        elevation =
            CardDefaults.cardElevation(
                defaultElevation = 4.dp
            ),

        shape =
            RoundedCornerShape(16.dp)
    ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Icon(

                imageVector =
                    icon,

                contentDescription =
                    title,

                tint =
                    Color(0xFF4DA8DA),

                modifier =
                    Modifier.size(30.dp)
            )

            Column(

                modifier =
                    Modifier.padding(
                        start = 16.dp
                    )
            ) {

                Text(

                    text =
                        title,

                    style =
                        MaterialTheme.typography.labelMedium,

                    color =
                        Color.Gray
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Text(

                    text =
                        value,

                    style =
                        MaterialTheme.typography.bodyLarge,

                    color =
                        Color(0xFF1E293B)
                )
            }
        }
    }
}

/*
 * Abre el formulario de creación de contacto del sistema.
 *
 * De esta manera el usuario mantiene control sobre
 * la información que se va a guardar.
 */
fun guardarEnContactos(
    context: Context,
    usuario: UserData
) {

    val intent =
        Intent(
            ContactsContract.Intents.Insert.ACTION
        ).apply {

            type =
                ContactsContract.RawContacts.CONTENT_TYPE

            putExtra(

                ContactsContract.Intents.Insert.NAME,

                "${usuario.name.first} " +
                        usuario.name.last
            )

            putExtra(

                ContactsContract.Intents.Insert.PHONE,

                usuario.phone
            )

            putExtra(

                ContactsContract.Intents.Insert.EMAIL,

                usuario.email
            )
        }

    context.startActivity(intent)
}

/*
 * Comparte la información del contacto utilizando
 * el sistema de compartir de Android.
 *
 * WhatsApp aparecerá entre las aplicaciones disponibles
 * si está instalado en el dispositivo.
 */
fun compartirContacto(
    context: Context,
    usuario: UserData
) {

    val nombre =
        "${usuario.name.first} " +
                usuario.name.last

    val mensaje = """
        Nombre: $nombre
        Teléfono: ${usuario.phone}
        Correo: ${usuario.email}
    """.trimIndent()

    val intent =
        Intent(Intent.ACTION_SEND).apply {

            type = "text/plain"

            putExtra(
                Intent.EXTRA_TEXT,
                mensaje
            )
        }

    context.startActivity(

        Intent.createChooser(
            intent,
            "Compartir contacto"
        )
    )
}

/*
 * Genera un documento PDF con la información
 * principal del usuario.
 */
fun guardarContactoComoPdf(
    context: Context,
    usuario: UserData
) {

    /*
     * Crea el documento PDF.
     */
    val pdfDocument =
        PdfDocument()

    /*
     * Tamaño aproximado de una hoja A4.
     */
    val pageInfo =
        PdfDocument.PageInfo.Builder(
            595,
            842,
            1
        ).create()

    val page =
        pdfDocument.startPage(pageInfo)

    val canvas =
        page.canvas

    /*
     * Configuración del texto.
     */
    val paint =
        Paint().apply {

            textSize = 20f

            color =
                android.graphics.Color.BLACK
        }

    val nombre =
        "${usuario.name.first} " +
                usuario.name.last

    canvas.drawText(
        "Información del contacto",
        50f,
        60f,
        paint
    )

    paint.textSize = 16f

    canvas.drawText(
        "Nombre: $nombre",
        50f,
        120f,
        paint
    )

    canvas.drawText(
        "Teléfono: ${usuario.phone}",
        50f,
        160f,
        paint
    )

    canvas.drawText(
        "Correo: ${usuario.email}",
        50f,
        200f,
        paint
    )

    val direccion =
        "${usuario.location.street.number} " +
                usuario.location.street.name

    canvas.drawText(
        "Dirección:",
        50f,
        240f,
        paint
    )

    canvas.drawText(
        direccion,
        50f,
        275f,
        paint
    )

    canvas.drawText(

        "${usuario.location.city}, " +
                usuario.location.state,

        50f,
        310f,
        paint
    )

    canvas.drawText(

        usuario.location.country,

        50f,
        345f,
        paint
    )

    /*
     * Finalizamos la página.
     */
    pdfDocument.finishPage(page)

    try {

        /*
         * Carpeta Descargas del dispositivo.
         */
        val carpeta =
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS
            )

        val archivo = File(

            carpeta,

            "contacto_" +
                    "${usuario.name.first}_" +
                    "${usuario.name.last}.pdf"
        )

        /*
         * Escribimos el PDF en el archivo.
         */
        pdfDocument.writeTo(
            FileOutputStream(archivo)
        )

        Toast.makeText(

            context,

            "PDF guardado en Descargas",

            Toast.LENGTH_LONG

        ).show()

    } catch (e: Exception) {

        /*
         * Manejo de errores durante la creación del PDF.
         */
        Toast.makeText(

            context,

            "Error al guardar PDF: ${e.message}",

            Toast.LENGTH_LONG

        ).show()

    } finally {

        /*
         * Cerramos siempre el documento.
         */
        pdfDocument.close()
    }
}

/*
 * Calcula la distancia entre dos coordenadas
 * utilizando la fórmula proporcionada por Android.
 *
 * El resultado se devuelve en kilómetros.
 */
fun calcularDistanciaKm(

    lat1: Double,
    lon1: Double,

    lat2: Double,
    lon2: Double

): Double {

    val resultados =
        FloatArray(1)

    android.location.Location.distanceBetween(

        lat1,
        lon1,

        lat2,
        lon2,

        resultados
    )

    /*
     * Android devuelve metros.
     *
     * Se divide entre 1000 para convertir a kilómetros.
     */
    return resultados[0] / 1000.0
}

/*
 * Convierte una dirección escrita en coordenadas.
 *
 * Esto resulta útil porque las coordenadas proporcionadas
 * por RandomUser pueden no coincidir exactamente con la
 * dirección textual.
 */
fun obtenerCoordenadasDeDireccion(

    context: Context,

    usuario: UserData,

    onResultado: (Double, Double) -> Unit

) {

    val geocoder =
        Geocoder(
            context,
            Locale.getDefault()
        )

    val direccion =

        "${usuario.location.street.number} " +
                "${usuario.location.street.name}, " +
                "${usuario.location.city}, " +
                "${usuario.location.state}, " +
                usuario.location.country

    try {

        val resultados =
            geocoder.getFromLocationName(
                direccion,
                1
            )

        if (!resultados.isNullOrEmpty()) {

            val ubicacion =
                resultados[0]

            /*
             * Devuelve las coordenadas encontradas.
             */
            onResultado(

                ubicacion.latitude,

                ubicacion.longitude
            )
        }

    } catch (e: Exception) {

        /*
         * Si ocurre un problema con Geocoder,
         * mostramos un mensaje en lugar de provocar
         * un cierre de la aplicación.
         */
        e.printStackTrace()
    }
}

/*
 * Abre Google Maps utilizando las coordenadas obtenidas
 * mediante la dirección del usuario.
 */
fun abrirGoogleMaps(

    context: Context,

    usuario: UserData

) {

    obtenerCoordenadasDeDireccion(

        context,

        usuario

    ) { latitud, longitud ->

        /*
         * URL de búsqueda de Google Maps.
         */
        val uri = Uri.parse(

            "https://www.google.com/maps/search/" +
                    "?api=1&query=$latitud,$longitud"
        )

        val intent =
            Intent(
                Intent.ACTION_VIEW,
                uri
            )

        context.startActivity(intent)
    }
}