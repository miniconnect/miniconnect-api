package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CheckableCloseableTest {

    @Test
    void testCheckCloseable() {
        CheckableCloseable checkableCloseable = new MockCheckableCloseable();
        checkableCloseable.checkClosed();
        checkableCloseable.close();
        assertThatThrownBy(() -> checkableCloseable.checkClosed()).isInstanceOf(IllegalStateException.class);
    }
    
    
    private static class MockCheckableCloseable implements CheckableCloseable {
        
        private boolean closed = false;
        
        
        @Override
        public void close() {
            closed = true; 
        }

        @Override
        public boolean isClosed() {
            return closed;
        }
        
    }
    
}
