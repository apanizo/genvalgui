package es.codegraff.gwt.genvalgui.rebind.creator.cell;

import java.util.ArrayList;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.SourceWriter;

import es.codegraff.gwt.genvalgui.client.auxiliar.GvgConstants;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgUtils;
import es.codegraff.gwt.genvalgui.rebind.creator.validator.ValidationCreator;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeClass;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeField;

public class CellConstructor {
	//Working for the chain of responsability
	private static ArrayList<CellConstructor> alChain; 	
	//Control
	private static int position = 0;
		
	public CellConstructor(){
		CellConstructor.position=0;
	}
	
	
	
	
	public static void checkConstructors() {
		if(CellConstructor.alChain == null)
			GvgUtils.logger.log(TreeLogger.ERROR, "[CellConstructor] The array of" +
					" constructor can not be null");
		if(CellConstructor.alChain.size()==0)
			GvgUtils.logger.log(TreeLogger.ERROR, "[CellConstructor] The size of the array " +
					"cant not be 0");
	}

	public static void addCustomCreatorsCells() {
		StringCellConstructor auxString = new StringCellConstructor();
		EnumCellConstructor auxEnum = new EnumCellConstructor();
		PermitedClassCellConstructor auxPermitedClass =	new PermitedClassCellConstructor();
		PrimitiveTypeCellConstructor auxPrimitive = new PrimitiveTypeCellConstructor();
		JavaUtilCellConstructor auxJavaUtil = new JavaUtilCellConstructor();
		
		CellConstructor.alChain = new ArrayList<CellConstructor>();
		CellConstructor.alChain.add(auxString);
		CellConstructor.alChain.add(auxPrimitive);
		CellConstructor.alChain.add(auxEnum);
		CellConstructor.alChain.add(auxPermitedClass);
		CellConstructor.alChain.add(auxJavaUtil);
	}

	
	
	// Methods for the Chains of responsabilities
	public static void reset(){
		CellConstructor.position=0;
	}

	protected static CellConstructor next(){
		if (CellConstructor.position == (CellConstructor.alChain.size())){
			CellConstructor.reset();
			return null;
		}
		CellConstructor aux = CellConstructor.alChain.get(CellConstructor.position);
		CellConstructor.position++;
		return aux;
	}
	
	public int getPosition() {
		return position;
	}

	public ArrayList<CellConstructor> getAlChain() {
		return alChain;
	}

	/**
	 * 
	 * @param gvgNodeField 
	 * @return
	 */
	public String createCellAndColumn(SourceWriter src, String nameClass,
			GvgGeneratorNodeField gvgNodeField) {
		CellConstructor aux = CellConstructor.next();
		return (aux==null) ? null : aux.createCellAndColumn(src, nameClass, gvgNodeField);
	}
	
	/**
	 * 
	 * @param nodeField with the parent rol {@link GvgConstants.Roles.ParentType}.
	 * @return the code for set the new value to the field.
	 */
	public String createUpdater(GvgGeneratorNodeField parentNodeField){
		CellConstructor aux = CellConstructor.next();
		return (aux==null) ? null : aux.createUpdater(parentNodeField);
	}	
	
	/**
	 * For this version, this Chain of Responsability is unnecesary, because
	 * we´ll to validate the global object property and show the errors in
	 * the column you are seeing at the time.
	 * 
	 * @param nodeField with the parent rol {@link GvgConstants.Roles.ParentType}.
	 * @return
	 */
	public String createValidateField(GvgGeneratorNodeField parentNodeField) {
		CellConstructor aux = CellConstructor.next();
		return (aux==null) ? null : aux.createUpdater(parentNodeField);
	}
	
	/**
	 * 
	 * @param nodeField with the parent rol {@link GvgConstants.Roles.ParentType}.
	 * @return
	 */
	public String createRowData(GvgGeneratorNodeField parentNodeField){
		CellConstructor aux = CellConstructor.next();
		return (aux==null) ? null : aux.createRowData(parentNodeField);
	}
	//--------------------------------------------------------------

	//General methods for generate code.
	/**
	 * @param nodeField with the normal rol {@link GvgConstants.Roles.Type}.
	 */
	public void generateCellCode(SourceWriter src,
			CellConstructor cellConstructor, String nameClass, GvgGeneratorNodeField nodeField) {
		//Local vars
		String type = nodeField.getGvgType().getType();
		String field = nodeField.getGvgIdInfo().getId();

		//Chain of Responsability
		String sChain = cellConstructor.createCellAndColumn(src, nameClass, nodeField);
		CellConstructor.reset();
		if (sChain == null){
			GvgUtils.logger.log(TreeLogger.ERROR,"[GwtGenValGuiGenerator] Not " +
					"found a custom creator column for the field: ["+
					type+" "+field+"] in class: "+nameClass, new UnableToCompleteException());
			src.println("//Not found a custom creator column for the type %s",type);
		}
	}

