package gui;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import geral.Constante;
import geral.Dialogo;
import geral.Util;
import gui.componente.DComboBox;
import gui.componente.DPasswordField;
import gui.componente.DTextField;
import modelo.BancoDados;
import regra.BancoDadosRN;

@SuppressWarnings("serial")
public class FormEditarBancoDados extends FormEditar {

	private Integer id = null;
	
	private JLabel
		lblNome = new JLabel(Constante.STR_NOME),
		lblMaquina = new JLabel(Constante.STR_MAQUINA),
		lblPorta = new JLabel(Constante.STR_PORTA),
		lblEndereco = new JLabel(Constante.STR_ENDERECO),
		lblCaracteres = new JLabel(Constante.STR_CARACTERES),
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
		cbxCaracteres = new DComboBox(true, Constante.STR_CARACTERES);
			
	public FormEditarBancoDados(BancoDados bancoDados) throws Exception {
		super(bancoDados == null);
		setSize(500, 370);
		setLocationRelativeTo(null);

		SpringLayout sl = new SpringLayout();
		JPanel p = getPnlPrincipal();
		p.setLayout(sl);
		
		p.add(lblNome);
		p.add(lblMaquina);
		p.add(lblPorta);
		p.add(lblEndereco);
		p.add(lblCaracteres);
		p.add(lblUsuario);
		p.add(lblSenha);

		p.add(txtNome);
		p.add(txtMaquina);
		p.add(txtPorta);
		p.add(txtEndereco);
		p.add(cbxCaracteres);
		p.add(txtUsuario);
		p.add(txtSenha);

		sl.putConstraint(SpringLayout.NORTH, lblNome, 20, SpringLayout.NORTH, p);
		sl.putConstraint(SpringLayout.WEST, lblNome, 20, SpringLayout.WEST, p);
				
		sl.putConstraint(SpringLayout.BASELINE, txtNome, 0, SpringLayout.BASELINE, lblNome);
		sl.putConstraint(SpringLayout.WEST, txtNome, 100, SpringLayout.WEST, p);
		sl.putConstraint(SpringLayout.EAST, txtNome, -20, SpringLayout.EAST, p);
		
		sl.putConstraint(SpringLayout.NORTH, lblMaquina, 20, SpringLayout.SOUTH, lblNome);
		sl.putConstraint(SpringLayout.WEST, lblMaquina, 0, SpringLayout.WEST, lblNome);
		
		sl.putConstraint(SpringLayout.BASELINE, txtMaquina, 0, SpringLayout.BASELINE, lblMaquina);
		sl.putConstraint(SpringLayout.WEST, txtMaquina, 0, SpringLayout.WEST, txtNome);

		sl.putConstraint(SpringLayout.NORTH, lblPorta, 20, SpringLayout.SOUTH, lblMaquina);
		sl.putConstraint(SpringLayout.WEST, lblPorta, 0, SpringLayout.WEST, lblMaquina);
		
		sl.putConstraint(SpringLayout.BASELINE, txtPorta, 0, SpringLayout.BASELINE, lblPorta);
		sl.putConstraint(SpringLayout.WEST, txtPorta, 0, SpringLayout.WEST, txtMaquina);

		sl.putConstraint(SpringLayout.NORTH, lblEndereco, 20, SpringLayout.SOUTH, lblPorta);
		sl.putConstraint(SpringLayout.WEST, lblEndereco, 0, SpringLayout.WEST, lblPorta);
		
		sl.putConstraint(SpringLayout.BASELINE, txtEndereco, 0, SpringLayout.BASELINE, lblEndereco);
		sl.putConstraint(SpringLayout.WEST, txtEndereco, 0, SpringLayout.WEST, txtPorta);
		sl.putConstraint(SpringLayout.EAST, txtEndereco, 0, SpringLayout.EAST, txtNome);

		sl.putConstraint(SpringLayout.NORTH, lblCaracteres, 20, SpringLayout.SOUTH, lblEndereco);
		sl.putConstraint(SpringLayout.WEST, lblCaracteres, 0, SpringLayout.WEST, lblEndereco);
		
		sl.putConstraint(SpringLayout.BASELINE, cbxCaracteres, 0, SpringLayout.BASELINE, lblCaracteres);
		sl.putConstraint(SpringLayout.WEST, cbxCaracteres, 0, SpringLayout.WEST, txtEndereco);
		
		sl.putConstraint(SpringLayout.NORTH, lblUsuario, 20, SpringLayout.SOUTH, lblCaracteres);
		sl.putConstraint(SpringLayout.WEST, lblUsuario, 0, SpringLayout.WEST, lblCaracteres);
		
		sl.putConstraint(SpringLayout.BASELINE, txtUsuario, 0, SpringLayout.BASELINE, lblUsuario);
		sl.putConstraint(SpringLayout.WEST, txtUsuario, 0, SpringLayout.WEST, cbxCaracteres);
		
		sl.putConstraint(SpringLayout.NORTH, lblSenha, 20, SpringLayout.SOUTH, lblUsuario);
		sl.putConstraint(SpringLayout.WEST, lblSenha, 0, SpringLayout.WEST, lblUsuario);
		
		sl.putConstraint(SpringLayout.BASELINE, txtSenha, 0, SpringLayout.BASELINE, lblSenha);
		sl.putConstraint(SpringLayout.WEST, txtSenha, 0, SpringLayout.WEST, txtUsuario);
								
		txtNome.setPreferredSize(new Dimension(300, 25));
		txtMaquina.setPreferredSize(new Dimension(200, 25));
		txtPorta.setPreferredSize(new Dimension(70, 25));
		txtEndereco.setPreferredSize(txtNome.getPreferredSize());
		cbxCaracteres.setPreferredSize(new Dimension(150, 25));
		txtUsuario.setPreferredSize(cbxCaracteres.getPreferredSize());
		txtSenha.setPreferredSize(txtUsuario.getPreferredSize());

		Util.add(cbxCaracteres, Constante.LISTA_CARACTERES);
		setBancoDados(bancoDados);
	}
	
