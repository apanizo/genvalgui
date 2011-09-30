package es.codegraff.gwt.genvalgui.rebind.creator.cell;

import com.google.gwt.codegen.server.StringSourceWriter;
import com.google.gwt.user.rebind.SourceWriter;

import es.codegraff.gwt.genvalgui.client.auxiliar.GvgConstants;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgUtils;
import es.codegraff.gwt.genvalgui.rebind.creator.popup.PopupCreator;
import es.codegraff.gwt.genvalgui.rebind.creator.validator.ValidationCreator;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeField;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgCellWidgetInfo;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgType;


public class PermitedClassCellConstructor extends CellConstructor {
	
	public PermitedClassCellConstructor(){
		
	}
	
	@Override
	public String createUpdater(GvgGeneratorNodeField parentNodeField){
		if ((GvgUtils.isPermitedClassType(parentNodeField.getGvgType().getTypeSimple())) ||
				(GvgUtils.isPermitedClassType(parentNodeField.getGvgType().getTypeParameterized()))){
			StringSourceWriter src = new StringSourceWriter();
			String type = parentNodeField.getChild().getGvgType().getType();
			
			String valueOf = GvgUtils.stringToPrimitive(type, 
					GvgConstants.VALUE_VAR_FIELD_UPDATER);
			
			src.println("	%s.set%s(%s)",	GvgConstants.OBJECT_VAR_FIELD_UPDATER,
					parentNodeField.getChild().getGvgCellWidgetInfo().getNameField(),
					valueOf);
			
			return src.toString();
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createUpdater(parentNodeField);
		}
	}
	
	@Override
	public String createRowData(GvgGeneratorNodeField parentNodeField){

		if (GvgUtils.isPermitedClassType(parentNodeField.getGvgType().getTypeSimple())){
			StringSourceWriter src = new StringSourceWriter();
			src.println("	%s.get%s()",
					parentNodeField.getChild().getGvgCellWidgetInfo().getNameVar(),
					parentNodeField.getChild().getGvgCellWidgetInfo().getNameField());
			return src.toString();
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createRowData(parentNodeField);
		}
	}

	@Override
	public String createCellAndColumn(SourceWriter src, String nameClass, GvgGeneratorNodeField gvgNodeField){	
		
		if (GvgUtils.isPermitedClassType(gvgNodeField.getGvgType().getType())){
			//Get Vars
			GvgGeneratorNodeField nodeField = gvgNodeField.clone();
			GvgType type= nodeField.getGvgType();
			GvgCellWidgetInfo cellInfo= nodeField.getGvgCellWidgetInfo();
			//Generate code
			PermitedClassCellConstructor.createPermitedClassCellBegin(src, type.getTypeParameterized(),
					type.getTypeSimple(), cellInfo.getNameButton(), cellInfo.getNameCell(),	cellInfo.getNameVarLocal(),
					nodeField.getParent().getGvgCellWidgetInfo().getNameTable());
				PopupCreator.createPopup(src,GvgConstants.NAME_POPUP);
				String table = PermitedClassCellConstructor.createPermitedClassCellContent(GvgConstants.NAME_BUILDER_METHOD,
						type.getTypeSimple(), cellInfo.getNameVarLocal());
				PopupCreator.configurePopup(src,table);
			PermitedClassCellConstructor.createPermitedClassCellEnd(src);
			CellConstructor.createColumnBegin(src, nodeField.getParent().getGvgType().getTypeParameterized(),
					type.getType(),	cellInfo.getNameColumn(), cellInfo.getNameCell(), cellInfo.getNameVar());
			PermitedClassCellConstructor.createPermitedClassColumnContent(src, nodeField.getParent().clone(),
					type.getTypeSimple());
			CellConstructor.createColumnEnd(src);
			return src.toString();
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createCellAndColumn(src, nameClass, gvgNodeField);
		}
	}
	
	private static void createPermitedClassCellBegin(SourceWriter src, String typeParameterized, 
			String typeSimple, String nameButton, String nameCell, String nameVarLocal, String parentNameTable) {
		src.println("	final ActionCell<%s> %s = new ActionCell<%s>(",
				typeParameterized,nameCell,typeParameterized);
		src.println("      \"%s\", new ActionCell.Delegate<%s>() {",nameButton, typeParameterized);
		src.println("            public void execute(final %s %s) {",typeParameterized,nameVarLocal);
		src.println("if (%s == null){", nameVarLocal);
		ValidationCreator.createValidationNull(src, parentNameTable,
				"The Permited Class: "+typeSimple+" is null. Check it.");
		src.println("}else{");
	}

	private static String createPermitedClassCellContent( String nameBuilderMethod, 
			String typeSimple, String nameVar) {
		StringSourceWriter src = new StringSourceWriter();
		src.println("%s(%s.class, %s)", nameBuilderMethod,typeSimple, nameVar);
		return src.toString();
	}
	
	private static void createPermitedClassCellEnd(SourceWriter src) {
		//the end of if null
		src.println("            }");
		src.println("            }");
	    src.println("          });");
	}
	
	private static void createPermitedClassColumnContent(SourceWriter src,
			GvgGeneratorNodeField nodeFieldParent, String typeSimple) {
		String sChain = CellConstructor.createColumnContent(src, new CellConstructor(), nodeFieldParent);
		if (sChain != null){
			src.println("	return	(%s) %s;",typeSimple, sChain);
		}
	}
}
