package es.codegraff.gwt.genvalgui.rebind.generator;

import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;

import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgCellWidgetInfo;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgIdInfo;
import es.codegraff.gwt.genvalgui.rebind.generator.auxiliar.GvgType;


/**
 * This class represents the info of the fields. For example:
 * 	class Person{
 * 		String name;
 * 		Integer age;
 *	}
 *
 *	This class represents the information of name and age. And it saved
 *	in an ArrayList into the GvgGeneratorNodeClass person.
 *
 * @author apanizo
 *
 */
public class GvgGeneratorNodeField extends GvgGeneratorNode{
	
	private GvgGeneratorNodeField parent;
	private GvgGeneratorNodeField child;
	private GvgType gvgType;
	private GvgCellWidgetInfo gvgCellWidgetInfo;
	private GvgIdInfo gvgIdInfo;
	
	GvgGeneratorNodeField(){
		
	}
	
	GvgGeneratorNodeField(GvgType gvgType,GvgCellWidgetInfo gvgCellWidgetInfo, 
			GvgIdInfo gvgIdInfo) {
		super();
		this.gvgType = gvgType;
		this.gvgCellWidgetInfo = gvgCellWidgetInfo;
		this.gvgIdInfo = gvgIdInfo;
	}

	//Methods
	
	public GvgGeneratorNodeField getParent(){
		return this.parent;
	}
	
	public GvgGeneratorNodeField getChild(){
		return this.child;
	}
		
	//getters y setters
	
	public GvgType getGvgType() {
		return gvgType;
	}
	public void setParent(GvgGeneratorNodeField parent) {
		this.parent = parent;
	}

	public void setChild(GvgGeneratorNodeField child) {
		this.child = child;
	}

	public GvgCellWidgetInfo getGvgCellWidgetInfo() {
		return gvgCellWidgetInfo;
	}
	public GvgIdInfo getGvgIdInfo() {
		return gvgIdInfo;
	}
	public void setGvgType(GvgType gvgType) {
		this.gvgType = gvgType;
	}
	public void setGvgCellWidgetInfo(GvgCellWidgetInfo gvgCellWidgetInfo) {
		this.gvgCellWidgetInfo = gvgCellWidgetInfo;
	}
	public void setGvgIdInfo(GvgIdInfo gvgIdInfo) {
		this.gvgIdInfo = gvgIdInfo;
	}

	//*******************************************
	/**
	 * This method creates all the family of a field:
	 * 	class Person{
	 * 		ArrayList<ArrayList<ArrayList<String> foo;
	 * 	}
	 * The family of this field is:
	 * 		Person - ArrayListFoo - ArrayListFoo2 - ArrayListFoo3 - String
	 * 	The tail of this family is made by the createFamilyNodeField.
	 * 	{@link GvgGeneratorNodeField.createNodeFieldFamily(...)}
	 */
	public static GvgGeneratorNodeField createNodeField(GvgGeneratorNodeField nodeOrigenClass, JField field){
		//GvgIdInfo
		GvgIdInfo gvgIdInfo = new GvgIdInfo(nodeOrigenClass.getGvgIdInfo().getPath()+"-"+field.getName(),
				field.getName(), field.getName(), field.isFinal(), field.isStatic());
		//CellWidgetInfo
		GvgCellWidgetInfo gvgCellWidgetInfo = new GvgCellWidgetInfo(gvgIdInfo);
		
		gvgCellWidgetInfo.setNameColumnTittle(GvgCellWidgetInfo.createNameColumnTittle(field));
		gvgCellWidgetInfo.setNameButton(GvgCellWidgetInfo.createNameButton(field));
		//GvgType
		GvgType gvgType = 
			new GvgType(field.getType().getQualifiedSourceName(),field.getType().getSimpleSourceName(),
					field.getType().getParameterizedQualifiedSourceName());
		//NodeField
		GvgGeneratorNodeField nodeField = new GvgGeneratorNodeField(gvgType, 
				gvgCellWidgetInfo, gvgIdInfo);
		//Parent and Childs
		nodeField.setParent(nodeOrigenClass);
		GvgGeneratorNodeField.createNodeFieldFamily(1,nodeField,field.getType().isParameterized());
			
		return nodeField; 
	}
	
