package gui;

import javax.swing.JToolBar;

import geral.Constante;
import gui.componente.DButton;

@SuppressWarnings("serial")
public abstract class PanelTarefaDetalheDestino extends PanelTarefaDetalhe {

	private DButton 
		btnPausar = new DButton(Constante.STR_PAUSAR, "pausar_24x24.png"),
		btnContinuar = new DButton(Constante.STR_CONTINUAR, "continuar_24x24.png");
	
	public PanelTarefaDetalheDestino(PanelTarefa panelTarefa) {
		super(panelTarefa);
		JToolBar toolBar = getToolBar();
		toolBar.add(btnPausar);
		toolBar.add(btnContinuar);
		atualizaTela();
	}

	protected DButton getBtnPausar() {
		return btnPausar;
	}
	
	protected DButton getBtnContinuar() {
		return btnContinuar;
	}
}
