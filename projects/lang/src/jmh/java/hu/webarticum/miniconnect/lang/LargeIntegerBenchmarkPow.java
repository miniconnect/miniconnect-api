package hu.webarticum.miniconnect.lang;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import scala.math.BigInt;
import spire.math.SafeLong;

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
 * Measures the performance of the pow method.
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeIntegerBenchmarkPow {
 
    @Param({"1", "2", "3", "17", "23647", "17327046823", "73940723641827470863472481630873"})
    private String baseStr;

    @Param({"1", "2", "3", "4", "5", "10", "25"})
    private int exponent;
    
    private long longBase;

    private BigInteger bigIntegerBase;

    private LargeInteger largeIntegerBase;
    
    private BigInt scalaBigIntBase;
    
    private SafeLong spireSafeLongBase;
    
    private org.jscience.mathematics.number.LargeInteger jscienceLargeIntegerBase;
    

    @Setup(Level.Iteration)
    public void setup() {
        bigIntegerBase = new BigInteger(baseStr);
        longBase = bigIntegerBase.longValue(); // can overflow
        largeIntegerBase = LargeInteger.of(baseStr);
        scalaBigIntBase = BigInt.apply(baseStr);
        spireSafeLongBase = SafeLong.apply(baseStr);
        jscienceLargeIntegerBase = org.jscience.mathematics.number.LargeInteger.valueOf(baseStr);
    }


    @Benchmark
    public void benchmarkLongWithMathPow(Blackhole blackhole) {
        blackhole.consume(Math.pow(longBase, exponent)); // can overflow
    }

    @Benchmark
    public void benchmarkBigInteger(Blackhole blackhole) {
        blackhole.consume(bigIntegerBase.pow(exponent));
    }

    @Benchmark
    public void benchmarkLargeInteger(Blackhole blackhole) {
        blackhole.consume(largeIntegerBase.pow(exponent));
    }

    @Benchmark
    public void benchmarkScalaBigInt(Blackhole blackhole) {
        blackhole.consume(scalaBigIntBase.pow(exponent));
    }

    @Benchmark
    public void benchmarkSpireSafeLong(Blackhole blackhole) {
        blackhole.consume(spireSafeLongBase.pow(exponent));
    }

    @Benchmark
    public void benchmarkJscienceLargeInteger(Blackhole blackhole) {
        blackhole.consume(jscienceLargeIntegerBase.pow(exponent));
    }

}
