package es.codegraff.gwt.genvalgui.rebind.creator.cell;

import com.google.gwt.codegen.server.StringSourceWriter;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.SourceWriter;

import es.codegraff.gwt.genvalgui.client.auxiliar.GvgConstants;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgUtils;
import es.codegraff.gwt.genvalgui.rebind.creator.popup.PopupCreator;
import es.codegraff.gwt.genvalgui.rebind.creator.table.TableCellCreator;
import es.codegraff.gwt.genvalgui.rebind.creator.validator.ValidationCreator;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeField;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgCellWidgetInfo;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgIdInfo;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgType;

public class JavaUtilCellConstructor extends CellConstructor {
	public JavaUtilCellConstructor(){
		
	}
		
	@Override
	public String createUpdater(GvgGeneratorNodeField parentNodeField){
		if (GvgUtils.isJavaUtilType(parentNodeField.getGvgType().getType())){
			if (parentNodeField.getGvgType().getType().equals(GvgConstants.ARRAYLIST)){
				return createUpdaterArrayList(parentNodeField.getChild().getGvgType().getType(),
							parentNodeField.getGvgCellWidgetInfo().getNameTable(),
							parentNodeField.getChild().getGvgCellWidgetInfo().getNameColumn(),
							parentNodeField.getGvgCellWidgetInfo().getNameVarLocal());
			}
			
			if (parentNodeField.getGvgType().getType().equals(GvgConstants.HASHMAP)){
				return createUpdaterHashMap(parentNodeField.getGvgType().getTypeParameterized(),
						parentNodeField.getChild().getGvgType().getType(),
						parentNodeField.getGvgCellWidgetInfo().getNameVarLocal(),
						parentNodeField.getGvgIdInfo().getId(),
						parentNodeField.getGvgCellWidgetInfo().getNameTable(),
						parentNodeField.getChild().getGvgCellWidgetInfo().getNameColumn()
						);
			}
			GvgUtils.logger.log(TreeLogger.ERROR, "Error, found a Java Util Class, " +
					"but not functionality yet for the type: "+parentNodeField.getGvgType().getTypeParameterized());
			return "";
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createUpdater(parentNodeField);
		}
	}

	private String createUpdaterArrayList(String type, String nameTable,
			String nameColumn, String nameVarLocal) {
		StringSourceWriter src = new StringSourceWriter();
		String valueOf = GvgUtils.stringToPrimitive(type,GvgConstants.VALUE_VAR_FIELD_UPDATER);
		src.println("%s = %s.getColumnIndex(%s);",GvgConstants.COLUMN_INDEX, nameTable, nameColumn);
		src.println("%s.remove(%s);",nameVarLocal, GvgConstants.COLUMN_INDEX);
		src.println("%s.add(%s, %s);",nameVarLocal, GvgConstants.COLUMN_INDEX, valueOf);
		return src.toString();
	}

	
	private String createUpdaterHashMap(String typeParameterized, String typeChild,
			String nameVarLocal, String id, String nameTable, String nameColumn) {
		StringSourceWriter src = new StringSourceWriter();
		int posMenor = typeParameterized.indexOf("<");
		int posComa = typeParameterized.indexOf(",");
		String keyType = typeParameterized.substring(posMenor+1, posComa);
		String valueOf = GvgUtils.stringToPrimitive(typeChild,GvgConstants.VALUE_VAR_FIELD_UPDATER);
		src.println("%s[] %skeyArray = new %s[object.size()];",keyType, id, keyType);
		src.println("%s.keySet().toArray(%skeyArray);",nameVarLocal,id);
		src.println("%s= %s.getColumnIndex(%s);",GvgConstants.COLUMN_INDEX, nameTable, nameColumn);
		src.println("%s.put(%skeyArray[%s], %s);",GvgConstants.OBJECT_VAR_FIELD_UPDATER,
				id, GvgConstants.COLUMN_INDEX, valueOf);
		return src.toString();
	}

	@Override
	public String createRowData(GvgGeneratorNodeField parentNodeField){
		if(GvgUtils.isJavaUtilType(parentNodeField.getGvgType().getTypeSimple())){
			return parentNodeField.getGvgCellWidgetInfo().getNameVarArray();
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createRowData(parentNodeField);
		}
	}
	
