package jsonpath;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;

import net.minidev.json.JSONArray;
import sf.FilterNode;
import snippet.Runner;

public class JsonPathEventMeta {

	public static void main(String[] args) {
        String eventType = "sap.hr.odm.task.decorated";
        String topic = "sap.hr.odm.task";
		Map<String,String> param = Map.of("eventType",eventType,"topic",topic);
		String template = "$[?(@['eventType'] == '${eventType}' && @['topic'] == '${topic}')]['filterParameters'][*]";
		StringSubstitutor substitutor = new StringSubstitutor(param);
		String filterParamJsonPath = substitutor.replace(template);
	
		String eventMetaFile = "event-meta.json";
		String json = getJson(eventMetaFile);
		String filterConfig = getJson("filter-complex.json");
		ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
		List<FilterNode> flattenedFilterNodes = null;
		try {
			FilterNode filterNode =  mapper.readValue(filterConfig, FilterNode.class);
			flattenedFilterNodes = flattenFilterNode(filterNode);
			System.out.println(flattenedFilterNodes);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		List<Pair<String,String>> tuples = retrieveFilterParamWithJsonPath(json,filterParamJsonPath);

		Map<String,String> filterParamNameToDataType = tuples.stream()
				     .collect(Collectors.toMap(Pair::getLeft,  Pair::getRight));

		if(!CollectionUtils.isEmpty(flattenedFilterNodes)) {
			validateFilterParamName(flattenedFilterNodes,filterParamNameToDataType);
			validateFilterParamDataType(flattenedFilterNodes,filterParamNameToDataType);
		}
		
		
	}

	private static List<Pair<String,String>> retrieveFilterParamWithJsonPath(String json,String jsonPath) {
		try {
			Object values = JsonPath.parse(json).read(jsonPath);
			if(values instanceof JSONArray) {
				JSONArray array = (JSONArray)values;
				return array.stream().map(node -> {
					if(node instanceof Map) {
						Map properties = (Map) node;
						return Pair.<String,String>of((String)properties.get("name"),
								(String)properties.get("dataType"));
					} else {
						return Pair.<String,String>of(null, null);		
					}
				})
				.filter(tuple -> Objects.nonNull(tuple.getLeft()) && Objects.nonNull(tuple.getRight()) )
				.collect(Collectors.toList());
			}
		} catch (JsonPathException e) {
			System.err.println(e.getMessage());
		}
		return List.of();
	}
	
	private static String getJson(String file) {
		String json = null;
		try {
			json = IOUtils.resourceToString(file, Charset.defaultCharset(),
					Runner.class.getClassLoader());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return json;
	}
	
	
	private static boolean validateFilterParamDataType(List<FilterNode> flattenedFilterNodes,Map<String,String> filterParamNameToDataType) {

		Optional<FilterNode> filterNodeWithMismatchedFilterParamDatatype = flattenedFilterNodes.stream()
				.filter(filterNode -> isFilterParamPresentWithMismatchedDatatype(filterNode,filterParamNameToDataType))
				.findFirst();
		System.out.println("filterNode with mismatched filter param datatype present="+filterNodeWithMismatchedFilterParamDatatype.isPresent());					
		return filterNodeWithMismatchedFilterParamDatatype.isEmpty();

	}
	
	private static boolean isFilterParamPresentWithMismatchedDatatype(FilterNode filterNode,Map<String,String> filterParamNameToDataType) {
		Map<String,String> javaDataTypeToJsDataType = Map.of(String.class.getName(),"string",Boolean.class.getName(),"boolean",
				Integer.class.getName(),"number",Float.class.getName(),"number",Long.class.getName(),"number",
				Double.class.getName(),"number");
		if(filterParamNameToDataType.containsKey(filterNode.getFieldOperand())) {
			var filterParamDatatype = filterParamNameToDataType.get(filterNode.getFieldOperand());
			if(!StringUtils.isEmpty(filterParamDatatype)) {
				var javaDataType = filterNode.getValueOperand().getClass().getName();
				return !filterParamDatatype.equals(javaDataTypeToJsDataType.get(javaDataType));
			} else {
				return true;
			}
			
		} else {
			return true;
		}
	}
	
	private static boolean validateFilterParamName(List<FilterNode> flattenedFilterNodes,Map<String,String> filterParamNameToDataType) {
		Optional<FilterNode> filterNodeWithUnknownFilterParam = flattenedFilterNodes.stream()
							.filter(filterNode -> !filterParamNameToDataType.containsKey(filterNode.getFieldOperand()))
							.findFirst();
		System.out.println("filterNode with unknown filter param present="+filterNodeWithUnknownFilterParam.isPresent());					
		return filterNodeWithUnknownFilterParam.isEmpty();
	}
	
	private static List<FilterNode> flattenFilterNode(FilterNode filterNode){
		if (!CollectionUtils.isEmpty(filterNode.getChildren())) {
			List<FilterNode> flattenedFilterNodes = new ArrayList<>();
			for (FilterNode childFilterNode : filterNode.getChildren()) {
				List<FilterNode> flattenedFilterNodesFromChild = flattenFilterNode(childFilterNode);
				flattenedFilterNodes.addAll(flattenedFilterNodesFromChild);
			}
			return flattenedFilterNodes;
		} else {
			if (!StringUtils.isEmpty(filterNode.getFieldOperand()) && !StringUtils.isEmpty(filterNode.getValueOperand())) {
				return List.of(filterNode);
			}
		}
		return List.of();
		
	}
}
