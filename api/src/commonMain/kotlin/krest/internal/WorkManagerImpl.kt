package krest.internal

import koncurrent.Later
import krest.WorkManager
import krest.params.SubmitWorkOptions

class WorkManagerImpl : WorkManager {
    override fun <P, R> submit(options: SubmitWorkOptions<P, R>): Later<String> {
        return Later.reject(NotImplementedError("Unimplemented submit work"))
    }
}