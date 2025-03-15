package hu.webarticum.miniconnect.lang.jackson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import hu.webarticum.miniconnect.lang.ImmutableMap;

public class ImmutableMapSerializer extends StdSerializer<ImmutableMap<?, ?>> {
    
    private static final long serialVersionUID = 1L;
    

    @SuppressWarnings("unchecked")
    public ImmutableMapSerializer() {
        super((Class<ImmutableMap<?, ?>>) (Class<?>) ImmutableMap.class);
    }

    @Override
    public void serialize(
            ImmutableMap<?, ?> value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeStartObject(value, value.size());
        for (Map.Entry<?, ?> item : value.entrySet()) {
            generator.writeObjectField(item.getKey().toString(), item.getValue()); // FIXME
        }
        generator.writeEndObject();
    }
    
    @Override
    public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
        return createSchemaNode("object");
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        JavaType keyType = typeHint.containedType(0);
        JavaType itemType = typeHint.containedType(1);
        visitor.expectArrayFormat(itemType);
        JsonSerializer<Object> keySerializer = visitor.getProvider().findKeySerializer(keyType, null);
        JsonSerializer<Object> itemSerializer = visitor.getProvider().findValueSerializer(itemType);
        TypeSerializer itemTypeSerializer = visitor.getProvider().findTypeSerializer(itemType);
        MapSerializer mapSerializer = MapSerializer.construct(
                Collections.emptySet(), null, true, itemTypeSerializer, keySerializer, itemSerializer, null);
        mapSerializer.acceptJsonFormatVisitor(visitor, itemType);
    }
    
}