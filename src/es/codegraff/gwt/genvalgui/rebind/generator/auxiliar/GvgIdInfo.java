package es.codegraff.gwt.genvalgui.rebind.generator.auxiliar;

/**
 * This class allows a unique id for each field. The algorithm
 * for create the id is simple.
 * For example: person-name-1 
 * If a field has a number in the id it means it has parameterizeds
 * types.
 * 
 * {@link GvgGeneratorNodeField //1:1}
 * @author apanizo
 *
 */
public class GvgIdInfo {
	
	private String path;
	private String id;
	private String field;
	private boolean isFinal;
	private boolean isStatic;
	
	
	public GvgIdInfo(String path, String id, String field, boolean isFinal, 
			boolean isStatic) {
		this.path = path;
		this.id = id;
		this.field = field;
		this.isFinal=isFinal;
		this.isStatic=isStatic;
	}


	public String getField() {
		return field;
	}


	public void setField(String field) {
		this.field = field;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public boolean isFinal() {
		return isFinal;
	}


	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}


	public boolean isStatic() {
		return isStatic;
	}


	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}


	public GvgIdInfo generateParameterizedInfo(int level){
		String localPath = this.path+level;
		String localId = this.id+level;
		
		return new GvgIdInfo(localPath, localId, this.field,
				this.isFinal, this.isStatic);
	}
	
	@Override
	public GvgIdInfo clone(){
		return new GvgIdInfo(this.path, this.id, this.field,
				this.isFinal, this.isStatic);
	}
	
	@Override
	public String toString(){
		String s="";
		s=s+"\t\t\tpath: "+this.path+"\n";
		s=s+"\t\t\tid: "+this.id+"\n";
		s=s+"\t\t\tfield: "+this.field+"\n";
		s=s+"\t\t\tisFinal: "+this.isFinal+"\n";
		s=s+"\t\t\tIsStatic: "+this.isStatic+"\n";
		return s;
	}

}
