package es.codegraff.gwt.genvalgui.rebind.creator.popup;

import com.google.gwt.user.rebind.SourceWriter;

import es.codegraff.gwt.genvalgui.client.auxiliar.GvgConstants;

public class PopupCreator {

	public static void createPopup(SourceWriter src, String namePopup) {
//		src.println(" 
//		PopupPanel %s = new PopupPanel();",namePopup);
		src.println("final DialogBox %s = new DialogBox();",namePopup);
	}

	public static void configurePopup(SourceWriter src, String nameTable) {
		src.println("VerticalPanel globalPanel = new VerticalPanel();");
		src.println("Button buttonClose = new Button(\"Close\");");
		src.println("buttonClose.addClickHandler(new ClickHandler() {");
		src.println("	@Override");
		src.println("	public void onClick(ClickEvent event) {");
		src.println("		%s.hide();",GvgConstants.NAME_POPUP);
		src.println("	}");
	    src.println("});");
		src.println("globalPanel.add(%s);",nameTable);
		src.println("globalPanel.add(buttonClose);");
		src.println("%s.add(globalPanel);",GvgConstants.NAME_POPUP);
		src.println("%s.setGlassEnabled(true);",GvgConstants.NAME_POPUP);
		src.println("%s.setPopupPosition(50,50);",GvgConstants.NAME_POPUP);
		src.println("buttonClose.getElement().setId(\"GenValGuiButton\");");
		src.println("buttonClose.setStylePrimaryName(\"GenValGuiButton\");");
        src.println("%s.show();",GvgConstants.NAME_POPUP);
	}

}
