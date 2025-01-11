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
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

/**
 * Compares addition performance of LargeInteger to Long, BigInteger, BigInt.
 */
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class LargeIntegerBenchmarkAddition {

    private long[] smallLongValues;

    private BigInteger[] smallBigIntegerValues;
    
    private LargeInteger[] smallLargeIntegerValues;
    
    private BigInt[] smallScalaBigIntValues;
    
    private SafeLong[] smallSpireSafeLongValues;
    
    private Apint[] smallApfloatApintValues;
    

    private BigInteger[] largeBigIntegerValues;
    
    private LargeInteger[] largeLargeIntegerValues;
    
    private BigInt[] largeScalaBigIntValues;
    
    private SafeLong[] largeSpireSafeLongValues;
    
    private Apint[] largeApfloatApintValues;
    
    
    @Setup(Level.Iteration)
    public void setup() {
        smallLongValues = new long[] {
                52738495L, // a
                -1034L, // b
                93845796L, // c
                0L, // d
                Integer.MAX_VALUE - 10L, // e
                (Long.MAX_VALUE / 3) + 2, // f
                1L, // g
        };

        smallBigIntegerValues = new BigInteger[smallLongValues.length];
        for (int i = 0; i < smallLongValues.length; i++) {
            smallBigIntegerValues[i] = BigInteger.valueOf(smallLongValues[i]);
        }
        
        smallLargeIntegerValues = new LargeInteger[smallBigIntegerValues.length];
        for (int i = 0; i < smallBigIntegerValues.length; i++) {
            smallLargeIntegerValues[i] = LargeInteger.of(smallBigIntegerValues[i]);
        }
        
        smallScalaBigIntValues = new BigInt[smallBigIntegerValues.length];
        for (int i = 0; i < smallBigIntegerValues.length; i++) {
            smallScalaBigIntValues[i] = BigInt.apply(smallBigIntegerValues[i]);
        }
        
        smallSpireSafeLongValues = new SafeLong[smallLongValues.length];
        for (int i = 0; i < smallLongValues.length; i++) {
            smallSpireSafeLongValues[i] = SafeLong.apply(smallLongValues[i]);
        }
        
        smallApfloatApintValues = new Apint[smallLongValues.length];
        for (int i = 0; i < smallLongValues.length; i++) {
            smallApfloatApintValues[i] = new Apint(smallLongValues[i]);
        }

        largeBigIntegerValues = new BigInteger[] {
                new BigInteger("575810763971431237461446818"), // p
                new BigInteger("904673097437829064823759470712"), // q
                new BigInteger("36236094960273609236274158157350249"), // r
                new BigInteger("32064872579074625870942357"), // s
                new BigInteger("-70943864902836361890438649134692837024732"), // t
        };
        
        largeLargeIntegerValues = new LargeInteger[largeBigIntegerValues.length];
        for (int i = 0; i < largeBigIntegerValues.length; i++) {
            largeLargeIntegerValues[i] = LargeInteger.of(largeBigIntegerValues[i]);
        }
        
        largeScalaBigIntValues = new BigInt[largeBigIntegerValues.length];
        for (int i = 0; i < largeBigIntegerValues.length; i++) {
            largeScalaBigIntValues[i] = BigInt.apply(largeBigIntegerValues[i]);
        }
        
        largeSpireSafeLongValues = new SafeLong[largeScalaBigIntValues.length];
        for (int i = 0; i < largeScalaBigIntValues.length; i++) {
            largeSpireSafeLongValues[i] = SafeLong.apply(largeScalaBigIntValues[i]);
        }
        
        largeApfloatApintValues = new Apint[largeBigIntegerValues.length];
        for (int i = 0; i < largeBigIntegerValues.length; i++) {
            largeApfloatApintValues[i] = new Apint(largeBigIntegerValues[i]);
        }
    }
    

    @Benchmark
    public void benchmarkSmallAdditionLong(Blackhole blackhole) {
        long a = smallLongValues[0];
        long b = smallLongValues[1];
        long c = smallLongValues[2];
        long d = smallLongValues[3];
        long e = smallLongValues[4];
        long f = smallLongValues[5];
        long g = smallLongValues[6];
        long result = a + b + c + d + e + f + g;
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkSmallAdditionBigInteger(Blackhole blackhole) {
        BigInteger a = smallBigIntegerValues[0];
        BigInteger b = smallBigIntegerValues[1];
        BigInteger c = smallBigIntegerValues[2];
        BigInteger d = smallBigIntegerValues[3];
        BigInteger e = smallBigIntegerValues[4];
        BigInteger f = smallBigIntegerValues[5];
        BigInteger g = smallBigIntegerValues[6];
        BigInteger result = a.add(b).add(c).add(d).add(e).add(f).add(g);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkSmallAdditionLargeInteger(Blackhole blackhole) {
        LargeInteger a = smallLargeIntegerValues[0];
        LargeInteger b = smallLargeIntegerValues[1];
        LargeInteger c = smallLargeIntegerValues[2];
        LargeInteger d = smallLargeIntegerValues[3];
        LargeInteger e = smallLargeIntegerValues[4];
        LargeInteger f = smallLargeIntegerValues[5];
        LargeInteger g = smallLargeIntegerValues[6];
        LargeInteger result = a.add(b).add(c).add(d).add(e).add(f).add(g);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkSmallAdditionScalaBigInt(Blackhole blackhole) {
        BigInt a = smallScalaBigIntValues[0];
        BigInt b = smallScalaBigIntValues[1];
        BigInt c = smallScalaBigIntValues[2];
        BigInt d = smallScalaBigIntValues[3];
        BigInt e = smallScalaBigIntValues[4];
        BigInt f = smallScalaBigIntValues[5];
        BigInt g = smallScalaBigIntValues[6];
        BigInt result = a.$plus(b).$plus(c).$plus(d).$plus(e).$plus(f).$plus(g);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkSmallAdditionSpireSafeLong(Blackhole blackhole) {
        SafeLong a = smallSpireSafeLongValues[0];
        SafeLong b = smallSpireSafeLongValues[1];
        SafeLong c = smallSpireSafeLongValues[2];
        SafeLong d = smallSpireSafeLongValues[3];
        SafeLong e = smallSpireSafeLongValues[4];
        SafeLong f = smallSpireSafeLongValues[5];
        SafeLong g = smallSpireSafeLongValues[6];
        SafeLong result = a.$plus(b).$plus(c).$plus(d).$plus(e).$plus(f).$plus(g);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkSmallAdditionApfloatApint(Blackhole blackhole) {
        Apint a = smallApfloatApintValues[0];
        Apint b = smallApfloatApintValues[1];
        Apint c = smallApfloatApintValues[2];
        Apint d = smallApfloatApintValues[3];
        Apint e = smallApfloatApintValues[4];
        Apint f = smallApfloatApintValues[5];
        Apint g = smallApfloatApintValues[6];
        Apint result = a.add(b).add(c).add(d).add(e).add(f).add(g);
        blackhole.consume(result);
    }

    //////////////////////////////////////////////////////////////////////////
    
    @Benchmark
    public void benchmarkLargeAdditionBigInteger(Blackhole blackhole) {
        BigInteger p = largeBigIntegerValues[0];
        BigInteger q = largeBigIntegerValues[1];
        BigInteger r = largeBigIntegerValues[2];
        BigInteger s = largeBigIntegerValues[3];
        BigInteger t = largeBigIntegerValues[4];
        BigInteger result = p.add(q).add(r).add(s).add(t);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkLargeAdditionLargeInteger(Blackhole blackhole) {
        LargeInteger p = largeLargeIntegerValues[0];
        LargeInteger q = largeLargeIntegerValues[1];
        LargeInteger r = largeLargeIntegerValues[2];
        LargeInteger s = largeLargeIntegerValues[3];
        LargeInteger t = largeLargeIntegerValues[4];
        LargeInteger result = p.add(q).add(r).add(s).add(t);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkLargeAdditionScalaBigInt(Blackhole blackhole) {
        BigInt p = largeScalaBigIntValues[0];
        BigInt q = largeScalaBigIntValues[1];
        BigInt r = largeScalaBigIntValues[2];
        BigInt s = largeScalaBigIntValues[3];
        BigInt t = largeScalaBigIntValues[4];
        BigInt result = p.$plus(q).$plus(r).$plus(s).$plus(t);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkLargeAdditionSpireSafeLong(Blackhole blackhole) {
        SafeLong p = largeSpireSafeLongValues[0];
        SafeLong q = largeSpireSafeLongValues[1];
        SafeLong r = largeSpireSafeLongValues[2];
        SafeLong s = largeSpireSafeLongValues[3];
        SafeLong t = largeSpireSafeLongValues[4];
        SafeLong result = p.$plus(q).$plus(r).$plus(s).$plus(t);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarLargeAdditionApfloatApint(Blackhole blackhole) {
        Apint p = largeApfloatApintValues[0];
        Apint q = largeApfloatApintValues[1];
        Apint r = largeApfloatApintValues[2];
        Apint s = largeApfloatApintValues[3];
        Apint t = largeApfloatApintValues[4];
        Apint result = p.add(q).add(r).add(s).add(t);
        blackhole.consume(result);
    }

    //////////////////////////////////////////////////////////////////////////
    
    @Benchmark
    public void benchmarkMixedAdditionBigInteger(Blackhole blackhole) {
        BigInteger a = smallBigIntegerValues[0];
        BigInteger b = smallBigIntegerValues[1];
        BigInteger c = smallBigIntegerValues[2];
        BigInteger d = smallBigIntegerValues[3];
        BigInteger e = smallBigIntegerValues[4];
        BigInteger f = smallBigIntegerValues[5];
        BigInteger g = smallBigIntegerValues[6];
        BigInteger p = largeBigIntegerValues[0];
        BigInteger q = largeBigIntegerValues[1];
        BigInteger r = largeBigIntegerValues[2];
        BigInteger s = largeBigIntegerValues[3];
        BigInteger t = largeBigIntegerValues[4];
        BigInteger sub1 = a.add(p);
        BigInteger sub2 = b.add(q);
        BigInteger sub3 = g.add(f);
        BigInteger sub4 = f.add(f).add(f).add(f);
        BigInteger sub5 = d.add(t);
        BigInteger sub6 = c.add(r).add(e).add(s);
        BigInteger result = sub1.add(sub2).add(sub3).add(sub4).add(sub5).add(sub6);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkMixedAdditionLargeInteger(Blackhole blackhole) {
        LargeInteger a = smallLargeIntegerValues[0];
        LargeInteger b = smallLargeIntegerValues[1];
        LargeInteger c = smallLargeIntegerValues[2];
        LargeInteger d = smallLargeIntegerValues[3];
        LargeInteger e = smallLargeIntegerValues[4];
        LargeInteger f = smallLargeIntegerValues[5];
        LargeInteger g = smallLargeIntegerValues[6];
        LargeInteger p = largeLargeIntegerValues[0];
        LargeInteger q = largeLargeIntegerValues[1];
        LargeInteger r = largeLargeIntegerValues[2];
        LargeInteger s = largeLargeIntegerValues[3];
        LargeInteger t = largeLargeIntegerValues[4];
        LargeInteger sub1 = a.add(p);
        LargeInteger sub2 = b.add(q);
        LargeInteger sub3 = g.add(f);
        LargeInteger sub4 = f.add(f).add(f).add(f);
        LargeInteger sub5 = d.add(t);
        LargeInteger sub6 = c.add(r).add(e).add(s);
        LargeInteger result = sub1.add(sub2).add(sub3).add(sub4).add(sub5).add(sub6);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkMixedAdditionScalaBigInt(Blackhole blackhole) {
        BigInt a = smallScalaBigIntValues[0];
        BigInt b = smallScalaBigIntValues[1];
        BigInt c = smallScalaBigIntValues[2];
        BigInt d = smallScalaBigIntValues[3];
        BigInt e = smallScalaBigIntValues[4];
        BigInt f = smallScalaBigIntValues[5];
        BigInt g = smallScalaBigIntValues[6];
        BigInt p = largeScalaBigIntValues[0];
        BigInt q = largeScalaBigIntValues[1];
        BigInt r = largeScalaBigIntValues[2];
        BigInt s = largeScalaBigIntValues[3];
        BigInt t = largeScalaBigIntValues[4];
        BigInt sub1 = a.$plus(p);
        BigInt sub2 = b.$plus(q);
        BigInt sub3 = g.$plus(f);
        BigInt sub4 = f.$plus(f).$plus(f).$plus(f);
        BigInt sub5 = d.$plus(t);
        BigInt sub6 = c.$plus(r).$plus(e).$plus(s);
        BigInt result = sub1.$plus(sub2).$plus(sub3).$plus(sub4).$plus(sub5).$plus(sub6);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkMixedAdditionSpireSafeLong(Blackhole blackhole) {
        SafeLong a = smallSpireSafeLongValues[0];
        SafeLong b = smallSpireSafeLongValues[1];
        SafeLong c = smallSpireSafeLongValues[2];
        SafeLong d = smallSpireSafeLongValues[3];
        SafeLong e = smallSpireSafeLongValues[4];
        SafeLong f = smallSpireSafeLongValues[5];
        SafeLong g = smallSpireSafeLongValues[6];
        SafeLong p = largeSpireSafeLongValues[0];
        SafeLong q = largeSpireSafeLongValues[1];
        SafeLong r = largeSpireSafeLongValues[2];
        SafeLong s = largeSpireSafeLongValues[3];
        SafeLong t = largeSpireSafeLongValues[4];
        SafeLong sub1 = a.$plus(p);
        SafeLong sub2 = b.$plus(q);
        SafeLong sub3 = g.$plus(f);
        SafeLong sub4 = f.$plus(f).$plus(f).$plus(f);
        SafeLong sub5 = d.$plus(t);
        SafeLong sub6 = c.$plus(r).$plus(e).$plus(s);
        SafeLong result = sub1.$plus(sub2).$plus(sub3).$plus(sub4).$plus(sub5).$plus(sub6);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkMixedAdditionApfloatApint(Blackhole blackhole) {
        Apint a = smallApfloatApintValues[0];
        Apint b = smallApfloatApintValues[1];
        Apint c = smallApfloatApintValues[2];
        Apint d = smallApfloatApintValues[3];
        Apint e = smallApfloatApintValues[4];
        Apint f = smallApfloatApintValues[5];
        Apint g = smallApfloatApintValues[6];
        Apint p = largeApfloatApintValues[0];
        Apint q = largeApfloatApintValues[1];
        Apint r = largeApfloatApintValues[2];
        Apint s = largeApfloatApintValues[3];
        Apint t = largeApfloatApintValues[4];
        Apint sub1 = a.add(p);
        Apint sub2 = b.add(q);
        Apint sub3 = g.add(f);
        Apint sub4 = f.add(f).add(f).add(f);
        Apint sub5 = d.add(t);
        Apint sub6 = c.add(r).add(e).add(s);
        Apint result = sub1.add(sub2).add(sub3).add(sub4).add(sub5).add(sub6);
        blackhole.consume(result);
    }

    //////////////////////////////////////////////////////////////////////////
    
    @Benchmark
    public void benchmarkMixedBottleneckAdditionBigInteger(Blackhole blackhole) {
        BigInteger f = smallBigIntegerValues[5];
        BigInteger p = largeBigIntegerValues[0];
        BigInteger result = p.add(f);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkMixedBottleneckAdditionLargeInteger(Blackhole blackhole) {
        LargeInteger f = smallLargeIntegerValues[5];
        LargeInteger p = largeLargeIntegerValues[0];
        LargeInteger result = p.add(f);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkMixedBottleneckAdditionScalaBigInt(Blackhole blackhole) {
        BigInt f = smallScalaBigIntValues[5];
        BigInt p = largeScalaBigIntValues[0];
        BigInt result = p.$plus(f);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkMixedBottleneckAdditionSpireSafeLong(Blackhole blackhole) {
        SafeLong f = smallSpireSafeLongValues[5];
        SafeLong p = largeSpireSafeLongValues[0];
        SafeLong result = p.$plus(f);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkMixedBottleneckAdditionApfloatApint(Blackhole blackhole) {
        Apint f = smallApfloatApintValues[5];
        Apint p = largeApfloatApintValues[0];
        Apint result = p.add(f);
        blackhole.consume(result);
    }

}
