package krest

import koncurrent.Later

interface Worker<in P, out R> {
    fun doWork(params: P): Later<R>
}