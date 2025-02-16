package hu.webarticum.miniconnect.lang;

import java.math.BigInteger;

import clojure.java.api.Clojure;
import clojure.lang.BigInt;
import clojure.lang.IFn;

public class ClojureArithmetic {

    static {
        IFn require = Clojure.var("clojure.core", "require");
        require.invoke(Clojure.read("clojure.math.numeric-tower"));
    }
    private static final IFn ADD = Clojure.var("clojure.core", "+");
    private static final IFn SUBTRACT = Clojure.var("clojure.core", "-");
    private static final IFn INCREMENT = Clojure.var("clojure.core", "inc");
    private static final IFn MULTIPLY = Clojure.var("clojure.core", "*");
    private static final IFn DIVIDE = Clojure.var("clojure.core", "quot");
    private static final IFn REMAINDER = Clojure.var("clojure.core", "rem");
    private static final IFn POW = Clojure.var("clojure.math.numeric-tower", "expt");
    private static final IFn SQRT = Clojure.var("clojure.math.numeric-tower", "sqrt");
    private static final IFn ABS = Clojure.var("clojure.core", "abs");
    private static final IFn MIN = Clojure.var("clojure.core", "min");
    private static final IFn MAX = Clojure.var("clojure.core", "max");
    private static final IFn BIGINT = Clojure.var("clojure.core", "bigint");

    public static Object add(Object a, Object b) {
        return ADD.invoke(a, b);
    }
    
    public static Object subtract(Object a, Object b) {
        return SUBTRACT.invoke(a, b);
    }

    public static Object negate(Object a) {
        return SUBTRACT.invoke(a);
    }

    public static Object increment(Object a) {
        return INCREMENT.invoke(a);
    }
    
    public static Object multiply(Object a, Object b) {
        return MULTIPLY.invoke(a, b);
    }

    public static Object divide(Object a, Object b) {
        return DIVIDE.invoke(a, b);
    }

    public static Object remainder(Object a, Object b) {
        return REMAINDER.invoke(a, b);
    }

    public static Object pow(Object a, Object b) {
        return POW.invoke(a, b);
    }

    public static Object sqrt(Object a) {
        return BIGINT.invoke(SQRT.invoke(a));
    }

    public static Object abs(Object a) {
        return ABS.invoke(a);
    }

    public static Object min(Object a, Object b) {
        return MIN.invoke(a, b);
    }

    public static Object max(Object a, Object b) {
        return MAX.invoke(a, b);
    }

    public static Object or(Object a, Object b) {
        if (isLongish(a) && isLongish(b)) {
            long longA = toLong(a);
            long longB = toLong(b);
            return BigInt.fromLong(longA | longB);
        } else {
            BigInteger bigIntegerA = toBigInteger(a);
            BigInteger bigIntegerB = toBigInteger(b);
            return BigInt.fromBigInteger(bigIntegerA.or(bigIntegerB));
        }
    }

    public static Object and(Object a, Object b) {
        if (isLongish(a) && isLongish(b)) {
            long longA = toLong(a);
            long longB = toLong(b);
            return BigInt.fromLong(longA & longB);
        } else {
            BigInteger bigIntegerA = toBigInteger(a);
            BigInteger bigIntegerB = toBigInteger(b);
            return BigInt.fromBigInteger(bigIntegerA.and(bigIntegerB));
        }
    }

    private static boolean isLongish(Object n) {
        return !(n instanceof BigInteger) && !(n instanceof BigInt);
    }

    private static long toLong(Object n) {
        if (n instanceof Number) {
            return ((Number) n).longValue();
        } else {
            return 1L;
        }
    }

    private static BigInteger toBigInteger(Object n) {
        if (n instanceof BigInteger) {
            return (BigInteger) n;
        } else if (n instanceof clojure.lang.BigInt) {
            return ((clojure.lang.BigInt) n).toBigInteger();
        } else if (n instanceof Number) {
            return BigInteger.valueOf(((Number) n).longValue());
        } else {
            return BigInteger.ONE;
        }
    }

}
