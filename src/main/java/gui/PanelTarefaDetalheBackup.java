package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import geral.Constante;
import geral.Dialogo;
import geral.Util;
import gui.componente.DButton;
import gui.componente.DTable;
import modelo.Pasta;
import modelo.TarefaDestino;
import modelo.TarefaBackup;
import regra.TarefaBackupRN;

@SuppressWarnings("serial")
public class PanelTarefaDetalheBackup extends PanelTarefaDetalheDestino {
	
	private class TarefaBackupTableModel extends AbstractTableModel implements TableModel {

		private List<TarefaBackup> dados = new ArrayList<TarefaBackup>();

		@Override
		public int getRowCount() {
			return dados.size();
		}

		@Override
		public int getColumnCount() {
			return 4;
		}
		
		@Override
		public String getColumnName(int col) {
			switch (col) {
				case 0: return Constante.STR_NOME;
				case 1: return Constante.STR_MANTER;
				case 2: return Constante.STR_ESTADO;
				case 3: return Constante.STR_MENSAGEM;
				default: return null;
			}
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			TarefaBackup tr = dados.get(row);
			switch (col) {
				case 0: return tr.getPasta().getNome();
				case 1: return tr.getQtdManter();
				case 2: return tr.getDescricaoEstado();
				case 3: return tr.getMensagem();
				default: return null;
			}
		}

		public List<TarefaBackup> getDados() {
			return dados;
		}
	}

	private TarefaBackupTableModel tableModel = new TarefaBackupTableModel();
	private DButton btnAlterar = new DButton(Constante.STR_ALTERAR, "alterar_24x24.png");
	
	public PanelTarefaDetalheBackup(PanelTarefa panelTarefa) throws Exception {
		super(panelTarefa);
		preparaTabela();
		carregaDados();
		btnPausarActionListener();
		btnContinuarActionListener();
		getToolBar().add(btnAlterar, 2);
		btnAlterarActionListener();
	}
	
	private void btnAlterarActionListener() {
		btnAlterar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				int linha = getTabela().getSelectedRow();
				if (linha >= 0) {
					TarefaBackup tb = new TarefaBackup(tableModel.getDados().get(linha));
					String s = String.valueOf(tb.getQtdManter());
					s = JOptionPane.showInputDialog(Constante.STR_MANTER, s);
					if (!Util.isNullOrEmpty(s)) {
						try {
							int qtdManter = Integer.parseInt(s.trim());
							if (qtdManter <= 0)
								throw new Exception(String.format(Constante.STR_VALOR_INVALIDO, s));
							tb.setQtdManter(qtdManter);
							tableModel.getDados().set(linha, TarefaBackupRN.grava(tb));
							tableModel.fireTableRowsUpdated(linha, linha);
							atualizaTela();
						} catch(Exception e) {
							Dialogo.erro(e);
						}
					}
				}
			}
		});
	}
	
	private void alteraEstado(TarefaBackup.Estado estado) {
		int linha = getTabela().getSelectedRow();
		if (linha >= 0) {
			TarefaBackup tb = new TarefaBackup(tableModel.getDados().get(linha));
			tb.setEstado(estado);
			try {
				tableModel.getDados().set(linha, TarefaBackupRN.grava(tb));
				tableModel.fireTableRowsUpdated(linha, linha);
				atualizaTela();
			} catch(Exception e) {
				Dialogo.erro(e);
			}
		}
	}
	
	private void btnPausarActionListener() {
		getBtnPausar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				alteraEstado(TarefaBackup.Estado.PAUSADO);
			}
		});
	}
	
	private void btnContinuarActionListener() {
		getBtnContinuar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				alteraEstado(TarefaBackup.Estado.NORMAL);
			}
		});
	}
	
	private void carregaDados() throws Exception {
		Integer tarefaId = getPanelTarefa().getTarefa().getId();
		if (tarefaId != null) {
			tableModel.dados = TarefaBackupRN.buscaPorTarefaId(tarefaId);
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

		// Largura das colunas
		tcm.getColumn(0).setPreferredWidth(300);
		tcm.getColumn(1).setPreferredWidth(50);
		tcm.getColumn(2).setPreferredWidth(100);
		tcm.getColumn(3).setPreferredWidth(300);
		
		// Alinhamento das colunas
		DefaultTableCellRenderer centro = new DefaultTableCellRenderer();
		centro.setHorizontalAlignment(JLabel.CENTER);
		tcm.getColumn(1).setCellRenderer(centro);
		tcm.getColumn(2).setCellRenderer(centro);
	}
	
	@Override
	protected void insereRegistro() throws Exception {
		FormTabelaPasta f = new FormTabelaPasta();
		if (f.exibeModal() == JOptionPane.OK_OPTION) {
			Pasta pasta = f.getSelecionada();
			TarefaBackup tb = new TarefaBackup();
			tb.setTarefaId(getPanelTarefa().getTarefa().getId());
			tb.getPasta().setId(pasta.getId());
			tb.getPasta().setNome(pasta.getNome());
			tableModel.getDados().add(TarefaBackupRN.grava(tb));
			tableModel.fireTableDataChanged();
			getTabela().selectLastRow();
		}
		
	}

	@Override
	protected void excluiRegistro() throws Exception {
		DTable tabela = getTabela();
		int linha = tabela.getSelectedRow();
		if (linha >= 0) {
			TarefaBackupRN.exclui(tableModel.getDados().get(linha).getId());
			tableModel.getDados().remove(linha);
			tableModel.fireTableRowsDeleted(linha, linha);
			tabela.selectRow(linha);
		}
	}

	@Override
	protected void atualizaTela() {
		super.atualizaTela();
		DButton btnPausar = getBtnPausar(), btnContinuar = getBtnContinuar();
		int linha = getTabela().getSelectedRow();
		if (linha < 0) {
			if (btnAlterar != null) btnAlterar.setEnabled(false);
			btnPausar.setEnabled(false);
			btnContinuar.setEnabled(false);
		} else {
			if (btnAlterar != null) btnAlterar.setEnabled(true);
			TarefaBackup tr = tableModel.getDados().get(linha);
			btnPausar.setEnabled(tr.getEstado() != TarefaDestino.Estado.PAUSADO);
			btnContinuar.setEnabled(!btnPausar.isEnabled());
		}
	}

}
