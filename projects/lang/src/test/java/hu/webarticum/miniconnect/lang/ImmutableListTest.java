package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import org.junit.jupiter.api.Test;

class ImmutableListTest {

    @Test
    void testCreators() {
        assertThat(ImmutableList.empty().asList()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3).asList()).containsExactly(1, 2, 3);
        assertThat(ImmutableList.fromCollection(Arrays.asList(3, 1, 2)).asList()).containsExactly(3, 1, 2);
        assertThat(ImmutableList.fromIterable(Arrays.asList(5, 9 ,7)).asList()).containsExactly(5, 9 ,7);
        assertThat(ImmutableList.fromIterable(ImmutableList.of(4, 7, 3)).asList()).containsExactly(4, 7, 3);
        assertThat(ImmutableList.fromIterable(() -> Arrays.asList(9, 2).iterator()).asList()).containsExactly(9, 2);
        assertThat(ImmutableList.fromIterator(Arrays.asList(9, 1, 3).iterator()).asList()).containsExactly(9, 1, 3);
        assertThat(ImmutableList.fill(3, 4).asList()).containsExactly(4, 4, 4);
        assertThat(ImmutableList.fill(2, i -> (i  +1) + ".").asList()).containsExactly("1.", "2.");
    }
    
    @Test
    void testIsEmpty() {
        assertThat(ImmutableList.empty().isEmpty()).isTrue();
        assertThat(ImmutableList.of(1, 2, 3).isEmpty()).isFalse();
        assertThat(ImmutableList.fill(12, 0).isEmpty()).isFalse();
    }

    @Test
    void testSize() {
        assertThat(ImmutableList.empty().size()).isZero();
        assertThat(ImmutableList.of(1, 2, 3).size()).isEqualTo(3);
        assertThat(ImmutableList.fill(12, 0).size()).isEqualTo(12);
    }

    @Test
    void testGet() {
        assertThat(ImmutableList.empty()).satisfies(e -> assertThatThrownBy(() ->
                ((ImmutableList<?>) e).get(0)).isInstanceOf(IndexOutOfBoundsException.class));
        assertThat(ImmutableList.of(1, 2, 3)).satisfies(e -> assertThatThrownBy(() ->
                ((ImmutableList<?>) e).get(7)).isInstanceOf(IndexOutOfBoundsException.class));
        assertThat(ImmutableList.of(1, 2, 3).get(2)).isEqualTo(3);
    }

    @Test
    void testContains() {
        assertThat(ImmutableList.empty().contains(3)).isFalse();
        assertThat(ImmutableList.of(1, 2, 3).contains("lorem")).isFalse();
        assertThat(ImmutableList.of(1, 2, 3).contains(9)).isFalse();
        assertThat(ImmutableList.of(1, 2, 3).contains(2)).isTrue();
    }

    @Test
    void testContainsAll() {
        assertThat(ImmutableList.empty().containsAll(ImmutableList.empty())).isTrue();
        assertThat(ImmutableList.empty().containsAll(ImmutableList.of(3))).isFalse();
        assertThat(ImmutableList.of(1, 2, 3).containsAll(ImmutableList.of(3, "lorem"))).isFalse();
        assertThat(ImmutableList.of(1, 2, 3).containsAll(ImmutableList.of(2, 7))).isFalse();
        assertThat(ImmutableList.of(1, 2, 3).containsAll(ImmutableList.of(1, 3))).isTrue();
    }

    @Test
    void testContainsAllCollection() {
        assertThat(ImmutableList.empty().containsAll(Collections.emptyList())).isTrue();
        assertThat(ImmutableList.empty().containsAll(Arrays.asList(3))).isFalse();
        assertThat(ImmutableList.of(1, 2, 3).containsAll(Arrays.asList(3, "lorem"))).isFalse();
        assertThat(ImmutableList.of(1, 2, 3).containsAll(Arrays.asList(2, 7))).isFalse();
        assertThat(ImmutableList.of(1, 2, 3).containsAll(Arrays.asList(1, 3))).isTrue();
    }
    
    @Test
    void testIndexOf() {
        assertThat(ImmutableList.empty().indexOf(3)).isEqualTo(-1);
        assertThat(ImmutableList.of(1, 2, 3).indexOf(9)).isEqualTo(-1);
        assertThat(ImmutableList.of(1, 2, 3).indexOf(2)).isEqualTo(1);
        assertThat(ImmutableList.of(1, 2, 3, 4, 2, 7).indexOf(2)).isEqualTo(1);
    }
    
    @Test
    void testLastIndexOf() {
        assertThat(ImmutableList.empty().lastIndexOf(3)).isEqualTo(-1);
        assertThat(ImmutableList.of(1, 2, 3).lastIndexOf(9)).isEqualTo(-1);
        assertThat(ImmutableList.of(1, 2, 3).lastIndexOf(2)).isEqualTo(1);
        assertThat(ImmutableList.of(1, 2, 3, 4, 2, 7).lastIndexOf(2)).isEqualTo(4);
    }
    
    @Test
    void testMap() {
        assertThat(ImmutableList.empty().map(v -> ":" + v).asList()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3).map(v -> ":" + v).asList()).containsExactly(":1", ":2", ":3");
        assertThat(ImmutableList.of(1, null, 3).map(v -> ":" + v).asList()).containsExactly(":1", ":null", ":3");
    }

    @Test
    void testMapIndex() {
        assertThat(ImmutableList.empty().map((i, v) -> i + ":" + v).asList()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3).map((i, v) -> i + ":" + v).asList()).containsExactly("0:1", "1:2", "2:3");
        assertThat(ImmutableList.of(1, null, 3).map((i, v) -> i + ":" + v).asList())
                .containsExactly("0:1", "1:null", "2:3");
    }

    @Test
    void testFilter() {
        assertThat(ImmutableList.empty().filter(i -> false).asList()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3, 4).filter(i -> (i % 2) == 0).asList()).containsExactly(2, 4);
        assertThat(ImmutableList.of(1, 2, 3, 4).filter(i -> (i % 5) == 0).asList()).isEmpty();
    }

    @Test
    void testSection() {
        assertThat(ImmutableList.empty().section(0, 0).asList()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3, 4)).satisfies(e -> assertThatThrownBy(() ->
                ((ImmutableList<?>) e).section(3, 7)).isInstanceOf(IndexOutOfBoundsException.class));
        assertThat(ImmutableList.of(1, 2, 3, 4).section(1, 3).asList()).containsExactly(2, 3);
    }

    @Test
    void testConcat() {
        assertThat(ImmutableList.empty().concat(ImmutableList.empty()).asList()).isEmpty();
        assertThat(ImmutableList.empty().concat(ImmutableList.of(3, 4)).asList()).containsExactly(3, 4);
        assertThat(ImmutableList.of(1, 2).concat(ImmutableList.empty()).asList()).containsExactly(1, 2);
        assertThat(ImmutableList.of(1, 2).concat(ImmutableList.of(3, 4)).asList()).containsExactly(1, 2, 3, 4);
    }

    @Test
    void testConcatCollection() {
        assertThat(ImmutableList.empty().concat(Collections.emptyList()).asList()).isEmpty();
        assertThat(ImmutableList.of(1, 2).concat(Collections.emptyList()).asList()).containsExactly(1, 2);
        assertThat(ImmutableList.of(1, 2).concat(Arrays.asList(3, 4)).asList()).containsExactly(1, 2, 3, 4);
    }

    @Test
    void testAppend() {
        assertThat(ImmutableList.empty().append(1).asList()).containsExactly(1);
        assertThat(ImmutableList.<Integer>empty().append(null).asList()).containsExactly((Integer) null);
        assertThat(ImmutableList.of(1, 2).append(3)).containsExactly(1, 2, 3);
        assertThat(ImmutableList.of(1, 2).append(null)).containsExactly(1, 2, null);
    }

    @Test
    void testForEachIndexEmpty() {
        StringWriter writer = new StringWriter();
        ImmutableList.empty().forEachIndex((i, v) -> writer.write(i + ":" + v + ", "));
        assertThat(writer).hasToString("");
    }

    @Test
    void testForEachIndexNonEmpty() {
        StringWriter writer = new StringWriter();
        ImmutableList.of(1, 2, 3).forEachIndex((i, v) -> writer.write(i + ":" + v + ", "));
        assertThat(writer).hasToString("0:1, 1:2, 2:3, ");
    }

    @Test
    void testReverseOrder() {
        assertThat(ImmutableList.empty().reverseOrder()).isEmpty();
        assertThat(ImmutableList.empty().reverseOrder().reverseOrder()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3).reverseOrder()).containsExactly(3, 2, 1);
        assertThat(ImmutableList.of(1, 2, 3).reverseOrder().reverseOrder()).containsExactly(1, 2, 3);
    }

    @Test
    void testSort() {
        assertThat(ImmutableList.empty().sort()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3).sort()).containsExactly(1, 2, 3);
        assertThat(ImmutableList.of(7, 3, 5, 2, 4, 6, 2, 1, 3).sort()).containsExactly(1, 2, 2, 3, 3, 4, 5, 6, 7);
    }

    @Test
    void testSortComparator() {
        assertThat(ImmutableList.<Integer>empty().sort(Comparator.reverseOrder())).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3).sort(Comparator.reverseOrder())).containsExactly(3, 2, 1);
        assertThat(ImmutableList.of(7, 3, 5, 2, 4, 6, 2, 1, 3).sort(Comparator.reverseOrder()))
                .containsExactly(7, 6, 5, 4, 3, 3, 2, 2, 1);
    }

    @Test
    void testResize() {
        assertThat(ImmutableList.empty().resize(0, null).asList()).isEmpty();
        assertThat(ImmutableList.empty().resize(3, (Integer) null).asList()).containsExactly(null, null, null);
        assertThat(ImmutableList.empty().resize(3, 1).asList()).containsExactly(1, 1, 1);
        assertThat(ImmutableList.of(1, 2, 3, 4).resize(1, (Integer) null).asList()).containsExactly(1);
        assertThat(ImmutableList.of(1, 2, 3, 4).resize(4, 0).asList()).containsExactly(1, 2, 3, 4);
        assertThat(ImmutableList.of(1, 2, 3, 4).resize(5, (Integer) null).asList()).containsExactly(1, 2, 3, 4, null);
        assertThat(ImmutableList.of(1, 2, 3, 4).resize(6, 2).asList()).containsExactly(1, 2, 3, 4, 2, 2);
    }

    @Test
    void testResizeIntFunction() {
        assertThat(ImmutableList.empty().resize(0, i -> null).asList()).isEmpty();
        assertThat(ImmutableList.empty().resize(3, i -> null).asList()).containsExactly(null, null, null);
        assertThat(ImmutableList.empty().resize(3, i -> 1).asList()).containsExactly(1, 1, 1);
        assertThat(ImmutableList.of(1, 2, 3, 4).resize(1, i -> i).asList()).containsExactly(1);
        assertThat(ImmutableList.of(1, 2, 3, 4).resize(4, i -> i).asList()).containsExactly(1, 2, 3, 4);
        assertThat(ImmutableList.of(1, 2, 3, 4).resize(5, i -> null).asList()).containsExactly(1, 2, 3, 4, null);
        assertThat(ImmutableList.of(1, 2, 3, 4).resize(6, i -> i).asList()).containsExactly(1, 2, 3, 4, 4, 5);
    }

    @Test
    void testBinarySearch() {
        assertThat(ImmutableList.empty().binarySearch(0)).isEqualTo(-1);
        assertThat(ImmutableList.of(1, 2, 2, 3, 5, 7).binarySearch(3)).isEqualTo(3);
        assertThat(ImmutableList.of(1, 2, 2, 3, 5, 7).binarySearch(2)).isBetween(1, 2);
        assertThat(ImmutableList.of(1, 2, 2, 3, 5, 7).binarySearch(0)).isEqualTo(-1);
        assertThat(ImmutableList.of(1, 2, 2, 3, 5, 7).binarySearch(4)).isEqualTo(-5);
        assertThat(ImmutableList.of(1, 2, 2, 3, 5, 7).binarySearch(9)).isEqualTo(-7);
    }

    @Test
    void testBinarySearchComparator() {
        @SuppressWarnings("unchecked")
        Comparator<Integer> comparator = (Comparator<Integer>) (Comparator<?>) Comparator.naturalOrder().reversed();
        assertThat(ImmutableList.<Integer>empty().binarySearch(0, comparator)).isEqualTo(-1);
        assertThat(ImmutableList.of(9, 4, 2, 2, 1).binarySearch(4, comparator)).isEqualTo(1);
        assertThat(ImmutableList.of(9, 4, 2, 2, 1).binarySearch(2, comparator)).isBetween(2, 3);
        assertThat(ImmutableList.of(9, 4, 2, 2, 1).binarySearch(12, comparator)).isEqualTo(-1);
        assertThat(ImmutableList.of(9, 4, 2, 2, 1).binarySearch(6, comparator)).isEqualTo(-2);
        assertThat(ImmutableList.of(9, 4, 2, 2, 1).binarySearch(0, comparator)).isEqualTo(-6);
    }

    @Test
    void testStream() {
        assertThat(ImmutableList.empty().stream()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3).stream()).containsExactly(1, 2, 3);
        assertThat(ImmutableList.of(1, 2, null, null, 3).stream()).containsExactly(1, 2, null, null, 3);
    }

    @Test
    void testToArray() {
        assertThat(ImmutableList.empty().toArray()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3).toArray()).containsExactly(1, 2, 3);
        assertThat(ImmutableList.of(1, 2, null, null, 3).toArray()).containsExactly(1, 2, null, null, 3);
    }

    @Test
    void testAsList() {
        assertThat(ImmutableList.empty().asList()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3).asList()).containsExactly(1, 2, 3);
        assertThat(ImmutableList.of(1, 2, null, null, 3).asList()).containsExactly(1, 2, null, null, 3);
    }

    @Test
    void testToArrayList() {
        assertThat(ImmutableList.empty().toArrayList()).isEmpty();
        assertThat(ImmutableList.of(1, 2, 3).toArrayList()).containsExactly(1, 2, 3);
        assertThat(ImmutableList.of(1, 2, null, null, 3).toArrayList()).containsExactly(1, 2, null, null, 3);
    }

    @Test
    void testAssign() {
        assertThat(ImmutableList.empty().assign(v -> v)).isEqualTo(ImmutableMap.empty());
        assertThat(ImmutableList.of(1, 2, 3).assign(v -> v)).isEqualTo(ImmutableMap.of(1, 1, 2, 2, 3, 3));
        assertThat(ImmutableList.of(1, 2, null, null, 3).assign(v -> v))
                .isEqualTo(ImmutableMap.of(null, null, 1, 1, 2, 2, 3, 3));
        assertThat(ImmutableList.of(1, 2, 3).assign(v -> v + "")).isEqualTo(ImmutableMap.of(1, "1", 2, "2", 3, "3"));
        assertThat(ImmutableList.of(1, 2, null, null, 3).assign(v -> v + ""))
                .isEqualTo(ImmutableMap.of(null, "null", 1, "1", 2, "2", 3, "3"));
    }

    @Test
    void testAssignIndex() {
        assertThat(ImmutableList.empty().assign((v, i) -> v)).isEqualTo(ImmutableMap.empty());
        assertThat(ImmutableList.of(1, 2, 3).assign((v, i) -> v + i)).isEqualTo(ImmutableMap.of(1, 1, 2, 3, 3, 5));
        assertThat(ImmutableList.of(1, 2, null, null, 3).assign((v, i) -> v))
                .isEqualTo(ImmutableMap.of(null, null, 1, 1, 2, 2, 3, 3));
        assertThat(ImmutableList.of(1, 2, 3).assign((v, i) -> i + ":" + v))
                .isEqualTo(ImmutableMap.of(1, "0:1", 2, "1:2", 3, "2:3"));
        assertThat(ImmutableList.of(1, 2, null, null, 3).assign((v, i) -> i + ":" + v))
                .isEqualTo(ImmutableMap.of(null, "3:null", 1, "0:1", 2, "1:2", 3, "4:3"));
    }

    @Test
    void testHashCode() {
        assertThat(ImmutableList.empty()).hasSameHashCodeAs(Collections.emptyList());
        assertThat(ImmutableList.of(1, 2, 3)).hasSameHashCodeAs(Arrays.asList(1, 2, 3));
    }

    @Test
    void testEquals() {
        assertThat(ImmutableList.empty()).satisfies(c -> assertThat(c).isEqualTo(c));
        assertThat(ImmutableList.empty()).isNotEqualTo(new ArrayList<>());
        assertThat(ImmutableList.empty()).isNotEqualTo(null);
        assertThat(ImmutableList.empty()).isEqualTo(ImmutableList.empty());
        assertThat(ImmutableList.empty()).isNotEqualTo(ImmutableList.of(1, 2, 3));
        assertThat(ImmutableList.of(1, 2, 3)).isNotEqualTo(ImmutableList.empty());
        assertThat(ImmutableList.of(1, 2, 3)).isEqualTo(ImmutableList.of(1, 2, 3));
        assertThat(ImmutableList.of(1, 2, 3)).isNotEqualTo(ImmutableList.of(1, 2, 3, 4));
    }

    @Test
    void testToString() {
        assertThat(ImmutableList.empty()).hasToString(Collections.emptyList().toString());
        assertThat(ImmutableList.of(1, 2, 3)).hasToString(Arrays.asList(1, 2, 3).toString());
    }
    
    @Test
    void testCreateCollector() {
        assertThat(Arrays.asList().stream().collect(ImmutableList.createCollector())).isEqualTo(ImmutableList.empty());
        ImmutableList<String> immutableList1 = Arrays.asList(1, 4, 3, 6, 2, null, null, 3, null, 4).stream()
                .filter(v -> v != Integer.valueOf(4))
                .map(v -> v + "")
                .collect(ImmutableList.createCollector());
        assertThat(immutableList1).isEqualTo(ImmutableList.of("1", "3", "6", "2", "null", "null", "3", "null"));
        ImmutableList<String> immutableList2 = Arrays.asList(1, 4, 3, 6, 2, null, null, 3, null, 4).parallelStream()
                .filter(v -> v != Integer.valueOf(4))
                .map(v -> v + "")
                .collect(ImmutableList.createCollector());
        assertThat(immutableList2).isEqualTo(ImmutableList.of("1", "3", "6", "2", "null", "null", "3", "null"));
    }
    
}
