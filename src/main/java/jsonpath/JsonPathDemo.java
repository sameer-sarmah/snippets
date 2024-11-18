package jsonpath;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;

import snippet.Runner;

public class JsonPathDemo {
	public static void main(String[] args) throws IOException {
		String jsonPathToUpdate = "$['jobId']";
		Random random = new Random();
		String jobId = String.valueOf(random.nextInt(1000000));
		var documentContextForUpdate = new DocumentContextConfig(jsonPathToUpdate,jobId,null,DocumentAction.UPDATE_PROPERTY);
		jsonPathWriteScenario(documentContextForUpdate);
		
		String jsonPathToAdd = "$['jobType']";
		String jobTypeValue = "Scheduled";
		String propertyKey = "jobType";
		var documentContextForAdd = new DocumentContextConfig(jsonPathToAdd,jobTypeValue,propertyKey,DocumentAction.CREATE_PROPERTY);
		jsonPathWriteScenario(documentContextForAdd);
		
		String jsonPathToDelete = "$['externalCode']";
		var documentContextForDelete = new DocumentContextConfig(jsonPathToDelete,null,null,DocumentAction.DELETE_PROPERTY);
		
		jsonPathWriteScenario(documentContextForDelete);
	}

	private static void googleGuava() {
		List<String> names = Lists.newArrayList("sameer", "mayuri");
		names = Collections.unmodifiableList(names);
		System.out.println(names);
		System.out.println(names.getClass());
		Ints.asList(1, 2, 3, 4);

	}

	private static void invalidJson() {
		String jsonPath = "$['configurations']['responseHandler'][?(@['type'] == 'XM')]";
		List<Map<String, Object>> xm = JsonPath.parse("sameer").read(jsonPath);
	}

	private static void jsonPathPredicateWithInvalidPath() {
		String jsonPath = "$['configurations']['responseHandler'][?(@['type'] == 'XM')]";
		String jsonConfiguration = null;
		try {
			jsonConfiguration = IOUtils.resourceToString("QualtricsWithoutXmType.json", Charset.defaultCharset(),
					Runner.class.getClassLoader());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		try {
			List<Map<String, Object>> xm = JsonPath.parse(jsonConfiguration).read(jsonPath);
		} catch (JsonPathException e) {
			System.err.println(e.getMessage());
		}
	}

	private static void jsonPathPredicate() {
		String jsonPath = "$['configurations']['responseHandler'][?(@['type'] == 'XM')]";
		String jsonConfiguration = null;
		try {
			jsonConfiguration = IOUtils.resourceToString("Qualtrics.json", Charset.defaultCharset(),
					Runner.class.getClassLoader());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map<String, Object>> xm = JsonPath.parse(jsonConfiguration).read(jsonPath);
		String configuredJsonPath = xm.get(0).get("jsonPath").toString();
		System.out.println(configuredJsonPath);
		String responseJson = null;
		try {
			responseJson = IOUtils.resourceToString("response.json", Charset.defaultCharset(),
					Runner.class.getClassLoader());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String propertyToLog = JsonPath.parse(responseJson).read(configuredJsonPath);
		System.out.println(propertyToLog);
	}

	private static void jsonPathWriteScenario(DocumentContextConfig documentContextConfig) {
		String responseJson = null;
		try {
			responseJson = IOUtils.resourceToString("response.json", Charset.defaultCharset(),
					Runner.class.getClassLoader());
		} catch (IOException e) {
			e.printStackTrace();
		}
		DocumentContext jsonContext = JsonPath.using(Configuration.defaultConfiguration()).parse(responseJson);
	
		if(documentContextConfig.getDocumentAction().equals(DocumentAction.CREATE_PROPERTY)) {
			jsonContext.put(documentContextConfig.getJsonPath(),documentContextConfig.getPropertyKey() , documentContextConfig.getPropertyValue());
		} else if(documentContextConfig.getDocumentAction().equals(DocumentAction.DELETE_PROPERTY)) {
			jsonContext.delete(documentContextConfig.getJsonPath());
		} else if(documentContextConfig.getDocumentAction().equals(DocumentAction.UPDATE_PROPERTY)){
			jsonContext.set(documentContextConfig.getJsonPath(), documentContextConfig.getPropertyValue());
		}	
		System.out.println(String.format("JSON=%s,Action=%s",jsonContext.jsonString(),documentContextConfig.getDocumentAction()));
	}

}
enum DocumentAction{
	CREATE_PROPERTY,DELETE_PROPERTY,UPDATE_PROPERTY
}

class DocumentContextConfig{
	private String jsonPath;
	private	String propertyValue;
	private	String propertyKey;
	private	DocumentAction documentAction;	
	
	public DocumentContextConfig(String jsonPath,String propertyValue, String propertyKey, DocumentAction documentAction) {
		super();
		this.jsonPath = jsonPath;
		this.propertyValue = propertyValue;
		this.propertyKey = propertyKey;
		this.documentAction = documentAction;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	public String getPropertyKey() {
		return propertyKey;
	}
	public void setPropertyKey(String propertyKey) {
		this.propertyKey = propertyKey;
	}
	public DocumentAction getDocumentAction() {
		return documentAction;
	}
	public void setDocumentAction(DocumentAction documentAction) {
		this.documentAction = documentAction;
	}
	public String getJsonPath() {
		return jsonPath;
	}
	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}
}

