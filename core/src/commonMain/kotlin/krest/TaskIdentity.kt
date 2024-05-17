package krest

import kotlin.reflect.KClass

interface TaskIdentity<out T : Task<*>> {
    val task: KClass<out T>
    val name: String?
}