package hu.webarticum.miniconnect.lang;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import scala.math.BigInt;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Measures the performance of the toByteArray method.
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeIntegerBenchmarkToByteArray {

    @Param({
        "-8273406478276237487029746",
        "-9223372036854775809",
        "-9223372036854775808",
        "-47087341237854",
        "-3263418",
        "-1",
        "0",
        "1",
        "2",
        "123",
        "4623702",
        "7408723477454",
        "9223372036854775807",
        "9223372036854775808",
        "8073627361860313673617294",
    })
    private String valueStr;


    private BigInteger bigIntegerValue;

    private LargeInteger largeIntegerValue;

    private BigInt scalaBigIntValue;
    
    private org.jscience.mathematics.number.LargeInteger jscienceLargeIntegerValue;
    
    private org.libj.math.BigInt libjBitIntValue;
    

    @Setup(Level.Iteration)
    public void setup() {
        bigIntegerValue = new BigInteger(valueStr);
        largeIntegerValue = LargeInteger.of(valueStr);
        scalaBigIntValue = BigInt.apply(valueStr);
        jscienceLargeIntegerValue = org.jscience.mathematics.number.LargeInteger.valueOf(valueStr);
        libjBitIntValue = new org.libj.math.BigInt(valueStr);
    }

    
    @Benchmark
    public void benchmarkBigInteger(Blackhole blackhole) {
        blackhole.consume(bigIntegerValue.toByteArray());
    }

    @Benchmark
    public void benchmarkLargeInteger(Blackhole blackhole) {
        blackhole.consume(largeIntegerValue.toByteArray());
    }

    public void benchmarkScalaBigInt(Blackhole blackhole) {
        blackhole.consume(scalaBigIntValue.toByteArray());
    }

    @Benchmark
    public void benchmarkJscienceLargeInteger(Blackhole blackhole) {
        int byteLength = jscienceLargeIntegerValue.bitLength() / 8;
        blackhole.consume(jscienceLargeIntegerValue.toByteArray(new byte[byteLength], 0));
    }

    @Benchmark
    public void benchmarkLibjBigInt(Blackhole blackhole) {
        blackhole.consume(libjBitIntValue.toByteArray(false));
    }

}
