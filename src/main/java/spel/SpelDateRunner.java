package spel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpelDateRunner {
	
	
	public static void main(String[] args) throws IOException {
		Map<String, Object> config = getMapToEvaluate();
		StandardEvaluationContext simpleContext = new StandardEvaluationContext(config);
		simpleContext.addPropertyAccessor(new MapAccessor());
		ExpressionParser parser = new SpelExpressionParser();

		ZonedDateTime thirtyJan = ZonedDateTime.parse("2023-01-30T00:00:00+00:00",ISO_OFFSET_DATE_TIME);
		ZonedDateTime fourthFeb = ZonedDateTime.parse("2023-02-04T00:00:00+00:00",ISO_OFFSET_DATE_TIME);
		System.out.println(thirtyJan.isBefore(fourthFeb));
		System.out.println(fourthFeb.isAfter(thirtyJan));
		
		
		
		
	}
	
	private static Map<String, Object> getMapToEvaluate() throws IOException{
		InputStream in = SpelRunner.class.getClassLoader().getResourceAsStream("emp-predicate.json");
		String json = IOUtils.toString(in, Charset.defaultCharset());
		 ObjectMapper mapper = new ObjectMapper();  
		 Map<String, Object> config = mapper.readValue(  
				 json, new TypeReference<Map<String, Object>>() {  
         });
		return config; 
	}
}
