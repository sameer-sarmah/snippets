package snippet;

public class Parent {

	protected void invokeWithoutReturn() {
		System.out.println("Parent.invokeWithoutReturn");
	}
	
	protected String invokeWithReturn() {
		System.out.println("Parent.invokeWithReturn");
		return "Parent";
	}
	
	protected String invokeWithReturnAndArgument(String arg) {
		System.out.println("Parent.invokeWithReturnAndArgument");
		return "Parent";
	}
	
	protected Entity invokeWithReturnAndClassArgument(Entity arg) {
		System.out.println("Parent.invokeWithReturnAndClassArgument");
		arg.setName("Java Agent");
		arg.setValue("Byte Buddy");
		return arg;
	}
}
