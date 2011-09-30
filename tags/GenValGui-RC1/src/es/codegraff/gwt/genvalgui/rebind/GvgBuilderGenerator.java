package es.codegraff.gwt.genvalgui.rebind;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.validation.client.GwtValidation;

import es.codegraff.gwt.genvalgui.client.GvgInfoAnnotation;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgUtils;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgConstants.Steps;
import es.codegraff.gwt.genvalgui.rebind.creator.GvgAbstractCreator;
import es.codegraff.gwt.genvalgui.rebind.creator.GvgCreator;
import es.codegraff.gwt.genvalgui.rebind.creator.cell.CellConstructor;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNode;

public class GvgBuilderGenerator extends Generator{

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			String typeName) throws UnableToCompleteException {
					
		
		//get the typeOracle
		TypeOracle typeOracle = context.getTypeOracle();
		
		//get the class type
	    JClassType type = typeOracle.findType(typeName);
	    
	    //get and check that the @GvgInfoAnnotation is correct
	    GvgInfoAnnotation gvgAnnotation = type.findAnnotationInTypeHierarchy(GvgInfoAnnotation.class);
	    checkAnnotation(gvgAnnotation, logger, typeName);
	    
	    //get and check that the @GwtAnnotation for get the groups.
	    String validatorClass = typeName.substring(0,typeName.lastIndexOf("."))+".GwtValidator";
	    GwtValidation validationAnnotation = typeOracle.findType(validatorClass)
	    				.findAnnotationInTypeHierarchy(GwtValidation.class);
	    checkAnnotation(validationAnnotation, logger, typeName);

	    //build the subclass name
	    String className = type.getSimpleSourceName()+"Impl";
		String packageName = type.getPackage().getName();

		return mediatorGenerator(logger, context, packageName, className, typeName,
				gvgAnnotation, validationAnnotation,typeOracle);
	    
	}	
			

	/**
	 * This method allows the steps for the process of generate the code.
	 * First it´s mandatory generate a correct Tree Node for save all 
	 * the information about the classes [fields, types, names...].
	 * Then, we must generate the code in relation to the Tree Node.
	 * 
	 * In addition, in the middle of the proccess the program refresh the
	 * information. In this version the is obligatory generate the:
	 * 	- Permited Class list
	 *  - Enum Class List.
	 *  
	 * @author apanizo
	 * @throws UnableToCompleteException 
	 */
	private String mediatorGenerator(TreeLogger logger, GeneratorContext context,
			String packageName, String className, String typeName,	
			GvgInfoAnnotation gvgAnnotation, GwtValidation validationAnnotation,
			TypeOracle typeOracle) throws UnableToCompleteException {
		
		checkAnnotation(gvgAnnotation, logger, typeName);
		//STEP 0 [Init]: Create the structure of GvgUtils. Necessary fot the STEP 1 and 2. 
			//Add information for the next step
				GvgUtils.addLogger(logger);
				GvgUtils.addPermitedType(gvgAnnotation);
				GvgUtils.setValidationGroups(validationAnnotation.groups());
			//Check the information for the next step.
				checkLogger(logger, Steps.Init);
				checkPermitedTypes(GvgUtils.getPermitedClass(),logger, Steps.Init);
		//STEP 1 [Generate]: Generate the Tree Node. The tree Node saves the information
			//Create the corresponding information [Tree Node]
				GvgGeneratorNode treeNode = new GvgGeneratorNode(logger,typeOracle,gvgAnnotation);
				treeNode.createTreeNode();
				//System.out.println(treeNode.toString());
			//Add information for the next step
				GvgUtils.addEnumType(treeNode.getEnumClass());	
				CellConstructor.addCustomCreatorsCells();
			//Check the information for the next step.
				checkEnumTypes(GvgUtils.getEnumClass(), logger, Steps.Generate);
				checkCellConstructors();
		//STEP 2 [Create]: Generate the code
				//Create the corresponding information [creator]
				GvgAbstractCreator creator = new GvgCreator(logger, context, validationAnnotation,
						gvgAnnotation, treeNode.getNodeClasses());	
			//Send the information for the next step.
				return creator.create(packageName,className,typeName);
	}
	
	private void checkCellConstructors() {
		CellConstructor.checkConstructors();
	}


	private void checkEnumTypes(ArrayList<String> enumClass, TreeLogger logger,
			Steps step) throws UnableToCompleteException {
		if (enumClass == null){
			GvgUtils.logger.log(TreeLogger.ERROR,"[GvgUtils.checkConditions]-" 
					+step.name()+". The List of enum is null.",new UnableToCompleteException());
			throw new UnableToCompleteException();
		}
	}

	private void checkPermitedTypes(ArrayList<String> permitedClass, TreeLogger logger, 
			Steps step) throws UnableToCompleteException {
		if ((permitedClass == null)||(permitedClass.size()==0 )){
			GvgUtils.logger.log(TreeLogger.ERROR,"[GvgUtils.checkConditions]-" 
					+step.name()+". The List of permited class is null or 0 size.",new UnableToCompleteException());
			throw new UnableToCompleteException();
		}
	}

	private void checkLogger(TreeLogger logger, Steps step) throws UnableToCompleteException {
		if(GvgUtils.logger == null){
			GWT.log("[GvgUtils.checkConditions]-" +step.name()+". The logger is null.",new UnableToCompleteException());
			throw new UnableToCompleteException();
		}
	}

	private <T> void checkAnnotation( T annotation, TreeLogger logger,
			String typeName) throws UnableToCompleteException {
	
		//Valid for all the annotations {GwtValidation and GvgInfoAnnotation}
		if (annotation == null)
			logger.log(TreeLogger.ERROR, typeName + " The annotation can " +
				"not be null", new UnableToCompleteException());
		
		if(annotation instanceof GvgInfoAnnotation){
		    if (((GvgInfoAnnotation)annotation).value().length == 0) {
		      logger.log(TreeLogger.ERROR,
		          "The @" + GvgInfoAnnotation.class.getSimpleName() + "  of " + typeName
		              + "must specify at least one bean type to validate.", new UnableToCompleteException());
		    }
		    return;
		}
		
	}
}


