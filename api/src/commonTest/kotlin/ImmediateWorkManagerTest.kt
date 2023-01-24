import expect.expect
import expect.toBe
import kase.Failure
import kase.Success
import koncurrent.Later
import krest.ImmediateWorkManager
import krest.VoidWorkManager
import krest.WorkManager
import krest.Worker
import krest.WorkerFactory
import krest.params.SubmitWorkOptions
import kotlin.test.Test
import kotlin.test.fail

class ImmediateWorkManagerTest {

    class TestWorker : Worker<Any, Any> {
        override fun doWork(params: Any): Later<Any> {
            println("Working with params: P")
            return Later.resolve(Unit)
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
    fun can_submit_a_work() {
        val manager: WorkManager = ImmediateWorkManager(TestWorkerFactory())
        manager.liveWorkProgress("test", null)

        val options = SubmitWorkOptions(
            type = "test",
            topic = "test",
            name = "test",
            params = Unit
        )

        val res = manager.submit(options)
        expect(res).toBe<Success<Any?>>()
    }
}