	@Override
	public String createCellAndColumn(SourceWriter src, String nameClass, GvgGeneratorNodeField gvgNodeField){
		if (GvgUtils.isJavaUtilType(gvgNodeField.getGvgType().getType())){
			//Get Vars
			GvgGeneratorNodeField nodeField = gvgNodeField.clone();
			GvgType type= nodeField.getGvgType();
			GvgCellWidgetInfo cellInfo= nodeField.getGvgCellWidgetInfo();
			GvgIdInfo idInfo = nodeField.getGvgIdInfo();
			//Generate code
			JavaUtilCellConstructor.createJavaUtilCellBegin(src, type.getTypeParameterized(),
					cellInfo.getNameButton(), cellInfo.getNameCell(),	cellInfo.getNameVarLocal(), 
					nodeField.getParent().getGvgCellWidgetInfo().getNameTable());
			
				PopupCreator.createPopup(src,GvgConstants.NAME_POPUP);
				TableCellCreator.createTable(src, nodeField.getGvgType().getTypeParameterized(),cellInfo.getNameTable());
					createFor(src, nodeField);
					JavaUtilCellConstructor.createJavaUtilCellContent(src, new CellConstructor(),
								nameClass,nodeField.getChild(),	type.getType(),idInfo.getId());
						TableCellCreator.createAddColumnToTable(src, cellInfo.getNameTable(), 
								nodeField.getChild().getGvgCellWidgetInfo().getNameColumn(),
								nodeField.getChild().getGvgCellWidgetInfo().getNameColumnTittle());
					createEndFor(src);
                    TableCellCreator.addInformationConvertedToAl(src,type.getTypeParameterized(),cellInfo.getNameVarLocal(),
							  cellInfo.getNameTable());
				PopupCreator.configurePopup(src,cellInfo.getNameTable());
				JavaUtilCellConstructor.createJavaUtilCellEnd(src);
			CellConstructor.createColumnBegin(src, nodeField.getParent().getGvgType().getTypeParameterized(),
					type.getTypeParameterized(),	cellInfo.getNameColumn(), cellInfo.getNameCell(), cellInfo.getNameVar());
			JavaUtilCellConstructor.createJavaUtilColumnContent(src, nodeField.getParent().clone());
			CellConstructor.createColumnEnd(src);
            return src.toString();
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createCellAndColumn(src, nameClass, gvgNodeField);
		}
	}
	
	private static void createJavaUtilCellBegin(SourceWriter src,
			String type, String nameButton, String nameCell, String varLocal, String parentNameTable) {
		src.println("	final ActionCell<%s> %s = new ActionCell<%s>(",type,nameCell,type);
		src.println("      \"%s\", new ActionCell.Delegate<%s>() {",nameButton, type);
		src.println("            public void execute(final %s %s) {",type,varLocal);
		src.println("if (%s == null){", varLocal);
		ValidationCreator.createValidationNull(src, parentNameTable,
				"The Java Util field is null. Check it");
		src.println("}else{");

	}
	private static void createJavaUtilCellContent(SourceWriter src,
			CellConstructor cellConstructor, String nameClass,
			GvgGeneratorNodeField nodeField, String type, String id) {
		cellConstructor.generateCellCode(src, cellConstructor, nameClass, nodeField);
	}
	
	private static void createJavaUtilCellEnd(SourceWriter src) {
		//the check null if
		src.println("}");
		src.println("            }");
	    src.println("          });");
	}
	
	private static void createJavaUtilColumnContent(SourceWriter src, GvgGeneratorNodeField nodeFieldParent) {
		String sChain = CellConstructor.createColumnContent(src, new CellConstructor(), nodeFieldParent);
		if (sChain != null){
			src.println("	return %s;",sChain);
		}
		
	}

	/**
	 * Marked as a class which must be changed in the future. If you add support
	 * for another Java.util class, you must add code in this method.
	 * @param src
	 * @param nodeField
	 */
	private void createFor(SourceWriter src, GvgGeneratorNodeField nodeField){
		if(GvgConstants.ARRAYLIST.equals(nodeField.getGvgType().getType())){
			src.println("for(final %s %s: %s){", nodeField.getChild().getGvgType().getType(),
					nodeField.getGvgCellWidgetInfo().getNameVarArray(),
					nodeField.getGvgCellWidgetInfo().getNameVarLocal());
			
			return;
		}
		if(GvgConstants.HASHMAP.equals(nodeField.getGvgType().getType())){
			src.println("for(final %s %s: %s.values()){",nodeField.getChild().getGvgType().getType(),
					nodeField.getGvgCellWidgetInfo().getNameVarArray(),
					nodeField.getGvgCellWidgetInfo().getNameVarLocal());
			return;
		}
		 				
		GvgUtils.logger.log(TreeLogger.ERROR, "[JavaUtilCellConstructor - createFor] Not found a " +
				"concrete implemented class to make the \"for\".",new UnableToCompleteException());
	}

	private void createEndFor(SourceWriter src) {
		src.println("				}");	
	}

}
	

