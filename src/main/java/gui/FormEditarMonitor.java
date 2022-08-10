package gui;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import geral.Constante;
import geral.Dialogo;
import gui.componente.DTextField;
import modelo.Monitor;
import regra.MonitorRN;

@SuppressWarnings("serial")
public class FormEditarMonitor extends FormEditar {

	private Integer id = null;
	
	private JLabel
		lblNome = new JLabel(Constante.STR_NOME),
		lblChave = new JLabel(Constante.STR_CHAVE);
	
	private DTextField
		txtNome = new DTextField(true, Constante.STR_NOME),
		txtChave = new DTextField(true, Constante.STR_CHAVE);
				
	public FormEditarMonitor(Monitor monitor) throws Exception {
		super(monitor == null);
		setSize(500, 200);
		setLocationRelativeTo(null);

		SpringLayout sl = new SpringLayout();
		JPanel p = getPnlPrincipal();
		p.setLayout(sl);
		
		p.add(lblNome);
		p.add(lblChave);

		p.add(txtNome);
		p.add(txtChave);

		sl.putConstraint(SpringLayout.NORTH, lblNome, 20, SpringLayout.NORTH, p);
		sl.putConstraint(SpringLayout.WEST, lblNome, 20, SpringLayout.WEST, p);
				
		sl.putConstraint(SpringLayout.BASELINE, txtNome, 0, SpringLayout.BASELINE, lblNome);
		sl.putConstraint(SpringLayout.WEST, txtNome, 100, SpringLayout.WEST, p);
		sl.putConstraint(SpringLayout.EAST, txtNome, -20, SpringLayout.EAST, p);
		
		sl.putConstraint(SpringLayout.NORTH, lblChave, 20, SpringLayout.SOUTH, lblNome);
		sl.putConstraint(SpringLayout.WEST, lblChave, 0, SpringLayout.WEST, lblNome);
		
		sl.putConstraint(SpringLayout.BASELINE, txtChave, 0, SpringLayout.BASELINE, lblChave);
		sl.putConstraint(SpringLayout.WEST, txtChave, 0, SpringLayout.WEST, txtNome);

		txtNome.setPreferredSize(new Dimension(300, 25));
		txtChave.setPreferredSize(new Dimension(200, 25));

		setMonitor(monitor);
	}
	
	public FormEditarMonitor() throws Exception {
		this(null);
	}

	public Monitor getMonitor() throws Exception {
		Monitor monitor = new Monitor();
		monitor.setId(this.id);
		monitor.setNome(txtNome.getText());
		monitor.setChave(txtChave.getText());
		return monitor;
	}
	
	private void setMonitor(Monitor monitor) {
		if (monitor == null) {
			this.id = null;
		} else {
			this.id = monitor.getId();
			txtNome.setText(monitor.getNome());
			txtChave.setText(monitor.getChave());
		}
	}
	
	private void verificaDados() throws Exception {
		txtNome.checkValue();
		txtChave.checkValue();
	}
	
	private boolean gravaDados() {
		try {
			verificaDados();
			setMonitor(MonitorRN.grava(getMonitor()));
			return true;
		} catch (Exception e) {
			Dialogo.erro(e);
		}
		return false;
	}
	
	@Override
	protected void onShow() {
		super.onShow();
		txtNome.grabFocus();
	}
	
	@Override
	protected boolean podeFechar(int resultado) {
		return (resultado != JOptionPane.OK_OPTION || gravaDados());
	}
	
}
