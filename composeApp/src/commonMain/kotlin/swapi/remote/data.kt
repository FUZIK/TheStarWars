package swapi.remote

import core.EntityIdentity
import core.Gender
import core.GenderSerializer
import core.People
import core.Starship
import kotlinx.serialization.Serializable

@Serializable
open class PagingResponse<I>(
    val next: String?,
    val results: List<I>
)

@Serializable
sealed interface RemoteEntity<T: EntityIdentity> {
    fun mapTo(): T
}

@Serializable
class RemotePeople(
    val name: String,
    @Serializable(with = GenderSerializer::class)
    val gender: Gender,
    val starships: List<String>
): RemoteEntity<People> {
    override fun mapTo() = mapToPeople()
}

@Serializable
class RemoteStarship(
    val name: String,
    val model: String,
    val manufacturer: String,
    val passengers: String,
    val pilots: List<String>
): RemoteEntity<Starship> {
    override fun mapTo() = mapToStarship()
}
