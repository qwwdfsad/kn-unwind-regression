import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.BenchmarkMode
import kotlinx.benchmark.BenchmarkTimeUnit
import kotlinx.benchmark.Measurement
import kotlinx.benchmark.Mode
import kotlinx.benchmark.OutputTimeUnit
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import kotlin.concurrent.Volatile
import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
@State(Scope.Benchmark)
@Measurement(iterations = 3, time = 1, timeUnit = BenchmarkTimeUnit.SECONDS)
@OutputTimeUnit(BenchmarkTimeUnit.MICROSECONDS)
@BenchmarkMode(Mode.AverageTime)
class RegressionIsolationBenchmarks {
    private var throwableSink: Throwable? = null
    private var intSink: Int = 0

    @Param("50")
    var depthValue: Int = 0

    @Benchmark
    fun plainExceptionNoRecursion() {
        try {
            throw TestException()
        } catch (_: TestException) {
        }
    }

    @Benchmark
    fun constructExceptionNoRecursion() {
        throwableSink = TestException()
        intSink = throwableSink!!.getStackTrace().size
    }

    @Benchmark
    fun constructExceptionWithRecursion() {
        throwableSink = plainRecursionCreateException(depthValue)
        intSink = throwableSink!!.getStackTrace().size
    }

    @Benchmark
    fun constructExceptionNoRecursionVolatile() {
        volatileThrowableSink = TestException()
    }

    @Benchmark
    fun constructExceptionWithRecursionVolatile() {
        volatileThrowableSink = plainRecursionCreateException(depthValue)
    }

    @Benchmark
    fun plainExceptionWithRecursion() {
        try {
            plainRecursionThrow(depthValue)
        } catch (_: TestException) {
        }
    }

    @Benchmark
    fun plainPrebuiltExceptionWithRecursion() {
        val exception = TestException()
        try {
            plainRecursionThrowExisting(depthValue, exception)
        } catch (_: TestException) {
        }
    }

    @Benchmark
    fun constructExceptionAtLeafThenThrowAtTop() {
        try {
            throw plainRecursionCreateException(depthValue)
        } catch (_: TestException) {
        }
    }

    @Benchmark
    fun suspendExceptionWithRecursion() = runBlocking {
        launch {
            try {
                suspendRecursionThrow(depthValue)
            } catch (_: TestException) {
            }
        }
    }

    @Benchmark
    fun cancellationExceptionWithRecursion() {
        try {
            plainRecursionThrowCancellation(depthValue)
        } catch (_: CancellationException) {
        }
    }

    @Benchmark
    fun prebuiltCancellationExceptionWithRecursion() {
        val exception = CancellationException()
        try {
            plainRecursionThrowExistingCancellation(depthValue, exception)
        } catch (_: CancellationException) {
        }
    }

    private suspend fun suspendRecursionWithYields(depth: Int) {
        if (depth == 0) {
            yield()
            yield()
        } else {
            suspendRecursionWithYields(depth - 1)
        }
    }

    private fun plainRecursionThrow(depth: Int) {
        if (depth == 0) {
            throw TestException()
        } else {
            plainRecursionThrow(depth - 1)
        }
    }

    private fun plainRecursionCreateException(depth: Int): TestException {
        return if (depth == 0) {
            TestException()
        } else {
            plainRecursionCreateException(depth - 1)
        }
    }

    private fun plainRecursionThrowExisting(depth: Int, exception: TestException) {
        if (depth == 0) {
            throw exception
        } else {
            plainRecursionThrowExisting(depth - 1, exception)
        }
    }

    private suspend fun suspendRecursionThrow(depth: Int) {
        if (depth == 0) {
            throw TestException()
        } else {
            suspendRecursionThrow(depth - 1)
        }
    }

    private fun plainRecursionThrowCancellation(depth: Int) {
        if (depth == 0) {
            throw CancellationException()
        } else {
            plainRecursionThrowCancellation(depth - 1)
        }
    }

    private fun plainRecursionThrowExistingCancellation(
        depth: Int,
        exception: CancellationException,
    ) {
        if (depth == 0) {
            throw exception
        } else {
            plainRecursionThrowExistingCancellation(depth - 1, exception)
        }
    }
}

private class TestException : Exception()

@Volatile
private var volatileThrowableSink: Throwable? = null
