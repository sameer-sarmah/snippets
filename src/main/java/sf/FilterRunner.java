package sf;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import spel.DateCompareUtil;
import spel.SpelRunner;

public class FilterRunner {

	public static void main(String[] args) throws IOException {

		var filterExp = generateSpel();		
		Map<String, Object> config = getMapToEvaluate();
		evaluateSpel(config,filterExp);

	}
	

	
	private static String generateSpel() throws IOException {
		InputStream in = SpelRunner.class.getClassLoader().getResourceAsStream("filter-complex.json");
		String json = IOUtils.toString(in, Charset.defaultCharset());
		ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		FilterNode filterNode =  mapper.readValue(json, FilterNode.class);
		System.out.println(mapper.writeValueAsString(filterNode));
		var filterExp = FilterExpressionBuilder.buildFilterExpression(filterNode);
		System.out.println(filterExp);
		return filterExp;
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
	
	private static void evaluateSpel(Map<String, Object> config,String filterExp) {
		StandardEvaluationContext simpleContext = new StandardEvaluationContext(config);
		simpleContext.setVariable("emp", config);
		try {
			simpleContext.setVariable("containsAny",
			        CollectionUtils.class.getDeclaredMethod("containsAny", Collection.class, Collection.class));
			simpleContext.setVariable("isAfter",
					DateCompareUtil.class.getDeclaredMethod("isAfter", String.class, String.class));		
			simpleContext.setVariable("isBefore",
					DateCompareUtil.class.getDeclaredMethod("isBefore", String.class, String.class));
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}

		simpleContext.addPropertyAccessor(new MapAccessor());
		ExpressionParser parser = new SpelExpressionParser();

		Expression expression =parser.parseExpression(filterExp);	
		boolean extractedObject = expression.getValue(simpleContext,Boolean.class);
		System.out.println(extractedObject);
	}
}
