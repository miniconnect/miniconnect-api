package hu.webarticum.miniconnect.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public abstract class LargeInteger extends Number implements Comparable<LargeInteger> {
    
    private static final long serialVersionUID = 1L;
    
    
    private static final long CACHE_LOW = -128L;

    private static final long CACHE_HIGH = 127L;
    
    private static final LargeInteger.ImplSmall[] cache =
            new LargeInteger.ImplSmall[(int) (CACHE_HIGH - CACHE_LOW)];
    static {
        for (int i = 0; i < cache.length; i++) {
            cache[i] = new ImplSmall(CACHE_LOW + i);
        }
    }
    

    private static final int LONG_BIT_COUNT = Long.BYTES * 8;
    
    private static final int MAX_SAFE_LONG_STR_LENGTH = 18;
    
    private static final int MAX_SAFE_LONG_RADIX_STR_LENGTH = 12;
    
    private static final int MAX_SAFE_LONG_RADIX = 36;
    
    private static final long MAX_SMALL_MULTIPLIER = (long) Math.sqrt(Long.MAX_VALUE);
    
    private static final long MAX_SMALL_POW_BASE_ABS = 55108L;
    
    
    public static final LargeInteger NEGATIVE_ONE = of(-1L);
    
    public static final LargeInteger ZERO = of(0L);
    
    public static final LargeInteger ONE = of(1L);
    
    public static final LargeInteger TWO = of(2L);
    
    public static final LargeInteger THREE = of(3L);
    
    public static final LargeInteger FOUR = of(4L);
    
    public static final LargeInteger FIVE = of(5L);
    
    public static final LargeInteger SIX = of(6L);
    
    public static final LargeInteger SEVEN = of(7L);
    
    public static final LargeInteger EIGHT = of(8L);
    
    public static final LargeInteger NINE = of(9L);
    
    public static final LargeInteger TEN = of(10L);
    
    public static final LargeInteger ELEVEN = of(11L);
    
    public static final LargeInteger TWELVE = of(12L);
    
    public static final LargeInteger TWENTY = of(20L);
    
    public static final LargeInteger HUNDRED = of(100L);


    public static LargeInteger of(byte value) {
        return of((long) value);
    }

    public static LargeInteger of(short value) {
        return of((long) value);
    }

    public static LargeInteger of(int value) {
        return of((long) value);
    }

    public static LargeInteger of(long value) {
        if (value < CACHE_HIGH && value >= CACHE_LOW) {
            return cache[(int) (value - CACHE_LOW)];
        }
        
        return new ImplSmall(value);
    }

    public static LargeInteger of(double value) {
        if (value < Long.MAX_VALUE - 100 && value > Long.MIN_VALUE + 100) {
            return of((long) value);
        }
        
        return of(BigDecimal.valueOf(value).toBigInteger());
    }
    
    public static LargeInteger of(BigInteger value) {
        return value.bitLength() <= 63 ? of(value.longValue(), value) : new ImplBig(value);
    }

    private static LargeInteger of(long value, BigInteger bigIntegerValue) {
        if (value < CACHE_HIGH && value >= CACHE_LOW) {
            ImplSmall result = cache[(int) (value - CACHE_LOW)];
            result.bigIntegerValue = bigIntegerValue;
            return result;
        }
        
        return new ImplSmall(value, bigIntegerValue);
    }

    public static LargeInteger of(String value) {
        if (value.length() <= MAX_SAFE_LONG_STR_LENGTH) {
            return of(Long.parseLong(value));
        } else {
            return of(new BigInteger(value));
        }
    }

    public static LargeInteger of(String value, int radix) {
        if (value.length() <= MAX_SAFE_LONG_RADIX_STR_LENGTH && radix <= MAX_SAFE_LONG_RADIX) {
            return of(Long.parseLong(value, radix));
        } else {
            return of(new BigInteger(value, radix));
        }
    }

    public static LargeInteger of(byte[] bytes) {
        if (bytes.length == Long.BYTES) {
            return of(ByteBuffer.wrap(bytes).getLong());
        } else if (bytes.length == 0) {
            return ZERO;
        } else if (bytes.length < Long.BYTES) {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            if (bytes[0] < 0) {
                buffer.putLong(-1L);
            }
            buffer.position(Long.BYTES - bytes.length);
            buffer.put(bytes);
            buffer.position(0);
            long value = buffer.getLong();
            return of(value);
        } else {
            return of(new BigInteger(bytes));
        }
    }

    public static LargeInteger of(BitSet bitSet) {
        return of(toBytes(bitSet));
    }

    public static LargeInteger nonNegativeOf(byte[] bytes) {
        return of(new BigInteger(1, bytes));
    }

    public static LargeInteger nonNegativeOf(BitSet bitSet) {
        return nonNegativeOf(toBytes(bitSet));
    }

    private static byte[] toBytes(BitSet bitSet) {
        byte[] bytes = bitSet.toByteArray();
        int halfLength = bytes.length / 2;
        for (int i = 0; i < halfLength; i++) {
            byte v = bytes[i];
            int flipIndex = bytes.length - i - 1;
            bytes[i] = bytes[flipIndex];
            bytes[flipIndex] = v;
        }
        return bytes;
    }

    public static LargeInteger[] arrayOf(long... values) {
        LargeInteger[] result = new LargeInteger[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = LargeInteger.of(values[i]);
        }
        return result;
    }

    public static LargeInteger[] arrayOf(String... values) {
        LargeInteger[] result = new LargeInteger[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = LargeInteger.of(values[i]);
        }
        return result;
    }
    
    public static LargeInteger[] arrayOf(BigInteger... values) {
        LargeInteger[] result = new LargeInteger[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = LargeInteger.of(values[i]);
        }
        return result;
    }
    
    public static LargeInteger[] arrayOf(LargeInteger... values) {
        return Arrays.copyOf(values, values.length);
    }


    // BigInteger' methods
    
    public abstract long longValueExact();
    
    public abstract int intValueExact();
    
    public abstract short shortValueExact();
    
    public abstract byte byteValueExact();
    
    public abstract byte[] toByteArray();
    
    public abstract boolean isProbablePrime(int certainty);
    
    public abstract LargeInteger nextProbablePrime();
    
    public abstract LargeInteger min(LargeInteger val);
    
    public abstract LargeInteger max(LargeInteger val);
    
    public abstract LargeInteger add(LargeInteger val);
    
    public abstract LargeInteger subtract(LargeInteger val);
    
    public abstract LargeInteger multiply(LargeInteger val);
    
    public abstract LargeInteger multiply(double val);
    
    public abstract LargeInteger divide(LargeInteger val);
    
    public abstract LargeInteger[] divideAndRemainder(LargeInteger val);
    
    public abstract LargeInteger remainder(LargeInteger val);
    
    public abstract LargeInteger pow(int exponent);
    
    public abstract LargeInteger sqrt();
    
    public abstract LargeInteger gcd(LargeInteger val);
    
    public abstract LargeInteger abs();
    
    public abstract LargeInteger negate();
    
    public abstract int signum();
    
    public abstract LargeInteger mod(LargeInteger m);
    
    public abstract LargeInteger modPow(LargeInteger exponent, LargeInteger m);
    
    public abstract LargeInteger modInverse(LargeInteger m);
    
    public abstract LargeInteger shiftLeft(int n);
    
    public abstract LargeInteger shiftRight(int n);
    
    public abstract LargeInteger and(LargeInteger val);
    
    public abstract LargeInteger or(LargeInteger val);
    
    public abstract LargeInteger xor(LargeInteger val);
    
    public abstract LargeInteger not();
    
    public abstract LargeInteger andNot(LargeInteger val);
    
    public abstract boolean testBit(int n);
    
    public abstract LargeInteger setBit(int n);
    
    public abstract LargeInteger clearBit(int n);
    
    public abstract LargeInteger flipBit(int n);
    
    public abstract int getLowestSetBit();
    
    public abstract int bitLength();
    
    public abstract int bitCount();
    
    
    // additional methods
    
    public abstract LargeInteger cached();
    
    public BitSet toBitSet() {
        byte[] bytes = toByteArray();
        int halfLength = bytes.length / 2;
        for (int i = 0; i < halfLength; i++) {
            byte v = bytes[i];
            int flipIndex = bytes.length - i - 1;
            bytes[i] = bytes[flipIndex];
            bytes[flipIndex] = v;
        }
        return BitSet.valueOf(bytes);
    }

    public abstract String toString(int radix);
    
    public abstract BigInteger bigIntegerValue();

    public abstract BigDecimal bigDecimalValue();
    
    public abstract boolean isFittingInLong();
    
    public abstract boolean isFittingInInt();
    
    public abstract boolean isFittingInShort();
    
    public abstract boolean isFittingInByte();

    public boolean isZero() {
        return equals(ZERO);
    }

    public boolean isNonZero() {
        return !equals(ZERO);
    }

    public boolean isPositive() {
        return signum() > 0;
    }

    public boolean isNonPositive() {
        return signum() <= 0;
    }

    public boolean isNegative() {
        return signum() < 0;
    }

    public boolean isNonNegative() {
        return signum() >= 0;
    }

    public abstract boolean isEven();

    public boolean isOdd() {
        return !isEven();
    }

    public boolean isDivisibleBy(LargeInteger val) {
        return remainder(val).isZero();
    }

    public boolean isEqualTo(LargeInteger val) {
        return equals(val);
    }

    public boolean isLessThan(LargeInteger val) {
        return compareTo(val) < 0;
    }

    public boolean isLessThanOrEqualTo(LargeInteger val) {
        return compareTo(val) <= 0;
    }
    
    public boolean isGreaterThan(LargeInteger val) {
        return compareTo(val) > 0;
    }

    public boolean isGreaterThanOrEqualTo(LargeInteger val) {
        return compareTo(val) >= 0;
    }
    
    public abstract boolean isPowerOfTwo();
    
    public abstract LargeInteger increment();
    
    public abstract LargeInteger decrement();
    
    public abstract LargeInteger random(Random random);
    

    private static class ImplSmall extends LargeInteger {
        
        private static final long serialVersionUID = 1L;
        
        
        private final long value;

        private transient volatile BigInteger bigIntegerValue = null;
        

        private ImplSmall(long value) {
            this.value = value;
        }

        private ImplSmall(long value, BigInteger bigIntegerValue) {
            this.value = value;
            this.bigIntegerValue = bigIntegerValue;
        }

        
        @Override
        public LargeInteger cached() {
            if (value < CACHE_HIGH && value >= CACHE_LOW) {
                return cache[(int) (value - CACHE_LOW)];
            }
            
            return this;
        }
        
        @Override
        public String toString() {
            return Long.toString(value);
        }

        @Override
        public String toString(int radix) {
            return Long.toString(value, radix);
        }
        
        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ImplSmall)) {
                return false;
            }
            
            return value == ((ImplSmall) other).value;
        }
        
        @Override
        public int hashCode() {
            return Long.hashCode(value);
        }
        
        @Override
        public int compareTo(LargeInteger other) {
            if (other instanceof ImplBig) {
                return -((ImplBig) other).value.signum();
            }
            
            return Long.compare(value, other.longValueExact());
        }

        @Override
        public BigInteger bigIntegerValue() {
            if (bigIntegerValue == null) {
                bigIntegerValue = BigInteger.valueOf(value);
            }
            return bigIntegerValue;
        }

        @Override
        public BigDecimal bigDecimalValue() {
            return BigDecimal.valueOf(value);
        }
        
        @Override
        public long longValue() {
            return value;
        }

        @Override
        public int intValue() {
            return (int) value;
        }

        @Override
        public float floatValue() {
            return value;
        }

        @Override
        public double doubleValue() {
            return value;
        }

        @Override
        public long longValueExact() {
            return value;
        }

        @Override
        public int intValueExact() {
            if (value > Integer.MAX_VALUE || value < Integer.MIN_VALUE) {
                throw new ArithmeticException("LargeInteger out of int range");
            }
            
            return intValue();
        }

        @Override
        public short shortValueExact() {
            if (value > Short.MAX_VALUE || value < Short.MIN_VALUE) {
                throw new ArithmeticException("LargeInteger out of short range");
            }
            
            return shortValue();
        }

        @Override
        public byte byteValueExact() {
            if (value > Byte.MAX_VALUE || value < Byte.MIN_VALUE) {
                throw new ArithmeticException("LargeInteger out of byte range");
            }
            
            return byteValue();
        }

        @Override
        public byte[] toByteArray() {
            return bigIntegerValue().toByteArray();
        }

        @Override
        public boolean isProbablePrime(int certainty) {
            return bigIntegerValue().isProbablePrime(certainty);
        }

        @Override
        public LargeInteger nextProbablePrime() {
            return of(bigIntegerValue().nextProbablePrime());
        }

        @Override
        public LargeInteger min(LargeInteger val) {
            if (val instanceof ImplBig) {
                return val.signum() > 0 ? this : val;
            }
            
            return new ImplSmall(Math.min(value, ((ImplSmall) val).value));
        }

        @Override
        public LargeInteger max(LargeInteger val) {
            if (val instanceof ImplBig) {
                return val.signum() < 0 ? this : val;
            }
            
            return new ImplSmall(Math.max(value, ((ImplSmall) val).value));
        }

        @Override
        public LargeInteger add(LargeInteger val) {
            if (val instanceof ImplSmall) {
                long otherValue = ((ImplSmall) val).value;
                long candidate = value + otherValue;
                if (((value ^ candidate) & (otherValue ^ candidate)) >= 0) {
                    return new ImplSmall(candidate);
                }
            }
            
            return of(bigIntegerValue().add(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger subtract(LargeInteger val) {
            if (val instanceof ImplSmall) {
                long otherValue = ((ImplSmall) val).value;
                long candidate = value - otherValue;
                if (((value ^ otherValue) & (value ^ candidate)) >= 0) {
                    return new ImplSmall(candidate);
                }
            }
            
            return of(bigIntegerValue().subtract(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger multiply(LargeInteger val) {
            if (value == 0) {
                return ZERO;
            } else if (val instanceof ImplSmall) {
                long otherValue = ((ImplSmall) val).value;
                long candidate = value * otherValue;
                if (otherValue == candidate / value) {
                    return new ImplSmall(candidate);
                }
            }
            
            return of(bigIntegerValue().multiply(val.bigIntegerValue()));
        }
        
        @Override
        public LargeInteger multiply(double val) {
            if (
                    (val < 1 && val > -1 && value > Long.MIN_VALUE) ||
                    (val < MAX_SMALL_MULTIPLIER - 100 && value < MAX_SMALL_MULTIPLIER &&
                            val > -MAX_SMALL_MULTIPLIER + 100 && value > -MAX_SMALL_MULTIPLIER + 1)) {
                return new ImplSmall((long) (value * val));
            }
            return of(BigDecimal.valueOf(value).multiply(BigDecimal.valueOf(val)).toBigInteger());
        }

        @Override
        public LargeInteger divide(LargeInteger val) {
            if (val instanceof ImplBig) {
                return ZERO;
            }
            
            return new ImplSmall(value / ((ImplSmall) val).value);
        }

        @Override
        public LargeInteger[] divideAndRemainder(LargeInteger val) {
            return new LargeInteger[] { divide(val), remainder(val) };
        }

        @Override
        public LargeInteger remainder(LargeInteger val) {
            if (val instanceof ImplBig) {
                return this;
            }
            
            return new ImplSmall(value % ((ImplSmall) val).value);
        }

        @Override
        public LargeInteger pow(int exponent) {
            if (exponent == 0) {
                return ONE;
            } else if (exponent == 1) {
                return this;
            } else if (exponent < 0) {
                throw new ArithmeticException("Negative exponent");
            } else if (
                    Math.abs(value) > MAX_SMALL_POW_BASE_ABS ||
                    exponent > 4 ||
                    value == Long.MIN_VALUE) {
                return of(bigIntegerValue().pow(exponent));
            }
            
            long square = value * value;
            long longResult;
            if (exponent == 2) {
                longResult = square;
            } else if (exponent == 3) {
                longResult = square * value;
            } else {
                longResult = square * square;
            }
            return new ImplSmall(longResult);
        }

        @Override
        public LargeInteger sqrt() {
            if (value < 0) {
                throw new ArithmeticException("Square root of negative LargeInteger");
            } else if (value >= 121) {
                long candidate = (long) Math.sqrt(value);
                if (candidate < value / candidate) {
                    while (candidate + 1 < value / (candidate + 1)) {
                        candidate++;
                    }
                } else {
                    while (candidate > value / candidate) {
                        candidate--;
                    }
                }
                return of(candidate);
            } else if (value == 0) {
                return ZERO;
            } else if (value < 4) {
                return ONE;
            } else if (value < 100) {
                if (value < 36) {
                    if (value < 16) {
                        if (value < 9) {
                            return TWO;
                        } else {
                            return THREE;
                        }
                    } else if (value < 25) {
                        return FOUR;
                    } else {
                        return FIVE;
                    }
                } else if (value < 64) {
                    if (value < 49) {
                        return SIX;
                    } else {
                        return SEVEN;
                    }
                } else if (value < 81) {
                    return EIGHT;
                } else {
                    return NINE;
                }
            } else {
                return TEN;
            }
        }

        @Override
        public LargeInteger gcd(LargeInteger val) {
            if (val instanceof ImplSmall && value > Long.MIN_VALUE) {
                long otherValue = ((ImplSmall) val).value;
                if (otherValue > Long.MIN_VALUE) {
                    return new ImplSmall(binaryGcd(Math.abs(value), Math.abs(otherValue)));
                }
            }
            
            return of(bigIntegerValue().gcd(val.bigIntegerValue()));
        }
        
        public static long binaryGcd(long a, long b) {
            if (a == 0) {
                return b;
            } else if (b == 0) {
                return a;
            }
            
            int commonFactorsOfTwo = 0;
            while (((a | b) & 1) == 0) {
                a >>= 1;
                b >>= 1;
                commonFactorsOfTwo++;
            }
            while ((a & 1) == 0) {
                a >>= 1;
            }
            while (b != 0) {
                while ((b & 1) == 0) {
                    b >>= 1;
                }
                if (a > b) {
                    long tmp = a;
                    a = b;
                    b = tmp;
                }
                b -= a;
            }
            return a << commonFactorsOfTwo;
        }
        
        @Override
        public LargeInteger abs() {
            if (value >= 0L) {
                return this;
            } else if (value == Long.MIN_VALUE) {
                return new ImplBig(bigIntegerValue().negate());
            } else {
                return new ImplSmall(-value);
            }
        }

        @Override
        public LargeInteger negate() {
            if (value == Long.MIN_VALUE) {
                return new ImplBig(bigIntegerValue().negate());
            }
            
            return new ImplSmall(-value);
        }

        @Override
        public int signum() {
            return Long.signum(value);
        }

        @Override
        public LargeInteger mod(LargeInteger m) {
            ImplSmall remainder = (ImplSmall) remainder(m);
            if (remainder.value >= 0) {
                return remainder;
            } else {
                return m.abs().add(remainder);
            }
        }

        @Override
        public LargeInteger modPow(LargeInteger exponent, LargeInteger m) {
            return of(bigIntegerValue().modPow(exponent.bigIntegerValue(), m.bigIntegerValue()));
        }

        @Override
        public LargeInteger modInverse(LargeInteger m) {
            return of(bigIntegerValue().modInverse(m.bigIntegerValue()));
        }

        @Override
        public LargeInteger shiftLeft(int n) {
            if (n == 0) {
                return this;
            } else if (n < 0) {
                return new ImplSmall(value >> -n);
            } else if (n >= 32 || value > MAX_SMALL_MULTIPLIER) {
                return of(bigIntegerValue().shiftLeft(n));
            }
            
            return new ImplSmall(value << n);
        }

        @Override
        public LargeInteger shiftRight(int n) {
            if (n == 0) {
                return this;
            } else if (n < 0 && n > -32 && value > -MAX_SMALL_MULTIPLIER && value <= MAX_SMALL_MULTIPLIER) {
                return new ImplSmall(value << -n);
            } else if (n < 0) {
                return of(bigIntegerValue().shiftRight(n));
            }
            
            return new ImplSmall(value >> n);
        }

        @Override
        public LargeInteger and(LargeInteger val) {
            if (val instanceof ImplBig) {
                return of(bigIntegerValue().and(val.bigIntegerValue()));
            }

            return new ImplSmall(value & ((ImplSmall) val).value);
        }

        @Override
        public LargeInteger or(LargeInteger val) {
            if (val instanceof ImplBig) {
                return of(bigIntegerValue().or(val.bigIntegerValue()));
            }

            return new ImplSmall(value | ((ImplSmall) val).value);
        }

        @Override
        public LargeInteger xor(LargeInteger val) {
            if (val instanceof ImplBig) {
                return of(bigIntegerValue().xor(val.bigIntegerValue()));
            }

            return new ImplSmall(value ^ ((ImplSmall) val).value);
        }

        @Override
        public LargeInteger not() {
            return new ImplSmall(~value);
        }

        @Override
        public LargeInteger andNot(LargeInteger val) {
            if (val instanceof ImplBig) {
                return of(bigIntegerValue().andNot(val.bigIntegerValue()));
            }

            return new ImplSmall(value & ~(((ImplSmall) val).value));
        }

        @Override
        public boolean testBit(int n) {
            if (n >= LONG_BIT_COUNT - 1) {
                return value < 0;
            } else if (n < 0) {
                throw negativeBitAddress();
            }
            
            return (value & (1 << n)) != 0;
        }

        @Override
        public LargeInteger setBit(int n) {
            if (n >= LONG_BIT_COUNT - 1) {
                return of(bigIntegerValue().setBit(n));
            } else if (n < 0) {
                throw negativeBitAddress();
            }
            
            return new ImplSmall(value | (1L << n));
        }

        @Override
        public LargeInteger clearBit(int n) {
            if (value < 0 && n >= LONG_BIT_COUNT - 1) {
                return of(bigIntegerValue().flipBit(n));
            } else if (n < 0) {
                throw negativeBitAddress();
            }

            return new ImplSmall(value & ~(1L << n));
        }

        @Override
        public LargeInteger flipBit(int n) {
            if (n >= LONG_BIT_COUNT - 1) {
                return of(bigIntegerValue().flipBit(n));
            } else if (n < 0) {
                throw negativeBitAddress();
            }

            return new ImplSmall(value ^ (1L << n));
        }
        
        private ArithmeticException negativeBitAddress() {
            return new ArithmeticException("Negative bit address");
        }

        @Override
        public int getLowestSetBit() {
            if (value == 0L) {
                return -1;
            }
            
            return Long.numberOfTrailingZeros(value);
        }

        @Override
        public int bitLength() {
            return 64 - Long.numberOfLeadingZeros(value >= 0L ? value : ~value);
        }

        @Override
        public int bitCount() {
            return Long.bitCount(value >= 0L ? value : ~value);
        }

        @Override
        public boolean isFittingInLong() {
            return true;
        }

        @Override
        public boolean isFittingInInt() {
            return value <= Integer.MAX_VALUE && value >= Integer.MIN_VALUE; 
        }

        @Override
        public boolean isFittingInShort() {
            return value <= Short.MAX_VALUE && value >= Short.MIN_VALUE;
        }

        @Override
        public boolean isFittingInByte() {
            return value <= Byte.MAX_VALUE && value >= Byte.MIN_VALUE;
        }

        @Override
        public boolean isEven() {
            return (value & 1) == 0;
        }

        @Override
        public boolean isPowerOfTwo() {
            if (value <= 0L) {
                return false;
            }
            
            return (value & (value - 1L)) == 0;
        }

        @Override
        public LargeInteger increment() {
            if (value == Long.MAX_VALUE) {
                return new ImplBig(bigIntegerValue().add(BigInteger.ONE));
            }
            
            return new ImplSmall(value + 1);
        }

        @Override
        public LargeInteger decrement() {
            if (value == Long.MIN_VALUE) {
                return new ImplBig(bigIntegerValue().subtract(BigInteger.ONE));
            }
            
            return new ImplSmall(value - 1);
        }
        
        @Override
        public LargeInteger random(Random random) {
            if (value <= 0) {
                throw new ArithmeticException("Random bound must be positive");
            }
            if (random instanceof ThreadLocalRandom) {
                return new ImplSmall(((ThreadLocalRandom) random).nextLong(value));
            }
            long mask = value - 1;
            long candidate = random.nextLong();
            if ((value & mask) == 0L) {
                candidate &= mask;
            } else {
                long unsigned = candidate >>> 1;
                candidate = unsigned % value;
                while (unsigned + mask - candidate < 0L) {
                    unsigned = random.nextLong() >>> 1;
                    candidate = unsigned % value;
                }
            }
            return new ImplSmall(candidate);
        }

    }
    
    private static class ImplBig extends LargeInteger {
        
        private static final long serialVersionUID = 1L;
        
        
        private final BigInteger value;
        

        private ImplBig(BigInteger value) {
            this.value = value;
        }


        @Override
        public LargeInteger cached() {
            return this;
        }
        
        @Override
        public String toString() {
            return value.toString();
        }

        @Override
        public String toString(int radix) {
            return value.toString(radix);
        }
        
        @Override
        public boolean equals(Object other) {
            if (!(other instanceof ImplBig)) {
                return false;
            }
            
            return value.equals(((ImplBig) other).value);
        }
        
        @Override
        public int hashCode() {
            return value.hashCode();
        }
        
        @Override
        public int compareTo(LargeInteger other) {
            if (other instanceof ImplSmall) {
                return value.signum();
            }
            
            return value.compareTo(other.bigIntegerValue());
        }

        @Override
        public BigInteger bigIntegerValue() {
            return value;
        }

        @Override
        public BigDecimal bigDecimalValue() {
            return new BigDecimal(value);
        }
        
        @Override
        public long longValue() {
            return value.longValue();
        }

        @Override
        public int intValue() {
            return value.intValue();
        }

        @Override
        public float floatValue() {
            return value.floatValue();
        }

        @Override
        public double doubleValue() {
            return value.doubleValue();
        }

        @Override
        public long longValueExact() {
            return value.longValueExact();
        }

        @Override
        public int intValueExact() {
            return value.intValueExact();
        }

        @Override
        public short shortValueExact() {
            return value.shortValueExact();
        }

        @Override
        public byte byteValueExact() {
            return value.byteValueExact();
        }

        @Override
        public byte[] toByteArray() {
            return value.toByteArray();
        }

        @Override
        public boolean isProbablePrime(int certainty) {
            return value.isProbablePrime(certainty);
        }

        @Override
        public LargeInteger nextProbablePrime() {
            return of(value.nextProbablePrime());
        }

        @Override
        public LargeInteger min(LargeInteger val) {
            return of(value.min(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger max(LargeInteger val) {
            return of(value.max(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger add(LargeInteger val) {
            return of(value.add(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger subtract(LargeInteger val) {
            return of(value.subtract(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger multiply(LargeInteger val) {
            return of(value.multiply(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger multiply(double val) {
            return of(new BigDecimal(value).multiply(BigDecimal.valueOf(val)).toBigInteger());
        }
        
        @Override
        public LargeInteger divide(LargeInteger val) {
            return of(value.divide(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger[] divideAndRemainder(LargeInteger val) {
            BigInteger[] bigIntegerResults = value.divideAndRemainder(val.bigIntegerValue());
            return new LargeInteger[] { of(bigIntegerResults[0]), of(bigIntegerResults[1]) };
        }

        @Override
        public LargeInteger remainder(LargeInteger val) {
            return of(value.remainder(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger pow(int exponent) {
            return of(value.pow(exponent));
        }

        @Override
        public LargeInteger sqrt() {
            if (value.signum() < 0) {
                throw new ArithmeticException("Square root of negative LargeInteger");
            }

            int bitLength = value.bitLength();
            int shift = bitLength - 63;
            if (shift % 2 == 1) {
                shift++;
            }
            double shiftedDoubleValue = value.shiftRight(shift).doubleValue();
            BigInteger shiftedApproximation = BigInteger.valueOf((long) Math.ceil(Math.sqrt(shiftedDoubleValue)));
            BigInteger result = shiftedApproximation.shiftLeft(shift >> 1);
            while (true) {
                BigInteger refinedResult = value.divide(result).add(result).shiftRight(1);
                if (refinedResult.compareTo(result) >= 0) {
                    return of(result);
                }
                result = refinedResult;
            }
        }

        @Override
        public LargeInteger gcd(LargeInteger val) {
            return of(value.gcd(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger abs() {
            return of(value.abs());
        }

        @Override
        public LargeInteger negate() {
            return of(value.negate());
        }

        @Override
        public int signum() {
            return value.signum();
        }

        @Override
        public LargeInteger mod(LargeInteger m) {
            return of(value.mod(m.bigIntegerValue()));
        }

        @Override
        public LargeInteger modPow(LargeInteger exponent, LargeInteger m) {
            return of(value.modPow(exponent.bigIntegerValue(), m.bigIntegerValue()));
        }

        @Override
        public LargeInteger modInverse(LargeInteger m) {
            return of(value.modInverse(m.bigIntegerValue()));
        }

        @Override
        public LargeInteger shiftLeft(int n) {
            return of(value.shiftLeft(n));
        }

        @Override
        public LargeInteger shiftRight(int n) {
            return of(value.shiftRight(n));
        }

        @Override
        public LargeInteger and(LargeInteger val) {
            return of(value.and(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger or(LargeInteger val) {
            return of(value.or(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger xor(LargeInteger val) {
            return of(value.xor(val.bigIntegerValue()));
        }

        @Override
        public LargeInteger not() {
            return of(value.not());
        }

        @Override
        public LargeInteger andNot(LargeInteger val) {
            return of(value.andNot(val.bigIntegerValue()));
        }

        @Override
        public boolean testBit(int n) {
            return value.testBit(n);
        }

        @Override
        public LargeInteger setBit(int n) {
            return of(value.setBit(n));
        }

        @Override
        public LargeInteger clearBit(int n) {
            return of(value.clearBit(n));
        }

        @Override
        public LargeInteger flipBit(int n) {
            return of(value.flipBit(n));
        }

        @Override
        public int getLowestSetBit() {
            return value.getLowestSetBit();
        }

        @Override
        public int bitLength() {
            return value.bitLength();
        }

        @Override
        public int bitCount() {
            return value.bitCount();
        }

        @Override
        public boolean isFittingInLong() {
            return false;
        }

        @Override
        public boolean isFittingInInt() {
            return false;
        }

        @Override
        public boolean isFittingInShort() {
            return false;
        }

        @Override
        public boolean isFittingInByte() {
            return false;
        }

        @Override
        public boolean isEven() {
            return !value.testBit(0);
        }

        @Override
        public boolean isPowerOfTwo() {
            if (isNonPositive()) {
                return false;
            }
            
            return value.and(value.subtract(BigInteger.ONE)).equals(BigInteger.ZERO);
        }

        @Override
        public LargeInteger increment() {
            if (isPositive()) {
                return new ImplBig(value.add(BigInteger.ONE));
            } else {
                return of(value.add(BigInteger.ONE));
            }
        }

        @Override
        public LargeInteger decrement() {
            if (isNegative()) {
                return new ImplBig(value.subtract(BigInteger.ONE));
            } else {
                return of(value.subtract(BigInteger.ONE));
            }
        }
        
        @Override
        public LargeInteger random(Random random) {
            if (value.signum() <= 0) {
                throw new ArithmeticException("Random bound must be positive");
            }
            int bitLength = value.subtract(BigInteger.ONE).bitLength();
            for (int i = 0; i < 100; i++) {
                BigInteger candidate = new BigInteger(bitLength, random);
                if (candidate.compareTo(value) < 0) {
                    return new ImplBig(candidate);
                }
            }
            LargeInteger fallbackCandidate = multiply(random.nextDouble());
            if (fallbackCandidate.isLessThan(this)) {
                return fallbackCandidate;
            }
            return this.decrement();
        }

    }
    
}
