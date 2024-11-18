package snippet;

import java.util.concurrent.Callable;

import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

public class ChildTest {

	@Spy
	private Child child;
	
    @BeforeMethod
    public void initMocks() {
    	MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void invokeWithReturnTest() {
		String returnValue = "StubbedParent";
		ClassAlterator.redefine(Parent.class, Parent.class,"invokeWithReturn",returnValue);
		Assert.assertEquals(new Parent().invokeWithReturn(),returnValue);
		Assert.assertEquals(child.invokeWithReturn(),returnValue.toUpperCase());
	}	
	
	@Test
	public void invokeWithReturnNullTest() {
		ClassAlterator.redefine(Parent.class, Parent.class,"invokeWithReturn");
		Assert.assertEquals(new Parent().invokeWithReturn(),null);
		Assert.assertEquals(child.invokeWithReturn(),"");
	}
	
	@Test
	public void invokeWithReturnAndArgumentTest() {
		String returnValue = "StubbedParent";
		ClassAlterator.redefine(Parent.class, Parent.class,"invokeWithReturnAndArgument",returnValue);
		Assert.assertEquals(new Parent().invokeWithReturnAndArgument(""),returnValue);
		Assert.assertEquals(child.invokeWithReturnAndArgument(""),returnValue.toUpperCase());
	}
	
	@Test
	public void invokeWithReturnAndClassArgumentTest() {
		Entity entity = new Entity("Java Agent","Byte buddy");
		ClassAlterator.redefineWithCustomReturnType("invokeWithReturnAndClassArgument",Parent.class);
		Assert.assertEquals(new Parent().invokeWithReturnAndClassArgument(entity),entity);
		Entity responseEntity = child.invokeWithReturnAndClassArgument(entity);
		Assert.assertEquals(responseEntity.getName(),entity.getName());
		Assert.assertEquals(responseEntity.getValue(),entity.getValue()+" Child");
	}
	

}


