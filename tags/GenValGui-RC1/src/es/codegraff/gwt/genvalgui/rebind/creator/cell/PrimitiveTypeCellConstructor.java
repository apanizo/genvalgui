package es.codegraff.gwt.genvalgui.rebind.creator.cell;

import com.google.gwt.user.rebind.SourceWriter;

import es.codegraff.gwt.genvalgui.client.auxiliar.GvgConstants;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgUtils;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeField;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgCellWidgetInfo;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgIdInfo;

public class PrimitiveTypeCellConstructor extends CellConstructor {

	public PrimitiveTypeCellConstructor(){
		
	}
	
	@Override
	public String createUpdater(GvgGeneratorNodeField parentNodeField){
		if ((GvgUtils.isPrimitiveType(parentNodeField.getGvgType().getTypeSimple())) || 
				(GvgUtils.isPrimitiveType(parentNodeField.getGvgType().getType()))){
			//Impossible that in this case. Leave the code for future versions.
			return "";
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createUpdater(parentNodeField);
		}
	}
	
	@Override
	public String createRowData(GvgGeneratorNodeField parentNodeField){
		if ((GvgUtils.isPrimitiveType(parentNodeField.getGvgType().getTypeSimple())) || 
				(GvgUtils.isPrimitiveType(parentNodeField.getGvgType().getType()))){
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
		if (GvgUtils.isPrimitiveType(gvgNodeField.getGvgType().getType())){
			//Get Vars
			GvgGeneratorNodeField nodeField = gvgNodeField.clone();
			GvgCellWidgetInfo cellInfo= nodeField.getGvgCellWidgetInfo();
			GvgIdInfo idInfo = nodeField.getGvgIdInfo();
			//Generate code
			PrimitiveTypeCellConstructor.createPrimitiveTypeCell(src, nodeField.getGvgType().getTypeSimple(),
					idInfo, cellInfo.getNameCell());
			CellConstructor.createColumnBegin(src, nodeField.getParent().getGvgType().getTypeParameterized(),
					"String",	cellInfo.getNameColumn(), cellInfo.getNameCell(), cellInfo.getNameVar());
				PrimitiveTypeCellConstructor.createPrimitiveColumnContent(src, nodeField.getParent().clone());
            CellConstructor.createColumnEnd(src);
            PrimitiveTypeCellConstructor.createFieldUpdater(src, cellInfo, nodeField);
		   
			return src.toString();
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createCellAndColumn(src, nameClass, gvgNodeField);
		}
	}

	private static void createFieldUpdater(SourceWriter src,
			GvgCellWidgetInfo cellInfo, GvgGeneratorNodeField nodeField) {
		if (!(nodeField.getGvgIdInfo().isFinal())){
			CellConstructor.createFieldUpdaterBegin(src,cellInfo.getNameColumn(),
					nodeField.getParent().getGvgType().getTypeParameterized());
			CellConstructor.createFieldUpdaterContent(src,nodeField);
			CellConstructor.createFieldUpdaterEnd(src);
		}
	}

	private static void createPrimitiveColumnContent(SourceWriter src, GvgGeneratorNodeField nodeFieldParent) {
		src.println("if (%s == null)",nodeFieldParent.getChild().getGvgCellWidgetInfo().getNameVar());
		src.println("	return \"\"; ");
		src.println("else{");
		String sChain = CellConstructor.createColumnContent(src, new CellConstructor(), nodeFieldParent);
		if (sChain != null){
			src.println("	if(String.valueOf(%s).equals(\"null\"))",sChain);
			src.println("		return \"\";");
			src.println("	else");
			src.println("		return String.valueOf(%s);",sChain);
		}
		src.println("}");
	}

	private static void createPrimitiveTypeCell(SourceWriter src, String typeSimple, GvgIdInfo idInfo, String nameCell) {
		if (idInfo.isFinal()){
			src.println("	final TextCell %s = new TextCell();",nameCell);
		}else{
			if (GvgConstants.BOOLEAN.equals(typeSimple)){
				src.println("	ArrayList<String> %sBooleanList = new ArrayList<String>();",idInfo.getId());
				src.println("	%BooleanList.add(\"true\");",idInfo.getId());
				src.println("	%BooleanList.add(\"false\");",idInfo.getId());
				src.println("	final SelectionCell %s = new SelectionCell(%sBooleanList);",nameCell,idInfo.getId());
				return;
			}
			src.println("	final TextInputCell %s = new TextInputCell();",nameCell);
		}
	}

}


