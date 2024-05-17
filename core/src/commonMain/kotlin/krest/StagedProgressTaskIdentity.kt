package krest

interface StagedProgressTaskIdentity<out T : Task<*>, R> : TaskIdentity<T>