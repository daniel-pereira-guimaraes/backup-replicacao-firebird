package gui;

import javax.swing.JTabbedPane;

import geral.Constante;
import modelo.Tarefa;

@SuppressWarnings("serial")
public class FormEditarTarefaBackup extends FormEditarTarefa {

	private PanelTarefaDetalheBackup pnlTarefaBackup;
	private PanelTarefaDetalheMonitor pnlTarefaMonitor;
	
	public FormEditarTarefaBackup(Tarefa tarefa) throws Exception {
		super(tarefa);
		PanelTarefa panelTarefa = getPanelTarefa();
		pnlTarefaBackup = new PanelTarefaDetalheBackup(panelTarefa);
		pnlTarefaMonitor = new PanelTarefaDetalheMonitor(panelTarefa);
		JTabbedPane tabbedPane = getTabbedPane();
		tabbedPane.add(Constante.STR_DESTINOS, pnlTarefaBackup);
		tabbedPane.add(Constante.STR_MONITORES, pnlTarefaMonitor);
	}

}
