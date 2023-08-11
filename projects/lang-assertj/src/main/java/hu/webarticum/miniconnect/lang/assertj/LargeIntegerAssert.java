package hu.webarticum.miniconnect.lang.assertj;

import java.util.Comparator;

import org.assertj.core.api.AbstractComparableAssert;
import org.assertj.core.api.NumberAssert;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.util.VisibleForTesting;

import hu.webarticum.miniconnect.lang.LargeInteger;

public class LargeIntegerAssert // NOSONAR equals
        extends AbstractComparableAssert<LargeIntegerAssert, LargeInteger>
        implements NumberAssert<LargeIntegerAssert, LargeInteger> {

    @VisibleForTesting
    LargeIntegers largeIntegers = LargeIntegers.instance();
    
    
    public LargeIntegerAssert(LargeInteger actual) {
        super(actual, LargeInteger.class);
    }
    

    @Override
    public LargeIntegerAssert isZero() {
        largeIntegers.assertIsZero(info, actual); // NOSONAR this is a test library
        return myself;
    }

    @Override
    public LargeIntegerAssert isNotZero() {
        largeIntegers.assertIsNotZero(info, actual);
        return myself;
    }

    @Override
    public LargeIntegerAssert isOne() {
        largeIntegers.assertIsOne(info, actual);
        return myself;
    }

    @Override
    public LargeIntegerAssert isPositive() {
        largeIntegers.assertIsPositive(info, actual);
        return myself;
    }

    @Override
    public LargeIntegerAssert isNegative() {
        largeIntegers.assertIsNegative(info, actual);
        return myself;
    }

    @Override
    public LargeIntegerAssert isNotPositive() {
        largeIntegers.assertIsNotPositive(info, actual);
        return myself;
    }

    @Override
    public LargeIntegerAssert isNotNegative() {
        largeIntegers.assertIsNotNegative(info, actual);
        return myself;
    }

    @Override
    public LargeIntegerAssert isBetween(LargeInteger start, LargeInteger end) {
        largeIntegers.assertIsBetween(info, actual, start, end);
        return myself;
    }

    @Override
    public LargeIntegerAssert isStrictlyBetween(LargeInteger start, LargeInteger end) {
        largeIntegers.assertIsStrictlyBetween(info, actual, start, end);
        return myself;
    }

    @Override
    public LargeIntegerAssert isCloseTo(LargeInteger expected, Offset<LargeInteger> offset) {
        largeIntegers.assertIsCloseTo(info, actual, expected, offset);
        return myself;
    }

    @Override
    public LargeIntegerAssert isCloseTo(LargeInteger expected, Percentage percentage) {
        largeIntegers.assertIsCloseToPercentage(info, actual, expected, percentage);
        return myself;
    }

    @Override
    public LargeIntegerAssert isNotCloseTo(LargeInteger expected, Offset<LargeInteger> offset) {
        largeIntegers.assertIsNotCloseTo(info, actual, expected, offset);
        return myself;
    }

    @Override
    public LargeIntegerAssert isNotCloseTo(LargeInteger expected, Percentage percentage) {
        largeIntegers.assertIsNotCloseToPercentage(info, actual, expected, percentage);
        return myself;
    }

    public LargeIntegerAssert isEqualTo(String expected) {
        return isEqualTo(LargeInteger.of(expected));
    }
    
    public LargeIntegerAssert isEqualTo(int expected) {
        return isEqualTo(LargeInteger.of(expected));
    }
    
    public LargeIntegerAssert isEqualTo(long expected) {
        return isEqualTo(LargeInteger.of(expected));
    }

    @Override
    public LargeIntegerAssert usingComparator(Comparator<? super LargeInteger> customComparator) {
        return usingComparator(customComparator, null);
    }

    @Override
    public LargeIntegerAssert usingComparator(
            Comparator<? super LargeInteger> customComparator, String customComparatorDescription) {
        this.largeIntegers = new LargeIntegers(
                new ComparatorBasedComparisonStrategy(customComparator, customComparatorDescription));
        return super.usingComparator(customComparator, customComparatorDescription);
    }

    @Override
    public LargeIntegerAssert usingDefaultComparator() {
        this.largeIntegers = LargeIntegers.instance();
        return super.usingDefaultComparator();
    }

}
