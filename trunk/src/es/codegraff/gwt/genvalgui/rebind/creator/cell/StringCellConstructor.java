package es.codegraff.gwt.genvalgui.rebind.creator.cell;

import com.google.gwt.user.rebind.SourceWriter;

import es.codegraff.gwt.genvalgui.client.auxiliar.GvgUtils;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeField;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgCellWidgetInfo;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgType;

public class StringCellConstructor extends CellConstructor {
	
	public StringCellConstructor(){
		
	}
	
	@Override
	public String createUpdater(GvgGeneratorNodeField parentNodeField){
		if ((GvgUtils.isStringType(parentNodeField.getGvgType().getTypeSimple())) ||
				(GvgUtils.isStringType(parentNodeField.getGvgType().getType()))){
			//Impossible that in this case. Leave the code for future versions.
			return "";
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createUpdater(parentNodeField);
		}
	}

	@Override
	public String createRowData(GvgGeneratorNodeField parentNodeField){
		if ((GvgUtils.isStringType(parentNodeField.getGvgType().getTypeSimple())) ||
				(GvgUtils.isStringType(parentNodeField.getGvgType().getType()))){
			//Impossible that in this case. Leave the code for future versions.
			return "";
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createRowData(parentNodeField);
		}
	}
	
	@Override
	public String createCellAndColumn(SourceWriter src, String nameClass,
			GvgGeneratorNodeField gvgNodeField) {
		if ((GvgUtils.isStringType(gvgNodeField.getGvgType().getTypeSimple()))||
				(GvgUtils.isStringType(gvgNodeField.getGvgType().getType()))){
			//Get Vars
			GvgGeneratorNodeField nodeField = gvgNodeField.clone();
			GvgType type= nodeField.getGvgType();
			GvgCellWidgetInfo cellInfo= nodeField.getGvgCellWidgetInfo();
			//Generate code
			StringCellConstructor.createStringCell(src, cellInfo.getNameCell());
			CellConstructor.createColumnBegin(src, nodeField.getParent().getGvgType().getTypeParameterized(),
					type.getType(),	cellInfo.getNameColumn(), cellInfo.getNameCell(), cellInfo.getNameVar());
				StringCellConstructor.createStringColumnContent(src, nodeField.getParent().clone());
            CellConstructor.createColumnEnd(src);
            CellConstructor.createFieldUpdaterBegin(src,cellInfo.getNameColumn(), nodeField.getParent().getGvgType().getTypeParameterized());
		    CellConstructor.createFieldUpdaterContent(src,nodeField);
		    CellConstructor.createFieldUpdaterEnd(src);
			return src.toString();
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createCellAndColumn(src, nameClass, gvgNodeField);
		}
	}
	
	

	private static void createStringCell(SourceWriter src, String nameCell) {
		src.println("final TextInputCell %s = new TextInputCell();", nameCell);
	}
	
	private static void createStringColumnContent(SourceWriter src, GvgGeneratorNodeField nodeFieldParent) {
		src.println("if (%s == null)",nodeFieldParent.getChild().getGvgCellWidgetInfo().getNameVar());
		src.println("	return \"\"; ");
		String sChain = CellConstructor.createColumnContent(src, new CellConstructor(), nodeFieldParent);
		if (sChain != null){
			src.println("	return %s;",sChain);
		}
		
	}
	
}
