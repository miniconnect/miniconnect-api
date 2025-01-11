package hu.webarticum.miniconnect.lang;

import java.io.IOException;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import scala.math.BigInt;

public class LargeIntegerMemoryMain {

    public static void main(String[] args) throws IOException {
        measureMemory("LargeInteger(1000)", LargeInteger.of(1000L));
        measureMemory("BigInt(1000)", BigInt.apply(1000L));
        measureMemory("LargeInteger(9162734681273492811274)", LargeInteger.of("9162734681273492811274"));
        measureMemory("BigInt(9162734681273492811274)", BigInt.apply("9162734681273492811274"));
    }

    private static void measureMemory(String name, Object object) throws IOException {
        GraphLayout layout = GraphLayout.parseInstance(object);
        long size = layout.totalSize();
        System.out.println(name + ": " + size + "     :: " + object.getClass());
        System.out.println();
        System.out.println(ClassLayout.parseInstance(object).toPrintable());
        System.out.println("=========================================");
    }
    
}
