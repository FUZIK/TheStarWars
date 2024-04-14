package swapi.remote

import io.ktor.resources.Resource

@Resource("/api")
class SWApi {
    @Resource("/starships")
    class Starships(val parent: SWApi = SWApi(), val page: Int = 1) {
        @Resource("/{id}")
        class ByID(val parent: Starships = Starships(), val id: Int)
        @Resource("/")
        class Search(val parent: Starships = Starships(), val page: Int = 1, val search: String)
    }
    @Resource("/people")
    class People(val parent: SWApi = SWApi(), val page: Int = 1) {
        @Resource("/{id}")
        class ByID(val parent: People = People(), val id: Int)
        @Resource("/")
        class Search(val parent: People = People(), val page: Int = 1, val search: String)
    }
}
