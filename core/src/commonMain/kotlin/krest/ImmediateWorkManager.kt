package krest

import cinematic.mutableLiveMapOf
import kase.Failure
import kase.Result
import kase.Success
import kase.progress.ProgressBus
import kase.progress.ProgressState
import koncurrent.later.then
import koncurrent.onUpdate
import krest.params.SubmitWorkOptions

class ImmediateWorkManager(private val factory: WorkerFactory) : WorkManager {
    private val workerLedger = mutableListOf<WorkerLedger>()

    override fun <P> submit(options: SubmitWorkOptions<P>): Result<Worker<P, Any?>> {
        val worker = factory.createWorker<P, Any?>(options) ?: return noWorkerRegistered(options)

        val entry = ledgerEntryOf(options)

        val progress = ProgressBus()
        worker.doWork(options.params, progress).onUpdate(progress) {
            entry.progress[options.name] = it
        }.then {
            entry.progress.remove(options.name)
        }

        return Success(worker)
    }

    override fun hasWorkScheduled(type: String, topic: String?): Boolean = workerLedger.firstOrNull {
        it.topic == topic && it.type == type
    } != null

    override fun liveWorkProgress(type: String, topic: String?) = ledgerEntryOf(type, topic, null).progress

    private fun ledgerEntryOf(options: SubmitWorkOptions<Any?>) = ledgerEntryOf(options.type, options.topic, options)

    private fun ledgerEntryOf(
        type: String,
        topic: String?,
        options: SubmitWorkOptions<Any?>?
    ): WorkerLedger = workerLedger.firstOrNull {
        it.topic == topic && it.type == type
    } ?: WorkerLedger(
        type = type,
        topic = topic,
        progress = if (options != null) mutableLiveMapOf(options.name to ProgressState.initial()) else mutableLiveMapOf()
    ).also {
        workerLedger.add(it)
    }

    private fun <P> noWorkerRegistered(options: SubmitWorkOptions<Any?>): Result<Worker<P, Any?>> {
        val message = "Failed to create worker ${options.type}->${options.name}"
        return Failure(IllegalArgumentException(message))
    }
}