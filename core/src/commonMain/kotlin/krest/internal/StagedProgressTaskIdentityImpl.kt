package krest.internal

import krest.StagedProgressTask
import krest.StagedProgressTaskIdentity
import krest.Task
import kotlin.reflect.KClass

@PublishedApi
internal class StagedProgressTaskIdentityImpl<T : StagedProgressTask<*, R>, R : Any>(
    override val task: KClass<out T>,
    override val name: String?
) : StagedProgressTaskIdentity<T, R>