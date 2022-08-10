package gui.componente;

@SuppressWarnings("serial")
public class DDateField extends DTextField {

	public DDateField() {
		super();
	}
	
	public DDateField(boolean required) {
		super(required);
	}
	
	public DDateField(boolean required, String name) {
		super(required, name);
	}
	
	@Override 
	public void checkValue() throws Exception {
		super.checkValue();
		try {
			setLocalDate(getLocalDate());
		} catch(Exception e) {
			grabFocus();
			throw e;
		}
	}
	
	@Override
	protected void formatValue() throws Exception {
		super.formatValue();
		setLocalDate(getLocalDate());
	}
		
}
