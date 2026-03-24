package hu.webarticum.miniconnect.lang;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * Simple immutable bit array implementation.
 *
 * It represents the bits in a long array with MSB first order:
 * the array layout is MSB first, and the MSB of each long corresponds
 * to the first logical bit it represents from the bit string.
 * The last long value, if incomplete (in case of length % 64 > 0),
 * contains the tail part in its MSB positions,
 * and the unused LSB positions are filled with zeros.
 */
public final class BitString implements Comparable<BitString>, Iterable<Boolean>, Serializable {

    private static final long serialVersionUID = 1436084935617731076L;

    private static final BitString EMPTY = new BitString(new long[0], 0);


    private final long[] data;

    private final int length;


    private BitString(long[] data, int length) {
        this.data = data;
        this.length = length;
    }

    public static BitString empty() {
        return EMPTY;
    }

    public static BitString of(boolean... bits) {
        int length = bits.length;
        int wordCount = (length + 63) >>> 6;
        long[] data = new long[wordCount];
        int fullWordCount = length >>> 6;
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
        int tailLength = length & 63;
        if (tailLength > 0) {
            long word = 0;
            int from = fullWordCount << 6;
            for (int j = from; j < length; j++) {
                if (bits[j]) {
                    word |= Long.MIN_VALUE >>> (j & 63);
                }
            }
            data[fullWordCount] = word;
        }
        return new BitString(data, length);
    }

    public static BitString of(byte[] bytes) {
        int byteCount = bytes.length;
        int length = byteCount << 3;
        int wordCount = (length + 63) >>> 6;
        long[] data = new long[wordCount];
        int fullWordCount = length >>> 6;
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
        return new BitString(data, length);
    }

    public static BitString of(long[] longs) {
        return new BitString(Arrays.copyOf(longs, longs.length), longs.length << 6);
    }

    public static BitString of(long[] longs, int length) {
        int ceilWordCount = (length + 63) >>> 6;
        int rawLength = longs.length << 6;
        if (length == rawLength) {
            return new BitString(Arrays.copyOf(longs, longs.length), length);
        } else if (length > rawLength) {
            long[] data = new long[ceilWordCount];
            System.arraycopy(longs, 0, data, 0, longs.length);
            return new BitString(data, length);
        } else {
            long[] data = new long[ceilWordCount];
            System.arraycopy(longs, 0, data, 0, ceilWordCount);
            int ceilLength = ceilWordCount << 6;
            if (length != ceilLength) {
                int fullWordCount = length >>> 6;
                int tailLength = length & 63;
                long tailMask = -1L << (64 - tailLength);
                data[fullWordCount] &= tailMask;
            }
            return new BitString(data, length);
        }
    }

    public static BitString of(String values) {
        int length = values.length();
        int wordCount = (length + 63) >>> 6;
        long[] data = new long[wordCount];
        int fullWordCount = length >>> 6;
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
        int tailLength = length & 63;
        if (tailLength > 0) {
            long word = 0;
            int from = fullWordCount << 6;
            for (int j = from; j < length; j++) {
                char c = values.charAt(j);
                if (c == '1') {
                    word |= Long.MIN_VALUE >>> (j & 63);
                } else if (c != '0') {
                    throw new IllegalArgumentException("Invalid character: '" + c + "' at " + j);
                }
            }
            data[fullWordCount] = word;
        }
        return new BitString(data, length);
    }

    public static Builder builder() {
        return new Builder();
    }


