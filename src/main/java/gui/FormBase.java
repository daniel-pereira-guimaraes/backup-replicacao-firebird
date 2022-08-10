package gui;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import geral.Constante;
import geral.Dialogo;

@SuppressWarnings("serial")
public abstract class FormBase extends JDialog implements WindowListener {
	
	private int resultado = JOptionPane.DEFAULT_OPTION;
	
	public FormBase() {
		super();
		if (isModal())
			setDefaultCloseOperation(HIDE_ON_CLOSE);
		else
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
	}
	
	protected static void exibe(final Class<?> classe) {
		if (JDialog.class.isAssignableFrom(classe)) {
			try {
				JDialog form = null;
				for (Window w : Window.getWindows()) {
					if (w.isShowing() && w.getClass().equals(classe)) {
						form = (JDialog) w;
						break;
					}
				}
				if (form == null)
					form = (JDialog) classe.newInstance();
				form.setVisible(true);
			} catch (Exception e) {
				Dialogo.erro(e);
			}
		} else
			Dialogo.erro(String.format(Constante.STR_CLASSES_INCOMPATIVEIS, 
				classe.getName(), JDialog.class.getName()));
	}

	public int exibeModal() {
		setModal(true);
		setVisible(true);
		return resultado;
	}
	
	public void atualizaTela() {}
	
	protected boolean podeFechar(int resultado) {
		return true;
	}
	
	protected void fecha(int resultado) {
		if (podeFechar(resultado)) {
			this.resultado = resultado;
			setVisible(false);
		}
	}
	
	protected void onShow() {
		atualizaTela();
	}
	
	@Override
	public void windowActivated(WindowEvent we) {}

	@Override
	public void windowClosed(WindowEvent we) {}

	@Override
	public void windowClosing(WindowEvent we) {}

	@Override
	public void windowDeactivated(WindowEvent we) {}

	@Override
	public void windowDeiconified(WindowEvent we) {}

	@Override
	public void windowIconified(WindowEvent we) {}

	@Override
	public void windowOpened(WindowEvent we) {
		onShow();
	}
	
}
