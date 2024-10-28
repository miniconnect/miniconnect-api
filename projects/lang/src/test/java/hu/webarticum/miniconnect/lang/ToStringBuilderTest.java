package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

class ToStringBuilderTest {

    @Test
    void testEmpty() {
    	assertThat(new ToStringBuilder(new FooClazz())
				.build())
			.isEqualTo("FooClazz { }");
    }

    @Test
    void testSingle() {
    	assertThat(new ToStringBuilder(new FooClazz())
    			.add("lorem", "ipsum")
				.build())
			.isEqualTo("FooClazz { lorem: ipsum }");
    }

    @Test
    void testMany() {
    	assertThat(new ToStringBuilder(new FooClazz())
    			.add("lorem", null)
    			.add("ipsum", "hello")
    			.add("dolor", 12)
    			.add("sit", new BigInteger("7542834754234"))
    			.add("amet", true)
    			.build())
    		.isEqualTo("FooClazz { lorem: null, ipsum: hello, dolor: 12, sit: 7542834754234, amet: true }");
    }
    
    private static final class FooClazz {
    }
    
}
