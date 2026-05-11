package hu.webarticum.miniconnect.lang.assertj.type;

import java.util.Comparator;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.ArraySortedAssert;
import org.assertj.core.api.BooleanArrayAssert;
import org.assertj.core.api.BooleanAssert;
import org.assertj.core.data.Index;
import org.assertj.core.util.VisibleForTesting;

import hu.webarticum.miniconnect.lang.BitString;

public class BitStringAssert // NOSONAR equals
        extends AbstractIterableAssert<BitStringAssert, BitString, Boolean, BooleanAssert>
        implements ArraySortedAssert<BitStringAssert, Boolean> {

    @VisibleForTesting
    protected final BooleanArrayAssert booleanArrayAssert;


    public BitStringAssert(BitString actual) {
        super(actual, BitStringAssert.class);
        booleanArrayAssert = new BooleanArrayAssert(actual != null ? actual.toBooleanArray() : null);
    }


    @Override
    protected BooleanAssert toAssert(Boolean value, String description) {
        return new BooleanAssert(value).as(description);
    }

    @Override
    protected BitStringAssert newAbstractIterableAssert(Iterable<? extends Boolean> iterable) {
        return new BitStringAssert(actual);
    }


    @Override
    public BitStringAssert isSorted() {
        booleanArrayAssert.isSorted();
        return myself;
    }

    @Override
    public BitStringAssert isSortedAccordingTo(Comparator<? super Boolean> comparator) {
        booleanArrayAssert.isSortedAccordingTo(comparator);
        return myself;
    }

    public BitStringAssert contains(boolean... values) {
        booleanArrayAssert.contains(values);
        return myself;
    }

    public BitStringAssert contains(String values) {
        booleanArrayAssert.contains(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert contains(int... values) {
        booleanArrayAssert.contains(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert contains(BitString values) {
        booleanArrayAssert.contains(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert containsOnly(boolean... values) {
        booleanArrayAssert.containsOnly(values);
        return myself;
    }

    public BitStringAssert containsOnly(String values) {
        booleanArrayAssert.containsOnly(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsOnly(int... values) {
        booleanArrayAssert.containsOnly(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsOnly(BitString values) {
        booleanArrayAssert.containsOnly(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert containsOnlyOnce(boolean... values) {
        booleanArrayAssert.containsOnlyOnce(values);
        return myself;
    }

    public BitStringAssert containsOnlyOnce(String values) {
        booleanArrayAssert.containsOnlyOnce(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsOnlyOnce(int... values) {
        booleanArrayAssert.containsOnlyOnce(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsOnlyOnce(BitString values) {
        booleanArrayAssert.containsOnlyOnce(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert containsSequence(boolean... values) {
        booleanArrayAssert.containsSequence(values);
        return myself;
    }

    public BitStringAssert containsSequence(String values) {
        booleanArrayAssert.containsSequence(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsSequence(int... values) {
        booleanArrayAssert.containsSequence(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsSequence(BitString values) {
        booleanArrayAssert.containsSequence(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert containsSubsequence(boolean... values) {
        booleanArrayAssert.containsSubsequence(values);
        return myself;
    }

    public BitStringAssert containsSubsequence(String values) {
        booleanArrayAssert.containsSubsequence(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsSubsequence(int... values) {
        booleanArrayAssert.containsSubsequence(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsSubsequence(BitString values) {
        booleanArrayAssert.containsSubsequence(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert startsWith(boolean... values) {
        booleanArrayAssert.startsWith(values);
        return myself;
    }

    public BitStringAssert startsWith(String values) {
        booleanArrayAssert.startsWith(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert startsWith(int... values) {
        booleanArrayAssert.startsWith(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert startsWith(BitString values) {
        booleanArrayAssert.startsWith(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert endsWith(boolean... values) {
        booleanArrayAssert.endsWith(values);
        return myself;
    }

    public BitStringAssert endsWith(String values) {
        booleanArrayAssert.endsWith(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert endsWith(int... values) {
        booleanArrayAssert.endsWith(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert endsWith(BitString values) {
        booleanArrayAssert.endsWith(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert containsExactly(boolean... values) {
        booleanArrayAssert.containsExactly(values);
        return myself;
    }

    public BitStringAssert containsExactly(String values) {
        booleanArrayAssert.containsExactly(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsExactly(int... values) {
        booleanArrayAssert.containsExactly(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsExactly(BitString values) {
        booleanArrayAssert.containsExactly(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert containsExactlyInAnyOrder(boolean... values) {
        booleanArrayAssert.containsExactlyInAnyOrder(values);
        return myself;
    }

    public BitStringAssert containsExactlyInAnyOrder(String values) {
        booleanArrayAssert.containsExactlyInAnyOrder(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsExactlyInAnyOrder(int... values) {
        booleanArrayAssert.containsExactlyInAnyOrder(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsExactlyInAnyOrder(BitString values) {
        booleanArrayAssert.containsExactlyInAnyOrder(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert containsAnyOf(boolean... values) {
        booleanArrayAssert.containsAnyOf(values);
        return myself;
    }

    public BitStringAssert containsAnyOf(String values) {
        booleanArrayAssert.containsAnyOf(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsAnyOf(int... values) {
        booleanArrayAssert.containsAnyOf(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert containsAnyOf(BitString values) {
        booleanArrayAssert.containsAnyOf(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert doesNotContain(boolean... values) {
        booleanArrayAssert.doesNotContain(values);
        return myself;
    }

    public BitStringAssert doesNotContain(String values) {
        booleanArrayAssert.doesNotContain(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert doesNotContain(int... values) {
        booleanArrayAssert.doesNotContain(toBooleanArray(values));
        return myself;
    }

    public BitStringAssert doesNotContain(BitString values) {
        booleanArrayAssert.doesNotContain(values.toBooleanArray());
        return myself;
    }

    public BitStringAssert contains(boolean value, Index index) {
        booleanArrayAssert.contains(value, index);
        return myself;
    }

    public BitStringAssert contains(char value, Index index) {
        booleanArrayAssert.contains(toBoolean(value), index);
        return myself;
    }

    public BitStringAssert contains(int value, Index index) {
        booleanArrayAssert.contains(toBoolean(value), index);
        return myself;
    }

    public BitStringAssert doesNotContain(boolean value, Index index) {
        booleanArrayAssert.doesNotContain(value, index);
        return myself;
    }

    public BitStringAssert doesNotContain(char value, Index index) {
        booleanArrayAssert.doesNotContain(toBoolean(value), index);
        return myself;
    }

    public BitStringAssert doesNotContain(int value, Index index) {
        booleanArrayAssert.doesNotContain(toBoolean(value), index);
        return myself;
    }

    private boolean toBoolean(char c) {
        if (c == '0') {
            return false;
        } else if (c == '1') {
            return true;
        } else {
            throw new IllegalArgumentException("Invalid bit value: " + c);
        }
    }

    private boolean toBoolean(int c) {
        if (c == 0) {
            return false;
        } else if (c == 1) {
            return true;
        } else {
            throw new IllegalArgumentException("Invalid bit value: " + c);
        }
    }

    private boolean[] toBooleanArray(String str) {
        return BitString.of(str).toBooleanArray();
    }

    private boolean[] toBooleanArray(int[] digits) {
        boolean[] result = new boolean[digits.length];
        for (int i = 0; i < digits.length; i++) {
            result[i] = toBoolean(digits[i]);
        }
        return result;
    }

}
