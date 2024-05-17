package krest

import koncurrent.Later

abstract class Task<in P> {
    abstract fun execute(params: P): Later<Unit>
}