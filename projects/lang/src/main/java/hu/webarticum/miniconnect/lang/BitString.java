package hu.webarticum.miniconnect.lang;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Simple immutable bit array implementation
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
                    word |= 1L << (j & 63);
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
                    word |= 1L << (j & 63);
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
                int shift = (j & 7) << 3;
                word |= unsigned << shift;
            }
            data[i] = word;
        }
        if (wordCount != fullWordCount) {
            long word = 0;
            int from = fullWordCount << 3;
            for (int j = from; j < byteCount; j++) {
                long unsigned = ((long) bytes[j]) & 0xFFL;
                int shift = (j & 7) << 3;
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
                long tailMask = (1L << tailSize) - 1;
                data[fullWordCount] &= tailMask;
            }
            return new BitString(data, size);
        }
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
        return compareWordArrays(data, other.data);
    }

    private static int compareWordArrays(long[] words1, long[] words2) {
        int commonCount = Math.min(words1.length, words2.length);
        for (int i = 0; i < commonCount; i++) {
            int cmp = compareWords(words1[i], words2[i]);
            if (cmp != 0) {
                return cmp;
            }
        }
        if (words1.length != commonCount) {
            for (int i = commonCount; i < words1.length; i++) {
                if (words1[i] != 0) {
                    return 1;
                }
            }
        } else if (words2.length != commonCount) {
            for (int i = commonCount; i < words2.length; i++) {
                if (words2[i] != 0) {
                    return -1;
                }
            }
        }
        return 0;
    }

    private static int compareWords(long word1, long word2) {
        long diff = word1 ^ word2;
        if (diff == 0) {
            return 0;
        }
        long mask = Long.lowestOneBit(diff);
        return ((word1 & mask) == 0) ? -1 : 1;
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

    public boolean get(int position) {
        int wordIndex = position >>> 6;
        long word = data[wordIndex];
        long masked = word & (1L << (position & 63));
        return masked != 0;
    }

    public BitString set(int position, boolean value) {
        int wordIndex = position >>> 6;
        long word = data[wordIndex];
        long masked = word & (1L << (position & 63));
        boolean found = masked != 0;
        if (found == value) {
            return this;
        }
        long mask = 1L << (position & 63);
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

    private class BitStringIterator implements Iterator<Boolean> {

        int position = 0;

        @Override
        public boolean hasNext() {
            return position < size;
        }

        @Override
        public Boolean next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no more bits");
            }
            boolean result = get(position);
            position++;
            return result;
        }

    }

}

