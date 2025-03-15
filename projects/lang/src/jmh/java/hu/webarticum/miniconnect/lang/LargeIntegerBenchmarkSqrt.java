package hu.webarticum.miniconnect.lang;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

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

import com.google.common.math.BigIntegerMath;

import clojure.lang.BigInt;

/**
 * Measures the performance of the sqrt method.
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeIntegerBenchmarkSqrt {
    
    @Param({
            "0", "1", "2", "3", "17", "25", "64", "100", "121", "147",
            "3262", "2348763", "5550736", "123567432", "9223372036854775807",
            "6127348745628736671870746732",
            "6407283476183409783461873347673306188236174",
            "21416545397522634523928076988623985594776896183857352521",
    })
    private String value;
    
    private LargeInteger largeInteger;
    
    private BigInteger bigInteger;
    
    private org.jscience.mathematics.number.LargeInteger jscienceLargeInteger;
    
    private BigInt clojureBigInt;
    

    @Setup(Level.Trial)
    public void setup() {
        largeInteger = LargeInteger.of(value);
        bigInteger = new BigInteger(value);
        jscienceLargeInteger = org.jscience.mathematics.number.LargeInteger.valueOf(value);
        clojureBigInt = BigInt.fromBigInteger(bigInteger);
    }

    @Benchmark
    public void benchmarkLargeIntegerSqrt(Blackhole blackhole) {
        blackhole.consume(largeInteger.sqrt());
    }

    @Benchmark
    public void benchmarkGuavaSqrtOnBigInteger(Blackhole blackhole) {
        blackhole.consume(BigIntegerMath.sqrt(bigInteger, RoundingMode.FLOOR));
    }

    @Benchmark
    public void benchmarkJscienceLargeIntegerSqrt(Blackhole blackhole) {
        blackhole.consume(jscienceLargeInteger.sqrt());
    }

    @Benchmark
    public void benchmarkClojureBigIntSqrt(Blackhole blackhole) {
        blackhole.consume(ClojureArithmetic.sqrt(clojureBigInt));
    }
    
}
