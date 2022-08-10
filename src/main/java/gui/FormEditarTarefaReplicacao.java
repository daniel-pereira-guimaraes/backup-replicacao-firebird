package gui;

import javax.swing.JTabbedPane;

import geral.Constante;
import modelo.Tarefa;

@SuppressWarnings("serial")
public class FormEditarTarefaReplicacao extends FormEditarTarefa {

	private PanelTarefaDetalheReplicacao pnlTarefaReplicacao;
	private PanelTarefaDetalheMonitor pnlTarefaMonitor;
	
	public FormEditarTarefaReplicacao(Tarefa tarefa) throws Exception {
		super(tarefa);
		PanelTarefa panelTarefa = getPanelTarefa();
		pnlTarefaReplicacao = new PanelTarefaDetalheReplicacao(panelTarefa);
		pnlTarefaMonitor = new PanelTarefaDetalheMonitor(panelTarefa);
		JTabbedPane tabbedPane = getTabbedPane();
		tabbedPane.add(Constante.STR_DESTINOS, pnlTarefaReplicacao);
		tabbedPane.add(Constante.STR_MONITORES, pnlTarefaMonitor);
	}

}
