package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class BitStringTest {

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
        }).data())
                .containsExactly(
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
    void testOfLongsWithSizeAndData() {
        assertThat(BitString.of(new long[0], 0).data()).isEmpty();
        assertThat(BitString.of(new long[0], 2).data()).containsExactly(0);
        assertThat(BitString.of(new long[2], 128).data()).containsExactly(0, 0);
        assertThat(BitString.of(new long[1], 140).data()).containsExactly(0, 0, 0);
        assertThat(BitString.of(new long[10], 65).data()).containsExactly(0, 0);
        assertThat(BitString.of(new long[] { 2432501L }, 64).data()).containsExactly(2432501);
        assertThat(BitString.of(new long[] { 7756231263636641610L }, 11).data()).containsExactly(7755198558331994112L);
        assertThat(BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L }, 36).data()).containsExactly(-5031317168688463872L);
        assertThat(BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L }, 77).data()).containsExactly(-5031317168539249322L, 3981182070595518464L);
        assertThat(BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L }, 193).data()).containsExactly(-5031317168539249322L, 3983041279643227983L, 0, 0);
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
    void testSize() {
        assertThat(BitString.empty().size()).isZero();
        assertThat(BitString.of(new boolean[0]).size()).isZero();
        assertThat(BitString.of(false).size()).isOne();
        assertThat(BitString.of(false).size()).isOne();
        assertThat(BitString.of(true).size()).isOne();
        assertThat(BitString.of(true, false).size()).isEqualTo(2);
        assertThat(BitString.of(new boolean[65]).size()).isEqualTo(65);
        assertThat(BitString.of(new byte[0]).size()).isZero();
        assertThat(BitString.of(new byte[3]).size()).isEqualTo(24);
        assertThat(BitString.of(new byte[20]).size()).isEqualTo(160);
        assertThat(BitString.of(new long[0]).size()).isZero();
        assertThat(BitString.of(new long[2]).size()).isEqualTo(128);
        assertThat(BitString.of(new long[0], 0).size()).isZero();
        assertThat(BitString.of(new long[0], 3).size()).isEqualTo(3);
        assertThat(BitString.of(new long[0], 173).size()).isEqualTo(173);
        assertThat(BitString.of("000000000").size()).isEqualTo(9);
        assertThat(BitString.of("11000100011010101111101010100001010111010101010010110001010101110000101011").size()).isEqualTo(74);
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
        assertThat((Iterable<Boolean>) BitString.of(new long[] { -7493989779944505344L }, 5)).containsExactly(true, false, false, true, true);
        assertThat((Iterable<Boolean>) BitString.of("110100111010"))
                .containsExactly(true, true, false, true, false, false, true, true, true, false, true, false);
        assertThat((Iterable<Boolean>) BitString.of("0011101101011000"))
                .containsExactly(false, false, true, true, true, false, true, true, false, true, false, true, true, false, false, false);
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
        assertThat((Object) BitString.of(new long[] { -3248317512688145487L, 4245054622059724800L }, 92))
                .hasToString("11010010111010111010100001101110101110101000101011101011101100010011101011101001011101101011");
        assertThat((Object) BitString.of(new long[] { -3248317512688145487L, 4245054622059724800L }, 100))
                .hasToString("1101001011101011101010000110111010111010100010101110101110110001001110101110100101110110101100000000");
        assertThat((Object) BitString.of("100010111011")).hasToString("100010111011");
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
        assertThat((Object) BitString.empty().resize(67))
                .isEqualTo(BitString.of("0000000000000000000000000000000000000000000000000000000000000000000"));
        assertThat((Object) BitString.of("1101001").resize(67))
                .isEqualTo(BitString.of("1101001000000000000000000000000000000000000000000000000000000000000"));
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
    void testGet() {
        assertThat(BitString.of("0").get(0)).isFalse();
        assertThat(BitString.of("1").get(0)).isTrue();
        assertThat(BitString.of("10110100").get(5)).isTrue();
        assertThat(BitString.of("100101101100010011101000101100101001001100011110100111101000111100111010").get(68)).isTrue();
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
        assertThat(BitString.of("100101101100010011101000101100101001001100011110100111101000111100111010").getStrict(68)).isTrue();
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
        assertThat((Object) BitString.of("0").set(2, true)).isEqualTo(BitString.of("001"));
        assertThat((Object) BitString.of("1").set(0, false)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("1").set(5, false)).isEqualTo(BitString.of("100000"));
        assertThat((Object) BitString.of("10110100").set(3, true)).isEqualTo(BitString.of("10110100"));
        assertThat((Object) BitString.of("10110100").set(3, false)).isEqualTo(BitString.of("10100100"));
        assertThat((Object) BitString.of("10110100").set(11, true)).isEqualTo(BitString.of("101101000001"));
    }

    @Test
    void testSetNegative() {
        assertThatThrownBy(() -> BitString.empty().set(-1, false)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testSetStrict() {
        assertThat((Object) BitString.of("0").setStrict(0, true)).isEqualTo(BitString.of("1"));
        assertThat((Object) BitString.of("1").setStrict(0, false)).isEqualTo(BitString.of("0"));
        assertThat((Object) BitString.of("10110100").setStrict(3, true)).isEqualTo(BitString.of("10110100"));
        assertThat((Object) BitString.of("10110100").setStrict(3, false)).isEqualTo(BitString.of("10100100"));
    }

    @Test
    void testSetStrictOutOfBounds() {
        assertThatThrownBy(() -> BitString.empty().setStrict(-1, false)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.empty().setStrict(0, false)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.empty().setStrict(1, false)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.of(new long[1]).setStrict(71, false)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.of(new long[1], 3).setStrict(22, false)).isInstanceOf(IndexOutOfBoundsException.class);
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

}