	/*
	 * Independent of the type of CellConstructor there are some parts of the
	 * code that are similar.
	 */
		
	//The columns is similar for all types.
	public static void createColumnBegin(SourceWriter src, String typeParent,
			String type, String nameColumn, String nameCell, String nameVar) {
		src.println("	final Column<%s,%s> %s = new Column<%s, %s>(%s) {",
	    		typeParent, type, nameColumn,typeParent,type,nameCell);
	    src.println("	  @Override");
	    src.println("	  public %s getValue(%s %s) {",type, typeParent, nameVar);
	    
	}
	
	public static String createColumnContent(SourceWriter src, CellConstructor cellConstructor,
			GvgGeneratorNodeField parentNodeField){
		String sChain = cellConstructor.createRowData(parentNodeField);
		CellConstructor.reset();
		if (sChain == null){
			GvgUtils.logger.log(TreeLogger.ERROR,"Not found a custom row data selection for the parent field: "
				+parentNodeField.getGvgType().getTypeParameterized() +" "+parentNodeField.getGvgIdInfo().getPath());					
			src.println("//Not found a custom creator column for the type %s",
					parentNodeField.getGvgType().getTypeParameterized());
		}
		return sChain;
	}
	public static void createColumnEnd(SourceWriter src){
		src.println("     }");
	    src.println("	};");
	}
	
	public static void createFieldUpdaterBegin(SourceWriter src, String nameColumn, String type) {
		src.println("%s.setFieldUpdater(new FieldUpdater<%s, String>() {",nameColumn,type);
		src.println("	@Override");
		src.println("	public void update(int index, %s %s, String %s) {",type,GvgConstants.OBJECT_VAR_FIELD_UPDATER,
				GvgConstants.VALUE_VAR_FIELD_UPDATER);
		src.println("	int %s;",GvgConstants.COLUMN_INDEX);
	}
	
	public static void createFieldUpdaterContent(SourceWriter src, GvgGeneratorNodeField nodeField) {
		src.println("try{");
		CellConstructor cellConstructor = new CellConstructor();
		String sChainRow = cellConstructor.createUpdater(nodeField.getParent().clone());
		CellConstructor.reset();
		if (sChainRow == null){
			GvgUtils.logger.log(TreeLogger.INFO,"	Not found a custom row data selection " +
					"for the field: "+nodeField.getGvgIdInfo().getPath());
			src.println("//Not found a custom creator column for the type %s", 
					nodeField.getGvgIdInfo().getPath());
		}else{
			src.println("	%s;",sChainRow);
		}
		src.println("}catch(Exception e){");
		ValidationCreator.createValidationInException(src, nodeField, 
				nodeField.getParent().getGvgCellWidgetInfo().getNameTable());
		src.println("}");
		if (!(GvgUtils.isJavaUtilType(nodeField.getParent().getGvgType().getTypeSimple())))
			ValidationCreator.createValidationField(src,nodeField);
		else{
			ValidationCreator.createErrorMessageSimple(src,
					"Please go to the initial table and press Validate",
					nodeField.getParent().getGvgCellWidgetInfo().getNameTable(),
					nodeField.getGvgCellWidgetInfo().getNameColumn());
		}
	}

	public static void createFieldUpdaterEnd(SourceWriter src) {
		src.println("     }");
		src.println("});");
	}
	
	/**
	 * Only for the first call, to make global validation
	 * @param src
	 * @param nodeClass
	 */
	public static void createColumnValidation(SourceWriter src, GvgGeneratorNodeClass nodeClass) {
		String nameCell = "buttonValidationCell";
		String nameColumn = "buttonValidationColumn";
		String parentType = nodeClass.getNameClass();
		src.println("final ButtonCell %s = new ButtonCell();",nameCell);
	    
		src.println("final Column<%s, String> %s = new Column<%s, String>(%s) {",
	    		parentType, nameColumn, parentType, nameCell);
	    src.println("  @Override");
	    src.println("  public String getValue(%s object) {", parentType);
	    src.println("    return \"Validate\";");
	    src.println("  }");
	    src.println("};");
	    src.println("%s.addColumn(%s, \"C-Validation\");",nodeClass.getNameTable(), nameColumn);
	    CellConstructor.createFieldUpdaterBegin(src, nameColumn, parentType);
		ValidationCreator.createValidationGlobal(src, parentType, nodeClass.getNameTable(), nameColumn);
		CellConstructor.createFieldUpdaterEnd(src);
	}
	
}
