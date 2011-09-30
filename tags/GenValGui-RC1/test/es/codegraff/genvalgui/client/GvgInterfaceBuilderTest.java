package es.codegraff.genvalgui.client;

import javax.validation.Validation;
import javax.validation.Validator;

import com.google.gwt.junit.client.GWTTestCase;

public class GvgInterfaceBuilderTest extends GWTTestCase{

	@Override
	public String getModuleName() {
		// TODO Auto-generated method stub
		return null;
	}
	public void testGvgInterfaceBuilder(){
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		assertNotNull(validator);
		assertTrue(validator instanceof javax.validation.Validator);
	}
}
