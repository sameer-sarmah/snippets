package sf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FilterNode {
	
	private String fieldOperand;
	private Operation operation;
	private Object valueOperand;
	
	private List<FilterNode> children = new ArrayList<>();	
	
	public String getFieldOperand() {
		return fieldOperand;
	}

	public void setFieldOperand(String fieldOperand) {
		this.fieldOperand = fieldOperand;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public Object getValueOperand() {
		return valueOperand;
	}

	public void setValueOperand(Object valueOperand) {
		this.valueOperand = valueOperand;
	}

	public List<FilterNode> getChildren() {
		return Collections.unmodifiableList(children) ;
	}

	
	
	@Override
	public String toString() {
		return "FilterNode [fieldOperand=" + fieldOperand + ", operation=" + operation + ", valueOperand="
				+ valueOperand + "]";
	}



	public static enum Operation { 
		AND("and",true),
		OR("or",true),
		EQUAL("eq",false),
		NOT_EQUAL("ne",false),
		GREATER_THAN("gt",false),
		GREATER_THAN_EQUAL("ge",false),
		LESS_THAN("lt",false),
		LESS_THAN_EQUAL("le",false),
		STARTS_WITH("startswith",false),
		ENDS_WITH("endswith",false),
		TOUPPERCASE("toupper",false),
		TOLOWERCASE("tolower",false),
		CONTAINS_ANY("containsAny",false),
		IS_BEFORE("isBefore",false),
		IS_AFTER("isAfter",false),
		TRIM("trim",false);
	
		
		public final String operationName;
		public final boolean logicalOperation;
		
		Operation(String operationName,boolean logicalOperation) {
			this.operationName = operationName;
			this.logicalOperation = logicalOperation;
		}
		@JsonValue
		public String getOperationName() {
			return operationName;
		}

		public boolean isLogicalOperation() {
			return logicalOperation;
		}
		
		@Override
		public String toString() {
			return operationName;			
		}
		

	}
}

