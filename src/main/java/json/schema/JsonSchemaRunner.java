package json.schema;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import snippet.Runner;

public class JsonSchemaRunner {

	public static void main(String[] args) {
	    JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
	    JsonSchema jsonSchema = factory.getSchema(getInputStream("product-schema.json")); 
	    JsonNode jsonSchemaNode =jsonSchema.getSchemaNode();
	    System.out.println(jsonSchemaNode.findValuesAsText("title"));
	    boolean isValid = validate(jsonSchema, "product.json");
	    System.out.println("File=product.json,valid="+isValid);
	    isValid = validate(jsonSchema, "product-invalid.json");
	    System.out.println("File=product-invalid.json,valid="+isValid);
	}
	
	private static InputStream getInputStream(String file) {
		return JsonSchemaRunner.class.getClassLoader().getResourceAsStream(file);
	}
	
	private static boolean validate(JsonSchema jsonSchema,String fileToValidate) {
	    JsonNode jsonNode = null;
	    ObjectMapper mapper = new ObjectMapper(); 
		try {
			jsonNode = mapper.readTree(getInputStream(fileToValidate));
			Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
			System.out.println(errors);
			return errors.isEmpty();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}
