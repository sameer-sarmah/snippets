package sf;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

public class FilterExpressionBuilder {
		
	public static String buildFilterExpression(FilterNode filterNode) {
		var filterString = new StringBuilder("");
		if (!CollectionUtils.isEmpty(filterNode.getChildren())) // if we have children operation should only be and/or
		{
			boolean isFirst = true;
			for (FilterNode childFilterNode : filterNode.getChildren()) {
				var childFilterString = buildFilterExpression(childFilterNode);
				if (!StringUtils.isEmpty(childFilterString)) {
					if (childFilterString.indexOf(" and ") > -1 || childFilterString.indexOf(" or ") > -1)
						childFilterString = "(" + childFilterString + ")";
					if (!isFirst) {
						filterString.append(" " + filterNode.getOperation() + " ");
					}

					filterString.append(childFilterString) ;
					isFirst = false;
				}
			}
		} else {
			if (!StringUtils.isEmpty(filterNode.getFieldOperand()) && !StringUtils.isEmpty(filterNode.getValueOperand())) {
				if (Objects.nonNull(filterNode.getOperation())) {
					//unary operations e.g #tolower(#root['gender'])
					if (filterNode.getOperation().equals(FilterNode.Operation.TOLOWERCASE)
							|| filterNode.getOperation().equals(FilterNode.Operation.TOUPPERCASE)
							|| filterNode.getOperation().equals(FilterNode.Operation.TRIM)) {
						return String.format("( #%s(%s) )",filterNode.getOperation(),generateSpelOperand(filterNode.getFieldOperand()));
					} 
					//binary string operations 
					//e.g #startswith(#root['gender'],'ma')
					//#root[age] gt 60
					else  if (filterNode.getOperation().equals(FilterNode.Operation.STARTS_WITH)
							|| filterNode.getOperation().equals(FilterNode.Operation.ENDS_WITH)){
						return String.format("( #%s(%s,'%s') )",filterNode.getOperation(),generateSpelOperand(filterNode.getFieldOperand())
								,filterNode.getValueOperand());
					}
					//collection operations
					//#containsAny(#emp['lob'],{'SuccessFactors','Hana'})
					else  if (filterNode.getOperation().equals(FilterNode.Operation.CONTAINS_ANY) 
							&& filterNode.getValueOperand() instanceof List){
						List<String> valueOperand = (List) filterNode.getValueOperand();
						if(!CollectionUtils.isEmpty(valueOperand)) {
							String stringifiedList =  valueOperand
									   .stream()
									   .map(org.springframework.util.StringUtils::quote)
									   .collect(Collectors.joining(",", "{", "}"));
							return String.format("( #%s(%s,%s) )",filterNode.getOperation(),generateSpelOperand(filterNode.getFieldOperand())
									,stringifiedList);
						}

						
						
					}
					//#isAfter(#emp['doj'],'2015-01-30T00:00:00+00:00')
					//#isBefore(#emp['doj'],'2023-01-30T00:00:00+00:00')
					else  if (filterNode.getOperation().equals(FilterNode.Operation.IS_AFTER)
							|| filterNode.getOperation().equals(FilterNode.Operation.IS_BEFORE)){
						return String.format("( #%s(%s,'%s') )",filterNode.getOperation(),generateSpelOperand(filterNode.getFieldOperand())
								,filterNode.getValueOperand());
					}
					//binary numeric/string operations 
					//#root[age] gt 60
					else  if (filterNode.getOperation().equals(FilterNode.Operation.GREATER_THAN)
							|| filterNode.getOperation().equals(FilterNode.Operation.GREATER_THAN_EQUAL)
							|| filterNode.getOperation().equals(FilterNode.Operation.LESS_THAN)
							|| filterNode.getOperation().equals(FilterNode.Operation.LESS_THAN_EQUAL)
							|| filterNode.getOperation().equals(FilterNode.Operation.NOT_EQUAL)
				|| filterNode.getOperation().equals(FilterNode.Operation.EQUAL)){
						boolean isStringValue = filterNode.getValueOperand() instanceof String ?true :false;
						return "( "+ generateSpelOperand(filterNode.getFieldOperand()) + " "+  filterNode.getOperation() +" "
								+(isStringValue?"'"+filterNode.getValueOperand()+"'":filterNode.getValueOperand()) + " )";
					}

				}
			}
		}

		return filterString.toString();
	}
	
	
	//   "department/name":"Engineering" =>  #emp[department][name] eq 'Engineering' 
	private static String generateSpelOperand(String operand) {
		if(!operand.contains("/")) {
			return String.format("#root['%s']",operand);
		}
		String[] segments =  operand.split("/");
		String spelExpressionPath = Arrays.stream(segments)
			.map(segment -> String.format("['%s']", segment) ) 
			.collect(Collectors.joining(""));
		return String.format("#root%s",spelExpressionPath);
	}

}
