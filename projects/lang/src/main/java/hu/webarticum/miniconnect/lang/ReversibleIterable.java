package hu.webarticum.miniconnect.lang;

import java.util.Collections;
import java.util.Iterator;

public interface ReversibleIterable<T> extends Iterable<T> {
    
    public ReversibleIterable<T> reverseOrder();


    public static <T> ReversibleIterable<T> empty() {
        return of(Collections::emptyIterator, Collections::emptyIterator);
    }

    public static <T> ReversibleIterable<T> of(Iterable<T> base, Iterable<T> reversed) {
        return new ReversibleIterableImpl<>(base, reversed);
    }
    
    public static <T> ReversibleIterable<T> reversedOfReference(Iterable<T> reversed, ReversibleIterable<T> original) {
        return new ReferenceReversedIterable<>(reversed, original);
    }
    

    public static class ReversibleIterableImpl<T> implements ReversibleIterable<T> {
        
        private final Iterable<T> base;
        
        private final Iterable<T> reversed;
        
        
        public ReversibleIterableImpl(Iterable<T> base, Iterable<T> reversed) {
            this.base = base;
            this.reversed = reversed;
        }


        @Override
        public Iterator<T> iterator() {
            return base.iterator();
        }

        @Override
        public ReversibleIterable<T> reverseOrder() {
            return new ReferenceReversedIterable<>(reversed, this);
        }
        
    }
    
    
    public static class ReferenceReversedIterable<T> implements ReversibleIterable<T> {
        
        private final Iterable<T> reversed;
        
        private final ReversibleIterable<T> original;
        
        
        private ReferenceReversedIterable(Iterable<T> reversed, ReversibleIterable<T> original) {
            this.reversed = reversed;
            this.original = original;
        }
        

        @Override
        public Iterator<T> iterator() {
            return reversed.iterator();
        }

        @Override
        public ReversibleIterable<T> reverseOrder() {
            return original;
        }
        
    }
    
}
