package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class LargeIntegerTest {
    
    private static final String DATA_DIR = "/hu/webarticum/miniconnect/lang/test-cases/ImmutableMapTest";
    
    
    // TODO
    
    @ParameterizedTest
    @CsvFileSource(resources = DATA_DIR + "/add-cases.csv", numLinesToSkip = 1)
    void testAdd(LargeInteger p1, LargeInteger p2, LargeInteger p3) {
        assertThat(p1.add(p2)).isEqualTo(p3);
    }
    
    @Test
    void test() {

        // TODO
        //assertThat(true).isFalse();
        
    }

}
