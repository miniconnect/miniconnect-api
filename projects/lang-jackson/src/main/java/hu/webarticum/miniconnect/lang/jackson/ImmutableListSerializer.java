package hu.webarticum.miniconnect.lang.jackson;

import java.io.IOException;
import java.lang.reflect.Type;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import hu.webarticum.miniconnect.lang.ImmutableList;

public class ImmutableListSerializer extends StdSerializer<ImmutableList<?>> {

    private static final long serialVersionUID = 1L;


    @SuppressWarnings("unchecked")
    public ImmutableListSerializer() {
        super((Class<ImmutableList<?>>) (Class<?>) ImmutableList.class);
    }

    @Override
    public void serialize(ImmutableList<?> value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeStartArray(value, value.size());
        for (Object item : value) {
            generator.writeObject(item);
        }
        generator.writeEndArray();
    }

    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        return createSchemaNode("array");
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        SerializerProvider provider = visitor.getProvider();
        JavaType itemType = extractItemType(provider, typeHint);
        JavaType arrayType = provider.getTypeFactory().constructArrayType(itemType);
        JsonSerializer<Object> itemSerializer = provider.findValueSerializer(itemType);
        visitor.expectArrayFormat(arrayType).itemsFormat(itemSerializer, itemType);
    }

    private JavaType extractItemType(SerializerProvider provider, JavaType typeHint) {
        if (typeHint == null || typeHint.containedTypeCount() == 0) {
            return provider.constructType(Object.class);
        }

        return typeHint.containedType(0);
    }

}
