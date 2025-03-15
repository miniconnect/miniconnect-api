package hu.webarticum.miniconnect.lang.assertj;

import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Numbers;
import org.assertj.core.util.VisibleForTesting;

import hu.webarticum.miniconnect.lang.LargeInteger;

public class LargeIntegers extends Numbers<LargeInteger> {

    private static final LargeIntegers INSTANCE = new LargeIntegers();


    public LargeIntegers(ComparisonStrategy comparisonStrategy) {
      super(comparisonStrategy);
    }

    @VisibleForTesting
    LargeIntegers() {
        super();
    }

    public static LargeIntegers instance() {
        return INSTANCE;
    }

    
    @Override
    protected LargeInteger zero() {
        return LargeInteger.ZERO;
    }

    @Override
    protected LargeInteger one() {
        return LargeInteger.ONE;
    }

    @Override
    protected LargeInteger absDiff(LargeInteger actual, LargeInteger other) {
        return actual.subtract(other).abs();
    }

    @Override
    protected boolean isGreaterThan(LargeInteger value, LargeInteger other) {
        return value.subtract(other).compareTo(zero()) > 0;
    }
    
}
