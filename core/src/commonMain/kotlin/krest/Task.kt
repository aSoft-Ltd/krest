package krest

import cinematic.Watcher
import koncurrent.Later

abstract class Task<in P> {
    internal val completers = mutableListOf<Watcher>()

    internal inner class TaskCompletionWatcher(
        internal val callback: CompletionHandler
    ) : Watcher {
        override fun stop() {
            completers.remove(this)
        }
    }

    internal fun addCompletionHandler(handler: CompletionHandler): Watcher {
        val watcher = TaskCompletionWatcher(handler)
        completers.add(watcher)
        return watcher
    }

    abstract fun execute(params: P): Later<Unit>
}