package krest

import cinematic.Watcher
import kase.progress.Progress
import koncurrent.Later

abstract class Task<in P> {
    internal val observers = mutableListOf<((Progress) -> Unit)?>()

    private inner class TaskWatcher(private var callback: ((progress: Progress) -> Unit)?) : Watcher {
        override fun stop() {
            observers.remove(callback)
            callback = null
        }
    }

    internal fun addWatcher(callback: (progress: Progress) -> Unit): Watcher {
        val watcher = TaskWatcher(callback)
        observers.removeAll { it == null }
        observers.add(callback)
        return watcher
    }

    abstract fun execute(params: P): Later<Unit>

    protected fun update(progress: Progress) {
        observers.removeAll { it == null }
        observers.filterNotNull().forEach { it(progress) }
    }
}