package serde;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.http.HttpMethod;

public class HttpMethodSerializer extends JsonSerializer<HttpMethod> {

    @Override
    public void serialize(HttpMethod httpMethod, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(httpMethod.name());
    }
}