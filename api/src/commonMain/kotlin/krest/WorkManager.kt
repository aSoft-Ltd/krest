package krest

import kase.Result
import koncurrent.ProgressState
import krest.params.SubmitWorkOptions
import live.LiveMap

interface WorkManager {
    fun <P, R> submit(options: SubmitWorkOptions<P>): Result<Worker<P, R>>

    fun liveWorkProgress(scope: String): LiveMap<String, ProgressState>
}