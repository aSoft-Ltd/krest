package krest.internal

import cinematic.Watcher
import krest.LinearProgressTask
import krest.LinearProgressTaskIdentity
import krest.ProgressTask
import krest.StagedProgressTask
import krest.StagedProgressTaskIdentity
import krest.Task
import krest.TaskIdentity
import krest.TaskInfo
import krest.TaskManager
import krest.TaskSubmitOptions
import status.Progress
import status.StagedProgressReport
import kotlin.reflect.KClass

@PublishedApi
internal class TaskManagerImpl : TaskManager {

    @PublishedApi
    internal val tasks = mutableMapOf<KClass<out Any>, () -> Task<*>>()

    private var counter = 0
    private val running = mutableListOf<TaskInfo>()

    override fun <T : Task<*>> register(type: KClass<T>, factory: () -> T) {
        tasks[type] = factory
    }

    override suspend fun <P, T : Task<P>> submit(options: TaskSubmitOptions<P, T>) {
        val factory = tasks[options.task] ?: return
        val task = factory() as Task<P>
        val uid = "${options.task.simpleName}-${counter++}"
        val info = TaskInfo(uid = uid, name = options.name ?: uid, task)
        running.add(info)
        try {
            task.execute(options.params)
            finish(info)
        } catch (err: Throwable) {
            finish(info)
        }
    }

    private fun finish(info: TaskInfo) {
        running.remove(info)
        val task = info.task
        (task as? ProgressTask<*, *>)?.removeAllWatchers()
        task.completers.forEach { (it as? Task<*>.TaskCompletionWatcher)?.callback?.invoke() }
        task.completers.clear()
    }

    override fun find(options: TaskIdentity<*>): TaskInfo? = running.find {
        it.task::class == options.task && it.name == options.name
    }

    override fun onCompleted(options: TaskIdentity<*>, callback: () -> Unit): Watcher? {
        val task = find(options)?.task ?: return null
        return task.addCompletionHandler(callback)
    }

    override fun isRunning(options: TaskIdentity<*>): Boolean = find(options) != null

    override fun running(): List<TaskInfo> = running

    override fun <R : Any> watch(options: LinearProgressTaskIdentity<*, R>, callback: (progress: Progress<R>) -> Unit): Watcher? {
        val task = find(options)?.task as? LinearProgressTask<*, *> ?: return null
        return task.addWatcher {
            try {
                callback(it as Progress<R>)
            } finally {

            }
        }
    }

    override fun <R> watch(options: StagedProgressTaskIdentity<*, R>, callback: (progress: StagedProgressReport<R>) -> Unit): Watcher? {
        val task = find(options)?.task as? StagedProgressTask<*, *> ?: return null
        return task.addWatcher {
            try {
                callback(it as StagedProgressReport<R>)
            } finally {

            }
        }
    }
}