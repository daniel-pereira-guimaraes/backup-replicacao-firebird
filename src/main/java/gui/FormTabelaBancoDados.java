package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import geral.Constante;
import geral.Dialogo;
import geral.Util;
import gui.componente.DButton;
import gui.componente.DTable;
import modelo.BancoDados;
import regra.BancoDadosRN;

@SuppressWarnings("serial")
public class FormTabelaBancoDados extends FormTabelaSimples {

	private class BancoDadosTableModel extends AbstractTableModel implements TableModel {
				
		private List<BancoDados> dados = new ArrayList<BancoDados>();
		
		public List<BancoDados> getDados() {
			return dados;
		}

		public void setDados(List<BancoDados> dados) {
			this.dados = dados;
		}

		@Override
		public int getRowCount() {
			return dados.size();
		}
		
		@Override
		public int getColumnCount() {
			return 6;
		}
		
		@Override
		public String getColumnName(int col) {
			switch (col) {
				case 0: return Constante.STR_NOME; 
				case 1: return Constante.STR_MAQUINA;
				case 2: return Constante.STR_PORTA;
				case 3: return Constante.STR_ENDERECO;
				case 4: return Constante.STR_CARACTERES; 
				case 5: return Constante.STR_USUARIO;
				default: return null;
			}
		}

		@Override
		public Object getValueAt(int row, int col) {
			BancoDados bancoDados = dados.get(row);
			switch (col) {
				case 0: return bancoDados.getNome(); 
				case 1: return bancoDados.getMaquina(); 
				case 2: return bancoDados.getPorta(); 
				case 3: return bancoDados.getEndereco(); 
				case 4: return bancoDados.getCaracteres(); 
				case 5: return bancoDados.getUsuario();
				default: return null;
			}
		}
		
	}
			
	private BancoDadosTableModel tableModel;
	private DButton btnTestarAcesso = new DButton(Constante.STR_TESTAR_ACESSO, "testar_24x24.png", true);
	
	public FormTabelaBancoDados() {
		super();
		setTitle(Constante.STR_BANCOS_DE_DADOS);
		setSize(950, 400);
		setLocationRelativeTo(null);
		setIconImage(Util.getResourceAsImage("bancoDados_48x48.png"));
		getToolBar().add(btnTestarAcesso);
		btnTestarAcessoActionListener();
	}
	
	private void btnTestarAcessoActionListener() {
		btnTestarAcesso.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					BancoDados bd = getSelecionado();
					BancoDadosRN.testaConexao(bd);
					Dialogo.informacao(String.format(Constante.STR_ACESSO_COM_SUCESSO, bd.getNome()));
				} catch(Throwable e) {
					Dialogo.erro(e);
				}
			}
		});
	}
	
	@Override
	public void atualizaTela() {
		super.atualizaTela();
		if (btnTestarAcesso != null)
			btnTestarAcesso.setEnabled(getTabela().getSelectedRow() >= 0);
	}
	
	@Override
	protected void preparaTabela() {
		tableModel = new BancoDadosTableModel();
		DTable tabela = getTabela();
		tabela.setModel(tableModel);
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel tcm = tabela.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(300);
		tcm.getColumn(1).setPreferredWidth(150);
		tcm.getColumn(2).setPreferredWidth(50);
		tcm.getColumn(3).setPreferredWidth(200);
		tcm.getColumn(4).setPreferredWidth(80);
		tcm.getColumn(5).setPreferredWidth(100);
	}
	
	@Override
	protected void carregaDados() {
		try {
			tableModel.setDados(BancoDadosRN.buscaPorNome(getCampoPesquisa().getText()));
			tableModel.fireTableDataChanged();
			getTabela().selectFirstRow();
			atualizaTela();
		} catch (Exception e) {
			Dialogo.erro(e);
		}
	}
	
	@Override
	protected void insereRegistro() throws Exception {
		FormEditarBancoDados f = new FormEditarBancoDados();
		if (f.exibeModal() == JOptionPane.OK_OPTION) {
			try {
				tableModel.getDados().add(f.getBancoDados());
				tableModel.fireTableDataChanged();
				getTabela().selectLastRow();
				atualizaTela();
			} catch (Exception e) {
				Dialogo.erro(e);
			}
		}
	}
	
	@Override
	protected void alteraRegistro() throws Exception {
		int linha = getTabela().getSelectedRow();
		if (linha >= 0) {
			FormEditarBancoDados f = new FormEditarBancoDados(tableModel.getDados().get(linha));
			if (f.exibeModal() == JOptionPane.OK_OPTION) {
				tableModel.getDados().set(linha, f.getBancoDados());
				tableModel.fireTableRowsUpdated(linha, linha);
				atualizaTela();
			}
		}
	}
	
	@Override
	protected void excluiRegistro() throws Exception {
		DTable tabela = getTabela();
		int linha = tabela.getSelectedRow();
		if (linha >= 0) {
			if (Dialogo.confirmacao(Constante.STR_CONF_EXCLUIR_REG)) {
				BancoDadosRN.exclui(tableModel.getDados().get(linha).getId());
				tableModel.getDados().remove(linha);
				tableModel.fireTableRowsDeleted(linha, linha);
				tabela.selectRow(linha);
				atualizaTela();
			}
		} else
			Dialogo.informacao(Constante.STR_SELECIONE_LINHA);
	}
	
	public BancoDados getSelecionado() {
		int linha = getTabela().getSelectedRow();
		if (linha >= 0)
			return tableModel.getDados().get(linha);
		else
			return null;
	}

}
