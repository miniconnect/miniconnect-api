package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.BitSet;
import java.util.Random;

import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

class LargeIntegerTest {
    
    private static final String CASE_DATA_DIR = "/hu/webarticum/miniconnect/lang/test-cases/LargeIntegerTest";

    @Test
    void testCreatorsPrimitiveIntegral() {
        assertThat(LargeInteger.of((byte) 123).bigIntegerValue()).isEqualTo(123);
        assertThat(LargeInteger.of((short) 12345).bigIntegerValue()).isEqualTo(12345);
        assertThat(LargeInteger.of(978675645).bigIntegerValue()).isEqualTo(978675645);
        assertThat(LargeInteger.of(958678475673645625L).bigIntegerValue()).isEqualTo(958678475673645625L);
        assertThat(LargeInteger.of(-237352934872934857L).bigIntegerValue()).isEqualTo(-237352934872934857L);
    }

    @Test
    void testCreatorsPrimitiveIntegralUnsigned() {
        assertThat(LargeInteger.ofUnsignedByte((byte) 0).bigIntegerValue()).isZero();
        assertThat(LargeInteger.ofUnsignedByte((byte) 48).bigIntegerValue()).isEqualTo(48);
        assertThat(LargeInteger.ofUnsignedByte((byte) -1).bigIntegerValue()).isEqualTo(255);
        assertThat(LargeInteger.ofUnsignedByte((byte) -107).bigIntegerValue()).isEqualTo(149);
        assertThat(LargeInteger.ofUnsignedShort((short) 0).bigIntegerValue()).isZero();
        assertThat(LargeInteger.ofUnsignedShort((short) 48233).bigIntegerValue()).isEqualTo(48233);
        assertThat(LargeInteger.ofUnsignedShort((short) -1).bigIntegerValue()).isEqualTo(65535);
        assertThat(LargeInteger.ofUnsignedShort((short) -14573).bigIntegerValue()).isEqualTo(50963);
        assertThat(LargeInteger.ofUnsignedShort((byte) -107).bigIntegerValue()).isEqualTo(65429);
        assertThat(LargeInteger.ofUnsignedInt(0).bigIntegerValue()).isZero();
        assertThat(LargeInteger.ofUnsignedInt(43289359).bigIntegerValue()).isEqualTo(43289359);
        assertThat(LargeInteger.ofUnsignedInt(-1).bigIntegerValue()).isEqualTo(4294967295L);
        assertThat(LargeInteger.ofUnsignedInt(-32783954).bigIntegerValue()).isEqualTo(4262183342L);
        assertThat(LargeInteger.ofUnsignedInt((byte) -107).bigIntegerValue()).isEqualTo(4294967189L);
        assertThat(LargeInteger.ofUnsignedInt((short) -14573).bigIntegerValue()).isEqualTo(4294952723L);
        assertThat(LargeInteger.ofUnsignedLong(0L).bigIntegerValue()).isZero();
        assertThat(LargeInteger.ofUnsignedLong(54783925).bigIntegerValue()).isEqualTo(54783925);
        assertThat(LargeInteger.ofUnsignedLong(70828395234752L).bigIntegerValue()).isEqualTo(70828395234752L);
        assertThat(LargeInteger.ofUnsignedLong(-1).bigIntegerValue()).isEqualTo("18446744073709551615");
        assertThat(LargeInteger.ofUnsignedLong(-5278345912984374L).bigIntegerValue()).isEqualTo("18441465727796567242");
        assertThat(LargeInteger.ofUnsignedLong((byte) -107).bigIntegerValue()).isEqualTo("18446744073709551509");
        assertThat(LargeInteger.ofUnsignedLong((short) -14573).bigIntegerValue()).isEqualTo("18446744073709537043");
        assertThat(LargeInteger.ofUnsignedLong((int) -32783954).bigIntegerValue()).isEqualTo("18446744073676767662");
    }

