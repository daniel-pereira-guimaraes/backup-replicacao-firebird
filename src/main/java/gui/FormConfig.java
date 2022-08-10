package gui;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import geral.Config;
import geral.Constante;
import geral.Dialogo;
import gui.componente.DButton;
import gui.componente.DIntField;
import gui.componente.DPasswordField;
import gui.componente.DTextField;

@SuppressWarnings("serial")
public class FormConfig extends FormOkCancelDlg {

	private JLabel
		lblEnderecoGBak = new JLabel(Constante.STR_ENDERECO_DO_GBAK),
		lblLoteReplicacao = new JLabel(Constante.STR_LOTE_REPLICACAO),
		lblLoteReplicacaoTransacoes = new JLabel(Constante.STR_TRANSACOES),
		lblIntervaloNotificacao = new JLabel(Constante.STR_INTERVALO_NOTIFICACAO),
		lblIntervaloNotificacaoMinutos = new JLabel(Constante.STR_MINUTOS),
		lblConexaoUsuario = new JLabel(Constante.STR_USUARIO_CONEXAO_LOCAL),
		lblConexaoSenha = new JLabel(Constante.STR_SENHA_CONEXAO_LOCAL);
	
	private DTextField
		txtEnderecoGBak = new DTextField(true, Constante.STR_ENDERECO_DO_GBAK),
		txtConexaoUsuario = new DTextField(true, Constante.STR_USUARIO_CONEXAO_LOCAL);
	
	private DPasswordField
		txtConexaoSenha = new DPasswordField(true, Constante.STR_SENHA_CONEXAO_LOCAL);
	
	private DIntField
		txtLoteReplicacao = new DIntField(true, Constante.STR_LOTE_REPLICACAO),
		txtIntervaloNotificacao = new DIntField(true, Constante.STR_INTERVALO_NOTIFICACAO);
	
	private DButton
		btnEnderecoGBak = new DButton(Constante.STR_PROCURAR);
				
