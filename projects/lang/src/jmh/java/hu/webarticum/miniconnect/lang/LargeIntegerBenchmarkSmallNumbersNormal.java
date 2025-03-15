package hu.webarticum.miniconnect.lang;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import scala.math.BigInt;
import spire.math.SafeLong;

import org.apfloat.ApfloatMath;
import org.apfloat.Apint;
import org.apfloat.ApintMath;
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
 * abs(
 *   (((A + B) * (A - B)) + 1)
 *     %
 *   ((C & D) | E)
 * )
 *   +
 * -max(B, D + min(A, E / A))
 * </pre>
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeIntegerBenchmarkSmallNumbersNormal {
    
    private static final BigInt SCALA_BIGINT_ONE = BigInt.apply(1);
    
    
    private Random random = new Random();
    

    private long[] longValues;

    private BigInteger[] bigIntegerValues;

    private LargeInteger[] largeIntegerValues;
    
    private BigInt[] scalaBigIntValues;
    
    private SafeLong[] spireSafeLongValues;
    
    private Apint[] apfloatApintValues;
    
    private org.jscience.mathematics.number.LargeInteger[] jscienceLargeIntegerValues;
    
    private clojure.lang.BigInt[] clojureBigIntValues;
    
    private org.libj.math.BigInt[] libjBigIntValues;
    
    private org.huldra.math.BigInt[] huldraBigIntValues;
    

    @Setup(Level.Iteration)
    public void setup() {
        longValues = new long[] {
                random.nextInt(30) + 100L,
                random.nextInt(20) + 10L,
                random.nextInt(5000) + 2000L,
                random.nextInt(70) + 50L,
                random.nextInt(50) + 90L,
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
        
        clojureBigIntValues = new clojure.lang.BigInt[longValues.length];
        for (int i = 0; i < longValues.length; i++) {
            clojureBigIntValues[i] = clojure.lang.BigInt.fromLong(longValues[i]);
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
        long a = longValues[0];
        long b = longValues[1];
        long c = longValues[2];
        long d = longValues[3];
        long e = longValues[4];
        long result =
                Math.abs(
                        (((a + b) * (a - b)) + 1L)
                            %
                        ((c & d) | e))
                    +
                -Math.max(b, d + Math.min(a, e / a));
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkBigInteger(Blackhole blackhole) {
        BigInteger a = bigIntegerValues[0];
        BigInteger b = bigIntegerValues[1];
        BigInteger c = bigIntegerValues[2];
        BigInteger d = bigIntegerValues[3];
        BigInteger e = bigIntegerValues[4];
        BigInteger result = a
                .add(b)
                .multiply(a.subtract(b))
                .add(BigInteger.ONE)
                .remainder(c.and(d).or(e)).abs()
                .add(b.max(d.add(a.min(e.divide(a)))).negate());
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkLargeInteger(Blackhole blackhole) {
        LargeInteger a = largeIntegerValues[0];
        LargeInteger b = largeIntegerValues[1];
        LargeInteger c = largeIntegerValues[2];
        LargeInteger d = largeIntegerValues[3];
        LargeInteger e = largeIntegerValues[4];
        LargeInteger result = a
                .add(b)
                .multiply(a.subtract(b))
                .increment()
                .remainder(c.and(d).or(e)).abs()
                .add(b.max(d.add(a.min(e.divide(a)))).negate());
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkScalaBigInt(Blackhole blackhole) {
        BigInt a = scalaBigIntValues[0];
        BigInt b = scalaBigIntValues[1];
        BigInt c = scalaBigIntValues[2];
        BigInt d = scalaBigIntValues[3];
        BigInt e = scalaBigIntValues[4];
        BigInt result = a
                .$plus(b)
                .$times(a.$minus(b))
                .$plus(SCALA_BIGINT_ONE)
                .$percent(c.$amp(d).$bar(e)).abs()
                .$plus(b.max(d.$plus(a.min(e.$div(a)))).unary_$minus());
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkSpireSafeLong(Blackhole blackhole) {
        SafeLong a = spireSafeLongValues[0];
        SafeLong b = spireSafeLongValues[1];
        SafeLong c = spireSafeLongValues[2];
        SafeLong d = spireSafeLongValues[3];
        SafeLong e = spireSafeLongValues[4];
        SafeLong result = a
                .$plus(b)
                .$times(a.$minus(b))
                .$plus(SCALA_BIGINT_ONE)
                .$percent(c.$amp(d).$bar(e)).abs()
                .$plus(b.max(d.$plus(a.min(e.$div(a)))).unary_$minus());
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkApfloatApint(Blackhole blackhole) {
        Apint a = apfloatApintValues[0];
        Apint b = apfloatApintValues[1];
        Apint c = apfloatApintValues[2];
        Apint d = apfloatApintValues[3];
        Apint e = apfloatApintValues[4];
        Apint andOrValue = new Apint((c.longValue() & d.longValue()) | e.longValue()); // FIXME
        Apint result = ApintMath.abs(
                a
                        .add(b)
                        .multiply(a.subtract(b))
                        .add(Apint.ONE)
                        .mod(andOrValue)
                ).floor()
                .add(ApfloatMath.max(b, d.add(ApfloatMath.min(a, e.divide(a)))).floor().negate());
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkJscienceLargeInteger(Blackhole blackhole) {
        org.jscience.mathematics.number.LargeInteger a = jscienceLargeIntegerValues[0];
        org.jscience.mathematics.number.LargeInteger b = jscienceLargeIntegerValues[1];
        org.jscience.mathematics.number.LargeInteger c = jscienceLargeIntegerValues[2];
        org.jscience.mathematics.number.LargeInteger d = jscienceLargeIntegerValues[3];
        org.jscience.mathematics.number.LargeInteger e = jscienceLargeIntegerValues[4];
        org.jscience.mathematics.number.LargeInteger andOrValue =
                org.jscience.mathematics.number.LargeInteger.valueOf(
                        (c.longValue() & d.longValue()) | e.longValue()); // FIXME
        org.jscience.mathematics.number.LargeInteger minRightValue = e.divide(a);
        org.jscience.mathematics.number.LargeInteger minValue = a.isLessThan(minRightValue) ? a : minRightValue;
        org.jscience.mathematics.number.LargeInteger maxRightValue = d.plus(minValue);
        org.jscience.mathematics.number.LargeInteger maxValue = b.isGreaterThan(maxRightValue) ? b : maxRightValue;
        org.jscience.mathematics.number.LargeInteger result = a
                .plus(b)
                .times(a.minus(b))
                .plus(org.jscience.mathematics.number.LargeInteger.ONE)
                .remainder(andOrValue).abs()
                .plus(maxValue.opposite());
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkClojureBigInt(Blackhole blackhole) {
        clojure.lang.BigInt a = clojureBigIntValues[0];
        clojure.lang.BigInt b = clojureBigIntValues[1];
        clojure.lang.BigInt c = clojureBigIntValues[2];
        clojure.lang.BigInt d = clojureBigIntValues[3];
        clojure.lang.BigInt e = clojureBigIntValues[4];
        Object result =
            ClojureArithmetic.add(
                ClojureArithmetic.remainder(
                    ClojureArithmetic.increment(
                        ClojureArithmetic.multiply(
                            a.add(b),
                            ClojureArithmetic.subtract(a, b)
                        )
                    ),
                    ClojureArithmetic.abs(
                        ClojureArithmetic.or(
                            ClojureArithmetic.and(c, d),
                            e
                        )
                    )
                ),
                ClojureArithmetic.negate(
                    ClojureArithmetic.max(
                        b,
                        ClojureArithmetic.add(
                            d,
                            ClojureArithmetic.min(
                                a,
                                ClojureArithmetic.divide(e, a)
                            )
                        )
                    )
                )
            )
        ;
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkLibjBigInt(Blackhole blackhole) {
        org.libj.math.BigInt a = libjBigIntValues[0];
        org.libj.math.BigInt b = libjBigIntValues[1];
        org.libj.math.BigInt c = libjBigIntValues[2];
        org.libj.math.BigInt d = libjBigIntValues[3];
        org.libj.math.BigInt e = libjBigIntValues[4];
        org.libj.math.BigInt ZERO = new org.libj.math.BigInt(0);
        org.libj.math.BigInt result = a.clone();
        result.add(b);
        org.libj.math.BigInt subResult = a.clone();
        subResult.sub(b);
        result.mul(subResult);
        result.add(1);
        org.libj.math.BigInt andOrResult = c.clone();
        andOrResult.and(d);
        andOrResult.or(e);
        result.rem(andOrResult);
        org.libj.math.BigInt divResult = e.clone();
        divResult.div(a);
        org.libj.math.BigInt minResult = a.min(divResult);
        org.libj.math.BigInt addResult = d.clone();
        addResult.add(minResult);
        org.libj.math.BigInt maxResult = b.max(addResult);
        org.libj.math.BigInt maxNegateResult = ZERO.clone();
        maxNegateResult.sub(maxResult);
        result.add(maxNegateResult);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkHuldraBigInt(Blackhole blackhole) {
        org.huldra.math.BigInt a = huldraBigIntValues[0];
        org.huldra.math.BigInt b = huldraBigIntValues[1];
        org.huldra.math.BigInt c = huldraBigIntValues[2];
        org.huldra.math.BigInt d = huldraBigIntValues[3];
        org.huldra.math.BigInt e = huldraBigIntValues[4];
        org.huldra.math.BigInt ZERO = new org.huldra.math.BigInt(0);
        org.huldra.math.BigInt result = a.copy();
        result.add(b);
        org.huldra.math.BigInt subResult = a.copy();
        subResult.sub(b);
        result.mul(subResult);
        result.add(1);
        org.huldra.math.BigInt andOrResult = c.copy();
        andOrResult.and(d);
        andOrResult.or(e);
        result.rem(andOrResult);
        if (result.compareTo(ZERO) < 0) {
            org.huldra.math.BigInt absSubValue = result;
            result = ZERO.copy();
            result.sub(absSubValue);
        }
        org.huldra.math.BigInt divResult = e.copy();
        divResult.div(a);
        org.huldra.math.BigInt minResult = a.compareTo(divResult) < 0 ? a : divResult;
        org.huldra.math.BigInt addResult = d.copy();
        addResult.add(minResult);
        org.huldra.math.BigInt maxResult = b.compareTo(addResult) < 0 ? b : addResult;
        org.huldra.math.BigInt maxNegateResult = ZERO.copy();
        maxNegateResult.sub(maxResult);
        result.add(maxNegateResult);
        blackhole.consume(result);
    }

}
