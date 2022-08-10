package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import geral.Constante;

@SuppressWarnings("serial")
public abstract class FormOkCancelDlg extends FormOkDlg {

	private JButton btnCancelar = new JButton(Constante.STR_CANCELAR);
	
	public FormOkCancelDlg() {
		super();
		getPnlBotoes().add(btnCancelar);
		btnCancelarClick();
		teclaEscape();
	}

	private void btnCancelarClick() {
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				fecha(JOptionPane.CANCEL_OPTION);
			}
		});
	}
	
	private void teclaEscape() {
		KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0);
	    Action actionListener = new AbstractAction() {
	        @Override
	        public void actionPerformed(ActionEvent ae) {
	        	fecha(JOptionPane.CANCEL_OPTION);
	        }
	    };
	    InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
	    inputMap.put(stroke, "ESC");
	    this.getRootPane().getActionMap().put("ESC", actionListener);		
	}
	
}
