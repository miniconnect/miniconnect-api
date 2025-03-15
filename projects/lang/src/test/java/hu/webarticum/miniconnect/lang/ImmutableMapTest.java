package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;

class ImmutableMapTest {

    @Test
    void testCreators() {
        assertThat(ImmutableMap.empty().asMap()).isEmpty();
        assertThat(ImmutableMap.of(1, "a").asMap()).isEqualTo(map(entry(1, "a")));
        assertThat(ImmutableMap.of(null, null).asMap()).isEqualTo(map(entry(null, null)));
        assertThat(ImmutableMap.of(1, null).asMap()).isEqualTo(map(entry(1, null)));
        assertThat(ImmutableMap.of(null, "a").asMap()).isEqualTo(map(entry(null, "a")));
        assertThat(ImmutableMap.of(1, "a", 2, "b").asMap()).isEqualTo(map(entry(1, "a"), entry(2, "b")));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").asMap())
                .isEqualTo(map(entry(1, "a"), entry(2, "b"), entry(3, "c")));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c", 4, "d").asMap())
                .isEqualTo(map(entry(1, "a"), entry(2, "b"), entry(3, "c"), entry(4, "d")));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e").asMap())
                .isEqualTo(map(entry(1, "a"), entry(2, "b"), entry(3, "c"), entry(4, "d"), entry(5, "e")));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e", 6, "f").asMap())
                .isEqualTo(map(
                        entry(1, "a"), entry(2, "b"), entry(3, "c"), entry(4, "d"), entry(5, "e"), entry(6, "f")));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e", 6, "f", 7, "g").asMap())
                .isEqualTo(map(
                        entry(1, "a"), entry(2, "b"), entry(3, "c"), entry(4, "d"), entry(5, "e"),
                        entry(6, "f"), entry(7, "g")));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e", 6, "f", 7, "g", 8, "h").asMap())
                .isEqualTo(map(
                        entry(1, "a"), entry(2, "b"), entry(3, "c"), entry(4, "d"), entry(5, "e"),
                        entry(6, "f"), entry(7, "g"), entry(8, "h")));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c", 4, "d", 5, "e", 6, "f", 7, "g", 8, "h", 9, "i").asMap())
                .isEqualTo(map(
                        entry(1, "a"), entry(2, "b"), entry(3, "c"), entry(4, "d"), entry(5, "e"),
                        entry(6, "f"), entry(7, "g"), entry(8, "h"), entry(9, "i")));
        assertThat(ImmutableMap.of(
                1, "a", 2, "b", 3, "c", 4, "d", 5, "e", 6, "f", 7, "g", 8, "h", 9, "i", 10, "j").asMap())
                .isEqualTo(map(
                        entry(1, "a"), entry(2, "b"), entry(3, "c"), entry(4, "d"), entry(5, "e"),
                        entry(6, "f"), entry(7, "g"), entry(8, "h"), entry(9, "i"), entry(10, "j")));
        assertThat(ImmutableMap.ofClazzes(
                Integer.class, String.class,
                1, "a", 2, "b", 3, "c", 4, "d", 5, "e",
                6, "f", 7, "g", 8, "h", 9, "i", 10, "j",
                11, "k", 12, "l", 13, "m", 14, "n", 15, "o").asMap())
                .isEqualTo(map(
                        entry(1, "a"), entry(2, "b"), entry(3, "c"), entry(4, "d"), entry(5, "e"),
                        entry(6, "f"), entry(7, "g"), entry(8, "h"), entry(9, "i"), entry(10, "j"),
                        entry(11, "k"), entry(12, "l"), entry(13, "m"), entry(14, "n"), entry(15, "o")));
        assertThat(ImmutableMap.fromMap(
                map(entry(1, "a"), entry(2, "b"), entry(3, "c"), entry(4, "d"), entry(5, "e"))).asMap())
                .isEqualTo(map(entry(1, "a"), entry(2, "b"), entry(3, "c"), entry(4, "d"), entry(5, "e")));
        assertThat(ImmutableMap.assignFrom(Arrays.asList(1, 2, 3), 3, k -> k + ".").asMap()).isEqualTo(
                map(entry(1, "1."), entry(2, "2."), entry(3, "3.")));
        assertThat(ImmutableMap.assignFrom(Arrays.asList(1, 2, 3), 3, (k, i) -> i + ":" + k).asMap()).isEqualTo(
                map(entry(1, "0:1"), entry(2, "1:2"), entry(3, "2:3")));
    }

