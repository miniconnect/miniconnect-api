package hu.webarticum.miniconnect.lang;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ImmutableList<T> implements ReversibleIterable<T>, Serializable {

    private static final long serialVersionUID = 1L;


    private final List<T> data; // NOSONAR


    private ImmutableList(List<T> data) {
        this.data = data;
    }

    
    public static <T> ImmutableList<T> empty() {
        return new ImmutableList<>(Collections.emptyList());
    }

    @SafeVarargs
    public static <T> ImmutableList<T> of(T... items) {
        return new ImmutableList<>(Arrays.asList(items));
    }

    public static <T> ImmutableList<T> fromCollection(Collection<T> collection) {
        return new ImmutableList<>(new ArrayList<>(collection));
    }

    @SuppressWarnings("unchecked")
    public static <T> ImmutableList<T> fromIterable(Iterable<? extends T> iterable) {
        if (iterable instanceof ImmutableList) {
            return (ImmutableList<T>) iterable;
        } else if (iterable instanceof Collection) {
            return fromCollection((Collection<T>) iterable);
        }
        
        return fromIterator(iterable.iterator());
    }

    public static <T> ImmutableList<T> fromIterator(Iterator<? extends T> iterator) {
        List<T> data = new ArrayList<>();
        iterator.forEachRemaining(data::add);
        return new ImmutableList<>(data);
    }

    public static ImmutableList<Boolean> fromBooleanArray(boolean[] booleanArray) {
        return IntStream.range(0, booleanArray.length).mapToObj(i -> (Boolean) booleanArray[i]).collect(ImmutableList.createCollector());
    }

    public static ImmutableList<Byte> fromByteArray(byte[] byteArray) {
        return IntStream.range(0, byteArray.length).mapToObj(i -> (Byte) byteArray[i]).collect(ImmutableList.createCollector());
    }

    public static ImmutableList<Short> fromShortArray(short[] shortArray) {
        return IntStream.range(0, shortArray.length).mapToObj(i -> (Short) shortArray[i]).collect(ImmutableList.createCollector());
    }

    public static ImmutableList<Integer> fromIntArray(int[] intArray) {
        return of(Arrays.stream(intArray).boxed().toArray(Integer[]::new));
    }

    public static ImmutableList<Long> fromLongArray(int[] longArray) {
        return of(Arrays.stream(longArray).boxed().toArray(Long[]::new));
    }

    public static ImmutableList<Float> fromFloatArray(float[] floatArray) {
        return IntStream.range(0, floatArray.length).mapToObj(i -> (Float) floatArray[i]).collect(ImmutableList.createCollector());
    }

    public static ImmutableList<Double> fromDoubleArray(double[] doubleArray) {
        return of(Arrays.stream(doubleArray).boxed().toArray(Double[]::new));
    }

    public static ImmutableList<Character> fromCharArray(char[] charArray) {
        return IntStream.range(0, charArray.length).mapToObj(i -> (Character) charArray[i]).collect(ImmutableList.createCollector());
    }

    public static ImmutableList<Character> fromCharArray(String characters) {
        return fromCharArray(characters.toCharArray());
    }

    public static <T> ImmutableList<T> fill(int size, T item) {
        return fill(size, i -> item);
    }

    public static <T> ImmutableList<T> fill(int size, IntFunction<T> supplier) {
        List<T> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            data.add(supplier.apply(i));
        }
        return new ImmutableList<>(data);
    }


    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int size() {
        return data.size();
    }

    public T get(int index) {
        return data.get(index);
    }

    public T first() {
        return data.get(0);
    }

    public T last() {
        return data.get(data.size() - 1);
    }

    public boolean contains(Object item) {
        return data.contains(item);
    }

    public boolean containsAll(ImmutableList<?> items) {
        return data.containsAll(items.data);
    }

    public boolean containsAll(Collection<?> items) {
        return data.containsAll(items);
    }
    
    public int indexOf(T item) {
        return data.indexOf(item);
    }

    public int lastIndexOf(T item) {
        return data.lastIndexOf(item);
    }

    public <U> ImmutableList<U> map(Function<T, U> mapper) {
        List<U> mappedData = new ArrayList<>(data.size());
        for (T item : data) {
            mappedData.add(mapper.apply(item));
        }
        return new ImmutableList<>(mappedData);
    }

    public <U> ImmutableList<U> map(BiFunction<Integer, T, U> mapper) {
        List<U> mappedData = new ArrayList<>(data.size());
        int length = data.size();
        for (int i = 0; i < length; i++) {
            T item = data.get(i);
            mappedData.add(mapper.apply(i, item));
        }
        return new ImmutableList<>(mappedData);
    }

    public ImmutableList<T> filter(Predicate<T> filter) {
        List<T> filteredData = new ArrayList<>();
        for (T item : data) {
            if (filter.test(item)) {
                filteredData.add(item);
            }
        }
        return new ImmutableList<>(filteredData);
    }
    
    public ImmutableList<T> section(int from, int until) {
        return new ImmutableList<>(data.subList(from, until));
    }

    public ImmutableList<T> concat(ImmutableList<? extends T> other) {
        List<T> newData = new ArrayList<>(data.size() + other.size());
        newData.addAll(data);
        newData.addAll(other.data);
        return new ImmutableList<>(newData);
    }

    public ImmutableList<T> concat(Iterable<? extends T> other) {
        List<T> newData = new ArrayList<>(data);
        for (T item : other) {
            newData.add(item);
        }
        return new ImmutableList<>(newData);
    }

    public ImmutableList<T> append(T item) {
        List<T> newData = new ArrayList<>(data.size() + 1);
        newData.addAll(data);
        newData.add(item);
        return new ImmutableList<>(newData);
    }

    public void forEachIndex(BiConsumer<Integer, T> action) {
        int length = data.size();
        for (int i = 0; i < length; i++) {
            T item = data.get(i);
            action.accept(i, item);
        }
    }

    public ReversibleIterable<T> reverseOrder() {
        ListIterator<T> listIterator = data.listIterator(data.size());
        return ReversibleIterable.reversedOfReference(() -> new Iterator<T>() {
    
            @Override
            public boolean hasNext() {
                return listIterator.hasPrevious();
            }

            @Override
            public T next() { // NOSONAR
                return listIterator.previous();
            }
            
        } , this);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public ImmutableList<T> sort() {
        List<T> sortedData = new ArrayList<>(data);
        Collections.sort((List) sortedData);
        return new ImmutableList<>(sortedData);
    }

    public ImmutableList<T> sort(Comparator<T> comparator) {
        List<T> sortedData = new ArrayList<>(data);
        Collections.sort(sortedData, comparator);
        return new ImmutableList<>(sortedData);
    }

    public ImmutableList<T> resize(int newSize, T fillItem) {
        return resize(newSize, i -> fillItem);
    }

    public ImmutableList<T> resize(int newSize, IntFunction<T> fillSupplier) {
        int currentSize = data.size();
        if (newSize == currentSize) {
            return this;
        } else if (newSize < currentSize) {
            return section(0, newSize);
        }
        
        List<T> newData = new ArrayList<>(newSize);
        newData.addAll(data);
        for (int i = currentSize; i < newSize; i++) {
            newData.add(fillSupplier.apply(i));
        }
        return new ImmutableList<>(newData);
    }

    @SuppressWarnings("unchecked")
    public int binarySearch(T value) {
        return binarySearch(value, (Comparator<T>) Comparator.naturalOrder());
    }

    public int binarySearch(T value, Comparator<? super T> comparator) {
        return Collections.binarySearch(data, value, comparator);
    }
    
    @Override
    public Iterator<T> iterator() {
        return Collections.unmodifiableList(data).iterator();
    }

    public Stream<T> stream() {
        return data.stream();
    }

    public Object[] toArray() {
        return data.toArray();
    }

    public List<T> asList() {
        return Collections.unmodifiableList(data);
    }

    public ArrayList<T> toArrayList() { // NOSONAR
        return new ArrayList<>(data);
    }

    public <U> ImmutableMap<T, U> assign(Function<T, U> valueMapper) {
        return ImmutableMap.assignFrom(data, data.size(), valueMapper);
    }

    public <U> ImmutableMap<T, U> assign(BiFunction<T, Integer, U> valueMapper) {
        return ImmutableMap.assignFrom(data, data.size(), valueMapper);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null) {
            return false;
        } else if (!(other instanceof ImmutableList)) {
            return false;
        }
        
        ImmutableList<?> otherList = (ImmutableList<?>) other;
        return data.equals(otherList.data);
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
    
    
    public static <T> Collector<T, ?, ImmutableList<T>> createCollector() {
        return Collector.of(
                (Supplier<List<T>>) ArrayList::new,
                (list, value) -> list.add(value),
                (leftList, rightList) -> {
                    leftList.addAll(rightList);
                    return leftList;
                },
                ImmutableList::fromCollection);
    }
    
}
