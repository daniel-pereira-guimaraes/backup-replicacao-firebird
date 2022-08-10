package gui;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class FormDlg extends FormBase {
	
	private JPanel
		pnlPrincipal = new JPanel(new BorderLayout()),
		pnlSul = new JPanel(),
		pnlBotoes = new JPanel(new GridLayout(1,0,10,10));
		
	public FormDlg() {
		super();
		///setResizable(false);
		add(pnlPrincipal, BorderLayout.CENTER);
		add(pnlSul, BorderLayout.SOUTH);
		addWindowListener(this);
		pnlSul.add(pnlBotoes);
		pnlBotoes.setBorder(BorderFactory.createEmptyBorder(10,15,10,15));
	}

	protected JPanel getPnlSul() {
		return pnlSul;
	}
	
	protected JPanel getPnlPrincipal() {
		return pnlPrincipal;
	}
	
	protected JPanel getPnlBotoes() {
		return pnlBotoes;
	}
	
}