    @Test
    void testIsEmpty() {
        assertThat(ImmutableMap.empty().isEmpty()).isTrue();
        assertThat(ImmutableMap.of(null, null).isEmpty()).isFalse();
        assertThat(ImmutableMap.of(1, null).isEmpty()).isFalse();
        assertThat(ImmutableMap.of(null, "a").isEmpty()).isFalse();
        assertThat(ImmutableMap.of(1, "a").isEmpty()).isFalse();
        assertThat(ImmutableMap.of(1, "a", 2, "b").isEmpty()).isFalse();
    }

    @Test
    void testSize() {
        assertThat(ImmutableMap.empty().size()).isZero();
        assertThat(ImmutableMap.of(null, null).size()).isEqualTo(1);
        assertThat(ImmutableMap.of(1, null).size()).isEqualTo(1);
        assertThat(ImmutableMap.of(null, "a").size()).isEqualTo(1);
        assertThat(ImmutableMap.of(1, "a").size()).isEqualTo(1);
        assertThat(ImmutableMap.of(1, "a", 2, "b").size()).isEqualTo(2);
    }

    @Test
    void testContainsKey() {
        assertThat(ImmutableMap.empty().containsKey(null)).isFalse();
        assertThat(ImmutableMap.empty().containsKey(1)).isFalse();
        assertThat(ImmutableMap.of(1, "a").containsKey(null)).isFalse();
        assertThat(ImmutableMap.of(1, "a").containsKey(3)).isFalse();
        assertThat(ImmutableMap.of(1, "a").containsKey(1)).isTrue();
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").containsKey(7)).isFalse();
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").containsKey(2)).isTrue();
    }

