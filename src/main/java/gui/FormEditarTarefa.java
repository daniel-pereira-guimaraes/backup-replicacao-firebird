package gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JTabbedPane;

import geral.Constante;
import modelo.Tarefa;

@SuppressWarnings("serial")
public class FormEditarTarefa extends FormBase {

	private PanelTarefa pnlTarefa;
	private JTabbedPane tabbedPane = new JTabbedPane();
	
	public FormEditarTarefa(Tarefa tarefa) throws Exception {
		super();
		setTitle(Constante.STR_TAREFA);
		setSize(new Dimension(800, 600));
		setLocationRelativeTo(null);
		
		pnlTarefa = new PanelTarefa(tarefa);

		Container ContentPane = getContentPane();
		ContentPane.add(pnlTarefa, BorderLayout.NORTH);
		ContentPane.add(tabbedPane, BorderLayout.CENTER);
	}
	
	public Tarefa getTarefa() {
		return pnlTarefa.getTarefa();
	}
	
	protected JTabbedPane getTabbedPane() {
		return tabbedPane;
	}
	
	protected PanelTarefa getPanelTarefa() {
		return pnlTarefa;
	}
}
