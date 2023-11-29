package krest

import kase.ExecutorState
import kase.Result
import krest.params.SubmitWorkOptions
import cinematic.LiveMap

interface WorkManager {
    fun <P> submit(options: SubmitWorkOptions<P>): Result<Worker<P, Any?>>

    fun hasWorkScheduled(type: String, topic: String?): Boolean

    fun liveWorkProgress(type: String, topic: String?): LiveMap<String, ExecutorState<Any?>>
}