package search

import core.EntityIdentity
import core.EntitySource
import core.PagingResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PagesLoader<T: EntityIdentity>(
    private val scope: CoroutineScope,
    private val entitySource: EntitySource<T>
) {
    private var job: Job? = null
    fun search(query: String, onPageLoaded: (List<T>) -> Unit, onFinish: () -> Unit) {
        job?.also(Job::cancel)
        job = scope.launch {
            var result: PagingResult<List<T>>? = null
            do {
                val page = result?.nextPage ?: 1
                result = entitySource.search(page, query)
                onPageLoaded(result.data)
            } while (result?.hasNext == true)
            onFinish()
        }
    }
}
