package es.codegraff.gwt.genvalgui.rebind.generator.auxiliar;

public class GvgType {
	String type;
	String typeSimple;
	String typeParameterized;
	
	public GvgType(String type, String simpleType, String parameterizedType){
		this.type=type;
		this.typeSimple=simpleType;
		this.typeParameterized = parameterizedType;
	}

	//getters and setters
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeSimple() {
		return typeSimple;
	}

	public void setTypeSimple(String typeSimple) {
		this.typeSimple = typeSimple;
	}

	public String getTypeParameterized() {
		return typeParameterized;
	}

	public void setTypeParameterized(String typeParameterized) {
		this.typeParameterized = typeParameterized;
	}
	@Override
	public GvgType clone(){
		return new GvgType(this.type, this.typeSimple, this.typeParameterized);
	}
	
	@Override
	public String toString(){
		String s="";
		s=s+"\t\t\ttypeSimple: "+this.typeSimple+"\n";
		s=s+"\t\t\ttype: "+this.type+"\n";
		s=s+"\t\t\ttypeParameterized: "+this.typeParameterized+"\n";
		return s;
	}
	
	
}
