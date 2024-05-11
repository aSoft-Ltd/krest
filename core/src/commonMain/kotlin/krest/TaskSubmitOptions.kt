package krest

import kotlin.reflect.KClass

class TaskSubmitOptions<P, T : Task<P>>(
    val task: KClass<T>,
    val params: P,
    val name: String? = null
)