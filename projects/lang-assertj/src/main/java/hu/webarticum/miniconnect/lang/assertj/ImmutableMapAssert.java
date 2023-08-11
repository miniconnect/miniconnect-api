package hu.webarticum.miniconnect.lang.assertj;

import static org.assertj.core.extractor.Extractors.extractedDescriptionOf;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.Condition;
import org.assertj.core.api.EnumerableAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.IntegerAssert;
import org.assertj.core.data.MapEntry;
import org.assertj.core.description.Description;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.Maps;
import org.assertj.core.util.VisibleForTesting;

import hu.webarticum.miniconnect.lang.ImmutableList;
import hu.webarticum.miniconnect.lang.ImmutableMap;

public class ImmutableMapAssert<K, V> // NOSONAR equals
        extends AbstractObjectAssert<ImmutableMapAssert<K, V>, ImmutableMap<K, V>>
        implements EnumerableAssert<ImmutableMapAssert<K, V>, Map.Entry<K, V>> {

    @VisibleForTesting
    Maps maps = Maps.instance();

    
    public ImmutableMapAssert(ImmutableMap<K, V> actual) {
        super(actual, ImmutableMapAssert.class);
    }


    @Override
    public void isNullOrEmpty() {
        maps.assertNullOrEmpty(info, actualAsMap());
    }

    @Override
    public void isEmpty() {
        maps.assertEmpty(info, actualAsMap());
    }

    @Override
    public ImmutableMapAssert<K, V> isNotEmpty() {
        maps.assertNotEmpty(info, actualAsMap());
        return myself;
    }

    @Override
    public ImmutableMapAssert<K, V> hasSize(int expected) {
        maps.assertHasSize(info, actualAsMap(), expected);
        return myself;
    }

    @Override
    public ImmutableMapAssert<K, V> hasSizeGreaterThan(int boundary) {
        maps.assertHasSizeGreaterThan(info, actualAsMap(), boundary);
        return myself;
    }

    @Override
    public ImmutableMapAssert<K, V> hasSizeGreaterThanOrEqualTo(int boundary) {
        maps.assertHasSizeGreaterThanOrEqualTo(info, actualAsMap(), boundary);
        return myself;
    }

    @Override
    public ImmutableMapAssert<K, V> hasSizeLessThan(int boundary) {
        maps.assertHasSizeLessThan(info, actualAsMap(), boundary);
        return myself;
    }

    @Override
    public ImmutableMapAssert<K, V> hasSizeLessThanOrEqualTo(int boundary) {
        maps.assertHasSizeLessThanOrEqualTo(info, actualAsMap(), boundary);
        return myself;
    }

    @Override
    public ImmutableMapAssert<K, V> hasSizeBetween(int lowerBoundary, int higherBoundary) {
        maps.assertHasSizeBetween(info, actualAsMap(), lowerBoundary, higherBoundary);
        return myself;
    }

    @Override
    public ImmutableMapAssert<K, V> hasSameSizeAs(Iterable<?> other) {
        maps.assertHasSameSizeAs(info, actualAsMap(), other);
        return myself;
    }

    @Override
    public ImmutableMapAssert<K, V> hasSameSizeAs(Object other) {
        maps.assertHasSameSizeAs(info, actualAsMap(), other);
        return myself;
    }

    public ImmutableMapAssert<K, V> hasSameSizeAs(ImmutableMap<?, ?> other) {
        maps.assertHasSameSizeAs(info, actualAsMap(), other.asMap());
        return myself;
    }

    public ImmutableMapAssert<K, V> hasSameSizeAs(Map<?, ?> other) {
        maps.assertHasSameSizeAs(info, actualAsMap(), other);
        return myself;
    }

    public ImmutableMapAssert<K, V> containsAllEntriesOf(Map<? extends K, ? extends V> other) {
        maps.assertContainsAllEntriesOf(info, actualAsMap(), other);
        return myself;
    }

    public ImmutableMapAssert<K, V> containsExactlyEntriesOf(ImmutableMap<? extends K, ? extends V> map) {
        return containsExactlyEntriesOf(map.asMap());
    }

    @SuppressWarnings("unchecked")
    public ImmutableMapAssert<K, V> containsExactlyEntriesOf(Map<? extends K, ? extends V> map) {
        return containsExactly(map.entrySet().toArray(new Map.Entry[0]));
    }

    public ImmutableMapAssert<K, V> containsExactlyInAnyOrderEntriesOf(ImmutableMap<? extends K, ? extends V> map) {
        return containsExactlyInAnyOrderEntriesOf(map.asMap());
    }

    @SuppressWarnings("unchecked")
    public ImmutableMapAssert<K, V> containsExactlyInAnyOrderEntriesOf(Map<? extends K, ? extends V> map) {
        return containsOnly(map.entrySet().toArray(new Map.Entry[0]));
    }

    public ImmutableMapAssert<K, V> containsEntry(K key, V value) {
        maps.assertContains(info, actualAsMap(), arrayOf(entryOf(key, value)));
        return myself;
    }
    
    public ImmutableMapAssert<K, V> doesNotContainEntry(K key, V value) {
        maps.assertDoesNotContain(info, actualAsMap(), arrayOf(entryOf(key, value)));
        return myself;
    }
    
    public ImmutableMapAssert<K, V> contains(@SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
        maps.assertContains(info, actualAsMap(), entries);
        return myself;
    }

    public ImmutableMapAssert<K, V> containsAnyOf(
            @SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
        maps.assertContainsAnyOf(info, actualAsMap(), entries);
        return myself;
    }

    public ImmutableMapAssert<K, V> containsExactly(
            @SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
        maps.assertContainsExactly(info, actualAsMap(), entries);
        return myself;
    }

    public ImmutableMapAssert<K, V> containsOnly(
            @SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
        maps.assertContainsOnly(info, actualAsMap(), entries);
        return myself;
    }

    public ImmutableMapAssert<K, V> doesNotContain(
            @SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
        maps.assertDoesNotContain(info, actualAsMap(), entries);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> hasEntrySatisfying(Condition<? super Map.Entry<K, V>> entryCondition) {
        maps.assertHasEntrySatisfying(info, actualAsMap(), entryCondition);
        return myself;
    }

    public ImmutableMapAssert<K, V> hasEntrySatisfying(Condition<? super K> keyCondition, Condition<? super V> valueCondition) {
        maps.assertHasEntrySatisfyingConditions(info, actualAsMap(), keyCondition, valueCondition);
        return myself;
    }

    public ImmutableMapAssert<K, V> hasEntrySatisfying(K key, Condition<? super V> valueCondition) {
        maps.assertHasEntrySatisfying(info, actualAsMap(), key, valueCondition);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> hasEntrySatisfying(K key, Consumer<? super V> valueRequirements) {
        maps.assertHasEntrySatisfying(info, actualAsMap(), key, valueRequirements);
        return myself;
    }

    public ImmutableMapAssert<K, V> allSatisfy(BiConsumer<? super K, ? super V> entryRequirements) {
        maps.assertAllSatisfy(info, actualAsMap(), entryRequirements);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> anySatisfy(BiConsumer<? super K, ? super V> entryRequirements) {
        maps.assertAnySatisfy(info, actualAsMap(), entryRequirements);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> noneSatisfy(BiConsumer<? super K, ? super V> entryRequirements) {
        maps.assertNoneSatisfy(info, actualAsMap(), entryRequirements);
        return myself;
    }

    public ImmutableMapAssert<K, V> containsKey(K key) {
        maps.assertContainsKey(info, actualAsMap(), key);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> containsKeys(@SuppressWarnings("unchecked") K... keys) {
        maps.assertContainsKeys(info, actualAsMap(), keys);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> hasKeySatisfying(Condition<? super K> keyCondition) {
        maps.assertHasKeySatisfying(info, actualAsMap(), keyCondition);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> doesNotContainKey(K key) {
        maps.assertDoesNotContainKey(info, actualAsMap(), key);
        return myself;
    }

    public ImmutableMapAssert<K, V> containsOnlyKeys(@SuppressWarnings("unchecked") K... keys) {
        maps.assertContainsOnlyKeys(info, actualAsMap(), keys);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> containsOnlyKeys(Iterable<? extends K> keys) {
        maps.assertContainsOnlyKeys(info, actualAsMap(), keys);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> doesNotContainKeys(@SuppressWarnings("unchecked") K... keys) {
        maps.assertDoesNotContainKeys(info, actualAsMap(), keys);
        return myself;
    }

    public ImmutableMapAssert<K, V> containsValue(V value) {
        maps.assertContainsValue(info, actualAsMap(), value);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> containsValues(@SuppressWarnings("unchecked") V... values) {
        maps.assertContainsValues(info, actualAsMap(), values);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> hasValueSatisfying(Condition<? super V> valueCondition) {
        maps.assertHasValueSatisfying(info, actualAsMap(), valueCondition);
        return myself;
    }
    
    public ImmutableMapAssert<K, V> doesNotContainValue(V value) {
        maps.assertDoesNotContainValue(info, actualAsMap(), value);
        return myself;
    }
    
    public IntegerAssert size() {
        return new IntegerAssert(actual.size());
    }

    public ImmutableListAssert<V> extractingByKeys(@SuppressWarnings("unchecked") K... keys) {
        isNotNull();
        ImmutableList<V> extractedValues = Stream.of(keys).map(actual::get).collect(ImmutableList.createCollector());
        String description = getDescription(() -> extractedDescriptionOf((Object[]) keys));
        return new ImmutableListAssert<>(extractedValues).as(description);
    }

    public AbstractObjectAssert<?, V> extractingByKey(K key) { // NOSONAR wildcard is ok
        isNotNull();
        V extractedValue = actual.get(key);
        String description = getDescription(() -> extractedDescriptionOf(key));
        return newObjectAssert(extractedValue).as(description);
    }

    public <A extends AbstractAssert<?, ?>> A extractingByKey(K key, InstanceOfAssertFactory<?, A> assertFactory) {
        isNotNull();
        V extractedValue = actual.get(key);
        String description = getDescription(() -> extractedDescriptionOf(key));
        return newObjectAssert(extractedValue).as(description).asInstanceOf(assertFactory);
    }

    public <V2> ImmutableListAssert<V2> extractingFromEntries(
            Function<? super Map.Entry<K, V>, V2> extractor) {
        isNotNull();
        ImmutableList<V2> extractedValues = actual.mapValues((k, v) -> extractor.apply(entryOf(k, v))).values();
        return new ImmutableListAssert<>(extractedValues);
    }

    public ImmutableListAssert<Tuple> extractingFromEntries(
            @SuppressWarnings("unchecked") Function<? super Map.Entry<K, V>, ?>... extractors) {
        Function<Map.Entry<K, V>, Tuple> tupleExtractor = v -> new Tuple(Stream.of(extractors)
                .map(extractor -> extractor.apply(v))
                .toArray());
        return extractingFromEntries(tupleExtractor);
    }

    public ImmutableListAssert<Object> flatExtracting(Object... keys) {
        List<Object> values = new ArrayList<>(actual.size());
        for (Object key : keys) {
            extractValue(actual.get(key), values);
        }
        return new ImmutableListAssert<>(ImmutableList.fromCollection(values));
    }
    
    private void extractValue(Object value, List<Object> target) {
        if (value instanceof Iterable<?>) {
            ((Iterable<?>) value).iterator().forEachRemaining(target::add);
        } else if (value.getClass().isArray()) {
            int length = Array.getLength(value);
            for (int i = 0; i < length; i++) {
                target.add(Array.get(value, i));
            }
        } else {
            target.add(value);
        }
    }
    
    @Override
    @Deprecated
    public ImmutableMapAssert<K, V> usingElementComparator(Comparator<? super Map.Entry<K, V>> customComparator) {
        throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
    }

    @Override
    @Deprecated
    public ImmutableMapAssert<K, V> usingDefaultElementComparator() {
        throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
    }

    private String getDescription(Supplier<String> fallbackSupplier) {
        Description currentDescription = info.description();
        return currentDescription != null ? currentDescription.value() : fallbackSupplier.get();
    }
    
    private Map<K, V> actualAsMap() {
        return actual != null ? actual.asMap() : null;
    }

    @SafeVarargs
    private static <T> T[] arrayOf(T... values) {
        return values;
    }

    private static <K, V> MapEntry<K, V> entryOf(K key, V value) {
        return MapEntry.entry(key, value);
    }
    
}
