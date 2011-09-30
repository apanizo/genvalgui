package es.codegraff.gwt.genvalgui.rebind.generator.auxiliar;

import com.google.gwt.core.ext.typeinfo.JField;

import es.codegraff.gwt.genvalgui.client.auxiliar.Caption;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgUtils;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeField;


/**
 * This class is the contract between the generated code and
 * the generator.
 * This class stores the name of the table, column and cell,
 * which belongs the field.
 * 
 * {@link GvgGeneratorNodeField //1:1}
 * 
 * @author apanizo
 *
 */
public class GvgCellWidgetInfo {
	private String nameTable;
	private String nameColumn;
	private String nameColumnTittle;
	private String nameButton;
	private String nameCell;
	private String nameVar;
	private String nameVarLocal;
	private String nameVarArray;
	private String nameField;
	
	
	
	private GvgCellWidgetInfo(String nameTable, String nameColumn, String nameColumnTittle,
			String nameButton, String nameCell, String nameVar, String nameVarLocal, String nameVarArray,
			String nameField) {
		this.nameTable = nameTable;
		this.nameColumn = nameColumn;
		this.nameColumnTittle = nameColumnTittle;
		this.nameButton = nameButton;
		this.nameCell = nameCell;
		this.nameVar=nameVar;
		this.nameVarLocal = nameVarLocal;
		this.nameVarArray = nameVarArray;
		this.nameField=nameField;
	}

	
	
	public GvgCellWidgetInfo(GvgIdInfo gvgIdInfo) {
		String zipName = GvgUtils.transformPathToNameVar(gvgIdInfo.getPath());
		this.nameTable = zipName+"Table";
		this.nameColumn = zipName+"Column";
		this.nameColumnTittle = GvgUtils.getFieldUpperCase(gvgIdInfo.getId());
		this.nameButton = "Click Me";
		this.nameCell = zipName+"Cell";
		this.nameVar=zipName+"Var";
		this.nameVarLocal = zipName+"Local";
		this.nameVarArray = zipName+"Array";
		this.nameField= GvgUtils.getFieldUpperCase(gvgIdInfo.getId());
	}

	//getters and setters
	
	
	public String getNameTable() {
		return nameTable;
	}

	public String getNameField() {
		return nameField;
	}

	public void setNameField(String nameField) {
		this.nameField = nameField;
	}

	public String getNameButton() {
		return nameButton;
	}

	public void setNameButton(String nameButton) {
		this.nameButton = nameButton;
	}

	public String getNameColumnTittle() {
		return nameColumnTittle;
	}

	public void setNameColumnTittle(String nameColumnTittle) {
		this.nameColumnTittle = nameColumnTittle;
	}
	

	public String getNameVar() {
		return nameVar;
	}

	public void setNameVar(String nameVar) {
		this.nameVar = nameVar;
	}

	public String getNameVarLocal() {
		return nameVarLocal;
	}

	public void setNameVarLocal(String nameVarLocal) {
		this.nameVarLocal = nameVarLocal;
	}

	public String getNameVarArray() {
		return nameVarArray;
	}

	public void setNameVarArray(String nameVarArray) {
		this.nameVarArray = nameVarArray;
	}

	public void setNameTable(String nameTable) {
		this.nameTable = nameTable;
	}

	public String getNameColumn() {
		return nameColumn;
	}

	public void setNameColumn(String nameColumn) {
		this.nameColumn = nameColumn;
	}

	public String getNameCell() {
		return nameCell;
	}

	public void setNameCell(String nameCell) {
		this.nameCell = nameCell;
	}

	public static String createNameColumnTittle(JField field) {
		return (field.isAnnotationPresent(Caption.class)) ?  
				field.getAnnotation(Caption.class).name() : GvgUtils.getFieldUpperCase(field.getName());
	}

	public static String createNameButton(JField field) {
		return (field.isAnnotationPresent(Caption.class)) ?  
				field.getAnnotation(Caption.class).button() : "Click Me" ;
	}
	
	public GvgCellWidgetInfo generateParameterizedInfo(Integer level) {
		String paramNameTable = this.nameTable+level;
		String paramNameColumn = this.nameColumn+level;
		String paramNameColumnTittle = this.nameColumnTittle+"_"+level+"_";
		String paramNameButton = this.nameButton;
		String paramNameCell = this.nameCell;//+level;
		String paramNameVar = this.nameVar+level;
		String paramNameVarLocal = this.nameVarLocal+level;
		String paramNameVarArray = this.nameVarArray;
		String paramNameField = this.nameField;
		GvgCellWidgetInfo aux = 
			new GvgCellWidgetInfo(paramNameTable,paramNameColumn,paramNameColumnTittle,
					paramNameButton, paramNameCell,paramNameVar, paramNameVarLocal, 
					paramNameVarArray, paramNameField);
		return aux;
	}
	
	@Override
	public GvgCellWidgetInfo clone(){
		return new GvgCellWidgetInfo(this.nameTable,this.nameColumn,this.nameColumnTittle,
				this.nameButton, this.nameCell, this.nameVar, this.nameVarLocal, this.nameVarArray,
				this.nameField);
	}
	
	@Override
	public String toString(){
		String s="";
		s=s+"\t\t\tnameTable: "+this.nameTable+"\n";
		s=s+"\t\t\tnameColumn: "+this.nameColumn+"\n";
		s=s+"\t\t\tnameColumnTittle: "+this.nameColumnTittle+"\n";
		s=s+"\t\t\tnameCell: "+this.nameCell+"\n";
		s=s+"\t\t\tnameVar: "+this.nameVar+"\n";
		s=s+"\t\t\tnameVarLocal: "+this.nameVarLocal+"\n";
		s=s+"\t\t\tnameVarArray: "+this.nameVarArray+"\n";
		s=s+"\t\t\tnameField: "+this.nameField+"\n";
		return s;
	}



	public static String createNameTable(String nameClass) {
		return nameClass.toLowerCase()+"Table";
	}
}