    @Test
    void testKeySet() {
        assertThat(ImmutableMap.empty().keySet()).isEmpty();
        assertThat(ImmutableMap.of((Integer) null, null).keySet()).containsExactly((Integer) null);
        assertThat(ImmutableMap.of(1, null).keySet()).containsExactly(1);
        assertThat(ImmutableMap.of((Integer) null, "a").keySet()).containsExactly((Integer) null);
        assertThat(ImmutableMap.of(1, "a").keySet()).containsExactly(1);
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, "c").keySet()).containsExactlyInAnyOrder(1, null, 3);
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").keySet()).containsExactlyInAnyOrder(1, 2, 3);
    }

    @Test
    void testKeys() {
        assertThat(ImmutableMap.empty().keys()).isEqualTo(ImmutableList.empty());
        assertThat(ImmutableMap.of((Integer) null, null).keys()).isEqualTo(ImmutableList.of((Integer) null));
        assertThat(ImmutableMap.of(1, null).keys()).isEqualTo(ImmutableList.of(1));
        assertThat(ImmutableMap.of((Integer) null, "a").keys()).isEqualTo(ImmutableList.of((Integer) null));
        assertThat(ImmutableMap.of(1, "a").keys()).isEqualTo(ImmutableList.of(1));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, "c").keys()).containsExactlyInAnyOrder(1, null, 3);
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").keys()).containsExactlyInAnyOrder(1, 2, 3);
    }

    @Test
    void testValues() {
        assertThat(ImmutableMap.empty().values()).isEqualTo(ImmutableList.empty());
        assertThat(ImmutableMap.of((Integer) null, null).values()).isEqualTo(ImmutableList.of((Integer) null));
        assertThat(ImmutableMap.of(1, (String) null).values()).isEqualTo(ImmutableList.of((String) null));
        assertThat(ImmutableMap.of((Integer) null, "a").values()).isEqualTo(ImmutableList.of("a"));
        assertThat(ImmutableMap.of(1, "a").values()).isEqualTo(ImmutableList.of("a"));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, "c").values()).containsExactlyInAnyOrder("a", "b", "c");
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").values()).containsExactlyInAnyOrder("a", "b", "c");
    }

    @Test
    void testGet() {
        assertThat(ImmutableMap.empty().get(1)).isNull();
        assertThat(ImmutableMap.empty().get(null)).isNull();
        assertThat(ImmutableMap.of((Integer) null, null).get(1)).isNull();
        assertThat(ImmutableMap.of((Integer) null, null).get(null)).isNull();
        assertThat(ImmutableMap.of(1, (String) null).get(1)).isNull();
        assertThat(ImmutableMap.of((Integer) null, "a").get(null)).isEqualTo("a");
        assertThat(ImmutableMap.of(1, "a").get(1)).isEqualTo("a");
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, "c").get(1)).isEqualTo("a");
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, "c").get(null)).isEqualTo("b");
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").get(7)).isNull();
    }

    @Test
    void testGetOrDefault() {
        assertThat(ImmutableMap.empty().getOrDefault(1, null)).isNull();
        assertThat(ImmutableMap.empty().getOrDefault(1, "x")).isEqualTo("x");
        assertThat(ImmutableMap.of((Integer) null, null).getOrDefault(null, "x")).isNull();
        assertThat(ImmutableMap.of((Integer) null, null).getOrDefault(1, "x")).isEqualTo("x");
        assertThat(ImmutableMap.of(1, "a").getOrDefault(1, "x")).isEqualTo("a");
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).getOrDefault(1, "x")).isEqualTo("a");
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).getOrDefault(3, "x")).isNull();
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).getOrDefault(null, "x")).isEqualTo("b");
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).getOrDefault(7, "x")).isEqualTo("x");
    }

    @Test
    void testEntrySet() {
        assertThat(ImmutableMap.empty().entrySet()).isEmpty();
        assertThat(ImmutableMap.of((Integer) null, null).entrySet()).containsExactly(entry(null, null));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).entrySet())
                .containsExactlyInAnyOrder(entry(1, "a"), entry(null, "b"), entry(3, null));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").entrySet())
                .containsExactlyInAnyOrder(entry(1, "a"), entry(2, "b"), entry(3, "c"));
    }

    @Test
    void testMap() {
        assertThat(ImmutableMap.empty().map(k -> k, v -> v).asMap()).isEmpty();
        assertThat(ImmutableMap.of(null, null).map(k -> k, v -> v).asMap()).containsExactly(entry(null, null));
        assertThat(ImmutableMap.of(1, "a").map(k -> null, v -> null).asMap()).containsExactly(entry(null, null));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).map(k -> k, v -> v).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(null, "b"), entry(3, null)));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).map(k -> ":" + k, v -> ":" + v).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(":1", ":a"), entry(":null", ":b"), entry(":3", ":null")));
    }

    @Test
    void testMapCross() {
        assertThat(ImmutableMap.empty().map((k, v) -> k, (k, v) -> v).asMap()).isEmpty();
        assertThat(ImmutableMap.of(null, null).map((k, v) -> k, (k, v) -> v).asMap())
                .containsExactly(entry(null, null));
        assertThat(ImmutableMap.of(1, "a").map((k, v) -> null, (k, v) -> null).asMap())
                .containsExactly(entry(null, null));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).map((k, v) -> k, (k, v) -> v).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(null, "b"), entry(3, null)));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).map((k, v) -> ":" + k, (k, v) -> k + ":" + v).asMap())
                .containsExactlyInAnyOrderEntriesOf(
                        map(entry(":1", "1:a"), entry(":null", "null:b"), entry(":3", "3:null")));
        assertThat(ImmutableMap.of(1, "a", 2, "b").map((k, v) -> k + "->" + v, (k, v) -> v + "<-" + k).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry("1->a", "a<-1"), entry("2->b", "b<-2")));
    }

    @Test
    void testMapValues() {
        assertThat(ImmutableMap.empty().mapValues((k, v) -> v).asMap()).isEmpty();
        assertThat(ImmutableMap.of(null, null).mapValues((k, v) -> v).asMap())
                .containsExactly(entry(null, null));
        assertThat(ImmutableMap.of(1, "a").mapValues((k, v) -> null).asMap())
                .containsExactly(entry(1, null));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).mapValues((k, v) -> v).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(null, "b"), entry(3, null)));
        
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).mapValues((k, v) -> k + ":" + v).asMap())
                .containsExactlyInAnyOrderEntriesOf(
                        map(entry(1, "1:a"), entry(null, "null:b"), entry(3, "3:null")));
    }

    @Test
    void testForEachEmpty() {
        Map<Object, Object> map = new HashMap<>();
        ImmutableMap.empty().forEach(map::put);
        assertThat(map).isEmpty();
    }

    @Test
    void testForEachNonEmpty() {
        Map<Object, Object> map = new HashMap<>();
        ImmutableMap.of(1, "a", 2, "b", 3, "c").forEach(map::put);
        assertThat(map).containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(2, "b"), entry(3, "c")));
    }

    @Test
    void testFilter() {
        assertThat(ImmutableMap.empty().filter(k -> k != null).asMap()).isEmpty();
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").filter(k -> (k % 2) == 1).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(3, "c")));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, "c", null, "d", null, "e").filter(k -> k == null).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(null, "b"), entry(null, "d"), entry(null, "e")));
    }

    @Test
    void testFilterCross() {
        assertThat(ImmutableMap.empty().filter((k, v) -> k != v).asMap()).isEmpty();
        assertThat(ImmutableMap.of(1, "a").filter((k, v) -> k != null).asMap()).containsExactly(entry(1, "a"));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).filter((k, v) -> k != null).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(3, null)));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).filter((k, v) -> v != null).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(null, "b")));
        assertThat(ImmutableMap.of("a", "a", "b", "B", null, null, "d", "d", "e", null, "F", "f")
                .filter((k, v) -> Objects.equals(k, v)).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry("a", "a"), entry(null, null), entry("d", "d")));
    }

    @Test
    void testMerge() {
        assertThat(ImmutableMap.empty().merge(ImmutableMap.empty()).asMap()).isEmpty();
        assertThat(ImmutableMap.empty().merge(ImmutableMap.of(1, "a", null, null)).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(null, null)));
        assertThat(ImmutableMap.of(1, "a", null, null).merge(ImmutableMap.empty()).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(null, null)));
        assertThat(ImmutableMap.of(1, "a", null, null).merge(ImmutableMap.of(1, "a", null, null)).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(null, null)));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, "c", 4, "d")
                .merge(ImmutableMap.of(2, "x", 4, "y", null, null, 9, "z")).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(
                        entry(1, "a"), entry(2, "x"), entry(null, null), entry(3, "c"), entry(4, "y"), entry(9, "z")));
    }

    @Test
    void testAsMap() {
        assertThat(ImmutableMap.empty().asMap()).isEmpty();
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(2, "b"), entry(3, "c")));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).asMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(null, "b"), entry(3, null)));
    }

    @Test
    void testToHashMap() {
        assertThat(ImmutableMap.empty().toHashMap()).isEmpty();
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c").toHashMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(2, "b"), entry(3, "c")));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null).toHashMap())
                .containsExactlyInAnyOrderEntriesOf(map(entry(1, "a"), entry(null, "b"), entry(3, null)));
    }

    @Test
    void testHashCode() {
        assertThat(ImmutableMap.empty()).hasSameHashCodeAs(Collections.emptyMap());
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c"))
                .hasSameHashCodeAs(map(entry(1, "a"), entry(2, "b"), entry(3, "c")));
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null))
                .hasSameHashCodeAs(map(entry(1, "a"), entry(null, "b"), entry(3, null)));
    }

    @Test
    void testEquals() {
        assertThat(ImmutableMap.empty()).isEqualTo(ImmutableMap.empty());
        assertThat(ImmutableMap.empty()).isNotEqualTo(null);
        assertThat(ImmutableMap.empty()).isNotEqualTo(Collections.emptyMap());
        assertThat(ImmutableMap.empty()).isNotEqualTo(map(entry(1, "a")));
        assertThat(ImmutableMap.empty()).isNotEqualTo(ImmutableMap.of(null, null));
        assertThat(ImmutableMap.of(null, null)).isNotEqualTo(ImmutableMap.empty());
        assertThat(ImmutableMap.empty()).isNotEqualTo(ImmutableMap.of(1, "a", 2, "b", 3, "c"));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c")).isNotEqualTo(ImmutableMap.empty());
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c")).isEqualTo(ImmutableMap.of(1, "a", 2, "b", 3, "c"));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c"))
                .isNotEqualTo(map(entry(1, "a"), entry(2, "b"), entry(3, "c")));
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c")).isNotEqualTo(ImmutableMap.of(1, "a", 3, "c"));
        assertThat(ImmutableMap.of(1, "a", 3, "c")).isNotEqualTo(ImmutableMap.of(1, "a", 2, "b", 3, "c"));
    }

    @Test
    void testToString() {
        assertThat(ImmutableMap.empty()).hasToString(Collections.emptyMap().toString());
        assertThat(ImmutableMap.of(1, "a", 2, "b", 3, "c"))
                .hasToString(map(entry(1, "a"), entry(2, "b"), entry(3, "c")).toString());
        assertThat(ImmutableMap.of(1, "a", null, "b", 3, null))
                .hasToString(map(entry(1, "a"), entry(null, "b"), entry(3, null)).toString());
    }

    private static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }
    
    @SafeVarargs
    private static <K, V> Map<K, V> map(Map.Entry<K, V>... entries) {
        Map<K, V> result = new HashMap<>();
        for (Map.Entry<K, V> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    
}