    @Test
    void testCreatorsDouble() {
        assertThat(LargeInteger.of(0.0).bigIntegerValue()).isZero();
        assertThat(LargeInteger.of(254678934.43).bigIntegerValue()).isEqualTo(254678934);
        assertThat(LargeInteger.of(254675347398934.9).bigIntegerValue()).isEqualTo(254675347398934L);
        assertThat(LargeInteger.of(254675347398930000000000.0).bigIntegerValue()).isEqualTo("254675347398930000000000");
        assertThat(LargeInteger.of(-1.0).bigIntegerValue()).isEqualTo(-1);
        assertThat(LargeInteger.of(-526374925723849.1).bigIntegerValue()).isEqualTo(-526374925723849L);
        assertThat(LargeInteger.of(-5436784573948000000000.0).bigIntegerValue()).isEqualTo("-5436784573948000000000");
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
        assertThat(LargeInteger.of(new byte[] { 13, -42, 1, -56, -122, 7, 1 }).bigIntegerValue())
                .isEqualTo(3894477844776705L);
        assertThat(LargeInteger.of(new byte[] { -13, -42, 1, -56, -122, 7, 1 }).bigIntegerValue())
                .isEqualTo(-3423871549700351L);
        assertThat(LargeInteger.of(new byte[] { 13, -42, 1, -56, -122, 7, 1, 123 }).bigIntegerValue())
                .isEqualTo(996986328262836603L);
        assertThat(LargeInteger.of(new byte[] { 13, -42, 1, -56, -122, 7, 1, 123, 99, -77 }).bigIntegerValue())
                .isEqualTo("65338496009033259639731");
    }

    @Test
    void testCreatorsBytesUnsigned() {
        assertThat(LargeInteger.ofUnsigned(new byte[0]).bigIntegerValue()).isZero();
        assertThat(LargeInteger.ofUnsigned(new byte[] { 0 }).bigIntegerValue()).isZero();
        assertThat(LargeInteger.ofUnsigned(new byte[] { -1 }).bigIntegerValue()).isEqualTo(255);
        assertThat(LargeInteger.ofUnsigned(new byte[] { -13, -42, 1, -56, -122, 7, 1 }).bigIntegerValue())
                .isEqualTo(68633722488227585L);
        assertThat(LargeInteger.ofUnsigned(new byte[] { 13, -42, 1, -56, -122, 7, 1, 4 }).bigIntegerValue())
                .isEqualTo(996986328262836484L);
        assertThat(LargeInteger.ofUnsigned(new byte[] { -13, -42, 1, -56, -122, 7, 1, 4 }).bigIntegerValue())
                .isEqualTo("17570232956986261764");
        assertThat(LargeInteger.ofUnsigned(new byte[] { 13, -42, 1, -56, -122, 7, 1, 123, 99, -77 }).bigIntegerValue())
                .isEqualTo("65338496009033259639731");
    }

