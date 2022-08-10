package gui;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import gui.componente.DTextField;

@SuppressWarnings("serial")
public abstract class FormTabelaSimples extends FormTabelaEditavel {

	private DTextField txtPesquisar = new DTextField();

	public FormTabelaSimples() {
		super();
		getPainelPesquisa().add(txtPesquisar, 0);
		txtPesquisar.setPreferredSize(new Dimension(200, 25));
		carregaDados();
		getTabela().selectFirstRow();
		txtPesquisarKeyListener();
	}
	
	private void txtPesquisarKeyListener() {
		txtPesquisar.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()  == KeyEvent.VK_ENTER)
					getBtnPesquisar().doClick();
			}
			@Override
			public void keyPressed(KeyEvent e) {}
		});
	}

	@Override
	protected void preparaPainelPesquisa() {
		getPainelPesquisa().add(txtPesquisar);
		super.preparaPainelPesquisa();
	}
		
	protected DTextField getCampoPesquisa() {
		return txtPesquisar;
	}
}
