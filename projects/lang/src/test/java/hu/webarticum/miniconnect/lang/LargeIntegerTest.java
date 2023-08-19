package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.BitSet;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

class LargeIntegerTest {
    
    private static final String CASE_DATA_DIR = "/hu/webarticum/miniconnect/lang/test-cases/LargeIntegerTest";
    

    @Test
    void testCreatorsPrimitive() {
        assertThat(LargeInteger.of((byte) 123).bigIntegerValue()).isEqualTo(123);
        assertThat(LargeInteger.of((short) 12345).bigIntegerValue()).isEqualTo(12345);
        assertThat(LargeInteger.of(978675645).bigIntegerValue()).isEqualTo(978675645);
        assertThat(LargeInteger.of(958678475673645625L).bigIntegerValue()).isEqualTo(958678475673645625L);
        assertThat(LargeInteger.of(-237352934872934857L).bigIntegerValue()).isEqualTo(-237352934872934857L);
    }

    @Test
    void testCreatorsString() {
        assertThat(LargeInteger.of("52344630590748003749376284693738").bigIntegerValue())
                .isEqualTo("52344630590748003749376284693738");
        assertThat(LargeInteger.of("32", 5).bigIntegerValue()).isEqualTo(17);
        assertThat(LargeInteger.of("32", 30).bigIntegerValue()).isEqualTo(92);
        assertThat(LargeInteger.of("-32", 30).bigIntegerValue()).isEqualTo(-92);
        assertThat(LargeInteger.of("11001010011100111010110111001", 2).bigIntegerValue()).isEqualTo(424572345);
        assertThat(LargeInteger.of("B42AB8BBF1D", 16).bigIntegerValue()).isEqualTo(12380973809437L);
        assertThat(LargeInteger.of("4AF607BCC3482EDA3467A0643AF7E428EB34", 16).bigIntegerValue())
                .isEqualTo("6530028905921377813337721059013328158386996");
    }

