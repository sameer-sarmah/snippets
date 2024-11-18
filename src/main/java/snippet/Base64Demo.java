package snippet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

public class Base64Demo {

	public static void main(String[] args) {
		InputStream inputStream =Base64Demo.class.getClassLoader().getResourceAsStream("privateKey.txt");
		try {
			//String encodedStr = IOUtils.toString(inputStream,Charset.defaultCharset());
			String encodedStr = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDl";
			byte[] bytes = encodedStr.getBytes();
			Properties ps = new Properties();
			String bytesValue = String.valueOf(bytes);
			ps.put(UUID.randomUUID().toString(), bytesValue);
			String string  = IOUtils.toString(bytes, "UTF-8");
			ps.put(UUID.randomUUID().toString(), string);
			ps.store(System.out, null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getEncodedString() {
		return "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDl";
	}

}
