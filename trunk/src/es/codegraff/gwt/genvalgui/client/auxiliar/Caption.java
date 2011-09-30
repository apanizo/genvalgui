package es.codegraff.gwt.genvalgui.client.auxiliar;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.CLASS) 
@Target(ElementType.FIELD)
public @interface Caption {
	String name();

	String button() default "";
}
