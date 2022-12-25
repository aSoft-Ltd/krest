package krest

import kase.Result
import krest.params.SubmitWorkOptions
import live.LiveMap

inline fun <P, R> WorkManager.submit(
    scope: String,
    name: String,
    params: P
): Result<Worker<P, R>> = submit(SubmitWorkOptions(scope, name, params))