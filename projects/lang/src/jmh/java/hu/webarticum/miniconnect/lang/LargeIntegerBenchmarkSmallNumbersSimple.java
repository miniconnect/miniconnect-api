package hu.webarticum.miniconnect.lang;

import java.math.BigInteger;
import java.util.Random;
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
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Compares performance of LargeInteger to other integral types in a simple case.
 * 
 * <p>Based on the following expression:</p>
 * 
 * <pre>
 * ((A * B) + C) / D
 * </pre>
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeIntegerBenchmarkSmallNumbersSimple {

    private Random random = new Random();
    

    private long[] longValues;

    private BigInteger[] bigIntegerValues;

    private LargeInteger[] largeIntegerValues;
    
    private BigInt[] scalaBigIntValues;
    
    private SafeLong[] spireSafeLongValues;
    
    private Apint[] apfloatApintValues;
    
    private org.jscience.mathematics.number.LargeInteger[] jscienceLargeIntegerValues;
    
    private org.libj.math.BigInt[] libjBigIntValues;
    
    private org.huldra.math.BigInt[] huldraBigIntValues;
    

    @Setup(Level.Iteration)
    public void setup() {
        longValues = new long[] {
                random.nextInt(1000) + 200L,
                random.nextInt(20) + 10L,
                random.nextInt(10000) + 2000L,
                random.nextInt(100) + 50L,
        };

        bigIntegerValues = new BigInteger[longValues.length];
        for (int i = 0; i < longValues.length; i++) {
            bigIntegerValues[i] = BigInteger.valueOf(longValues[i]);
        }
        
        largeIntegerValues = new LargeInteger[longValues.length];
        for (int i = 0; i < longValues.length; i++) {
            largeIntegerValues[i] = LargeInteger.of(longValues[i]);
        }
        
        scalaBigIntValues = new BigInt[longValues.length];
        for (int i = 0; i < longValues.length; i++) {
            scalaBigIntValues[i] = BigInt.apply(longValues[i]);
        }
        
        spireSafeLongValues = new SafeLong[longValues.length];
        for (int i = 0; i < longValues.length; i++) {
            spireSafeLongValues[i] = SafeLong.apply(longValues[i]);
        }
        
        apfloatApintValues = new Apint[longValues.length];
        for (int i = 0; i < longValues.length; i++) {
            apfloatApintValues[i] = new Apint(longValues[i]);
        }

        jscienceLargeIntegerValues = new org.jscience.mathematics.number.LargeInteger[longValues.length];
        for (int i = 0; i < longValues.length; i++) {
            jscienceLargeIntegerValues[i] = org.jscience.mathematics.number.LargeInteger.valueOf(longValues[i]);
        }

        libjBigIntValues = new org.libj.math.BigInt[longValues.length];
        for (int i = 0; i < longValues.length; i++) {
            libjBigIntValues[i] = new org.libj.math.BigInt(longValues[i]);
        }

        huldraBigIntValues = new org.huldra.math.BigInt[longValues.length];
        for (int i = 0; i < longValues.length; i++) {
            huldraBigIntValues[i] = new org.huldra.math.BigInt(longValues[i]);
        }
    }
    

    @Benchmark
    public void benchmarkLong(Blackhole blackhole) {
        blackhole.consume(
                ((longValues[0] * longValues[1]) + longValues[2]) / longValues[3]);
    }

    @Benchmark
    public void benchmarkBigInteger(Blackhole blackhole) {
        blackhole.consume(
                bigIntegerValues[0]
                        .multiply(bigIntegerValues[1])
                        .add(bigIntegerValues[2])
                        .divide(bigIntegerValues[3]));
    }

    @Benchmark
    public void benchmarkLargeInteger(Blackhole blackhole) {
        blackhole.consume(
                largeIntegerValues[0]
                        .multiply(largeIntegerValues[1])
                        .add(largeIntegerValues[2])
                        .divide(largeIntegerValues[3]));
    }

    @Benchmark
    public void benchmarkScalaBigInt(Blackhole blackhole) {
        blackhole.consume(
                scalaBigIntValues[0]
                        .$times(scalaBigIntValues[1])
                        .$plus(scalaBigIntValues[2])
                        .$div(scalaBigIntValues[3]));
    }

    @Benchmark
    public void benchmarkSpireSafeLong(Blackhole blackhole) {
        blackhole.consume(
                spireSafeLongValues[0]
                        .$times(spireSafeLongValues[1])
                        .$plus(spireSafeLongValues[2])
                        .$div(spireSafeLongValues[3]));
    }

    @Benchmark
    public void benchmarkApfloatApint(Blackhole blackhole) {
        blackhole.consume(
                apfloatApintValues[0]
                        .multiply(apfloatApintValues[1])
                        .add(apfloatApintValues[2])
                        .divide(apfloatApintValues[3]));
    }

    @Benchmark
    public void benchmarkJscienceLargeInteger(Blackhole blackhole) {
        blackhole.consume(
                jscienceLargeIntegerValues[0]
                        .times(jscienceLargeIntegerValues[1])
                        .plus(jscienceLargeIntegerValues[2])
                        .divide(jscienceLargeIntegerValues[3]));
    }

    @Benchmark
    public void benchmarkLibjBigInt(Blackhole blackhole) {
        org.libj.math.BigInt result = libjBigIntValues[0].clone();
        result.mul(libjBigIntValues[1]);
        result.add(libjBigIntValues[2]);
        result.div(libjBigIntValues[3]);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkHuldraBigInt(Blackhole blackhole) {
        org.huldra.math.BigInt result = huldraBigIntValues[0].copy();
        result.mul(huldraBigIntValues[1]);
        result.add(huldraBigIntValues[2]);
        result.div(huldraBigIntValues[3]);
        blackhole.consume(result);
    }

}
