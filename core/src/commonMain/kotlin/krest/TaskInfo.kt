package krest

data class TaskInfo(
    val uid: String,
    val name: String,
    val task: Task<*>,
)