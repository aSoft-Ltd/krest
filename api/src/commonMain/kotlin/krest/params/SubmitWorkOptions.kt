package krest.params

import kotlinx.serialization.Serializable

@Serializable
class SubmitWorkOptions<out P>(
    val type: String,
    val topic: String?,
    val name: String,
    val params: P
)