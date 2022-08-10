package gui.componente;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JTextField;

import geral.Dialogo;
import geral.Util;

@SuppressWarnings("serial")
public class DTextField extends JTextField implements FocusListener {

	private boolean required = false;

	public DTextField() {
		super();
		addFocusListener(this);
	}

	public DTextField(boolean required) {
		this();
		this.required = required;
	}
	
	public DTextField(boolean required, String name) {
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
		return Util.isNullOrEmpty(getText());
	}
	
	public void checkValue() throws Exception {
		if (required && isEmpty()) {
			grabFocus();
			Dialogo.requiredField(this);
		}
	}
	
	protected void formatValue() throws Exception {}
	
	public Integer getInt() throws Exception {
		if (isEmpty())
			return null;
		else
			return Integer.parseInt(getText());
	}
	
	public Double getDouble() throws Exception {
		if (isEmpty())
			return null;
		else
			return Util.toDouble(getText());
	}
	
	public LocalDate getLocalDate() throws Exception {
		return Util.toLocalDate(getText());
	}

	public LocalTime getLocalTime() throws Exception {
		return Util.toLocalTime(getText());
	}
	
	public void setInt(Integer value) {
		if (value == null)
			setText(null);
		else
			setText(String.valueOf(value));
	}
	
	public void setDouble(Double value) {
		if (value == null)
			setText(null);
		else
			setText(String.valueOf(value));
	}
	
	public void setLocalDate(LocalDate localDate) {
		setText(Util.toString(localDate));
	}
	
	public void setLocalTime(LocalTime localTime) {
		setText(Util.toString(localTime));
	}
	
	@Override
	public String getText() {
		return super.getText().trim();
	}
	
	@Override 
	public void setText(String text) {
		if (text == null)
			super.setText(text);
		else
			super.setText(text.trim());
	}
	
	@Override
	public void focusGained(FocusEvent fe) {
		selectAll();
	}

	@Override
	public void focusLost(FocusEvent fe) {
		try {
			if (!isEmpty())
				formatValue();
		} catch (Throwable ignora) {}
	}

}
