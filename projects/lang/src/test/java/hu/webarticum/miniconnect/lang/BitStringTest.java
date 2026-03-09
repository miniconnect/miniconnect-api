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
    }

    @Test
    void testIsEmpty() {
        assertThat(BitString.empty().isEmpty()).isTrue();
        assertThat(BitString.of(false).isEmpty()).isFalse();
        assertThat(BitString.of(true).isEmpty()).isFalse();
        assertThat(BitString.of(new long[] { 4160867386445L }, 0).isEmpty()).isTrue();
        assertThat(BitString.of(new long[] { 4160867386445L }, 10).isEmpty()).isFalse();
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
    }

    @Test
    void testIterator() {
        assertThat(BitString.empty().iterator()).isExhausted();
        assertThat((Iterable<Boolean>) BitString.of(false)).containsExactly(false);
        assertThat((Iterable<Boolean>) BitString.of(true, false)).containsExactly(true, false);
        assertThat((Iterable<Boolean>) BitString.of(new boolean[65])).hasSize(65).containsOnly(false);
        assertThat((Iterable<Boolean>) BitString.of(new long[] { -7493989779944505344L }, 5)).containsExactly(true, false, false, true, true);
    }

    @Test
    void testHashCode() {
        assertThat((Object) BitString.of(false)).hasSameHashCodeAs(BitString.of(new long[1], 1));
        assertThat((Object) BitString.of(true)).hasSameHashCodeAs(BitString.of(new long[] { -9223372036854775808L }, 1));
        assertThat((Object) BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L }, 77))
                .hasSameHashCodeAs(BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L, 4611278346822072L }, 77));
    }

    @Test
    void testEquals() {
        assertThat((Object) BitString.of(false)).isEqualTo(BitString.of(new long[1], 1));
        assertThat((Object) BitString.of(true)).isEqualTo(BitString.of(new long[] { -9223372036854775808L }, 1));
        assertThat((Object) BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L }, 77))
                .isEqualTo(BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L, 4611278346822072L }, 77));
    }

    @Test
    void testEqualsFalse() {
        assertThat((Object) BitString.empty()).isNotEqualTo(BitString.of(false));
        assertThat((Object) BitString.of(false)).isNotEqualTo(BitString.empty());
        assertThat((Object) BitString.of(true)).isNotEqualTo(BitString.empty());
        assertThat((Object) BitString.of(new long[1])).isNotEqualTo(BitString.of(new long[2]));
        assertThat((Object) BitString.of(new long[] { -5031317168539249322L, 3983041279643227983L }))
                .isNotEqualTo(BitString.of(new long[] { -5031317168539249322L, 3983041279643227984L }));
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
    }

    @Test
    void testGet() {
        assertThat(BitString.of(false).get(0)).isFalse();
        assertThat(BitString.of(true).get(0)).isTrue();
        assertThat(BitString.of(true, false, true, true, false, true, false, false).get(5)).isTrue();
        assertThat(BitString.of(new long[] { -3248317512688145487L, 4245054622059724800L }, 92).get(17)).isFalse();
        assertThat(BitString.of(new long[] { -3248317512688145487L, 4245054622059724800L }, 92).get(67)).isTrue();
        assertThat(BitString.empty().get(-1)).isFalse();
        assertThat(BitString.empty().get(0)).isFalse();
        assertThat(BitString.of(true).get(1)).isFalse();
        assertThat(BitString.of(new long[2]).get(130)).isFalse();
    }

    @Test
    void testGetStrict() {
        assertThat(BitString.of(false).getStrict(0)).isFalse();
        assertThat(BitString.of(true).getStrict(0)).isTrue();
        assertThat(BitString.of(true, false, true, true, false, true, false, false).getStrict(5)).isTrue();
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
        assertThat((Object) BitString.of(false).set(0, true)).isEqualTo(BitString.of(true));
        assertThat((Object) BitString.of(false).set(2, true)).isEqualTo(BitString.of(false, false, true));
        assertThat((Object) BitString.of(true).set(0, false)).isEqualTo(BitString.of(false));
        assertThat((Object) BitString.of(true).set(5, false)).isEqualTo(BitString.of(true, false, false, false, false, false));
        assertThat((Object) BitString.of(true, false, true, true, false, true, false, false).set(3, true))
                .isEqualTo(BitString.of(true, false, true, true, false, true, false, false));
        assertThat((Object) BitString.of(true, false, true, true, false, true, false, false).set(3, false))
                .isEqualTo(BitString.of(true, false, true, false, false, true, false, false));
        assertThat((Object) BitString.of(true, false, true, true, false, true, false, false).set(11, true))
                .isEqualTo(BitString.of(true, false, true, true, false, true, false, false, false, false, false, true));
    }

    @Test
    void testSetNegative() {
        assertThatThrownBy(() -> BitString.empty().set(-1, false)).isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testSetStrict() {
        assertThat((Object) BitString.of(false).setStrict(0, true)).isEqualTo(BitString.of(true));
        assertThat((Object) BitString.of(true).setStrict(0, false)).isEqualTo(BitString.of(false));
        assertThat((Object) BitString.of(true, false, true, true, false, true, false, false).setStrict(3, true))
                .isEqualTo(BitString.of(true, false, true, true, false, true, false, false));
        assertThat((Object) BitString.of(true, false, true, true, false, true, false, false).setStrict(3, false))
                .isEqualTo(BitString.of(true, false, true, false, false, true, false, false));
    }

    @Test
    void testSetStrictOutOfBounds() {
        assertThatThrownBy(() -> BitString.empty().setStrict(-1, false)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.empty().setStrict(0, false)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.empty().setStrict(1, false)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.of(new long[1]).setStrict(71, false)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> BitString.of(new long[1], 3).setStrict(22, false)).isInstanceOf(IndexOutOfBoundsException.class);
    }

}
