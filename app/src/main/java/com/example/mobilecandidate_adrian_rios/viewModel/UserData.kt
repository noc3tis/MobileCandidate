package com.example.mobilecandidate_adrian_rios.viewModel

/*
 * Respuesta principal de RandomUser.
 *
 * La API devuelve una lista llamada "results".
 */
data class RandomUserResponse(
    val results: List<UserData>
)

/*
 * Modelo que representa un usuario completo obtenido de la API.
 */
data class UserData(

    val gender: String,

    val name: NameInfo,

    val location: LocationInfo,

    val email: String,

    val login: LoginInfo,

    val dob: DobInfo,

    val phone: String,

    val cell: String,

    val picture: PictureInfo,

    val nat: String
)

/*
 * Información del nombre.
 */
data class NameInfo(
    val title: String,
    val first: String,
    val last: String
)

/*
 * Información de ubicación.
 */
data class LocationInfo(
    val street: StreetInfo,
    val city: String,
    val state: String,
    val country: String,
    val coordinates: CoordinatesInfo
)

/*
 * Coordenadas proporcionadas por RandomUser.
 */
data class CoordinatesInfo(
    val latitude: String,
    val longitude: String
)

/*
 * Información de la calle.
 */
data class StreetInfo(
    val number: Int,
    val name: String
)

/*
 * Información relacionada con el login del usuario.
 */
data class LoginInfo(
    val uuid: String,
    val username: String
)

/*
 * Información relacionada con la fecha de nacimiento.
 */
data class DobInfo(
    val number: String,
    val age: Int
)

/*
 * URLs de las diferentes imágenes del usuario.
 */
data class PictureInfo(
    val large: String,
    val medium: String,
    val thumbnail: String
)