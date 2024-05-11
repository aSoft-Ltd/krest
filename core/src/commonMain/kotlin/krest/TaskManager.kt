package krest

import cinematic.Watcher
import kase.progress.Progress
import kotlin.reflect.KClass

/**
 * A task manager is responsible for executing tasks that are not scoped to a specific screen and managing their lifecycle.
 *
 * This manager should be used to execute tasks with in the same application process and not across different processes.
 *
 * Background tasks executed by the System should be scheduled using a [WorkManager] not this [TaskManager].
 */
interface TaskManager {

    /**
     * Register a task factory that will be used to execute tasks of the given type.
     */
    fun <T : Task<*>> register(type: KClass<T>, factory: () -> T)

    /**
     * Submit a task for execution with the given options.
     *
     * if you need your task to be unique, you can provide a name for it.
     *
     * Tasks with no names, will be given a unique name based on their type.
     */
    fun <P, T : Task<P>> submit(options: TaskSubmitOptions<P, T>)

    /**
     * Find a running task with the given options.
     *
     * @return a [TaskInfo] or `null` if no task is found.
     */
    fun find(options: TaskSubmitOptions<*, *>): TaskInfo?

    /**
     * Check if a task with the given options is running.
     *
     * @return a [Boolean]
     */
    fun isRunning(options: TaskSubmitOptions<*, *>): Boolean = find(options) != null

    /**
     * Get a list of all running tasks.
     *
     * @return a [List] of [TaskInfo]
     */
    fun running(): List<TaskInfo>

    /**
     * Watch the progress of a task with the given options.
     *
     * @param callback will be called with the progress of the task.
     * @return a [Watcher]
     */
    fun watch(options: TaskSubmitOptions<*, *>, callback: (progress: Progress) -> Unit): Watcher?
}