	/**
	 * This method save the family of composed fields. For example if we have:
	 * 	class Person{
	 * 		ArrayList<ArrayList<ArrayList<String> foo;
	 * 	}
	 * The family of this field is:
	 * 		Person - ArrayListFoo - ArrayListFoo2 - ArrayListFoo3 - String
	 * This methdos makes the next nodes:
	 *  	ArrayListFoo2 - ArrayListFoo3 - String
	 *   
	 * @param position
	 * @param nodeParentField
	 * @param parameterized
	 */
	private static void createNodeFieldFamily(int position,
			GvgGeneratorNodeField nodeParentField, JParameterizedType parameterized) {
		
		if(parameterized == null){
			nodeParentField.setChild(null);
			nodeParentField.getGvgCellWidgetInfo().setNameTable(null);
			//nodeParentField.getGvgCellWidgetInfo().setNameVarArray(null);
		}else{
			JClassType[] types = parameterized.getTypeArgs();
			if (types.length>1)
				GvgGeneratorNode.checkIsValidParameterizedField(types[0],0,types.length);
			JClassType pFinal = types[types.length-1];
			GvgGeneratorNode.checkIsValidParameterizedField(pFinal,types.length-1,types.length);
			
			if(pFinal.isEnum() != null)
			GvgGeneratorNode.addEnumType(pFinal.getQualifiedSourceName());
			
			GvgGeneratorNodeField child = new GvgGeneratorNodeField(
					new GvgType(pFinal.getQualifiedSourceName(),pFinal.getSimpleSourceName(),
							pFinal.getParameterizedQualifiedSourceName()), 
					nodeParentField.getGvgCellWidgetInfo().generateParameterizedInfo(position), 
					nodeParentField.getGvgIdInfo().generateParameterizedInfo(position));
			nodeParentField.setChild(child);
			child.setParent(nodeParentField);
			
			GvgGeneratorNodeField.createNodeFieldFamily(++position,
					child, pFinal.isParameterized());
		}
	}

	//++++++++++++++++++++++++++++++++++++++++++++
	public GvgGeneratorNodeField clone(){
		GvgGeneratorNodeField cloneField = this.cloneBody();
		this.cloneParents(cloneField);
		this.cloneChilds(cloneField);
		return cloneField;
	}
	private GvgGeneratorNodeField cloneBody() {
		return new GvgGeneratorNodeField(
				this.getGvgType().clone(), 
	    		this.getGvgCellWidgetInfo().clone(),
	    		this.getGvgIdInfo().clone());
	}

	private void cloneChilds(GvgGeneratorNodeField cloneField) {
		GvgGeneratorNodeField auxiliar = cloneField;
		GvgGeneratorNodeField child = this.getChild();
		GvgGeneratorNodeField cloneChild;
		for(;;){
			if(child != null){
				cloneChild = child.cloneBody();
				auxiliar.setChild(cloneChild);
				cloneChild.setParent(auxiliar);
			}else{
				auxiliar.setChild(null);
				break;
			}
			auxiliar=cloneChild;
			child=child.getChild();
		}
	}

	private void cloneParents(GvgGeneratorNodeField cloneField) {
		GvgGeneratorNodeField auxiliar = cloneField;
		GvgGeneratorNodeField parent = this.getParent();
		GvgGeneratorNodeField cloneParent;
		for(;;){
			if(parent != null){
				cloneParent = parent.cloneBody();
				auxiliar.setParent(cloneParent);
				cloneParent.setChild(auxiliar);
			}else{
				auxiliar.setParent(null);
				break;
			}
			auxiliar=cloneParent;
			parent=parent.getParent();
		}
	}

		
	@Override
	public String toString(){
		String s="";
		s=s+"\t\tGvgIdInfo: \n"+this.gvgIdInfo.toString();
		s=s+"\t\tGvgCellWidgetInfo: \n"+this.gvgCellWidgetInfo.toString();
		s=s+"\t\tGvgType: \n"+this.gvgType.toString();
		s=s+"\t\t\tParameterized Fields: \n";
		GvgGeneratorNodeField parent = this.getParent(); 
		for(;;){
			if(parent==null){
				s=s+"\t\t\t\tparent: null";
				break;
			}else{
				s=s+"\t\t\t\tparent: "+parent.getGvgIdInfo().getPath();
			}
			parent= parent.getParent();
		}
		GvgGeneratorNodeField child = this.getChild();
		for(;;){
			if(child==null){
				s=s+"\t\t\t\tchild: null";
				break;
			}else{
				s=s+"\t\t\t\tchild: "+child.getGvgIdInfo().getPath();
			}
			child = child.getChild();
		}
		return s;
	}

}
