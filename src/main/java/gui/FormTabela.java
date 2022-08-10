package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import geral.Constante;
import gui.componente.DButton;
import gui.componente.DTable;

@SuppressWarnings("serial")
public abstract class FormTabela extends FormBase {

	private JToolBar toolBar = new JToolBar();
	private JPanel 
		pnlCentro = new JPanel(),
		pnlPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT)),
		pnlSul = null;
	private DButton 
		btnPesquisar = new DButton(Constante.STR_PESQUISAR, "lupa_15x15.png"),
		btnOk = null, 
		btnCancelar = null;
	private DTable tabela = new DTable();
	private JScrollPane spTabela = new JScrollPane();
	
	public FormTabela() {
		super();
		add(toolBar, BorderLayout.NORTH);
		add(pnlCentro, BorderLayout.CENTER);
		
		pnlCentro.setLayout(new BorderLayout());
		pnlCentro.add(pnlPesquisa, BorderLayout.NORTH);
		pnlCentro.add(spTabela, BorderLayout.CENTER);
		
		pnlPesquisa.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		pnlPesquisa.add(btnPesquisar);
		
		tabela.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		spTabela.setViewportView(tabela);
		spTabela.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(10,15,15,15),
				BorderFactory.createLineBorder(Color.GRAY, 1)));
		
		btnPesquisar.setPreferredSize(new Dimension(120, 25));
		btnPesquisar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				carregaDados();
			}
		});
		
		tabela.addMouseListener(new MouseListener() {
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
				if (isModal() && !me.isConsumed() &&
					me.getButton() == MouseEvent.BUTTON1 && 
					me.getClickCount() == 2 && tabela.getSelectedRow() >= 0) {
					fecha(JOptionPane.OK_OPTION);
					me.consume();
				}
			}
		});
		
		tabela.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				if (isModal() && e.getKeyCode() == KeyEvent.VK_ENTER && tabela.getSelectedRow() >= 0)
					fecha(JOptionPane.OK_OPTION);
			}
		});
		
		ListSelectionModel cellSelectionModel = tabela.getSelectionModel();
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				atualizaTela();
			}
		});		
		
		preparaTabela();
	}
		
	protected void preparaPainelPesquisa() {
		pnlPesquisa.add(btnPesquisar);
	}
	
	protected JToolBar getToolBar() {
		return toolBar;
	}
	
	protected JPanel getPainelPesquisa() {
		return pnlPesquisa;
	}
	
	protected DButton getBtnPesquisar() {
		return btnPesquisar;
	}
	
	protected JScrollPane getSpTabela() {
		return spTabela;
	}
	
	protected DTable getTabela() {
		return tabela;
	}
	
	protected abstract void preparaTabela();
	protected abstract void carregaDados();
	
	private void adicionaOkCancelar() {
		if (pnlSul == null) {
			pnlSul = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 0));
			pnlSul.setBorder(BorderFactory.createEmptyBorder(0,0,15,5));
		}
		
		if (btnOk == null) {
			btnOk = new DButton(Constante.STR_OK);
			btnOk.setDefaultCapable(true);
			btnOk.setPreferredSize(new Dimension(100, 25));
			btnOk.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FormTabela.this.fecha(JOptionPane.OK_OPTION);
				}
			});
		}
		
		if (btnCancelar == null) {
			btnCancelar = new DButton(Constante.STR_CANCELAR);
			btnCancelar.setPreferredSize(new Dimension(100, 25));
			btnCancelar.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					FormTabela.this.fecha(JOptionPane.CANCEL_OPTION);
				}
			});
		}
		
		add(pnlSul, BorderLayout.SOUTH);
		pnlSul.add(btnOk);
		pnlSul.add(btnCancelar);
	}
	
	protected DButton getBtnOk() {
		return btnOk;
	}
	
	protected DButton getBtnCancelar() {
		return btnCancelar;
	}
	
	@Override
	public void atualizaTela() {
		super.atualizaTela();
		if (btnOk != null)
			btnOk.setEnabled(tabela.getSelectedRow() >= 0);
	}
	
	@Override
	protected void onShow() {
		super.onShow();
		preparaPainelPesquisa();
		if (isModal())
			adicionaOkCancelar();
	}

}
