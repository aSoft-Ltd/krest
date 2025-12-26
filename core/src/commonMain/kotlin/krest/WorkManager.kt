package krest

import cinematic.LiveMap
import kase.Result
import kase.progress.ProgressState
import krest.params.SubmitWorkOptions

interface WorkManager {
    suspend fun <P> submit(options: SubmitWorkOptions<P>): Result<Worker<P, Any?>>

    fun hasWorkScheduled(type: String, topic: String?): Boolean

    fun liveWorkProgress(type: String, topic: String?): LiveMap<String, ProgressState>
}