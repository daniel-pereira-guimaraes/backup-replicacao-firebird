package gui.componente;

import geral.Constante;

@SuppressWarnings("serial")
public class DIntField extends DTextField {

	private int minValue = 0;
	private int maxValue = -1;
	
	public DIntField() {
		super();
	}
	
	public DIntField(boolean required) {
		super(required);
	}
	
	public DIntField(boolean required, String name) {
		super(required, name);
	}
	
	@Override 
	public void checkValue() throws Exception {
		super.checkValue();
		try {
			int value = getInt();
			setInt(value);
			if (minValue <= maxValue) {
				if (value < minValue || value > maxValue) {
					throw new Exception(String.format(
						Constante.STR_INTERVALO_INVALIDO, 
						value, minValue, maxValue));
				}
			}
		} catch(Exception e) {
			grabFocus();
			throw e;
		}
	}
	
	@Override
	protected void formatValue() throws Exception {
		super.formatValue();
		setInt(getInt());
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
		
}
