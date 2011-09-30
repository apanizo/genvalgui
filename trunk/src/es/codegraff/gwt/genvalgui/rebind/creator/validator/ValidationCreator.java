package es.codegraff.gwt.genvalgui.rebind.creator.validator;

import com.google.gwt.user.rebind.SourceWriter;

import es.codegraff.gwt.genvalgui.client.auxiliar.GvgConstants;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgUtils;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeField;

public class ValidationCreator {

	public static void createValidationInException(SourceWriter src,
			GvgGeneratorNodeField nodeField, String nameTable) {
		ValidationCreator.cleanErrMsg(src);
		
		src.println("GvgInterfaceBuilder.emp.addErrorMsg(\"Exception error. Please check the change\");");
		src.println("if (e.getMessage() != null)");
		src.println("	GvgInterfaceBuilder.emp.addErrorMsg(\"\t\"+e.getMessage());");
		ValidationCreator.showErrMsg(src,nameTable, nodeField.getGvgCellWidgetInfo().getNameColumn());
		
		src.println("%s.redraw();",nameTable);
		src.println("return;");
		
	}

	public static void createValidationField(SourceWriter src,
			GvgGeneratorNodeField nodeField) {
		
		GvgGeneratorNodeField originalNodeField = nodeField;
		for(;;){
			if (originalNodeField.getParent() == null)
				break;
			originalNodeField = originalNodeField.getParent();
		}
		src.println("		Set<ConstraintViolation<%s>> violations =",originalNodeField.getGvgType().getTypeParameterized()); 
		src.println("		GvgInterfaceBuilder.validator.validateProperty(%s,\"%s\",%s);",
				GvgConstants.OBJECT_VAR_FIELD_UPDATER, nodeField.getGvgIdInfo().getField(),
				GvgUtils.getValidationGroupsString());
		
		ValidationCreator.createErrorMessageValidation(src, originalNodeField.getGvgType().getTypeParameterized(),
				nodeField.getParent().getGvgCellWidgetInfo().getNameTable(),
				nodeField.getGvgCellWidgetInfo().getNameColumn());
	}

	public static void createValidationGlobal(SourceWriter src, String parentType, String nameTable, String nameColumn) {
		
		src.println("		Set<ConstraintViolation<%s>> violations =",parentType); 
		src.println("			GvgInterfaceBuilder.validator.validate(%s,%s);",
				GvgConstants.OBJECT_VAR_FIELD_UPDATER, 	GvgUtils.getValidationGroupsString());
		
		ValidationCreator.createErrorMessageValidation(src, parentType, nameTable, nameColumn);
	}

	private static void createErrorMessageValidation(SourceWriter src, String parentType, String nameTable,
			String nameColumn) {
		ValidationCreator.cleanErrMsg(src);
		src.println("		if (!violations.isEmpty()) {");
		src.println("			for (ConstraintViolation<%s> constraintViolation : violations) {",parentType);
		src.println("				GvgInterfaceBuilder.emp.addErrorMsg(constraintViolation.getMessage());"); 
		src.println("			}");
		ValidationCreator.showErrMsg(src, nameTable, nameColumn);
		src.println("		}");
		src.println("		%s.redraw();",nameTable);
		
	}
	public static void createErrorMessageSimple(SourceWriter src, String message, String nameTable, String nameColumn){
		ValidationCreator.cleanErrMsg(src);
		src.println("		GvgInterfaceBuilder.emp.addErrorMsg(\"%s\");",message);
		ValidationCreator.showErrMsg(src, nameTable, nameColumn);
	}
	
	private static void cleanErrMsg(SourceWriter src) {
		src.println("if(GvgInterfaceBuilder.emp!=null){");
		src.println("	GvgInterfaceBuilder.emp.clearErrorMsgs();");
		src.println("}");
	}
	
	private static void showErrMsg(SourceWriter src, String nameTable,
			String nameColumn) {
		src.println("%s = %s.getColumnIndex(%s);",GvgConstants.COLUMN_INDEX,nameTable,nameColumn);
		src.println("GvgInterfaceBuilder.emp.showPanel((Element)%s.getRowElement(%s).getCells().getItem(%s).cast());",nameTable,
				GvgConstants.INDEX_VAR_FIELD_UPDATER, GvgConstants.COLUMN_INDEX);
		
	}

	public static void createValidationNull(SourceWriter src, String parentNameTable,
			String msg) {
		src.println("	HorizontalPanel popupContainer = new HorizontalPanel();");
		src.println("	popupContainer.setSpacing(5);");
		src.println("	popupContainer.add(new Image(\"/imgs/errorNull/err.png\"));");
		src.println("	final HTML pInfo = new HTML();");
		src.println("	popupContainer.add(pInfo);");
		src.println("	final PopupPanel p = new PopupPanel(true, false);");
		src.println("	p.setWidget(popupContainer);");
		src.println("	pInfo.setHTML(\"GenValGui App <br><i> %s </i>\");",msg);
		src.println("	int left = %s.getAbsoluteLeft() + 10;",parentNameTable);
		src.println("	int top = %s.getAbsoluteTop()- 10;",parentNameTable);
		src.println("	p.setPopupPosition(left, top);");
		src.println("	p.show();");
		
	}
}
