package serde;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class HttpMethodDeserializer extends JsonDeserializer<HttpMethod> {

	@Override
	public HttpMethod deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = p.readValueAsTree();
        String httpMethod = node.asText();
        if(StringUtils.isBlank(httpMethod)) {
        	JsonNode nameNode = node.findValue("name");
        	httpMethod = nameNode.asText();
        } 
        return HttpMethod.valueOf(httpMethod);
	}


}