package krest

import koncurrent.Later

interface Worker<out P, out R> {
    val params: P
    fun doWork(): Later<R>
}