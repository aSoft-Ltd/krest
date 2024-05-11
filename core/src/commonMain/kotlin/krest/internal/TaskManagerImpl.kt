package krest.internal

import cinematic.Watcher
import kase.progress.Progress
import koncurrent.later.finally
import krest.Task
import krest.TaskInfo
import krest.TaskManager
import krest.TaskSubmitOptions
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

    override fun <P, T : Task<P>> submit(options: TaskSubmitOptions<P, T>) {
        val factory = tasks[options.task] ?: return
        val task = factory() as Task<P>
        val uid = "${options.task.simpleName}-${counter++}"
        val info = TaskInfo(uid = uid, name = options.name ?: uid, task)
        running.add(info)
        try {
            task.execute(options.params).finally {
                running.remove(info)
                task.observers.clear()
            }
        } catch (err: Throwable) {
            running.remove(info)
            task.observers.clear()
        }
    }

    override fun find(options: TaskSubmitOptions<*, *>): TaskInfo? = running.find {
        it.task::class == options.task && it.name == options.name
    }

    override fun isRunning(options: TaskSubmitOptions<*, *>): Boolean = find(options) != null

    override fun running(): List<TaskInfo> = running

    override fun watch(options: TaskSubmitOptions<*, *>, callback: (progress: Progress) -> Unit): Watcher? {
        return find(options)?.task?.addWatcher(callback)
    }
}