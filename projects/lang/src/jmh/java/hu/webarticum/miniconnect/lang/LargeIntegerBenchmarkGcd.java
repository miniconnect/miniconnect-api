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
 * Measures the performance of the gcd and isRelativelyPrimeTo method.
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeIntegerBenchmarkGcd {

    @Param({"-17", "-1", "0", "1", "2", "3", "4", "5", "6", "100", "5472845709"})
    private String aStr;

    @Param({"-17", "-1", "0", "1", "2", "3", "4", "5", "6", "100", "5472845709"})
    private String bStr;
    
    private BigInteger bigIntegerA;

    private BigInteger bigIntegerB;

    private LargeInteger largeIntegerA;

    private LargeInteger largeIntegerB;

    private BigInt scalaBigIntA;
    
    private BigInt scalaBigIntB;
    
    private SafeLong spireSafeLongA;
    
    private SafeLong spireSafeLongB;
    
    private org.jscience.mathematics.number.LargeInteger jscienceLargeIntegerA;
    
    private org.jscience.mathematics.number.LargeInteger jscienceLargeIntegerB;
    

    @Setup(Level.Iteration)
    public void setup() {
        bigIntegerA = new BigInteger(aStr);
        bigIntegerB = new BigInteger(bStr);
        largeIntegerA = LargeInteger.of(aStr);
        largeIntegerB = LargeInteger.of(bStr);
        scalaBigIntA = BigInt.apply(aStr);
        scalaBigIntB = BigInt.apply(bStr);
        spireSafeLongA = SafeLong.apply(aStr);
        spireSafeLongB = SafeLong.apply(bStr);
        jscienceLargeIntegerA = org.jscience.mathematics.number.LargeInteger.valueOf(aStr);
        jscienceLargeIntegerB = org.jscience.mathematics.number.LargeInteger.valueOf(bStr);
    }


    @Benchmark
    public void benchmarkBigInteger(Blackhole blackhole) {
        blackhole.consume(bigIntegerA.gcd(bigIntegerB));
    }

    @Benchmark
    public void benchmarkLargeInteger(Blackhole blackhole) {
        blackhole.consume(largeIntegerA.gcd(largeIntegerB));
    }

    @Benchmark
    public void benchmarkLargeIntegerIsRelativelyPrimeTo(Blackhole blackhole) {
        blackhole.consume(largeIntegerA.isRelativelyPrimeTo(largeIntegerB));
    }

    @Benchmark
    public void benchmarkScalaBigInt(Blackhole blackhole) {
        blackhole.consume(scalaBigIntA.gcd(scalaBigIntB));
    }

    @Benchmark
    public void benchmarkSpireSafeLong(Blackhole blackhole) {
        blackhole.consume(spireSafeLongA.gcd(spireSafeLongB));
    }

    @Benchmark
    public void benchmarkJscienceLargeInteger(Blackhole blackhole) {
        blackhole.consume(jscienceLargeIntegerA.gcd(jscienceLargeIntegerB));
    }

}
