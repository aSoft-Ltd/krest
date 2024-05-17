package krest.internal

import krest.Task
import krest.TaskIdentity
import kotlin.reflect.KClass

@PublishedApi
internal class TaskIdentityImpl<T : Task<*>>(
    override val task: KClass<out T>,
    override val name: String?
) : TaskIdentity<T>