    @Test
    void testCreatorsObject() {
        assertThat(LargeInteger.of(new BigInteger("73658273647235762749234572072073628073")).bigIntegerValue())
                .isEqualTo("73658273647235762749234572072073628073");
        assertThat(LargeInteger.of(BitSet.valueOf(new byte[] { 42, 0, -3, 42, 12 })).bigIntegerValue())
                .isEqualTo(52260831274L);
        assertThat(LargeInteger.ofUnsigned(BitSet.valueOf(new byte[] { 42, 0, -3, 42, -12 })).bigIntegerValue())
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
    void testConstants() {
        assertThat(LargeInteger.NEGATIVE_ONE.longValueExact()).isEqualTo(-1);
        assertThat(LargeInteger.ZERO.longValueExact()).isEqualTo(0L);
        assertThat(LargeInteger.ONE.longValueExact()).isEqualTo(1L);
        assertThat(LargeInteger.TWO.longValueExact()).isEqualTo(2L);
        assertThat(LargeInteger.THREE.longValueExact()).isEqualTo(3L);
        assertThat(LargeInteger.FOUR.longValueExact()).isEqualTo(4L);
        assertThat(LargeInteger.FIVE.longValueExact()).isEqualTo(5L);
        assertThat(LargeInteger.SIX.longValueExact()).isEqualTo(6L);
        assertThat(LargeInteger.SEVEN.longValueExact()).isEqualTo(7L);
        assertThat(LargeInteger.EIGHT.longValueExact()).isEqualTo(8L);
        assertThat(LargeInteger.NINE.longValueExact()).isEqualTo(9L);
        assertThat(LargeInteger.TEN.longValueExact()).isEqualTo(10L);
        assertThat(LargeInteger.ELEVEN.longValueExact()).isEqualTo(11L);
        assertThat(LargeInteger.TWELVE.longValueExact()).isEqualTo(12L);
        assertThat(LargeInteger.TWENTY.longValueExact()).isEqualTo(20L);
        assertThat(LargeInteger.HUNDRED.longValueExact()).isEqualTo(100L);
        
    }

    @Test
    void testCache() {
        assertThat(LargeInteger.of(-1)).isSameAs(LargeInteger.of(-1)).extracting(n -> n.intValue()).isEqualTo(-1);
        assertThat(LargeInteger.of(0)).isSameAs(LargeInteger.of(0)).extracting(n -> n.intValue()).isEqualTo(0);
        assertThat(LargeInteger.of(1)).isSameAs(LargeInteger.of(1)).extracting(n -> n.intValue()).isEqualTo(1);
        assertThat(LargeInteger.of(2)).isSameAs(LargeInteger.of(2)).extracting(n -> n.intValue()).isEqualTo(2);
        assertThat(LargeInteger.of(10)).isSameAs(LargeInteger.of(10)).extracting(n -> n.intValue()).isEqualTo(10);
        assertThat(LargeInteger.of(-7)).isSameAs(LargeInteger.of(-7)).extracting(n -> n.intValue()).isEqualTo(-7);
        assertThat(LargeInteger.of(5)).isSameAs(LargeInteger.of(5)).extracting(n -> n.intValue()).isEqualTo(5);
        assertThat(LargeInteger.of(21)).isSameAs(LargeInteger.of(21)).extracting(n -> n.intValue()).isEqualTo(21);
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
    @CsvFileSource(resources = CASE_DATA_DIR + "/primitiveValue-cases.csv", numLinesToSkip = 1)
    void testPrimitiveValue(
            LargeInteger n,
            byte byteValue,
            short shortValue,
            int intValue,
            long longValue,
            float floatValue,
            double doubleValue) {
        assertThat(n.byteValue()).as("%s.byteValue()", n).isEqualTo(byteValue);
        assertThat(n.shortValue()).as("%s.shortValue()", n).isEqualTo(shortValue);
        assertThat(n.intValue()).as("%s.intValue()", n).isEqualTo(intValue);
        assertThat(n.longValue()).as("%s.longValue()", n).isEqualTo(longValue);
        assertThat(n.floatValue()).isCloseTo(floatValue, Percentage.withPercentage(1e-5));
        assertThat(n.doubleValue()).isCloseTo(doubleValue, Percentage.withPercentage(1e-10));
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
    @CsvFileSource(resources = CASE_DATA_DIR + "/toByteArray-cases.csv", numLinesToSkip = 1)
    void testToByteArray(LargeInteger n, String bytesHex) {
        byte[] bytes = convertHexToBytes(bytesHex);
        assertThat(n.toByteArray()).containsExactly(bytes);
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/isProbablePrime-cases.csv", numLinesToSkip = 1)
    void testIsProbablePrime(LargeInteger n, int certainty, boolean isProbablePrime) {
        assertThat(n.isProbablePrime(certainty)).as("%s.isProbablePrime(%d)", n, certainty).isEqualTo(isProbablePrime);
    }
    
    @Test
    void testIsProbablePrimeSameAsByBigInteger() {
        int certainty = 10;
        for (int i = 1; i <= 100; i++) {
            assertThat(LargeInteger.of(i).isProbablePrime(certainty)).as("%d.isProbablePrime(%d)", i, certainty)
                    .isEqualTo(BigInteger.valueOf(i).isProbablePrime(certainty));
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/nextProbablePrime-cases.csv", numLinesToSkip = 1)
    void testNextProbablePrime(LargeInteger n, LargeInteger nextProbablePrime) {
        assertThat(n.nextProbablePrime()).as("%s.nextProbablePrime()", n).isEqualTo(nextProbablePrime);
    }

    @Test
    void testNextProbablePrimeSameAsByBigInteger() {
        LargeInteger largeIntegerValue = LargeInteger.ONE;
        BigInteger bigIntegerValue = BigInteger.ONE;
        for (int i = 1; i <= 100; i++) {
            largeIntegerValue = largeIntegerValue.nextProbablePrime();
            bigIntegerValue = bigIntegerValue.nextProbablePrime();
            assertThat(largeIntegerValue.bigIntegerValue()).as("%s.nextProbablePrime()", largeIntegerValue)
                    .isEqualTo(bigIntegerValue);
        }
    }

    @Test
    void testNextProbablePrimeThrow() {
        assertThat(LargeInteger.of(-1)).satisfies(n -> assertThatThrownBy(() -> n.nextProbablePrime())
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(-4637)).satisfies(n -> assertThatThrownBy(() -> n.nextProbablePrime())
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(Long.MIN_VALUE)).satisfies(n -> assertThatThrownBy(() -> n.nextProbablePrime())
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of("-7516823740682704672346173480673234"))
                .satisfies(n -> assertThatThrownBy(() -> n.nextProbablePrime())
                .isInstanceOf(ArithmeticException.class));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/max-cases.csv", numLinesToSkip = 1)
    void testMax(LargeInteger max, LargeInteger other) {
        assertThat(max.max(other)).as("max(%s, %s)", max, other).isEqualTo(max);
        assertThat(other.max(max)).as("max(%s, %s)", other, max).isEqualTo(max);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/max-cases.csv", numLinesToSkip = 1)
    void testMin(LargeInteger max, LargeInteger other) {
        assertThat(max.min(other)).as("min(%s, %s)", max, other).isEqualTo(other);
        assertThat(other.min(max)).as("min(%s, %s)", other, max).isEqualTo(other);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/add-cases.csv", numLinesToSkip = 1)
    void testAdd(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(n.add(m)).as("%s + %s", n, m).isEqualTo(result);
        assertThat(m.add(n)).as("%s + %s", m, n).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/add-cases.csv", numLinesToSkip = 1)
    void testSubtract(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(result.subtract(m)).as("%s - %s", result, m).isEqualTo(n);
        assertThat(result.subtract(n)).as("%s - %s", result, n).isEqualTo(m);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/multiply-cases.csv", numLinesToSkip = 1)
    void testMultiply(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(n.multiply(m)).as("%s * %s", n, m).isEqualTo(result);
        assertThat(m.multiply(n)).as("%s * %s", m, n).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/multiplyDouble-cases.csv", numLinesToSkip = 1)
    void testMultiplyDouble(LargeInteger n, double m, LargeInteger result) {
        assertThat(n.multiply(m)).as("%s * %s", n, m).isEqualTo(result);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/pow-cases.csv", numLinesToSkip = 1)
    void testPow(LargeInteger n, int exponent, LargeInteger result) {
        assertThat(n.pow(exponent)).as("%s ^ %d", n, exponent).isEqualTo(result);
    }

    @Test
    void testPowThrow() {
        assertThatThrownBy(() -> LargeInteger.TEN.pow(-1)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> LargeInteger.TEN.pow(-3)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> LargeInteger.TEN.pow(-32342)).isInstanceOf(ArithmeticException.class);
        LargeInteger largeValue = LargeInteger.of("431827054723472902916834285349");
        assertThatThrownBy(() -> largeValue.pow(-1)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> largeValue.pow(-3)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> largeValue.pow(-32342)).isInstanceOf(ArithmeticException.class);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/sqrt-cases.csv", numLinesToSkip = 1)
    void testSqrt(LargeInteger n, LargeInteger result) {
        assertThat(n.sqrt()).as("sqrt(%s)", n).isEqualTo(result);
    }

    @Test
    void testSqrtThrow() {
        assertThatThrownBy(() -> LargeInteger.NEGATIVE_ONE.sqrt()).isInstanceOf(ArithmeticException.class);
        assertThat(LargeInteger.of(-2L)).satisfies(n ->
                assertThatThrownBy(() -> n.sqrt()).isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(-10L)).satisfies(n ->
                assertThatThrownBy(() -> n.sqrt()).isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(-3000000000L)).satisfies(n ->
                assertThatThrownBy(() -> n.sqrt()).isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of("-630726728761239200643702832335")).satisfies(n ->
                assertThatThrownBy(() -> n.sqrt()).isInstanceOf(ArithmeticException.class));
    }
    
    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/multiply-cases.csv", numLinesToSkip = 1)
    void testDivideRelatedNoRemainder(LargeInteger n, LargeInteger m, LargeInteger result) {
        if (!n.isZero()) {
            testDivideRelatedNoRemainderOneWay(result, n, m);
        } else if (!m.isZero()) {
            testDivideRelatedNoRemainderOneWay(result, m, n);
        }
    }
    
    void testDivideRelatedNoRemainderOneWay(LargeInteger toBeDivided, LargeInteger dividing, LargeInteger quotient) {
        assertThat(toBeDivided.divide(dividing)).as("%s / %s", toBeDivided, dividing).isEqualTo(quotient);
        assertThat(toBeDivided.remainder(dividing)).as("%s % %s", toBeDivided, dividing).isEqualTo(LargeInteger.ZERO);
        assertThat(toBeDivided.mod(dividing.abs())).as("%s mod %s", toBeDivided, dividing).isEqualTo(LargeInteger.ZERO);
        assertThat(toBeDivided.isDivisibleBy(dividing)).as("%s.isDivisibleBy(%s)", toBeDivided, dividing).isTrue();

        LargeInteger[] quotientAndRemainder = toBeDivided.divideAndRemainder(dividing);
        assertThat(quotientAndRemainder[0]).as("%s.divideAndRemainder(%s).result", toBeDivided, dividing)
                .isEqualTo(quotient);
        assertThat(quotientAndRemainder[1]).as("%s.divideAndRemainder(%s).remainder", toBeDivided, dividing)
                .isEqualTo(LargeInteger.ZERO);

    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/divideAndRemainder-cases.csv", numLinesToSkip = 1)
    void testDivideAndRemainderRelated(LargeInteger n, LargeInteger m, LargeInteger result, LargeInteger remainder) {
        assertThat(n.divide(m)).as("%s / %s", n, m).isEqualTo(result);
        assertThat(n.remainder(m)).as("%s %% %s", n, m).isEqualTo(remainder);
        LargeInteger[] quotientAndRemainder = n.divideAndRemainder(m);
        assertThat(quotientAndRemainder[0]).as("%s.divideAndRemainder(%s).result", n, m).isEqualTo(result);
        assertThat(quotientAndRemainder[1]).as("%s.divideAndRemainder(%s).remainder", n, m).isEqualTo(remainder);
        LargeInteger mod = remainder.isNegative() ? n.add(remainder) : remainder;
        assertThat(n.mod(m.abs())).as("%s mod %s", n, m).isEqualTo(mod);
        assertThat(n.isDivisibleBy(m)).as("%s.isDivisibleBy(%s)", n, m).isEqualTo(remainder.isZero());
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/modPow-cases.csv", numLinesToSkip = 1)
    void testModPow(LargeInteger n, LargeInteger pow, LargeInteger m, LargeInteger result) {
        assertThat(n.modPow(pow, m)).as("%s^%s mod %s", n, pow, m).isEqualTo(result);
    }

    @Test
    void testModPowNonPositiveModulusThrow() {
        LargeInteger negativeExponent = LargeInteger.of(-3);
        LargeInteger negativeModulus = LargeInteger.of("-4315351552345256003240342342355054");
        
        assertThat(LargeInteger.of(123)).satisfies(n -> assertThatThrownBy(() ->
                n.modPow(LargeInteger.TWO, LargeInteger.ZERO))
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(7)).satisfies(n -> assertThatThrownBy(() ->
                n.modPow(negativeExponent, negativeModulus))
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(-4248452)).satisfies(n -> assertThatThrownBy(() ->
                n.modPow(negativeExponent, negativeModulus))
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of("5710824587203948283245672477"))
                .satisfies(n -> assertThatThrownBy(() -> n.modPow(negativeExponent, negativeModulus))
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of("-5060740937372951929237635638"))
                .satisfies(n -> assertThatThrownBy(() -> n.modPow(negativeExponent, negativeModulus))
                .isInstanceOf(ArithmeticException.class));
    }

    @Test
    void testModPowSmallNoInverseThrow() {
        LargeInteger n = LargeInteger.of(4);
        LargeInteger m = LargeInteger.of(6);
        LargeInteger negativeExponent = LargeInteger.of(-3);
        assertThatThrownBy(() -> m.modPow(negativeExponent, n)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.modPow(negativeExponent, m)).isInstanceOf(ArithmeticException.class);
    }

    @Test
    void testModPowLargeNoInverseThrow() {
        LargeInteger n = LargeInteger.of("444444444444444444444444444444444444444444");
        LargeInteger m = LargeInteger.of("666666666666666666666666666666666666666666");
        LargeInteger negativeExponent = LargeInteger.of(-3);
        assertThatThrownBy(() -> m.modPow(negativeExponent, n)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.modPow(negativeExponent, m)).isInstanceOf(ArithmeticException.class);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/modInverse-cases.csv", numLinesToSkip = 1)
    void testModInverse(LargeInteger n, LargeInteger m, LargeInteger result) {
        assertThat(n.modInverse(m)).as("%s mod-1 %s", n, m).isEqualTo(result);
    }

    @Test
    void testModInverseNonPositiveModulusThrow() {
        LargeInteger negativeModulus = LargeInteger.of(-3);
        
        assertThat(LargeInteger.of(123)).satisfies(n -> assertThatThrownBy(() -> n.modInverse(LargeInteger.ZERO))
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(7)).satisfies(n -> assertThatThrownBy(() -> n.modInverse(negativeModulus))
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of(-4248452)).satisfies(n -> assertThatThrownBy(() -> n.modInverse(negativeModulus))
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of("5710824587203948283245672477"))
                .satisfies(n -> assertThatThrownBy(() -> n.modInverse(negativeModulus))
                .isInstanceOf(ArithmeticException.class));
        assertThat(LargeInteger.of("-5060740937372951929237635638"))
                .satisfies(n -> assertThatThrownBy(() -> n.modInverse(negativeModulus))
                .isInstanceOf(ArithmeticException.class));
    }

    @Test
    void testModInverseSmallNoInverseThrow() {
        LargeInteger n = LargeInteger.of(4);
        LargeInteger m = LargeInteger.of(6);
        assertThatThrownBy(() -> m.modInverse(n)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.modInverse(m)).isInstanceOf(ArithmeticException.class);
    }

    @Test
    void testModInverseLargeNoInverseThrow() {
        LargeInteger n = LargeInteger.of("444444444444444444444444444444444444444444");
        LargeInteger m = LargeInteger.of("666666666666666666666666666666666666666666");
        assertThatThrownBy(() -> m.modInverse(n)).isInstanceOf(ArithmeticException.class);
        assertThatThrownBy(() -> n.modInverse(m)).isInstanceOf(ArithmeticException.class);
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
        assertThatThrownBy(() -> n.isDivisibleBy(LargeInteger.ZERO)).isInstanceOf(ArithmeticException.class);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/gcd-cases.csv", numLinesToSkip = 1)
    void testGcd(LargeInteger n, LargeInteger m, LargeInteger gcd) {
        assertThat(n.gcd(m)).as("gcd(%s, %s)", n, m).isEqualTo(gcd);
        assertThat(m.gcd(n)).as("gcd(%s, %s)", m, n).isEqualTo(gcd);
        assertThat(n.gcd(m.negate())).as("gcd(%s, %s)", n, m.negate()).isEqualTo(gcd);
        assertThat(m.negate().gcd(n)).as("gcd(%s, %s)", m.negate(), n).isEqualTo(gcd);
        assertThat(n.negate().gcd(m)).as("gcd(%s, %s)", n.negate(), m).isEqualTo(gcd);
        assertThat(m.gcd(n.negate())).as("gcd(%s, %s)", m, n.negate()).isEqualTo(gcd);
        assertThat(n.negate().gcd(m.negate())).as("gcd(%s, %s)", n.negate(), m.negate()).isEqualTo(gcd);
        assertThat(m.negate().gcd(n.negate())).as("gcd(%s, %s)", m.negate(), n.negate()).isEqualTo(gcd);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/isEvenOddPowerOfTwo-cases.csv", numLinesToSkip = 1)
    void testIsEvenOddPowerOfTwo(LargeInteger n, boolean even, boolean odd, boolean powerOfTwo) {
        assertThat(n.isEven()).as("%s is even", n).isEqualTo(even);
        assertThat(n.isOdd()).as("%s is odd", n).isEqualTo(odd);
        assertThat(n.isPowerOfTwo()).as("%s is power-of-two", n).isEqualTo(powerOfTwo);
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
        assertThat(n.signum()).as("%s.signum()", n).isEqualTo(signum);
        assertThat(n.isZero()).as("%s.isZero()", n).isEqualTo(zero);
        assertThat(n.isPositive()).as("%s.isPositive()", n).isEqualTo(positive);
        assertThat(n.isNonPositive()).as("%s.isNonPositive()", n).isEqualTo(nonPositive);
        assertThat(n.isNegative()).as("%s.isNegative()", n).isEqualTo(negative);
        assertThat(n.isNonNegative()).as("%s.isNonNegative()", n).isEqualTo(nonNegative);
        assertThat(n.abs()).as("%s.abs()", n).isEqualTo(abs);
        assertThat(n.negate()).as("%s.negate()", n).isEqualTo(negated);
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/incrementDecrement-cases.csv", numLinesToSkip = 1)
    void testIncrementDecrement(LargeInteger n, LargeInteger incremented, LargeInteger decremented) {
        assertThat(n.increment()).as("%s++", n).isEqualTo(incremented);
        assertThat(n.decrement()).as("%s--", n).isEqualTo(decremented);
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
        assertThat(LargeInteger.of(4235)).isNotEqualTo(new Object());
        assertThat(LargeInteger.of("562384864853845273459308085003"))
                .isNotEqualTo(LargeInteger.of("92584759238457857923586795605727502"));
        assertThat(LargeInteger.of("562384864853845273459308085003"))
                .isNotEqualTo(new BigInteger("562384864853845273459308085003"));
        assertThat(LargeInteger.of("562384864853845273459308085003"))
                .isNotEqualTo(new Object());
    }

    @Test
    void testToString() {
        assertThat(LargeInteger.ZERO).hasToString("0");
        assertThat(LargeInteger.ONE).hasToString("1");
        assertThat(LargeInteger.NEGATIVE_ONE).hasToString("-1");
        assertThat(LargeInteger.TWO).hasToString("2");
        assertThat(LargeInteger.TEN).hasToString("10");
        assertThat(LargeInteger.of(-325)).hasToString("-325");
        assertThat(LargeInteger.of(4143534)).hasToString("4143534");
        assertThat(LargeInteger.of(-4143534)).hasToString("-4143534");
        assertThat(LargeInteger.of(Long.MAX_VALUE)).hasToString(Long.toString(Long.MAX_VALUE));
        assertThat(LargeInteger.of(Long.MIN_VALUE)).hasToString(Long.toString(Long.MIN_VALUE));
        assertThat(LargeInteger.of("3246723045291384725909688254217")).hasToString("3246723045291384725909688254217");
        assertThat(LargeInteger.of("-3246723045291384725909688254217")).hasToString("-3246723045291384725909688254217");
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/toStringRadix-cases.csv", numLinesToSkip = 1)
    void testToStringRadix(LargeInteger n, int toRadix, String stringValue) {
        assertThat(n.toString(toRadix)).as("%s --> (%d)", n, toRadix).isEqualToIgnoringCase(stringValue);
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

    @Test
    void testCached() {
        assertThat(LargeInteger.ONE.cached()).isSameAs(LargeInteger.ONE);
        assertThat(LargeInteger.ZERO.add(LargeInteger.ZERO).cached()).isSameAs(LargeInteger.ZERO);
        assertThat(LargeInteger.ZERO.add(LargeInteger.ONE).cached()).isSameAs(LargeInteger.ONE);
        assertThat(LargeInteger.of(7).add(LargeInteger.of(15)).cached()).isSameAs(LargeInteger.of(22));
        assertThat(LargeInteger.of(5).subtract(LargeInteger.of(12)).cached()).isSameAs(LargeInteger.of(-7));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/toBitSet-cases.csv", numLinesToSkip = 1)
    void testToBitSet(LargeInteger n, String bitSetBytesHex) {
        assertThat(n.toBitSet()).as("%s.toBitSet()", n).isEqualTo(BitSet.valueOf(convertHexToBytes(bitSetBytesHex)));
    }

    @ParameterizedTest
    @CsvFileSource(resources = CASE_DATA_DIR + "/isFittingIn-cases.csv", numLinesToSkip = 1)
    void testIsFittingIn(
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
    @CsvFileSource(resources = CASE_DATA_DIR + "/binaryBitwise-cases.csv", numLinesToSkip = 1)
    void testBinaryBitwise(
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

    @ParameterizedTest
    @ValueSource(strings = { "0", "-1", "-13", "-42543", "-5764763287450682343455473450862" })
    void testRandomThrow(String value) {
        Random random = new Random(999L);
        LargeInteger high = LargeInteger.of(value);
        assertThatThrownBy(() -> high.random(random)).isInstanceOf(ArithmeticException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = { "1", "19", "42673489345", "7941203427346852946087460936" })
    void testRandom(String value) {
        Random random = new Random(999L);
        LargeInteger high = LargeInteger.of(value);
        assertThat(ImmutableList.fill(1000, i -> high.random(random)))
                .allMatch(LargeInteger::isNonNegative)
                .allMatch(r -> r.isLessThan(high));
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
