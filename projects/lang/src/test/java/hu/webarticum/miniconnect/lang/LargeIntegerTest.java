package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class LargeIntegerTest {
    
    private static final String CASE_DATA_DIR = "/hu/webarticum/miniconnect/lang/test-cases/LargeIntegerTest";
    
    
    @Test
    void testCreators() {

        // TODO
        
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

        // TODO
        
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/shortValue-cases.csv", numLinesToSkip = 1)
    void testShortValue(LargeInteger n, short shortValue) {
        assertThat(n.shortValue()).as("%s.shortValue()", n).isEqualTo(shortValue);
    }

    @Test
    void testShortValueExact() {

        // TODO
        
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/intValue-cases.csv", numLinesToSkip = 1)
    void testIntValue(LargeInteger n, int intValue) {
        assertThat(n.intValue()).as("%s.intValue()", n).isEqualTo(intValue);
    }

    @Test
    void testIntValueExact() {

        // TODO
        
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/longValue-cases.csv", numLinesToSkip = 1)
    void testLongValue(LargeInteger n, long longValue) {
        assertThat(n.longValue()).as("%s.longValue()", n).isEqualTo(longValue);
    }

    @Test
    void testLongValueExact() {

        // TODO
        
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/floatValue-cases.csv", numLinesToSkip = 1)
    void testFloatValue(LargeInteger n, float floatValue) {
        
        // TODO
        // approximation...
        
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/doubleValue-cases.csv", numLinesToSkip = 1)
    void testDoubleValue(LargeInteger n, double doubleValue) {
        
        // TODO
        // approximation...
        
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/toByteArray-cases.csv", numLinesToSkip = 1)
    void testToByteArray(LargeInteger n, String bytesHex) {
        byte[] bytes = convertHexToBytes(bytesHex);
        assertThat(n.toByteArray()).containsExactly(bytes);
    }
    
    private byte[] convertHexToBytes(String bytesHex) {
        if (bytesHex.isEmpty()) {
            return new byte[0];
        }
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
