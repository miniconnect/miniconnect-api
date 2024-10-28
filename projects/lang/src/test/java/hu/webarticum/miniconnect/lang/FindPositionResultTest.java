package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class FindPositionResultTest {
    
    @Test
    void testEqualsFound() {
    	assertThat(FindPositionResult.found(LargeInteger.ONE))
				.isEqualTo(FindPositionResult.found(LargeInteger.ONE))
				.isEqualTo(FindPositionResult.of(true, LargeInteger.ONE))
				.isNotEqualTo(FindPositionResult.found(LargeInteger.TWO))
				.isNotEqualTo(FindPositionResult.notFound(LargeInteger.ONE))
				.isNotEqualTo(FindPositionResult.notFound(LargeInteger.TEN));
    }

    @Test
    void testEqualsNotFound() {
    	assertThat(FindPositionResult.notFound(LargeInteger.ONE))
				.isEqualTo(FindPositionResult.notFound(LargeInteger.ONE))
				.isEqualTo(FindPositionResult.of(false, LargeInteger.ONE))
				.isNotEqualTo(FindPositionResult.notFound(LargeInteger.ZERO))
				.isNotEqualTo(FindPositionResult.found(LargeInteger.ONE))
				.isNotEqualTo(FindPositionResult.found(LargeInteger.TWO));
    }

    @Test
    void testToStringFound() {
    	assertThat(FindPositionResult.found(LargeInteger.ONE)).hasToString("Found at 1");
    }

    @Test
    void testToStringNotFound() {
    	assertThat(FindPositionResult.notFound(LargeInteger.TEN)).hasToString("Not found; insert at 10");
    }

    @Test
    void testHashCode() {
    	assertThat(FindPositionResult.found(LargeInteger.ONE))
				.hasSameHashCodeAs(FindPositionResult.of(true, LargeInteger.ONE));
    }
    
}
