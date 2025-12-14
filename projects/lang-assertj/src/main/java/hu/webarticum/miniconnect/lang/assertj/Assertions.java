package hu.webarticum.miniconnect.lang.assertj;

import hu.webarticum.miniconnect.lang.ByteString;
import hu.webarticum.miniconnect.lang.CheckableCloseable;
import hu.webarticum.miniconnect.lang.ImmutableList;
import hu.webarticum.miniconnect.lang.ImmutableMap;
import hu.webarticum.miniconnect.lang.LargeInteger;
import hu.webarticum.miniconnect.lang.assertj.type.ByteStringAssert;
import hu.webarticum.miniconnect.lang.assertj.type.CheckableCloseableAssert;
import hu.webarticum.miniconnect.lang.assertj.type.ImmutableListAssert;
import hu.webarticum.miniconnect.lang.assertj.type.ImmutableMapAssert;
import hu.webarticum.miniconnect.lang.assertj.type.LargeIntegerAssert;

public class Assertions {

    private Assertions() {
        // static class
    }


    public static ByteStringAssert assertThat(ByteString actual) {
        return new ByteStringAssert(actual);
    }

    public static CheckableCloseableAssert assertThat(CheckableCloseable actual) {
        return new CheckableCloseableAssert(actual);
    }

    public static <T> ImmutableListAssert<T> assertThat(ImmutableList<? extends T> actual) {
        return new ImmutableListAssert<>(actual);
    }

    public static <K, V> ImmutableMapAssert<K, V> assertThat(ImmutableMap<K, V> actual) {
        return new ImmutableMapAssert<>(actual);
    }

    public static LargeIntegerAssert assertThat(LargeInteger actual) {
        return new LargeIntegerAssert(actual);
    }

}
