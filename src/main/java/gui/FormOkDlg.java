package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import geral.Constante;

@SuppressWarnings("serial")
public abstract class FormOkDlg extends FormDlg {
	
	private JButton btnOk = new JButton(Constante.STR_OK);
	
	public FormOkDlg() {
		super();
		getRootPane().setDefaultButton(btnOk);
		getPnlBotoes().add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fecha(JOptionPane.OK_OPTION);
			}
		});
		
	}
	
	protected JButton getBtnOk() {
		return btnOk;
	}

}
