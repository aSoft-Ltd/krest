package krest

import kotlin.reflect.KClass

open class TaskSubmitOptions<P, out T : Task<P>>(
    override val task: KClass<out T>,
    open val params: P,
    override val name: String? = null
) : TaskIdentity<T>