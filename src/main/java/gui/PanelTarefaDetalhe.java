package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;

import geral.Constante;
import geral.Dialogo;
import gui.componente.DButton;
import gui.componente.DTable;

@SuppressWarnings("serial")
public abstract class PanelTarefaDetalhe extends JPanel {

	private JToolBar toolBar = new JToolBar();
	private DButton 
		btnInserir = new DButton(Constante.STR_INSERIR, "inserir_24x24.png"),
		btnExcluir = new DButton(Constante.STR_EXCLUIR, "excluir_24x24.png");
	
	private DTable tabela = new DTable();
	private JScrollPane spTabela = new JScrollPane();
	private PanelTarefa panelTarefa;
			
	public PanelTarefaDetalhe(PanelTarefa panelTarefa) {
		super();
		this.panelTarefa = panelTarefa;
		setLayout(new BorderLayout());
		add(toolBar, BorderLayout.NORTH);
		add(spTabela, BorderLayout.CENTER);
		
		toolBar.add(btnInserir);
		toolBar.add(btnExcluir);
		
		tabela.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		spTabela.setViewportView(tabela);
		spTabela.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

		tabelaSelectionListener();
		btnInserirActionListener();
		btnExcluirActionListener();	
	}
		
	private void tabelaSelectionListener() {
	    tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				atualizaTela();
			}
		});
	}
	
	private void btnInserirActionListener() {
		btnInserir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					panelTarefa.verificaTarefa();
					insereRegistro();
					atualizaTela();
				} catch(Throwable e) {
					Dialogo.erro(e);
				}
			}
		});
	}
	
	private void btnExcluirActionListener() {
		btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					if (Dialogo.confirmacao(Constante.STR_CONF_EXCLUIR_REG)) { 
						excluiRegistro();
						atualizaTela();
					}
				} catch(Throwable e) {
					Dialogo.erro(e);
				}
			}
		});
	}
	
	protected JToolBar getToolBar() {
		return toolBar;
	}
	
	protected DButton getBtnExcluir() {
		return btnExcluir;
	}
	
	protected DTable getTabela() {
		return tabela;
	}
	
	protected PanelTarefa getPanelTarefa() {
		return panelTarefa;
	}
	
	protected void atualizaTela() {
		TableModel tableModel = tabela.getModel();
		btnInserir.setEnabled(tableModel != null);
		btnExcluir.setEnabled(tableModel != null && tableModel.getRowCount() > 0);
	}
	
	protected abstract void preparaTabela();
	protected abstract void insereRegistro() throws Throwable;
	protected abstract void excluiRegistro() throws Throwable;
}
