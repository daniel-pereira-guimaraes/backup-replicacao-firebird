package gui.componente;

import javax.swing.JComboBox;

import geral.Dialogo;

@SuppressWarnings("serial")
public class DComboBox extends JComboBox<Object> {

	private boolean required = false;

	public DComboBox (boolean required) {
		super();
		this.required = required;
	}
	
	public DComboBox(boolean required, String name) {
		this(required);
		setName(name);
	}
		
	public boolean getRequired() {
		return required;
	}
	
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public boolean isEmpty() {
		Object item = getSelectedItem();
		if (item == null)
			return true;
		else if (item instanceof String)
			return ((String) item).isEmpty();
		else
			return false;
	}
	
	public void checkValue() throws Exception {
		if (required && isEmpty()) {
			grabFocus();
			Dialogo.requiredField(this);
		}
	}

}
