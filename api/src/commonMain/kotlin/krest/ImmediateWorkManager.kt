package krest

import kase.Failure
import kase.Result
import kase.Success
import kollections.to
import koncurrent.ProgressState
import krest.params.SubmitWorkOptions
import live.LiveMap
import live.MutableLiveMap
import live.mutableLiveMapOf

class ImmediateWorkManager(private val factory: WorkerFactory) : WorkManager {
    private val liveWorkerStateMap = mutableMapOf<String, MutableLiveMap<String, ProgressState>>()

    override fun <P, R> submit(
        options: SubmitWorkOptions<P>
    ): Result<Worker<P, R>> {
        val worker = factory.createWorker<P, R>(options) ?: return noWorkerRegistered(options)
        val live = liveWorkerStateMap.getOrPut(options.scope) {
            mutableLiveMapOf(options.name to ProgressState.initial())
        }
        worker.doWork(options.params).onUpdate {
            live[options.name] = it
        }.then {
            live.remove(options.name)
        }
        return Success(worker)
    }

    override fun liveWorkProgress(scope: String): LiveMap<String, ProgressState> {
        return liveWorkerStateMap.getOrPut(scope) { mutableLiveMapOf() }
    }

    private fun <P, R> noWorkerRegistered(options: SubmitWorkOptions<*>): Result<Worker<P, R>> {
        val message = "Failed to create worker ${options.scope}->${options.name}"
        return Failure(IllegalArgumentException(message))
    }
}