package hu.webarticum.miniconnect.lang;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import scala.math.BigInt;
import spire.math.SafeLong;

import org.apfloat.Apint;
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
public class LargeIntegerBenchmarkMultiply {

    @Param({"-123", "-1", "0", "1", "2", "3", "15", "54378", "36244120342359", "3741618073567480545439713746"})
    private String aStr;

    @Param({"-123", "-1", "0", "1", "2", "3", "15", "54378", "36244120342359", "3741618073567480545439713746"})
    private String bStr;
    
    private long longA;
    
    private long longB;

    private BigInteger bigIntegerA;

    private BigInteger bigIntegerB;

    private LargeInteger largeIntegerA;

    private LargeInteger largeIntegerB;

    private BigInt scalaBigIntA;
    
    private BigInt scalaBigIntB;
    
    private SafeLong spireSafeLongA;
    
    private SafeLong spireSafeLongB;
    
    private Apint apfloatApintA;
    
    private Apint apfloatApintB;
    
    private org.jscience.mathematics.number.LargeInteger jscienceLargeIntegerA;
    
    private org.jscience.mathematics.number.LargeInteger jscienceLargeIntegerB;
    
    private clojure.lang.BigInt clojureBigIntA;
    
    private clojure.lang.BigInt clojureBigIntB;
    
    private org.libj.math.BigInt libjBigIntA;
    
    private org.libj.math.BigInt libjBigIntB;
    
    private org.huldra.math.BigInt huldraBigIntA;
    
    private org.huldra.math.BigInt huldraBigIntB;
    

    @Setup(Level.Iteration)
    public void setup() {
        bigIntegerA = new BigInteger(aStr);
        bigIntegerB = new BigInteger(bStr);
        longA = bigIntegerA.longValue(); // can overflow
        longB = bigIntegerB.longValue(); // can overflow
        largeIntegerA = LargeInteger.of(aStr);
        largeIntegerB = LargeInteger.of(bStr);
        scalaBigIntA = BigInt.apply(aStr);
        scalaBigIntB = BigInt.apply(bStr);
        spireSafeLongA = SafeLong.apply(aStr);
        spireSafeLongB = SafeLong.apply(bStr);
        apfloatApintA = new Apint(aStr);
        apfloatApintB = new Apint(bStr);
        jscienceLargeIntegerA = org.jscience.mathematics.number.LargeInteger.valueOf(aStr);
        jscienceLargeIntegerB = org.jscience.mathematics.number.LargeInteger.valueOf(bStr);
        clojureBigIntA = clojure.lang.BigInt.fromBigInteger(bigIntegerA);
        clojureBigIntB = clojure.lang.BigInt.fromBigInteger(bigIntegerB);
        libjBigIntA = new org.libj.math.BigInt(aStr);
        libjBigIntB = new org.libj.math.BigInt(bStr);
        huldraBigIntA = new org.huldra.math.BigInt(aStr);
        huldraBigIntB = new org.huldra.math.BigInt(bStr);
    }


    @Benchmark
    public void benchmarkLong(Blackhole blackhole) {
        blackhole.consume(longA * longB); // can overflow
    }

    @Benchmark
    public void benchmarkBigInteger(Blackhole blackhole) {
        blackhole.consume(bigIntegerA.multiply(bigIntegerB));
    }

    @Benchmark
    public void benchmarkLargeInteger(Blackhole blackhole) {
        blackhole.consume(largeIntegerA.multiply(largeIntegerB));
    }

    @Benchmark
    public void benchmarkScalaBigInt(Blackhole blackhole) {
        blackhole.consume(scalaBigIntA.$times(scalaBigIntB));
    }

    @Benchmark
    public void benchmarkSpireSafeLong(Blackhole blackhole) {
        blackhole.consume(spireSafeLongA.$times(spireSafeLongB));
    }

    @Benchmark
    public void benchmarkApfloatApint(Blackhole blackhole) {
        blackhole.consume(apfloatApintA.multiply(apfloatApintB));
    }

    @Benchmark
    public void benchmarkJscienceLargeInteger(Blackhole blackhole) {
        blackhole.consume(jscienceLargeIntegerA.times(jscienceLargeIntegerB));
    }

    @Benchmark
    public void benchmarkClojureBigInt(Blackhole blackhole) {
        blackhole.consume(clojureBigIntA.multiply(clojureBigIntB));
    }

    @Benchmark
    public void benchmarkLibjBigInt(Blackhole blackhole) {
        org.libj.math.BigInt result = libjBigIntA.clone();
        result.mul(libjBigIntB);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkHuldraBigInt(Blackhole blackhole) {
        org.huldra.math.BigInt result = huldraBigIntA.copy();
        result.mul(huldraBigIntB);
        blackhole.consume(result);
    }

}
