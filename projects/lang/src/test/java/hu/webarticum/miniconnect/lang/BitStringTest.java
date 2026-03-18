package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BitStringTest {

    private static BitString BITS_160 = BitString.of(
            "1101001111001000101011010111010100101011101101011101010110101111" +
            "0110011101011011010001001101011101000101011101011001111000101010" +
            "01110001010110011110001101101110");

    @Test
    void testOfEmptyAndData() {
        assertThat(BitString.empty().data()).isEmpty();
    }

    @Test
    void testOfBitsAndData() {
        assertThat(BitString.of(new boolean[0]).data()).isEmpty();
        assertThat(BitString.of(false).data()).containsExactly(0);
        assertThat(BitString.of(true, false, true).data()).containsExactly(-6917529027641081856L);
        assertThat(BitString.of(false, true, true).data()).containsExactly(6917529027641081856L);
        assertThat(BitString.of(true, true, true, true, true).data()).containsExactly(-576460752303423488L);
        assertThat(BitString.of(true, false, true, false, false, true, true, false).data()).containsExactly(-6485183463413514240L);
        assertThat(BitString.of(new boolean[64]).data()).containsExactly(0);
        assertThat(BitString.of(new boolean[65]).data()).containsExactly(0, 0);
    }

    @Test
    void testOfBytesAndData() {
        assertThat(BitString.of(new byte[0]).data()).isEmpty();
        assertThat(BitString.of(new byte[1]).data()).containsExactly(0);
        assertThat(BitString.of(new byte[3]).data()).containsExactly(0);
        assertThat(BitString.of(new byte[8]).data()).containsExactly(0);
        assertThat(BitString.of(new byte[9]).data()).containsExactly(0, 0);
        assertThat(BitString.of(new byte[] { 1 }).data()).containsExactly(72057594037927936L);
        assertThat(BitString.of(new byte[] { -41, 117 }).data()).containsExactly(-2921428783279898624L);
        assertThat(BitString.of(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }).data())
                .containsExactly(283686952306183L, 578994027093819392L);
        assertThat(BitString.of(new byte[] {
                0, 4, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 9, 0, 0, 0, 0,
                0, 0, 7, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 8, 0, 0, 0,
                1, 0, 0, 0, 0, 0, 0, 1,
                0, 2, 0,
        }).data()).containsExactly(
                1125899906842624L, 38654705664L, 7696581394432L,
                134217728L, 72057594037927937L, 562949953421312L);
    }

    @Test
    void testOfLongsAndData() {
        assertThat(BitString.of(new long[0]).data()).isEmpty();
        assertThat(BitString.of(new long[1]).data()).containsExactly(0);
        assertThat(BitString.of(new long[2]).data()).containsExactly(0, 0);
        assertThat(BitString.of(new long[3]).data()).containsExactly(0, 0, 0);
        assertThat(BitString.of(new long[] { 5274894190323240324L, -24234345L, 9202353453245L }).data())
                .containsExactly(5274894190323240324L, -24234345L, 9202353453245L);
    }

    @Test
    void testOfLongsWithLengthAndData() {
        assertThat(BitString.of(new long[0], 0).data()).isEmpty();
        assertThat(BitString.of(new long[0], 2).data()).containsExactly(0);
        assertThat(BitString.of(new long[2], 128).data()).containsExactly(0, 0);
        assertThat(BitString.of(new long[1], 140).data()).containsExactly(0, 0, 0);
        assertThat(BitString.of(new long[10], 65).data()).containsExactly(0, 0);
        assertThat(BitString.of(new long[] { 2432501L }, 64).data()).containsExactly(2432501);
        assertThat(BitString.of(new long[] { 7756231263636641610L }, 11).data()).containsExactly(7755198558331994112L);
        assertThat(BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L }, 36).data())
                .containsExactly(-5031317168688463872L);
        assertThat(BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L }, 77).data())
                .containsExactly(-5031317168539249322L, 3981182070595518464L);
        assertThat(BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L }, 193).data())
                .containsExactly(-5031317168539249322L, 3983041279643227983L, 0, 0);
    }

    @Test
    void testOfStringAndData() {
        assertThat(BitString.of("").data()).isEmpty();
        assertThat(BitString.of("0").data()).containsExactly(0);
        assertThat(BitString.of("000000").data()).containsExactly(0);
        assertThat(BitString.of("0000000000000000000000000000000000000000000000000000000000000000").data()).containsExactly(0);
        assertThat(BitString.of("00000000000000000000000000000000000000000000000000000000000000000").data()).containsExactly(0, 0);
        assertThat(BitString.of("001").data()).containsExactly(2305843009213693952L);
        assertThat(BitString.of("1").data()).containsExactly(-9223372036854775808L);
        assertThat(BitString.of("101").data()).containsExactly(-6917529027641081856L);
        assertThat(BitString.of("10010101110110100101111010101010101011101010100011010101110101010100110101").data())
                .containsExactly(-7648696929967614507L, 5566449139429933056L);
    }

    @Test
    void testOfStringIllegal() {
        assertThatThrownBy(() -> BitString.of("012010")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitString.of("99")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitString.of("-1011")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitString.of("0110 1001")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitString.of("10.11")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitString.of("10,11")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitString.of("\0")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitString.of("false")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitString.of("true")).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitString.of("lorem")).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testBuilder() {
        assertThat((Object) BitString.builder().build()).isEqualTo(BitString.empty());
        assertThat((Object) BitString.builder().append(false).build()).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.builder().append(true).build()).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.builder().append('0').build()).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.builder().append('1').build()).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.builder()
                .append(false).append(true).append(false).append(true).append(true).append(false)
                .build()).isEqualTo(BitString.of("010110"));
        assertThat((Object) BitString.builder()
                .append(false).append(true).append('0').append(false).append('1').append(false)
                .build()).isEqualTo(BitString.of("010010"));
        assertThat((Object) BitString.builder()
                .append(false).append(false).append(true).append(false).append(true).append(false).append(false).append(true)
                .append(true).append(true).append(false).append(true).append(false).append(true).append(false).append(false)
                .append(false).append(true).append(true).append(false).append(true).append(false).append(true).append(false)
                .append(false).append(true).append(true).append(true).append(false).append(true).append(false).append(true)
                .append(false).append(false).append(true).append(true).append(true).append(false).append(true).append(false)
                .append(true).append(false).append(false).append(false).append(true).append(true).append(true).append(false)
                .append(true).append(false).append(false).append(false).append(true).append(true).append(false).append(true)
                .append(false).append(true).append(true).append(true).append(false).append(true).append(false).append(true)
                .append(true).append(true).append(false).append(true).append(false).append(true).append(true).append(true)
                .append(false).append(true).append(false).append(true).append(false).append(false).append(false).append(true)
                .append(true).append(true).append(false).append(true).append(false).append(true).append(true)
                .build()).isEqualTo(BitString.of(
                "0010100111010100011010100111010100111010100011101000110101110101" +
                "11010111010100011101011"));
        assertThat((Object) BitString.builder().append(new boolean[] { true, true, false }).build()).isEqualTo(BitString.of("110"));
        assertThat((Object) BitString.builder().append("0").build()).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.builder().append("1").build()).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.builder().append("11011010").build()).isEqualTo(BitString.of("11011010"));
        assertThat((Object) BitString.builder().append(
                "1010011011110101000111010001010111010100011010101010011010101100" +
                "0110100011110101110101011010101011110101010111010100111010100011" +
                "101011101011101000").build())
                .isEqualTo(BitString.of(
                "1010011011110101000111010001010111010100011010101010011010101100" +
                "0110100011110101110101011010101011110101010111010100111010100011" +
                "101011101011101000"));
        assertThat((Object) BitString.builder()
                .append("1011010111010011010111010001110101110101110110101001110101110101")
                .append("01110001101010101100")
                .build()).isEqualTo(BitString.of(
                "1011010111010011010111010001110101110101110110101001110101110101" +
                "01110001101010101100"));
        assertThat((Object) BitString.builder()
                .append("1001").append("1").append("01101110").append("01100011").append("000")
                .build()).isEqualTo(BitString.of(
                "100110110111001100011000"));
        assertThat((Object) BitString.builder()
                .append("0010100001010111010110100010101111101010001101101010011101110111")
                .append("001110101110100110")
                .append("10001101000011011010")
                .append("10011010001110101001101001")
                .append("0011110101111010111101111101101110111011010001101001101011011101")
                .append("10010011110")
                .append("1001101011101011")
                .build()).isEqualTo(BitString.of(
                "0010100001010111010110100010101111101010001101101010011101110111" +
                "0011101011101001101000110100001101101010011010001110101001101001" +
                "0011110101111010111101111101101110111011010001101001101011011101" +
                "100100111101001101011101011"));
        assertThat((Object) BitString.builder()
                .append("1001110100111010111010110101010101110111010100111101011010101")
                .append("0000000000000000")
                .build()).isEqualTo(BitString.of(
                "1001110100111010111010110101010101110111010100111101011010101000" +
                "0000000000000"));
        assertThat((Object) BitString.builder()
                .append("01011101011110101011101010111101010111010101110101011")
                .append("11010111010100110101011101010")
                .build()).isEqualTo(BitString.of(
                "0101110101111010101110101011110101011101010111010101111010111010" +
                "100110101011101010"));
        assertThat((Object) BitString.builder()
                .append("100011101010101001010111101")
                .append("01011101011110101001")
                .append("10110010101101010011010100")
                .append("0110101010100110101010110111")
                .append("001011010110101011101010010")
                .append("0101011101011101011010011101011101010")
                .append("101011111010100011010001110101001101")
                .build()).isEqualTo(BitString.of(
                "1000111010101010010101111010101110101111010100110110010101101010" +
                "0110101000110101010100110101010110111001011010110101011101010010" +
                "0101011101011101011010011101011101010101011111010100011010001110" +
                "101001101"));
        assertThat((Object) BitString.builder()
                .append(
                        "1010111010111101010010101110101000110101110101111001010010101010" +
                        "11101010010101000101010101101010101100")
                .append(
                        "0001101111011101010001010111010100010110101010101101001011010110" +
                        "0110101101010001000101001100101011010100101011101010000101110101" +
                        "11010110101001001111010001010110101110101")
                .build()).isEqualTo(BitString.of(
                "1010111010111101010010101110101000110101110101111001010010101010" +
                "1110101001010100010101010110101010110000011011110111010100010101" +
                "1101010001011010101010110100101101011001101011010100010001010011" +
                "0010101101010010101110101000010111010111010110101001001111010001" +
                "010110101110101"));
        assertThat((Object) BitString.builder().append(BitString.empty()).build()).isEqualTo(BitString.empty());
        assertThat((Object) BitString.builder().append(BitString.of("10110")).build()).isEqualTo(BitString.of("10110"));
        assertThat((Object) BitString.builder()
                .append("1011101011")
                .append('0')
                .append(BitString.of("000001100"))
                .append(true)
                .append(false)
                .append('1')
                .append('1')
                .append(BitString.of("111010001001101011100"))
                .append("100000110101110101000001")
                .append(
                        "1100100010101110101011110101011101010111101010101010111010110100" +
                        "1")
                .append(false)
                .append(new boolean[] { true, false, true, true, false })
                .build()).isEqualTo(BitString.of(
                "1011101011000000110010111110100010011010111001000001101011101010" +
                "0000111001000101011101010111101010111010101111010101010101110101" +
                "101001010110"));
    }

    @Test
    void testLength() {
        assertThat(BitString.empty().length()).isZero();
        assertThat(BitString.of(new boolean[0]).length()).isZero();
        assertThat(BitString.of(false).length()).isOne();
        assertThat(BitString.of(false).length()).isOne();
        assertThat(BitString.of(true).length()).isOne();
        assertThat(BitString.of(true, false).length()).isEqualTo(2);
        assertThat(BitString.of(new boolean[65]).length()).isEqualTo(65);
        assertThat(BitString.of(new byte[0]).length()).isZero();
        assertThat(BitString.of(new byte[3]).length()).isEqualTo(24);
        assertThat(BitString.of(new byte[20]).length()).isEqualTo(160);
        assertThat(BitString.of(new long[0]).length()).isZero();
        assertThat(BitString.of(new long[2]).length()).isEqualTo(128);
        assertThat(BitString.of(new long[0], 0).length()).isZero();
        assertThat(BitString.of(new long[0], 3).length()).isEqualTo(3);
        assertThat(BitString.of(new long[0], 173).length()).isEqualTo(173);
        assertThat(BitString.of("000000000").length()).isEqualTo(9);
        assertThat(BitString.of(
                "11000100011010101111101010100001010111010101010010110001010101110000101011")
                .length()).isEqualTo(74);
    }

    @Test
    void testIsEmpty() {
        assertThat(BitString.empty().isEmpty()).isTrue();
        assertThat(BitString.of(false).isEmpty()).isFalse();
        assertThat(BitString.of(true).isEmpty()).isFalse();
        assertThat(BitString.of(new long[] { 4160867386445L }, 0).isEmpty()).isTrue();
        assertThat(BitString.of(new long[] { 4160867386445L }, 10).isEmpty()).isFalse();
        assertThat(BitString.of("00111100011010110").isEmpty()).isFalse();
    }

    @Test
    void testCompareTo() {
        assertThat(BitString.empty().compareTo(BitString.empty())).isZero();
        assertThat(BitString.empty().compareTo(BitString.of(false))).isNegative();
        assertThat(BitString.of(false).compareTo(BitString.empty())).isPositive();
        assertThat(BitString.of(true).compareTo(BitString.of(true))).isZero();
        assertThat(BitString.of(true).compareTo(BitString.of(true, false))).isNegative();
        assertThat(BitString.of(true, false).compareTo(BitString.of(true))).isPositive();
        assertThat(BitString.of(true, false).compareTo(BitString.of(true, false))).isZero();
        assertThat(BitString.of(new long[2]).compareTo(BitString.of(new long[5]))).isNegative();
        assertThat(BitString.of(new long[4]).compareTo(BitString.of(new long[1]))).isPositive();
        assertThat(BitString.of(new long[] { 7, 3 }).compareTo(BitString.of(new long[] { 7, 3 }))).isZero();
        assertThat(BitString.of(new long[] { 7, 3, 0 }).compareTo(BitString.of(new long[] { 7, 3, 0 }))).isZero();
        assertThat(BitString.of(new long[] { 7, 3, 0 }).compareTo(BitString.of(new long[] { 7, 3, 0, 0, 0 }))).isNegative();
        assertThat(BitString.of(new long[] { 7, 3, 0 }).compareTo(BitString.of(new long[] { 7, 3 }))).isPositive();
        assertThat(BitString.empty().compareTo(BitString.of(true))).isNegative();
        assertThat(BitString.of(true).compareTo(BitString.empty())).isPositive();
        assertThat(BitString.empty().compareTo(BitString.of(new long[] { 7, 3, 0 }))).isNegative();
        assertThat(BitString.of(new long[] { 7, 3, 0 }).compareTo(BitString.empty())).isPositive();
        assertThat(BitString.of(new long[] { 7, 3, 1 }).compareTo(BitString.of(new long[] { 7, 3, 3 }))).isNegative();
        assertThat(BitString.of(new long[] { 7, 3, 3 }).compareTo(BitString.of(new long[] { 7, 3, 1 }))).isPositive();
        assertThat(BitString.of(new long[] { 7, 3, 1 }).compareTo(BitString.of(new long[] { 7, 3, 2 }))).isNegative();
        assertThat(BitString.of(new long[] { 7, 3, 2 }).compareTo(BitString.of(new long[] { 7, 3, 1 }))).isPositive();
        assertThat(BitString.of(new long[] { 7, 3 }).compareTo(BitString.of(new long[] { 7, 3, 0, 5 }))).isNegative();
        assertThat(BitString.of(new long[] { 7, 3, 0, 5 }).compareTo(BitString.of(new long[] { 7, 3 }))).isPositive();
        assertThat(BitString.of("100000101").compareTo(BitString.of("100000101"))).isZero();
        assertThat(BitString.of("100000101").compareTo(BitString.of("1000001010"))).isNegative();
        assertThat(BitString.of("1000001010").compareTo(BitString.of("100000101"))).isPositive();
        assertThat(BitString.of("100110010").compareTo(BitString.of("100111010"))).isNegative();
        assertThat(BitString.of("100111010").compareTo(BitString.of("100110010"))).isPositive();
    }

    @Test
    void testIterator() {
        assertThat(BitString.empty().iterator()).isExhausted();
        assertThat((Iterable<Boolean>) BitString.of(false)).containsExactly(false);
        assertThat((Iterable<Boolean>) BitString.of(true, false)).containsExactly(true, false);
        assertThat((Iterable<Boolean>) BitString.of(new boolean[65])).hasSize(65).containsOnly(false);
        assertThat((Iterable<Boolean>) BitString.of(new long[] { -7493989779944505344L }, 5)).containsExactly(
                true, false, false, true, true);
        assertThat((Iterable<Boolean>) BitString.of("110100111010")).containsExactly(
                true, true, false, true, false, false, true, true, true, false, true, false);
        assertThat((Iterable<Boolean>) BitString.of("0011101101011000")).containsExactly(
                false, false, true, true, true, false, true, true,
                false, true, false, true, true, false, false, false);
    }

    @Test
    void testHashCode() {
        assertThat((Object) BitString.of(false)).hasSameHashCodeAs(BitString.of("0"));
        assertThat((Object) BitString.of(true)).hasSameHashCodeAs(BitString.of("1"));
        assertThat((Object) BitString.of("11010001101011010100")).hasSameHashCodeAs(BitString.of("11010001101011010100"));
    }

    @Test
    void testEquals() {
        assertThat((Object) BitString.of(false)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of(true)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("11010001101011010100")).isEqualTo(BitString.of("11010001101011010100"));
    }

    @Test
    void testEqualsFalse() {
        assertThat((Object) BitString.empty()).isNotEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of(false)).isNotEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of(true)).isNotEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("11010001101011010100")).isNotEqualTo(BitString.of("10110101001110011001"));
        assertThat((Object) BitString.of(new long[1])).isNotEqualTo(BitString.of(new long[2]));
    }

    @Test
    void testToString() {
        assertThat((Object) BitString.empty()).hasToString("");
        assertThat((Object) BitString.of(false)).hasToString("0");
        assertThat((Object) BitString.of(true)).hasToString("1");
        assertThat((Object) BitString.of(false, true, true, false, false, true)).hasToString("011001");
        assertThat((Object) BitString.of(new long[] { -3248317512688145487L, 4245054622059724800L }, 92)).hasToString(
                "11010010111010111010100001101110101110101000101011101011101100010011101011101001011101101011");
        assertThat((Object) BitString.of(new long[] { -3248317512688145487L, 4245054622059724800L }, 100)).hasToString(
                "1101001011101011101010000110111010111010100010101110101110110001001110101110100101110110101100000000");
        assertThat((Object) BitString.of("100010111011")).hasToString("100010111011");
    }

    @Test
    void testHasZerosOnly() {
        assertThat(BitString.empty().hasZerosOnly()).isTrue();
        assertThat(BitString.of("0").hasZerosOnly()).isTrue();
        assertThat(BitString.of("00").hasZerosOnly()).isTrue();
        assertThat(BitString.of("00000").hasZerosOnly()).isTrue();
        assertThat(BitString.of("00000000").hasZerosOnly()).isTrue();
        assertThat(BitString.of("00000000000000000").hasZerosOnly()).isTrue();
        assertThat(BitString.of("1").hasZerosOnly()).isFalse();
        assertThat(BitString.of("01").hasZerosOnly()).isFalse();
        assertThat(BitString.of("10").hasZerosOnly()).isFalse();
        assertThat(BitString.of("01101").hasZerosOnly()).isFalse();
        assertThat(BitString.of("11010110").hasZerosOnly()).isFalse();
        assertThat(BitString.of("11111111").hasZerosOnly()).isFalse();
        assertThat(BitString.of("100011110101").hasZerosOnly()).isFalse();
        assertThat(BitString.of("00000000101").hasZerosOnly()).isFalse();
        assertThat(BitString.of("1000000000000").hasZerosOnly()).isFalse();
    }

    @Test
    void testHasOnesOnly() {
        assertThat(BitString.empty().hasOnesOnly()).isTrue();
        assertThat(BitString.of("1").hasOnesOnly()).isTrue();
        assertThat(BitString.of("11").hasOnesOnly()).isTrue();
        assertThat(BitString.of("11111").hasOnesOnly()).isTrue();
        assertThat(BitString.of("11111111").hasOnesOnly()).isTrue();
        assertThat(BitString.of("11111111111111111").hasOnesOnly()).isTrue();
        assertThat(BitString.of("0").hasOnesOnly()).isFalse();
        assertThat(BitString.of("10").hasOnesOnly()).isFalse();
        assertThat(BitString.of("01").hasOnesOnly()).isFalse();
        assertThat(BitString.of("01101").hasOnesOnly()).isFalse();
        assertThat(BitString.of("11010110").hasOnesOnly()).isFalse();
        assertThat(BitString.of("00000000").hasOnesOnly()).isFalse();
        assertThat(BitString.of("11111111000").hasOnesOnly()).isFalse();
        assertThat(BitString.of("111111110001").hasOnesOnly()).isFalse();
        assertThat(BitString.of("00000000111111").hasOnesOnly()).isFalse();
        assertThat(BitString.of("00010000111111").hasOnesOnly()).isFalse();
    }

    @Test
    void testGet() {
        assertThat(BitString.of("0").get(0)).isFalse();
        assertThat(BitString.of("1").get(0)).isTrue();
        assertThat(BitString.of("10110100").get(5)).isTrue();
        assertThat(BitString.of(
                "100101101100010011101000101100101001001100011110100111101000111100111010")
                .get(68)).isTrue();
        assertThat(BitString.of(new long[] { -3248317512688145487L, 4245054622059724800L }, 92).get(17)).isFalse();
        assertThat(BitString.of(new long[] { -3248317512688145487L, 4245054622059724800L }, 92).get(67)).isTrue();
        assertThat(BitString.empty().get(-1)).isFalse();
        assertThat(BitString.empty().get(0)).isFalse();
        assertThat(BitString.of("1").get(1)).isFalse();
        assertThat(BitString.of(new long[2]).get(130)).isFalse();
    }

    @Test
    void testGetStrict() {
        assertThat(BitString.of("0").getStrict(0)).isFalse();
        assertThat(BitString.of("1").getStrict(0)).isTrue();
        assertThat(BitString.of("10110100").getStrict(5)).isTrue();
        assertThat(BitString.of(
                "100101101100010011101000101100101001001100011110100111101000111100111010")
                .getStrict(68)).isTrue();
        assertThat(BitString.of(new long[] { -3248317512688145487L, 4245054622059724800L }, 92).getStrict(17)).isFalse();
        assertThat(BitString.of(new long[] { -3248317512688145487L, 4245054622059724800L }, 92).getStrict(67)).isTrue();
    }

    @Test
    void testGetStrictOutOfBounds() {
        assertThatThrownBy(() -> BitString.empty().getStrict(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.empty().getStrict(0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.of(true).getStrict(1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.of(new long[2]).getStrict(130)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testSet() {
        assertThat((Object) BitString.of("0").set(0, true)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("0").set(0, false)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("1").set(0, false)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("1").set(0, true)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("0").set(2, true)).isEqualTo(BitString.of("001"));
        assertThat((Object) BitString.of("1").set(5, false)).isEqualTo(BitString.of("100000"));
        assertThat((Object) BitString.of("10110100").set(3, true)).isEqualTo(BitString.of("10110100"));
        assertThat((Object) BitString.of("10110100").set(3, false)).isEqualTo(BitString.of("10100100"));
        assertThat((Object) BitString.of("10110100").set(11, true)).isEqualTo(BitString.of("101101000001"));
        assertThat((Object) BitString.of("10110100").set(72, true)).isEqualTo(BitString.of(
                "1011010000000000000000000000000000000000000000000000000000000000" +
                "000000001"));
        assertThat((Object) BitString.of("10110100").set(128, true)).isEqualTo(BitString.of(
                "1011010000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1"));
        assertThat((Object) BitString.of("10110100").set(192, false)).isEqualTo(BitString.of(
                "1011010000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0"));
        assertThat((Object) BitString.of("10110100").set(204, true)).isEqualTo(BitString.of(
                "1011010000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000001"));
    }

    @Test
    void testSetNegative() {
        assertThatThrownBy(() -> BitString.empty().set(-1, false)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testFlipStrict() {
        assertThat((Object) BitString.of("0").flipStrict(0)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("1").flipStrict(0)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("10110100").flipStrict(3)).isEqualTo(BitString.of("10100100"));
        assertThat((Object) BitString.of("10110100").flipStrict(4)).isEqualTo(BitString.of("10111100"));
    }

    @Test
    void testFlipStrictOutOfBounds() {
        assertThatThrownBy(() -> BitString.empty().flipStrict(-1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.empty().flipStrict(0)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.empty().flipStrict(1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.of(new long[1]).flipStrict(71)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.of(new long[1], 3).flipStrict(22)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testNot() {
        assertThat((Object) BitString.empty().not()).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("0").not()).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("1").not()).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("001011010").not()).isEqualTo(BitString.of("110100101"));
        assertThat((Object) BitString.of("11010000000000").not()).isEqualTo(BitString.of("00101111111111"));
        assertThat((Object) BitString.of("1001010001101010000101010111011000011100001111010111010010011011001101").not())
                .isEqualTo(BitString.of("0110101110010101111010101000100111100011110000101000101101100100110010"));
    }

    @Test
    void testAnd() {
        assertThat((Object) BitString.empty().and(BitString.empty())).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("1010").and(BitString.empty())).isEqualTo(BitString.of("0000"));
        assertThat((Object) BitString.empty().and(BitString.of("1100"))).isEqualTo(BitString.of("0000"));
        assertThat((Object) BitString.of("1010").and(BitString.of("1100"))).isEqualTo(BitString.of("1000"));
        assertThat((Object) BitString.of(
                "00110111011010111000110000101011010110000101010111010010000011110010011100011110").and(BitString.of(
                "010111010101110001010111010111000110101101110101011110101"))).isEqualTo(BitString.of(
                "00010101010010000000010000001000010010000101010101010010000000000000000000000000"));
    }

    @Test
    void testOr() {
        assertThat((Object) BitString.empty().or(BitString.empty())).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("1010").or(BitString.empty())).isEqualTo(BitString.of("1010"));
        assertThat((Object) BitString.empty().or(BitString.of("1100"))).isEqualTo(BitString.of("1100"));
        assertThat((Object) BitString.of("1010").or(BitString.of("1100"))).isEqualTo(BitString.of("1110"));
        assertThat((Object) BitString.of(
                "00110111011010111000110000101011010110000101010111010010000011110010011100011110").or(BitString.of(
                "010111010101110001010111010111000110101101110101011110101"))).isEqualTo(BitString.of(
                "01111111011111111101111101111111011110110111010111111010100011110010011100011110"));
    }

    @Test
    void testXor() {
        assertThat((Object) BitString.empty().xor(BitString.empty())).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("1010").xor(BitString.empty())).isEqualTo(BitString.of("1010"));
        assertThat((Object) BitString.empty().xor(BitString.of("1100"))).isEqualTo(BitString.of("1100"));
        assertThat((Object) BitString.of("1010").xor(BitString.of("1100"))).isEqualTo(BitString.of("0110"));
        assertThat((Object) BitString.of(
                "00110111011010111000110000101011010110000101010111010010000011110010011100011110").xor(BitString.of(
                "010111010101110001010111010111000110101101110101011110101"))).isEqualTo(BitString.of(
                "01101010001101111101101101110111001100110010000010101000100011110010011100011110"));
    }

    @Test
    void testConcat() {
        assertThat((Object) BitString.empty().concat(BitString.empty())).isEqualTo(BitString.empty());
        assertThat((Object) BitString.empty().concat(BitString.of("0"))).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("0").concat(BitString.empty())).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.empty().concat(BitString.of("1"))).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("1").concat(BitString.empty())).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("1").concat(BitString.of("0"))).isEqualTo(BitString.of("10"));
        assertThat((Object) BitString.of("0").concat(BitString.of("1"))).isEqualTo(BitString.of("01"));
        assertThat((Object) BitString.of("1001110").concat(BitString.of("001110"))).isEqualTo(BitString.of("1001110001110"));
        assertThat((Object) BitString.of("01100010101101011010101111010")
                .concat(BitString.of("00011001110101000110101100101011001")))
                .isEqualTo(BitString.of("0110001010110101101010111101000011001110101000110101100101011001"));
        assertThat((Object) BitString.of("10110001010110011011011101011")
                .concat(BitString.of("0110100001100111010100011011010101100101011001")))
                .isEqualTo(BitString.of("101100010101100110110111010110110100001100111010100011011010101100101011001"));
        assertThat((Object) BitString.of("10011001001001100101101010101011")
                .concat(BitString.of("01011010110110101010110101010001010110111010101110100001101000110100011010110")))
                .isEqualTo(BitString.of(
                        "1001100100100110010110101010101101011010110110101010110101010001" +
                        "010110111010101110100001101000110100011010110"));
        assertThat((Object) BitString.of(
                        "101001110111110110010110100010110111010100001101011100")
                .concat(BitString.of(
                        "001110101101011110101010000101011010001010110101000010101110101010111010101000110100011010101100")))
                .isEqualTo(BitString.of(
                        "1010011101111101100101101000101101110101000011010111000011101011" +
                        "0101111010101000010101101000101011010100001010111010101011101010" +
                        "1000110100011010101100"));
        assertThat((Object) BitString.of(
                        "0011010001100111011101010101000101010111010101010111010101000111" +
                        "1010001110001010101011101010001010011101000010101101010111010101" +
                        "0010100010100011")
                .concat(BitString.of(
                        "0100110101000011101010001011101110101010110001011010010101011101" +
                        "10101011110101110011100")))
                .isEqualTo(BitString.of(
                        "0011010001100111011101010101000101010111010101010111010101000111" +
                        "1010001110001010101011101010001010011101000010101101010111010101" +
                        "0010100010100011010011010100001110101000101110111010101011000101" +
                        "101001010101110110101011110101110011100"));
        assertThat((Object) BitString.of(
                        "0110101101101000110101110101001010111010100010101011010010010101" +
                        "1010001010110100010101101001010111001001010010110101001111010100" +
                        "01101000101011100101010011010101111010001")
                .concat(BitString.of(
                        "1001110101011110000110011101011110101010010010100011010100011010" +
                        "0010101000110010111100011100011001111010101001011010100101010110" +
                        "010110101110101111000010110111000111000111110")))
                .isEqualTo(BitString.of(
                        "0110101101101000110101110101001010111010100010101011010010010101" +
                        "1010001010110100010101101001010111001001010010110101001111010100" +
                        "0110100010101110010101001101010111101000110011101010111100001100" +
                        "1110101111010101001001010001101010001101000101010001100101111000" +
                        "1110001100111101010100101101010010101011001011010111010111100001" +
                        "0110111000111000111110"));
    }

    @Test
    void testSubstring() {
        assertThat((Object) BitString.empty().substring(0, 0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("10").substring(0, 0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("10").substring(1, 1)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("10").substring(2, 2)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("10").substring(0, 1)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("10").substring(1, 2)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("10").substring(0, 2)).isEqualTo(BitString.of("10"));
        assertThat((Object) BitString.of("1011001000").substring(2, 9)).isEqualTo(BitString.of("1100100"));
        assertThat((Object) BitString.of(
                "1010101001100101001001001110101001110011010111000110011001010011" +
                "101010111110101010"
            ).substring(54, 73)).isEqualTo(BitString.of(
                "1001010011101010111"));
        assertThat((Object) BitString.of(
                "1101010101110101011110001010111010100011001010100010100111010100" +
                "0100110101000101011001001101100101011101010101100011010101101011" +
                "0101011110111000111010"
            ).substring(64, 135)).isEqualTo(BitString.of(
                "0100110101000101011001001101100101011101010101100011010101101011" +
                "0101011"));
        BitString fourAndHalf = BitString.of(
                "1010011101010100010101101001101101010001101011101010101010101011" +
                "0101010110101110010011001101011000101011010101010101001110001100" +
                "0111010101110101011010101010101101111010010010001010101101010100" +
                "1010001101110000110101010100111010100010101000101101011101010101" +
                "10101011101011110101010101010011");
        assertThat((Object) fourAndHalf.substring(42, 170)).isEqualTo(BitString.of(
                "1011101010101010101011010101011010111001001100110101100010101101" +
                "0101010101001110001100011101010111010101101010101010110111101001"));
        assertThat((Object) fourAndHalf.substring(42, 181)).isEqualTo(BitString.of(
                "1011101010101010101011010101011010111001001100110101100010101101" +
                "0101010101001110001100011101010111010101101010101010110111101001" +
                "00100010101"));
        assertThat((Object) fourAndHalf.substring(42, 192)).isEqualTo(BitString.of(
                "1011101010101010101011010101011010111001001100110101100010101101" +
                "0101010101001110001100011101010111010101101010101010110111101001" +
                "0010001010101101010100"));
        assertThat((Object) fourAndHalf.substring(42, 204)).isEqualTo(BitString.of(
                "1011101010101010101011010101011010111001001100110101100010101101" +
                "0101010101001110001100011101010111010101101010101010110111101001" +
                "0010001010101101010100101000110111"));
    }

    @Test
    void testSubstringIllegal() {
        BitString bitString = BitString.of("0110");
        assertThatThrownBy(() -> bitString.substring(-1, 3)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> bitString.substring(2, 7)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> bitString.substring(-1, 7)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> bitString.substring(-1, -1)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> bitString.substring(7, 7)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> bitString.substring(3, 2)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> bitString.substring(3, -1)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> bitString.substring(7, 2)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> BitString.empty().substring(0, 1)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testWindowFullyOutside() {
        assertThat((Object) BitString.empty().window(0, 0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.empty().window(-3, 0)).isEqualTo(BitString.of("000"));
        assertThat((Object) BitString.empty().window(-64, 0)).isEqualTo(BitString.of(new long[1]));
        assertThat((Object) BitString.empty().window(-256, -128)).isEqualTo(BitString.of(new long[2]));
        assertThat((Object) BitString.empty().window(-256, -120)).isEqualTo(BitString.of(new long[3], 136));
        assertThat((Object) BitString.empty().window(50, 60)).isEqualTo(BitString.of("0000000000"));
        assertThat((Object) BitString.of("10011").window(0, 0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("10011").window(-3, 0)).isEqualTo(BitString.of("000"));
        assertThat((Object) BitString.of("10011").window(-64, 0)).isEqualTo(BitString.of(new long[1]));
        assertThat((Object) BitString.of("10011").window(-256, -128)).isEqualTo(BitString.of(new long[2]));
        assertThat((Object) BitString.of("10011").window(-256, -120)).isEqualTo(BitString.of(new long[3], 136));
        assertThat((Object) BitString.of("10011").window(50, 60)).isEqualTo(BitString.of("0000000000"));
    }

    @Test
    void testWindowUnshiftedFromNegative() {
        assertThat((Object) BitString.empty().window(-64, 3)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "000"));
        assertThat((Object) BitString.of("10011").window(-64, 3)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "100"));
        assertThat((Object) BitString.of("10011").window(-64, 14)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "10011000000000"));
        assertThat((Object) BitString.of("10011").window(-64, 71)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1001100000000000000000000000000000000000000000000000000000000000" +
                "0000000"));
        assertThat((Object) BITS_160.window(-64, 43)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1101001111001000101011010111010100101011101"));
        assertThat((Object) BITS_160.window(-64, 64)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1101001111001000101011010111010100101011101101011101010110101111"));
        assertThat((Object) BITS_160.window(-64, 72)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "01100111"));
        assertThat((Object) BITS_160.window(-64, 128)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010"));
        assertThat((Object) BITS_160.window(-64, 140)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "011100010101"));
        assertThat((Object) BITS_160.window(-64, 160)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "01110001010110011110001101101110"));
        assertThat((Object) BITS_160.window(-64, 179)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "011100010101100111100011011011100000000000000000000"));
        assertThat((Object) BITS_160.window(-64, 192)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "0111000101011001111000110110111000000000000000000000000000000000"));
        assertThat((Object) BITS_160.window(-64, 218)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "0111000101011001111000110110111000000000000000000000000000000000" +
                "00000000000000000000000000"));
    }

    @Test
    void testWindowUnshiftedFromZero() {
        assertThat((Object) BitString.of("10011").window(0, 3)).isEqualTo(BitString.of("100"));
        assertThat((Object) BitString.of("10011").window(0, 14)).isEqualTo(BitString.of("10011000000000"));
        assertThat((Object) BitString.of("10011").window(0, 71)).isEqualTo(BitString.of(
                "1001100000000000000000000000000000000000000000000000000000000000" +
                "0000000"));
        assertThat((Object) BITS_160.window(0, 43)).isEqualTo(BitString.of(
                "1101001111001000101011010111010100101011101"));
        assertThat((Object) BITS_160.window(0, 64)).isEqualTo(BitString.of(
                "1101001111001000101011010111010100101011101101011101010110101111"));
        assertThat((Object) BITS_160.window(0, 72)).isEqualTo(BitString.of(
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "01100111"));
        assertThat((Object) BITS_160.window(0, 128)).isEqualTo(BitString.of(
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010"));
        assertThat((Object) BITS_160.window(0, 140)).isEqualTo(BitString.of(
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "011100010101"));
        assertThat((Object) BITS_160.window(0, 160)).isEqualTo(BitString.of(
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "01110001010110011110001101101110"));
        assertThat((Object) BITS_160.window(0, 179)).isEqualTo(BitString.of(
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "011100010101100111100011011011100000000000000000000"));
        assertThat((Object) BITS_160.window(0, 192)).isEqualTo(BitString.of(
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "0111000101011001111000110110111000000000000000000000000000000000"));
        assertThat((Object) BITS_160.window(0, 218)).isEqualTo(BitString.of(
                "1101001111001000101011010111010100101011101101011101010110101111" +
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "0111000101011001111000110110111000000000000000000000000000000000" +
                "00000000000000000000000000"));
    }

    @Test
    void testWindowUnshiftedFromInner() {
        assertThat((Object) BITS_160.window(64, 64)).isEqualTo(BitString.empty());
        assertThat((Object) BITS_160.window(64, 72)).isEqualTo(BitString.of("01100111"));
        assertThat((Object) BITS_160.window(64, 128)).isEqualTo(BitString.of(
                "0110011101011011010001001101011101000101011101011001111000101010"));
        assertThat((Object) BITS_160.window(64, 140)).isEqualTo(BitString.of(
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "011100010101"));
        assertThat((Object) BITS_160.window(64, 160)).isEqualTo(BitString.of(
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "01110001010110011110001101101110"));
        assertThat((Object) BITS_160.window(64, 179)).isEqualTo(BitString.of(
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "011100010101100111100011011011100000000000000000000"));
        assertThat((Object) BITS_160.window(64, 192)).isEqualTo(BitString.of(
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "0111000101011001111000110110111000000000000000000000000000000000"));
        assertThat((Object) BITS_160.window(64, 218)).isEqualTo(BitString.of(
                "0110011101011011010001001101011101000101011101011001111000101010" +
                "0111000101011001111000110110111000000000000000000000000000000000" +
                "00000000000000000000000000"));
    }

    @Test
    void testWindowUnshiftedFromTail() {
        assertThat((Object) BITS_160.window(128, 128)).isEqualTo(BitString.empty());
        assertThat((Object) BITS_160.window(128, 140)).isEqualTo(BitString.of("011100010101"));
        assertThat((Object) BITS_160.window(128, 160)).isEqualTo(BitString.of(
                "01110001010110011110001101101110"));
        assertThat((Object) BITS_160.window(128, 179)).isEqualTo(BitString.of(
                "011100010101100111100011011011100000000000000000000"));
        assertThat((Object) BITS_160.window(128, 192)).isEqualTo(BitString.of(
                "0111000101011001111000110110111000000000000000000000000000000000"));
        assertThat((Object) BITS_160.window(128, 218)).isEqualTo(BitString.of(
                "0111000101011001111000110110111000000000000000000000000000000000" +
                "00000000000000000000000000"));
    }

    @Test
    void testWindowShiftedFromNegativeFar() {
        assertThat((Object) BitString.empty().window(-75, 3)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000"));
        assertThat((Object) BitString.of("10011").window(-75, 3)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000100"));
        assertThat((Object) BitString.of("10011").window(-75, 14)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000010011000000000"));
        assertThat((Object) BitString.of("10011").window(-75, 53)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000010011000000000000000000000000000000000000000000000000"));
        assertThat((Object) BitString.of("10011").window(-75, 71)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000010011000000000000000000000000000000000000000000000000" +
                "000000000000000000"));
        assertThat((Object) BitString.of("10001010011101001000001001000101100001110010110110001").window(-75, 53)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000010001010011101001000001001000101100001110010110110001"));
        assertThat((Object) BITS_160.window(-75, 14)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010"));
        assertThat((Object) BITS_160.window(-75, 53)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010"));
        assertThat((Object) BITS_160.window(-75, 59)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "101101"));
        assertThat((Object) BITS_160.window(-75, 64)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "10110101111"));
        assertThat((Object) BITS_160.window(-75, 100)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "10110101111011001110101101101000100110101110100"));
        assertThat((Object) BITS_160.window(-75, 128)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "1011010111101100111010110110100010011010111010001010111010110011" +
                "11000101010"));
        assertThat((Object) BITS_160.window(-75, 151)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "1011010111101100111010110110100010011010111010001010111010110011" +
                "1100010101001110001010110011110001"));
        assertThat((Object) BITS_160.window(-75, 160)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "1011010111101100111010110110100010011010111010001010111010110011" +
                "1100010101001110001010110011110001101101110"));
        assertThat((Object) BITS_160.window(-75, 168)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "1011010111101100111010110110100010011010111010001010111010110011" +
                "110001010100111000101011001111000110110111000000000"));
        assertThat((Object) BITS_160.window(-75, 181)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "1011010111101100111010110110100010011010111010001010111010110011" +
                "1100010101001110001010110011110001101101110000000000000000000000"));
        assertThat((Object) BITS_160.window(-75, 190)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "1011010111101100111010110110100010011010111010001010111010110011" +
                "1100010101001110001010110011110001101101110000000000000000000000" +
                "000000000"));
        assertThat((Object) BITS_160.window(-75, 192)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "1011010111101100111010110110100010011010111010001010111010110011" +
                "1100010101001110001010110011110001101101110000000000000000000000" +
                "00000000000"));
        assertThat((Object) BITS_160.window(-75, 200)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "1011010111101100111010110110100010011010111010001010111010110011" +
                "1100010101001110001010110011110001101101110000000000000000000000" +
                "0000000000000000000"));
        assertThat((Object) BITS_160.window(-75, 270)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000011010011110010001010110101110101001010111011010111010" +
                "1011010111101100111010110110100010011010111010001010111010110011" +
                "1100010101001110001010110011110001101101110000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000"));
    }

    @Test
    void testWindowShiftedFromNegativeClose() {
        assertThat((Object) BitString.empty().window(-7, 3)).isEqualTo(BitString.of("0000000000"));
        assertThat((Object) BitString.of("10011").window(-10, 3)).isEqualTo(BitString.of("0000000000100"));
        assertThat((Object) BITS_160.window(-41, 112)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000011010011110010001010110" +
                "1011101010010101110110101110101011010111101100111010110110100010" +
                "0110101110100010101110101"));
        assertThat((Object) BITS_160.window(-32, 160)).isEqualTo(BitString.of(
                "0000000000000000000000000000000011010011110010001010110101110101" +
                "0010101110110101110101011010111101100111010110110100010011010111" +
                "0100010101110101100111100010101001110001010110011110001101101110"));
        assertThat((Object) BITS_160.window(-10, 190)).isEqualTo(BitString.of(
                "0000000000110100111100100010101101011101010010101110110101110101" +
                "0110101111011001110101101101000100110101110100010101110101100111" +
                "1000101010011100010101100111100011011011100000000000000000000000" +
                "00000000"));
    }

    @Test
    void testWindowShiftedFromInnerSmall() {
        assertThat((Object) BitString.of("10011").window(1, 3)).isEqualTo(BitString.of("00"));
        assertThat((Object) BitString.of("10011").window(1, 4)).isEqualTo(BitString.of("001"));
        assertThat((Object) BitString.of("10011").window(3, 9)).isEqualTo(BitString.of("110000"));
        assertThat((Object) BitString.of("10011").window(3, 65)).isEqualTo(BitString.of(
                "11000000000000000000000000000000000000000000000000000000000000"));
        assertThat((Object) BITS_160.window(12, 25)).isEqualTo(BitString.of("1000101011010"));
        assertThat((Object) BITS_160.window(12, 64)).isEqualTo(BitString.of(
                "1000101011010111010100101011101101011101010110101111"));
        assertThat((Object) BITS_160.window(12, 160)).isEqualTo(BitString.of(
                "1000101011010111010100101011101101011101010110101111011001110101" +
                "1011010001001101011101000101011101011001111000101010011100010101" +
                "10011110001101101110"));
        assertThat((Object) BITS_160.window(12, 167)).isEqualTo(BitString.of(
                "1000101011010111010100101011101101011101010110101111011001110101" +
                "1011010001001101011101000101011101011001111000101010011100010101" +
                "100111100011011011100000000"));
        assertThat((Object) BITS_160.window(12, 204)).isEqualTo(BitString.of(
                "1000101011010111010100101011101101011101010110101111011001110101" +
                "1011010001001101011101000101011101011001111000101010011100010101" +
                "1001111000110110111000000000000000000000000000000000000000000000"));
        assertThat((Object) BITS_160.window(12, 210)).isEqualTo(BitString.of(
                "1000101011010111010100101011101101011101010110101111011001110101" +
                "1011010001001101011101000101011101011001111000101010011100010101" +
                "1001111000110110111000000000000000000000000000000000000000000000" +
                "000000"));
    }

    @Test
    void testWindowShiftedFromInnerMiddle() {
        assertThat((Object) BITS_160.window(77, 87)).isEqualTo(BitString.of("0110100010"));
        assertThat((Object) BITS_160.window(77, 128)).isEqualTo(BitString.of(
                "011010001001101011101000101011101011001111000101010"));
        assertThat((Object) BITS_160.window(77, 135)).isEqualTo(BitString.of(
                "0110100010011010111010001010111010110011110001010100111000"));
        assertThat((Object) BITS_160.window(77, 141)).isEqualTo(BitString.of(
                "0110100010011010111010001010111010110011110001010100111000101011"));
        assertThat((Object) BITS_160.window(77, 153)).isEqualTo(BitString.of(
                "0110100010011010111010001010111010110011110001010100111000101011" +
                "001111000110"));
        assertThat((Object) BITS_160.window(77, 160)).isEqualTo(BitString.of(
                "0110100010011010111010001010111010110011110001010100111000101011" +
                "0011110001101101110"));
        assertThat((Object) BITS_160.window(77, 170)).isEqualTo(BitString.of(
                "0110100010011010111010001010111010110011110001010100111000101011" +
                "00111100011011011100000000000"));
        assertThat((Object) BITS_160.window(77, 205)).isEqualTo(BitString.of(
                "0110100010011010111010001010111010110011110001010100111000101011" +
                "0011110001101101110000000000000000000000000000000000000000000000"));
        assertThat((Object) BITS_160.window(77, 230)).isEqualTo(BitString.of(
                "0110100010011010111010001010111010110011110001010100111000101011" +
                "0011110001101101110000000000000000000000000000000000000000000000" +
                "0000000000000000000000000"));
    }

    @Test
    void testWindowShiftedFromTail() {
        assertThat((Object) BITS_160.window(135, 145)).isEqualTo(BitString.of("1010110011"));
        assertThat((Object) BITS_160.window(135, 160)).isEqualTo(BitString.of("1010110011110001101101110"));
        assertThat((Object) BITS_160.window(135, 170)).isEqualTo(BitString.of(
                "10101100111100011011011100000000000"));
        assertThat((Object) BITS_160.window(135, 199)).isEqualTo(BitString.of(
                "1010110011110001101101110000000000000000000000000000000000000000"));
        assertThat((Object) BITS_160.window(135, 210)).isEqualTo(BitString.of(
                "1010110011110001101101110000000000000000000000000000000000000000" +
                "00000000000"));
    }

    @Test
    void testWindowIllegal() {
        BitString bitString = BitString.of("10011");
        assertThatThrownBy(() -> bitString.window(3, 2)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testShiftLeft() {
        assertThat((Object) BitString.empty().shiftLeft(0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.empty().shiftLeft(-2)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.empty().shiftLeft(3)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("1011011101").shiftLeft(-2)).isEqualTo(BitString.of("0000000000"));
        assertThat((Object) BitString.of("1011011101").shiftLeft(0)).isEqualTo(BitString.of("1011011101"));
        assertThat((Object) BitString.of("1011011101").shiftLeft(1)).isEqualTo(BitString.of("0110111010"));
        assertThat((Object) BitString.of("1011011101").shiftLeft(4)).isEqualTo(BitString.of("0111010000"));
        assertThat((Object) BitString.of("1011011101").shiftLeft(9)).isEqualTo(BitString.of("1000000000"));
        assertThat((Object) BitString.of("1011011101").shiftLeft(10)).isEqualTo(BitString.of("0000000000"));
        assertThat((Object) BitString.of("1011011101").shiftLeft(11)).isEqualTo(BitString.of("0000000000"));
        assertThat((Object) BitString.of(
                "1011011101010001010101110101000101110101010111010101111100101001" +
                "00101011101001110101").shiftLeft(64))
                .isEqualTo(BitString.of(
                "0010101110100111010100000000000000000000000000000000000000000000" +
                "00000000000000000000"));
        assertThat((Object) BitString.of(
                "0110110100111101000101010101011100010111010101011010101100010101" +
                "1010010111010101101010001010110100110011001011100100100110011010" +
                "1110010101100010011101001101011010100001100110111010010101010110" +
                "0011011010101100101011010100101101001010010101101010101010110101" +
                "1011101110010").shiftLeft(178))
                .isEqualTo(BitString.of(
                "1001010101011000110110101011001010110101001011010010100101011010" +
                "1010101011010110111011100100000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000"));
        assertThat((Object) BitString.of(
                "1001101000111101101110101001101011010110101110101101010101111010" +
                "0011001110101110101110101110101011101011101010100010101100101101" +
                "1101000111101110101011101010111010110101011110111101001000101010" +
                "0110101011101101011101010011010101110110100100101011001000111011").shiftLeft(35))
                .isEqualTo(BitString.of(
                "10110101110101101010101111010" +
                "0011001110101110101110101110101011101011101010100010101100101101" +
                "1101000111101110101011101010111010110101011110111101001000101010" +
                "011010101110110101110101001101010111011010010010101100100011101100000000000000000000000000000000000"));
        assertThat((Object) BitString.of(
                "0101111011101010101101101011110100010101011010101011101010111011" +
                "0101110110101010111010111010101000111011101010011010101011101010" +
                "1010100011010101001011101011010101011110101000101010010000101011" +
                "1001110110101110110100001010101110101010101010111010010110110101" +
                "110101001011010101110110101000111010101010111010").shiftLeft(149))
                .isEqualTo(BitString.of(
                "1101011010101011110101000101010010000101011" +
                "1001110110101110110100001010101110101010101010111010010110110101" +
                "110101001011010101110110101000111010101010111010" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000"));
    }

    @Test
    void testShiftRight() {
        assertThat((Object) BitString.empty().shiftRight(0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.empty().shiftRight(-2)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.empty().shiftRight(3)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("1011011101").shiftRight(-2)).isEqualTo(BitString.of("0000000000"));
        assertThat((Object) BitString.of("1011011101").shiftRight(0)).isEqualTo(BitString.of("1011011101"));
        assertThat((Object) BitString.of("1011011101").shiftRight(1)).isEqualTo(BitString.of("0101101110"));
        assertThat((Object) BitString.of("1011011101").shiftRight(4)).isEqualTo(BitString.of("0000101101"));
        assertThat((Object) BitString.of("1011011101").shiftRight(9)).isEqualTo(BitString.of("0000000001"));
        assertThat((Object) BitString.of("1011011101").shiftRight(64)).isEqualTo(BitString.of("0000000000"));
        assertThat((Object) BitString.of(
                "1011011101010001010101110101000101110101010111010101111100101001" +
                "00101011101001110101").shiftRight(64))
                .isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "10110111010100010101"));
        assertThat((Object) BitString.of(
                "1001101000111101101110101001101011010110101110101101010101111010" +
                "0011001110101110101110101110101011101011101010100010101100101101" +
                "1101000111101110101011101010111010110101011110111101001000101010" +
                "0110101011101101011101010011010101110110100100101011001000111011").shiftRight(150))
                .isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000100110100011110110111010100110101101011010" +
                "1110101101010101111010001100111010111010111010111010101110101110"));
        assertThat((Object) BitString.of(
                "0110110100111101000101010101011100010111010101011010101100010101" +
                "1010010111010101101010001010110100110011001011100100100110011010" +
                "1110010101100010011101001101011010100001100110111010010101010110" +
                "0011011010101100101011010100101101001010010101101010101010110101" +
                "1011101110010").shiftRight(178))
                .isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000001101101001111" +
                "0100010101010101110001011101010101101010110001010110100101110101" +
                "0110101000101"));
        assertThat((Object) BitString.of(
                "0101111011101010101101101011110100010101011010101011101010111011" +
                "0101110110101010111010111010101000111011101010011010101011101010" +
                "1010100011010101001011101011010101011110101000101010010000101011" +
                "1001110110101110110100001010101110101010101010111010010110110101" +
                "110101001011010101110110101000111010101010111010").shiftRight(149))
                .isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000101111011101010101101101011110100010101011" +
                "0101010111010101110110101110110101010111010111010101000111011101" +
                "010011010101011101010101010001101010100101110101"));
    }

    @Test
    void testPadLeft() {
        assertThat((Object) BitString.empty().padLeft(0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.empty().padLeft(1)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.empty().padLeft(5)).isEqualTo(BitString.of("00000"));
        assertThat((Object) BitString.of("0").padLeft(0)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("0").padLeft(1)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("0").padLeft(7)).isEqualTo(BitString.of("0000000"));
        assertThat((Object) BitString.of("1").padLeft(0)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("1").padLeft(1)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("1").padLeft(7)).isEqualTo(BitString.of("0000001"));
        assertThat((Object) BitString.of("11010100").padLeft(3)).isEqualTo(BitString.of("11010100"));
        assertThat((Object) BitString.of("11010100").padLeft(12)).isEqualTo(BitString.of("000011010100"));
        assertThat((Object) BitString.of("11010100").padLeft(65)).isEqualTo(BitString.of(
                "00000000000000000000000000000000000000000000000000000000011010100"));
        assertThat((Object) BitString.of("11010100").padLeft(133)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000110" +
                "10100"));
    }

    @Test
    void testPadRight() {
        assertThat((Object) BitString.empty().padRight(0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.empty().padRight(1)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.empty().padRight(5)).isEqualTo(BitString.of("00000"));
        assertThat((Object) BitString.of("0").padRight(0)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("0").padRight(1)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("0").padRight(7)).isEqualTo(BitString.of("0000000"));
        assertThat((Object) BitString.of("1").padRight(0)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("1").padRight(1)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("1").padRight(7)).isEqualTo(BitString.of("1000000"));
        assertThat((Object) BitString.of("11010100").padRight(3)).isEqualTo(BitString.of("11010100"));
        assertThat((Object) BitString.of("11010100").padRight(12)).isEqualTo(BitString.of("110101000000"));
        assertThat((Object) BitString.of("11010100").padRight(65)).isEqualTo(BitString.of(
                "11010100000000000000000000000000000000000000000000000000000000000"));
    }

    @Test
    void testResizeToSameSize() {
        assertThat((Object) BitString.empty().resize(0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.empty().resize(1)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("0110").resize(4)).isEqualTo(BitString.of("0110"));
        assertThat((Object) BitString.of(new long[] { 512342L }, 77).resize(77)).isEqualTo(BitString.of(new long[] { 512342L }, 77));
        assertThat((Object) BitString.of(new long[5]).resize(320)).isEqualTo(BitString.of(new long[5]));
    }

    @Test
    void testResizeLarger() {
        assertThat((Object) BitString.empty().resize(3)).isEqualTo(BitString.of("000"));
        assertThat((Object) BitString.of("10").resize(7)).isEqualTo(BitString.of("1000000"));
        assertThat((Object) BitString.empty().resize(67)).isEqualTo(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000000"));
        assertThat((Object) BitString.of("1101001").resize(67)).isEqualTo(BitString.of(
                "1101001000000000000000000000000000000000000000000000000000000000000"));
        assertThat((Object) BitString.of(new long[5]).resize(372)).isEqualTo(BitString.of(new long[5], 372));
    }

    @Test
    void testResizeSmaller() {
        assertThat((Object) BitString.of("10").resize(0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of("10").resize(1)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("01").resize(1)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("0110").resize(2)).isEqualTo(BitString.of("01"));
        assertThat((Object) BitString.of("11001010001010101110").resize(9)).isEqualTo(BitString.of("110010100"));
        assertThat((Object) BitString.of(new long[5]).resize(0)).isEqualTo(BitString.empty());
        assertThat((Object) BitString.of(new long[5]).resize(3)).isEqualTo(BitString.of("000"));
    }

    @Test
    void testResizeNegative() {
        assertThatThrownBy(() -> BitString.empty().resize(-1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testIndexOfOne() {
        assertThat(BitString.empty().indexOfOne()).isEqualTo(-1);
        assertThat(BitString.of("0").indexOfOne()).isEqualTo(-1);
        assertThat(BitString.of("00").indexOfOne()).isEqualTo(-1);
        assertThat(BitString.of("00000000").indexOfOne()).isEqualTo(-1);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000").indexOfOne()).isEqualTo(-1);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000").indexOfOne()).isEqualTo(-1);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000").indexOfOne()).isEqualTo(-1);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000").indexOfOne()).isEqualTo(-1);
        assertThat(BitString.of("1").indexOfOne()).isEqualTo(0);
        assertThat(BitString.of("10").indexOfOne()).isEqualTo(0);
        assertThat(BitString.of("10000").indexOfOne()).isEqualTo(0);
        assertThat(BitString.of(
                "1000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000").indexOfOne()).isEqualTo(0);
        assertThat(BitString.of(
                "1000000000000000000000000000000000000000000000000000000000000000" +
                "000000000001000000000000").indexOfOne()).isEqualTo(0);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "000000000001000000000000").indexOfOne()).isEqualTo(75);
        assertThat(BitString.of(
            "0000000000000000000000000000000000000000000000000000000000000000" +
            "0000000000000000000000000000000000000000000000000000000000000000" +
            "0000000000000000000000000000000000000000000000000000000000000000" +
            "0000000000000000000000000000000000000000001000000000000000000000" +
            "0000000000000000000000000000000000000000000000000000000000000000" +
            "0000000000000000010000000000000000000000000000000000000000000000" +
            "0000000000000000000000000000000000000000000000000000000000000000" +
            "0000000000000000000000000000000000000000000000000000000000000000").indexOfOne()).isEqualTo(234);
        assertThat(BitString.of("1011010").indexOfOne()).isEqualTo(0);
        assertThat(BitString.of("000101001101").indexOfOne()).isEqualTo(3);
        assertThat(BitString.of(
                "0000000100010000101100001000111000000110010001000010000101000010" +
                "00100001010001001010100").indexOfOne()).isEqualTo(7);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0101000001010000000101010100000010101100001101010011010000001010").indexOfOne()).isEqualTo(65);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "00000000000000101101001010000").indexOfOne()).isEqualTo(142);
    }

    @Test
    void testIndexOfZero() {
        assertThat(BitString.empty().indexOfZero()).isEqualTo(-1);
        assertThat(BitString.of("1").indexOfZero()).isEqualTo(-1);
        assertThat(BitString.of("11").indexOfZero()).isEqualTo(-1);
        assertThat(BitString.of("11111111").indexOfZero()).isEqualTo(-1);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111").indexOfZero()).isEqualTo(-1);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111").indexOfZero()).isEqualTo(-1);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111").indexOfZero()).isEqualTo(-1);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000").indexOfOne()).isEqualTo(-1);
        assertThat(BitString.of("0").indexOfZero()).isEqualTo(0);
        assertThat(BitString.of("01").indexOfZero()).isEqualTo(0);
        assertThat(BitString.of("01111").indexOfZero()).isEqualTo(0);
        assertThat(BitString.of(
                "0111111111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111").indexOfZero()).isEqualTo(0);
        assertThat(BitString.of(
                "0111111111111111111111111111111111111111111111111111111111111111" +
                "111111111110111111111111").indexOfZero()).isEqualTo(0);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "111111111110111111111111").indexOfZero()).isEqualTo(75);
        assertThat(BitString.of(
            "1111111111111111111111111111111111111111111111111111111111111111" +
            "1111111111111111111111111111111111111111111111111111111111111111" +
            "1111111111111111111111111111111111111111111111111111111111111111" +
            "1111111111111111111111111111111111111111110111111111111111111111" +
            "1111111111111111111111111111111111111111111111111111111111111111" +
            "1111111111111111101111111111111111111111111111111111111111111111" +
            "1111111111111111111111111111111111111111111111111111111111111111" +
            "1111111111111111111111111111111111111111111111111111111111111111").indexOfZero()).isEqualTo(234);
        assertThat(BitString.of("0100101").indexOfZero()).isEqualTo(0);
        assertThat(BitString.of("111010110010").indexOfZero()).isEqualTo(3);
        assertThat(BitString.of(
                "1111111011101111010011110111000111111001101110111101111010111101" +
                "11011110101110110101011").indexOfZero()).isEqualTo(7);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "1010111110101111111010101011111101010011110010101100101111110101").indexOfZero()).isEqualTo(65);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "11111111111111010010110101111").indexOfZero()).isEqualTo(142);
    }

    @Test
    void testIndexOfBitAliases() {
        assertThat(BitString.of("000101001101").indexOf(true)).isEqualTo(3);
        assertThat(BitString.of("000101001101").indexOf('1')).isEqualTo(3);
        assertThat(BitString.of("111010110010").indexOf(false)).isEqualTo(3);
        assertThat(BitString.of("111010110010").indexOf('0')).isEqualTo(3);
    }

    @Test
    void testLastIndexOfOne() {
        assertThat(BitString.empty().lastIndexOfOne()).isEqualTo(-1);
        assertThat(BitString.of("0").lastIndexOfOne()).isEqualTo(-1);
        assertThat(BitString.of("00").lastIndexOfOne()).isEqualTo(-1);
        assertThat(BitString.of("00000000").lastIndexOfOne()).isEqualTo(-1);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000").lastIndexOfOne()).isEqualTo(-1);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000000").lastIndexOfOne()).isEqualTo(-1);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000").lastIndexOfOne()).isEqualTo(-1);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000").lastIndexOfOne()).isEqualTo(-1);
        assertThat(BitString.of("1").lastIndexOfOne()).isEqualTo(0);
        assertThat(BitString.of("01").lastIndexOfOne()).isEqualTo(1);
        assertThat(BitString.of("00001").lastIndexOfOne()).isEqualTo(4);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000000001").lastIndexOfOne()).isEqualTo(87);
        assertThat(BitString.of(
                "0000000000000000000000010000000000000000000000000000000000000000" +
                "000000000000000000000001").lastIndexOfOne()).isEqualTo(87);
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "000000000001000000000000").lastIndexOfOne()).isEqualTo(75);
        assertThat(BitString.of(
                "0000000000000000000000000000000000001000000000000000000000000000" +
                "000000000000000000000000").lastIndexOfOne()).isEqualTo(36);
        assertThat(BitString.of(
                "0000000000000000000000000000000000001000000000000000000000000000" +
                "000000000001000000000000").lastIndexOfOne()).isEqualTo(75);
        assertThat(BitString.of(
            "0000000000000000000000000000000000000000000000000000000000000000" +
            "0000000000000000000000000000000000000000000000000000000000000000" +
            "0000000000000000000000000000000000000000000000000000000000000000" +
            "0000000000000000000000000000000000000000001000000000000000000000" +
            "0000000000000000000000000000000000000000000000000000000000000000" +
            "0000000000000000010000000000000000000000000000000000000000000000" +
            "0000000000000000000000000000000000000000000000000000000000000000" +
            "0000000000000000000000000000000000000000000000000000000000000000").lastIndexOfOne()).isEqualTo(337);
        assertThat(BitString.of("1011010").lastIndexOfOne()).isEqualTo(5);
        assertThat(BitString.of("0101011000").lastIndexOfOne()).isEqualTo(6);
        assertThat(BitString.of("000101001101").lastIndexOfOne()).isEqualTo(11);
        assertThat(BitString.of(
                "0000010001000010110010000011010010001100100010000100001010001010" +
                "0010000101000100101010000").lastIndexOfOne()).isEqualTo(84);
        assertThat(BitString.of(
                "0000101100110011110100000110010010000001011000001000000000000000" +
                "000000000000000000000000").lastIndexOfOne()).isEqualTo(48);
    }

    @Test
    void testLastIndexOfZero() {
        assertThat(BitString.empty().lastIndexOfZero()).isEqualTo(-1);
        assertThat(BitString.of("1").lastIndexOfZero()).isEqualTo(-1);
        assertThat(BitString.of("11").lastIndexOfZero()).isEqualTo(-1);
        assertThat(BitString.of("11111111").lastIndexOfZero()).isEqualTo(-1);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111").lastIndexOfZero()).isEqualTo(-1);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111111").lastIndexOfZero()).isEqualTo(-1);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111").lastIndexOfZero()).isEqualTo(-1);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "1111111111111111111").lastIndexOfZero()).isEqualTo(-1);
        assertThat(BitString.of("0").lastIndexOfZero()).isEqualTo(0);
        assertThat(BitString.of("10").lastIndexOfZero()).isEqualTo(1);
        assertThat(BitString.of("11110").lastIndexOfZero()).isEqualTo(4);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "111111111111111111111110").lastIndexOfZero()).isEqualTo(87);
        assertThat(BitString.of(
                "1111111111111111111111101111111111111111111111111111111111111111" +
                "111111111111111111111110").lastIndexOfZero()).isEqualTo(87);
        assertThat(BitString.of(
                "1111111111111111111111111111111111111111111111111111111111111111" +
                "111111111110111111111111").lastIndexOfZero()).isEqualTo(75);
        assertThat(BitString.of(
                "1111111111111111111111111111111111110111111111111111111111111111" +
                "111111111111111111111111").lastIndexOfZero()).isEqualTo(36);
        assertThat(BitString.of(
                "1111111111111111111111111111111111110111111111111111111111111111" +
                "111111111110111111111111").lastIndexOfZero()).isEqualTo(75);
        assertThat(BitString.of(
            "1111111111111111111111111111111111111111111111111111111111111111" +
            "1111111111111111111111111111111111111111111111111111111111111111" +
            "1111111111111111111111111111111111111111111111111111111111111111" +
            "1111111111111111111111111111111111111111110111111111111111111111" +
            "1111111111111111111111111111111111111111111111111111111111111111" +
            "1111111111111111101111111111111111111111111111111111111111111111" +
            "1111111111111111111111111111111111111111111111111111111111111111" +
            "1111111111111111111111111111111111111111111111111111111111111111").lastIndexOfZero()).isEqualTo(337);
        assertThat(BitString.of("0100101").lastIndexOfZero()).isEqualTo(5);
        assertThat(BitString.of("1010100111").lastIndexOfZero()).isEqualTo(6);
        assertThat(BitString.of("111010110010").lastIndexOfZero()).isEqualTo(11);
        assertThat(BitString.of(
                "1111101110111101001101111100101101110011011101111011110101110101" +
                "1101111010111011010101111").lastIndexOfZero()).isEqualTo(84);
        assertThat(BitString.of(
                "1111010011001100001011111001101101111110100111110111111111111111" +
                "111111111111111111111111").lastIndexOfZero()).isEqualTo(48);
    }

    @Test
    void testLastIndexOfBitAliases() {
        assertThat(BitString.of("0101011000").lastIndexOf(true)).isEqualTo(6);
        assertThat(BitString.of("0101011000").lastIndexOf('1')).isEqualTo(6);
        assertThat(BitString.of("1010100111").lastIndexOf(false)).isEqualTo(6);
        assertThat(BitString.of("1010100111").lastIndexOf('0')).isEqualTo(6);
    }

    @Test
    void testStartsWith() {
        assertThat(BitString.empty().startsWith(BitString.empty())).isTrue();
        assertThat(BitString.empty().startsWith(BitString.of("0"))).isFalse();
        assertThat(BitString.empty().startsWith(BitString.of("000"))).isFalse();
        assertThat(BitString.empty().startsWith(BitString.of("0000000000"))).isFalse();
        assertThat(BitString.empty().startsWith(BitString.of("1"))).isFalse();
        assertThat(BitString.empty().startsWith(BitString.of("10010"))).isFalse();
        assertThat(BitString.empty().startsWith(BitString.of("0111010"))).isFalse();
        assertThat(BitString.of("0").startsWith(BitString.empty())).isTrue();
        assertThat(BitString.of("0").startsWith(BitString.of("0"))).isTrue();
        assertThat(BitString.of("0").startsWith(BitString.of("1"))).isFalse();
        assertThat(BitString.of("0").startsWith(BitString.of("01"))).isFalse();
        assertThat(BitString.of("0").startsWith(BitString.of("0011010"))).isFalse();
        assertThat(BitString.of("1").startsWith(BitString.empty())).isTrue();
        assertThat(BitString.of("1").startsWith(BitString.of("1"))).isTrue();
        assertThat(BitString.of("1").startsWith(BitString.of("0"))).isFalse();
        assertThat(BitString.of("01").startsWith(BitString.of("0"))).isTrue();
        assertThat(BitString.of("01").startsWith(BitString.of("1"))).isFalse();
        assertThat(BitString.of("10011010111000").startsWith(BitString.of("10011"))).isTrue();
        assertThat(BitString.of("10011010111000").startsWith(BitString.of("10110"))).isFalse();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).startsWith(BitString.of(
                "0110101010111010111"))).isTrue();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).startsWith(BitString.of(
                "0101111010"))).isFalse();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).startsWith(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"))).isTrue();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).startsWith(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "1100001"))).isTrue();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).startsWith(BitString.of(
                "0110101010111010101101011110101000110101110101001011110101110101" +
                "1100001"))).isFalse();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).startsWith(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "1100101"))).isFalse();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).startsWith(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010000"))).isFalse();
    }

    @Test
    void testEndsWith() {
        assertThat(BitString.empty().endsWith(BitString.empty())).isTrue();
        assertThat(BitString.empty().endsWith(BitString.of("0"))).isFalse();
        assertThat(BitString.empty().endsWith(BitString.of("000"))).isFalse();
        assertThat(BitString.empty().endsWith(BitString.of("0000000000"))).isFalse();
        assertThat(BitString.empty().endsWith(BitString.of("1"))).isFalse();
        assertThat(BitString.empty().endsWith(BitString.of("10010"))).isFalse();
        assertThat(BitString.empty().endsWith(BitString.of("0111010"))).isFalse();
        assertThat(BitString.of("0").endsWith(BitString.empty())).isTrue();
        assertThat(BitString.of("0").endsWith(BitString.of("0"))).isTrue();
        assertThat(BitString.of("0").endsWith(BitString.of("1"))).isFalse();
        assertThat(BitString.of("0").endsWith(BitString.of("01"))).isFalse();
        assertThat(BitString.of("0").endsWith(BitString.of("0011010"))).isFalse();
        assertThat(BitString.of("1").endsWith(BitString.empty())).isTrue();
        assertThat(BitString.of("1").endsWith(BitString.of("1"))).isTrue();
        assertThat(BitString.of("1").endsWith(BitString.of("0"))).isFalse();
        assertThat(BitString.of("01").endsWith(BitString.of("0"))).isFalse();
        assertThat(BitString.of("01").endsWith(BitString.of("1"))).isTrue();
        assertThat(BitString.of("10011010111000").endsWith(BitString.of("11000"))).isTrue();
        assertThat(BitString.of("10011010111000").endsWith(BitString.of("10110"))).isFalse();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).endsWith(BitString.of(
                "00011101010"))).isTrue();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).endsWith(BitString.of(
                "0101111010"))).isFalse();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).endsWith(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"))).isTrue();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).endsWith(BitString.of(
                "1010111010111101011110101000110101110101001011110101110101110000" +
                "11101010"))).isTrue();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).endsWith(BitString.of(
                "1010111010111101011010101000110101110101001011110101110101110000" +
                "11101010"))).isFalse();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).endsWith(BitString.of(
                "1010111010111101011110101000110101110101001011110101110101110000" +
                "11101110"))).isFalse();
        assertThat(BitString.of(
                "0110101010111010111101011110101000110101110101001011110101110101" +
                "11000011101010"
                ).endsWith(BitString.of(
                "0000110101010111010111101011110101000110101110101001011110101110" +
                "10111000011101010"))).isFalse();
    }

    @Test
    void testMatch() {
        assertThat(BitString.empty().match(BitString.empty(), 0)).isTrue();
        assertThat(BitString.of("0").match(BitString.empty(), 0)).isTrue();
        assertThat(BitString.of("0").match(BitString.empty(), 1)).isTrue();
        assertThat(BitString.of("0").match(BitString.empty(), 2)).isFalse();
        assertThat(BitString.of("0").match(BitString.empty(), 15)).isFalse();
        assertThat(BitString.of("0").match(BitString.empty(), -1)).isFalse();
        assertThat(BitString.of("0").match(BitString.empty(), -3)).isFalse();
        assertThat(BitString.of("101100").match(BitString.empty(), 0)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("1"), 0)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("10"), 0)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("101"), 0)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("1011"), 0)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("10110"), 0)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("101100"), 0)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("1011000"), 0)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("1011001"), 0)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("1"), 1)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("101100"), 1)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("1011001"), 1)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("0"), 1)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("01"), 1)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("011"), 1)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("01100"), 1)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("0111"), 1)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("01101"), 1)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("011000"), 1)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("011001"), 1)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("0"), 3)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("1"), 3)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("100"), 3)).isTrue();
        assertThat(BitString.of("101100").match(BitString.of("1000"), 3)).isFalse();
        assertThat(BitString.of("101100").match(BitString.of("1001"), 3)).isFalse();
        assertThat(BitString.of("0000000000000000000000000000000000000000000000000000000000000000")
                .match(BitString.of("0000000"), 10)).isTrue();
        assertThat(BitString.of("0000000000000000000000000000000000000000000000000000000000000000")
                .match(BitString.of("00010000"), 10)).isFalse();
        assertThat(BitString.of("0000000000000000000000000000000000000000000000000000000000000000")
                .match(BitString.of("0000000"), 60)).isFalse();
        assertThat(BitString.of("0000000000000000000000000000000000000000000000000000000000000000")
                .match(BitString.of("00010000"), 60)).isFalse();
        assertThat(BitString.of("0000000000000000000000000000000000000000000000000000000000000000")
                .match(BitString.of("0000000010000"), 60)).isFalse();
        assertThat(BITS_160.match(BitString.empty(), -1)).isFalse();
        assertThat(BITS_160.match(BitString.empty(), 80)).isTrue();
        assertThat(BITS_160.match(BitString.of("0100010011"), 80)).isTrue();
        assertThat(BITS_160.match(BitString.of("0101101010"), 80)).isFalse();
        assertThat(BITS_160.match(BitString.of("001"), 120)).isTrue();
        assertThat(BITS_160.match(BitString.of("0010101001110001"), 120)).isTrue();
        assertThat(BITS_160.match(BitString.of("00011010011100"), 120)).isFalse();
        assertThat(BITS_160.match(BitString.of("1101101110"), 150)).isTrue();
        assertThat(BITS_160.match(BitString.of("1101101111"), 150)).isFalse();
        assertThat(BITS_160.match(BitString.of("11011011100"), 150)).isFalse();
        assertThat(BITS_160.match(BitString.empty(), 160)).isTrue();
        assertThat(BITS_160.match(BitString.empty(), 165)).isFalse();
    }

    @Test
    void testIndexOfSubstring() {
        assertThat(BitString.empty().indexOf(BitString.empty())).isEqualTo(0);
        assertThat(BitString.empty().indexOf(BitString.of("0"))).isEqualTo(-1);
        assertThat(BitString.empty().indexOf(BitString.of("1"))).isEqualTo(-1);
        assertThat(BitString.empty().indexOf(BitString.of("1010100"))).isEqualTo(-1);
        assertThat(BitString.of("0").indexOf(BitString.empty())).isEqualTo(0);
        assertThat(BitString.of("0").indexOf(BitString.of("0"))).isEqualTo(0);
        assertThat(BitString.of("0").indexOf(BitString.of("00"))).isEqualTo(-1);
        assertThat(BitString.of("0").indexOf(BitString.of("1"))).isEqualTo(-1);
        assertThat(BitString.of("1").indexOf(BitString.empty())).isEqualTo(0);
        assertThat(BitString.of("1").indexOf(BitString.of("1"))).isEqualTo(0);
        assertThat(BitString.of("1").indexOf(BitString.of("11"))).isEqualTo(-1);
        assertThat(BitString.of("1").indexOf(BitString.of("0"))).isEqualTo(-1);
        assertThat(BitString.of("100110111010").indexOf(BitString.of("0"))).isEqualTo(1);
        assertThat(BitString.of("100110111010").indexOf(BitString.of("1"))).isEqualTo(0);
        assertThat(BitString.of("100110111010").indexOf(BitString.of("000"))).isEqualTo(-1);
        assertThat(BitString.of("100110111010").indexOf(BitString.of("100"))).isEqualTo(0);
        assertThat(BitString.of("100110111010").indexOf(BitString.of("110"))).isEqualTo(3);
        assertThat(BitString.of("100110111010").indexOf(BitString.of("1010"))).isEqualTo(8);
        assertThat(BitString.of("100110111010").indexOf(BitString.of("1101"))).isEqualTo(3);
        assertThat(BitString.of("100110111010").indexOf(BitString.of("0100"))).isEqualTo(-1);
        assertThat(BitString.of("100110111010").indexOf(BitString.of("100110111010"))).isEqualTo(0);
        assertThat(BitString.of(
                "0000000000001110000000000000000000000000000000000000000000000000" +
                "00011110000000000").indexOf(BitString.of("111"))).isEqualTo(12);
        assertThat(BitString.of(
                "0000000000001110000000000000000000000000000000000000000000000000" +
                "00011110000000000").indexOf(BitString.of("1111"))).isEqualTo(67);
        assertThat(BITS_160.indexOf(BitString.of("0"))).isEqualTo(2);
        assertThat(BITS_160.indexOf(BitString.of("1"))).isEqualTo(0);
        assertThat(BITS_160.indexOf(BitString.of("100"))).isEqualTo(3);
        assertThat(BITS_160.indexOf(BitString.of("10001010110"))).isEqualTo(12);
        assertThat(BITS_160.indexOf(BitString.of("1101011101010"))).isEqualTo(20);
        assertThat(BITS_160.indexOf(BitString.of("11010111010001010111"))).isEqualTo(88);
        assertThat(BITS_160.indexOf(BitString.of("1101011110"))).isEqualTo(55);
        assertThat(BITS_160.indexOf(BitString.of("1001110"))).isEqualTo(66);
        assertThat(BITS_160.indexOf(BitString.of("100111100010101001110001"))).isEqualTo(112);
        assertThat(BITS_160.indexOf(BitString.of("10111010100101110"))).isEqualTo(-1);
        assertThat(BITS_160.indexOf(BitString.of("11111111111111111111"))).isEqualTo(-1);
    }

    @Test
    void testLastIndexOfSubstring() {
        assertThat(BitString.empty().lastIndexOf(BitString.empty())).isEqualTo(0);
        assertThat(BitString.empty().lastIndexOf(BitString.of("0"))).isEqualTo(-1);
        assertThat(BitString.empty().lastIndexOf(BitString.of("1"))).isEqualTo(-1);
        assertThat(BitString.empty().lastIndexOf(BitString.of("1010100"))).isEqualTo(-1);
        assertThat(BitString.of("0").lastIndexOf(BitString.empty())).isEqualTo(1);
        assertThat(BitString.of("0").lastIndexOf(BitString.of("0"))).isEqualTo(0);
        assertThat(BitString.of("0").lastIndexOf(BitString.of("00"))).isEqualTo(-1);
        assertThat(BitString.of("0").lastIndexOf(BitString.of("1"))).isEqualTo(-1);
        assertThat(BitString.of("1").lastIndexOf(BitString.empty())).isEqualTo(1);
        assertThat(BitString.of("1").lastIndexOf(BitString.of("1"))).isEqualTo(0);
        assertThat(BitString.of("1").lastIndexOf(BitString.of("11"))).isEqualTo(-1);
        assertThat(BitString.of("1").lastIndexOf(BitString.of("0"))).isEqualTo(-1);
        assertThat(BitString.of("100110111010").lastIndexOf(BitString.of("0"))).isEqualTo(11);
        assertThat(BitString.of("100110111010").lastIndexOf(BitString.of("1"))).isEqualTo(10);
        assertThat(BitString.of("100110111010").lastIndexOf(BitString.of("000"))).isEqualTo(-1);
        assertThat(BitString.of("100110111010").lastIndexOf(BitString.of("100"))).isEqualTo(0);
        assertThat(BitString.of("100110111010").lastIndexOf(BitString.of("110"))).isEqualTo(7);
        assertThat(BitString.of("100110111010").lastIndexOf(BitString.of("1010"))).isEqualTo(8);
        assertThat(BitString.of("100110111010").lastIndexOf(BitString.of("1101"))).isEqualTo(7);
        assertThat(BitString.of("100110111010").lastIndexOf(BitString.of("0100"))).isEqualTo(-1);
        assertThat(BitString.of("100110111010").lastIndexOf(BitString.of("100110111010"))).isEqualTo(0);
        assertThat(BitString.of(
                "0000000000001110000000000000000000000000000000000000000000000000" +
                "00011110000000000").lastIndexOf(BitString.of("111"))).isEqualTo(68);
        assertThat(BitString.of(
                "0000000000001110000000000000000000000000000000000000000000000000" +
                "00011110000000000").lastIndexOf(BitString.of("1111"))).isEqualTo(67);
        assertThat(BITS_160.lastIndexOf(BitString.of("0"))).isEqualTo(159);
        assertThat(BITS_160.lastIndexOf(BitString.of("1"))).isEqualTo(158);
        assertThat(BITS_160.lastIndexOf(BitString.of("100"))).isEqualTo(146);
        assertThat(BITS_160.lastIndexOf(BitString.of("10001010110"))).isEqualTo(131);
        assertThat(BITS_160.lastIndexOf(BitString.of("1101011101010"))).isEqualTo(42);
        assertThat(BITS_160.lastIndexOf(BitString.of("11010111010001010111"))).isEqualTo(88);
        assertThat(BITS_160.lastIndexOf(BitString.of("1101011110"))).isEqualTo(55);
        assertThat(BITS_160.lastIndexOf(BitString.of("1001110"))).isEqualTo(126);
        assertThat(BITS_160.lastIndexOf(BitString.of("100111100010101001110001"))).isEqualTo(112);
        assertThat(BITS_160.lastIndexOf(BitString.of("10111010100101110"))).isEqualTo(-1);
        assertThat(BITS_160.lastIndexOf(BitString.of("11111111111111111111"))).isEqualTo(-1);
    }

    @Test
    void testCountOfOnes() {
        assertThat(BitString.empty().countOfOnes()).isEqualTo(0);
        assertThat(BitString.of("0").countOfOnes()).isEqualTo(0);
        assertThat(BitString.of("1").countOfOnes()).isEqualTo(1);
        assertThat(BitString.of("0000000").countOfOnes()).isEqualTo(0);
        assertThat(BitString.of("1111111").countOfOnes()).isEqualTo(7);
        assertThat(BitString.of("01110001011011001").countOfOnes()).isEqualTo(9);
        assertThat(BitString.of(
                "1000111101010011011011010111010001101111011001010101110101011101" +
                "0100101101101101101110101000100011110101110101000011010101110101" +
                "1101000101000101000101001").countOfOnes()).isEqualTo(83);
    }

    @Test
    void testCountOfZeros() {
        assertThat(BitString.empty().countOfZeros()).isEqualTo(0);
        assertThat(BitString.of("0").countOfZeros()).isEqualTo(1);
        assertThat(BitString.of("1").countOfZeros()).isEqualTo(0);
        assertThat(BitString.of("0000000").countOfZeros()).isEqualTo(7);
        assertThat(BitString.of("1111111").countOfZeros()).isEqualTo(0);
        assertThat(BitString.of("01110001011011001").countOfZeros()).isEqualTo(8);
        assertThat(BitString.of(
                "1000111101010011011011010111010001101111011001010101110101011101" +
                "0100101101101101101110101000100011110101110101000011010101110101" +
                "1101000101000101000101001").countOfZeros()).isEqualTo(70);
    }

    @Test
    void testToLong() {
        assertThat(BitString.empty().toLong()).isEqualTo(0);
        assertThat(BitString.of("0").toLong()).isZero();
        assertThat(BitString.of("00").toLong()).isZero();
        assertThat(BitString.of("000").toLong()).isZero();
        assertThat(BitString.of("000000000000").toLong()).isZero();
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000").toLong()).isZero();
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000000").toLong()).isZero();
        assertThat(BitString.of("1").toLong()).isOne();
        assertThat(BitString.of("01").toLong()).isOne();
        assertThat(BitString.of("001").toLong()).isOne();
        assertThat(BitString.of("000000000000001").toLong()).isOne();
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000001").toLong()).isOne();
        assertThat(BitString.of(
                "0000000000000000000000000000000000000000000000000000000000000000" +
                "0000000000000000000000001").toLong()).isOne();
        assertThat(BitString.of("10").toLong()).isEqualTo(2);
        assertThat(BitString.of("100").toLong()).isEqualTo(4);
        assertThat(BitString.of("1000").toLong()).isEqualTo(8);
        assertThat(BitString.of("1000000000").toLong()).isEqualTo(512);
        assertThat(BitString.of(
                "1000000000000000000000000000000000000000000000000000000000000000").toLong()).isEqualTo(
                -9223372036854775808L);
        assertThat(BitString.of(
                "1000000000000000000000000000000000000000000000000000000000000000" +
                "000000000000000000").toLong()).isZero();
        assertThat(BitString.of("1011").toLong()).isEqualTo(11);
        assertThat(BitString.of("100111010").toLong()).isEqualTo(314);
        assertThat(BitString.of("000111011010001011101000110101110100").toLong()).isEqualTo(7955451252L);
        assertThat(BitString.of(
                "1101011101011011101010010100101101111001110100101011101000100110").toLong()).isEqualTo(
                -2928560991042880986L);
        assertThat(BitString.of(
                "1000000000000000000000000000000000000000000000000000000000000000" +
                "0000000001101001101").toLong()).isEqualTo(845);
        assertThat(BitString.of(
                "0111011110101000101110110101100110001011010110100010101110100101" +
                "01011100010010111010101011").toLong()).isEqualTo(
                7362656064861449899L);
    }

    @Test
    void testToBooleanArray() {
        assertThat(BitString.empty().toBooleanArray()).isEmpty();
        assertThat(BitString.of("0").toBooleanArray()).containsExactly(false);
        assertThat(BitString.of("000").toBooleanArray()).containsExactly(false, false, false);
        assertThat(BitString.of("00000000").toBooleanArray()).containsExactly(
                false, false, false, false, false, false, false, false);
        assertThat(BitString.of("0000000000").toBooleanArray()).containsExactly(
                false, false, false, false, false, false, false, false,
                false, false);
        assertThat(BitString.of("0000000000000000").toBooleanArray()).containsExactly(
                false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false);
        assertThat(BitString.of("00000000000000000").toBooleanArray()).containsExactly(
                false, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false,
                false);
        assertThat(BitString.of("1").toBooleanArray()).containsExactly(true);
        assertThat(BitString.of("10").toBooleanArray()).containsExactly(true, false);
        assertThat(BitString.of("10000").toBooleanArray()).containsExactly(true, false, false, false, false);
        assertThat(BitString.of("10000000").toBooleanArray()).containsExactly(
                true, false, false, false, false, false, false, false);
        assertThat(BitString.of("1000000000").toBooleanArray()).containsExactly(
                true, false, false, false, false, false, false, false,
                false, false);
        assertThat(BitString.of("1000000000000000").toBooleanArray()).containsExactly(
                true, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false);
        assertThat(BitString.of("1000000000000000000").toBooleanArray()).containsExactly(
                true, false, false, false, false, false, false, false,
                false, false, false, false, false, false, false, false,
                false, false, false);
        assertThat(BitString.of("11").toBooleanArray()).containsExactly(true, true);
        assertThat(BitString.of("1011").toBooleanArray()).containsExactly(true, false, true, true);
        assertThat(BitString.of("10011011").toBooleanArray()).containsExactly(
                true, false, false, true, true, false, true, true);
        assertThat(BitString.of("11011100010110").toBooleanArray()).containsExactly(
                true, true, false, true, true, true, false, false,
                false, true, false, true, true, false);
        assertThat(BitString.of("1011000101110110").toBooleanArray()).containsExactly(
                true, false, true, true, false, false, false, true,
                false, true, true, true, false, true, true, false);
        assertThat(BitString.of("1011011011101001101").toBooleanArray()).containsExactly(
                true, false, true, true, false, true, true, false,
                true, true, true, false, true, false, false, true,
                true, false, true);
        assertThat(BitString.of("011").toBooleanArray()).containsExactly(false, true, true);
        assertThat(BitString.of("00010100000011").toBooleanArray()).containsExactly(
                false, false, false, true, false, true, false, false,
                false, false, false, false, true, true);
        assertThat(BitString.of("0011011001000100011000").toBooleanArray()).containsExactly(
                false, false, true, true, false, true, true, false,
                false, true, false, false, false, true, false, false,
                false, true, true, false, false, false);
        assertThat(BitString.of(
                "1011001011101001100111010100011101011011010100011101010111010101" +
                "1101010110101000111010101101001001000111010100010101011110101011" +
                "101010").toBooleanArray()).containsExactly(
                true, false, true, true, false, false, true, false,
                true, true, true, false, true, false, false, true,
                true, false, false, true, true, true, false, true,
                false, true, false, false, false, true, true, true,
                false, true, false, true, true, false, true, true,
                false, true, false, true, false, false, false, true,
                true, true, false, true, false, true, false, true,
                true, true, false, true, false, true, false, true,
                true, true, false, true, false, true, false, true,
                true, false, true, false, true, false, false, false,
                true, true, true, false, true, false, true, false,
                true, true, false, true, false, false, true, false,
                false, true, false, false, false, true, true, true,
                false, true, false, true, false, false, false, true,
                false, true, false, true, false, true, true, true,
                true, false, true, false, true, false, true, true,
                true, false, true, false, true, false);
    }

    @Test
    void testToByteArrayLeftAligned() {
        assertThat(BitString.empty().toByteArrayLeftAligned()).isEmpty();
        assertThat(BitString.of("0").toByteArrayLeftAligned()).containsExactly(0);
        assertThat(BitString.of("000").toByteArrayLeftAligned()).containsExactly(0);
        assertThat(BitString.of("00000000").toByteArrayLeftAligned()).containsExactly(0);
        assertThat(BitString.of("0000000000").toByteArrayLeftAligned()).containsExactly(0, 0);
        assertThat(BitString.of("0000000000000000").toByteArrayLeftAligned()).containsExactly(0, 0);
        assertThat(BitString.of("00000000000000000").toByteArrayLeftAligned()).containsExactly(0, 0, 0);
        assertThat(BitString.of("1").toByteArrayLeftAligned()).containsExactly(128);
        assertThat(BitString.of("10").toByteArrayLeftAligned()).containsExactly(128);
        assertThat(BitString.of("10000").toByteArrayLeftAligned()).containsExactly(128);
        assertThat(BitString.of("10000000").toByteArrayLeftAligned()).containsExactly(128);
        assertThat(BitString.of("1000000000").toByteArrayLeftAligned()).containsExactly(128, 0);
        assertThat(BitString.of("1000000000000000").toByteArrayLeftAligned()).containsExactly(128, 0);
        assertThat(BitString.of("1000000000000000000").toByteArrayLeftAligned()).containsExactly(128, 0, 0);
        assertThat(BitString.of("11").toByteArrayLeftAligned()).containsExactly(192);
        assertThat(BitString.of("1011").toByteArrayLeftAligned()).containsExactly(176);
        assertThat(BitString.of("10011011").toByteArrayLeftAligned()).containsExactly(155);
        assertThat(BitString.of("11011100010110").toByteArrayLeftAligned()).containsExactly(220, 88);
        assertThat(BitString.of("1011000101110110").toByteArrayLeftAligned()).containsExactly(177, 118);
        assertThat(BitString.of("1011011011101001101").toByteArrayLeftAligned()).containsExactly(182, 233, 160);
        assertThat(BitString.of("011").toByteArrayLeftAligned()).containsExactly(96);
        assertThat(BitString.of("00010100000011").toByteArrayLeftAligned()).containsExactly(20, 12);
        assertThat(BitString.of("0011011001000100011000").toByteArrayLeftAligned()).containsExactly(54, 68, 96);
        assertThat(BitString.of(
                "1011001011101001100111010100011101011011010100011101010111010101" +
                "1101010110101000111010101101001001000111010100010101011110101011" +
                "101010").toByteArrayLeftAligned()).containsExactly(
                178, 233, 157, 71, 91, 81, 213, 213, 213, 168, 234, 210, 71, 81, 87, 171, 168);
    }

    @Test
    void testToByteArrayRightAligned() {
        assertThat(BitString.empty().toByteArrayRightAligned()).isEmpty();
        assertThat(BitString.of("0").toByteArrayRightAligned()).containsExactly(0);
        assertThat(BitString.of("000").toByteArrayRightAligned()).containsExactly(0);
        assertThat(BitString.of("00000000").toByteArrayRightAligned()).containsExactly(0);
        assertThat(BitString.of("0000000000").toByteArrayRightAligned()).containsExactly(0, 0);
        assertThat(BitString.of("0000000000000000").toByteArrayRightAligned()).containsExactly(0, 0);
        assertThat(BitString.of("00000000000000000").toByteArrayRightAligned()).containsExactly(0, 0, 0);
        assertThat(BitString.of("1").toByteArrayRightAligned()).containsExactly(1);
        assertThat(BitString.of("10").toByteArrayRightAligned()).containsExactly(2);
        assertThat(BitString.of("10000").toByteArrayRightAligned()).containsExactly(16);
        assertThat(BitString.of("10000000").toByteArrayRightAligned()).containsExactly(128);
        assertThat(BitString.of("1000000000").toByteArrayRightAligned()).containsExactly(2, 0);
        assertThat(BitString.of("1000000000000000").toByteArrayRightAligned()).containsExactly(128, 0);
        assertThat(BitString.of("1000000000000000000").toByteArrayRightAligned()).containsExactly(4, 0, 0);
        assertThat(BitString.of("11").toByteArrayRightAligned()).containsExactly(3);
        assertThat(BitString.of("1011").toByteArrayRightAligned()).containsExactly(11);
        assertThat(BitString.of("10011011").toByteArrayRightAligned()).containsExactly(155);
        assertThat(BitString.of("11011100010110").toByteArrayRightAligned()).containsExactly(55, 22);
        assertThat(BitString.of("1011000101110110").toByteArrayRightAligned()).containsExactly(177, 118);
        assertThat(BitString.of("1011011011101001101").toByteArrayRightAligned()).containsExactly(5, 183, 77);
        assertThat(BitString.of("011").toByteArrayRightAligned()).containsExactly(3);
        assertThat(BitString.of("00010100000011").toByteArrayRightAligned()).containsExactly(5, 3);
        assertThat(BitString.of("0011011001000100011000").toByteArrayRightAligned()).containsExactly(13, 145, 24);
        assertThat(BitString.of(
                "1011001011101001100111010100011101011011010100011101010111010101" +
                "1101010110101000111010101101001001000111010100010101011110101011" +
                "101010").toByteArrayRightAligned()).containsExactly(
                44, -70, 103, 81, -42, -44, 117, 117, 117, 106, 58, -76, -111, -44, 85, -22, -22);
    }

    @Test
    void testToUnsignedBigInteger() {
        assertThat(BitString.empty().toUnsignedBigInteger()).isZero();
        assertThat(BitString.of("0").toUnsignedBigInteger()).isZero();
        assertThat(BitString.of("000").toUnsignedBigInteger()).isZero();
        assertThat(BitString.of("00000000").toUnsignedBigInteger()).isZero();
        assertThat(BitString.of("0000000000").toUnsignedBigInteger()).isZero();
        assertThat(BitString.of("0000000000000000").toUnsignedBigInteger()).isZero();
        assertThat(BitString.of("00000000000000000").toUnsignedBigInteger()).isZero();
        assertThat(BitString.of("1").toUnsignedBigInteger()).isEqualTo(1);
        assertThat(BitString.of("10").toUnsignedBigInteger()).isEqualTo(2);
        assertThat(BitString.of("10000").toUnsignedBigInteger()).isEqualTo(16);
        assertThat(BitString.of("10000000").toUnsignedBigInteger()).isEqualTo(128);
        assertThat(BitString.of("1000000000").toUnsignedBigInteger()).isEqualTo(512);
        assertThat(BitString.of("1000000000000000").toUnsignedBigInteger()).isEqualTo(32768);
        assertThat(BitString.of("1000000000000000000").toUnsignedBigInteger()).isEqualTo(262144);
        assertThat(BitString.of("11").toUnsignedBigInteger()).isEqualTo(3);
        assertThat(BitString.of("1011").toUnsignedBigInteger()).isEqualTo(11);
        assertThat(BitString.of("10011011").toUnsignedBigInteger()).isEqualTo(155);
        assertThat(BitString.of("11011100010110").toUnsignedBigInteger()).isEqualTo(14102);
        assertThat(BitString.of("1011000101110110").toUnsignedBigInteger()).isEqualTo(45430);
        assertThat(BitString.of("1011011011101001101").toUnsignedBigInteger()).isEqualTo(374605);
        assertThat(BitString.of("011").toUnsignedBigInteger()).isEqualTo(3);
        assertThat(BitString.of("00010100000011").toUnsignedBigInteger()).isEqualTo(1283);
        assertThat(BitString.of("0011011001000100011000").toUnsignedBigInteger()).isEqualTo(889112);
        assertThat(BitString.of(
                "1011001011101001100111010100011101011011010100011101010111010101" +
                "1101010110101000111010101101001001000111010100010101011110101011" +
                "101010").toUnsignedBigInteger()).isEqualTo("15220197018209473326918024313688311589610");
    }

}
