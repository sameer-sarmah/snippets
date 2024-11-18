package serde;



import org.springframework.http.HttpMethod;
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class HttpConfigV1 {
	@JsonSerialize(using = HttpMethodSerializer.class)
	private HttpMethod httpMethod;
	
	private String path;
	
	private String queryString;
	
	private LinkedMultiValueMap<String,String> headers;


	public HttpMethod getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public LinkedMultiValueMap<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(LinkedMultiValueMap<String, String> headers) {
		this.headers = headers;
	}

	@Override
	public String toString() {
		return "HttpConfigV1 [httpMethod=" + httpMethod + ", path=" + path + ", queryString=" + queryString + "]";
	}
	
	
}
