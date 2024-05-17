package krest

interface LinearProgressTaskIdentity<out T : LinearProgressTask<*, R>, R : Any> : TaskIdentity<T>