package swapi.remote

import core.ID
import core.PagingResult
import core.People
import core.Starship
import io.ktor.http.Url

fun <T, I> PagingResponse<I>.toPagingResult(page: Int, mapping: (I) -> T): PagingResult<List<T>> {
    val hasNext = next != null
    return PagingResult(
        data = results.map(mapping),
        hasNext = hasNext,
        nextPage = if (hasNext) page + 1 else 0
    )
}

private fun String.urlToID(): ID? = try {
    Url(this).pathSegments.run {
        val l = last()
        if (l == "") this[size - 2] else l
    }.toIntOrNull()
} catch (e: Exception) {
    null
}

private fun String.nameToIDXD() = asSequence().sumOf { it.code }

fun RemoteStarship.mapToStarship() = Starship(
    id = name.nameToIDXD(),
    name = name,
    model = model,
    manufacturer = manufacturer,
    passengers = passengers.toIntOrNull() ?: 0,
    pilots = pilots.mapNotNull { it.urlToID() }
)

fun RemotePeople.mapToPeople() = People(
    id = name.nameToIDXD(),
    name = name,
    gender = gender,
    starships = starships.mapNotNull { it.urlToID() }
)
