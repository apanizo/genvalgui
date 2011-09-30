package es.codegraff.gwt.genvalgui.client.auxiliar;

import java.util.ArrayList;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;

import es.codegraff.gwt.genvalgui.client.GvgInfoAnnotation;


public class GvgUtils{
	public static TreeLogger logger;
	private static ArrayList<String> enumClass= null;
	private static ArrayList<String> permitedClass= null;
	private static Class<?>[] validationGroups;	
	public static ArrayList<String> getEnumClass() {
		return enumClass;
	}

	public static ArrayList<String> getPermitedClass() {
		return GvgUtils.permitedClass;
	}
	
	public static boolean isJavaUtilType(String type){
		
		if ((GvgConstants.HASHMAP.equals(type))||(GvgConstants.HASHMAP_SIMPLE.equals(type)))
			return true;
		if ((GvgConstants.ARRAYLIST.equals(type))||(GvgConstants.ARRAYLIST_SIMPLE.equals(type)))
			return true;
		if (type.startsWith("java.Util")){
			GvgUtils.logger.log(TreeLogger.ERROR, "[Utils - isJavaUtilClass] " +
					"- A Java util class: ["+type+"] have been found but is not allowed.",
					new UnableToCompleteException());
			return false;
		}
		return false;
	}
	public static boolean isPrimitiveType(String type){
		for (JPrimitiveType j : JPrimitiveType.values()){
			if (j.getQualifiedSourceName().equals(type))
				return true;
			if (j.getSimpleSourceName().equals(type))
				return true;
			if (j.getQualifiedBoxedSourceName().equals(type))
				return true;
		}
		return false;
	}
	public static boolean isStringType(String type){
		if ((GvgConstants.STRING.equals(type))||(GvgConstants.STRING_SIMPLE.equals(type)))
			return true;
		return false;
	}
	public static boolean isEnumType(String type){
		return GvgUtils.enumClass.contains(type);
	}
	public static boolean isPermitedClassType (String type){
		return GvgUtils.permitedClass.contains(type);
	}
	//Methdos for generic proposal
	public static  String getFieldUpperCase(String name) {
		String aux = name.substring(0, 1);
		String mayus = aux.toUpperCase()+name.substring(1);
		return mayus;
	}
	
	//Methods for add information
	public static void addPermitedType(GvgInfoAnnotation gvgAnnotation){
		if(GvgUtils.permitedClass == null)
			GvgUtils.permitedClass = new ArrayList<String>(); 
		for(Class<?> c : gvgAnnotation.value()){
			GvgUtils.permitedClass.add(c.getName());
			GvgUtils.permitedClass.add(c.getSimpleName());
		}
	}
	public static void addLogger(TreeLogger logger) {
		GvgUtils.logger=logger;
	}

	public static void addEnumType(ArrayList<String> enumClass) {
		GvgUtils.enumClass=enumClass;
	}

	public static boolean isPermitedSimpleType(String s) {
		if(!s.contains("."))
			return true;
		return false;
	}

	public static String transformPathToNameVar(String path) {
		return path.replace("-","").toLowerCase();
	}

	public static String stringToPrimitive(String type, String nameVar){
		if ((type.equals(GvgConstants.INTEGER))||(type.equals(GvgConstants.INTEGER_SIMPLE))){
			return GvgConstants.INTEGER_CONVERSOR;
		}
		if ((type.equals(GvgConstants.DOUBLE))||(type.equals(GvgConstants.DOUBLE_SIMPLE))){
			return GvgConstants.DOUBLE_CONVERSOR;
		}
		if ((type.equals(GvgConstants.CHAR_SIMPLE))||(type.equals(GvgConstants.CHAR))){
			return GvgConstants.CHAR_CONVERSOR;
		}
		if ((type.equals(GvgConstants.FLOAT))||(type.equals(GvgConstants.FLOAT_SIMPLE))){
			return GvgConstants.FLOAT_CONVERSOR;
		}
		if ((type.equals(GvgConstants.BOOLEAN))||(type.equals(GvgConstants.BOOLEAN_SIMPLE))){
			return GvgConstants.BOOLEAN_SIMPLE;
		}
		if ((type.equals(GvgConstants.STRING))||(type.equals(GvgConstants.STRING_SIMPLE))){
			return GvgConstants.VALUE_VAR_FIELD_UPDATER;
		}
		if(GvgUtils.isEnumType(type)){
			return type+".valueOf("+GvgConstants.VALUE_VAR_FIELD_UPDATER+")";
		}
		GvgUtils.logger.log(TreeLogger.ERROR, "[GvgUtils - toPrimitive] " +
				"Error, not found a primitive conversion for the type: "+type);
		return "";
	}

	public static String getValidationGroupsString() {
		String s = "";
		for(Class<?> c : GvgUtils.validationGroups){
			s= s+c.getName()+".class,";
		}
		return  s.substring(0, s.length()-1);
	}
	public static Class<?>[] getValidationGroups(){
		return GvgUtils.validationGroups;
	}
	public static void setValidationGroups(Class<?>[] groups) {
		GvgUtils.validationGroups=groups;
		
	}
	
}
