package swapi.remote

import io.ktor.resources.Resource

@Resource("/api")
class SWApiResource {
    @Resource("/{entity}")
    class Entity(val parent: SWApiResource = SWApiResource(), val entity: String, val page: Int = 1) {
        @Resource("/{id}")
        class ByID(val e: String, val parent: Entity = Entity(entity = e), val id: Int)
        @Resource("/")
        class Search(val e: String, val parent: Entity = Entity(entity = e), val page: Int = 1, val search: String)
    }
}
