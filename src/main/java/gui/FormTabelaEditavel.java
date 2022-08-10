package gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JToolBar;

import geral.Constante;
import geral.Dialogo;
import gui.componente.DButton;

@SuppressWarnings("serial")
public abstract class FormTabelaEditavel extends FormTabela {
	
	private DButton 
		btnInserir = new DButton(Constante.STR_INSERIR, "inserir_24x24.png", true),
		btnAlterar = new DButton(Constante.STR_ALTERAR, "alterar_24x24.png", true),
		btnExcluir = new DButton(Constante.STR_EXCLUIR, "excluir_24x24.png", true);
		
	public FormTabelaEditavel() {
		super();
		
		JToolBar toolBar = getToolBar();
		toolBar.add(btnInserir);
		toolBar.add(btnAlterar);
		toolBar.add(btnExcluir);

		btnInserir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					insereRegistro();
				} catch(Exception e) {
					Dialogo.erro(e);
				}
			}
		});
		
		btnAlterar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					alteraRegistro();
				} catch(Exception e) {
					Dialogo.erro(e);
				}
			}
		});
		
		btnExcluir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					excluiRegistro();
				} catch(Exception e) {
					Dialogo.erro(e);
				}
				
			}
		});
		
		getTabela().addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent ke) {}
			@Override
			public void keyReleased(KeyEvent ke) {}
			@Override
			public void keyPressed(KeyEvent ke) {
				try {
					switch (ke.getKeyCode()) {
						case KeyEvent.VK_INSERT:
							if (btnInserir.isEnabled()) {
								insereRegistro();
								ke.consume();
							}
							break;
						case KeyEvent.VK_ENTER:
							if (!isModal() && btnAlterar.isEnabled()) {
								alteraRegistro();
								ke.consume();
							}
							break;
						case KeyEvent.VK_DELETE:
							if (btnExcluir.isEnabled()) {
								excluiRegistro();
								ke.consume();
							}
							break;
					}
				} catch (Exception e) {
					Dialogo.erro(e);
				}
			}
		});
		
		getTabela().addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent me) {}
			@Override
			public void mousePressed(MouseEvent me) {}
			@Override
			public void mouseExited(MouseEvent me) {}
			@Override
			public void mouseEntered(MouseEvent me) {}
			@Override
			public void mouseClicked(MouseEvent me) {
				if (!me.isConsumed() &&
					me.getButton() == MouseEvent.BUTTON1 && 
					me.getClickCount() == 2 && 
					btnAlterar.isEnabled()) {
					try {
						alteraRegistro();
					} catch (Exception e) {
						Dialogo.erro(e);
					}
				}
				
			}
		});
	}

	protected DButton getBtnAlterar() {
		return btnAlterar;
	}
	
	protected DButton getBtnExcluir() {
		return btnExcluir;
	}
	
	@Override
	protected void onShow() {
		super.onShow();
		getTabela().grabFocus();
	}
	
	protected abstract void insereRegistro() throws Exception;
	protected abstract void alteraRegistro() throws Exception;
	protected abstract void excluiRegistro() throws Exception;
			
	@Override
	public void atualizaTela() {
		super.atualizaTela();
		boolean temSelecao = getTabela().getSelectedRow() >= 0;
		btnAlterar.setEnabled(temSelecao);
		btnExcluir.setEnabled(temSelecao);
	}
	
}
