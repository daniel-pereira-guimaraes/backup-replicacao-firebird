package gui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import geral.Config;
import geral.Constante;
import geral.Dialogo;
import geral.Monitoramento;
import geral.Saida;
import geral.Util;
import gui.componente.DButton;
import gui.componente.DTable;
import regra.TarefaRN;

@SuppressWarnings("serial")
public class FormPrincipal extends JFrame implements ActionListener {
		
	private static FormPrincipal formPrincipal = null;
	
	private boolean verificandoTarefasPendentes = false;
	
	public class EventoTableModel extends AbstractTableModel implements TableModel {
	
		private class Evento {
			private String data;
			private String hora;
			private String mensagem;

			public Evento(String mensagem) {
				this.mensagem = mensagem;
				LocalDateTime dataHora = LocalDateTime.now();
				this.data = Util.toString(dataHora.toLocalDate());
				this.hora = Util.toString(dataHora.toLocalTime());
			}
			
			public String getData() {
				return data;
			}
			
			public String getHora() {
				return hora;
			}
			
			public String getMensagem() {
				return mensagem;
			}
		}

		private List<Evento> dados = new ArrayList<Evento>();
		
		public void adiciona(String mensagem) {
			dados.add(new Evento(mensagem));
			if (dados.size() > 100)
				dados.remove(0);
		}
		
		public void limpa() {
			dados.clear();
		}
				
		@Override
		public int getRowCount() {
			return dados.size();
		}
		
		@Override
		public int getColumnCount() {
			return 3;
		}
		
		@Override
		public String getColumnName(int col) {
			switch (col) {
				case 0: return Constante.STR_DATA; 
				case 1: return Constante.STR_HORA;
				case 2: return Constante.STR_MENSAGEM;
				default: return null;
			}
		}

		@Override
		public Object getValueAt(int row, int col) {
			Evento e = dados.get(row);
			switch (col) {
				case 0: return e.getData(); 
				case 1: return e.getHora();
				case 2: return e.getMensagem();
				default: return null;
			}
		}
		
	}
		
	private JMenuBar menuBar = new JMenuBar();

	private JMenu 
		mnuCadastro = new JMenu(Constante.STR_CADASTRO),
		mnuFerramenta = new JMenu(Constante.STR_FERRAMENTAS),
		mnuAjuda = new JMenu(Constante.STR_AJUDA);
	
	private JMenuItem
		mniBancoDados = new JMenuItem(Constante.STR_BANCOS_DE_DADOS),
		mniPasta = new JMenuItem(Constante.STR_PASTAS_PARA_BACKUP),
		mniMonitor = new JMenuItem(Constante.STR_MONITORES),
		mniTarefa = new JMenuItem(Constante.STR_TAREFAS),
		mniConfig = new JMenuItem(Constante.STR_CONFIGURACOES),
		mniContinuar = new JMenuItem(Constante.STR_CONTINUAR),
		mniPausar = new JMenuItem(Constante.STR_PAUSAR),
		mniLimpar = new JMenuItem(Constante.STR_LIMPAR),
		mniSobre = new JMenuItem(Constante.STR_SOBRE_SISTEMA);
	
	private JToolBar toolBar = new JToolBar();
		
	private DButton
		btnBancoDados = new DButton(Constante.STR_BANCOS_DE_DADOS, "bancoDados_48x48.png", true),
		btnPasta = new DButton(Constante.STR_PASTAS_PARA_BACKUP, "pasta_48x48.png", true),
		btnMonitor = new DButton(Constante.STR_MONITORES, "monitor_48x48.png", true),
		btnTarefa = new DButton(Constante.STR_TAREFAS, "backup_48x48.png", true),
		btnConfig = new DButton(Constante.STR_CONFIGURACOES, "config_48x48.png", true),
		btnContinuar = new DButton(Constante.STR_CONTINUAR, "continuar_48x48.png", true),
		btnPausar = new DButton(Constante.STR_PAUSAR, "pausar_48x48.png", true),
		btnLimpar = new DButton(Constante.STR_LIMPAR, "limpar_48x48.png", true);

