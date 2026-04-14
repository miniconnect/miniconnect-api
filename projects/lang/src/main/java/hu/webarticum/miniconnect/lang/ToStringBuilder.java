package hu.webarticum.miniconnect.lang;

/**
 * Helper for building result of the <code>toString()</code> method based on fields.
 */
public class ToStringBuilder {

    private final StringBuilder stringBuilder = new StringBuilder();

    private boolean empty = true;


    public ToStringBuilder(Object object) {
        stringBuilder.append(object.getClass().getSimpleName());
        stringBuilder.append(" {");
    }


    public ToStringBuilder add(String key, Object value) {
        if (empty) {
            stringBuilder.append(" ");
            empty = false;
        } else {
            stringBuilder.append(", ");
        }
        stringBuilder.append(key);
        stringBuilder.append(": ");
        stringBuilder.append(value);
        return this;
    }

    public String build() {
        return stringBuilder.toString() + " }";
    }

}
