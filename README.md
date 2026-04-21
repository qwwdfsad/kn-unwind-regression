- benchmark: `./gradlew macosArm64Benchmark -Pkotlin.version=<version> --rerun-tasks`
- manual: `./gradlew linkProfileReleaseExecutableMacosArm64 -Pkotlin.version=<version> --rerun-tasks then build/bin/macosArm64/profileReleaseExecutable/profile.kexe bench-fresh-ref 50 100000`

The benchmarkl you are interested in is `plainExceptionWithRecursion`

On my machine:

2.3.20:
```
macosArm64 summary:
Benchmark                                                                 (depthValue)  Mode  Cnt    Score   Error  Units
RegressionIsolationBenchmarks.cancellationExceptionWithRecursion                    50  avgt    5   27.194 ± 0.446  us/op
RegressionIsolationBenchmarks.constructExceptionAtLeafThenThrowAtTop                50  avgt    5    0.852 ± 0.022  us/op
RegressionIsolationBenchmarks.constructExceptionNoRecursion                         50  avgt    5   68.905 ± 0.522  us/op
RegressionIsolationBenchmarks.constructExceptionWithRecursion                       50  avgt    5  574.370 ± 2.126  us/op
RegressionIsolationBenchmarks.plainExceptionNoRecursion                             50  avgt    5    0.167          us/op
RegressionIsolationBenchmarks.plainExceptionWithRecursion                           50  avgt    5   25.902 ± 0.387  us/op
RegressionIsolationBenchmarks.plainPrebuiltExceptionWithRecursion                   50  avgt    5    3.183 ± 0.029  us/op
RegressionIsolationBenchmarks.prebuiltCancellationExceptionWithRecursion            50  avgt    5    3.171 ± 0.017  us/op
RegressionIsolationBenchmarks.suspendExceptionWithRecursion                         50  avgt    5   27.289 ± 0.135  us/op
```

2.4.0-Beta1:
```
Benchmark                                                                 (depthValue)  Mode  Cnt    Score   Error  Units
RegressionIsolationBenchmarks.cancellationExceptionWithRecursion                    50  avgt    5   41.503 ± 0.950  us/op
RegressionIsolationBenchmarks.constructExceptionAtLeafThenThrowAtTop                50  avgt    5    0.833 ± 0.026  us/op
RegressionIsolationBenchmarks.constructExceptionNoRecursion                         50  avgt    5   67.507 ± 0.276  us/op

RegressionIsolationBenchmarks.constructExceptionWithRecursion                       50  avgt    5  584.207 ± 4.520  us/op
RegressionIsolationBenchmarks.plainExceptionNoRecursion                             50  avgt    5    0.163 ± 0.001  us/op
RegressionIsolationBenchmarks.plainExceptionWithRecursion                           50  avgt    5   40.159 ± 0.238  us/op
RegressionIsolationBenchmarks.plainPrebuiltExceptionWithRecursion                   50  avgt    5    4.125 ± 0.040  us/op
RegressionIsolationBenchmarks.prebuiltCancellationExceptionWithRecursion            50  avgt    5    4.113 ± 0.043  us/op
RegressionIsolationBenchmarks.suspendExceptionWithRecursion                         50  avgt    5   42.351 ± 0.339  us/op
```
