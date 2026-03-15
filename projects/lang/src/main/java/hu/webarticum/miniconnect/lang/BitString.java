package hu.webarticum.miniconnect.lang;

import java.io.Serializable;
import java.math.BigInteger;
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

    public boolean hasZerosOnly() {
        for (long word : data) {
            if (word != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean hasOnesOnly() {
        int fullWordCount = size >>> 6;
        for (int i = 0; i < fullWordCount; i++) {
            long word = data[i];
            if (word != -1L) {
                return false;
            }
        }
        int tailSize = size & 63;
        if (tailSize > 0) {
            long lastWord = data[fullWordCount];
            long onePaddedLastWord = lastWord | (-1L >>> tailSize);
            if (onePaddedLastWord != -1L) {
                return false;
            }
        }
        return true;
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

    public BitString and(BitString other) {
        int resultSize = Math.max(size, other.size);
        int resultDataSize = Math.max(data.length, other.data.length);
        long[] resultData = new long[resultDataSize];
        int commonDataSize = Math.min(data.length, other.data.length);
        for (int i = 0; i < commonDataSize; i++) {
            resultData[i] = data[i] & other.data[i];
        }
        return new BitString(resultData, resultSize);
    }

    public BitString or(BitString other) {
        int resultSize = Math.max(size, other.size);
        int resultDataSize = Math.max(data.length, other.data.length);
        long[] resultData = new long[resultDataSize];
        int commonDataSize = Math.min(data.length, other.data.length);
        for (int i = 0; i < commonDataSize; i++) {
            resultData[i] = data[i] | other.data[i];
        }
        if (data.length > other.data.length) {
            System.arraycopy(data, commonDataSize, resultData, commonDataSize, resultDataSize - commonDataSize);
        } else if (data.length != other.data.length) {
            System.arraycopy(other.data, commonDataSize, resultData, commonDataSize, resultDataSize - commonDataSize);
        }
        return new BitString(resultData, resultSize);
    }

    public BitString xor(BitString other) {
        int resultSize = Math.max(size, other.size);
        int resultDataSize = Math.max(data.length, other.data.length);
        long[] resultData = new long[resultDataSize];
        int commonDataSize = Math.min(data.length, other.data.length);
        for (int i = 0; i < commonDataSize; i++) {
            resultData[i] = data[i] ^ other.data[i];
        }
        if (data.length > other.data.length) {
            System.arraycopy(data, commonDataSize, resultData, commonDataSize, resultDataSize - commonDataSize);
        } else if (data.length != other.data.length) {
            System.arraycopy(other.data, commonDataSize, resultData, commonDataSize, resultDataSize - commonDataSize);
        }
        return new BitString(resultData, resultSize);
    }

    public BitString concat(BitString other) {
        if (other.isEmpty()) {
            return this;
        }
        int resultSize = size + other.size;
        int resultDataSize = (resultSize + 63) >>> 6;
        long[] resultData = new long[resultDataSize];
        int fullWordCount = size >>> 6;
        if (fullWordCount != 0) {
            System.arraycopy(data, 0, resultData, 0, fullWordCount);
        }
        int tailSize = size & 63;
        if (tailSize == 0) {
            System.arraycopy(other.data, fullWordCount, resultData, fullWordCount, other.data.length);
        } else {
            long wordPrefix = data[fullWordCount];
            int suffixSize = 64 - tailSize;
            for (int i = 0; i < other.data.length; i++) {
                long wordSuffix = other.data[i] >>> tailSize;
                long word = wordPrefix | wordSuffix;
                resultData[fullWordCount + i] = word;
                wordPrefix = other.data[i] << suffixSize;
            }
            int otherTailSize = other.size & 63;
            if (tailSize + otherTailSize > 64) {
                resultData[resultDataSize - 1] = wordPrefix;
            }
        }
        return new BitString(resultData, resultSize);
    }

    public BitString substring(int from, int until) {
        if (from < 0 || until > size) {
            throw new IndexOutOfBoundsException();
        } else if (from > until) {
            throw new IllegalArgumentException("Invalid substring");
        } else if (from == until) {
            return EMPTY;
        } else if (from == 0 && until == size) {
            return this;
        }
        int resultSize = until - from;
        int resultDataSize = (resultSize + 63) >>> 6;
        int shift = from & 63;
        long[] resultData = new long[resultDataSize];
        int resultTailSize = resultSize & 63;
        int fromWordIndex = from >>> 6;
        int resultFullWordCount = resultSize >>> 6;
        if (shift == 0) {
            System.arraycopy(data, fromWordIndex, resultData, 0, resultFullWordCount);
            if (resultTailSize != 0) {
                int lastAffectedWordIndex = until >>> 6;
                long tailMask = -1L << (64 - resultTailSize);
                resultData[resultDataSize - 1] = data[lastAffectedWordIndex] & tailMask;
            }
        } else {
            int prefixSize = 64 - shift;
            long wordPrefix = data[fromWordIndex] << shift;
            int firstAlignedWordIndex = fromWordIndex + 1;
            for (int i = 0; i < resultFullWordCount; i++) {
                long dataWord = data[firstAlignedWordIndex + i];
                long wordSuffix = dataWord >>> prefixSize;
                long word = wordPrefix | wordSuffix;
                resultData[i] = word;
                wordPrefix = dataWord << shift;
            }
            if (resultTailSize != 0) {
                long word = wordPrefix;
                if (shift + resultTailSize > 64) {
                    int lastAffectedWordIndex = until >>> 6;
                    long dataWord = data[lastAffectedWordIndex];
                    long wordSuffix = dataWord >>> prefixSize;
                    word = wordPrefix | wordSuffix;
                }
                long wordMask = -1L << (64 - resultTailSize);
                word &= wordMask;
                resultData[resultDataSize - 1] = word;
            }
        }
        return new BitString(resultData, resultSize);
    }

    public BitString window(int from, int until) {
        if (from > until) {
            throw new IllegalArgumentException("Invalid substring");
        } else if (from == until) {
            return EMPTY;
        } else if (from == 0 && until == size) {
            return this;
        }
        int resultSize = until - from;
        int resultDataSize = (resultSize + 63) >>> 6;
        long[] resultData = new long[resultDataSize];
        if (size == 0 || until <= 0 || from >= size) {
            return new BitString(resultData, resultSize);
        }
        int shift = from & 63;
        if (shift == 0) {
            int fromWordIndex = from >> 6;
            int sourceStartWordIndex = Math.max(0, fromWordIndex);
            int targetStartWordIndex = Math.max(0, -fromWordIndex);
            int effectiveUntil = Math.min(size, until);
            int effectiveUntilFullWordIndex = effectiveUntil >>> 6;
            int copyFullWordCount = effectiveUntilFullWordIndex - sourceStartWordIndex;
            if (copyFullWordCount > 0) {
                System.arraycopy(data, sourceStartWordIndex, resultData, targetStartWordIndex, copyFullWordCount);
            }
            int effectiveTailSize = effectiveUntil & 63;
            if (effectiveTailSize != 0) {
                int resultTailWordIndex = (effectiveUntil - from) >>> 6;
                if (until < size) {
                    long tailMask = -1L << (64 - effectiveTailSize);
                    resultData[resultTailWordIndex] = data[effectiveUntilFullWordIndex] & tailMask;
                } else {
                    resultData[resultTailWordIndex] = data[effectiveUntilFullWordIndex];
                }
            }
        } else {
            int sourceStartWordIndex = Math.max(0, (from + 63) >> 6);
            int targetStartWordIndex = Math.max(0, -1 - (from >> 6));
            int effectiveUntil = Math.min(size, until);
            int effectiveTargetUntil = effectiveUntil - from;
            int resultEndFilledWordCount = effectiveTargetUntil >>> 6;
            int prefixSize = 64 - shift;
            long wordPrefix = sourceStartWordIndex > 0 ? data[sourceStartWordIndex - 1] << shift : 0L;
            int dataIndex = sourceStartWordIndex;
            for (int i = targetStartWordIndex; i < resultEndFilledWordCount; i++) {
                long dataWord = data[dataIndex];
                long wordSuffix = dataWord >>> prefixSize;
                long word = wordPrefix | wordSuffix;
                resultData[i] = word;
                wordPrefix = dataWord << shift;
                dataIndex++;
            }
            int effectiveTargetTail = effectiveTargetUntil & 63;
            if (effectiveTargetTail != 0) {
                long word = wordPrefix;
                if (effectiveTargetTail < prefixSize) {
                    long mask = -1L << (64 - effectiveTargetTail);
                    word &= mask;
                } else if (effectiveTargetTail != prefixSize && dataIndex < data.length) {
                    long dataWord = data[dataIndex];
                    long wordSuffix = dataWord >>> prefixSize;
                    long mask = -1L << (64 - effectiveTargetTail);
                    word = (word | wordSuffix) & mask;
                }
                resultData[resultEndFilledWordCount] = word;
            }
        }
        return new BitString(resultData, resultSize);
    }

    public BitString padLeft(int minSize) {
        if (minSize <= size) {
            return this;
        }
        int newDataSize = (minSize + 63) >>> 6;
        long[] newData = new long[newDataSize];
        int padSize = minSize - size;
        int firstFilledResultWordIndex = padSize >>> 6;
        if ((padSize & 63) == 0) {
            System.arraycopy(data, 0, newData, firstFilledResultWordIndex, data.length);
        } else {
            long wordPrefix = 0L;
            int prefixSize = padSize & 63;
            int suffixSize = 64 - prefixSize;
            for (int i = 0; i < data.length; i++) {
                long dataWord = data[i];
                long wordSuffix = dataWord >>> prefixSize;
                long word = wordPrefix | wordSuffix;
                newData[firstFilledResultWordIndex + i] = word;
                wordPrefix = dataWord << suffixSize;
            }
            int tailSize = size & 63;
            if (prefixSize + tailSize > 64) {
                newData[newDataSize - 1] = wordPrefix;
            }
        }
        return new BitString(newData, minSize);
    }

    public BitString padRight(int minSize) {
        if (minSize <= size) {
            return this;
        }
        int newDataSize = (minSize + 63) >>> 6;
        long[] newData = new long[newDataSize];
        System.arraycopy(data, 0, newData, 0, data.length);
        return new BitString(newData, minSize);
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

    public long toLong() {
        if (size == 0) {
            return 0L;
        }
        int tailSize = size & 63;
        long word = data[data.length - 1] >>> (64 - tailSize);
        if (data.length == 1) {
            return word;
        }
        word |= data[data.length - 2] << tailSize;
        return word;
    }

    public boolean[] toBooleanArray() {
        boolean[] result = new boolean[size];
        int fullWordSize = size >>> 6;
        long mask = -9223372036854775808L;
        for (int i = 0; i < fullWordSize; i++) {
            long word = data[i];
            int startBitIndex = i << 6;
            result[startBitIndex] = (word & mask) != 0;
            for (int j = 1; j < 64; j++) {
                word <<= 1;
                int bitIndex = startBitIndex + j;
                result[bitIndex] = (word & mask) != 0;
            }
        }
        int tailSize = size & 63;
        if (tailSize != 0) {
            long word = data[fullWordSize];
            int startBitIndex = fullWordSize << 6;
            result[startBitIndex] = (word & mask) != 0;
            for (int i = 1; i < tailSize; i++) {
                word <<= 1;
                int bitIndex = startBitIndex + i;
                result[bitIndex] = (word & mask) != 0;
            }
        }
        return result;
    }

    public byte[] toByteArrayLeftAligned() {
        int byteCount = (size + 7) >>> 3;
        byte[] result = new byte[byteCount];
        int fullWordCount = size >>> 6;
        for (int i = 0; i < fullWordCount; i++) {
            long word = data[i];
            int fromByteIndex = i << 3;
            for (int j = 0; j < 8; j++) {
                int byteIndex = fromByteIndex + j;
                int shift = 56 - (j << 3);
                result[byteIndex] = (byte) ((word >>> shift) & 255);
            }
        }
        int tailByteCount = byteCount & 7;
        if (tailByteCount > 0) {
            long word = data[fullWordCount];
            int fromByteIndex = fullWordCount << 3;
            for (int i = 0; i < tailByteCount; i++) {
                int byteIndex = fromByteIndex + i;
                int shift = 56 - (i << 3);
                result[byteIndex] = (byte) ((word >>> shift) & 255);
            }
        }
        return result;
    }

    public byte[] toByteArrayRightAligned() {
        int byteTailSize = size & 7;
        if (byteTailSize == 0) {
            return toByteArrayLeftAligned();
        }
        int byteCount = (size + 7) >>> 3;
        byte[] result = new byte[byteCount];
        int fullWordCount = byteCount >>> 3;
        byte bytePrefix = 0;
        for (int i = 0; i < fullWordCount; i++) {
            long word = data[i];
            int fromByteIndex = i << 3;
            result[fromByteIndex] = (byte) ((word >>> (64 - byteTailSize)) | bytePrefix);
            for (int j = 1; j < 8; j++) {
                int byteIndex = fromByteIndex + j;
                int shift = 64 - byteTailSize - (j << 3);
                result[byteIndex] = (byte) ((word >>> shift) & 255);
            }
            bytePrefix = (byte) (word << byteTailSize);
        }
        int tailByteCount = byteCount & 7;
        if (tailByteCount > 0) {
            long word = data[fullWordCount];
            int fromByteIndex = fullWordCount << 3;
            result[fromByteIndex] = (byte) ((word >>> (64 - byteTailSize)) | bytePrefix);
            for (int i = 1; i < tailByteCount; i++) {
                int byteIndex = fromByteIndex + i;
                int shift = 64 - byteTailSize - (i << 3);
                result[byteIndex] = (byte) ((word >>> shift) & 255);
            }
        }
        return result;
    }

    public BigInteger toUnsignedBigInteger() {
        return new BigInteger(1, toByteArrayRightAligned());
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

