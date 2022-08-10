package gui;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import geral.Constante;
import geral.Dialogo;
import gui.componente.DComboBox;
import gui.componente.DPasswordField;
import gui.componente.DTextField;
import modelo.Pasta;
import regra.PastaRN;

@SuppressWarnings("serial")
public class FormEditarPasta extends FormEditar {

	private Integer id = null;
	
	private JLabel
		lblNome = new JLabel(Constante.STR_NOME),
		lblTipo = new JLabel(Constante.STR_CARACTERES),
		lblMaquina = new JLabel(Constante.STR_MAQUINA),
		lblPorta = new JLabel(Constante.STR_PORTA),
		lblEndereco = new JLabel(Constante.STR_ENDERECO),
		lblUsuario = new JLabel(Constante.STR_USUARIO),
		lblSenha = new JLabel(Constante.STR_SENHA);
	
	private DTextField
		txtNome = new DTextField(true, Constante.STR_NOME),
		txtMaquina = new DTextField(true, Constante.STR_MAQUINA),
		txtEndereco = new DTextField(true, Constante.STR_ENDERECO),
		txtPorta = new DTextField(true, Constante.STR_PORTA),
		txtUsuario = new DTextField(true, Constante.STR_USUARIO);
	
	private DPasswordField
		txtSenha = new DPasswordField(true, Constante.STR_SENHA);
	
	private DComboBox
		cbxTipo = new DComboBox(true, Constante.STR_TIPO);
			
	public FormEditarPasta(Pasta pasta) throws Exception {
		super(pasta == null);
		setSize(500, 370);
		setLocationRelativeTo(null);

		SpringLayout sl = new SpringLayout();
		JPanel p = getPnlPrincipal();
		p.setLayout(sl);
		
		p.add(lblNome);
		p.add(lblTipo);
		p.add(lblMaquina);
		p.add(lblPorta);
		p.add(lblEndereco);
		p.add(lblUsuario);
		p.add(lblSenha);

		p.add(txtNome);
		p.add(cbxTipo);
		p.add(txtMaquina);
		p.add(txtPorta);
		p.add(txtEndereco);
		p.add(txtUsuario);
		p.add(txtSenha);

		sl.putConstraint(SpringLayout.NORTH, lblNome, 20, SpringLayout.NORTH, p);
		sl.putConstraint(SpringLayout.WEST, lblNome, 20, SpringLayout.WEST, p);
				
		sl.putConstraint(SpringLayout.BASELINE, txtNome, 0, SpringLayout.BASELINE, lblNome);
		sl.putConstraint(SpringLayout.WEST, txtNome, 100, SpringLayout.WEST, p);
		sl.putConstraint(SpringLayout.EAST, txtNome, -20, SpringLayout.EAST, p);
		
		sl.putConstraint(SpringLayout.NORTH, lblTipo, 20, SpringLayout.SOUTH, lblNome);
		sl.putConstraint(SpringLayout.WEST, lblTipo, 0, SpringLayout.WEST, lblNome);
		
		sl.putConstraint(SpringLayout.BASELINE, cbxTipo, 0, SpringLayout.BASELINE, lblTipo);
		sl.putConstraint(SpringLayout.WEST, cbxTipo, 0, SpringLayout.WEST, txtNome);
		
		sl.putConstraint(SpringLayout.NORTH, lblMaquina, 20, SpringLayout.SOUTH, lblTipo);
		sl.putConstraint(SpringLayout.WEST, lblMaquina, 0, SpringLayout.WEST, lblTipo);
		
		sl.putConstraint(SpringLayout.BASELINE, txtMaquina, 0, SpringLayout.BASELINE, lblMaquina);
		sl.putConstraint(SpringLayout.WEST, txtMaquina, 0, SpringLayout.WEST, cbxTipo);

		sl.putConstraint(SpringLayout.NORTH, lblPorta, 20, SpringLayout.SOUTH, lblMaquina);
		sl.putConstraint(SpringLayout.WEST, lblPorta, 0, SpringLayout.WEST, lblMaquina);
		
		sl.putConstraint(SpringLayout.BASELINE, txtPorta, 0, SpringLayout.BASELINE, lblPorta);
		sl.putConstraint(SpringLayout.WEST, txtPorta, 0, SpringLayout.WEST, txtMaquina);

		sl.putConstraint(SpringLayout.NORTH, lblEndereco, 20, SpringLayout.SOUTH, lblPorta);
		sl.putConstraint(SpringLayout.WEST, lblEndereco, 0, SpringLayout.WEST, lblPorta);
		
		sl.putConstraint(SpringLayout.BASELINE, txtEndereco, 0, SpringLayout.BASELINE, lblEndereco);
		sl.putConstraint(SpringLayout.WEST, txtEndereco, 0, SpringLayout.WEST, txtPorta);
		sl.putConstraint(SpringLayout.EAST, txtEndereco, 0, SpringLayout.EAST, txtNome);

		sl.putConstraint(SpringLayout.NORTH, lblUsuario, 20, SpringLayout.SOUTH, lblEndereco);
		sl.putConstraint(SpringLayout.WEST, lblUsuario, 0, SpringLayout.WEST, lblEndereco);
		
		sl.putConstraint(SpringLayout.BASELINE, txtUsuario, 0, SpringLayout.BASELINE, lblUsuario);
		sl.putConstraint(SpringLayout.WEST, txtUsuario, 0, SpringLayout.WEST, txtEndereco);
		
		sl.putConstraint(SpringLayout.NORTH, lblSenha, 20, SpringLayout.SOUTH, lblUsuario);
		sl.putConstraint(SpringLayout.WEST, lblSenha, 0, SpringLayout.WEST, lblUsuario);
		
		sl.putConstraint(SpringLayout.BASELINE, txtSenha, 0, SpringLayout.BASELINE, lblSenha);
		sl.putConstraint(SpringLayout.WEST, txtSenha, 0, SpringLayout.WEST, txtUsuario);
								
		txtNome.setPreferredSize(new Dimension(300, 25));
		cbxTipo.setPreferredSize(new Dimension(100, 25));
		txtMaquina.setPreferredSize(new Dimension(200, 25));
		txtPorta.setPreferredSize(new Dimension(70, 25));
		txtEndereco.setPreferredSize(txtNome.getPreferredSize());
		txtUsuario.setPreferredSize(cbxTipo.getPreferredSize());
		txtSenha.setPreferredSize(txtUsuario.getPreferredSize());

		carregaTipos();
		cbxTipoItemListener();
		setPasta(pasta);
	}

