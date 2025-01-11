package hu.webarticum.miniconnect.lang;

import java.math.BigInteger;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import scala.math.BigInt;
import spire.math.SafeLong;

import org.apfloat.ApfloatMath;
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
 * Compares performance of LargeInteger to Long, BigInteger, BigInt in a complex case.
 * 
 * <p>Based on the following expression:</p>
 * 
 * <pre>
 * abs(
 *   ((((A + B) * (A - B)) + 1) ** 3)
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
public class LargeIntegerBenchmarkSmallNumbersPow {
    
    private static final BigInt SCALA_BIGINT_ONE = BigInt.apply(1);
    
    
    private Random random = new Random();
    

    private long[] longValues;

    private BigInteger[] bigIntegerValues;

    private LargeInteger[] largeIntegerValues;
    
    private BigInt[] scalaBigIntValues;
    
    private SafeLong[] spireSafeLongValues;
    
    private Apint[] apfloatApintValues;
    

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
                        (long) Math.pow((((a + b) * (a - b)) + 1L), 3)
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
                .pow(3)
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
                .pow(3)
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
                .pow(3)
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
                .pow(3)
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
        Apint result = ApfloatMath.abs(
                ApfloatMath.pow(
                                a
                                        .add(b)
                                        .multiply(a.subtract(b))
                                        .add(Apint.ONE),
                                3
                        ).floor()
                        .mod(new Apint((c.longValue() & d.longValue()) | e.longValue()))
                ).floor()
                .add(ApfloatMath.max(b, d.add(ApfloatMath.min(a, e.divide(a)))).floor().negate());
        blackhole.consume(result);
    }

}
