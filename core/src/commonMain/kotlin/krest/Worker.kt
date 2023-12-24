package krest

import koncurrent.Later
import koncurrent.later.then
import koncurrent.later.andThen
import koncurrent.later.andZip
import koncurrent.later.zip
import koncurrent.later.catch

interface Worker<in P, out R> {
    fun doWork(params: P): Later<R>
}