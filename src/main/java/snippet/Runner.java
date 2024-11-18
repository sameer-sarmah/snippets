package snippet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.StringUtils;

public class Runner {
 
	public static void main(String[] args) throws IOException {

		long currentSecTime = Instant.now().getEpochSecond();
		long currentMilliSecTime = Instant.now().toEpochMilli();
		System.out.println(currentSecTime);
		System.out.println(currentMilliSecTime);
		while(true) {
			InputStream in = Runner.class.getClassLoader().getSystemResourceAsStream("filter-complex.json");
			String json = IOUtils.toString(in,Charset.defaultCharset());
			byte[] previous = json.getBytes();
			byte[] current = json.getBytes();
			System.out.println("Are arrays equal="+ String.valueOf(previous == current));
//			while(true) {
//				
//			}
			//byte[] utf8EncodedByte = json.getBytes("UTF-8");
		}

//		byte[] defaultEncodedByte = json.getBytes();
//		byte[] utf8EncodedByte = json.getBytes("UTF-8");
		
		

	}
	
	private void isAssignable() {
		   Class<? extends Number> numberClass = Number.class;	
		   Class<Integer> integerClass = Integer.class;
		   System.out.println(integerClass.isAssignableFrom(numberClass));
		   System.out.println(numberClass.isAssignableFrom(integerClass));
		   
	}
	
	private void quoteStrings() {
		   List<String> superSet = List.of("A","B","C","D") ;
		   String lstStr = superSet
				   .stream()
				   .map(StringUtils::quote)
				   .collect(Collectors.joining(",", "{", "}"));
		   System.out.println(lstStr);
		   
		   boolean flag = Boolean.parseBoolean("sadf");
		   System.out.println(flag);
	}
	

}
