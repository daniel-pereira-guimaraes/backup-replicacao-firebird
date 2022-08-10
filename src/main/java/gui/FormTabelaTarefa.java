package gui;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import geral.Constante;
import geral.Dialogo;
import gui.componente.DTable;
import modelo.Tarefa;
import regra.TarefaRN;

@SuppressWarnings("serial")
public class FormTabelaTarefa extends FormTabelaSimples {

	private class TarefaTableModel extends AbstractTableModel implements TableModel {
				
		private List<Tarefa> dados = new ArrayList<Tarefa>();
		
		public List<Tarefa> getDados() {
			return dados;
		}

		public void setDados(List<Tarefa> dados) {
			this.dados = dados;
		}

		@Override
		public int getRowCount() {
			return dados.size();
		}
		
		@Override
		public int getColumnCount() {
			return 3;
		}
		
		@Override
		public String getColumnName(int col) {
			switch (col) {
				case 0: return Constante.STR_NOME;
				case 1: return Constante.STR_TIPO; 
				case 2: return Constante.STR_BANCO_DE_DADOS;
				default: return null;
			}
		}

		@Override
		public Object getValueAt(int row, int col) {
			Tarefa tarefa = dados.get(row);
			switch (col) {
				case 0: return tarefa.getNome();
				case 1: return tarefa.getTipo().toString(); 
				case 2: return tarefa.getBancoDados().getNome();
				default: return null;
			}
		}
		
	}
			
	TarefaTableModel modelo;
	
	public FormTabelaTarefa() {
		super();
		setTitle(Constante.STR_TAREFAS);
		setSize(700, 400);
		setLocationRelativeTo(null);
		///setIconImage(Util.getResourceAsImage("tarefa_48x48.png"));
	}
	
	@Override
	protected void preparaTabela() {
		modelo = new TarefaTableModel();
		JTable tabela = getTabela();
		tabela.setModel(modelo);
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel tcm = tabela.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(300);
		tcm.getColumn(1).setPreferredWidth(100);
		tcm.getColumn(2).setPreferredWidth(200);
	}
	
	@Override
	protected void carregaDados() {
		try {
			modelo.setDados(TarefaRN.buscaPorNome(getCampoPesquisa().getText()));
			modelo.fireTableDataChanged();
			getTabela().selectFirstRow();
			atualizaTela();
		} catch (Exception e) {
			Dialogo.erro(e);
		}
	}
	
	@Override
	protected void insereRegistro() throws Exception {
		FormEditarTarefa f;
		Tarefa.Tipo tipo = Dialogo.tipoTarefaDlg(Tarefa.Tipo.BACKUP);
		if (tipo != null) {
			Tarefa tarefa = new Tarefa(tipo);
			if (tipo == Tarefa.Tipo.BACKUP)
				f = new FormEditarTarefaBackup(tarefa);
			else
				f = new FormEditarTarefaReplicacao(tarefa);
			f.exibeModal();
			carregaDados();
		}
	}
	
	@Override
	protected void alteraRegistro() throws Exception {
		int linha = getTabela().getSelectedRow();
		if (linha >= 0) {
			FormEditarTarefa f;
			Tarefa tarefa = modelo.getDados().get(linha);
			if (tarefa.getTipo() == Tarefa.Tipo.BACKUP)
				f = new FormEditarTarefaBackup(tarefa);
			else
				f = new FormEditarTarefaReplicacao(tarefa);
			f.exibeModal();
			carregaDados();
		}
	}
	
	@Override
	protected void excluiRegistro() throws Exception {
		DTable tabela = getTabela();
		int linha = tabela.getSelectedRow();
		if (linha >= 0) {
			if (Dialogo.confirmacao(Constante.STR_CONF_EXCLUIR_REG)) {
				TarefaRN.exclui(modelo.getDados().get(linha).getId());
				modelo.getDados().remove(linha);
				modelo.fireTableRowsDeleted(linha, linha);
				tabela.selectRow(linha);
				atualizaTela();
			}
		} else
			Dialogo.informacao(Constante.STR_SELECIONE_LINHA);
	}

	public Tarefa getSelecionado() {
		int linha = getTabela().getSelectedRow();
		if (linha >= 0)
			return modelo.getDados().get(linha);
		else
			return null;
	}
	
}
