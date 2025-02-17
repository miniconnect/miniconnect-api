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
public class LargeIntegerBenchmarkAdd {

    @Param({
        "-763426087416928347290835",
        "-9223372036854775808",
        "-15",
        "0",
        "1",
        "2",
        "41234",
        "6748187346627180607",
        "9223372036854775807",
        "9223372036854775808",
        "47162348791820872334614854",
        "78307238172423789037823974832422",
    })
    private String aStr;

    @Param({
        "-4673847510640837346028374845234",
        "-15",
        "0",
        "1",
        "41234",
        "7306183862347640802",
        "9223372036854775807",
        "47162348791820872334614854",
    })
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
        blackhole.consume(longA + longB); // can overflow
    }

    @Benchmark
    public void benchmarkBigInteger(Blackhole blackhole) {
        blackhole.consume(bigIntegerA.add(bigIntegerB));
    }

    @Benchmark
    public void benchmarkLargeInteger(Blackhole blackhole) {
        blackhole.consume(largeIntegerA.add(largeIntegerB));
    }

    @Benchmark
    public void benchmarkScalaBigInt(Blackhole blackhole) {
        blackhole.consume(scalaBigIntA.$plus(scalaBigIntB));
    }

    @Benchmark
    public void benchmarkSpireSafeLong(Blackhole blackhole) {
        blackhole.consume(spireSafeLongA.$plus(spireSafeLongB));
    }

    @Benchmark
    public void benchmarkApfloatApint(Blackhole blackhole) {
        blackhole.consume(apfloatApintA.add(apfloatApintB));
    }

    @Benchmark
    public void benchmarkJscienceLargeInteger(Blackhole blackhole) {
        blackhole.consume(jscienceLargeIntegerA.plus(jscienceLargeIntegerB));
    }

    @Benchmark
    public void benchmarkClojureBigInt(Blackhole blackhole) {
        blackhole.consume(clojureBigIntA.add(clojureBigIntB));
    }

    @Benchmark
    public void benchmarkLibjBigInt(Blackhole blackhole) {
        org.libj.math.BigInt result = libjBigIntA.clone();
        result.add(libjBigIntB);
        blackhole.consume(result);
    }

    @Benchmark
    public void benchmarkHuldraBigInt(Blackhole blackhole) {
        org.huldra.math.BigInt result = huldraBigIntA.copy();
        result.add(huldraBigIntB);
        blackhole.consume(result);
    }

}
