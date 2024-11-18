package snippet;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

public class TLS {

	public static void main(String[] args) {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			System.out.println(sslContext);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
