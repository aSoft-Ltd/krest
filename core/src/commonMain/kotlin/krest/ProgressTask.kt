package krest

abstract class ProgressTask<in P, R : Any> : Task<P>() {
    internal abstract fun removeAllWatchers()
}