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
 * Measures the performance of the pow method.
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeIntegerBenchmarkOfStr {

    @Param({
        "-8273406478276237487029746",
        "-9223372036854775809",
        "-9223372036854775808",
        "-3263418",
        "-1",
        "0",
        "1",
        "2",
        "123",
        "4623702",
        "9223372036854775807",
        "9223372036854775808",
        "8073627361860313673617294",
    })
    private String valueStr;


    @Benchmark
    public void benchmarkBigInteger(Blackhole blackhole) {
        blackhole.consume(new BigInteger(valueStr));
    }

    @Benchmark
    public void benchmarkLargeInteger(Blackhole blackhole) {
        blackhole.consume(LargeInteger.of(valueStr));
    }

    @Benchmark
    public void benchmarkScalaBigInt(Blackhole blackhole) {
        blackhole.consume(BigInt.apply(valueStr));
    }

    @Benchmark
    public void benchmarkSpireSafeLong(Blackhole blackhole) {
        blackhole.consume(SafeLong.apply(valueStr));
    }

    @Benchmark
    public void benchmarkApfloatApint(Blackhole blackhole) {
        blackhole.consume(new Apint(valueStr));
    }

    @Benchmark
    public void benchmarkJscienceLargeInteger(Blackhole blackhole) {
        blackhole.consume(org.jscience.mathematics.number.LargeInteger.valueOf(valueStr));
    }

    @Benchmark
    public void benchmarkLibjBigInt(Blackhole blackhole) {
        blackhole.consume(org.libj.math.BigInt.valueOf(valueStr));
    }

    @Benchmark
    public void benchmarkHuldraBigInt(Blackhole blackhole) {
        blackhole.consume(new org.huldra.math.BigInt(valueStr));
    }

}
