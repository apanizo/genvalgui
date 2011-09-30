package es.codegraff.gwt.genvalgui.rebind.generator;

import java.util.ArrayList;

import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.TypeOracle;

import es.codegraff.gwt.genvalgui.client.GvgInfoAnnotation;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgConstants;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgUtils;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgCellWidgetInfo;

/**
 * The class for save in a tree the information about the classes that are passed.
 * See the example: 
 * 	@GvgInfoAnnotation(value={Person.class,Adress.class,Email.class})
 *		public interface GwtGenValGuiBuilder extends GvgInterfaceBuilder{
 *		}
 * The explanation is logic. I need information about the fields for generate the
 * CellTable. 
 * @author apanizo
 *
 */
public class GvgGeneratorNode {
	private ArrayList<GvgGeneratorNodeClass> nodeClasses = new ArrayList<GvgGeneratorNodeClass>();
	private static ArrayList<String> enumClass = new ArrayList<String>();	
	
	private TreeLogger logger;
	private TypeOracle typeOracle;
	private GvgInfoAnnotation gvgAnnotation;
	
	public GvgGeneratorNode(){
		
	}
	
	public GvgGeneratorNode(TreeLogger logger, TypeOracle typeOracle, GvgInfoAnnotation gvgAnnotation){
		this.logger=logger;
		this.typeOracle=typeOracle;
		this.gvgAnnotation = gvgAnnotation;
	}
	
	public ArrayList<String> getEnumClass() {
		return GvgGeneratorNode.enumClass;
	}

	public ArrayList<GvgGeneratorNodeClass> getNodeClasses() {
		return this.nodeClasses;
	}
	
	/**
	 * This method only is called here and in the generating method of
	 * parameterized types (i.e ArrayList<MyEnum> myEnum; )
	 * @param type
	 */
	protected static void addEnumType(String type) {
		GvgGeneratorNode.enumClass.add(type);
		
	}

	public void createTreeNode() throws UnableToCompleteException{
		for (Class<?> gvgClass : this.gvgAnnotation.value()){
			GvgGeneratorNodeClass nodeClass = new GvgGeneratorNodeClass();
			nodeClass.setNameClassSimple(gvgClass.getSimpleName());
			nodeClass.setNameClass(gvgClass.getName());
			nodeClass.setNameTable(GvgCellWidgetInfo.createNameTable(nodeClass.getNameClassSimple()));
			try{
				JClassType type = typeOracle.findType(gvgClass.getName());
				ArrayList<GvgGeneratorNodeField> nodeFields = 
					GvgGeneratorNodeClass.createNodeFields(type);
				
				nodeClass.setFields(nodeFields);
				this.nodeClasses.add(nodeClass);
			}catch(Exception e){
				this.logger.log(TreeLogger.ERROR, e.getMessage(), new UnableToCompleteException());
			}
		}
	}

	@Override
	public String toString(){
		String s="";
		for(GvgGeneratorNodeClass node : this.nodeClasses){
			s=s+"Class: "+node.getNameClass()+"\n";
			s=s+node.toString()+"\n\n";
		}
		return s;
	}
	
	/**
	 * Important method because all the future decissions about the valid
	 * or invalid characteristics of fields (like static, arrays (int[][]),...)
	 * will be checked here.
	 * 
	 * <Restrictions>
	 * 		For now we can work with PrimitiveTypes, EnumTypes, PermitedClassTypes 
	 * 			and	StringTypes, ArrayList and HashMap. 
	 * 		There is not any checks about the characteristics of fields (static 
	 * 			final transient...)
	 * 		The array fields are not treated.
	 * 		There is one restriction in HashMap type. The K must be a primitive Type.
	 * </Restrictions>
	 * 
	 *  @author apanizo
	 *  
	 *  @param field that will be checked.
	 */
	protected static void checkIsValidField(JField field) {
		String type = field.getType().getQualifiedSourceName();
		if (GvgUtils.isPrimitiveType(type)){
			return;
		}
		if (GvgUtils.isStringType(type)){
			return;
		}
		if (GvgUtils.isPermitedClassType(type)){
			return;
		}
		if (field.getType().isEnum() != null){
			GvgGeneratorNode.addEnumType(type);
			return;
		}
		if (GvgUtils.isJavaUtilType(type)){
			if((GvgConstants.HASHMAP.equals(type))||(GvgConstants.HASHMAP_SIMPLE.equals(type))){
				if(GvgUtils.isJavaUtilType(field.getType().isParameterized().getTypeArgs()[0].getSimpleSourceName())){
					GvgUtils.logger.log(TreeLogger.ERROR, "Error while parsing the GenValGui generator. The " +
								"key of HashMap mustn´t  be a JavaUtilClass", new UnableToCompleteException());
				}
				if(GvgUtils.isPermitedClassType(field.getType().isParameterized().getTypeArgs()[0].getSimpleSourceName())){
					GvgUtils.logger.log(TreeLogger.ERROR, "Error while parsing the GenValGui generator. The " +
							"key of HashMap mustn´t  be a Permited Class", new UnableToCompleteException());
				}
				if(field.getType().isEnum() != null){
					GvgUtils.logger.log(TreeLogger.ERROR, "Error while parsing the GenValGui generator. The " +
							"key of HashMap mustn´t  be a Enum Type", new UnableToCompleteException());
				}
			}
			return;
		}
		GvgUtils.logger.log(TreeLogger.ERROR, "[GvgGeneratorNode - checkField]Error while parsing the GenValGui " +
				"generator. The field "+field.getName()+" has a unknow type: "+type, new UnableToCompleteException());
		
	}

	protected static void checkIsValidParameterizedField(JClassType parameterizedField,int position, int size){
		String fieldSourceName = parameterizedField.getQualifiedSourceName();
		if(size>2){
			GvgUtils.logger.log(TreeLogger.ERROR,"[chekParameterizedField]Error while " +
					"parsing the GenValGui generator. There is no support for parameterizeds types with" +
					"three or more types T,U V.", new UnableToCompleteException());
		}
		if (GvgConstants.HASHMAP.equals(parameterizedField.getQualifiedSourceName())){
			if(position==0){
				GvgUtils.logger.log(TreeLogger.ERROR,"[chekParameterizedField]Error while parsing " +
						"the GenValGui generator. The key of HashMap must be a Primitive Type", 
						new UnableToCompleteException());
			}
		}
		if(!((GvgUtils.isPrimitiveType(fieldSourceName))||(parameterizedField.isEnum()!= null)||
				(GvgUtils.isStringType(fieldSourceName))||(GvgUtils.isPermitedClassType(fieldSourceName))||
				(GvgUtils.isJavaUtilType((fieldSourceName))))){
			GvgUtils.logger.log(TreeLogger.ERROR, "[GvgGeneratorNode - checkField]Error while parsing the GenValGui " +
					"generator. The parameterized type has a unknow type: "+fieldSourceName,
					new UnableToCompleteException());
		}
	}
	
}
