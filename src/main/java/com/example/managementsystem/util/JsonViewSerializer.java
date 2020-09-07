package com.example.managementsystem.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class JsonViewSerializer {

    public static class RelationalBase extends JsonSerializer<Object> {
        @Override
        public void serialize(Object value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writerWithView(JsonViews.Base.class).writeValue(jsonGenerator, value);
        }
    }
}
