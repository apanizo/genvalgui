package es.codegraff.gwt.genvalgui.rebind.creator;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.ConstraintViolation;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.TextInputCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;
import com.google.gwt.validation.client.GwtValidation;

import es.codegraff.gwt.genvalgui.client.GvgInfoAnnotation;
import es.codegraff.gwt.genvalgui.client.GvgInterfaceBuilder;
import es.codegraff.gwt.genvalgui.client.auxiliar.GvgConstants;
import es.codegraff.gwt.genvalgui.rebind.creator.cell.CellConstructor;
import es.codegraff.gwt.genvalgui.rebind.creator.table.TableCellCreator;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeClass;
import es.codegraff.gwt.genvalgui.rebind.generator.GvgGeneratorNodeField;


public class GvgCreator extends GvgAbstractCreator{
	private TreeLogger logger;
	private GeneratorContext context;
	private GwtValidation validationAnnotation;
	private GvgInfoAnnotation gvgAnnotation;
	private ArrayList<GvgGeneratorNodeClass> nodeClass;
	
	public GvgCreator(TreeLogger logger, GeneratorContext context,
			GwtValidation validationAnnotation, GvgInfoAnnotation gvgAnnotation,
			ArrayList<GvgGeneratorNodeClass> nodeClass) {
		this.logger = logger;
		this.context=context;
		this.validationAnnotation = validationAnnotation;
		this.gvgAnnotation=gvgAnnotation;
		this.nodeClass=nodeClass;
	}


	@Override
	public String create(String packageName, String className, String typeName) 
									throws UnableToCompleteException {
		PrintWriter printWriter = this.context.tryCreate(this.logger, 
	    		packageName, className);

	    ClassSourceFileComposerFactory composerFactory =
	    	new ClassSourceFileComposerFactory(packageName, className);
	    compose(composerFactory, typeName);
	    SourceWriter src = composerFactory.createSourceWriter(this.context, printWriter);
	    writeClassBody(src);
		src.commit(logger);
		return composerFactory.getCreatedClassName();

		
		
	}

	@Override
	protected void compose(ClassSourceFileComposerFactory composerFactory, String typeName) {
		addImports(composerFactory);
	    addInterface(composerFactory, typeName);

		
	}

	private void addImports(ClassSourceFileComposerFactory composerFactory) {
		for (Class<?> c: this.gvgAnnotation.value()){
			composerFactory.addImport(c.getName());
		}
		composerFactory.addImport(ArrayList.class.getName());
		composerFactory.addImport(HashMap.class.getName());
		composerFactory.addImport(Window.class.getName());
		composerFactory.addImport(Widget.class.getName());
		composerFactory.addImport(Logger.class.getName());
		composerFactory.addImport(Level.class.getName());
		composerFactory.addImport(GWT.class.getName());
		composerFactory.addImport(CellTable.class.getName());
		composerFactory.addImport(ActionCell.class.getName());
		composerFactory.addImport(SelectionCell.class.getName());
		composerFactory.addImport(PopupPanel.class.getName());
		composerFactory.addImport(TextInputCell.class.getName());
		composerFactory.addImport(Column.class.getName());
		composerFactory.addImport(FieldUpdater.class.getName());
		composerFactory.addImport(GvgConstants.class.getName());
		composerFactory.addImport(Set.class.getName());
		composerFactory.addImport(ConstraintViolation.class.getName());
		composerFactory.addImport(Element.class.getName());
		composerFactory.addImport(ButtonCell.class.getName());
		composerFactory.addImport(GvgInterfaceBuilder.class.getName());
		for(Class<?> c : this.validationAnnotation.groups()){
			composerFactory.addImport(c.getName());
		}
		composerFactory.addImport(RootPanel.class.getName());
		composerFactory.addImport(HorizontalPanel.class.getName());
		composerFactory.addImport(HTML.class.getName());
		composerFactory.addImport(Image.class.getName());
		composerFactory.addImport(TextCell.class.getName());
		composerFactory.addImport(ClickEvent.class.getName());
		composerFactory.addImport(ClickHandler.class.getName());
		composerFactory.addImport(Button.class.getName());
		composerFactory.addImport(DialogBox.class.getName());
		composerFactory.addImport(VerticalPanel.class.getName());

		
	}
	private void addInterface( ClassSourceFileComposerFactory composerFactory, 
			String typeName) {
		composerFactory.addImplementedInterface(typeName);
	}
	
	@Override
	protected void writeClassBody(SourceWriter src)
			throws UnableToCompleteException {
	    writeVars(src);
		writeConstructor(src);
		writeMethods(src);
	}

	private void writeVars(SourceWriter src) {
		// TODO Auto-generated method stub
		
	}
	
	private void writeConstructor(SourceWriter src) {
		// TODO Auto-generated method stub
		
	}

	private void writeMethods(SourceWriter src) {
		builGuiCreator(src,this.nodeClass);
		
		
	}

	private <T>  void builGuiCreator(SourceWriter src,
			ArrayList<GvgGeneratorNodeClass> gvgNodeClass) {
		src.println("public <T> CellTable<T> buildGui(Class<T> c, Object object){");
		src.println("	if(object == null)");
		src.println("		return null;");
		
		src.println("if (!(object instanceof ArrayList)){");
		src.println("	ArrayList<T> objectAl = new ArrayList<T>();");
		src.println("	objectAl.add((T) object);");
		src.println("	return ((CellTable<T>)buildGui(c,objectAl));");
		src.println("}");
		for (GvgGeneratorNodeClass nodeClass: gvgNodeClass){
			src.println("	if(c.getName().equals(\"%s\")){",nodeClass.getNameClass());
			src.println("		return ((CellTable<T>) build%s(%s.class, (ArrayList<%s>) object));",
					nodeClass.getNameClassSimple(),nodeClass.getNameClassSimple(),
					nodeClass.getNameClassSimple());
			src.println("	}");
		}
		
		src.println("	return null;");
		src.println("}");
		for (GvgGeneratorNodeClass nodeClass: gvgNodeClass){
			buildGuiCreatorType(src, nodeClass);
		}
	}


	private void buildGuiCreatorType(SourceWriter src, GvgGeneratorNodeClass nodeClass) {
		src.println("public <T> CellTable<T> build%s (Class<T> c, final ArrayList<%s> object%s){",
				nodeClass.getNameClassSimple(),nodeClass.getNameClassSimple(),
				nodeClass.getNameClassSimple());
		String type=nodeClass.getNameClassSimple();
		String nameTable= nodeClass.getNameTable();
		
		TableCellCreator.createTable(src, type, nameTable);
		CellConstructor cellConstructor = new CellConstructor();
		for(GvgGeneratorNodeField nodeField : nodeClass.getFields()){
			String nameColumn = nodeField.getGvgCellWidgetInfo().getNameColumn();
			String nameColumnTittle = nodeField.getGvgCellWidgetInfo().getNameColumnTittle();
			cellConstructor.generateCellCode(src,cellConstructor, nodeClass.getNameClassSimple(), nodeField);
			TableCellCreator.createAddColumnToTable(src, nameTable, nameColumn,nameColumnTittle);
		}
		CellConstructor.createColumnValidation(src, nodeClass);
		String globalName = "object"+nodeClass.getNameClassSimple();
		TableCellCreator.addInformation(src,globalName,nodeClass.getNameTable());
		src.println("return (CellTable<T>)%s;",nameTable);
		src.println("}");
		
	}


	

}
