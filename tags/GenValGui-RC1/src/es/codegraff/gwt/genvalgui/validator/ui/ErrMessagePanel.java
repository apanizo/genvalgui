package es.codegraff.gwt.genvalgui.validator.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class ErrMessagePanel extends PopupPanel{
	
	public ErrMessagePanel(){
		super(true);
		
		//setZIndex(this, 5000);  //Fix for thirdparty componenets, i.e, gxt, gwtext
		
		this.box = new ErrMessageBox();
		box.setShowCloseButton(true);
		this.add(box);
		
		box.getLinkClose().addClickListener(new ClickListener(){

			public void onClick(Widget sender) {
				ErrMessagePanel.this.hide();
			}});
		
		this.setStylePrimaryName("validate_pnl");
	}
	
	public ErrMessageBox getErrorBox() {
		return box;
	}
		
	public void showPanel(UIObject uiObject){
		showPanel(uiObject.getElement());
	}
	
	public void showPanel(final Element attachedElement){
		this.attachedElement = attachedElement;
		this.setPopupPositionAndShow(new PositionCallback(){

			public void setPosition(int offsetWidth, int offsetHeight) {
				setPopupPosition(getLeft(attachedElement, offsetWidth), getTop(attachedElement, offsetHeight));
			}});
	}
	
	public void addErrorMsg(String msg){
		box.addErrorMsg(msg);
	}
	
	public void clearErrorMsgs(){
		box.clearErrorMsgs();
	}
	
	private int getTop(Element uiObject, int offsetHeight){
		int result = uiObject.getAbsoluteTop();
		result = result - offsetHeight;
	  return result;
	}
	
	private int getLeft(Element uiObject, int offsetWidth){
		return uiObject.getAbsoluteLeft() + uiObject.getOffsetWidth() - 40;
		//return uiObject.getOffsetLeft() + uiObject.getOffsetWidth() - 40;
	}
	
	private static void changeToStaticPositioning(Element elem) {
    DOM.setStyleAttribute(elem, "left", "");
    DOM.setStyleAttribute(elem, "top", "");
    DOM.setStyleAttribute(elem, "position", "");
  }
	
	protected void setZIndex(Widget w, int zindex){
		Element h = w.getElement();
		DOM.setStyleAttribute(h, "z-Index", String.valueOf(zindex));
	}
	
	protected void setWidgetPositionImpl(Widget w, int left, int top) {
    Element h = w.getElement();
    if ((left == -1) && (top == -1)) {
      changeToStaticPositioning(h);
    } else {
      DOM.setStyleAttribute(h, "position", "absolute");
      DOM.setStyleAttribute(h, "left", left + "px");
      DOM.setStyleAttribute(h, "top", top + "px");
    }
  }


	public Element getAttachedElement() {
		return attachedElement;
	}

	private final ErrMessageBox box;
	private Element attachedElement;
}
