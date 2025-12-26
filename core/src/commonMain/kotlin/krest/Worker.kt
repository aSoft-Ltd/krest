package krest

import kase.progress.ProgressBus
import kase.progress.VoidProgressBus

interface Worker<in P, out R> {
    suspend fun doWork(params: P, progress: ProgressBus = VoidProgressBus): R
}