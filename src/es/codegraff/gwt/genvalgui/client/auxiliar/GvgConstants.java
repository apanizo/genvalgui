package es.codegraff.gwt.genvalgui.client.auxiliar;

import java.util.ArrayList;
import java.util.HashMap;

public class GvgConstants {
	
	/**
	 * The steps necessary to pass the mediator for generate the Tree Node
	 * and the code creator.
	 * 
	 * {@link GvgBuilderGenerator.mediatorGenerator(...)}
	 * 
	 * @author apanizo
	 *
	 */
	public enum Steps{
		Init, Generate, Create
	}

	/**
	 * The meaning of roles is simple. As you know, the build Generator writes
	 * the Cell, Column and field updater for each field.
	 * In addition, the classes are contained in the package 
	 * genvalgui.rebind.creator.cell implements  one  method  for  write all the 
	 * parts,so the different are obviously, when you create the cell and column 
	 * I pass the  GvgGeneratorNodeField  that represent the field, but when you 
	 * want to write nested tabled, columns and cell (because you have a complex 
	 * field, like ArrayList<HasMap<Integer,String>>) the code will be different.
	 * 
	 * In short, if you have a method that has the parent Rol you must call with
	 * nodeField.getParent().  
	 */
	public enum Roles{
		ParentType, Type, ChildType
	}
	
	public static final String STRING = String.class.getName();
	public static final String STRING_SIMPLE = "String ";
	
	public static final String BOOLEAN = Boolean.class.getName();
	public static final String BOOLEAN_SIMPLE = "boolean";
	public static final String BOOLEAN_CONVERSOR = "Boolean.parseBoolean(value)"; 
	
	public static final String INTEGER = Integer.class.getName();
	public static final String INTEGER_SIMPLE = "int";
	public static final String INTEGER_CONVERSOR = "Integer.parseInt(value)";
	
	public static final String FLOAT = Float.class.getName();
	public static final String FLOAT_SIMPLE = "float";
	public static final String FLOAT_CONVERSOR = "Float.parseFloat(value)";
	
	public static final String DOUBLE = Double.class.getName();
	public static final String DOUBLE_SIMPLE = "double";
	public static final String DOUBLE_CONVERSOR = "Double.parseDouble(value)";
	
	public static final String CHAR = Character.class.getName();
	public static final String CHAR_SIMPLE = "char";
	public static final String CHAR_CONVERSOR = "value.charAt(0)";
	
	
	public static final String ARRAYLIST = ArrayList.class.getName();
	public static final String ARRAYLIST_SIMPLE = "ArrayList";
	
	public static final String HASHMAP = HashMap.class.getName();
	public static final String HASHMAP_SIMPLE = "HashMap";

	/**
	 * Vars for the FieldUpdater
	 */
	public static final String COLUMN_INDEX = "columnIndex";
	public static final String OBJECT_VAR_FIELD_UPDATER = "object";
	public static String VALUE_VAR_FIELD_UPDATER = "value";
	public static final String INDEX_VAR_FIELD_UPDATER = "index";
	/**
	 * Vars for the validation
	 */
	
	
	
	/**
	 * Vars fot the codeGenerator
	 */
	public static String NAME_POPUP="p";
	public static final String NAME_BUILDER_METHOD = "buildGui";
}
