package krest

import kase.ExecutorState
import kase.Result
import krest.params.SubmitWorkOptions
import live.LiveMap

interface WorkManager {
    fun <P> submit(options: SubmitWorkOptions<P>): Result<Worker<P, *>>

    fun liveWorkProgress(type: String, topic: String?): LiveMap<String, ExecutorState<Any?>>
}