package arrow.benchmarks

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.CompilerControl
import org.openjdk.jmh.annotations.Fork
import org.openjdk.jmh.annotations.Measurement
import org.openjdk.jmh.annotations.Param
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Warmup
import java.util.concurrent.TimeUnit

@State(Scope.Thread)
@Fork(2)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10)
@CompilerControl(CompilerControl.Mode.DONT_INLINE)
open class AttemptNonRaised {

  @Param("10000")
  var size: Int = 0

  @Benchmark
  fun cats(): Any =
    arrow.benchmarks.effects.scala.cats.AttemptNonRaised.ioLoopHappy(size, 0).unsafeRunSync()

  @Benchmark
  fun zio(): Any =
    arrow.benchmarks.effects.scala.zio.AttemptNonRaised.run(size)

  @Benchmark
  fun kio(): Int =
    arrow.benchmarks.effects.kio.AttemptNonRaised.attemptNonRaised(size)
}