	public FormConfig() throws InvalidPropertiesFormatException, FileNotFoundException, IOException  {
		setTitle(Constante.STR_CONFIGURACOES);
		setSize(600, 300);
		setResizable(false);
		setLocationRelativeTo(null);

		SpringLayout sl = new SpringLayout();
		JPanel p = getPnlPrincipal();
		p.setLayout(sl);
		
		p.add(lblEnderecoGBak);
		p.add(txtEnderecoGBak);
		p.add(btnEnderecoGBak);
		
		p.add(lblLoteReplicacao);
		p.add(txtLoteReplicacao);
		p.add(lblLoteReplicacaoTransacoes);
		
		p.add(lblIntervaloNotificacao);
		p.add(txtIntervaloNotificacao);
		p.add(lblIntervaloNotificacaoMinutos);
		
		p.add(lblConexaoUsuario);
		p.add(txtConexaoUsuario);

		p.add(lblConexaoSenha);
		p.add(txtConexaoSenha);

		sl.putConstraint(SpringLayout.NORTH, btnEnderecoGBak, 20, SpringLayout.NORTH, p);
		sl.putConstraint(SpringLayout.EAST, btnEnderecoGBak, -20, SpringLayout.EAST, p);
		
		sl.putConstraint(SpringLayout.NORTH, txtEnderecoGBak, 0, SpringLayout.NORTH, btnEnderecoGBak);
		sl.putConstraint(SpringLayout.SOUTH, txtEnderecoGBak, 0, SpringLayout.SOUTH, btnEnderecoGBak);
		sl.putConstraint(SpringLayout.EAST, txtEnderecoGBak, -10, SpringLayout.WEST, btnEnderecoGBak);
		sl.putConstraint(SpringLayout.WEST, txtEnderecoGBak, 200, SpringLayout.WEST, p);

		sl.putConstraint(SpringLayout.BASELINE, lblEnderecoGBak, 0, SpringLayout.BASELINE, txtEnderecoGBak);
		sl.putConstraint(SpringLayout.WEST, lblEnderecoGBak, 20, SpringLayout.WEST, p);
		
		sl.putConstraint(SpringLayout.NORTH, txtLoteReplicacao, 10, SpringLayout.SOUTH, txtEnderecoGBak);
		sl.putConstraint(SpringLayout.WEST, txtLoteReplicacao, 0, SpringLayout.WEST, txtEnderecoGBak);

		sl.putConstraint(SpringLayout.BASELINE, lblLoteReplicacao, 0, SpringLayout.BASELINE, txtLoteReplicacao);
		sl.putConstraint(SpringLayout.WEST, lblLoteReplicacao, 0, SpringLayout.WEST, lblEnderecoGBak);

		sl.putConstraint(SpringLayout.BASELINE, lblLoteReplicacaoTransacoes, 0, SpringLayout.BASELINE, txtLoteReplicacao);
		sl.putConstraint(SpringLayout.WEST, lblLoteReplicacaoTransacoes, 5, SpringLayout.EAST, txtLoteReplicacao);
		
		sl.putConstraint(SpringLayout.NORTH, txtIntervaloNotificacao, 10, SpringLayout.SOUTH, txtLoteReplicacao);
		sl.putConstraint(SpringLayout.WEST, txtIntervaloNotificacao, 0, SpringLayout.WEST, txtLoteReplicacao);

		sl.putConstraint(SpringLayout.BASELINE, lblIntervaloNotificacao, 0, SpringLayout.BASELINE, txtIntervaloNotificacao);
		sl.putConstraint(SpringLayout.WEST, lblIntervaloNotificacao, 0, SpringLayout.WEST, lblLoteReplicacao);
		
		sl.putConstraint(SpringLayout.BASELINE, lblIntervaloNotificacaoMinutos, 0, SpringLayout.BASELINE, txtIntervaloNotificacao);
		sl.putConstraint(SpringLayout.WEST, lblIntervaloNotificacaoMinutos, 5, SpringLayout.EAST, txtIntervaloNotificacao);

		
		sl.putConstraint(SpringLayout.NORTH, txtConexaoUsuario, 10, SpringLayout.SOUTH, txtIntervaloNotificacao);
		sl.putConstraint(SpringLayout.WEST, txtConexaoUsuario, 0, SpringLayout.WEST, txtIntervaloNotificacao);

		sl.putConstraint(SpringLayout.BASELINE, lblConexaoUsuario, 0, SpringLayout.BASELINE, txtConexaoUsuario);
		sl.putConstraint(SpringLayout.WEST, lblConexaoUsuario, 0, SpringLayout.WEST, lblIntervaloNotificacao);

		sl.putConstraint(SpringLayout.NORTH, txtConexaoSenha, 10, SpringLayout.SOUTH, txtConexaoUsuario);
		sl.putConstraint(SpringLayout.WEST, txtConexaoSenha, 0, SpringLayout.WEST, txtConexaoUsuario);

		sl.putConstraint(SpringLayout.BASELINE, lblConexaoSenha, 0, SpringLayout.BASELINE, txtConexaoSenha);
		sl.putConstraint(SpringLayout.WEST, lblConexaoSenha, 0, SpringLayout.WEST, lblConexaoUsuario);
		
		txtEnderecoGBak.setPreferredSize(new Dimension(300, 25));
		btnEnderecoGBak.setPreferredSize(new Dimension(100, 25));
		txtLoteReplicacao.setPreferredSize(new Dimension(100, 25));
		txtIntervaloNotificacao.setPreferredSize(txtLoteReplicacao.getPreferredSize());
		txtConexaoUsuario.setPreferredSize(new Dimension(150, 25));
		txtConexaoSenha.setPreferredSize(new Dimension(150, 25));
		
		btnEnderecoGBakActionListener();
		carregaDados();
	}
	
	private void btnEnderecoGBakActionListener() {
		btnEnderecoGBak.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File(txtEnderecoGBak.getText()));
				if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					txtEnderecoGBak.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
	}
	
	private void carregaDados() throws InvalidPropertiesFormatException, FileNotFoundException, IOException {
		txtEnderecoGBak.setText(Config.getEnderecoGBak());
		txtLoteReplicacao.setInt(Config.getLoteReplicacao());
		txtIntervaloNotificacao.setInt(Config.getIntervaloNotificacao() / 1000 / 60);
		txtConexaoUsuario.setText(Config.getConexaoUsuario());
		txtConexaoSenha.setText(Config.getConexaoSenha());
	}
	
	private void verificaDados() throws Exception {
		txtEnderecoGBak.checkValue();
		txtLoteReplicacao.checkValue();
		txtIntervaloNotificacao.checkValue();
		txtConexaoUsuario.checkValue();
		txtConexaoSenha.checkValue();
	}

	private boolean gravaDados() {
		try {
			verificaDados();
			Config.setEnderecoGBak(txtEnderecoGBak.getText());
			Config.setLoteReplicacao(txtLoteReplicacao.getInt());
			Config.setIntervaloNotificacao(txtIntervaloNotificacao.getInt() * 1000 * 60);
			Config.setConexaoUsuario(txtConexaoUsuario.getText());
			Config.setConexaoSenha(new String(txtConexaoSenha.getPassword()));
			return true;
		} catch (Exception e) {
			Dialogo.erro(e);
		}
		return false;
	}
		
	@Override
	protected boolean podeFechar(int resultado) {
		return (resultado != JOptionPane.OK_OPTION || gravaDados());
	}

}


