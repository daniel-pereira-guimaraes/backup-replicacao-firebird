package gui.componente;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

import geral.Dialogo;

@SuppressWarnings("serial")
public class DPasswordField extends JPasswordField implements FocusListener {

	private boolean required = false;

	public DPasswordField() {
		super();
		addFocusListener(this);
	}

	public DPasswordField(boolean required) {
		this();
		this.required = required;
	}
	
	public DPasswordField(boolean required, String name) {
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
		return getPassword().length == 0;
	}
	
	public void checkValue() throws Exception {
		if (required && isEmpty()) {
			grabFocus();
			Dialogo.requiredField(this);
		}
	}
		
	@Override
	public void focusGained(FocusEvent fe) {
		selectAll();
	}

	@Override
	public void focusLost(FocusEvent fe) {}

}
