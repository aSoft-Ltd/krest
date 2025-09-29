package krest

import kase.progress.ProgressBus
import kase.progress.VoidProgressBus
import koncurrent.Later
import koncurrent.awaited.then
import koncurrent.awaited.andThen
import koncurrent.awaited.andZip
import koncurrent.awaited.zip
import koncurrent.awaited.catch

interface Worker<in P, out R> {
    fun doWork(params: P, progress: ProgressBus = VoidProgressBus): Later<R>
}