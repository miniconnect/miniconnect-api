package hu.webarticum.miniconnect.lang.assertj;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.AssertFactory;
import org.assertj.core.api.Condition;
import org.assertj.core.api.IndexedObjectEnumerableAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectAssertFactory;
import org.assertj.core.data.Index;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Lists;
import org.assertj.core.util.VisibleForTesting;

import hu.webarticum.miniconnect.lang.ImmutableList;

public class ImmutableListAssert<T> // NOSONAR equals
        extends AbstractIterableAssert<ImmutableListAssert<T>, ImmutableList<? extends T>, T, ObjectAssert<T>>
        implements IndexedObjectEnumerableAssert<ImmutableListAssert<T>, T> {

    @VisibleForTesting
    Lists lists = Lists.instance();

    @VisibleForTesting
    AssertFactory<T, ObjectAssert<T>> assertFactory;


    public ImmutableListAssert(ImmutableList<? extends T> actual) {
        this(actual, new ObjectAssertFactory<>());
    }

    public ImmutableListAssert(ImmutableList<? extends T> actual, AssertFactory<T, ObjectAssert<T>> assertFactory) {
        super(actual, ImmutableListAssert.class);
        this.assertFactory = assertFactory;
    }


    @Override
    public ImmutableListAssert<T> contains(T value, Index index) {
        lists.assertContains(info, actualAsList(), value, index); // NOSONAR this is a test library
        return myself;
    }

    @Override
    public ImmutableListAssert<T> doesNotContain(T value, Index index) {
        lists.assertDoesNotContain(info, actualAsList(), value, index);
        return myself;
    }

    @Override
    protected ObjectAssert<T> toAssert(T value, String description) {
        return assertFactory.createAssert(value).as(description);
    }

    @Override
    protected ImmutableListAssert<T> newAbstractIterableAssert(Iterable<? extends T> iterable) {
        return new ImmutableListAssert<>(actual, assertFactory);
    }

    public ImmutableListAssert<T> has(Condition<? super T> condition, Index index) {
        lists.assertHas(info, actualAsList(), condition, index);
        return myself;
    }

    public ImmutableListAssert<T> is(Condition<? super T> condition, Index index) {
        lists.assertIs(info, actualAsList(), condition, index);
        return myself;
    }

    public ImmutableListAssert<T> satisfies(Consumer<? super T> requirements, Index index) {
        lists.satisfies(info, actualAsList(), requirements, index);
        return myself;
    }

    public ImmutableListAssert<T> isSorted() {
        lists.assertIsSorted(info, actualAsList());
        return myself;
    }

    public ImmutableListAssert<T> isSortedAccordingTo(Comparator<? super T> comparator) {
        lists.assertIsSortedAccordingToComparator(info, actualAsList(), comparator);
        return myself;
    }
    
    @Override
    public ImmutableListAssert<T> usingElementComparator(Comparator<? super T> customComparator) {
        lists = new Lists(new ComparatorBasedComparisonStrategy(customComparator));
        return super.usingElementComparator(customComparator);
    }

    @Override
    public ImmutableListAssert<T> usingDefaultElementComparator() {
        lists = Lists.instance();
        return super.usingDefaultElementComparator();
    }

    @Override
    protected ImmutableListAssert<T> usingComparisonStrategy(ComparisonStrategy comparisonStrategy) {
        lists = new Lists(comparisonStrategy);
        return super.usingComparisonStrategy(comparisonStrategy);
    }

    public ReversibleIterableAssert<T> reverseOrder() {
        return new ReversibleIterableAssert<>(actual.reverseOrder(), assertFactory);
    }

    @SuppressWarnings("unchecked")
    private List<T> actualAsList() {
        return actual != null ? ((ImmutableList<T>) actual).asList() : null;
    }
    
}
