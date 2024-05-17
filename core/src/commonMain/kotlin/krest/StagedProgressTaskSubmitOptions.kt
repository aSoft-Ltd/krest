package krest

import kotlin.reflect.KClass

class StagedProgressTaskSubmitOptions<P, T : StagedProgressTask<P, R>, R : Any>(
    override val task: KClass<out T>,
    override val params: P,
    override val name: String? = null
) : TaskSubmitOptions<P, T>(task, params, name), StagedProgressTaskIdentity<T, R>