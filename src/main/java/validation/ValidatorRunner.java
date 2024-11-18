package validation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import spel.SpelRunner;

public class ValidatorRunner {

	public static void main(String[] args) throws IOException {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		User user = new User();
		user.setAge(50);
		
		System.out.println("Valid="+isValid(validator,user));
		
		InputStream in = SpelRunner.class.getClassLoader().getResourceAsStream("entity/user.json");
		String json = IOUtils.toString(in, Charset.defaultCharset());
		ObjectMapper mapper = new ObjectMapper(); 
		user = mapper.readValue(json, User.class);
		
		System.out.println("Valid="+isValid(validator,user));
	}
	
	private static boolean isValid(Validator validator,User user) {
		Set<ConstraintViolation<User>> violations = validator.validate(user);
		for (ConstraintViolation<User> violation : violations) {
		    System.err.println(violation.getMessage()); 
		}
		return violations.isEmpty();
	}

}
