package hu.webarticum.miniconnect.lang.assertj.type;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.internal.Failures;

import hu.webarticum.miniconnect.lang.CheckableCloseable;

public class CheckableCloseableAssert extends AbstractAssert<CheckableCloseableAssert, CheckableCloseable> {
    
    public CheckableCloseableAssert(CheckableCloseable actual) {
        super(actual, CheckableCloseableAssert.class);
    }
    

    public CheckableCloseableAssert isOpen() {
        if (actual.isClosed()) {
            throw Failures.instance().failure("Unexpected closed status");
        }
        return this;
    }
    
    public CheckableCloseableAssert isClosed() {
        if (!actual.isClosed()) {
            throw Failures.instance().failure("Unexpected open status");
        }
        return this;
    }
    
}
