package krest.params

import kotlinx.serialization.Serializable
import krest.Worker

@Serializable
class SubmitWorkOptions<out P, out R>(
    val scope: String,
    val params: P,
    val worker: Worker<R>
)