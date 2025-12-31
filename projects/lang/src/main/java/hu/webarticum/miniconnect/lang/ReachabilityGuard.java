package hu.webarticum.miniconnect.lang;

/**
 * A Java8-compatible, closeable alternative to Java9's {@code Reference.reachabilityFence}
 */
public final class ReachabilityGuard implements AutoCloseable {

    @SuppressWarnings("unused")
    private static volatile Object FENCE;

    private Object ref;


    private ReachabilityGuard(Object ref) {
        this.ref = ref;
    }

    public static ReachabilityGuard of(Object ref) {
        return new ReachabilityGuard(ref);
    }


    @Override
    public void close() {
        FENCE = ref;
        ref = null;
        FENCE = null;
    }

}
