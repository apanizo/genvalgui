package es.codegraff.gwt.genvalgui.client;

import javax.validation.Validation;
import javax.validation.Validator;

import com.google.gwt.user.cellview.client.CellTable;

import es.codegraff.gwt.genvalgui.validator.ui.ErrMessagePanel;


public interface GvgInterfaceBuilder {
	
	static final Validator validator = 
		Validation.buildDefaultValidatorFactory().getValidator();
	
	static final ErrMessagePanel emp = new ErrMessagePanel();
	
	public <T> CellTable<T> buildGui(Class<T> c,Object o);
	
}
