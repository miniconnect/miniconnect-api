package hu.webarticum.miniconnect.lang.assertj;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.assertj.core.api.AssertFactory;
import org.assertj.core.api.ObjectAssert;

import hu.webarticum.miniconnect.lang.ByteString;
import hu.webarticum.miniconnect.lang.CheckableCloseable;
import hu.webarticum.miniconnect.lang.ImmutableList;
import hu.webarticum.miniconnect.lang.ImmutableMap;
import hu.webarticum.miniconnect.lang.LargeInteger;

public class Assertions {
    
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAP;
    static {
        Map<Class<?>, Class<?>> primitiveTypeMap = new HashMap<>();
        primitiveTypeMap.put(boolean.class, Boolean.class);
        primitiveTypeMap.put(byte.class, Byte.class);
        primitiveTypeMap.put(short.class, Short.class);
        primitiveTypeMap.put(char.class, Character.class);
        primitiveTypeMap.put(int.class, Integer.class);
        primitiveTypeMap.put(long.class, Long.class);
        primitiveTypeMap.put(float.class, Float.class);
        primitiveTypeMap.put(double.class, Double.class);
        PRIMITIVE_TYPE_MAP = Collections.unmodifiableMap(primitiveTypeMap);
    }
    
    
    private Assertions() {
        // static class
    }
    

    public static ByteStringAssert assertThat(ByteString actual) {
        return new ByteStringAssert(actual);
    }

    public static CheckableCloseableAssert assertThat(CheckableCloseable actual) {
        return new CheckableCloseableAssert(actual);
    }

    public static <T> ImmutableListAssert<T> assertThat(ImmutableList<? extends T> actual) {
        return new ImmutableListAssert<>(actual);
    }

    public static <T> ImmutableListAssert<T> assertThat(
            ImmutableList<? extends T> actual, AssertFactory<T, ObjectAssert<T>> assertFactory) {
        return new ImmutableListAssert<>(actual, assertFactory);
    }

    public static <T> ImmutableListAssert<T> assertThat(
            ImmutableList<? extends T> actual, Class<?> assertClazz) {
        return new ImmutableListAssert<>(actual, value -> buildAssert(value, assertClazz));
    }

    public static <K, V> ImmutableMapAssert<K, V> assertThat(ImmutableMap<K, V> actual) {
        return new ImmutableMapAssert<>(actual);
    }

    public static LargeIntegerAssert assertThat(LargeInteger actual) {
        return new LargeIntegerAssert(actual);
    }

    public static <T> ObjectAssert<T> buildAssert(T value, Class<?> assertFactoryClazz) {
        for (Constructor<?> constructor : assertFactoryClazz.getDeclaredConstructors()) {
            Optional<ObjectAssert<T>> newAssertOptional = buildAssert(value, constructor);
            if (newAssertOptional.isPresent()) {
                return newAssertOptional.get();
            }
        }
        throw new IllegalArgumentException(
                "Failed to find a constructor matching " + value + " class to build the expected Assert class");
    }

    public static <T> Optional<ObjectAssert<T>> buildAssert(T value, Constructor<?> constructor) {
        try {
            return buildAssertThrows(value, constructor);
        } catch (
                InstantiationException |
                IllegalAccessException |
                IllegalArgumentException |
                InvocationTargetException e) {
            throw new IllegalArgumentException(
                    "Failed to build an assert object with " + value + ": " + e.getMessage(), e);
        }
    }
    
    private static <T> Optional<ObjectAssert<T>> buildAssertThrows(T value, Constructor<?> constructor)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        if (constructor.getParameterTypes().length != 1) {
            return Optional.empty();
        }
        
        Class<?> parameterType = constructor.getParameterTypes()[0];
        Class<?> parameterWrapperType = parameterType;
        if (parameterType.isPrimitive()) {
            if (value == null) {
                return Optional.empty();
            } else {
                parameterWrapperType = PRIMITIVE_TYPE_MAP.get(parameterType);
            }
        }
        
        if (!parameterWrapperType.isAssignableFrom(value.getClass())) {
            return Optional.empty();
        }
        
        @SuppressWarnings("unchecked")
        ObjectAssert<T> newAssert = (ObjectAssert<T>) constructor.newInstance(value);
        return Optional.of(newAssert);
    }

}
