package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ByteStringTest {
    
    @Test
    void testEmpty() {
        ByteString emptyByteString = ByteString.empty();
        assertThat(emptyByteString.length()).isZero();
    }
    
    @Test
    void test() {

        // TODO
        //assertThat(true).isFalse();
        
    }

}
