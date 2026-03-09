package hu.webarticum.miniconnect.lang;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple immutable bit array implementation.
 *
 * It represents the bits in a long array with MSB first order:
 * the array layout is MSB first, and the MSB of each long corresponds
 * to the first logical bit it represents from the bit string.
 * The last long value, if incomplete (in case of size % 64 > 0),
 * contains the tail part in its MSB positions,
 * and the unused LSB positions are filled with zeros.
 */
public final class BitString implements Comparable<BitString>, Iterable<Boolean>, Serializable {

    private static final long serialVersionUID = 1436084935617731076L;

    private static final BitString EMPTY = new BitString(new long[0], 0);


    private final long[] data;

    private final int size;


    private BitString(long[] data, int size) {
        this.data = data;
        this.size = size;
    }

    public static BitString empty() {
        return EMPTY;
    }

    public static BitString of(boolean... bits) {
        int size = bits.length;
        int wordCount = (size + 63) >>> 6;
        long[] data = new long[wordCount];
        int fullWordCount = size >>> 6;
        for (int i = 0; i < fullWordCount; i++) {
            long word = 0;
            int from = i << 6;
            int until = from + 64;
            for (int j = from; j < until; j++) {
                if (bits[j]) {
                    word |= Long.MIN_VALUE >>> (j & 63);
                }
            }
            data[i] = word;
        }
        int tailSize = size & 63;
        if (tailSize > 0) {
            long word = 0;
            int from = fullWordCount << 6;
            for (int j = from; j < size; j++) {
                if (bits[j]) {
                    word |= Long.MIN_VALUE >>> (j & 63);
                }
            }
            data[fullWordCount] = word;
        }
        return new BitString(data, size);
    }

    public static BitString of(byte[] bytes) {
        int byteCount = bytes.length;
        int size = byteCount << 3;
        int wordCount = (size + 63) >>> 6;
        long[] data = new long[wordCount];
        int fullWordCount = size >>> 6;
        for (int i = 0; i < fullWordCount; i++) {
            long word = 0;
            int from = i << 3;
            int until = from + 8;
            for (int j = from; j < until; j++) {
                long unsigned = ((long) bytes[j]) & 0xFFL;
                int shift = (7 - (j & 7)) << 3;
                word |= unsigned << shift;
            }
            data[i] = word;
        }
        if (wordCount != fullWordCount) {
            long word = 0;
            int from = fullWordCount << 3;
            for (int j = from; j < byteCount; j++) {
                long unsigned = ((long) bytes[j]) & 0xFFL;
                int shift = (7 - (j & 7)) << 3;
                word |= unsigned << shift;
            }
            data[fullWordCount] = word;
        }
        return new BitString(data, size);
    }

    public static BitString of(long[] longs) {
        return new BitString(Arrays.copyOf(longs, longs.length), longs.length << 6);
    }

    public static BitString of(long[] longs, int size) {
        int ceilWordCount = (size + 63) >>> 6;
        int rawSize = longs.length << 6;
        if (size == rawSize) {
            return new BitString(Arrays.copyOf(longs, longs.length), size);
        } else if (size > rawSize) {
            long[] data = new long[ceilWordCount];
            System.arraycopy(longs, 0, data, 0, longs.length);
            return new BitString(data, size);
        } else {
            long[] data = new long[ceilWordCount];
            System.arraycopy(longs, 0, data, 0, ceilWordCount);
            int ceilSize = ceilWordCount << 6;
            if (size != ceilSize) {
                int fullWordCount = size >>> 6;
                int tailSize = size & 63;
                long tailMask = -1L << (64 - tailSize);
                data[fullWordCount] &= tailMask;
            }
            return new BitString(data, size);
        }
    }

    public static BitString of(String values) {
        int size = values.length();
        int wordCount = (size + 63) >>> 6;
        long[] data = new long[wordCount];
        int fullWordCount = size >>> 6;
        for (int i = 0; i < fullWordCount; i++) {
            long word = 0;
            int from = i << 6;
            int until = from + 64;
            for (int j = from; j < until; j++) {
                char c = values.charAt(j);
                if (c == '1') {
                    word |= Long.MIN_VALUE >>> (j & 63);
                } else if (c != '0') {
                    throw new IllegalArgumentException("Invalid character: '" + c + "' at " + j);
                }
            }
            data[i] = word;
        }
        int tailSize = size & 63;
        if (tailSize > 0) {
            long word = 0;
            int from = fullWordCount << 6;
            for (int j = from; j < size; j++) {
                char c = values.charAt(j);
                if (c == '1') {
                    word |= Long.MIN_VALUE >>> (j & 63);
                } else if (c != '0') {
                    throw new IllegalArgumentException("Invalid character: '" + c + "' at " + j);
                }
            }
            data[fullWordCount] = word;
        }
        return new BitString(data, size);
    }


    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public long[] data() {
        return Arrays.copyOf(data, data.length);
    }

