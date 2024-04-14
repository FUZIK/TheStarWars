package core

enum class EntityType {
    People,
    FILMS,
    Starships,
    Vehicles,
    Species,
    PLANETS
}

enum class Gender {
    MALE,
    FEMALE,
    UNKNOWN, NOT_AVAILABLE,
    HERMAPHRODITE,
}

typealias ID = Int

interface EntityIdentity {
    val id: ID
}

sealed interface EntityNamed: EntityIdentity {
    val name: String
}

class People(
    override val id: ID,
    override val name: String,
    val gender: Gender,
    val starships: List<ID>
): EntityNamed {
    override fun equals(other: Any?) = other is People && other.id == id
}

class Starship(
    override val id: ID,
    override val name: String,
    val model: String,
    val manufacturer: String,
    val passengers: Int,
    val pilots: List<ID>
): EntityNamed {
    override fun equals(other: Any?) = other is Starship && other.id == id
}