    public int length() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
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
        return Integer.compare(length, other.length);
    }

    @Override
    public Iterator<Boolean> iterator() {
        return new BitStringIterator();
    }

    @Override
    public int hashCode() {
        return (length * 37) + Arrays.hashCode(data);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BitString)) {
            return false;
        }
        BitString other = (BitString) obj;
        return length == other.length && Arrays.equals(data, other.data);
    }

    @Override
    public String toString() {
        StringBuilder resultBuilder = new StringBuilder(length());
        for (int i = 0; i < length(); i++) {
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
        int fullWordCount = length >>> 6;
        for (int i = 0; i < fullWordCount; i++) {
            long word = data[i];
            if (word != -1L) {
                return false;
            }
        }
        int tailLength = length & 63;
        if (tailLength > 0) {
            long lastWord = data[fullWordCount];
            long onePaddedLastWord = lastWord | (-1L >>> tailLength);
            if (onePaddedLastWord != -1L) {
                return false;
            }
        }
        return true;
    }

    public boolean get(int position) {
        if (position < 0 || position >= length) {
            return false;
        }
        int wordIndex = position >>> 6;
        long word = data[wordIndex];
        long mask = Long.MIN_VALUE >>> (position & 63);
        long masked = word & mask;
        return masked != 0;
    }

    public boolean getStrict(int position) {
        if (position < 0 || position >= length) {
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
        int wordIndex = position >>> 6;
        if (wordIndex >= data.length) {
            long[] newData = new long[wordIndex + 1];
            System.arraycopy(data, 0, newData, 0, data.length);
            if (value) {
                newData[wordIndex] = Long.MIN_VALUE >>> (position & 63);
            }
            return new BitString(newData, position + 1);
        }
        boolean lengthIncreased = position >= length;
        long word = data[wordIndex];
        long mask = Long.MIN_VALUE >>> (position & 63);
        long masked = word & mask;
        boolean wasSet = masked != 0;
        if (wasSet == value) {
            return lengthIncreased ? new BitString(data, position + 1) : this;
        }
        long changedWord = word ^ mask;
        long[] newData = replacedWord(data, wordIndex, changedWord);
        int newLength = lengthIncreased ? position + 1 : length;
        return new BitString(newData, newLength);
    }

    public BitString setStrict(int position, boolean value) {
        if (position < 0 || position >= length) {
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
        return new BitString(newData, length);
    }

    public BitString flip(int position) {
        if (position < 0) {
            throw new IndexOutOfBoundsException();
        }
        int wordIndex = position >>> 6;
        if (wordIndex >= data.length) {
            long[] newData = new long[wordIndex + 1];
            System.arraycopy(data, 0, newData, 0, data.length);
            newData[wordIndex] = Long.MIN_VALUE >>> (position & 63);
            return new BitString(newData, position + 1);
        }
        boolean lengthIncreased = position >= length;
        long word = data[wordIndex];
        long mask = Long.MIN_VALUE >>> (position & 63);
        long changedWord = word ^ mask;
        long[] newData = replacedWord(data, wordIndex, changedWord);
        int newLength = lengthIncreased ? position + 1 : length;
        return new BitString(newData, newLength);
    }

    public BitString flipStrict(int position) {
        if (position < 0 || position >= length) {
            throw new IndexOutOfBoundsException();
        }
        int wordIndex = position >>> 6;
        long word = data[wordIndex];
        long mask = Long.MIN_VALUE >>> (position & 63);
        long changedWord = word ^ mask;
        long[] newData = replacedWord(data, wordIndex, changedWord);
        return new BitString(newData, length);
    }

    private long[] replacedWord(long[] original, int wordIndex, long newWord) {
        if (original.length == 1) {
            return new long[] { newWord };
        }
        long[] result = new long[original.length];
        if (wordIndex != 0) {
            System.arraycopy(original, 0, result, 0, wordIndex);
        }
        result[wordIndex] = newWord;
        if (wordIndex < original.length - 1) {
            int from = wordIndex + 1;
            System.arraycopy(original, from, result, from, original.length - wordIndex - 1);
        }
        return result;
    }

    public BitString not() {
        long[] newData = new long[data.length];
        int fullWordCount = length >>> 6;
        for (int i = 0; i < fullWordCount; i++) {
            newData[i] = ~data[i];
        }
        if (data.length != fullWordCount) {
            int tailLength = length & 63;
            long tailMask = -1L << (64 - tailLength);
            newData[fullWordCount] = ~data[fullWordCount] & tailMask;
        }
        return new BitString(newData, length);
    }

    public BitString and(BitString other) {
        int resultLength = Math.max(length, other.length);
        int resultDataLength = Math.max(data.length, other.data.length);
        long[] resultData = new long[resultDataLength];
        int commonDataLength = Math.min(data.length, other.data.length);
        for (int i = 0; i < commonDataLength; i++) {
            resultData[i] = data[i] & other.data[i];
        }
        return new BitString(resultData, resultLength);
    }

    public BitString or(BitString other) {
        int resultLength = Math.max(length, other.length);
        int resultDataLength = Math.max(data.length, other.data.length);
        long[] resultData = new long[resultDataLength];
        int commonDataLength = Math.min(data.length, other.data.length);
        for (int i = 0; i < commonDataLength; i++) {
            resultData[i] = data[i] | other.data[i];
        }
        if (data.length > other.data.length) {
            System.arraycopy(data, commonDataLength, resultData, commonDataLength, resultDataLength - commonDataLength);
        } else if (data.length != other.data.length) {
            System.arraycopy(other.data, commonDataLength, resultData, commonDataLength, resultDataLength - commonDataLength);
        }
        return new BitString(resultData, resultLength);
    }

    public BitString xor(BitString other) {
        int resultLength = Math.max(length, other.length);
        int resultDataLength = Math.max(data.length, other.data.length);
        long[] resultData = new long[resultDataLength];
        int commonDataLength = Math.min(data.length, other.data.length);
        for (int i = 0; i < commonDataLength; i++) {
            resultData[i] = data[i] ^ other.data[i];
        }
        if (data.length > other.data.length) {
            System.arraycopy(data, commonDataLength, resultData, commonDataLength, resultDataLength - commonDataLength);
        } else if (data.length != other.data.length) {
            System.arraycopy(other.data, commonDataLength, resultData, commonDataLength, resultDataLength - commonDataLength);
        }
        return new BitString(resultData, resultLength);
    }

    public BitString concat(BitString other) {
        if (other.length == 0) {
            return this;
        } else if (length == 0) {
            return other;
        }
        int resultLength = length + other.length;
        int resultDataLength = (resultLength + 63) >>> 6;
        long[] resultData = new long[resultDataLength];
        int fullWordCount = length >>> 6;
        if (fullWordCount != 0) {
            System.arraycopy(data, 0, resultData, 0, fullWordCount);
        }
        int tailLength = length & 63;
        if (tailLength == 0) {
            System.arraycopy(other.data, fullWordCount, resultData, fullWordCount, other.data.length);
        } else {
            long wordPrefix = data[fullWordCount];
            int suffixLength = 64 - tailLength;
            for (int i = 0; i < other.data.length; i++) {
                long wordSuffix = other.data[i] >>> tailLength;
                long word = wordPrefix | wordSuffix;
                resultData[fullWordCount + i] = word;
                wordPrefix = other.data[i] << suffixLength;
            }
            int otherTailLength = other.length & 63;
            if (tailLength + otherTailLength > 64) {
                resultData[resultDataLength - 1] = wordPrefix;
            }
        }
        return new BitString(resultData, resultLength);
    }

    public BitString substring(int from, int until) {
        if (from < 0 || until > length) {
            throw new IndexOutOfBoundsException();
        } else if (from > until) {
            throw new IllegalArgumentException("Invalid substring");
        } else if (from == until) {
            return EMPTY;
        } else if (from == 0 && until == length) {
            return this;
        }
        int resultLength = until - from;
        int resultDataLength = (resultLength + 63) >>> 6;
        int shift = from & 63;
        long[] resultData = new long[resultDataLength];
        int resultTailLength = resultLength & 63;
        int fromWordIndex = from >>> 6;
        int resultFullWordCount = resultLength >>> 6;
        if (shift == 0) {
            System.arraycopy(data, fromWordIndex, resultData, 0, resultFullWordCount);
            if (resultTailLength != 0) {
                int lastAffectedWordIndex = until >>> 6;
                long tailMask = -1L << (64 - resultTailLength);
                resultData[resultDataLength - 1] = data[lastAffectedWordIndex] & tailMask;
            }
        } else {
            int prefixLength = 64 - shift;
            long wordPrefix = data[fromWordIndex] << shift;
            int firstAlignedWordIndex = fromWordIndex + 1;
            for (int i = 0; i < resultFullWordCount; i++) {
                long dataWord = data[firstAlignedWordIndex + i];
                long wordSuffix = dataWord >>> prefixLength;
                long word = wordPrefix | wordSuffix;
                resultData[i] = word;
                wordPrefix = dataWord << shift;
            }
            if (resultTailLength != 0) {
                long word = wordPrefix;
                if (shift + resultTailLength > 64) {
                    int lastAffectedWordIndex = until >>> 6;
                    long dataWord = data[lastAffectedWordIndex];
                    long wordSuffix = dataWord >>> prefixLength;
                    word = wordPrefix | wordSuffix;
                }
                long wordMask = -1L << (64 - resultTailLength);
                word &= wordMask;
                resultData[resultDataLength - 1] = word;
            }
        }
        return new BitString(resultData, resultLength);
    }

    public BitString substring(int from) {
        return substring(from, length);
    }

    public BitString window(int from, int until) {
        if (from > until) {
            throw new IllegalArgumentException("Invalid substring");
        } else if (from == until) {
            return EMPTY;
        } else if (from == 0 && until == length) {
            return this;
        }
        int resultLength = until - from;
        int resultDataLength = (resultLength + 63) >>> 6;
        long[] resultData = new long[resultDataLength];
        if (length == 0 || until <= 0 || from >= length) {
            return new BitString(resultData, resultLength);
        }
        int shift = from & 63;
        if (shift == 0) {
            int fromWordIndex = from >> 6;
            int sourceStartWordIndex = Math.max(0, fromWordIndex);
            int targetStartWordIndex = Math.max(0, -fromWordIndex);
            int effectiveUntil = Math.min(length, until);
            int effectiveUntilFullWordIndex = effectiveUntil >>> 6;
            int copyFullWordCount = effectiveUntilFullWordIndex - sourceStartWordIndex;
            if (copyFullWordCount > 0) {
                System.arraycopy(data, sourceStartWordIndex, resultData, targetStartWordIndex, copyFullWordCount);
            }
            int effectiveTailLength = effectiveUntil & 63;
            if (effectiveTailLength != 0) {
                int resultTailWordIndex = (effectiveUntil - from) >>> 6;
                if (until < length) {
                    long tailMask = -1L << (64 - effectiveTailLength);
                    resultData[resultTailWordIndex] = data[effectiveUntilFullWordIndex] & tailMask;
                } else {
                    resultData[resultTailWordIndex] = data[effectiveUntilFullWordIndex];
                }
            }
        } else {
            int sourceStartWordIndex = Math.max(0, (from + 63) >> 6);
            int targetStartWordIndex = Math.max(0, -1 - (from >> 6));
            int effectiveUntil = Math.min(length, until);
            int effectiveTargetUntil = effectiveUntil - from;
            int resultEndFilledWordCount = effectiveTargetUntil >>> 6;
            int prefixLength = 64 - shift;
            long wordPrefix = sourceStartWordIndex > 0 ? data[sourceStartWordIndex - 1] << shift : 0L;
            int dataIndex = sourceStartWordIndex;
            for (int i = targetStartWordIndex; i < resultEndFilledWordCount; i++) {
                long dataWord = data[dataIndex];
                long wordSuffix = dataWord >>> prefixLength;
                long word = wordPrefix | wordSuffix;
                resultData[i] = word;
                wordPrefix = dataWord << shift;
                dataIndex++;
            }
            int effectiveTargetTail = effectiveTargetUntil & 63;
            if (effectiveTargetTail != 0) {
                long word = wordPrefix;
                if (effectiveTargetTail < prefixLength) {
                    long mask = -1L << (64 - effectiveTargetTail);
                    word &= mask;
                } else if (effectiveTargetTail != prefixLength && dataIndex < data.length) {
                    long dataWord = data[dataIndex];
                    long wordSuffix = dataWord >>> prefixLength;
                    long mask = -1L << (64 - effectiveTargetTail);
                    word = (word | wordSuffix) & mask;
                }
                resultData[resultEndFilledWordCount] = word;
            }
        }
        return new BitString(resultData, resultLength);
    }

    public BitString shiftLeft(int shift) {
        if (length == 0 || shift == 0) {
            return this;
        } else if (shift < 0 || shift >= length) {
            return new BitString(new long[data.length], length);
        } else if (data.length == 1) {
            long mask = -1L << (64 - length);
            long resultWord = (data[0] << shift) & mask;
            return new BitString(new long[] { resultWord }, length);
        }
        long[] resultData = new long[data.length];
        int shiftWordCount = shift >>> 6;
        int inWordShift = shift & 63;
        int tailLength = length & 63;
        if (inWordShift == 0) {
            int copyWordCount = data.length - shiftWordCount;
            System.arraycopy(data, shiftWordCount, resultData, 0, copyWordCount);
            if (tailLength != 0) {
                long mask = -1L << (64 - tailLength);
                resultData[0] = resultData[0] & mask;
            }
        } else {
            int innerWordCount = data.length - 1;
            int shiftWordCountCeiled = shiftWordCount + 1;
            int intersectionWordCount = innerWordCount - shiftWordCount;
            int fullTailLength = tailLength == 0 ? 64 : tailLength;
            int prefixLength = 64 - inWordShift;
            long dataWord = data[shiftWordCount];
            for (int i = 0; i < intersectionWordCount; i++) {
                long wordPrefix = dataWord << inWordShift;
                dataWord = data[i + shiftWordCountCeiled];
                long wordSuffix = dataWord >>> prefixLength;
                resultData[i] = wordPrefix | wordSuffix;
            }
            if (inWordShift < fullTailLength) {
                long wordPrefix = dataWord << inWordShift;
                resultData[intersectionWordCount] = wordPrefix;
            }
        }
        return new BitString(resultData, length);
    }

    public BitString shiftRight(int shift) {
        if (length == 0 || shift == 0) {
            return this;
        } else if (shift < 0 || shift >= length) {
            return new BitString(new long[data.length], length);
        } else if (data.length == 1) {
            long mask = -1L << (64 - length);
            long resultWord = (data[0] >>> shift) & mask;
            return new BitString(new long[] { resultWord }, length);
        }
        long[] resultData = new long[data.length];
        int shiftWordCount = shift >>> 6;
        int inWordShift = shift & 63;
        int tailLength = length & 63;
        if (inWordShift == 0) {
            int copyWordCount = data.length - shiftWordCount;
            System.arraycopy(data, 0, resultData, shiftWordCount, copyWordCount);
            if (tailLength != 0) {
                int lastWordIndex = data.length - 1;
                long mask = -1L << (64 - tailLength);
                resultData[lastWordIndex] = resultData[lastWordIndex] & mask;
            }
        } else {
            int fullWordCount = length >>> 6;
            int commonEndFilledWordCount = fullWordCount - shiftWordCount;
            int suffixLength = 64 - inWordShift;
            long wordPrefix = 0;
            for (int i = 0; i < commonEndFilledWordCount; i++) {
                long dataWord = data[i];
                long wordSuffix = dataWord >>> inWordShift;
                resultData[i + shiftWordCount] = wordPrefix | wordSuffix;
                wordPrefix = dataWord << suffixLength;
            }
            if (tailLength != 0) {
                int lastWordIndex = data.length - 1;
                long mask = -1L << (64 - tailLength);
                if (inWordShift >= tailLength) {
                    resultData[lastWordIndex] = wordPrefix & mask;
                } else {
                    long wordSuffix = data[commonEndFilledWordCount] >>> inWordShift;
                    resultData[lastWordIndex] = (wordPrefix | wordSuffix) & mask;
                }
            }
        }
        return new BitString(resultData, length);
    }

    public BitString padLeft(int minLength) {
        if (minLength <= length) {
            return this;
        }
        int newDataLength = (minLength + 63) >>> 6;
        long[] newData = new long[newDataLength];
        int padLength = minLength - length;
        int firstFilledResultWordIndex = padLength >>> 6;
        if ((padLength & 63) == 0) {
            System.arraycopy(data, 0, newData, firstFilledResultWordIndex, data.length);
        } else {
            long wordPrefix = 0L;
            int prefixLength = padLength & 63;
            int suffixLength = 64 - prefixLength;
            for (int i = 0; i < data.length; i++) {
                long dataWord = data[i];
                long wordSuffix = dataWord >>> prefixLength;
                long word = wordPrefix | wordSuffix;
                newData[firstFilledResultWordIndex + i] = word;
                wordPrefix = dataWord << suffixLength;
            }
            int tailLength = length & 63;
            if (prefixLength + tailLength > 64) {
                newData[newDataLength - 1] = wordPrefix;
            }
        }
        return new BitString(newData, minLength);
    }

    public BitString padRight(int minLength) {
        if (minLength <= length) {
            return this;
        }
        int newDataLength = (minLength + 63) >>> 6;
        long[] newData = new long[newDataLength];
        System.arraycopy(data, 0, newData, 0, data.length);
        return new BitString(newData, minLength);
    }

    public BitString resize(int newLength) {
        if (newLength > length) {
            int newDatadLength = (newLength + 63) >>> 6;
            long[] newData = new long[newDatadLength];
            System.arraycopy(data, 0, newData, 0, data.length);
            return new BitString(newData, newLength);
        } else if (newLength == length) {
            return this;
        } else if (newLength < 0) {
            throw new IllegalArgumentException("Length must not be negative");
        } else {
            int newDatadLength = (newLength + 63) >>> 6;
            long[] newData = new long[newDatadLength];
            int fullWordLength = newLength >>> 6;
            if (fullWordLength != 0) {
                System.arraycopy(data, 0, newData, 0, fullWordLength);
            }
            int newTailLength = newLength & 63;
            if (newTailLength != 0) {
                long tailMask = -1L << (64 - newTailLength);
                newData[fullWordLength] = data[fullWordLength] & tailMask;
            }
            return new BitString(newData, newLength);
        }
    }

    public BitString reverse() {
        if (length == 0) {
            return this;
        }
        long[] newData = new long[data.length];
        int tailLength = length & 63;
        if (tailLength == 0) {
            for (int i = 0; i < data.length; i++) {
                newData[i] = Long.reverse(data[data.length - i - 1]);
            }
        } else {
            int fullWordCount = data.length - 1;
            int suffixLength = 64 - tailLength;
            long wordPrefix = Long.reverse(data[fullWordCount]) << suffixLength;
            for (int i = 0; i < fullWordCount; i++) {
                long reversedDataWord = Long.reverse(data[fullWordCount - i - 1]);
                long wordSuffix = reversedDataWord >>> tailLength;
                newData[i] = wordPrefix | wordSuffix;
                wordPrefix = reversedDataWord << suffixLength;
            }
            newData[fullWordCount] = wordPrefix;
        }
        return new BitString(newData, length);
    }

    public int indexOf(boolean bit) {
        return bit ? indexOfOne() : indexOfZero();
    }

    public int indexOf(boolean bit, int fromIndex) {
        return bit ? indexOfOne(fromIndex) : indexOfZero(fromIndex);
    }

    public int indexOfOne() {
        for (int i = 0; i < data.length; i++) {
            int pos = Long.numberOfLeadingZeros(data[i]);
            if (pos != 64) {
                return (i << 6) + pos;
            }
        }
        return -1;
    }

    public int indexOfOne(int fromIndex) {
        if (fromIndex >= length) {
            return -1;
        } else if (fromIndex < 0) {
            fromIndex = 0;
        }
        int fromWordIndex = fromIndex >>> 6;
        int fromFullWordIndex = (fromIndex + 63) >>> 6;
        if (fromWordIndex != fromFullWordIndex) {
            long mask = -1L >>> (fromIndex & 63);
            int pos = Long.numberOfLeadingZeros(data[fromWordIndex] & mask);
            if (pos != 64) {
                return (fromWordIndex << 6) + pos;
            }
        }
        for (int i = fromFullWordIndex; i < data.length; i++) {
            int pos = Long.numberOfLeadingZeros(data[i]);
            if (pos != 64) {
                return (i << 6) + pos;
            }
        }
        return -1;
    }

    public int indexOfZero() {
        int fullWordCount = length >>> 6;
        for (int i = 0; i < fullWordCount; i++) {
            int pos = Long.numberOfLeadingZeros(~data[i]);
            if (pos != 64) {
                return (i << 6) + pos;
            }
        }
        int tailLength = length & 63;
        if (tailLength != 0) {
            long mask = -1L << (64 - tailLength);
            long invertedTailWord = (~data[fullWordCount]) & mask;
            int pos = Long.numberOfLeadingZeros(invertedTailWord);
            if (pos != 64) {
                return (fullWordCount << 6) + pos;
            }
        }
        return -1;
    }

    public int indexOfZero(int fromIndex) {
        if (fromIndex >= length) {
            return -1;
        } else if (fromIndex < 0) {
            fromIndex = 0;
        }
        int tailLength = length & 63;
        int fromWordIndex = fromIndex >>> 6;
        int fromFullWordIndex = (fromIndex + 63) >>> 6;
        if (fromWordIndex != fromFullWordIndex) {
            boolean isLast = (fromFullWordIndex == data.length);
            long fromMask = -1L >>> (fromIndex & 63);
            long invertedWord = (~data[fromWordIndex]) & fromMask;
            if (isLast && tailLength != 0) {
                invertedWord &= -1L << (64 - tailLength);
            }
            int pos = Long.numberOfLeadingZeros(invertedWord);
            if (pos != 64) {
                return (fromWordIndex << 6) + pos;
            } else if (isLast) {
                return -1;
            }
        }
        int fullWordCount = length >>> 6;
        for (int i = fromFullWordIndex; i < fullWordCount; i++) {
            int pos = Long.numberOfLeadingZeros(~data[i]);
            if (pos != 64) {
                return (i << 6) + pos;
            }
        }
        if (tailLength != 0) {
            long mask = -1L << (64 - tailLength);
            long invertedTailWord = (~data[fullWordCount]) & mask;
            int pos = Long.numberOfLeadingZeros(invertedTailWord);
            if (pos != 64) {
                return (fullWordCount << 6) + pos;
            }
        }
        return -1;
    }

    public int indexOf(char bitChar) {
        if (bitChar == '1') {
            return indexOfOne();
        } else if (bitChar == '0') {
            return indexOfZero();
        } else {
            throw new IllegalArgumentException("Invalid character: '" + bitChar + "'");
        }
    }

    public int indexOf(char bitChar, int fromIndex) {
        if (bitChar == '1') {
            return indexOfOne(fromIndex);
        } else if (bitChar == '0') {
            return indexOfZero(fromIndex);
        } else {
            throw new IllegalArgumentException("Invalid character: '" + bitChar + "'");
        }
    }

    public int lastIndexOf(boolean bit) {
        return bit ? lastIndexOfOne() : lastIndexOfZero();
    }

    public int lastIndexOf(boolean bit, int fromIndex) {
        return bit ? lastIndexOfOne(fromIndex) : lastIndexOfZero(fromIndex);
    }

    public int lastIndexOfOne() {
        for (int i = data.length - 1; i >= 0; i--) {
            int pad = Long.numberOfTrailingZeros(data[i]);
            if (pad != 64) {
                int pos = 64 - pad - 1;
                return (i << 6) + pos;
            }
        }
        return -1;
    }

    public int lastIndexOfOne(int fromIndex) {
        if (fromIndex >= length) {
            fromIndex = length - 1;
        }
        if (fromIndex < 0) {
            return -1;
        }
        int effectiveLength = fromIndex + 1;
        int fullWordCount = effectiveLength >>> 6;
        int tailLength = effectiveLength & 63;
        if (tailLength != 0) {
            long mask = -1L << (64 - tailLength);
            long tailWord = data[fullWordCount] & mask;
            int pad = Long.numberOfTrailingZeros(tailWord);
            if (pad != 64) {
                int pos = 64 - pad - 1;
                return (fullWordCount << 6) + pos;
            }
        }
        for (int i = fullWordCount - 1; i >= 0; i--) {
            int pad = Long.numberOfTrailingZeros(data[i]);
            if (pad != 64) {
                int pos = 64 - pad - 1;
                return (i << 6) + pos;
            }
        }
        return -1;
    }

    public int lastIndexOfZero() {
        int fullWordCount = length >>> 6;
        int tailLength = length & 63;
        if (tailLength != 0) {
            long mask = -1L << (64 - tailLength);
            long invertedTailWord = (~data[fullWordCount]) & mask;
            int pad = Long.numberOfTrailingZeros(invertedTailWord);
            if (pad != 64) {
                int pos = 64 - pad - 1;
                return (fullWordCount << 6) + pos;
            }
        }
        for (int i = fullWordCount - 1; i >= 0; i--) {
            int pad = Long.numberOfTrailingZeros(~data[i]);
            if (pad != 64) {
                int pos = 64 - pad - 1;
                return (i << 6) + pos;
            }
        }
        return -1;
    }

    public int lastIndexOfZero(int fromIndex) {
        if (fromIndex >= length) {
            fromIndex = length - 1;
        }
        if (fromIndex < 0) {
            return -1;
        }
        int effectiveLength = fromIndex + 1;
        int fullWordCount = effectiveLength >>> 6;
        int tailLength = effectiveLength & 63;
        if (tailLength != 0) {
            long mask = -1L << (64 - tailLength);
            long invertedTailWord = (~data[fullWordCount]) & mask;
            int pad = Long.numberOfTrailingZeros(invertedTailWord);
            if (pad != 64) {
                int pos = 64 - pad - 1;
                return (fullWordCount << 6) + pos;
            }
        }
        for (int i = fullWordCount - 1; i >= 0; i--) {
            int pad = Long.numberOfTrailingZeros(~data[i]);
            if (pad != 64) {
                int pos = 64 - pad - 1;
                return (i << 6) + pos;
            }
        }
        return -1;
    }

    public int lastIndexOf(char bitChar) {
        if (bitChar == '1') {
            return lastIndexOfOne();
        } else if (bitChar == '0') {
            return lastIndexOfZero();
        } else {
            throw new IllegalArgumentException("Invalid character: '" + bitChar + "'");
        }
    }

    public int lastIndexOf(char bitChar, int fromIndex) {
        if (bitChar == '1') {
            return lastIndexOfOne(fromIndex);
        } else if (bitChar == '0') {
            return lastIndexOfZero(fromIndex);
        } else {
            throw new IllegalArgumentException("Invalid character: '" + bitChar + "'");
        }
    }

    public int indexOf(BitString substring) {
        return indexOfInternal(substring, 0);
    }

    public int indexOf(BitString substring, int fromIndex) {
        return indexOfInternal(substring, Math.max(0, fromIndex));
    }

    private int indexOfInternal(BitString substring, int fromIndex) {
        if (substring.length == 0) {
            return fromIndex > length ? -1 : fromIndex;
        } else if (length == 0) {
            return -1;
        } else if (substring.length == 1) {
            return indexOf(substring.get(0), fromIndex);
        } else if (substring.length == length) {
            return equals(substring) ? 0 : -1;
        } else if (substring.length > length) {
            return -1;
        }
        int maxValidPosition = length - substring.length;
        for (int i = fromIndex; i <= maxValidPosition; i++) {
            if (matchInternal(substring, i)) {
                return i;
            }
        }
        return -1;
    }

    public int lastIndexOf(BitString substring) {
        return lastIndexOfInternal(substring, length - substring.length);
    }

    public int lastIndexOf(BitString substring, int fromIndex) {
        return lastIndexOfInternal(substring, Math.min(length - substring.length, fromIndex));
    }

    private int lastIndexOfInternal(BitString substring, int fromIndex) {
        if (substring.length == 0) {
            return fromIndex < 0 ? -1 : fromIndex;
        } else if (length == 0) {
            return -1;
        } else if (substring.length == 1) {
            return lastIndexOf(substring.get(0), fromIndex);
        } else if (substring.length == length) {
            return equals(substring) ? 0 : -1;
        } else if (substring.length > length) {
            return -1;
        }
        for (int i = fromIndex; i >= 0; i--) {
            if (matchInternal(substring, i)) {
                return i;
            }
        }
        return -1;
    }

    public boolean startsWith(BitString substring) {
        return match(substring, 0);
    }

    public boolean endsWith(BitString substring) {
        return match(substring, length - substring.length);
    }

    public boolean match(BitString substring, int position) {
        if (position > length || position < 0) {
            return false;
        } else if (substring.length == 0) {
            return true;
        } else if (position + substring.length > length) {
            return false;
        }
        return matchInternal(substring, position);
    }

    public int countOfOnes() {
        int result = 0;
        for (long word : data) {
            result += Long.bitCount(word);
        }
        return result;
    }

    public int countOfZeros() {
        return length - countOfOnes();
    }

    private boolean matchInternal(BitString substring, int position) {
        int substringDataLength = (substring.length + 63) >>> 6;
        int shift = position & 63;
        int substringTailLength = substring.length & 63;
        int fromWordIndex = position >>> 6;
        int substringFullWordCount = substring.length >>> 6;
        if (shift == 0) {
            for (int i = 0; i < substringFullWordCount; i++) {
                if (substring.data[i] != data[fromWordIndex + i]) {
                    return false;
                }
            }
            if (substringTailLength != 0) {
                int until = position + substring.length;
                int lastAffectedWordIndex = until >>> 6;
                long tailMask = -1L << (64 - substringTailLength);
                long word = data[lastAffectedWordIndex] & tailMask;
                if (substring.data[substringDataLength - 1] != word) {
                    return false;
                }
            }
        } else {
            int prefixLength = 64 - shift;
            long wordPrefix = data[fromWordIndex] << shift;
            int firstAlignedWordIndex = fromWordIndex + 1;
            for (int i = 0; i < substringFullWordCount; i++) {
                long dataWord = data[firstAlignedWordIndex + i];
                long wordSuffix = dataWord >>> prefixLength;
                long word = wordPrefix | wordSuffix;
                if (substring.data[i] != word) {
                    return false;
                }
                wordPrefix = dataWord << shift;
            }
            if (substringTailLength != 0) {
                long word = wordPrefix;
                if (shift + substringTailLength > 64) {
                    int until = position + substring.length;
                    int lastAffectedWordIndex = until >>> 6;
                    long dataWord = data[lastAffectedWordIndex];
                    long wordSuffix = dataWord >>> prefixLength;
                    word = wordPrefix | wordSuffix;
                }
                long wordMask = -1L << (64 - substringTailLength);
                word &= wordMask;
                if (substring.data[substringDataLength - 1] != word) {
                    return false;
                }
            }
        }
        return true;
    }

    public long toLong() {
        if (length == 0) {
            return 0L;
        }
        int tailLength = length & 63;
        long word = data[data.length - 1] >>> (64 - tailLength);
        if (data.length == 1) {
            return word;
        }
        word |= data[data.length - 2] << tailLength;
        return word;
    }

    public boolean[] toBooleanArray() {
        boolean[] result = new boolean[length];
        int fullWordLength = length >>> 6;
        long mask = -9223372036854775808L;
        for (int i = 0; i < fullWordLength; i++) {
            long word = data[i];
            int startBitIndex = i << 6;
            result[startBitIndex] = (word & mask) != 0;
            for (int j = 1; j < 64; j++) {
                word <<= 1;
                int bitIndex = startBitIndex + j;
                result[bitIndex] = (word & mask) != 0;
            }
        }
        int tailLength = length & 63;
        if (tailLength != 0) {
            long word = data[fullWordLength];
            int startBitIndex = fullWordLength << 6;
            result[startBitIndex] = (word & mask) != 0;
            for (int i = 1; i < tailLength; i++) {
                word <<= 1;
                int bitIndex = startBitIndex + i;
                result[bitIndex] = (word & mask) != 0;
            }
        }
        return result;
    }

    public byte[] toByteArrayLeftAligned() {
        int byteCount = (length + 7) >>> 3;
        byte[] result = new byte[byteCount];
        int fullWordCount = length >>> 6;
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
        int byteTailLength = length & 7;
        if (byteTailLength == 0) {
            return toByteArrayLeftAligned();
        }
        int byteCount = (length + 7) >>> 3;
        byte[] result = new byte[byteCount];
        int fullWordCount = byteCount >>> 3;
        byte bytePrefix = 0;
        for (int i = 0; i < fullWordCount; i++) {
            long word = data[i];
            int fromByteIndex = i << 3;
            result[fromByteIndex] = (byte) ((word >>> (64 - byteTailLength)) | bytePrefix);
            for (int j = 1; j < 8; j++) {
                int byteIndex = fromByteIndex + j;
                int shift = 64 - byteTailLength - (j << 3);
                result[byteIndex] = (byte) ((word >>> shift) & 255);
            }
            bytePrefix = (byte) (word << byteTailLength);
        }
        int tailByteCount = byteCount & 7;
        if (tailByteCount > 0) {
            long word = data[fullWordCount];
            int fromByteIndex = fullWordCount << 3;
            result[fromByteIndex] = (byte) ((word >>> (64 - byteTailLength)) | bytePrefix);
            for (int i = 1; i < tailByteCount; i++) {
                int byteIndex = fromByteIndex + i;
                int shift = 64 - byteTailLength - (i << 3);
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
            return position < length;
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


    public static class Builder {

        private final LinkedList<long[]> sections = new LinkedList<>();

        private int length = 0;


        private Builder() {
            // use BitString.builder() instead
        }


        public Builder append(BitString bitsToAppend) {
            int increment = bitsToAppend.length;
            if (increment == 0) {
                return this;
            }
            int tailLength = length & 63;
            int newLength = length + increment;
            if (tailLength == 0) {
                sections.add(Arrays.copyOf(bitsToAppend.data, bitsToAppend.data.length));
            } else {
                long[] lastSection = sections.getLast();
                lastSection[lastSection.length - 1] |= bitsToAppend.data[0] >>> tailLength;
                int suffixLength = 64 - tailLength;
                if (increment > suffixLength) {
                    int wordCount = (length + 63) >>> 6;
                    int newWordCount = (newLength + 63) >>> 6;
                    int nextWordCount = newWordCount - wordCount;
                    long[] nextSection = new long[nextWordCount];
                    int newFullWordCount = newLength >>> 6;
                    int nextFullWordCount = newFullWordCount - wordCount;
                    long wordPrefix = bitsToAppend.data[0] << suffixLength;
                    for (int i = 0; i < nextFullWordCount; i++) {
                        long sourceWord = bitsToAppend.data[i + 1];
                        long wordSuffix = sourceWord >>> tailLength;
                        nextSection[i] = wordPrefix | wordSuffix;
                        wordPrefix = sourceWord << suffixLength;
                    }
                    if (nextFullWordCount != nextWordCount) {
                        long lastNewWord = wordPrefix;
                        if (bitsToAppend.data.length != nextWordCount) {
                            long wordSuffix = bitsToAppend.data[nextWordCount] >>> tailLength;
                            lastNewWord |= wordSuffix;
                        }
                        nextSection[nextFullWordCount] = lastNewWord;
                    }
                    sections.add(nextSection);
                }
            }
            length = newLength;
            return this;
        }

        public Builder append(boolean[] bitsToAppend) {
            return append(BitString.of(bitsToAppend));
        }

        public Builder append(String bitsToAppend) {
            return append(BitString.of(bitsToAppend));
        }

        public Builder append(boolean bitToAppend) {
            int tailLength = length & 63;
            if (tailLength == 0) {
                long newWord = bitToAppend ? -9223372036854775808L : 0L;
                sections.add(new long[] { newWord });
            } else if (bitToAppend) {
                long[] lastSection = sections.getLast();
                lastSection[lastSection.length - 1] |= 1L << (64 - tailLength - 1);
            }
            length++;
            return this;
        }

        public Builder append(char bitChar) {
            if (bitChar == '1') {
                return append(true);
            } else if (bitChar == '0') {
                return append(false);
            } else {
                throw new IllegalArgumentException("Invalid character: '" + bitChar + "'");
            }
        }

        public BitString build() {
            int wordCount = (length + 63) >>> 6;
            long[] data = new long[wordCount];
            int pos = 0;
            for (long[] section : sections) {
                System.arraycopy(section, 0, data, pos, section.length);
                pos += section.length;
            }
            return new BitString(data, length);
        }

    }

}

