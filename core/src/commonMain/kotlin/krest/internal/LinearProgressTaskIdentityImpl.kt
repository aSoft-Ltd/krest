package krest.internal

import krest.LinearProgressTask
import krest.LinearProgressTaskIdentity
import krest.Task
import krest.TaskIdentity
import kotlin.reflect.KClass

@PublishedApi
internal class LinearProgressTaskIdentityImpl<T : LinearProgressTask<*, R>, R : Any>(
    override val task: KClass<out T>,
    override val name: String?
) : LinearProgressTaskIdentity<T, R>