	private EventoTableModel tableModel = new EventoTableModel();
	private DTable tabela = new DTable();
	private JScrollPane spTabela = new JScrollPane();
	private Timer timerTarefas = new Timer();
	
	public FormPrincipal() {
		super();
		formPrincipal = this;
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle(Constante.STR_TITULO_APLICACAO);
		setIconImage(Util.getResourceAsImage("gbr_64x64.png"));
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(new Dimension(d.width * 8 / 10, d.height * 8 / 10));
		setLocationRelativeTo(null);
		setJMenuBar(menuBar);
		add(toolBar, BorderLayout.NORTH);
		add(spTabela, BorderLayout.CENTER);
				
		mnuCadastro.add(mniBancoDados);
		mnuCadastro.add(mniPasta);
		mnuCadastro.add(mniMonitor);
		mnuCadastro.add(mniTarefa);
				
		mnuFerramenta.add(mniConfig);
		mnuFerramenta.add(mniContinuar);
		mnuFerramenta.add(mniPausar);
		mnuFerramenta.add(mniLimpar);
		
		mnuAjuda.add(mniSobre);
		
		menuBar.add(mnuCadastro);
		menuBar.add(mnuFerramenta);
		menuBar.add(mnuAjuda);

		toolBar.add(btnBancoDados);
		toolBar.add(btnPasta);
		toolBar.add(btnMonitor);
		toolBar.add(btnTarefa);
		toolBar.add(btnConfig);
		toolBar.add(btnContinuar);
		toolBar.add(btnPausar);
		toolBar.add(btnLimpar);
		
		tabela.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		spTabela.setViewportView(tabela);
		spTabela.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(10,15,15,15),
				BorderFactory.createLineBorder(Color.GRAY, 1)));
						
		mniBancoDados.addActionListener(this);
		mniPasta.addActionListener(this);
		mniMonitor.addActionListener(this);
		mniTarefa.addActionListener(this);
		mniConfig.addActionListener(this);
		mniContinuar.addActionListener(this);
		mniPausar.addActionListener(this);
		mniLimpar.addActionListener(this);
		mniSobre.addActionListener(this);

		btnBancoDados.addActionListener(this);
		btnPasta.addActionListener(this);
		btnMonitor.addActionListener(this);
		btnTarefa.addActionListener(this);
		btnConfig.addActionListener(this);
		btnContinuar.addActionListener(this);
		btnLimpar.addActionListener(this);
		btnPausar.addActionListener(this);

		preparaTabela();
		ajustaRolagemVertical();
		atualizaTela();
		if (!Config.getSistemaPausado()) monitoramento(true);
		programaTimer(0);
		
		tabela.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent me) {
				if (!me.isConsumed() &&
					me.getButton() == MouseEvent.BUTTON1 && 
					me.getClickCount() == 2 &&
					tabela.getSelectedRow() >= 0) {
					String s = tabela.getModel().getValueAt(tabela.getSelectedRow(), 2).toString();
					Dialogo.informacao(s.replaceAll("\\<.*?\\>", ""));
				}
			}
		});
	}
		
	private void ajustaRolagemVertical() {
		spTabela.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
	        public void adjustmentValueChanged(AdjustmentEvent e) {
	        	// Mantém a rolagem no final se a linha selecionada for a última.
	        	if (tabela.getSelectedRow() == tableModel.getRowCount() - 1)
	        		e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
	        }
	    });
	}
	
	public static FormPrincipal getFormPrincipal() {
		return formPrincipal;
	}
	
	public EventoTableModel getTableModel() {
		return tableModel;
	}
	
	public DTable getTabela() {
		return tabela;
	}
	
	private void preparaTabela() {
		tabela.setModel(tableModel);
		tabela.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		TableColumnModel tcm = tabela.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(100);
		tcm.getColumn(1).setPreferredWidth(100);
		tcm.getColumn(2).setPreferredWidth(1000);
		
		DefaultTableCellRenderer crCentro = new DefaultTableCellRenderer();
		crCentro.setHorizontalAlignment(JLabel.CENTER);
		tcm.getColumn(0).setCellRenderer(crCentro);
		tcm.getColumn(1).setCellRenderer(crCentro);
	}

	/*
	private void ajustaBotoes() {
		Dimension d = new Dimension(150, 85);
		for (int i = 0; i < toolBar.getComponentCount(); i++) {
			Component c = toolBar.getComponent(i);
			c.setPreferredSize(d);
			c.setMinimumSize(d);
			//c.setMaximumSize(d);
		}
	}
	*/
	
	private synchronized void programaTimer(int tempo) {
		if (!Config.getSistemaPausado()) {
			timerTarefas = new Timer();
			timerTarefas.schedule(new TimerTask() {
				@Override
				public void run() {
					executaTarefasPendentes();
				}
			}, tempo);
		}
	}

	private synchronized void cancelaTimer() {
		if (timerTarefas != null) {
			timerTarefas.cancel();
			timerTarefas = null;
		}
	}
	
	private void executaTarefasPendentes() {
		if (!verificandoTarefasPendentes) {
			new Thread(new Runnable() {
	            @Override
	            public void run() {
	            	try {
	            		verificandoTarefasPendentes = true;
	            		atualizaTela();
		            	cancelaTimer();
		        		TarefaRN.verificaTarefasPendentes();
		        		monitoramento(false);
						programaTimer(1000);
	            	} finally {
	            		atualizaTela();
	            		verificandoTarefasPendentes = false;
	            	}
	            }
	        }).start();
		}
	}
	
		
	private void sobre() {
		try {
			JOptionPane.showMessageDialog(this,
				Constante.STR_DADOS_SISTEMA, 
				Constante.STR_SOBRE_SISTEMA,
				JOptionPane.INFORMATION_MESSAGE,
				Util.getResourceAsImageIcon("gbr_64x64.png"));
		} catch (Exception e) {
			Dialogo.erro(e);
		}
	}
	
	private synchronized void atualizaTela() {
		btnContinuar.setEnabled(Config.getSistemaPausado());
		btnPausar.setEnabled(!btnContinuar.isEnabled());
	}
	
	private void monitoramento(boolean forcarEnvio) {
		try {
			Monitoramento.enviaTarefas(Config.getSistemaPausado(), forcarEnvio);
		} catch (Throwable e) {
			Saida.escreve(Saida.Tipo.ERRO, e.getMessage());
		}
	}

	private void continua() throws FileNotFoundException, IOException {
		try {
			Config.setSistemaPausado(false);
		} finally {
			monitoramento(true);
			programaTimer(0);
			atualizaTela();
		}
	}
	
	private void pausa() throws FileNotFoundException, IOException {
		try {
			Config.setSistemaPausado(true);
		} finally {
			cancelaTimer();
			atualizaTela();
			monitoramento(true);
		}
	}

	private void limpa() {
		tableModel.limpa();
		tableModel.fireTableDataChanged();
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		try {
			if (ae.getSource() == mniBancoDados || ae.getSource() == btnBancoDados)
				FormBase.exibe(FormTabelaBancoDados.class);
			else if (ae.getSource() == mniPasta || ae.getSource() == btnPasta)
				FormBase.exibe(FormTabelaPasta.class);
			else if (ae.getSource() == mniMonitor || ae.getSource() == btnMonitor)
				FormBase.exibe(FormTabelaMonitor.class);
			else if (ae.getSource() == mniTarefa || ae.getSource() == btnTarefa)
				FormBase.exibe(FormTabelaTarefa.class);
			else if (ae.getSource() == mniConfig || ae.getSource() == btnConfig)
				FormBase.exibe(FormConfig.class);
			else if (ae.getSource() == mniContinuar || ae.getSource() == btnContinuar)
				continua();
			else if (ae.getSource() == mniPausar || ae.getSource() == btnPausar)
				pausa();
			else if (ae.getSource() == mniLimpar || ae.getSource() == btnLimpar)
				limpa();
			else if (ae.getSource() == mniSobre)
				sobre();
		} catch (Throwable e) {
			Dialogo.erro(e);
		}
	}
	
}