	public FormEditarBancoDados() throws Exception {
		this(null);
	}

	public BancoDados getBancoDados() throws Exception {
		BancoDados bancoDados = new BancoDados();
		bancoDados.setId(this.id);
		bancoDados.setNome(txtNome.getText());
		bancoDados.setMaquina(txtMaquina.getText());
		bancoDados.setPorta(txtPorta.getInt());
		bancoDados.setEndereco(txtEndereco.getText());
		bancoDados.setCaracteres(cbxCaracteres.getSelectedItem().toString());
		bancoDados.setUsuario(txtUsuario.getText());
		bancoDados.setSenha(new String(txtSenha.getPassword()));
		return bancoDados;
	}
	
	private void setBancoDados(BancoDados bancoDados) {
		if (bancoDados == null) {
			this.id = null;
			txtMaquina.setText(Constante.BD_MAQUINA_PADRAO);
			txtPorta.setText(String.valueOf(Constante.BD_PORTA_PADRAO));
			txtEndereco.setText(Constante.BD_ENDERECO_PADRAO);
			cbxCaracteres.setSelectedItem(Constante.BD_CARACTERES_PADRAO);
			txtUsuario.setText(Constante.BD_USUARIO_PADRAO);
			txtSenha.setText(Constante.BD_SENHA_PADRAO);
		} else {
			this.id = bancoDados.getId();
			txtNome.setText(bancoDados.getNome());
			txtMaquina.setText(bancoDados.getMaquina());
			txtPorta.setText(String.valueOf(bancoDados.getPorta()));
			txtEndereco.setText(bancoDados.getEndereco());
			cbxCaracteres.setSelectedItem(bancoDados.getCaracteres());
			txtUsuario.setText(bancoDados.getUsuario());
			txtSenha.setText(bancoDados.getSenha());
		}
	}
	
	private void verificaDados() throws Exception {
		txtNome.checkValue();
		txtMaquina.checkValue();
		txtPorta.checkValue();
		txtEndereco.checkValue();
		cbxCaracteres.checkValue();
		txtUsuario.checkValue();
		txtSenha.checkValue();
	}
	
	private boolean gravaDados() {
		try {
			verificaDados();
			setBancoDados(BancoDadosRN.grava(getBancoDados()));
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
