import kase.progress.Progress
import kommander.expect
import koncurrent.Later
import koncurrent.later
import koncurrent.toLater
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import krest.Task
import krest.TaskManager
import krest.TaskSubmitOptions
import krest.register
import kotlin.math.round
import kotlin.test.Test

class TaskManagerTest {

    class ConsolePrinterTask : Task<Person>() {
        override fun execute(params: Person): Later<Unit> {
            return println("Hello ${params.name}").toLater()
        }
    }

    class Person(val name: String)

    @Test
    fun should_be_able_to_execute_a_task_with_arguments() {
        val tasks = TaskManager()
        tasks.register { ConsolePrinterTask() }
        val options = TaskSubmitOptions(ConsolePrinterTask::class, Person("John Doe"))
        tasks.submit(options)
    }

    class ProgressData(
        override val done: Long,
        override val total: Long
    ) : Progress {
        override val doneAmountAsDouble: Double
            get() = TODO("Not yet implemented")
        override val totalAmountAsDouble: Double
            get() = TODO("Not yet implemented")
        override val doneFraction: Double
            get() = TODO("Not yet implemented")
        override val remainingFraction: Double
            get() = TODO("Not yet implemented")
        override val donePercentage: Double get() = round((done.toDouble() / total.toDouble()) * 100)
        override val remainingPercentage: Double get() = 100.0 - donePercentage
    }

    class AsyncConsolePrinterTask(
        private val scope: CoroutineScope,
        private val duration: Long
    ) : Task<Person>() {
        override fun execute(params: Person): Later<Unit> = scope.later(context = Dispatchers.Default) {
            var passed = 0L
            while (passed < duration) {
                delay(1000)
                passed += 1000
                update(ProgressData(passed, duration))
            }
            println("Hello ${params.name}").toLater()
        }
    }

    @Test
    fun should_register_a_truly_async_task() = runTest {
        val tasks = TaskManager()
        tasks.register { AsyncConsolePrinterTask(this, 1000) }

        val options = TaskSubmitOptions(AsyncConsolePrinterTask::class, Person("John Async Doe"))

        repeat(5) {
            tasks.submit(options)
        }
    }

    @Test
    fun should_be_able_to_get_running_tasks() = runTest {
        val tasks = TaskManager()
        var count = 0
        tasks.register { AsyncConsolePrinterTask(this, (1000 * (++count)).toLong()) }


        repeat(5) {
            val options = TaskSubmitOptions(AsyncConsolePrinterTask::class, Person("John Async Doe ${it + 1}"))
            tasks.submit(options)
        }

        val running = tasks.running()
        expect(running.size).toBe(5)
        withContext(Dispatchers.Default) {
            repeat(5) {
                delay(1000)
                println("Running tasks: ${running.size}")
            }
        }
        expect(running.size).toBe(0)
    }

    @Test
    fun should_be_able_to_schedule_tasks_conditionally() = runTest {
        val tasks = TaskManager()
        tasks.register { AsyncConsolePrinterTask(this, 2000) }
        val options = TaskSubmitOptions(AsyncConsolePrinterTask::class, Person("John Doe"), name = "John Doe")

        repeat(5) {
            println("Attempting to submit task")
            if (tasks.isRunning(options)) {
                println("Task is already running")
                println("Aborting submission")
            } else {
                println("Task is not running. Submitting")
                tasks.submit(options)
            }
            println("=====================================")
            expect(tasks.running().size).toBe(1)
            withContext(Dispatchers.Default) {
                delay(1000)
            }
        }
    }

    @Test
    fun should_be_able_to_observe_task_progress() = runTest {
        val tasks = TaskManager()
        tasks.register { AsyncConsolePrinterTask(this, 8500) }
        val options = TaskSubmitOptions(AsyncConsolePrinterTask::class, Person("John Doe"), name = "John Doe")
        tasks.submit(options)
        val watcher = tasks.watch(options) {
            println("Progress: ${it.donePercentage}")
        }
        withContext(Dispatchers.Default) { delay(7000) }
        watcher?.stop()
    }
}