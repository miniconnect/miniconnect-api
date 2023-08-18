package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigInteger;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class LargeIntegerTest {
    
    private static final String CASE_DATA_DIR = "/hu/webarticum/miniconnect/lang/test-cases/LargeIntegerTest";
    

    @Test
    void testCreators() {
        
        // TODO
        
    }

    @Test
    void testArrayCreators() {

        // TODO
        
    }

    @Test
    void testCache() {
        assertThat(LargeInteger.of(-1)).isSameAs(LargeInteger.NEGATIVE_ONE);
        assertThat(LargeInteger.of(0)).isSameAs(LargeInteger.ZERO);
        assertThat(LargeInteger.of(1)).isSameAs(LargeInteger.ONE);
        assertThat(LargeInteger.of(2)).isSameAs(LargeInteger.TWO);
        assertThat(LargeInteger.of(10)).isSameAs(LargeInteger.TEN);
        assertThat(LargeInteger.of(-7)).isSameAs(LargeInteger.of(-7));
        assertThat(LargeInteger.of(5)).isSameAs(LargeInteger.of(5));
        assertThat(LargeInteger.of(21)).isSameAs(LargeInteger.of(21));
        assertThat(LargeInteger.of("266363024420535859")).isNotSameAs(LargeInteger.of("266363024420535859"));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/compareTo-cases.csv", numLinesToSkip = 1)
    void testCompareTo(LargeInteger n, LargeInteger m, int cmpSignum) {
        assertThat(Integer.signum(n.compareTo(m))).as("%s cmp %s", n, m).isEqualTo(cmpSignum);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/byteValue-cases.csv", numLinesToSkip = 1)
    void testByteValue(LargeInteger n, byte byteValue) {
        assertThat(n.byteValue()).as("%s.byteValue()", n).isEqualTo(byteValue);
    }

    @Test
    void testByteValueExact() {
        assertThat(LargeInteger.ZERO.byteValueExact()).isZero();
        assertThat(LargeInteger.of(10).byteValueExact()).isEqualTo((byte) 10);
        assertThat(LargeInteger.of(-10).byteValueExact()).isEqualTo((byte) -10);
        assertThat(LargeInteger.of(-200)).satisfies(n ->
                assertThatThrownBy(() -> n.byteValueExact()).isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(200)).satisfies(n ->
                assertThatThrownBy(() -> n.byteValueExact()).isInstanceOf(ArithmeticException.class));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/shortValue-cases.csv", numLinesToSkip = 1)
    void testShortValue(LargeInteger n, short shortValue) {
        assertThat(n.shortValue()).as("%s.shortValue()", n).isEqualTo(shortValue);
    }

    @Test
    void testShortValueExact() {
        assertThat(LargeInteger.ZERO.shortValueExact()).isZero();
        assertThat(LargeInteger.of(10).shortValueExact()).isEqualTo((short) 10);
        assertThat(LargeInteger.of(-10).shortValueExact()).isEqualTo((short) -10);
        assertThat(LargeInteger.of(-200).shortValueExact()).isEqualTo((short) -200);
        assertThat(LargeInteger.of(200).shortValueExact()).isEqualTo((short) 200);
        assertThat(LargeInteger.of(-40000)).satisfies(n ->
                assertThatThrownBy(() -> n.shortValueExact()).isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(40000)).satisfies(n ->
                assertThatThrownBy(() -> n.shortValueExact()).isInstanceOf(ArithmeticException.class));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/intValue-cases.csv", numLinesToSkip = 1)
    void testIntValue(LargeInteger n, int intValue) {
        assertThat(n.intValue()).as("%s.intValue()", n).isEqualTo(intValue);
    }

    @Test
    void testIntValueExact() {
        assertThat(LargeInteger.ZERO.intValueExact()).isZero();
        assertThat(LargeInteger.of(10).intValueExact()).isEqualTo(10);
        assertThat(LargeInteger.of(-10).intValueExact()).isEqualTo(-10);
        assertThat(LargeInteger.of(-200).intValueExact()).isEqualTo(-200);
        assertThat(LargeInteger.of(200).intValueExact()).isEqualTo(200);
        assertThat(LargeInteger.of(-40000).intValueExact()).isEqualTo(-40000);
        assertThat(LargeInteger.of(40000).intValueExact()).isEqualTo(40000);
        assertThat(LargeInteger.of(-3000000000L)).satisfies(n ->
                assertThatThrownBy(() -> n.intValueExact()).isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(3000000000L)).satisfies(n ->
                assertThatThrownBy(() -> n.intValueExact()).isInstanceOf(ArithmeticException.class));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/longValue-cases.csv", numLinesToSkip = 1)
    void testLongValue(LargeInteger n, long longValue) {
        assertThat(n.longValue()).as("%s.longValue()", n).isEqualTo(longValue);
    }

    @Test
    void testLongValueExact() {
        assertThat(LargeInteger.ZERO.longValueExact()).isZero();
        assertThat(LargeInteger.of(10).longValueExact()).isEqualTo(10L);
        assertThat(LargeInteger.of(-10).longValueExact()).isEqualTo(-10L);
        assertThat(LargeInteger.of(-200).longValueExact()).isEqualTo(-200L);
        assertThat(LargeInteger.of(200).longValueExact()).isEqualTo(200L);
        assertThat(LargeInteger.of(-40000).longValueExact()).isEqualTo(-40000L);
        assertThat(LargeInteger.of(40000).longValueExact()).isEqualTo(40000L);
        assertThat(LargeInteger.of(-3000000000L).longValueExact()).isEqualTo(-3000000000L);
        assertThat(LargeInteger.of(3000000000L).longValueExact()).isEqualTo(3000000000L);
        assertThat(LargeInteger.of("-10000000000000000000")).satisfies(n ->
                assertThatThrownBy(() -> n.longValueExact()).isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of("10000000000000000000")).satisfies(n ->
                assertThatThrownBy(() -> n.longValueExact()).isInstanceOf(ArithmeticException.class));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/floatValue-cases.csv", numLinesToSkip = 1)
    void testFloatValue(LargeInteger n, float floatValue) {
        assertThat(n.floatValue()).isCloseTo(floatValue, Percentage.withPercentage(1e-5));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/doubleValue-cases.csv", numLinesToSkip = 1)
    void testDoubleValue(LargeInteger n, double doubleValue) {
        assertThat(n.doubleValue()).isCloseTo(doubleValue, Percentage.withPercentage(1e-10));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/toByteArray-cases.csv", numLinesToSkip = 1)
    void testToByteArray(LargeInteger n, String bytesHex) {
        byte[] bytes = convertHexToBytes(bytesHex);
        assertThat(n.toByteArray()).containsExactly(bytes);
    }
    
    private byte[] convertHexToBytes(String bytesHex) {
        String[] byteHexes = bytesHex.split(" ");
        byte[] result = new byte[byteHexes.length];
        for (int i = 0; i < byteHexes.length; i++) {
            result[i] = (byte) Short.parseShort(byteHexes[i], 16);
        }
        return result;
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/isProbablePrime-cases.csv", numLinesToSkip = 1)
    void testIsProbablePrime(LargeInteger n, int certainty, boolean isProbablePrime) {
        assertThat(n.isProbablePrime(certainty)).isEqualTo(isProbablePrime);
    }
    
    @Test
    void testIsProbablePrimeSameAsByBigInteger() {
        for (int i = 1; i <= 100; i++) {
            assertThat(LargeInteger.of(i).isProbablePrime(10)).isEqualTo(BigInteger.valueOf(i).isProbablePrime(10));
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/nextProbablePrime-cases.csv", numLinesToSkip = 1)
    void testNextProbablePrime(LargeInteger n, LargeInteger nextProbablePrime) {
        assertThat(n.nextProbablePrime()).isEqualTo(nextProbablePrime);
    }

    @Test
    void testNextProbablePrimeSameAsByBigInteger() {
        LargeInteger largeIntegerValue = LargeInteger.ONE;
        BigInteger bigIntegerValue = BigInteger.ONE;
        for (int i = 1; i <= 100; i++) {
            largeIntegerValue = largeIntegerValue.nextProbablePrime();
            bigIntegerValue = bigIntegerValue.nextProbablePrime();
            assertThat(largeIntegerValue.bigIntegerValue()).isEqualTo(bigIntegerValue);
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/min-cases.csv", numLinesToSkip = 1)
    void testMin(LargeInteger n, LargeInteger m, LargeInteger min) {
        assertThat(n.min(m)).as("min(%s, %s)", n, m).isEqualTo(min);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/max-cases.csv", numLinesToSkip = 1)
    void testMax(LargeInteger n, LargeInteger m, LargeInteger max) {
        assertThat(n.max(m)).as("max(%s, %s)", n, m).isEqualTo(max);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/add-cases.csv", numLinesToSkip = 1)
    void testAdd(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(n.add(m)).as("%s + %s", n, m).isEqualTo(result);
    }

    @Test
    void testXXX() {
        
        // TODO
        
        /*
        subtract(LargeInteger val)
        multiply(LargeInteger val)
        divide(LargeInteger val)
        divideAndRemainder(LargeInteger val)
        remainder(LargeInteger val)
        pow(int exponent)
        gcd(LargeInteger val)
        abs()
        negate()
        signum()
        mod(LargeInteger m)
        modPow(LargeInteger exponent, LargeInteger m)
        modInverse(LargeInteger m)
        shiftLeft(int n)
        shiftRight(int n)
        and(LargeInteger val)
        or(LargeInteger val)
        xor(LargeInteger val)
        not()
        andNot(LargeInteger val)
        testBit(int n)
        setBit(int n)
        clearBit(int n)
        flipBit(int n)
        getLowestSetBit()
        bitLength()
        bitCount()
        toBitSet()
        toString(int radix)
        bigIntegerValue()
        bigDecimalValue()
        isFittingInLong()
        isFittingInInt()
        isFittingInShort()
        isFittingInByte()
        isZero()
        isPositive()
        isNonPositive()
        isNegative()
        isNonNegative()
        isEven()
        isOdd()
        isDivisibleBy(LargeInteger val)
        isEqualTo(LargeInteger val)
        isLessThan(LargeInteger val)
        isLessThanOrEqualTo(LargeInteger val)
        isGreaterThan(LargeInteger val)
        isGreaterThanOrEqualTo(LargeInteger val)
        isPowerOfTwo()
        increment()
        decrement()
        */
    }
    
}
