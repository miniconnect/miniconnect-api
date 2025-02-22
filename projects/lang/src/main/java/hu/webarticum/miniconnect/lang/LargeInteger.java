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
    
    
    private static final ImplSmall[] cache = new ImplSmall[256];
    static {
        for (long i = -128L; i < 128L; i++) {
            cache[Byte.toUnsignedInt((byte) i)] = new ImplSmall(i);
        }
    }
    

    private static final int LONG_BIT_COUNT = Long.BYTES * 8;
    
    private static final int MAX_SAFE_LONG_STR_LENGTH = 18;
    
    private static final int MAX_SAFE_LONG_RADIX_STR_LENGTH = 12;
    
    private static final int MAX_SAFE_LONG_RADIX = 36;
    
    private static final long MAX_SMALL_MULTIPLIER = (long) Math.sqrt(Long.MAX_VALUE);
    
    private static final long MAX_SMALL_POW_BASE_ABS = 55108L;
    
    private static final long MAX_POWER_OF_TWO = 1L << (Long.SIZE - 2);
    
    
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


    public static LargeInteger of(boolean value) {
        return value ? ONE : ZERO;
    }

    public static LargeInteger of(char value) {
        byte byteValue = (byte) value;
        if (byteValue == value) {
            return cache[value];
        }
        
        return new ImplSmall(value);
    }
    
    public static LargeInteger of(byte value) {
        return cache[Byte.toUnsignedInt(value)];
    }

    public static LargeInteger of(short value) {
        byte byteValue = (byte) value;
        if (byteValue == value) {
            return cache[Byte.toUnsignedInt(byteValue)];
        }
        
        return new ImplSmall(value);
    }

    public static LargeInteger of(int value) {
        byte byteValue = (byte) value;
        if (byteValue == value) {
            return cache[Byte.toUnsignedInt(byteValue)];
        }
        
        return new ImplSmall(value);
    }

    public static LargeInteger of(long value) {
        byte byteValue = (byte) value;
        if (byteValue == value) {
            return cache[Byte.toUnsignedInt(byteValue)];
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
        byte byteValue = (byte) value;
        if (byteValue == value) {
            ImplSmall result = cache[Byte.toUnsignedInt(byteValue)];
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

    public static LargeInteger ofUnsignedByte(byte value) {
        return of(Byte.toUnsignedLong(value));
    }

    public static LargeInteger ofUnsignedShort(short value) {
        return of(Short.toUnsignedLong(value));
    }

    public static LargeInteger ofUnsignedInt(int value) {
        return of(Integer.toUnsignedLong(value));
    }

    public static LargeInteger ofUnsignedLong(long value) {
        if (value >= 0) {
            return of(value);
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.putLong(value);
            return of(new BigInteger(1, buffer.array()));
        }
    }

    public static LargeInteger ofUnsigned(byte[] bytes) {
        if (bytes.length > Long.BYTES ) {
            return of(new BigInteger(1, bytes));
        } else if (bytes.length == 0) {
            return ZERO;
        } else if (bytes.length < Long.BYTES) {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            buffer.position(Long.BYTES - bytes.length);
            buffer.put(bytes);
            buffer.position(0);
            long value = buffer.getLong();
            return of(value);
        } else if (bytes[0] < 0) {
            return of(new BigInteger(1, bytes));
        } else {
            return of(ByteBuffer.wrap(bytes).getLong());
        }
    }

    public static LargeInteger ofUnsigned(BitSet bitSet) {
        return ofUnsigned(toBytes(bitSet));
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


    // BigInteger's methods
    
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

    public abstract char charValue();
    
    public abstract char charValueExact();

    public abstract boolean booleanValue();
    
    public abstract boolean booleanValueExact();
    
    public abstract BitSet toBitSet();
    
    public abstract String toString(int radix);

    public abstract BigInteger bigIntegerValue();

    public abstract BigDecimal bigDecimalValue();

    public abstract boolean isFittingInLong();
    
    public abstract boolean isFittingInInt();
    
    public abstract boolean isFittingInShort();
    
    public abstract boolean isFittingInByte();

    public abstract boolean isFittingInChar();
    
    public abstract boolean isFittingInBoolean();
    
    public abstract boolean isZero();

    public abstract boolean isNonZero();

    public abstract boolean isPositive();

    public abstract boolean isNonPositive();

    public abstract boolean isNegative();

    public abstract boolean isNonNegative();

    public abstract boolean isEven();

    public abstract boolean isOdd();
    
    public abstract boolean isDivisibleBy(LargeInteger val);

    public abstract boolean isDivisorOf(LargeInteger val);

    public abstract boolean isRelativelyPrimeTo(LargeInteger val);

    public abstract boolean isEqualTo(LargeInteger val);

    public abstract boolean isLessThan(LargeInteger val);

    public abstract boolean isLessThanOrEqualTo(LargeInteger val);
    
    public abstract boolean isGreaterThan(LargeInteger val);

    public abstract boolean isGreaterThanOrEqualTo(LargeInteger val);
    
    public abstract boolean isPowerOfTwo();
    
    public abstract LargeInteger increment();
    
    public abstract LargeInteger decrement();

    public abstract LargeInteger twice();

    public abstract LargeInteger half();

    public abstract LargeInteger log2();
    
    public abstract LargeInteger floorPowerOfTwo();
    
    public abstract LargeInteger ceilingPowerOfTwo();

    public abstract LargeInteger add(long val);

    public abstract LargeInteger subtract(long val);

    public abstract LargeInteger multiply(long val);

    public abstract LargeInteger divide(long val);

    public abstract LargeInteger remainder(long val);

    public abstract LargeInteger mod(long m);
    
    public abstract boolean isDivisibleBy(long val);

    public abstract boolean isEqualTo(long val);
    
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
            byte byteValue = (byte) value;
            if (byteValue == value) {
                return cache[Byte.toUnsignedInt(byteValue)];
            }
            
            return this;
        }

        @Override
        public BitSet toBitSet() {
            int skipByteCount;
            if (value < 0) {
                skipByteCount = (Long.numberOfLeadingZeros(~value) - 1) / 8;
            } else {
                skipByteCount = (Long.numberOfLeadingZeros(value) - 1) / 8;
            }
            int bytesLength = 8 - skipByteCount;
            byte[] bytes = new byte[bytesLength];
            int bitOffset = 0;
            for (int i = 0; i < bytesLength; i++) {
                bytes[i] = (byte) (value >>> bitOffset);
                bitOffset += 8;
            }
            return BitSet.valueOf(bytes);
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
        public char charValue() {
            return (char) value;
        }

        @Override
        public boolean booleanValue() {
            return value != 0L;
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
            int intValue = (int) value;
            if (intValue != value) {
                throw new ArithmeticException("LargeInteger out of int range");
            }
            
            return intValue;
        }

        @Override
        public short shortValueExact() {
            short shortValue = (short) value;
            if (shortValue != value) {
                throw new ArithmeticException("LargeInteger out of short range");
            }
            
            return shortValue;
        }

        @Override
        public byte byteValueExact() {
            byte byteValue = (byte) value;
            if (byteValue != value) {
                throw new ArithmeticException("LargeInteger out of byte range");
            }
            
            return byteValue;
        }

        @Override
        public char charValueExact() {
            char charValue = (char) value;
            if (charValue != value) {
                throw new ArithmeticException("LargeInteger out of char range");
            }
            
            return charValue;
        }

        @Override
        public boolean booleanValueExact() {
            if ((value | 1L) != 1L) {
                throw new ArithmeticException("LargeInteger out of boolean range");
            }
            
            return value != 0L;
        }

        @Override
        public byte[] toByteArray() {
            int skipByteCount;
            if (value < 0) {
                skipByteCount = (Long.numberOfLeadingZeros(~value) - 1) / 8;
            } else {
                skipByteCount = (Long.numberOfLeadingZeros(value) - 1) / 8;
            }
            int resultLength = 8 - skipByteCount;
            byte[] result = new byte[resultLength];
            int bitOffset = (resultLength - 1) * 8;
            for (int i = 0; i < resultLength; i++) {
                result[i] = (byte) (value >>> bitOffset);
                bitOffset -= 8;
            }
            return result;
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
                if (value == Long.MIN_VALUE &&
                        ((ImplBig) val).value.negate().equals(BigInteger.valueOf(Long.MIN_VALUE))) {
                    return NEGATIVE_ONE;
                } else {
                    return ZERO;
                }
            }

            long otherValue = ((ImplSmall) val).value;
            if (value == Long.MIN_VALUE && otherValue == -1L) {
                return new ImplBig(BigInteger.valueOf(Long.MIN_VALUE).negate());
            }
            
            return new ImplSmall(value / otherValue);
        }

        @Override
        public LargeInteger[] divideAndRemainder(LargeInteger val) {
            if (val instanceof ImplBig) {
                if (value == Long.MIN_VALUE &&
                        ((ImplBig) val).value.equals(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE))) {
                    return new LargeInteger[] { NEGATIVE_ONE, ZERO };
                } else {
                    return new LargeInteger[] { ZERO, this };
                }
            }
            
            long otherValue = ((ImplSmall) val).value;
            LargeInteger divResult;
            if (value == Long.MIN_VALUE && otherValue == -1L) {
                divResult = new ImplBig(BigInteger.valueOf(Long.MIN_VALUE).negate());
            } else {
                divResult = new ImplSmall(value / otherValue);
            }
            
            return new LargeInteger[] { divResult, new ImplSmall(value % otherValue) };
        }

        @Override
        public LargeInteger remainder(LargeInteger val) {
            if (val instanceof ImplBig) {
                if (value == Long.MIN_VALUE &&
                        ((ImplBig) val).value.equals(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE))) {
                    return ZERO;
                } else {
                    return this;
                }
            }
            
            return new ImplSmall(value % ((ImplSmall) val).value);
        }

        @Override
        public LargeInteger pow(int exponent) {
            if (exponent > 1) {
                // most probable case, nothing to do
            } else if (exponent == 1) {
                return this;
            } else if (exponent == 0) {
                return ONE;
            } else {
                throw new ArithmeticException("Negative exponent");
            }
            
            if (value > 1) {
                // most probable case, nothing to do
            } else if (value == 0 || value == 1) {
                return this;
            } else if (value == -1) {
                return ((exponent & 1) == 0) ? this.negate() : this;
            }
            
            if (
                    Math.abs(value) > MAX_SMALL_POW_BASE_ABS ||
                    exponent > 4 ||
                    value == Long.MIN_VALUE) {
                int maxExponent = 63 / (64 - Long.numberOfLeadingZeros(Math.abs(value)));
                if (exponent > maxExponent) {
                    if (maxExponent >= 3) {
                        BigInteger bigIntegerResult = BigInteger.valueOf(value * value * value);
                        bigIntegerResult = bigIntegerResult.pow(exponent / 3);
                        int expRemainder = exponent % 3;
                        if (expRemainder == 1) {
                            bigIntegerResult = bigIntegerResult.multiply(bigIntegerValue());
                        } else if (expRemainder == 2) {
                            bigIntegerResult = bigIntegerResult.multiply(BigInteger.valueOf(value * value));
                        }
                        return of(bigIntegerResult);
                    } else if (maxExponent == 2) {
                        BigInteger bigIntegerResult = BigInteger.valueOf(value * value);
                        bigIntegerResult = bigIntegerResult.pow(exponent >> 1);
                        if ((exponent & 1) == 1) {
                            bigIntegerResult = bigIntegerResult.multiply(bigIntegerValue());
                        }
                        return of(bigIntegerResult);
                    } else {
                        return of(bigIntegerValue().pow(exponent));
                    }
                }
            }
            
            long longResult;
            switch (exponent) {
                case 2:
                    longResult = value * value;
                    break;
                case 3:
                    longResult = (value * value) * value;
                    break;
                case 4:
                    long square = value * value;
                    longResult = square * square;
                    break;
                default:
                    int e = exponent;
                    long base = value;
                    longResult = 1L;
                    while (true) {
                        if ((e & 1) == 1) {
                            longResult = longResult * base;
                        }
                        e >>= 1;
                        if (e == 0) {
                            break;
                        }
                        base = base * base;
                    }
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
            if (value == 0L) {
                return val.abs();
            } else if (value == 1L) {
                return ONE;
            } else if (val instanceof ImplBig || value == Long.MIN_VALUE || ((ImplSmall) val).value == Long.MIN_VALUE) {
                return of(bigIntegerValue().gcd(val.bigIntegerValue()));
            }

            long otherValue = ((ImplSmall) val).value;
            if (otherValue == 0L) {
                return abs();
            } else if (otherValue == 1L) {
                return ONE;
            }

            int xShift = Long.numberOfTrailingZeros(value);
            int yShift = Long.numberOfTrailingZeros(otherValue);

            long a = Math.abs(value >> xShift);
            long b = Math.abs(otherValue >> yShift);
            while (a != b) {
                if (a > b) {
                    a -= b;
                    a >>= Long.numberOfTrailingZeros(a);
                } else {
                    b -= a;
                    b >>= Long.numberOfTrailingZeros(b);
                }
            }

            int minShift = xShift < yShift ? xShift : yShift;
            return new ImplSmall(a << minShift);
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
            if (m.isNonPositive()) {
                throw new ArithmeticException("Modulus not positive");
            }
            ImplSmall remainder = (ImplSmall) remainder(m);
            if (remainder.value >= 0) {
                return remainder;
            } else {
                return m.add(remainder);
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
            
            return (value & (1L << n)) != 0;
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
            return (int) value == value;
        }

        @Override
        public boolean isFittingInShort() {
            return (short) value == value;
        }

        @Override
        public boolean isFittingInByte() {
            return (byte) value == value;
        }

        @Override
        public boolean isFittingInChar() {
            return (char) value == value;
        }

        @Override
        public boolean isFittingInBoolean() {
            return (value | 1L) == 1L;
        }
        
        @Override
        public boolean isZero() {
            return value == 0;
        }

        @Override
        public boolean isNonZero() {
            return value != 0;
        }

        @Override
        public boolean isPositive() {
            return value > 0;
        }

        @Override
        public boolean isNonPositive() {
            return value <= 0;
        }

        @Override
        public boolean isNegative() {
            return value < 0;
        }

        @Override
        public boolean isNonNegative() {
            return value >= 0;
        }

        @Override
        public boolean isEven() {
            return (value & 1L) == 0L;
        }

        @Override
        public boolean isOdd() {
            return (value & 1L) == 1L;
        }

        @Override
        public boolean isDivisibleBy(LargeInteger val) {
            if (val instanceof ImplBig) {
                if (value == 0) {
                    return true;
                } else if (value == Long.MIN_VALUE) {
                    return ((ImplBig) val).value.equals(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
                }
                return false;
            }
            
            return (value % ((ImplSmall) val).value) == 0;
        }

        @Override
        public boolean isDivisorOf(LargeInteger val) {
            if (val instanceof ImplBig) {
                return ((ImplBig) val).value.remainder(bigIntegerValue()).signum() == 0;
            }
            
            return (((ImplSmall) val).value % value) == 0;
        }

        @Override
        public boolean isRelativelyPrimeTo(LargeInteger val) {
            if (
                    ((value & 1) == 0 && val.isEven()) ||
                    (value % 3 == 0 && val.isDivisibleBy(LargeInteger.THREE)) ||
                    (value == 0 || val.isZero())) {
                return false;
            }
            
            return gcd(val).isEqualTo(ONE);
        }

        @Override
        public boolean isEqualTo(LargeInteger val) {
            return equals(val);
        }

        @Override
        public boolean isLessThan(LargeInteger val) {
            if (val instanceof ImplBig) {
                return ((ImplBig) val).isPositive();
            }
            
            return value < ((ImplSmall) val).value;
        }

        @Override
        public boolean isLessThanOrEqualTo(LargeInteger val) {
            if (val instanceof ImplBig) {
                return ((ImplBig) val).isPositive();
            }
            
            return value <= ((ImplSmall) val).value;
        }

        @Override
        public boolean isGreaterThan(LargeInteger val) {
            if (val instanceof ImplBig) {
                return ((ImplBig) val).isNegative();
            }
            
            return value > ((ImplSmall) val).value;
        }

        @Override
        public boolean isGreaterThanOrEqualTo(LargeInteger val) {
            if (val instanceof ImplBig) {
                return ((ImplBig) val).isNegative();
            }
            
            return value >= ((ImplSmall) val).value;
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
        public LargeInteger twice() {
            if (value == 0L) {
                return ZERO;
            }
            
            long candidate = value * 2L;
            if (candidate / value == 2L) {
                return new ImplSmall(candidate);
            }
            
            return new ImplBig(bigIntegerValue.multiply(BigInteger.valueOf(2L)));
        }

        @Override
        public LargeInteger half() {
            return of(value / 2);
        }

        @Override
        public LargeInteger log2() {
            if (value < 1L) {
                throw new ArithmeticException("Value is not positive");
            }
            
            return of((Long.SIZE - 1) - Long.numberOfLeadingZeros(value));
        }

        @Override
        public LargeInteger floorPowerOfTwo() {
            if (value < 1L) {
                throw new ArithmeticException("Value is not positive");
            }

            return of(1L << ((Long.SIZE - 1) - Long.numberOfLeadingZeros(value)));
        }

        @Override
        public LargeInteger ceilingPowerOfTwo() {
            if (value > MAX_POWER_OF_TWO) {
                int bitPosition = Long.SIZE - Long.numberOfLeadingZeros(value - 1);
                return new ImplBig(BigInteger.ZERO.setBit(bitPosition));
            } else if (value < 1L) {
                throw new ArithmeticException("Value is not positive");
            }
            
            return of(1L << -Long.numberOfLeadingZeros(value - 1));
        }

        @Override
        public LargeInteger add(long val) {
            long candidate = value + val;
            if (((value ^ candidate) & (val ^ candidate)) >= 0) {
                return new ImplSmall(candidate);
            }
            
            return of(bigIntegerValue().add(BigInteger.valueOf(val)));
        }

        @Override
        public LargeInteger subtract(long val) {
            long candidate = value - val;
            if (((value ^ val) & (value ^ candidate)) >= 0) {
                return new ImplSmall(candidate);
            }
            
            return of(bigIntegerValue().subtract(BigInteger.valueOf(val)));
        }

        @Override
        public LargeInteger multiply(long val) {
            if (value == 0) {
                return ZERO;
            }
            
            long candidate = value * val;
            if (val == candidate / value) {
                return new ImplSmall(candidate);
            }
            
            return of(bigIntegerValue().multiply(BigInteger.valueOf(val)));
        }

        @Override
        public LargeInteger divide(long val) {
            return new ImplSmall(value / val);
        }

        @Override
        public LargeInteger remainder(long val) {
            return new ImplSmall(value % val);
        }

        @Override
        public LargeInteger mod(long m) {
            if (m <= 0) {
                throw new ArithmeticException("Modulus not positive");
            }
            
            long remainder = value % m;
            if (remainder >= 0) {
                return new ImplSmall(remainder);
            } else {
                return new ImplSmall(m + remainder);
            }
        }

        @Override
        public boolean isDivisibleBy(long val) {
            return (value % val) == 0;
        }
        
        @Override
        public boolean isEqualTo(long val) {
            return value == val;
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
        public BitSet toBitSet() {
            byte[] bytes = value.toByteArray();
            int halfLength = bytes.length / 2;
            for (int i = 0; i < halfLength; i++) {
                byte v = bytes[i];
                int flipIndex = bytes.length - i - 1;
                bytes[i] = bytes[flipIndex];
                bytes[flipIndex] = v;
            }
            return BitSet.valueOf(bytes);
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
        public char charValue() {
            return (char) value.shortValue();
        }

        @Override
        public boolean booleanValue() {
            return true;
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
            throw new ArithmeticException("LargeInteger out of long range");
        }

        @Override
        public int intValueExact() {
            throw new ArithmeticException("LargeInteger out of int range");
        }

        @Override
        public short shortValueExact() {
            throw new ArithmeticException("LargeInteger out of short range");
        }

        @Override
        public byte byteValueExact() {
            throw new ArithmeticException("LargeInteger out of byte range");
        }

        @Override
        public char charValueExact() {
            throw new ArithmeticException("LargeInteger out of char range");
        }

        @Override
        public boolean booleanValueExact() {
            throw new ArithmeticException("LargeInteger out of boolean range");
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
            switch (exponent) {
                case 0:
                    return ONE;
                case 1:
                    return this;
                case 2:
                    return new ImplBig(value.multiply(value));
                case 4:
                    BigInteger square = value.multiply(value);
                    return new ImplBig(square.multiply(square));
                default:
                    return new ImplBig(value.pow(exponent));
            }
        }

        @Override
        public LargeInteger sqrt() {
            if (isNegative()) {
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
            return new ImplBig(value.setBit(n));
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
        public boolean isFittingInChar() {
            return false;
        }

        @Override
        public boolean isFittingInBoolean() {
            return false;
        }
        
        @Override
        public boolean isZero() {
            return false;
        }

        @Override
        public boolean isNonZero() {
            return true;
        }

        @Override
        public boolean isPositive() {
            return signum() > 0;
        }

        @Override
        public boolean isNonPositive() {
            return signum() <= 0;
        }

        @Override
        public boolean isNegative() {
            return signum() < 0;
        }

        @Override
        public boolean isNonNegative() {
            return signum() >= 0;
        }

        @Override
        public boolean isEven() {
            return !value.testBit(0);
        }

        @Override
        public boolean isOdd() {
            return value.testBit(0);
        }

        @Override
        public boolean isDivisibleBy(LargeInteger val) {
            return remainder(val).isZero();
        }

        @Override
        public boolean isDivisorOf(LargeInteger val) {
            return val.bigIntegerValue().remainder(value).signum() == 0;
        }

        @Override
        public boolean isRelativelyPrimeTo(LargeInteger val) {
            if (
                    (isEven() && val.isEven()) ||
                    (isDivisibleBy(THREE) && val.isDivisibleBy(THREE)) ||
                    val.isZero()) {
                return false;
            }

            return gcd(val).isEqualTo(ONE);
        }

        @Override
        public boolean isEqualTo(LargeInteger val) {
            return equals(val);
        }

        @Override
        public boolean isLessThan(LargeInteger val) {
            return compareTo(val) < 0;
        }

        @Override
        public boolean isLessThanOrEqualTo(LargeInteger val) {
            return compareTo(val) <= 0;
        }

        @Override
        public boolean isGreaterThan(LargeInteger val) {
            return compareTo(val) > 0;
        }

        @Override
        public boolean isGreaterThanOrEqualTo(LargeInteger val) {
            return compareTo(val) >= 0;
        }
        
        @Override
        public boolean isPowerOfTwo() {
            if (isNonPositive()) {
                return false;
            }
            
            return value.getLowestSetBit() == value.bitLength() - 1;
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
        public LargeInteger twice() {
            return new ImplBig(value.multiply(BigInteger.valueOf(2L)));
        }

        @Override
        public LargeInteger half() {
            return of(value.divide(BigInteger.valueOf(2L)));
        }

        @Override
        public LargeInteger log2() {
            if (isNonPositive()) {
                throw new ArithmeticException("Value is not positive");
            }
            
            return of(value.bitLength() - 1);
        }

        @Override
        public LargeInteger floorPowerOfTwo() {
            if (isNonPositive()) {
                throw new ArithmeticException("Value is not positive");
            }

            int bitPosition = value.bitLength() - 1;
            return of(BigInteger.ZERO.setBit(bitPosition));
        }

        @Override
        public LargeInteger ceilingPowerOfTwo() {
            if (isNonPositive()) {
                throw new ArithmeticException("Value is not positive");
            }
            
            int bitPosition = value.bitLength() - 1;
            if (!isPowerOfTwo()) {
                bitPosition++;
            }
            return of(BigInteger.ZERO.setBit(bitPosition));
        }

        @Override
        public LargeInteger add(long val) {
            return of(value.add(BigInteger.valueOf(val)));
        }

        @Override
        public LargeInteger subtract(long val) {
            return of(value.subtract(BigInteger.valueOf(val)));
        }

        @Override
        public LargeInteger multiply(long val) {
            return of(value.multiply(BigInteger.valueOf(val)));
        }

        @Override
        public LargeInteger divide(long val) {
            return of(value.divide(BigInteger.valueOf(val)));
        }

        @Override
        public LargeInteger remainder(long val) {
            return of(value.remainder(BigInteger.valueOf(val)));
        }

        @Override
        public LargeInteger mod(long m) {
            return of(value.mod(BigInteger.valueOf(m)));
        }
        
        @Override
        public boolean isDivisibleBy(long val) {
            return value.remainder(BigInteger.valueOf(val)).signum() == 0;
        }
        
        @Override
        public boolean isEqualTo(long val) {
            return false;
        }
        
        @Override
        public LargeInteger random(Random random) {
            if (isNonPositive()) {
                throw new ArithmeticException("Random bound must be positive");
            }
            
            int bitLength = value.subtract(BigInteger.ONE).bitLength();
            for (int i = 0; i < 100; i++) {
                BigInteger candidate = new BigInteger(bitLength, random);
                if (candidate.compareTo(value) < 0) {
                    return of(candidate);
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
