@file:Suppress("NOTHING_TO_INLINE")

package krest

import krest.internal.LinearProgressTaskIdentityImpl
import krest.internal.StagedProgressTaskIdentityImpl
import krest.internal.TaskIdentityImpl
import kotlin.reflect.KClass

inline fun <P, T : Task<P>> TaskIdentity<T>.toSubmitOptions(params: P) = TaskSubmitOptions(
    task = task,
    params = params,
    name = name
)

inline fun <P, T : LinearProgressTask<P, R>, R : Any> LinearProgressTaskIdentity<T, R>.toSubmitOptions(params: P) = LinearProgressTaskSubmitOptions(
    task = task,
    params = params,
    name = name
)

inline fun <P, T : StagedProgressTask<P, R>, R : Any> StagedProgressTaskIdentity<T, R>.toSubmitOptions(params: P) = StagedProgressTaskSubmitOptions(
    task = task,
    params = params,
    name = name
)

inline fun <T : Task<*>> TaskIdentity(
    task: KClass<T>,
    name: String?
): TaskIdentity<T> = TaskIdentityImpl(task, name)

inline fun <R : Any, T : LinearProgressTask<*, R>> KClass<T>.named(value: String): LinearProgressTaskIdentity<T, R> = LinearProgressTaskIdentityImpl(this, value)

inline fun <R : Any, T : StagedProgressTask<*, R>> KClass<T>.named(value: String): StagedProgressTaskIdentity<T, R> = StagedProgressTaskIdentityImpl(this, value)

inline fun <T : Task<*>> KClass<T>.named(value: String): TaskIdentity<T> = TaskIdentityImpl(this, value)