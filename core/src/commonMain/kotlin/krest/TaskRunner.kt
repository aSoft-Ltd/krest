package krest

import cinematic.Watcher
import status.Progress
import status.StagedProgressReport

interface TaskRunner {

    /**
     * Submit a task for execution with the given options.
     *
     * if you need your task to be unique, you can provide a name for it.
     *
     * Tasks with no names, will be given a unique name based on their type.
     */
    suspend fun <P, T : Task<P>> submit(options: TaskSubmitOptions<P, T>)

    /**
     * Check if a task with the given options is running.
     *
     * @return a [Boolean]
     */
    fun isRunning(options: TaskIdentity<*>): Boolean


    /**
     * Registers a listener to be invoked when the task is completed
     */
    fun onCompleted(options: TaskIdentity<*>, callback: () -> Unit) : Watcher?


    /**
     * Watch the progress of a task with the given options.
     *
     * @param callback will be called with the progress of the task.
     * @return a [Watcher]
     */
    fun <R : Any> watch(options: LinearProgressTaskIdentity<*, R>, callback: (progress: Progress<R>) -> Unit): Watcher?

    /**
     * Watch the progress of a task with the given options.
     *
     * @param callback will be called with the progress of the task.
     * @return a [Watcher]
     */
    fun <R> watch(options: StagedProgressTaskIdentity<*, R>, callback: (progress: StagedProgressReport<R>) -> Unit): Watcher?
}