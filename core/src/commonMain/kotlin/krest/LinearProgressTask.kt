package krest

import cinematic.Watcher
import status.Progress

abstract class LinearProgressTask<in P, R : Any> : ProgressTask<P, R>() {
    private val observers = mutableListOf<((Progress<R>) -> Unit)?>()

    private inner class TaskWatcher(private var callback: ((progress: Progress<R>) -> Unit)?) : Watcher {
        override fun stop() {
            observers.remove(callback)
            callback = null
        }
    }

    internal fun addWatcher(callback: (progress: Progress<R>) -> Unit): Watcher {
        val watcher = TaskWatcher(callback)
        observers.removeAll { it == null }
        observers.add(callback)
        return watcher
    }

    protected fun update(progress: Progress<R>) {
        observers.removeAll { it == null }
        observers.filterNotNull().forEach { it(progress) }
    }

    override fun removeAllWatchers() {
        observers.clear()
    }
}