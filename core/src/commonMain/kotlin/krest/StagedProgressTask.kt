package krest

import cinematic.Watcher
import status.StagedProgressReport

abstract class StagedProgressTask<in P, R : Any> : ProgressTask<P,R>() {
    private val observers = mutableListOf<((StagedProgressReport<R>) -> Unit)?>()

    private inner class TaskWatcher(private var callback: ((progress: StagedProgressReport<R>) -> Unit)?) : Watcher {
        override fun stop() {
            observers.remove(callback)
            callback = null
        }
    }

    internal fun addWatcher(callback: (progress: StagedProgressReport<R>) -> Unit): Watcher {
        val watcher = TaskWatcher(callback)
        observers.removeAll { it == null }
        observers.add(callback)
        return watcher
    }

    protected fun update(progress: StagedProgressReport<R>) {
        observers.removeAll { it == null }
        observers.filterNotNull().forEach { it(progress) }
    }

    final override fun removeAllWatchers() {
        observers.clear()
    }
}