	public FormEditarPasta() throws Exception {
		this(null);
	}
	
	@Override
	public void atualizaTela() {
		super.atualizaTela();
		boolean ftp = cbxTipo.getSelectedItem().equals(Pasta.Tipo.FTP);
		
		txtMaquina.setEditable(ftp);
		txtPorta.setEditable(ftp);
		txtUsuario.setEditable(ftp);
		txtSenha.setEditable(ftp);

		txtMaquina.setEnabled(ftp);
		txtPorta.setEnabled(ftp);
		txtUsuario.setEnabled(ftp);
		txtSenha.setEnabled(ftp);
		
		txtMaquina.setRequired(ftp);
		txtPorta.setRequired(ftp);
		txtUsuario.setRequired(ftp);
		txtSenha.setRequired(ftp);
		
		if (ftp) {
			if (txtPorta.isEmpty())
				txtPorta.setText(String.valueOf(Constante.FTP_PORTA_PADRAO));
		} else {
			txtMaquina.setText(null);
			txtPorta.setText(null);
			txtUsuario.setText(null);
			txtSenha.setText(null);
		}
	}

	private void carregaTipos() {
		for (Pasta.Tipo tipo : Pasta.Tipo.values())
			cbxTipo.addItem(tipo);
	}

	private void cbxTipoItemListener() {
		cbxTipo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				atualizaTela();
			}
		});
	}

	public Pasta getPasta() throws Exception {
		Pasta pasta = new Pasta();
		pasta.setId(this.id);
		pasta.setNome(txtNome.getText());
		pasta.setTipo((Pasta.Tipo)cbxTipo.getSelectedItem());
		pasta.setMaquina(txtMaquina.getText());
		pasta.setPorta(txtPorta.getInt());
		pasta.setEndereco(txtEndereco.getText());
		pasta.setUsuario(txtUsuario.getText());
		pasta.setSenha(new String(txtSenha.getPassword()));
		return pasta;
	}
	
	private void setPasta(Pasta pasta) {
		if (pasta == null) {
			this.id = null;
			cbxTipo.setSelectedItem(Pasta.Tipo.LOCAL);
		} else {
			this.id = pasta.getId();
			txtNome.setText(pasta.getNome());
			cbxTipo.setSelectedItem(pasta.getTipo());
			txtMaquina.setText(pasta.getMaquina());
			txtPorta.setInt(pasta.getPorta());
			txtEndereco.setText(pasta.getEndereco());
			txtUsuario.setText(pasta.getUsuario());
			txtSenha.setText(pasta.getSenha());
		}
	}
	
	private void verificaDados() throws Exception {
		txtNome.checkValue();
		cbxTipo.checkValue();
		txtMaquina.checkValue();
		txtPorta.checkValue();
		txtEndereco.checkValue();
		txtUsuario.checkValue();
		txtSenha.checkValue();
	}
	
	private boolean gravaDados() {
		try {
			verificaDados();
			setPasta(PastaRN.grava(getPasta()));
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
