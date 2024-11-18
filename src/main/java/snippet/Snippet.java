package snippet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import spel.SpelRunner;

public class Snippet {
	public static void main(String[] args) throws IOException {
		List<Entity> entities = Arrays.asList(new Entity("name","sameer"),new Entity("name","mayuri"));
		entities.stream()
				.map(entity -> entity.getName().equals("name") ? Optional.<Entity>of(entity) : Optional.<Entity>empty())
				.filter(Optional::isPresent)
				.map(Optional::get)
				.forEach((Entity entity) -> System.out.println(entity.getName()));
		
		
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("HmacSHA256");
            keyGenerator.init(256);
            SecretKey secretKey = keyGenerator.generateKey();
            String base64Key = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println(base64Key);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

	}
}
