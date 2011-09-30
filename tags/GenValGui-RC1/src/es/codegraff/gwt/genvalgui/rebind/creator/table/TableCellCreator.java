package es.codegraff.gwt.genvalgui.rebind.creator.table;

import com.google.gwt.user.rebind.SourceWriter;

public class TableCellCreator {

	public static void createTable(SourceWriter src, String type, String nameTable){
		src.println("final CellTable<%s> %s = new CellTable<%s>();",type,nameTable,type);		
	}

	public static void addInformation(SourceWriter src, String globalVar, String nameTable){
		src.println("%s.setRowData(0, %s);",nameTable, globalVar);
//        TableCellCreator.writeTableSize(src,nameTable,
//				"String.valueOf(RootPanel.get().getOffsetHeight())+\"px\"",
//				"String.valueOf(RootPanel.get().getOffsetWidth())+\"px\"");
	}

	public static void createAddColumnToTable(SourceWriter src, String nameTable,
			String nameColumn, String nameColumnTittle) {
		src.println("%s.addColumn(%s, \"%s\");",nameTable, nameColumn, nameColumnTittle);
	}
	
	@SuppressWarnings("unused")
	private static void writeTableSize(SourceWriter src, String nameTable,
			String height, String width) {
		src.println("%s.setTableLayoutFixed(true);",nameTable);
		src.println("%s.setHeight(%s);",nameTable, height);
		src.println("%s.setWidth(%s);",nameTable, width);
	}

	public static void addInformationConvertedToAl(SourceWriter src,
			String type, String nameVar, String nameTable) {
		src.println("if (%s != null){",nameVar);
		src.println("	ArrayList<%s> auxList = new ArrayList<%s>();",type,type);
        src.println("	auxList.add(%s);",nameVar);
        src.println("	%s.setRowData(0, auxList);",nameTable);
        src.println("}");
        
//        TableCellCreator.writeTableSize(src,nameTable,
//				"String.valueOf(RootPanel.get().getOffsetHeight())+\"px\"",
//				"String.valueOf(RootPanel.get().getOffsetWidth())+\"px\"");

		
	}
}
