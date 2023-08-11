package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class ReversibleIterableTest {
    
    @Test
    void testEmpty() {
        ReversibleIterable<Integer> reversibleIterable = ReversibleIterable.empty();
        assertThat(reversibleIterable).isEmpty();
        assertThat(reversibleIterable.reverseOrder()).isEmpty();
        assertThat(reversibleIterable.reverseOrder().reverseOrder()).isEmpty();
    }

    @Test
    void testSomeElements() {
        ReversibleIterable<Integer> reversibleIterable = ReversibleIterable.of(
                Arrays.asList(4, 2, 6, 6, 7, 0, 2),
                Arrays.asList(2, 0, 7, 6, 6, 2, 4));
        assertThat(reversibleIterable).containsExactly(4, 2, 6, 6, 7, 0, 2);
        assertThat(reversibleIterable.reverseOrder()).containsExactly(2, 0, 7, 6, 6, 2, 4);
        assertThat(reversibleIterable.reverseOrder().reverseOrder()).containsExactly(4, 2, 6, 6, 7, 0, 2);
    }

    @Test
    void testReversedOfReference() {
        ReversibleIterable<Integer> original = ReversibleIterable.of(
                Arrays.asList(4, 2, 6, 6, 7, 0, 2),
                Arrays.asList(2, 0, 7, 6, 6, 2, 4));
        ReversibleIterable<Integer> reversed = ReversibleIterable.reversedOfReference(
                Arrays.asList(2, 0, 7, 6, 6, 2, 4), original);
        assertThat(reversed).containsExactly(2, 0, 7, 6, 6, 2, 4);
        assertThat(reversed.reverseOrder()).isSameAs(original).containsExactly(4, 2, 6, 6, 7, 0, 2);
    }

}
