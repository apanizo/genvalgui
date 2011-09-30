package es.codegraff.gwt.genvalgui.rebind.creator;

import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public abstract class GvgAbstractCreator {
	
	public abstract String create(String packageName, String className, String typeName) 
				throws UnableToCompleteException;
		
	protected abstract void compose(ClassSourceFileComposerFactory composerFactory, String typeName);
	protected abstract void writeClassBody(SourceWriter sourceWriter)
    	throws UnableToCompleteException;
	
}
