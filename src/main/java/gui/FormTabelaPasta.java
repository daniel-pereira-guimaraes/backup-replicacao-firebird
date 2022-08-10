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
import modelo.Pasta;
import regra.PastaRN;

@SuppressWarnings("serial")
public class FormTabelaPasta extends FormTabelaSimples {

	private class PastaTableModel extends AbstractTableModel implements TableModel {
				
		private List<Pasta> dados = new ArrayList<Pasta>();
		
		public List<Pasta> getDados() {
			return dados;
		}

		public void setDados(List<Pasta> dados) {
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
				case 1: return Constante.STR_TIPO; 
				case 2: return Constante.STR_MAQUINA;
				case 3: return Constante.STR_PORTA;
				case 4: return Constante.STR_ENDERECO;
				case 5: return Constante.STR_USUARIO;
				default: return null;
			}
		}

		@Override
		public Object getValueAt(int row, int col) {
			Pasta pasta = dados.get(row);
			switch (col) {
				case 0: return pasta.getNome(); 
				case 1: return pasta.getTipo(); 
				case 2: return pasta.getMaquina(); 
				case 3: return pasta.getPorta(); 
				case 4: return pasta.getEndereco(); 
				case 5: return pasta.getUsuario();
				default: return null;
			}
		}
		
	}
			
	PastaTableModel tableModel;
	private DButton btnTestarAcesso = new DButton(Constante.STR_TESTAR_ACESSO, "testar_24x24.png", true);
	
	public FormTabelaPasta() {
		super();
		setTitle(Constante.STR_PASTAS_PARA_BACKUP);
		setSize(950, 400);
		setLocationRelativeTo(null);
		setIconImage(Util.getResourceAsImage("pasta_48x48.png"));
		getToolBar().add(btnTestarAcesso);
		btnTestarAcessoActionListener();
	}
	
	private void btnTestarAcessoActionListener() {
		btnTestarAcesso.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					Pasta p = getSelecionada();
					PastaRN.testaAcesso(p);
					Dialogo.informacao(String.format(Constante.STR_ACESSO_COM_SUCESSO, p.getNome()));
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
		tableModel = new PastaTableModel();
		JTable tabela = getTabela();
		tabela.setModel(tableModel);
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel tcm = tabela.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(300);
		tcm.getColumn(1).setPreferredWidth(80);
		tcm.getColumn(2).setPreferredWidth(150);
		tcm.getColumn(3).setPreferredWidth(50);
		tcm.getColumn(4).setPreferredWidth(200);
		tcm.getColumn(5).setPreferredWidth(100);
	}
	
	@Override
	protected void carregaDados() {
		try {
			tableModel.setDados(PastaRN.buscaPorNome(getCampoPesquisa().getText()));
			tableModel.fireTableDataChanged();
			getTabela().selectFirstRow();
			atualizaTela();
		} catch (Exception e) {
			Dialogo.erro(e);
		}
	}
	
	@Override
	protected void insereRegistro() throws Exception {
		FormEditarPasta f = new FormEditarPasta();
		if (f.exibeModal() == JOptionPane.OK_OPTION) {
			try {
				tableModel.getDados().add(f.getPasta());
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
			FormEditarPasta f = new FormEditarPasta(tableModel.getDados().get(linha));
			if (f.exibeModal() == JOptionPane.OK_OPTION) {
				tableModel.getDados().set(linha, f.getPasta());
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
				PastaRN.exclui(tableModel.getDados().get(linha).getId());
				tableModel.getDados().remove(linha);
				tableModel.fireTableRowsDeleted(linha, linha);
				tabela.selectRow(linha);
				atualizaTela();
			}
		} else
			Dialogo.informacao(Constante.STR_SELECIONE_LINHA);
	}
	
	public Pasta getSelecionada() {
		int linha = getTabela().getSelectedRow();
		if (linha >= 0)
			return tableModel.getDados().get(linha);
		else
			return null;
	}

}
