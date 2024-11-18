package snippet;

import java.lang.reflect.Method;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import static net.bytebuddy.matcher.ElementMatchers.*;

public class ClassAlterator {
	
	public static Class subclass(Class klass,Class superClass,String methodName ,Object returnValue) {
		Class dynamicType = new ByteBuddy()
				  .subclass(superClass)
				  .method(ElementMatchers.named(methodName))
				  .intercept(FixedValue.value(returnValue))
				  .make()
				  .load(klass.getClassLoader(),          
				        ClassLoadingStrategy.Default.INJECTION)
				  .getLoaded();
		return dynamicType;
	}
	
	public static Class redefine(Class klass,Class superClass,String methodName ,Object returnValue) {
		ByteBuddyAgent.install();
		Class dynamicType = new ByteBuddy()
				  .redefine(superClass)
				  .method(ElementMatchers.named(methodName))
				  .intercept(FixedValue.value(returnValue))
				  .make()
				  .load(klass.getClassLoader(),ClassReloadingStrategy.fromInstalledAgent())
				  .getLoaded();
		return dynamicType;
	}
	
	public static Class redefine(Class klass,Class superClass,String methodName) {
		ByteBuddyAgent.install();
		Class dynamicType = new ByteBuddy()
				  .redefine(superClass)
				  .method(ElementMatchers.named(methodName))
				  .intercept(FixedValue.nullValue())
				  .make()
				  .load(klass.getClassLoader(),ClassReloadingStrategy.fromInstalledAgent())
				  .getLoaded();
		return dynamicType;
	}
	
	public static Class redefineWithCustomReturnType(String methodName ,Class classInWhichMethodDeclared) {
		ByteBuddyAgent.install();
		ElementMatcher.Junction<MethodDescription> compositeMatcher = isDeclaredBy(classInWhichMethodDeclared).and(named(methodName));

		Class dynamicType = new ByteBuddy()
				  .redefine(classInWhichMethodDeclared)
				  .method(compositeMatcher)
				  .intercept(MethodDelegation.to(Interceptor.class))
				  .make()
				  .load(classInWhichMethodDeclared.getClassLoader(),ClassReloadingStrategy.fromInstalledAgent())
				  .getLoaded();
		return dynamicType;
	}
	
    public static class Interceptor {		  
		  @RuntimeType
		  public static Entity invokeWithReturnAndClassArgument(Entity arg) {
			  Entity entity = new Entity("Java Agent","Byte buddy");
			  return entity;
		  }
    }
}
