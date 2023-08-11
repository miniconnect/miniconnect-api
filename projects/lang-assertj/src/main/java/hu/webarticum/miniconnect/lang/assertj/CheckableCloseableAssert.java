package hu.webarticum.miniconnect.lang.assertj;

import org.assertj.core.internal.Failures;

import hu.webarticum.miniconnect.lang.CheckableCloseable;

public class CheckableCloseableAssert {
    
    private final CheckableCloseable actual;
    

    public CheckableCloseableAssert(CheckableCloseable actual) {
        this.actual = actual;
    }
    

    public CheckableCloseableAssert isOpen() {
        if (actual.isClosed()) {
            Failures.instance().failure("Unexpected closed status");
        }
        return this;
    }
    
    public CheckableCloseableAssert isClosed() {
        if (!actual.isClosed()) {
            Failures.instance().failure("Unexpected open status");
        }
        return this;
    }
    
}
