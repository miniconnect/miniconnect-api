package hu.webarticum.miniconnect.lang;
import java.io.Closeable;

/**
 * A state-checkable variant of the Closeable interface not allowing checked exceptions.
 */
public interface CheckableCloseable extends Closeable {

    @Override
    public void close();

    public boolean isClosed();

    public default void checkClosed() {
        if (isClosed()) {
            String className = this.getClass().getName();
            throw new IllegalStateException("Closed instance of " + className);
        }
    }

}
