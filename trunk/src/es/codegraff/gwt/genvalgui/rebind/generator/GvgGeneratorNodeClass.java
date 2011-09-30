package es.codegraff.gwt.genvalgui.rebind.generator;

import java.util.ArrayList;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;

import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgCellWidgetInfo;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgIdInfo;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgType;

/**
 * This class represent the parent class. For example:
 * 	class Person{
 * 		String name;
 * 		Integer age;
 * 	}
 * This class represent the info of Person.
 * 
 * @author apanizo
 *
 */
public class GvgGeneratorNodeClass {
	private String nameClass;
	private String nameClassSimple;
	private String nameTable;
	private ArrayList<GvgGeneratorNodeField> fields = new ArrayList<GvgGeneratorNodeField>();
	
	//getters and setters
	public String getNameClass() {
		return nameClass;
	}
	public void setNameClass(String nameClass) {
		this.nameClass = nameClass;
	}
	public ArrayList<GvgGeneratorNodeField> getFields() {
		return fields;
	}
	public void setFields(ArrayList<GvgGeneratorNodeField> fields) {
		this.fields = fields;
	}

	public String getNameClassSimple() {
		return nameClassSimple;
	}
	public void setNameClassSimple(String nameClassSimple) {
		this.nameClassSimple = nameClassSimple;
	}
	public String getNameTable() {
		return nameTable;
	}
	public void setNameTable(String nameTable) {
		this.nameTable = nameTable;
	}

	//methods
	public static ArrayList<GvgGeneratorNodeField> createNodeFields(JClassType type){
		ArrayList<GvgGeneratorNodeField> containerNodeFields = new ArrayList<GvgGeneratorNodeField>();
		GvgIdInfo idInfo = new GvgIdInfo(type.getName(), type.getName(),null, false, false);
		GvgGeneratorNodeField nodeOrigenClass = new GvgGeneratorNodeField(
				new GvgType(type.getQualifiedSourceName(), type.getName(), type.getParameterizedQualifiedSourceName()), 
				new GvgCellWidgetInfo(idInfo), 
				idInfo);
		for(JField field : type.getFields()){
			//Check
			GvgGeneratorNode.checkIsValidField(field); 
			//Create the GvgGeneratorNodeField
			GvgGeneratorNodeField nodeField = GvgGeneratorNodeField.createNodeField(nodeOrigenClass, field);
			//Add to the Node Class Container
			containerNodeFields.add(nodeField);
		}
		return containerNodeFields;
	}

	@Override
	public String toString(){
		String s="";
		for(GvgGeneratorNodeField nodeField: this.fields){
			s=s+"\tField: "+nodeField.getGvgIdInfo().getId()+"\n";
			s=s+nodeField.toString()+"\n";
		}
		return s;
	}
	
}
