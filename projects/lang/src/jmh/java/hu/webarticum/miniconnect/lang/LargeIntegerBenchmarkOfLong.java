package hu.webarticum.miniconnect.lang;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import scala.math.BigInt;
import spire.math.SafeLong;

import org.apfloat.Apint;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Measures the performance of the of(long) method.
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeIntegerBenchmarkOfLong {

    @Param({"-42364780746", "-1", "0", "1", "60724637814"})
    private long longValue;


    @Benchmark
    public void benchmarkBigInteger(Blackhole blackhole) {
        blackhole.consume(BigInteger.valueOf(longValue));
    }

    @Benchmark
    public void benchmarkLargeInteger(Blackhole blackhole) {
        blackhole.consume(LargeInteger.of(longValue));
    }

    @Benchmark
    public void benchmarkScalaBigInt(Blackhole blackhole) {
        blackhole.consume(BigInt.apply(longValue));
    }

    @Benchmark
    public void benchmarkSpireSafeLong(Blackhole blackhole) {
        blackhole.consume(SafeLong.apply(longValue));
    }

    @Benchmark
    public void benchmarkApfloatApint(Blackhole blackhole) {
        blackhole.consume(new Apint(longValue));
    }

    @Benchmark
    public void benchmarkJscienceLargeInteger(Blackhole blackhole) {
        blackhole.consume(org.jscience.mathematics.number.LargeInteger.valueOf(longValue));
    }

    @Benchmark
    public void benchmarkLibjBigInt(Blackhole blackhole) {
        blackhole.consume(org.libj.math.BigInt.valueOf(longValue));
    }

    @Benchmark
    public void benchmarkHuldraBigInt(Blackhole blackhole) {
        blackhole.consume(new org.huldra.math.BigInt(longValue));
    }

}
