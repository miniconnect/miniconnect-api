package hu.webarticum.miniconnect.lang.assertj;

import java.util.Comparator;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.ArraySortedAssert;
import org.assertj.core.api.ByteArrayAssert;
import org.assertj.core.api.ByteAssert;
import org.assertj.core.data.Index;
import org.assertj.core.util.VisibleForTesting;

import hu.webarticum.miniconnect.lang.ByteString;

public class ByteStringAssert // NOSONAR equals
        extends AbstractIterableAssert<ByteStringAssert, ByteString, Byte, ByteAssert>
        implements ArraySortedAssert<ByteStringAssert, Byte> {

    @VisibleForTesting
    protected final ByteArrayAssert byteArrayAssert;
    
    
    public ByteStringAssert(ByteString actual) {
        super(actual, Byte.class);
        byteArrayAssert = new ByteArrayAssert(actual.extract());
    }

    
    @Override
    protected ByteAssert toAssert(Byte value, String description) {
        return new ByteAssert(value).as(description);
    }

    @Override
    protected ByteStringAssert newAbstractIterableAssert(Iterable<? extends Byte> iterable) {
        return new ByteStringAssert(actual);
    }


    @Override
    public ByteStringAssert isSorted() {
        byteArrayAssert.isSorted();
        return myself;
    }

    @Override
    public ByteStringAssert isSortedAccordingTo(Comparator<? super Byte> comparator) {
        byteArrayAssert.isSortedAccordingTo(comparator);
        return myself;
    }

    public ByteStringAssert contains(byte... values) {
        byteArrayAssert.contains(values);
        return myself;
    }
    
    public ByteStringAssert contains(int... values) {
        byteArrayAssert.contains(values);
        return myself;
    }
    public ByteStringAssert contains(ByteString values) {
        byteArrayAssert.contains(values.extract());
        return myself;
    }
    
    public ByteStringAssert containsOnly(byte... values) {
        byteArrayAssert.containsOnly(values);
        return myself;
    }
    
    public ByteStringAssert containsOnly(int... values) {
        byteArrayAssert.containsOnly(values);
        return myself;
    }

    public ByteStringAssert containsOnly(ByteString values) {
        byteArrayAssert.containsOnly(values.extract());
        return myself;
    }

    public ByteStringAssert containsOnlyOnce(byte... values) {
        byteArrayAssert.containsOnlyOnce(values);
        return myself;
    }
    
    public ByteStringAssert containsOnlyOnce(int... values) {
        byteArrayAssert.containsOnlyOnce(values);
        return myself;
    }

    public ByteStringAssert containsOnlyOnce(ByteString values) {
        byteArrayAssert.containsOnlyOnce(values.extract());
        return myself;
    }

    public ByteStringAssert containsSequence(byte... values) {
        byteArrayAssert.containsSequence(values);
        return myself;
    }
    
    public ByteStringAssert containsSequence(int... values) {
        byteArrayAssert.containsSequence(values);
        return myself;
    }

    public ByteStringAssert containsSequence(ByteString values) {
        byteArrayAssert.containsSequence(values.extract());
        return myself;
    }

    public ByteStringAssert containsSubsequence(byte... values) {
        byteArrayAssert.containsSubsequence(values);
        return myself;
    }
    
    public ByteStringAssert containsSubsequence(int... values) {
        byteArrayAssert.containsSubsequence(values);
        return myself;
    }

    public ByteStringAssert containsSubsequence(ByteString values) {
        byteArrayAssert.containsSubsequence(values.extract());
        return myself;
    }

    public ByteStringAssert startsWith(byte... values) {
        byteArrayAssert.startsWith(values);
        return myself;
    }
    
    public ByteStringAssert startsWith(int... values) {
        byteArrayAssert.startsWith(values);
        return myself;
    }

    public ByteStringAssert startsWith(ByteString values) {
        byteArrayAssert.startsWith(values.extract());
        return myself;
    }

    public ByteStringAssert endsWith(byte... values) {
        byteArrayAssert.endsWith(values);
        return myself;
    }
    
    public ByteStringAssert endsWith(int... values) {
        byteArrayAssert.endsWith(values);
        return myself;
    }

    public ByteStringAssert endsWith(ByteString values) {
        byteArrayAssert.endsWith(values.extract());
        return myself;
    }

    public ByteStringAssert containsExactly(byte... values) {
        byteArrayAssert.containsExactly(values);
        return myself;
    }
    
    public ByteStringAssert containsExactly(int... values) {
        byteArrayAssert.containsExactly(values);
        return myself;
    }

    public ByteStringAssert containsExactly(ByteString values) {
        byteArrayAssert.containsExactly(values.extract());
        return myself;
    }

    public ByteStringAssert containsExactlyInAnyOrder(byte... values) {
        byteArrayAssert.containsExactlyInAnyOrder(values);
        return myself;
    }
    
    public ByteStringAssert containsExactlyInAnyOrder(int... values) {
        byteArrayAssert.containsExactlyInAnyOrder(values);
        return myself;
    }

    public ByteStringAssert containsExactlyInAnyOrder(ByteString values) {
        byteArrayAssert.containsExactlyInAnyOrder(values.extract());
        return myself;
    }

    public ByteStringAssert containsAnyOf(byte... values) {
        byteArrayAssert.containsAnyOf(values);
        return myself;
    }
    
    public ByteStringAssert containsAnyOf(int... values) {
        byteArrayAssert.containsAnyOf(values);
        return myself;
    }

    public ByteStringAssert containsAnyOf(ByteString values) {
        byteArrayAssert.containsAnyOf(values.extract());
        return myself;
    }

    public ByteStringAssert doesNotContain(byte... values) {
        byteArrayAssert.doesNotContain(values);
        return myself;
    }
    
    public ByteStringAssert doesNotContain(int... values) {
        byteArrayAssert.doesNotContain(values);
        return myself;
    }

    public ByteStringAssert doesNotContain(ByteString values) {
        byteArrayAssert.doesNotContain(values.extract());
        return myself;
    }
    
    public ByteStringAssert contains(byte value, Index index) {
        byteArrayAssert.contains(value, index);
        return myself;
    }

    public ByteStringAssert contains(int value, Index index) {
        byteArrayAssert.contains(value, index);
        return myself;
    }

    public ByteStringAssert doesNotContain(byte value, Index index) {
        byteArrayAssert.doesNotContain(value, index);
        return myself;
    }

    public ByteStringAssert doesNotContain(int value, Index index) {
        byteArrayAssert.doesNotContain(value, index);
        return myself;
    }
    
}
