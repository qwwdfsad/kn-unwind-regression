import kotlin.concurrent.Volatile

private class ProfileException : Exception()

@Volatile
private var profileThrowableSink: Throwable? = null

fun main(args: Array<String>) {
    val mode = args.getOrNull(0) ?: "bench-fresh-ref"
    val depth = args.getOrNull(1)?.toInt() ?: 50
    val iterations = args.getOrNull(2)?.toInt() ?: 5_000_000

    when (mode) {
        "fresh" -> repeat(iterations) {
            try {
                freshThrow(depth)
            } catch (_: ProfileException) {
            }
        }

        "construct" -> repeat(iterations) {
            profileThrowableSink = constructOnly(depth)
        }

        "prebuilt" -> repeat(iterations) {
            val exception = ProfileException()
            try {
                throwExisting(depth, exception)
            } catch (_: ProfileException) {
            }
        }

        "bench-fresh-ref" -> {
            val benchmark = Bench().also { it.depthValue = depth }
            val fn: (Bench) -> Unit =
                Bench::plainExceptionWithRecursion
            repeat(iterations) {
                fn(benchmark)
            }
        }

        else -> error("Unknown mode: $mode")
    }

    println("done")
}

private fun freshThrow(depth: Int) {
    if (depth == 0) {
        throw ProfileException()
    } else {
        freshThrow(depth - 1)
    }
}

private fun constructOnly(depth: Int): ProfileException {
    return if (depth == 0) {
        ProfileException()
    } else {
        constructOnly(depth - 1)
    }
}

private fun throwExisting(depth: Int, exception: ProfileException) {
    if (depth == 0) {
        throw exception
    } else {
        throwExisting(depth - 1, exception)
    }
}
