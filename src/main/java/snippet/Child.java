package snippet;

import org.apache.commons.lang3.StringUtils;

public class Child extends Parent{
	
	public void invokeWithoutReturn() {
		super.invokeWithoutReturn();
		System.out.println("Child.invokeWithoutReturn");
	}
	
	public String invokeWithReturn() {
		System.out.println("Child.invokeWithReturn");
		String valueFromParent =super.invokeWithReturn();
		return StringUtils.isEmpty(valueFromParent) ? "" :valueFromParent.toUpperCase();
	}
	
	public String invokeWithReturnAndArgument(String arg) {
		System.out.println("Child.invokeWithReturnAndArgument");
		String valueFromParent =super.invokeWithReturnAndArgument(arg);
		return StringUtils.isEmpty(valueFromParent) ? "" :valueFromParent.toUpperCase();
	}
	
	public Entity invokeWithReturnAndClassArgument(Entity arg) {
		System.out.println("Child.invokeWithReturnAndClassArgument");
		Entity valueFromParent =super.invokeWithReturnAndClassArgument(arg);
		valueFromParent.setName(valueFromParent.getName());
		valueFromParent.setValue(valueFromParent.getValue() + " Child");
		System.out.println("Decorated Entity in Child.invokeWithReturnAndClassArgument");
		return valueFromParent;
	}
}
