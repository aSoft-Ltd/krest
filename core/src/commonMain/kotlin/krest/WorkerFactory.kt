package krest

import krest.params.SubmitWorkOptions

interface WorkerFactory {
    fun <P, R> createWorker(options: SubmitWorkOptions<P>): Worker<P,R>?
}