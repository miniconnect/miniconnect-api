package hu.webarticum.miniconnect.lang.assertj;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.AssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.util.VisibleForTesting;

import hu.webarticum.miniconnect.lang.ReversibleIterable;

public class ReversibleIterableAssert<T> // NOSONAR equals
        extends AbstractIterableAssert<
                ReversibleIterableAssert<T>, ReversibleIterable<? extends T>, T, ObjectAssert<T>> {

    @VisibleForTesting
    AssertFactory<T, ObjectAssert<T>> assertFactory;
    
    
    protected ReversibleIterableAssert(
            ReversibleIterable<? extends T> actual, AssertFactory<T, ObjectAssert<T>> assertFactory) {
        super(actual, ReversibleIterableAssert.class);
        this.assertFactory = assertFactory;
    }

    
    @Override
    protected ObjectAssert<T> toAssert(T value, String description) {
        return assertFactory.createAssert(value).as(description);
    }

    @Override
    protected ReversibleIterableAssert<T> newAbstractIterableAssert(Iterable<? extends T> iterable) {
        return new ReversibleIterableAssert<>(actual, assertFactory);
    }
    
    public ReversibleIterableAssert<T> reverseOrder() {
        return new ReversibleIterableAssert<>(actual.reverseOrder(), assertFactory);
    }
    
}
