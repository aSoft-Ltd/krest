package krest

import krest.internal.TaskManagerImpl
import kotlin.js.JsName

@JsName("taskManagerOf")
fun TaskManager(): TaskManager = TaskManagerImpl()