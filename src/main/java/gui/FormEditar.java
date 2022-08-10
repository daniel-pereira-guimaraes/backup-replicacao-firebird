package gui;

import geral.Constante;
import geral.Util;

@SuppressWarnings("serial")
public abstract class FormEditar extends FormOkCancelDlg {

	public FormEditar(boolean inserir) {
		super();
		if (inserir) {
			setTitle(Constante.STR_INSERIR);
			setIconImage(Util.getResourceAsImage("inserir_24x24.png"));
		} else {
			setTitle(Constante.STR_ALTERAR);
			setIconImage(Util.getResourceAsImage("alterar_24x24.png"));
		}
		
	}
}