    @Override
    public int compareTo(BitString other) {
        int commonCount = Math.min(data.length, other.data.length);
        for (int i = 0; i < commonCount; i++) {
            int cmp = Long.compare(data[i], other.data[i]);
            if (cmp != 0) {
                return cmp;
            }
        }
        return Integer.compare(size, other.size);
    }

    @Override
    public Iterator<Boolean> iterator() {
        return new BitStringIterator();
    }

    @Override
    public int hashCode() {
        return (size * 37) + Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BitString)) {
            return false;
        }
        BitString other = (BitString) obj;
        return size == other.size && Arrays.equals(data, other.data);
    }

    @Override
    public String toString() {
        StringBuilder resultBuilder = new StringBuilder(size());
        for (int i = 0; i < size(); i++) {
            resultBuilder.append(get(i) ? '1' : '0');
        }
        return resultBuilder.toString();
    }

    public BitString resize(int newSize) {
        if (newSize > size) {
            int newDataSize = (newSize + 63) >>> 6;
            long[] newData = new long[newDataSize];
            System.arraycopy(data, 0, newData, 0, data.length);
            return new BitString(newData, newSize);
        } else if (newSize == size) {
            return this;
        } else if (newSize < 0) {
            throw new IllegalArgumentException("Size must not be negative");
        } else {
            int newDataSize = (newSize + 63) >>> 6;
            long[] newData = new long[newDataSize];
            int fullWordSize = newSize >>> 6;
            if (fullWordSize != 0) {
                System.arraycopy(data, 0, newData, 0, fullWordSize);
            }
            int newTailSize = newSize & 63;
            if (newTailSize != 0) {
                long tailMask = -1L << (64 - newTailSize);
                newData[fullWordSize] = data[fullWordSize] & tailMask;
            }
            return new BitString(newData, newSize);
        }
    }

    public boolean get(int position) {
        if (position < 0 || position >= size) {
            return false;
        }
        int wordIndex = position >>> 6;
        long word = data[wordIndex];
        long mask = Long.MIN_VALUE >>> (position & 63);
        long masked = word & mask;
        return masked != 0;
    }

    public boolean getStrict(int position) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException();
        }
        int wordIndex = position >>> 6;
        long word = data[wordIndex];
        long mask = Long.MIN_VALUE >>> (position & 63);
        long masked = word & mask;
        return masked != 0;
    }

    public BitString set(int position, boolean value) {
        if (position < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (position >= size) {
            int targetCeilWordCount = (size + 63) >>> 6;
            BitString result;
            if (targetCeilWordCount == data.length) {
                result = new BitString(data, position + 1);
            } else {
                long[] newData = new long[targetCeilWordCount];
                System.arraycopy(data, 0, newData, 0, data.length);
                result = new BitString(data, position + 1);
            }
            if (value) {
                result = result.set(position, value);
            }
            return result;
        }
        int wordIndex = position >>> 6;
        long word = data[wordIndex];
        long mask = Long.MIN_VALUE >>> (position & 63);
        long masked = word & mask;
        boolean wasSet = masked != 0;
        if (wasSet == value) {
            return this;
        }
        long changedWord = word ^ mask;
        long[] newData = replacedWord(data, wordIndex, changedWord);
        return new BitString(newData, size);
    }

    public BitString setStrict(int position, boolean value) {
        if (position < 0 || position >= size) {
            throw new IndexOutOfBoundsException();
        }
        int wordIndex = position >>> 6;
        long word = data[wordIndex];
        long mask = Long.MIN_VALUE >>> (position & 63);
        long masked = word & mask;
        boolean wasSet = masked != 0;
        if (wasSet == value) {
            return this;
        }
        long changedWord = word ^ mask;
        long[] newData = replacedWord(data, wordIndex, changedWord);
        return new BitString(newData, size);
    }

    private long[] replacedWord(long[] original, int wordIndex, long newWord) {
        if (original.length == 1) {
            return new long[] { newWord };
        }
        long[] result = new long[original.length];
        if (wordIndex != 0) {
            System.arraycopy(original, 0, result, 0, wordIndex);
        }
        if (wordIndex < original.length - 1) {
            int from = wordIndex;
            System.arraycopy(original, from, result, from, original.length - wordIndex - 1);
        }
        return result;
    }

    public BitString not() {
        long[] newData = new long[data.length];
        int fullWordCount = size >>> 6;
        for (int i = 0; i < fullWordCount; i++) {
            newData[i] = ~data[i];
        }
        if (data.length != fullWordCount) {
            int tailSize = size & 63;
            long tailMask = -1L << (64 - tailSize);
            newData[fullWordCount] = ~data[fullWordCount] & tailMask;
        }
        return new BitString(newData, size);
    }

    private class BitStringIterator implements Iterator<Boolean> {

        int position = 0;

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public Boolean next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more bits");
            }
            boolean result = get(position);
            position++;
            return result;
        }

    }

}

