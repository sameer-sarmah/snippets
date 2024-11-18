package serde;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class SerDe {

	public static void main(String[] args) {
		serDeUsingCustomSerializer();
		serDeUsingCustomDeserializer();
	}
	
	private static void serDeUsingCustomSerializer() {
		InputStream in = SerDe.class.getClassLoader().getResourceAsStream("http-config.json");
		try {
			ObjectMapper mapper = getConfiguredObjectMapper();
			//ObjectMapper mapper = getSimpleObjectMapper();
			HttpConfigV1 configV1  = mapper.readValue(in, HttpConfigV1.class);
			byte[] serialized = mapper.writeValueAsBytes(configV1);
			String json = new String(serialized);
			System.out.println(json);
			configV1 = mapper.readValue(serialized, HttpConfigV1.class);
			Assert.notNull(configV1,"Deserialized config should not be null");
			System.out.println(configV1);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	private static void serDeUsingCustomDeserializer() {
		InputStream in = SerDe.class.getClassLoader().getResourceAsStream("http-config.json");
		try {
			ObjectMapper mapper = getConfiguredObjectMapper();
			//ObjectMapper mapper = getSimpleObjectMapper();
			HttpConfigV2 configV2  = mapper.readValue(in, HttpConfigV2.class);
			byte[] serialized = mapper.writeValueAsBytes(configV2);
			String json = new String(serialized);
			System.out.println(json);
			configV2 = mapper.readValue(serialized, HttpConfigV2.class);
			Assert.notNull(configV2,"Deserialized config should not be null");
			System.out.println(configV2);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	
	//Same config as ObjectMapper used by DEB for serialization and deserialization
	private static ObjectMapper getConfiguredObjectMapper() {
		return JsonMapper.builder()
			    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			    .enable(MapperFeature.PROPAGATE_TRANSIENT_MARKER)
			    .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
			    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS).build()
			    .setSerializationInclusion(Include.NON_NULL)
			    .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
	}
	
	private static ObjectMapper getSimpleObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		return objectMapper;
	} 

}

