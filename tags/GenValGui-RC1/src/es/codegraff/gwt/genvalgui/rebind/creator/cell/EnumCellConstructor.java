package es.codegraff.gwt.genvalgui.rebind.creator.cell;

import com.google.gwt.user.rebind.SourceWriter;

import es.codegraff.gwt.genvalgui.client.auxiliar.GvgUtils;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeField;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgCellWidgetInfo;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgIdInfo;

public class EnumCellConstructor extends CellConstructor {

	public EnumCellConstructor(){
		
	}

	@Override
	public String createUpdater(GvgGeneratorNodeField parentNodeField){
		if ((GvgUtils.isEnumType(parentNodeField.getGvgType().getTypeParameterized()))
				|| (GvgUtils.isEnumType(parentNodeField.getGvgType().getType()))){
			//Impossible that in this case. Leave the code for future versions.
			return "";
		}else{
			CellConstructor aux = CellConstructor.next();
			return (aux==null) ? null : aux.createUpdater(parentNodeField);
		}
	}
	
	@Override
	public String createRowData(GvgGeneratorNodeField parentNodeField){
		if ((GvgUtils.isEnumType(parentNodeField.getGvgType().getTypeSimple())) || 
				(GvgUtils.isEnumType(parentNodeField.getGvgType().getType()))){
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
		if (GvgUtils.isEnumType(gvgNodeField.getGvgType().getType())){
			//Get Vars
			GvgGeneratorNodeField nodeField = gvgNodeField.clone();
			GvgCellWidgetInfo cellInfo= nodeField.getGvgCellWidgetInfo();
			GvgIdInfo idInfo = nodeField.getGvgIdInfo();
			//Generate code
			EnumCellConstructor.createEnumCell(src, nodeField.getGvgType().getType(),
					idInfo.getId(), cellInfo.getNameCell());
			
			CellConstructor.createColumnBegin(src, nodeField.getParent().getGvgType().getTypeParameterized(),
					"String",	cellInfo.getNameColumn(), cellInfo.getNameCell(), cellInfo.getNameVar());
				EnumCellConstructor.createEnumColumnContent(src, nodeField.getParent().clone());
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
	
	private static void createEnumColumnContent(SourceWriter src, GvgGeneratorNodeField nodeFieldParent) {
		src.println("if (%s == null)",nodeFieldParent.getChild().getGvgCellWidgetInfo().getNameVar());
	    src.println("	return null;");
		String sChain = CellConstructor.createColumnContent(src, 
				new CellConstructor(), nodeFieldParent);
		if (sChain != null){
			src.println("		return ((%s == null) ? null : %s.name());",sChain,sChain);
		}
	}

	private static void createEnumCell(SourceWriter src, String type, 
			String id, String nameCell) {
		src.println("	ArrayList<String> %sList = new ArrayList<String>();",id);
		src.println("	%sList.add(\"Select\");",id);
		src.println("	for (%s auxEnum: %s.values()){",type,type);
		src.println("		%sList.add(auxEnum.name());",id);
		src.println("	}");
		src.println("	final SelectionCell %s = new SelectionCell(%sList);",nameCell,id);
	}

}