    @Test
    void testCreatorsStringThrow() {
        assertThatThrownBy(() -> LargeInteger.of("2346i73")).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> LargeInteger.of("52845723948573458975845738745x437"))
                .isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> LargeInteger.of("B42AB8BBF1D", 15)).isInstanceOf(NumberFormatException.class);
        assertThatThrownBy(() -> LargeInteger.of("4AF607BCC3482EDA3467A0643AF7E428EB34", 15))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    void testCreatorsBytes() {
        assertThat(LargeInteger.of(new byte[0]).bigIntegerValue()).isZero();
        assertThat(LargeInteger.of(new byte[] { 0 }).bigIntegerValue()).isZero();
        assertThat(LargeInteger.of(new byte[] { 0, 0, 0, 0, 0 }).bigIntegerValue()).isZero();
        assertThat(LargeInteger.of(new byte[] { 13, -42, 1, -56, -122, 7, 1, 123 }).bigIntegerValue())
                .isEqualTo(996986328262836603L);
        assertThat(LargeInteger.of(new byte[] { 13, -42, 1, -56, -122, 7, 1 }).bigIntegerValue())
                .isEqualTo(3894477844776705L);
        assertThat(LargeInteger.of(new byte[] { -13, -42, 1, -56, -122, 7, 1 }).bigIntegerValue())
                .isEqualTo(-3423871549700351L);
        assertThat(LargeInteger.of(new byte[] { 13, -42, 1, -56, -122, 7, 1, 123, 99, -77 }).bigIntegerValue())
                .isEqualTo("65338496009033259639731");
        assertThat(LargeInteger.nonNegativeOf(new byte[] { -13, -42, 1, -56, -122, 7, 1 }).bigIntegerValue())
                .isEqualTo(68633722488227585L);
    }

    @Test
    void testCreatorsObject() {
        assertThat(LargeInteger.of(new BigInteger("73658273647235762749234572072073628073")).bigIntegerValue())
                .isEqualTo("73658273647235762749234572072073628073");
        assertThat(LargeInteger.of(BitSet.valueOf(new byte[] { 42, 0, -3, 42, 12 })).bigIntegerValue())
                .isEqualTo(52260831274L);
        assertThat(LargeInteger.nonNegativeOf(BitSet.valueOf(new byte[] { 42, 0, -3, 42, -12 })).bigIntegerValue())
                .isEqualTo(1048693243946L);
    }
    
    @Test
    void testArrayCreators() {
        assertThat(LargeInteger.arrayOf(3L, 3125L, -1L, 0L)).containsExactly(
                LargeInteger.of(3L), LargeInteger.of(3125L), LargeInteger.of(-1L), LargeInteger.of(0L));
        assertThat(LargeInteger.arrayOf("9", "-2556", "173468174555349823472347")).containsExactly(
                LargeInteger.of(9), LargeInteger.of(-2556), LargeInteger.of("173468174555349823472347"));
        assertThat(LargeInteger.arrayOf(
                BigInteger.valueOf(42), BigInteger.valueOf(-234234), new BigInteger("4234782945723"))).containsExactly(
                LargeInteger.of(42), LargeInteger.of(-234234), LargeInteger.of(4234782945723L));
        assertThat(LargeInteger.arrayOf(
                LargeInteger.of(11), LargeInteger.of(-32), LargeInteger.of("58234957934591491745394"))).containsExactly(
                LargeInteger.of(11), LargeInteger.of(-32), LargeInteger.of("58234957934591491745394"));
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
    @CsvFileSource(resources = CASE_DATA_DIR + "/compareToRelated-cases.csv", numLinesToSkip = 1)
    void testCompareToRelated(
            LargeInteger n,
            LargeInteger m,
            int cmpSignum,
            boolean equalTo,
            boolean lessThan,
            boolean lessThanOrEqualTo,
            boolean greaterThan,
            boolean greaterThanOrEqualTo) {
        assertThat(Integer.signum(n.compareTo(m))).as("%s cmp %s", n, m).isEqualTo(cmpSignum);
        assertThat(n.isEqualTo(m)).as("%s == %s", n, m).isEqualTo(equalTo);
        assertThat(n.equals(m)).as("%s.equals(%s)", n, m).isEqualTo(equalTo);
        assertThat(n.isLessThan(m)).as("%s < %s", n, m).isEqualTo(lessThan);
        assertThat(n.isLessThanOrEqualTo(m)).as("%s <= %s", n, m).isEqualTo(lessThanOrEqualTo);
        assertThat(n.isGreaterThan(m)).as("%s > %s", n, m).isEqualTo(greaterThan);
        assertThat(n.isGreaterThanOrEqualTo(m)).as("%s >= %s", n, m).isEqualTo(greaterThanOrEqualTo);
        
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

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/subtract-cases.csv", numLinesToSkip = 1)
    void testSubtract(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(n.subtract(m)).as("%s - %s", n, m).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/multiply-cases.csv", numLinesToSkip = 1)
    void testMultiply(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(n.multiply(m)).as("%s * %s", n, m).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/pow-cases.csv", numLinesToSkip = 1)
    void testPow(LargeInteger n, int exponent, LargeInteger result) {
        assertThat(n.pow(exponent)).as("%s ^ %d", n, exponent).isEqualTo(result);
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/divide-cases.csv", numLinesToSkip = 1)
    void testDivide(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(n.divide(m)).as("%s / %s", n, m).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/divideAndRemainder-cases.csv", numLinesToSkip = 1)
    void testDivideAndRemainder(LargeInteger n, LargeInteger m, LargeInteger result, LargeInteger remainder) {
        LargeInteger[] resultAndRemainder = n.divideAndRemainder(m);
        assertThat(resultAndRemainder[0]).as("%s.divideAndRemainder(%s).result", n, m).isEqualTo(result);
        assertThat(resultAndRemainder[1]).as("%s.divideAndRemainder(%s).remainder", n, m).isEqualTo(remainder);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/remainder-cases.csv", numLinesToSkip = 1)
    void testRemainder(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(n.remainder(m)).as("%s % %s", n, m).isEqualTo(result);
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/mod-cases.csv", numLinesToSkip = 1)
    void testMod(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(n.mod(m)).as("%s mod %s", n, m).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/modPow-cases.csv", numLinesToSkip = 1)
    void testModPow(LargeInteger n, LargeInteger pow, LargeInteger m, LargeInteger result) {
        assertThat(n.modPow(pow, m)).as("%s^%s mod %s", n, pow, m).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/modInverse-cases.csv", numLinesToSkip = 1)
    void testModInverse(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(n.modInverse(m)).as("%s mod-1 %s", n, m).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/isDivisibleBy-cases.csv", numLinesToSkip = 1)
    void testIsDivisibleBy(LargeInteger n, LargeInteger m, boolean divisible) {
        assertThat(n.isDivisibleBy(m)).as("%2$s | %1$s", n, m).isEqualTo(divisible);
    }

    @Test
    void testDivisionByZero() {
        LargeInteger n = LargeInteger.of(123);
        LargeInteger e = LargeInteger.of(15);
        assertThatThrownBy(() -> n.divide(LargeInteger.ZERO)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.remainder(LargeInteger.ZERO)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.divideAndRemainder(LargeInteger.ZERO)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.mod(LargeInteger.ZERO)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.modPow(e, LargeInteger.ZERO)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.modInverse(LargeInteger.ZERO)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.modInverse(n)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.isDivisibleBy(LargeInteger.ZERO)).isInstanceOf(ArithmeticException.class);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/gcd-cases.csv", numLinesToSkip = 1)
    void testGcd(LargeInteger n, LargeInteger m, LargeInteger gcd) {
        assertThat(n.gcd(m)).as("gcd(%s, %1)", n, m).isEqualTo(gcd);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/isEvenOddPowerOfTwo-cases.csv", numLinesToSkip = 1)
    void testIsEvenOddPowerOfTwo(LargeInteger n, boolean even, boolean odd, boolean powerOfTwo) {
        assertThat(n.isEven()).isEqualTo(even);
        assertThat(n.isOdd()).isEqualTo(odd);
        assertThat(n.isPowerOfTwo()).isEqualTo(powerOfTwo);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/signumRelated-cases.csv", numLinesToSkip = 1)
    void testSignumRelated(
            LargeInteger n,
            int signum,
            boolean zero,
            boolean positive,
            boolean nonPositive,
            boolean negative,
            boolean nonNegative,
            LargeInteger abs,
            LargeInteger negated) {
        assertThat(n.signum()).isEqualTo(signum);
        assertThat(n.isZero()).isEqualTo(zero);
        assertThat(n.isPositive()).isEqualTo(positive);
        assertThat(n.isNonPositive()).isEqualTo(nonPositive);
        assertThat(n.isNegative()).isEqualTo(negative);
        assertThat(n.isNonNegative()).isEqualTo(nonNegative);
        assertThat(n.abs()).isEqualTo(abs);
        assertThat(n.negate()).isEqualTo(negated);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/incrementDecrement-cases.csv", numLinesToSkip = 1)
    void testIncrementDecrement(LargeInteger n, LargeInteger incremented, LargeInteger decremented) {
        assertThat(n.increment()).isEqualTo(incremented);
        assertThat(n.decrement()).isEqualTo(decremented);
    }

    @ParameterizedTest
    @ValueSource(longs = { -237682734235L, -32L, 0L, 54L, 132L, 3458793857L, 3458603928750237205L })
    void testHashCodeSmall(long value) {
        assertThat(LargeInteger.of(value)).hasSameHashCodeAs(value);
    }

    @ParameterizedTest
    @ValueSource(strings = { "-23462435820793856783445934", "527304597834057925806734072474" })
    void testHashCodeLarge(String value) {
        assertThat(LargeInteger.of(value)).hasSameHashCodeAs(new BigInteger(value));
    }

    @Test
    void testEquals() {
        assertThat(LargeInteger.of(0L)).isEqualTo(LargeInteger.ZERO);
        assertThat(LargeInteger.of(2354)).isEqualTo(LargeInteger.of(2354));
        assertThat(LargeInteger.of("24800734658709265845")).isEqualTo(LargeInteger.of("24800734658709265845"));
        assertThat(LargeInteger.of(4235)).isNotEqualTo(LargeInteger.of(7294));
        assertThat(LargeInteger.of("562384864853845273459308085003"))
                .isNotEqualTo(LargeInteger.of("92584759238457857923586795605727502"));
        assertThat(LargeInteger.of("562384864853845273459308085003"))
                .isNotEqualTo(new BigInteger("562384864853845273459308085003"));
        assertThat(LargeInteger.of("562384864853845273459308085003"))
                .isNotEqualTo(new Object());
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/toString-cases.csv", numLinesToSkip = 1)
    void testToString(LargeInteger n, String stringValue) {
        assertThat(n).hasToString(stringValue);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/toStringRadix-cases.csv", numLinesToSkip = 1)
    void testToStringRadix(LargeInteger n, int toRadix, String stringValue) {
        assertThat(n.toString(toRadix)).as("%s --> (%d)", n, toRadix).isEqualTo(stringValue);
    }

    @ParameterizedTest
    @ValueSource(strings = { "-23462435820793856783445934", "-32", "0", "54", "527304597834057925806734072474" })
    void testBigIntegerValue(String value) {
        assertThat(LargeInteger.of(value).bigIntegerValue()).isEqualTo(new BigInteger(value));
    }

    @ParameterizedTest
    @ValueSource(strings = { "-23462435820793856783445934", "-32", "0", "54", "527304597834057925806734072474" })
    void testBigDecimalValue(String value) {
        assertThat(LargeInteger.of(value).bigDecimalValue()).isEqualTo(new BigDecimal(new BigInteger(value)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/toBitSet-cases.csv", numLinesToSkip = 1)
    void testToBitSet(LargeInteger n, String bitSetBytesHex) {
        assertThat(n.toBitSet()).isEqualTo(BitSet.valueOf(convertHexToBytes(bitSetBytesHex)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/fittingIn-cases.csv", numLinesToSkip = 1)
    void testFittingIn(
            LargeInteger n,
            boolean fittingInLong,
            boolean fittingInInt,
            boolean fittingInShort,
            boolean fittingInByte) {
        assertThat(n.isFittingInLong()).as("%s.isFittingInLong()", n).isEqualTo(fittingInLong);
        assertThat(n.isFittingInInt()).as("%s.isFittingInInt()", n).isEqualTo(fittingInInt);
        assertThat(n.isFittingInShort()).as("%s.isFittingInShort()", n).isEqualTo(fittingInShort);
        assertThat(n.isFittingInByte()).as("%s.isFittingInByte()", n).isEqualTo(fittingInByte);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/bitStat-cases.csv", numLinesToSkip = 1)
    void testBitStat(LargeInteger n, int lowestSetBit, int bitLength, int bitCount) {
        assertThat(n.getLowestSetBit()).as("%s.getLowestSetBit()", n).isEqualTo(lowestSetBit);
        assertThat(n.bitLength()).as("%s.bitLength()", n).isEqualTo(bitLength);
        assertThat(n.bitCount()).as("%s.bitCount()", n).isEqualTo(bitCount);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/binaryBinary-cases.csv", numLinesToSkip = 1)
    void testBinaryBinary(
            LargeInteger n,
            LargeInteger m,
            LargeInteger andResult,
            LargeInteger orResult,
            LargeInteger xorResult,
            LargeInteger andNotResult) {
        assertThat(n.and(m)).as("%s & %s", n, m).isEqualTo(andResult);
        assertThat(n.or(m)).as("%s | %s", n, m).isEqualTo(orResult);
        assertThat(n.xor(m)).as("%s xor %s", n, m).isEqualTo(xorResult);
        assertThat(n.andNot(m)).as("%s & not %s", n, m).isEqualTo(andNotResult);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/not-cases.csv", numLinesToSkip = 1)
    void testNot(LargeInteger n, LargeInteger result) {
        assertThat(n.not()).as("not %s", n).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/bitBased-cases.csv", numLinesToSkip = 1)
    void testBitBased(
            LargeInteger n,
            int bitIndex,
            boolean set,
            LargeInteger setResult,
            LargeInteger clearResult,
            LargeInteger flipResult) {
        assertThat(n.testBit(bitIndex)).as("%s[%d]", n, bitIndex).isEqualTo(set);
        assertThat(n.setBit(bitIndex)).as("set %s[%d]", n, bitIndex).isEqualTo(setResult);
        assertThat(n.clearBit(bitIndex)).as("clear %s[%d]", n, bitIndex).isEqualTo(clearResult);
        assertThat(n.flipBit(bitIndex)).as("flip %s[%d]", n, bitIndex).isEqualTo(flipResult);
    }
    
    @Test
    void testBitBasedThrow() {
        assertThatThrownBy(() -> LargeInteger.ONE.testBit(-1)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> LargeInteger.ONE.setBit(-1)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> LargeInteger.ONE.clearBit(-1)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> LargeInteger.ONE.flipBit(-1)).isInstanceOf(ArithmeticException.class);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/shiftLeft-cases.csv", numLinesToSkip = 1)
    void testShiftLeft(LargeInteger n, int p, LargeInteger result) {
        assertThat(n.shiftLeft(p)).as("%s << %d", n, p).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/shiftRight-cases.csv", numLinesToSkip = 1)
    void testShiftRight(LargeInteger n, int p, LargeInteger result) {
        assertThat(n.shiftRight(p)).as("%s >> %d", n, p).isEqualTo(result);
    }

    private byte[] convertHexToBytes(String bytesHex) {
        String[] byteHexes = bytesHex.split(" ");
        byte[] result = new byte[byteHexes.length];
        for (int i = 0; i < byteHexes.length; i++) {
            result[i] = (byte) Short.parseShort(byteHexes[i], 16);
        }
        return result;
    }

}
