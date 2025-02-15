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
 * Compares performance of LargeInteger to other integral types in a complex case.
 * 
 * <p>Based on the following expression:</p>
 * 
 * <pre>
 * (
 *     (
 *         ((E * F) + (C - B) - (D * E))
 *             /
 *         E
 *     ) * -(E + 1) * (D | E)
 * ) + A
 * </pre>
 * 
 * Here, A is a really large number;
 * B and C are not too large numbers, close to each other;
 * D a number within the @code{long} range, but not too small,
 * E is a small number,
 * and, finally, F is equal to 2.
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeIntegerBenchmarkMixedNumbers {
    
    private static final BigInt SCALA_BIGINT_ONE = BigInt.apply(1);
    
    
    private Random random = new Random();
    

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
        bigIntegerValues = new BigInteger[] {
                new BigInteger("2734056287350452551043983294854383045924")
                        .add(BigInteger.valueOf(random.nextInt(10000000))),
                BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.valueOf(random.nextInt(1000) + 10000000L)),
                BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.valueOf(random.nextInt(1000) + 11000000L)),
                BigInteger.valueOf(((Long.MAX_VALUE / 3) * 2) + random.nextInt(10000) + 10000L),
                BigInteger.valueOf(random.nextInt(50) + 300L),
                BigInteger.valueOf(2),
        };
        
        largeIntegerValues = new LargeInteger[bigIntegerValues.length];
        for (int i = 0; i < bigIntegerValues.length; i++) {
            largeIntegerValues[i] = LargeInteger.of(bigIntegerValues[i]);
        }
        
        scalaBigIntValues = new BigInt[bigIntegerValues.length];
        for (int i = 0; i < bigIntegerValues.length; i++) {
            scalaBigIntValues[i] = BigInt.apply(bigIntegerValues[i]);
        }
        
        spireSafeLongValues = new SafeLong[scalaBigIntValues.length];
        for (int i = 0; i < scalaBigIntValues.length; i++) {
            spireSafeLongValues[i] = SafeLong.apply(scalaBigIntValues[i]);
        }
        
        apfloatApintValues = new Apint[bigIntegerValues.length];
        for (int i = 0; i < bigIntegerValues.length; i++) {
            apfloatApintValues[i] = new Apint(bigIntegerValues[i]);
        }
        
        jscienceLargeIntegerValues = new org.jscience.mathematics.number.LargeInteger[bigIntegerValues.length];
        for (int i = 0; i < bigIntegerValues.length; i++) {
            jscienceLargeIntegerValues[i] = org.jscience.mathematics.number.LargeInteger.valueOf(bigIntegerValues[i]);
        }

        libjBigIntValues = new org.libj.math.BigInt[bigIntegerValues.length];
        for (int i = 0; i < bigIntegerValues.length; i++) {
            libjBigIntValues[i] = new org.libj.math.BigInt(bigIntegerValues[i]);
        }

        huldraBigIntValues = new org.huldra.math.BigInt[bigIntegerValues.length];
        for (int i = 0; i < bigIntegerValues.length; i++) {
            huldraBigIntValues[i] = new org.huldra.math.BigInt(bigIntegerValues[i].toString());
        }
    }
    

    @Benchmark
    public void benchmarkBigInteger(Blackhole blackhole) {
        BigInteger a = bigIntegerValues[0];
        BigInteger b = bigIntegerValues[1];
        BigInteger c = bigIntegerValues[2];
        BigInteger d = bigIntegerValues[3];
        BigInteger e = bigIntegerValues[4];
        BigInteger f = bigIntegerValues[5];
        BigInteger result = e
                .multiply(f)
                .add(c.subtract(b))
                .subtract(d.multiply(e))
                .divide(e)
                .multiply(e.add(BigInteger.ONE).negate())
                .multiply(d.or(e))
                .add(a);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkLargeInteger(Blackhole blackhole) {
        LargeInteger a = largeIntegerValues[0];
        LargeInteger b = largeIntegerValues[1];
        LargeInteger c = largeIntegerValues[2];
        LargeInteger d = largeIntegerValues[3];
        LargeInteger e = largeIntegerValues[4];
        LargeInteger f = largeIntegerValues[5];
        LargeInteger result = e
                .multiply(f)
                .add(c.subtract(b))
                .subtract(d.multiply(e))
                .divide(e)
                .multiply(e.increment().negate())
                .multiply(d.or(e))
                .add(a);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkScalaBigInt(Blackhole blackhole) {
        BigInt a = scalaBigIntValues[0];
        BigInt b = scalaBigIntValues[1];
        BigInt c = scalaBigIntValues[2];
        BigInt d = scalaBigIntValues[3];
        BigInt e = scalaBigIntValues[4];
        BigInt f = scalaBigIntValues[5];
        BigInt result = e
                .$times(f)
                .$plus(c.$minus(b))
                .$minus(d.$times(e))
                .$div(e)
                .$times(e.$plus(SCALA_BIGINT_ONE).unary_$minus())
                .$times(d.$bar(e))
                .$plus(a);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkSpireSafeLong(Blackhole blackhole) {
        SafeLong a = spireSafeLongValues[0];
        SafeLong b = spireSafeLongValues[1];
        SafeLong c = spireSafeLongValues[2];
        SafeLong d = spireSafeLongValues[3];
        SafeLong e = spireSafeLongValues[4];
        SafeLong f = spireSafeLongValues[5];
        SafeLong result = e
                .$times(f)
                .$plus(c.$minus(b))
                .$minus(d.$times(e))
                .$div(e)
                .$times(e.$plus(SCALA_BIGINT_ONE).unary_$minus())
                .$times(d.$bar(e))
                .$plus(a);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkApfloatApint(Blackhole blackhole) {
        Apint a = apfloatApintValues[0];
        Apint b = apfloatApintValues[1];
        Apint c = apfloatApintValues[2];
        Apint d = apfloatApintValues[3];
        Apint e = apfloatApintValues[4];
        Apint f = apfloatApintValues[5];
        Apint orValue = new Apint(d.longValue() | e.longValue()); // FIXME
        Apint result = e
                .multiply(f)
                .add(c.subtract(b))
                .subtract(d.multiply(e))
                .divide(e)
                .multiply(e.add(Apint.ONE).negate())
                .multiply(orValue)
                .add(a);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkJscienceLargeInteger(Blackhole blackhole) {
        org.jscience.mathematics.number.LargeInteger a = jscienceLargeIntegerValues[0];
        org.jscience.mathematics.number.LargeInteger b = jscienceLargeIntegerValues[1];
        org.jscience.mathematics.number.LargeInteger c = jscienceLargeIntegerValues[2];
        org.jscience.mathematics.number.LargeInteger d = jscienceLargeIntegerValues[3];
        org.jscience.mathematics.number.LargeInteger e = jscienceLargeIntegerValues[4];
        org.jscience.mathematics.number.LargeInteger f = jscienceLargeIntegerValues[5];
        org.jscience.mathematics.number.LargeInteger orValue =
                org.jscience.mathematics.number.LargeInteger.valueOf(d.longValue() | e.longValue()); // FIXME
        org.jscience.mathematics.number.LargeInteger result = e
                .times(f)
                .plus(c.minus(b))
                .minus(d.times(e))
                .divide(e)
                .times(e.plus(1L).opposite())
                .times(orValue)
                .plus(a);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkLibjBigInt(Blackhole blackhole) {
        org.libj.math.BigInt a = libjBigIntValues[0];
        org.libj.math.BigInt b = libjBigIntValues[1];
        org.libj.math.BigInt c = libjBigIntValues[2];
        org.libj.math.BigInt d = libjBigIntValues[3];
        org.libj.math.BigInt e = libjBigIntValues[4];
        org.libj.math.BigInt f = libjBigIntValues[5];
        org.libj.math.BigInt result = e.clone();
        result.mul(f);
        org.libj.math.BigInt subResult = c.clone();
        subResult.sub(b);
        result.add(subResult);
        org.libj.math.BigInt mulResult = d.clone();
        mulResult.mul(e);
        result.sub(mulResult);
        result.div(e);
        org.libj.math.BigInt incrementResult = e.clone();
        incrementResult.add(1);
        org.libj.math.BigInt incrementNegateResult = new org.libj.math.BigInt(0);
        incrementNegateResult.sub(incrementResult);
        result.mul(incrementNegateResult);
        org.libj.math.BigInt orResult = d.clone();
        orResult.or(e);
        result.mul(orResult);
        result.add(a);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkHuldraBigInt(Blackhole blackhole) {
        org.huldra.math.BigInt a = huldraBigIntValues[0];
        org.huldra.math.BigInt b = huldraBigIntValues[1];
        org.huldra.math.BigInt c = huldraBigIntValues[2];
        org.huldra.math.BigInt d = huldraBigIntValues[3];
        org.huldra.math.BigInt e = huldraBigIntValues[4];
        org.huldra.math.BigInt f = huldraBigIntValues[5];
        org.huldra.math.BigInt result = e.copy();
        result.mul(f);
        org.huldra.math.BigInt subResult = c.copy();
        subResult.sub(b);
        result.add(subResult);
        org.huldra.math.BigInt mulResult = d.copy();
        mulResult.mul(e);
        result.sub(mulResult);
        result.div(e);
        org.huldra.math.BigInt incrementResult = e.copy();
        incrementResult.add(1);
        org.huldra.math.BigInt incrementNegateResult = new org.huldra.math.BigInt(0);
        incrementNegateResult.sub(incrementResult);
        result.mul(incrementNegateResult);
        org.huldra.math.BigInt orResult = d.copy();
        orResult.or(e);
        result.mul(orResult);
        result.add(a);
        blackhole.consume(result);
    }

}
