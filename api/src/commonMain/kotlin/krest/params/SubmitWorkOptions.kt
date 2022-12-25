package krest.params

import kotlinx.serialization.Serializable

@Serializable
class SubmitWorkOptions<out P>(
    val scope: String,
    val name: String,
    val params: P
)