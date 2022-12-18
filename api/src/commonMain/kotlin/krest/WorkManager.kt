package krest

import koncurrent.Later
import krest.params.SubmitWorkOptions
import live.LiveMap

interface WorkManager {
    fun <P, R> submit(options: SubmitWorkOptions<P, R>): Later<String>

    fun loadWorkers(scope: String): LiveMap<>
}