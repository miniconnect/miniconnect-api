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
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;
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
        JavaType itemType = typeHint.containedType(0);
        visitor.expectArrayFormat(itemType);
        JsonSerializer<Object> itemSerializer = visitor.getProvider().findValueSerializer(itemType);
        TypeSerializer itemTypeSerializer = visitor.getProvider().findTypeSerializer(itemType);
        ObjectArraySerializer arraySerializer = new ObjectArraySerializer(
                itemType, true, itemTypeSerializer, itemSerializer);
        arraySerializer.acceptJsonFormatVisitor(visitor, itemType);
    }
    
}