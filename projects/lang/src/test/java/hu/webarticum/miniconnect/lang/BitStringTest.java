package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(BitString.of(true, false, true).data()).containsExactly(5);
        assertThat(BitString.of(true, true, true, true, true).data()).containsExactly(31);
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
        assertThat(BitString.of(new byte[] { 125, -12 }).data()).containsExactly(62589);
        assertThat(BitString.of(new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }).data())
                .containsExactly(506097522914230528L, 2312);
        assertThat(BitString.of(new byte[] {
                0, 4, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 9, 0, 0, 0, 0,
                0, 0, 7, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 8, 0, 0, 0,
                1, 0, 0, 0, 0, 0, 0, 1,
        }).data())
                .containsExactly(1024, 150994944, 458752, 34359738368L, 72057594037927937L);
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
        assertThat(BitString.of(new long[] { 2432501L }, 7).data()).containsExactly(117);
        assertThat(BitString.of(new long[] { 2432501L }, 193).data()).containsExactly(2432501, 0, 0, 0);
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
    void testCompareTo() {
        assertThat(BitString.empty().compareTo(BitString.empty())).isZero();
        assertThat(BitString.empty().compareTo(BitString.of(false))).isZero();
        assertThat(BitString.of(false).compareTo(BitString.empty())).isZero();
        assertThat(BitString.of(true).compareTo(BitString.of(true))).isZero();
        assertThat(BitString.of(true).compareTo(BitString.of(true, false))).isZero();
        assertThat(BitString.of(true, false).compareTo(BitString.of(true))).isZero();
        assertThat(BitString.of(true, false).compareTo(BitString.of(true, false))).isZero();
        assertThat(BitString.of(new long[2]).compareTo(BitString.of(new long[5]))).isZero();
        assertThat(BitString.of(new long[4]).compareTo(BitString.of(new long[1]))).isZero();
        assertThat(BitString.of(new long[] { 7, 3 }).compareTo(BitString.of(new long[] { 7, 3 }))).isZero();
        assertThat(BitString.of(new long[] { 7, 3, 0 }).compareTo(BitString.of(new long[] { 7, 3, 0 }))).isZero();
        assertThat(BitString.of(new long[] { 7, 3, 0 }).compareTo(BitString.of(new long[] { 7, 3, 0, 0, 0 }))).isZero();
        assertThat(BitString.of(new long[] { 7, 3, 0 }).compareTo(BitString.of(new long[] { 7, 3 }))).isZero();
        assertThat(BitString.empty().compareTo(BitString.of(true))).isNegative();
        assertThat(BitString.of(true).compareTo(BitString.empty())).isPositive();
        assertThat(BitString.empty().compareTo(BitString.of(new long[] { 7, 3, 0 }))).isNegative();
        assertThat(BitString.of(new long[] { 7, 3, 0 }).compareTo(BitString.empty())).isPositive();
        assertThat(BitString.of(new long[] { 7, 3, 1 }).compareTo(BitString.of(new long[] { 7, 3, 3 }))).isNegative();
        assertThat(BitString.of(new long[] { 7, 3, 3 }).compareTo(BitString.of(new long[] { 7, 3, 1 }))).isPositive();
        assertThat(BitString.of(new long[] { 7, 3, 2 }).compareTo(BitString.of(new long[] { 7, 3, 1 }))).isNegative();
        assertThat(BitString.of(new long[] { 7, 3, 1 }).compareTo(BitString.of(new long[] { 7, 3, 2 }))).isPositive();
        assertThat(BitString.of(new long[] { 7, 3 }).compareTo(BitString.of(new long[] { 7, 3, 0, 5 }))).isNegative();
        assertThat(BitString.of(new long[] { 7, 3, 0, 5 }).compareTo(BitString.of(new long[] { 7, 3 }))).isPositive();
    }
    
        @Test
        void testIterator() {
            assertThat(BitString.empty().iterator()).isExhausted();
            assertThat((Iterable<Boolean>) BitString.of(false)).containsExactly(false);
            assertThat((Iterable<Boolean>) BitString.of(true, false)).containsExactly(true, false);
            assertThat((Iterable<Boolean>) BitString.of(new boolean[65])).hasSize(65).containsOnly(false);
            assertThat((Iterable<Boolean>) BitString.of(new long[] { 11 }, 5));
        }

}
 