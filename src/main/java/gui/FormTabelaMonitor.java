package gui;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import geral.Constante;
import geral.Dialogo;
import geral.Monitoramento;
import geral.Util;
import gui.componente.DTable;
import modelo.Monitor;
import regra.MonitorRN;

@SuppressWarnings("serial")
public class FormTabelaMonitor extends FormTabelaSimples {

	private class MonitorTableModel extends AbstractTableModel implements TableModel {
				
		private List<Monitor> dados = new ArrayList<Monitor>();
		
		public List<Monitor> getDados() {
			return dados;
		}

		public void setDados(List<Monitor> dados) {
			this.dados = dados;
		}

		@Override
		public int getRowCount() {
			return dados.size();
		}
		
		@Override
		public int getColumnCount() {
			return 2;
		}
		
		@Override
		public String getColumnName(int col) {
			switch (col) {
				case 0: return Constante.STR_NOME; 
				case 1: return Constante.STR_CHAVE;
				default: return null;
			}
		}

		@Override
		public Object getValueAt(int row, int col) {
			Monitor bd = dados.get(row);
			switch (col) {
				case 0: return bd.getNome(); 
				case 1: return bd.getChave(); 
				default: return null;
			}
		}
		
	}
			
	MonitorTableModel modelo;
	
	public FormTabelaMonitor() {
		super();
		setTitle(Constante.STR_MONITORES);
		setSize(700, 400);
		setLocationRelativeTo(null);
		setIconImage(Util.getResourceAsImage("monitor_48x48.png"));
	}
	
	@Override
	protected void preparaTabela() {
		modelo = new MonitorTableModel();
		JTable tabela = getTabela();
		tabela.setModel(modelo);
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel tcm = tabela.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(300);
		tcm.getColumn(1).setPreferredWidth(200);
	}
	
	@Override
	protected void carregaDados() {
		try {
			modelo.setDados(MonitorRN.buscaPorNome(getCampoPesquisa().getText()));
			modelo.fireTableDataChanged();
			getTabela().selectFirstRow();
			atualizaTela();
		} catch (Exception e) {
			Dialogo.erro(e);
		}
	}
	
	@Override
	protected void insereRegistro() throws Exception {
		FormEditarMonitor f = new FormEditarMonitor();
		if (f.exibeModal() == JOptionPane.OK_OPTION) {
			try {
				modelo.getDados().add(f.getMonitor());
				modelo.fireTableDataChanged();
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
			FormEditarMonitor f = new FormEditarMonitor(modelo.getDados().get(linha));
			if (f.exibeModal() == JOptionPane.OK_OPTION) {
				modelo.getDados().set(linha, f.getMonitor());
				modelo.fireTableRowsUpdated(linha, linha);
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
				Monitor monitor = modelo.getDados().get(linha);
				MonitorRN.exclui(monitor.getId());
				modelo.getDados().remove(linha);
				modelo.fireTableRowsDeleted(linha, linha);
				tabela.selectRow(linha);
				atualizaTela();
				Monitoramento.removeMonitor(monitor.getChave());
			}
		} else
			Dialogo.informacao(Constante.STR_SELECIONE_LINHA);
	}

	public Monitor getSelecionado() {
		int linha = getTabela().getSelectedRow();
		if (linha >= 0)
			return modelo.getDados().get(linha);
		else
			return null;
	}
	
}
