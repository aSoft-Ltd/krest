package krest

import kotlin.reflect.KClass

/**
 * A task manager is responsible for executing tasks that are not scoped to a specific screen and managing their lifecycle.
 *
 * This manager should be used to execute tasks with in the same application process and not across different processes.
 *
 * Background tasks executed by the System should be scheduled using a [WorkManager] not this [TaskManager].
 */
interface TaskManager : TaskRunner {

    /**
     * Register a task factory that will be used to execute tasks of the given type.
     */
    fun <T : Task<*>> register(type: KClass<T>, factory: () -> T)


    /**
     * Find a running task with the given options.
     *
     * @return a [TaskInfo] or `null` if no task is found.
     */
    fun find(options: TaskIdentity<*>): TaskInfo?


    /**
     * Get a list of all running tasks.
     *
     * @return a [List] of [TaskInfo]
     */
    fun running(): List<TaskInfo>
}