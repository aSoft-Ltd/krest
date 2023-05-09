@file:JsExport
@file:Suppress("NON_EXPORTABLE_TYPE")

package krest

import kase.ExecutorState
import cinematic.MutableLiveMap
import kotlin.js.JsExport

class WorkerLedger(
    val type: String,
    val topic: String?,
    val progress: MutableLiveMap<String, ExecutorState<Any?>>
)