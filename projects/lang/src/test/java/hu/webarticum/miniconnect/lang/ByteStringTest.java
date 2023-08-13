package hu.webarticum.miniconnect.lang;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class ByteStringTest {
    
    @Test
    void testEmptyInstance() {
        ByteString emptyByteString = ByteString.empty();
        assertThat(emptyByteString.isEmpty()).isTrue();
        assertThat(emptyByteString.length()).isZero();
        assertThat(emptyByteString.extract()).isEmpty();
        assertThat(emptyByteString.iterator()).isExhausted();
        assertThatThrownBy(() -> emptyByteString.extract(10, 20)).isInstanceOf(IndexOutOfBoundsException.class);
    }
    
    @Test
    void testCreators() {
        assertThat(ByteString.of(new byte[] { 108, 111, 114, 101, 109 }).extract())
                .containsExactly(108, 111, 114, 101, 109);
        assertThat(ByteString.of(new byte[] { 108, 111, 114, 101, 109 }, 1, 2).extract())
                .containsExactly(111, 114);
        assertThat(ByteString.of("lorem").extract())
                .containsExactly(108, 111, 114, 101, 109);
        assertThat(ByteString.of("lorem", StandardCharsets.US_ASCII).extract())
                .containsExactly(108, 111, 114, 101, 109);
        assertThat(ByteString.ofByte((byte) 123).extract())
                .containsExactly(123);
        assertThat(ByteString.ofByte(3700).extract())
                .containsExactly(116);
        assertThat(ByteString.ofChar('r').extract())
                .containsExactly(0, 114);
        assertThat(ByteString.ofShort((short) 123).extract())
                .containsExactly(0, 123);
        assertThat(ByteString.ofInt(123).extract())
                .containsExactly(0, 0, 0, 123);
        assertThat(ByteString.ofLong(123L).extract())
                .containsExactly(0, 0, 0, 0, 0, 0, 0, 123);
        assertThat(ByteString.ofFloat(0.5f).extract())
                .containsExactly(63, 0, 0, 0);
        assertThat(ByteString.ofDouble(0.5).extract())
                .containsExactly(63, -32, 0, 0, 0, 0, 0, 0);
        assertThat(ByteString.fromInputStream(new ByteArrayInputStream(
                "lorem ipsum".getBytes(StandardCharsets.UTF_8))).extract())
                .containsExactly(108, 111, 114, 101, 109, 32, 105, 112, 115, 117, 109);
        assertThat(ByteString.fromInputStream(new ByteArrayInputStream(
                "lorem ipsum".getBytes(StandardCharsets.UTF_8)), 11).extract())
                .containsExactly(108, 111, 114, 101, 109, 32, 105, 112, 115, 117, 109);
    }

    @Test
    void testBuilderEmpty() {
        ByteString.Builder builder = ByteString.builder();
        assertThat(builder.length()).isZero();
        assertThat(builder.build()).isEmpty();
    }

    @Test
    void testBuilderSingle() {
        ByteString.Builder builder = ByteString.builder().append(ByteString.of("abc"));
        assertThat(builder.length()).isEqualTo(3);
        assertThat(builder.build()).map(b -> (int) b).containsExactly(97, 98, 99);
    }
    
    @Test
    void testBuilderComplex() {
        ByteString.Builder builder = ByteString.builder()
                .append(ByteString.of("lorem"))
                .append(ByteString.of("lorem"), 1, 2)
                .append((byte) 45)
                .append(new byte[] { 7, -12, 84, 101, 3, 7, 0, 99, 23 }, 3, 4)
                .append(new byte[] { 7, -12 })
                .appendChar('r')
                .appendShort((short) 123)
                .appendInt(234)
                .appendLong(456L)
                .appendFloat(0.25f)
                .appendDouble(0.75);
        assertThat(builder.length()).isEqualTo(42);
        assertThat(builder.build()).map(b -> (int) b).containsExactly(
                108, 111, 114, 101, 109, 111, 114, 45, 101, 3, 7, 0, 7, -12, 0, 114, 0, 123, 0, 0, 0, -22,
                0, 0, 0, 0, 0, 0, 1, -56, 62, -128, 0, 0, 63, -24, 0, 0, 0, 0, 0, 0);
    }

    @Test
    void testIsEmpty() {
        assertThat(ByteString.empty().isEmpty()).isTrue();
        assertThat(ByteString.of("").isEmpty()).isTrue();
        assertThat(ByteString.ofByte(0).isEmpty()).isFalse();
        assertThat(ByteString.of("lorem").isEmpty()).isFalse();
    }

    @Test
    void testByteAt() {
        ByteString byteString = ByteString.of("lorem");
        assertThatThrownBy(() -> byteString.byteAt(10)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThat(byteString.byteAt(2)).isEqualTo((byte) 114);
    }

    @Test
    void testSubstring() {
        ByteString byteString = ByteString.of("lorem");
        assertThatThrownBy(() -> byteString.substring(9)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThat(byteString.substring(3)).isEqualTo(ByteString.of("em"));
    }

    @Test
    void testSubstringUntil() {
        ByteString byteString = ByteString.of("lorem");
        assertThatThrownBy(() -> byteString.substring(2, 10)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThat(byteString.substring(1, 1)).isEqualTo(ByteString.empty());
        assertThat(byteString.substring(1, 3)).isEqualTo(ByteString.of("or"));
    }

    @Test
    void testSubstringLength() {
        ByteString byteString = ByteString.of("lorem");
        assertThatThrownBy(() -> byteString.substringLength(3, 5)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThat(byteString.substringLength(1, 0)).isEqualTo(ByteString.empty());
        assertThat(byteString.substringLength(2, 3)).isEqualTo(ByteString.of("rem"));
        assertThat(byteString.substringLength(0, 5)).isSameAs(byteString);
    }

    @Test
    void testExtract() {
        assertThat(ByteString.empty().extract()).isEmpty();
        assertThat(ByteString.of(new byte[] { 0, 0, 1, -3, 100 }).extract()).containsExactly(0, 0, 1, -3, 100);
        assertThat(ByteString.of("lorem ipsum").extract()).asString(StandardCharsets.UTF_8).isEqualTo("lorem ipsum");
        assertThat(ByteString.of("\u0152\u019D\u03A6\u1FFC").extract()).asString(StandardCharsets.UTF_8)
                .isEqualTo("\u0152\u019D\u03A6\u1FFC");
    }

    @Test
    void testExtractUntil() {
        assertThat(ByteString.empty().extract(0, 0)).isEmpty();
        assertThat(ByteString.of(new byte[] { 0, 0, 1, -3, 100 }).extract(1, 4)).containsExactly(0, 1, -3);
        assertThat(ByteString.of("lorem ipsum").extract(1, 1)).isEmpty();
        assertThat(ByteString.of("lorem ipsum").extract(3, 7)).asString(StandardCharsets.UTF_8).isEqualTo("em i");
        assertThat(ByteString.of("\u0152\u019D\u03A6\u1FFC").extract(2, 6)).asString(StandardCharsets.UTF_8)
                .isEqualTo("\u019D\u03A6");
        assertThat(ByteString.of("\u0152\u019D\u03A6\u1FFC").extract(3, 7)).containsExactly(157, 206, 166, 225);
    }

    @Test
    void testExtractLength() {
        assertThat(ByteString.empty().extractLength(0, 0)).isEmpty();
        assertThat(ByteString.of(new byte[] { 0, 0, 1, -3, 100 }).extractLength(1, 4)).containsExactly(0, 1, -3, 100);
        assertThat(ByteString.of("lorem ipsum").extractLength(1, 0)).isEmpty();
        assertThat(ByteString.of("lorem ipsum").extractLength(3, 3)).asString(StandardCharsets.UTF_8).isEqualTo("em ");
        assertThat(ByteString.of("\u0152\u019D\u03A6\u1FFC").extractLength(4, 5)).asString(StandardCharsets.UTF_8)
                .isEqualTo("\u03A6\u1FFC");
        assertThat(ByteString.of("\u0152\u019D\u03A6\u1FFC").extractLength(1, 6))
                .containsExactly(146, 198, 157, 206, 166, 225);
    }

    @Test
    void testExtractToException() {
        ByteString emptyByteString = ByteString.empty();
        assertThatThrownBy(() -> emptyByteString.extractTo(new byte[1], 1, 1, 0))
                .isInstanceOf(IndexOutOfBoundsException.class);
        ByteString shortByteString = ByteString.of("r");
        assertThatThrownBy(() -> shortByteString.extractTo(new byte[0], 1, 1, 0))
                .isInstanceOf(IndexOutOfBoundsException.class);
    }

    @Test
    void testExtractToEmpty() {
        assertDoesNotThrow(() -> ByteString.empty().extractTo(new byte[0], 0, 0, 0));
    }

    @Test
    void testExtractTo() {
        byte[] target = new byte[5];
        
        ByteString.empty().extractTo(target, 0, 0, 0);
        assertThat(target).containsExactly(0, 0, 0, 0, 0);

        ByteString.of("lorem").extractTo(target, 1, 3, 2);
        assertThat(target).containsExactly(0, 101, 109, 0, 0);

        ByteString.of("lorem").extractTo(target, 0, 0, 2);
        assertThat(target).containsExactly(108, 111, 109, 0, 0);
    }

    @Test
    void testWriteTo() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteString.empty().writeTo(out);
        ByteString.of("lorem").writeTo(out);
        ByteString.empty().writeTo(out);
        ByteString.of(" ipsum").writeTo(out);
        assertThat(out.toByteArray()).asString(StandardCharsets.UTF_8).isEqualTo("lorem ipsum");
    }

    @Test
    void testAsBuffer() throws IOException {
        assertThat(bytesOf(ByteString.empty().asBuffer())).isEmpty();
        assertThat(bytesOf(ByteString.of("lorem").asBuffer())).asString(StandardCharsets.UTF_8).isEqualTo("lorem");
        assertThat(bytesOf(ByteString.of("\u0152\u019D\u03A6\u1FFC").asBuffer())).asString(StandardCharsets.UTF_8)
                .isEqualTo("\u0152\u019D\u03A6\u1FFC");
    }
    
    private byte[] bytesOf(ByteBuffer buffer) {
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }

    @Test
    void testInputStream() throws IOException {
        assertThat(ByteString.empty().inputStream()).isEmpty();
        assertThat(ByteString.of("lorem").inputStream()).asString(StandardCharsets.UTF_8).isEqualTo("lorem");
        assertThat(ByteString.of("\u0152\u019D\u03A6\u1FFC").inputStream()).asString(StandardCharsets.UTF_8)
                .isEqualTo("\u0152\u019D\u03A6\u1FFC");
    }

    @Test
    void testInputStreamOffset() throws IOException {
        assertThat(ByteString.empty().inputStream(0, 0)).isEmpty();
        assertThat(ByteString.of("lorem").inputStream(1, 0)).isEmpty();
        assertThat(ByteString.of("lorem").inputStream(1, 2)).asString(StandardCharsets.UTF_8).isEqualTo("or");
        assertThat(ByteString.of("\u0152\u019D\u03A6\u1FFC").inputStream(2, 2)).asString(StandardCharsets.UTF_8)
                .isEqualTo("\u019D");
    }

    @Test
    void testHashCode() throws IOException {
        assertThat(ByteString.empty().hashCode()).isEqualTo(Arrays.hashCode(new byte[0]));
        assertThat(ByteString.of("lorem").hashCode())
                    .isEqualTo(Arrays.hashCode("lorem".getBytes(StandardCharsets.UTF_8)));
    }
    
    @Test
    void test() {
        
        // TODO
        //assertThat(true).isFalse();
        
    }
    
}
