import kase.Result
import kase.Success
import kase.progress.ProgressBus
import kommander.expect
import kommander.toBe
import kotlinx.coroutines.test.runTest
import krest.ImmediateWorkManager
import krest.VoidWorkManager
import krest.WorkManager
import krest.Worker
import krest.WorkerFactory
import krest.params.SubmitWorkOptions
import kotlin.test.Test

class ImmediateWorkManagerTest {

    class TestWorker : Worker<Any, Any> {
        override suspend fun doWork(params: Any, progress: ProgressBus): Any {
            println("Working with params: P")
            return Unit
        }
    }

    class TestWorkerFactory : WorkerFactory {
        override fun <P, R> createWorker(options: SubmitWorkOptions<P>) = TestWorker() as Worker<P, R>
    }

    @Test
    fun can_observe_a_work_manager_with_a_null_topic() {
        val live = VoidWorkManager.liveWorkProgress("test", null)
        expect(live).toBeNonNull()
    }

    @Test
    fun can_submit_a_work() = runTest {
        val manager: WorkManager = ImmediateWorkManager(TestWorkerFactory())
        manager.liveWorkProgress("test", null)

        val options = SubmitWorkOptions(
            type = "test",
            topic = "test",
            name = "test",
            params = Unit
        )

        val res = manager.submit(options)
        expect<Result<Worker<Unit, Any?>>>(res).toBe<Success<Any?>>()
    }
}