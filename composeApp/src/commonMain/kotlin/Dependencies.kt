
import core.EntitySource
import core.FavoriteSource
import core.People
import core.Starship
import favorite.FavoriteSourceImpl
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.resources.Resources
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import swapi.EntitySourceImpl
import swapi.local.EntityInMemorySourceImpl
import swapi.remote.EntityRemoteSourceImpl
import swapi.remote.RemotePeople
import swapi.remote.RemoteStarship

object Dependencies {
    private val swClient: HttpClient = provideSWClient()
    val favoriteSource: FavoriteSource = FavoriteSourceImpl()
    val starshipSource: EntitySource<Starship> = EntitySourceImpl(
        localSource = EntityInMemorySourceImpl(),
        remoteSource = EntityRemoteSourceImpl<Starship, RemoteStarship>(swClient, "starships")
    )
    val peopleSource: EntitySource<People> = EntitySourceImpl(
        localSource = EntityInMemorySourceImpl(),
        remoteSource = EntityRemoteSourceImpl<People, RemotePeople>(swClient, "people")
    )
    private fun provideSWClient(): HttpClient = HttpClient {
        install(Resources)
        install(ContentNegotiation) {
            json(json = Json {
                ignoreUnknownKeys = true
            })
        }
        defaultRequest {
            url("https://swapi.dev/")
        }
    }
}