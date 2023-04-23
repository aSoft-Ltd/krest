package krest

import krest.params.SubmitWorkOptions

object VoidWorkerFactory : WorkerFactory {
    override fun <P, R> createWorker(options: SubmitWorkOptions<P>): Worker<P, R>? = null
}