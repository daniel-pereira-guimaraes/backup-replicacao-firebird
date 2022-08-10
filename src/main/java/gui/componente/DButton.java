package gui.componente;

import javax.swing.JButton;
import javax.swing.SwingConstants;

import geral.Util;

@SuppressWarnings("serial")
public class DButton extends JButton {
	
	public DButton(String text) {
		super(text);
	}
	
	public DButton(String text, String iconResource) {
		super(text, Util.getResourceAsImageIcon(iconResource));
	}
	
	public DButton(String text, String iconResource, boolean iconOnTop) {
		this(text, iconResource);
		if (iconOnTop) {
			setVerticalTextPosition(SwingConstants.BOTTOM);
			setHorizontalTextPosition(SwingConstants.CENTER);
		}
	}

}
