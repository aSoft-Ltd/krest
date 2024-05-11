package krest

/**
 * Register a task factory that will be used to execute tasks of the given type.
 */
inline fun <reified T : Task<*>> TaskManager.register(noinline factory: () -> T) {
    register(T::class, factory)
}