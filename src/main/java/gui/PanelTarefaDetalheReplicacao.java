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

import firebird.FirebirdConexao;
import firebird.FirebirdReplicador;
import geral.Config;
import geral.Constante;
import geral.Dialogo;
import gui.componente.DButton;
import gui.componente.DTable;
import modelo.BancoDados;
import modelo.Tarefa;
import modelo.TarefaDestino;
import modelo.TarefaReplicacao;
import regra.BancoDadosRN;
import regra.TarefaReplicacaoRN;

@SuppressWarnings("serial")
public class PanelTarefaDetalheReplicacao extends PanelTarefaDetalheDestino {
	
	private class TarefaReplicacaoTableModel extends AbstractTableModel implements TableModel {

		private List<TarefaReplicacao> dados = new ArrayList<TarefaReplicacao>();

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
				case 1: return Constante.STR_ESTADO;
				case 2: return Constante.STR_MENSAGEM;
				default: return null;
			}
		}
		
		@Override
		public Object getValueAt(int row, int col) {
			TarefaReplicacao tr = dados.get(row);
			switch (col) {
				case 0: return tr.getBancoDados().getNome();
				case 1: return tr.getDescricaoEstado();
				case 2: return tr.getMensagem();
				default: return null;
			}
		}

		public List<TarefaReplicacao> getDados() {
			return dados;
		}
	}

	private TarefaReplicacaoTableModel tableModel = new TarefaReplicacaoTableModel();
	
	public PanelTarefaDetalheReplicacao(PanelTarefa panelTarefa) throws Exception {
		super(panelTarefa);
		preparaTabela();
		carregaDados();
		btnPausarActionListener();
		btnContinuarActionListener();
	}
	
	private void alteraEstado(TarefaReplicacao.Estado estado) {
		int linha = getTabela().getSelectedRow();
		if (linha >= 0) {
			TarefaReplicacao tr = tableModel.getDados().get(linha);
			tr.setEstado(estado);
			try {
				TarefaReplicacaoRN.grava(tr);
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
				alteraEstado(TarefaReplicacao.Estado.PAUSADO);
			}
		});
	}
	
	private void btnContinuarActionListener() {
		getBtnContinuar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				alteraEstado(TarefaReplicacao.Estado.NORMAL);
			}
		});
	}
	
	private void carregaDados() throws Exception {
		Integer tarefaId = getPanelTarefa().getTarefa().getId();
		if (tarefaId != null) {
			tableModel.dados = TarefaReplicacaoRN.buscaPorTarefaId(tarefaId);
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
		tcm.getColumn(0).setPreferredWidth(300);
		tcm.getColumn(1).setPreferredWidth(150);
		tcm.getColumn(2).setPreferredWidth(300);
	}

	@Override
	protected void insereRegistro() throws Exception {
		FormTabelaBancoDados f = new FormTabelaBancoDados();
		if (f.exibeModal() == JOptionPane.OK_OPTION) {
			Tarefa tarefa = getPanelTarefa().getTarefa();
			BancoDados bd = f.getSelecionado();
			if (bd.equals(tarefa.getBancoDados()))
				throw new Exception(Constante.STR_BANCO_DESTINO_IGUAL_ORIGEM);
			if (FirebirdConexao.bancoExiste(bd)) 
				throw new Exception(Constante.STR_BANCO_DESTINO_JA_EXISTE);
			TarefaReplicacao tr = new TarefaReplicacao();
			tr.setTarefaId(tarefa.getId());
			tr.getBancoDados().setId(bd.getId());
			tr.getBancoDados().setNome(bd.getNome());
			tableModel.getDados().add(TarefaReplicacaoRN.grava(tr));
			tableModel.fireTableDataChanged();
			getTabela().selectLastRow();
		}
		
	}

	@Override
	protected void excluiRegistro() throws Exception, Throwable {
		DTable tabela = getTabela();
		int linha = tabela.getSelectedRow();
		if (linha >= 0) {
			TarefaReplicacao tr = tableModel.getDados().get(linha);
			if (tr.getEstado() != TarefaReplicacao.Estado.PAUSADO) 
				throw new Exception(Constante.STR_PAUSAR_PARA_EXCLUIR);
			BancoDados bd = BancoDadosRN.buscaPorId(tr.getBancoDados().getId());
			TarefaReplicacaoRN.exclui(tr.getId());
			tableModel.getDados().remove(linha);
			tableModel.fireTableRowsDeleted(linha, linha);
			tabela.selectRow(linha);
			if (FirebirdConexao.bancoExiste(bd)) {
				Tarefa tarefa = getPanelTarefa().getTarefaEditada();
				BancoDados bdOrigem = BancoDadosRN.buscaPorId(tarefa.getBancoDados().getId());
				FirebirdReplicador replicador = new FirebirdReplicador(bdOrigem, bd, Config.getEnderecoGBak());
				replicador.despreparaDestino();
			}
		}
	}

	@Override
	protected void atualizaTela() {
		super.atualizaTela();
		DButton btnPausar = getBtnPausar(), btnContinuar = getBtnContinuar();
		int linha = getTabela().getSelectedRow();
		if (linha < 0) {
			btnPausar.setEnabled(false);
			btnContinuar.setEnabled(false);
		} else {
			TarefaReplicacao tr = tableModel.getDados().get(linha);
			btnPausar.setEnabled(tr.getEstado() != TarefaDestino.Estado.PAUSADO);
			btnContinuar.setEnabled(!btnPausar.isEnabled());
		}
	}

}
