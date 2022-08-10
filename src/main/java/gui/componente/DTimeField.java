package gui.componente;

@SuppressWarnings("serial")
public class DTimeField extends DTextField {

	public DTimeField() {
		super();
	}
	
	public DTimeField(boolean required) {
		super(required);
	}
	
	public DTimeField(boolean required, String name) {
		super(required, name);
	}
	
	@Override 
	public void checkValue() throws Exception {
		super.checkValue();
		try {
			setLocalTime(getLocalTime());
		} catch(Exception e) {
			grabFocus();
			throw e;
		}
	}
	
	@Override
	protected void formatValue() throws Exception {
		super.formatValue();
		setLocalTime(getLocalTime());
	}
		
}
