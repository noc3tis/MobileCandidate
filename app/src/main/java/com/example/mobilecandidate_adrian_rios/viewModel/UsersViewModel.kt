package com.example.mobilecandidate_adrian_rios.viewModel

//Objeto principal de respuesta de la API
data class RandomUserResponse(
    val results: List<UserData>
)

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

data class NameInfo(
    val title: String,
    val first: String,
    val last: String
)

data class LocationInfo(
    val street: StreetInfo,
    val city: String,
    val state: String,
    val country: String
)

data class StreetInfo(
    val number: Int,
    val name: String,
)

data class LoginInfo(
    val uuid: String,
    val username: String
)

data class DobInfo(
    val number: String,
    val age: Int
)

data class PictureInfo(
    val large: String,
    val medium: String,
    val thumbnail: String
)



