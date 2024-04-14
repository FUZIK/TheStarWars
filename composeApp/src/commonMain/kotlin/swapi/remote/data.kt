package swapi.remote

import core.Gender
import core.GenderSerializer
import kotlinx.serialization.Serializable

@Serializable
class PagingResponse<I>(
    val next: String?,
    val results: List<I>
)

@Serializable
class RemotePeople(
    val name: String,
    @Serializable(with = GenderSerializer::class)
    val gender: Gender,
    val starships: List<String>
)

@Serializable
class RemoteStarship(
    val name: String,
    val model: String,
    val manufacturer: String,
    val passengers: String,
    val pilots: List<String>
)
