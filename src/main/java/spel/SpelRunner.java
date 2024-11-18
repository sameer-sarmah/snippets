package spel;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpelRunner {

	public static void main(String[] args) throws IOException {
		Map<String, Object> config = getMapToEvaluate();
		evaluateWhenValuesAreMap(config);
		

	}
	
	private static void evaluateWhenValuesAreMap(Map<String, Object> config) {

		StandardEvaluationContext simpleContext = new StandardEvaluationContext(config);
		simpleContext.setVariable("emp", config);
		simpleContext.addPropertyAccessor(new MapAccessor());
		ExpressionParser parser = new SpelExpressionParser();

//		Expression expression =parser.parseExpression("( #this['gender'] == 'male' && #this['age'] > 60 ) || #this[department][name] == 'Engineering' ");
//		Expression expression =parser.parseExpression("( #this['gender'] eq 'male' and #this['age'] gt 60 ) or #this[department][name] eq 'Engineering' ");
//		Expression expression =parser.parseExpression("( #this[gender] eq 'male' and #this[age] gt 60 )");

		registerFunctions(simpleContext);
		//functionsForCollections(simpleContext, parser);
		dateOperators(simpleContext, parser);
		

	}
	
	private static void registerFunctions(StandardEvaluationContext simpleContext) {
		try {
			simpleContext.setVariable("endsWith",
			        StringUtils.class.getDeclaredMethod("endsWith", CharSequence.class, CharSequence.class));
			
			simpleContext.setVariable("startswith",
			        StringUtils.class.getDeclaredMethod("startsWith", CharSequence.class, CharSequence.class));
			
			simpleContext.setVariable("trim",
			        StringUtils.class.getDeclaredMethod("trim", String.class));
			
			simpleContext.setVariable("toupper",
			        StringUtils.class.getDeclaredMethod("upperCase", String.class));
			
			simpleContext.setVariable("tolower",
			        StringUtils.class.getDeclaredMethod("lowerCase", String.class));
			
			simpleContext.setVariable("contains",
			        StringUtils.class.getDeclaredMethod("contains", CharSequence.class, CharSequence.class));
			
			simpleContext.setVariable("containsAny",
			        CollectionUtils.class.getDeclaredMethod("containsAny", Collection.class, Collection.class));
			
			simpleContext.setVariable("isAfter",
					DateCompareUtil.class.getDeclaredMethod("isAfter", String.class, String.class));
			
			simpleContext.setVariable("isBefore",
					DateCompareUtil.class.getDeclaredMethod("isBefore", String.class, String.class));
			
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	private static void dateOperators(StandardEvaluationContext simpleContext,ExpressionParser parser) {
		String expressionStr = "(  #isAfter(#emp['doj'],'2015-01-30T00:00:00+00:00') and #isBefore(#emp['doj'],'2023-01-30T00:00:00+00:00') )";
		Expression expression =parser.parseExpression(expressionStr);	
		boolean extractedObject = expression.getValue(simpleContext,Boolean.class);
		System.out.println(expressionStr +" => "+extractedObject);
		
	}
	
	private static void binaryOperators(StandardEvaluationContext simpleContext,ExpressionParser parser) {
		String expressionStr = "( #emp['gender'] eq 'male' and #emp['age'] gt 60 ) or #emp[department][name] eq 'Engineering' ";
		Expression expression =parser.parseExpression(expressionStr);	
		boolean extractedObject = expression.getValue(simpleContext,Boolean.class);
		System.out.println(expressionStr +" => "+extractedObject);
		
		expressionStr = "#emp[department][name] ne 'Engineering' ";
		expression =parser.parseExpression(expressionStr);
		extractedObject = expression.getValue(simpleContext,Boolean.class);
		System.out.println(expressionStr +" => "+extractedObject);
	}
	
	private static void stringFunctions(StandardEvaluationContext simpleContext,ExpressionParser parser) {
		String expressionStr = "#endsWith(#emp['gender'],'ale')";
		boolean endsWithAle = parser.parseExpression(expressionStr).getValue(simpleContext, Boolean.class);
		System.out.println(expressionStr +" => "+endsWithAle);
		
		boolean startsWithMa = parser.parseExpression(
		        "#startswith(#emp['gender'],'ma')").getValue(simpleContext, Boolean.class);
		System.out.println(startsWithMa);
		
		boolean containsMa = parser.parseExpression(
		        "#contains(#emp['gender'],'ma')").getValue(simpleContext, Boolean.class);
		System.out.println("#contains(#emp['gender'],'ma'),result="+String.valueOf(containsMa));
		
		String upperCase = parser.parseExpression(
		        "#toupper(#emp['gender'])").getValue(simpleContext, String.class);
		System.out.println(upperCase);
		
		String lowerCase = parser.parseExpression(
		        "#tolower(#emp['gender'])").getValue(simpleContext, String.class);
		System.out.println(lowerCase);
		
		String trim = parser.parseExpression(
		        "#trim(#emp[department][name])").getValue(simpleContext, String.class);
		System.out.println(trim);
	}
	
	private static void functionsForCollections(StandardEvaluationContext simpleContext,ExpressionParser parser) {
		List<String> allowedLobs = List.of("SuccessFactors","Hana");
		String allowedLobsStr = allowedLobs
								   .stream()
								   .map(org.springframework.util.StringUtils::quote)
								   .collect(Collectors.joining(",", "{", "}"));
		String spelExpression = String.format("#containsAny(#emp['lob'],%s)", allowedLobsStr);
		Boolean containsAny = parser.parseExpression(spelExpression).getValue(simpleContext, Boolean.class);
		System.out.println(spelExpression +" => " +containsAny);
		
		simpleContext.setVariable("allowedLobs", allowedLobs);
		containsAny = parser.parseExpression(
		        "#containsAny(#emp['lob'],#allowedLobs)").getValue(simpleContext, Boolean.class);
		System.out.println("#containsAny(#emp['lob'],#allowedLobs) => " +containsAny);
		
		
		List<Integer> skills = List.of(333);
		String skillsStr = skills
								   .stream()
								   .map(String::valueOf)
								   .collect(Collectors.joining(",", "{", "}"));
		spelExpression = String.format("#containsAny(#emp['skills'],%s)", skillsStr);
		containsAny = parser.parseExpression(spelExpression).getValue(simpleContext, Boolean.class);
		System.out.println(spelExpression +" => " +containsAny);
	}
	
	// eq , ne , gt ,ge , lt,le,in,like,startswith,not startswith,endswith,not endswith,toupper,tolower,trim
	private void evaluateWhenValuesAreTypedObjects(Map<String, Object> config) {
		var department = new Department("Engineering");
		config.remove("department");
		config.put("department", department);
		StandardEvaluationContext simpleContext = new StandardEvaluationContext(config);
		simpleContext.setVariable("emp", config);
		simpleContext.addPropertyAccessor(new MapAccessor());
		ExpressionParser parser = new SpelExpressionParser();

//		Expression expression =parser.parseExpression("( #this['gender'] == 'male' && #this['age'] > 60 ) || #this[department][name] == 'Engineering' ");
//		Expression expression =parser.parseExpression("( #this['gender'] eq 'male' and #this['age'] gt 60 ) or #this[department][name] eq 'Engineering' ");
//		Expression expression =parser.parseExpression("( #this[gender] eq 'male' and #this[age] gt 60 )");
		Expression expression =parser.parseExpression("( #emp['gender'] eq 'male' and #emp['age'] gt 60 ) or #emp[department][name] eq 'Engineering' ");
		
		boolean extractedObject = expression.getValue(simpleContext,Boolean.class);
		System.out.println(extractedObject);
		
		expression =parser.parseExpression("#emp[department][name] ne 'Engineering' ");
		
		extractedObject = expression.getValue(simpleContext,Boolean.class);
		System.out.println(extractedObject);
//		
//		expression =parser.parseExpression(" #emp['department']['name'] ");
//		Object departmentName = expression.getValue(simpleContext);
//		System.out.println(departmentName);
//		
//		expression =parser.parseExpression(" #this['gender'] ");
//		String gender = expression.getValue(simpleContext,String.class);
//		System.out.println(gender);
//		
//		expression =parser.parseExpression(" #root['age'] ");
//		int age = expression.getValue(simpleContext,Integer.class);
//		System.out.println(age);
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
class Department{
	
	private String name;
	
	public Department(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	
}
