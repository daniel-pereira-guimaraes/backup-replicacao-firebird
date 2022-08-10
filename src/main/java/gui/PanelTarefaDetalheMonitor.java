package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import geral.Constante;
import gui.componente.DTable;
import modelo.Monitor;
import modelo.TarefaMonitor;
import regra.TarefaMonitorRN;

@SuppressWarnings("serial")
public class PanelTarefaDetalheMonitor extends PanelTarefaDetalhe {
	
	private class TarefaMonitorTableModel extends AbstractTableModel implements TableModel {

		private List<TarefaMonitor> dados = new ArrayList<TarefaMonitor>();

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
			TarefaMonitor tm = dados.get(row);
			switch (col) {
				case 0: return tm.getMonitor().getNome();
				case 1: return tm.getMonitor().getChave();
				default: return null;
			}
		}

		public List<TarefaMonitor> getDados() {
			return dados;
		}
	}

	private TarefaMonitorTableModel tableModel = new TarefaMonitorTableModel();
	
	public PanelTarefaDetalheMonitor(PanelTarefa panelTarefa) throws Exception {
		super(panelTarefa);
		preparaTabela();
		carregaDados();
	}
	
	private void carregaDados() throws Exception {
		Integer tarefaId = getPanelTarefa().getTarefa().getId();
		if (tarefaId != null) {
			tableModel.dados = TarefaMonitorRN.buscaPorTarefaId(tarefaId);
			tableModel.fireTableDataChanged();
			getTabela().selectFirstRow();
		}
		atualizaTela();
	}
	
	@Override
	protected void preparaTabela() {
		DTable tabela = getTabela();
		tabela.setModel(tableModel);
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel tcm = tabela.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(400);
		tcm.getColumn(1).setPreferredWidth(300);
	}
	
	@Override
	protected void insereRegistro() throws Exception {
		FormTabelaMonitor f = new FormTabelaMonitor();
		if (f.exibeModal() == JOptionPane.OK_OPTION) {
			Monitor m = f.getSelecionado();
			TarefaMonitor tm = new TarefaMonitor();
			tm.setTarefaId(getPanelTarefa().getTarefa().getId());
			tm.getMonitor().setId(m.getId());
			tm.getMonitor().setNome(m.getNome());
			tableModel.getDados().add(TarefaMonitorRN.grava(tm));
			tableModel.fireTableDataChanged();
			getTabela().selectLastRow();
		}
	}

	@Override
	protected void excluiRegistro() throws Exception {
		DTable tabela = getTabela();
		int linha = tabela.getSelectedRow();
		if (linha >= 0) {
			TarefaMonitorRN.exclui(tableModel.getDados().get(linha).getId());
			tableModel.getDados().remove(linha);
			tableModel.fireTableRowsDeleted(linha, linha);
			tabela.selectRow(linha);
		}
	}

}
