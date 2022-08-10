package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalTime;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import geral.Constante;
import geral.Dialogo;
import geral.Util;
import gui.componente.DButton;
import gui.componente.DComboBox;
import gui.componente.DIntField;
import gui.componente.DTextField;
import gui.componente.DTimeField;
import modelo.BancoDados;
import modelo.Tarefa;
import regra.BancoDadosRN;
import regra.TarefaRN;

@SuppressWarnings("serial")
public class PanelTarefa extends JPanel implements ItemListener, DocumentListener {

	private Tarefa tarefa;
	private boolean modificado = false;
	
	private JToolBar 
		toolBar = new JToolBar();
	
	private DButton 
		btnSalvar = new DButton(Constante.STR_SALVAR, "salvar_24x24.png"),
		btnCancelar = new DButton(Constante.STR_CANCELAR, "cancelar_24x24.png");
	
	private JPanel 
		pnlDados = new JPanel();
	
	private JLabel
		lblNome = new JLabel(Constante.STR_NOME),
		lblTipo = new JLabel(Constante.STR_TIPO),
		lblBancoDados = new JLabel(Constante.STR_BANCOS_DE_DADOS),
		lblIntervalo = new JLabel(Constante.STR_INTERVALO_SEGUNDOS),
		lblHoraInicial = new JLabel(Constante.STR_HORA_INICIAL),
		lblHoraFinal = new JLabel(Constante.STR_HORA_FINAL);
	
	private DTextField
		txtNome = new DTextField(true, Constante.STR_NOME),
		txtTipo = new DTextField(true, Constante.STR_TIPO);
	
	private DTimeField
		txtHoraInicial = new DTimeField(true, Constante.STR_HORA_INICIAL),
		txtHoraFinal = new DTimeField(true, Constante.STR_HORA_FINAL);
		
	private DIntField
		txtIntervalo = new DIntField(true);
	
	private DComboBox
		cbxBancoDados = new DComboBox(true, Constante.STR_BANCO_DE_DADOS);
	
	private JPanel
		pnlDiasSemana = new JPanel(new FlowLayout(FlowLayout.LEADING, 10, 5));
	
	private JCheckBox
		chkDomingo = new JCheckBox(Constante.STR_DOMINGO),
		chkSegunda = new JCheckBox(Constante.STR_SEGUNDA),
		chkTerca = new JCheckBox(Constante.STR_TERCA),
		chkQuarta = new JCheckBox(Constante.STR_QUARTA),
		chkQuinta = new JCheckBox(Constante.STR_QUINTA),
		chkSexta = new JCheckBox(Constante.STR_SEXTA),
		chkSabado = new JCheckBox(Constante.STR_SABADO);
	
	public PanelTarefa(Tarefa tarefa) throws Exception {
		super();
		setLayout(new BorderLayout());
		add(toolBar, BorderLayout.NORTH);
		add(pnlDados, BorderLayout.CENTER);
		
		toolBar.add(btnSalvar);
		toolBar.add(btnCancelar);
		
		SpringLayout sl = new SpringLayout();
		pnlDados.setLayout(sl);
		
		pnlDados.add(lblNome);
		pnlDados.add(lblTipo);
		pnlDados.add(lblBancoDados);
		pnlDados.add(lblIntervalo);
		pnlDados.add(lblHoraInicial);
		pnlDados.add(lblHoraFinal);

		pnlDados.add(txtNome);
		pnlDados.add(txtTipo);
		pnlDados.add(cbxBancoDados);
		pnlDados.add(txtIntervalo);
		pnlDados.add(txtHoraInicial);
		pnlDados.add(txtHoraFinal);
		pnlDados.add(pnlDiasSemana);
		
		pnlDiasSemana.setBorder(BorderFactory.createTitledBorder(Constante.STR_DIAS_DA_SEMANA));
		pnlDiasSemana.add(chkDomingo);
		pnlDiasSemana.add(chkSegunda);
		pnlDiasSemana.add(chkTerca);
		pnlDiasSemana.add(chkQuarta);
		pnlDiasSemana.add(chkQuinta);
		pnlDiasSemana.add(chkSexta);
		pnlDiasSemana.add(chkSabado);
		
		// Nome
		sl.putConstraint(SpringLayout.NORTH, lblNome, 10, SpringLayout.NORTH, pnlDados);
		sl.putConstraint(SpringLayout.NORTH, txtNome, 2, SpringLayout.SOUTH, lblNome);
		sl.putConstraint(SpringLayout.WEST, lblNome, 20, SpringLayout.WEST, pnlDados);				
		sl.putConstraint(SpringLayout.WEST, txtNome, 0, SpringLayout.WEST, lblNome);
		sl.putConstraint(SpringLayout.EAST, txtNome, -10, SpringLayout.WEST, txtTipo);
		
		// Tipo
		sl.putConstraint(SpringLayout.BASELINE, lblTipo, 0, SpringLayout.BASELINE, lblNome);
		sl.putConstraint(SpringLayout.BASELINE, txtTipo, 0, SpringLayout.BASELINE, txtNome);
		sl.putConstraint(SpringLayout.WEST, lblTipo, 0, SpringLayout.WEST, txtTipo);
		sl.putConstraint(SpringLayout.EAST, txtTipo, -20, SpringLayout.EAST, pnlDados);

		// Hora final
		sl.putConstraint(SpringLayout.NORTH, lblHoraFinal, 10, SpringLayout.SOUTH, txtTipo);
		sl.putConstraint(SpringLayout.EAST, lblHoraFinal, -20, SpringLayout.EAST, pnlDados);
		
		sl.putConstraint(SpringLayout.NORTH, txtHoraFinal, 2, SpringLayout.SOUTH, lblHoraFinal);
		sl.putConstraint(SpringLayout.EAST, txtHoraFinal, 0, SpringLayout.EAST, txtTipo);
		sl.putConstraint(SpringLayout.WEST, lblHoraFinal, 0, SpringLayout.WEST, txtHoraFinal);

		// Hora inicial
		sl.putConstraint(SpringLayout.BASELINE, lblHoraInicial, 0, SpringLayout.BASELINE, lblHoraFinal);
		sl.putConstraint(SpringLayout.BASELINE, txtHoraInicial, 0, SpringLayout.BASELINE, txtHoraFinal);
		sl.putConstraint(SpringLayout.EAST, txtHoraInicial, -10, SpringLayout.WEST, txtHoraFinal);
		sl.putConstraint(SpringLayout.WEST, lblHoraInicial, 0, SpringLayout.WEST, txtHoraInicial);
		
		// Intervalo
		sl.putConstraint(SpringLayout.BASELINE, lblIntervalo, 0, SpringLayout.BASELINE, lblHoraInicial);
		sl.putConstraint(SpringLayout.BASELINE, txtIntervalo, 0, SpringLayout.BASELINE, txtHoraInicial);
		sl.putConstraint(SpringLayout.EAST, txtIntervalo, -10, SpringLayout.WEST, txtHoraInicial);
		sl.putConstraint(SpringLayout.WEST, lblIntervalo, 0, SpringLayout.WEST, txtIntervalo);
		
		// BancoDados
		sl.putConstraint(SpringLayout.BASELINE, lblBancoDados, 0, SpringLayout.BASELINE, lblIntervalo);
		sl.putConstraint(SpringLayout.BASELINE, cbxBancoDados, 0, SpringLayout.BASELINE, txtIntervalo);
		sl.putConstraint(SpringLayout.EAST, cbxBancoDados, -10, SpringLayout.WEST, txtIntervalo);
		sl.putConstraint(SpringLayout.WEST, cbxBancoDados, 0, SpringLayout.WEST, txtNome);
		sl.putConstraint(SpringLayout.WEST, lblBancoDados, 0, SpringLayout.WEST, cbxBancoDados);
		
		// Dias da semana
		sl.putConstraint(SpringLayout.NORTH, pnlDiasSemana, 10, SpringLayout.SOUTH, cbxBancoDados);
		sl.putConstraint(SpringLayout.WEST, pnlDiasSemana, 0, SpringLayout.WEST, cbxBancoDados);
		sl.putConstraint(SpringLayout.EAST, pnlDiasSemana, 0, SpringLayout.EAST, txtHoraFinal);

		// Painel de dados
		sl.putConstraint(SpringLayout.SOUTH, pnlDados, 20, SpringLayout.SOUTH, pnlDiasSemana);
		
		// Tamanho preferencial dos componentes.
		txtTipo.setPreferredSize(new Dimension(150, 25));
		txtNome.setPreferredSize(new Dimension(450, 25));
		cbxBancoDados.setPreferredSize(new Dimension(300, 25));
		txtIntervalo.setPreferredSize(new Dimension(150, 25));
		txtHoraInicial.setPreferredSize(new Dimension(100, 25));
		txtHoraFinal.setPreferredSize(new Dimension(100, 25));
		pnlDiasSemana.setPreferredSize(new Dimension(600, 60));
		
		// Configurações especiais
		txtTipo.setEditable(false);
		
		// Eventos
		txtNome.getDocument().addDocumentListener(this);
		cbxBancoDados.addItemListener(this);
		txtIntervalo.getDocument().addDocumentListener(this);
		txtHoraInicial.getDocument().addDocumentListener(this);
		txtHoraFinal.getDocument().addDocumentListener(this);
		chkDomingo.addItemListener(this);
		chkSegunda.addItemListener(this);
		chkTerca.addItemListener(this);
		chkQuarta.addItemListener(this);
		chkQuinta.addItemListener(this);
		chkSexta.addItemListener(this);
		chkSabado.addItemListener(this);
		
		btnSalvarActionListener();
		btnCancelarActionlistener();
		carregaCbxBancoDados();
		setTarefa(tarefa);
	}
	
	private void salva() throws Exception {
		verificaDados();
		setTarefa(TarefaRN.grava(getTarefaEditada()));
	}
	
	private void btnSalvarActionListener() {
		btnSalvar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					salva();
				} catch(Exception e) {
					Dialogo.erro(e);
				}
			}
		});
	}
	
	private void btnCancelarActionlistener() {
		btnCancelar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setTarefa(tarefa);
			}
		});
	}
	
	private void carregaCbxBancoDados() throws Exception {
		cbxBancoDados.addItem(null);
		Util.add(cbxBancoDados, BancoDadosRN.buscaPorNome(null).toArray());
	}
		
	public void setTarefa(Tarefa tarefa) {
		this.tarefa = tarefa;
		txtTipo.setText(tarefa.getTipo().toString());
		txtNome.setText(tarefa.getNome());
		if (tarefa.getBancoDados().getId() == null)
			cbxBancoDados.setSelectedItem(null);
		else
			cbxBancoDados.setSelectedItem(tarefa.getBancoDados());

		if (tarefa.getTipo() == Tarefa.Tipo.BACKUP) {
			lblIntervalo.setText(Constante.STR_INTERVALO_MINUTOS);
			txtIntervalo.setMinValue(1); // 30 minutos  /// 1 minuto só para testes
			txtIntervalo.setMaxValue(24*60); // 24 horas
			txtIntervalo.setInt(tarefa.getIntervalo() / 60);
		} else {
			lblIntervalo.setText(Constante.STR_INTERVALO_SEGUNDOS);
			txtIntervalo.setMinValue(5); // 5 segundos
			txtIntervalo.setMaxValue(5*60); // 5 minutos
			txtIntervalo.setInt(tarefa.getIntervalo());
		}
		txtIntervalo.setName(lblIntervalo.getText());
		txtHoraInicial.setText(tarefa.getHoraInicial().toString());
		txtHoraFinal.setText(tarefa.getHoraFinal().toString());
		chkDomingo.setSelected(tarefa.isDomingo());
		chkSegunda.setSelected(tarefa.isSegunda());
		chkTerca.setSelected(tarefa.isTerca());
		chkQuarta.setSelected(tarefa.isQuarta());
		chkQuinta.setSelected(tarefa.isQuinta());
		chkSexta.setSelected(tarefa.isSexta());
		chkSabado.setSelected(tarefa.isSabado());
		setModificado(false);
	}
	
	public Tarefa getTarefa() {
		return this.tarefa;
	}
		
	private void verificaSalvar() throws Exception {
		if (modificado)
			salva();
	}
	
	public void verificaTarefa() throws Exception {
		verificaSalvar();
		if (tarefa.getId() == null) {
			txtNome.grabFocus();
			throw new Exception(Constante.STR_INFORMAR_CAB_TAREFA);
		}
	}
	
	private void verificaDados() throws Exception {
		txtNome.checkValue();
		cbxBancoDados.checkValue();
		txtIntervalo.checkValue();
		txtHoraInicial.checkValue();
		txtHoraFinal.checkValue();
	}
	
	public Tarefa getTarefaEditada() throws Exception {
		verificaDados();
		BancoDados bancoDados = (BancoDados) cbxBancoDados.getSelectedItem();
		Tarefa tarefa = new Tarefa();
		tarefa.setId(this.tarefa.getId());
		tarefa.setNome(txtNome.getText());
		tarefa.setTipo(Tarefa.Tipo.fromString(txtTipo.getText()));
		if (bancoDados != null) {
			tarefa.getBancoDados().setId(bancoDados.getId());
			tarefa.getBancoDados().setNome(bancoDados.getNome());
		} 
		if (tarefa.getTipo() == Tarefa.Tipo.BACKUP) 
			tarefa.setIntervalo(txtIntervalo.getInt() * 60);
		else
			tarefa.setIntervalo(txtIntervalo.getInt());
		tarefa.setHoraInicial(LocalTime.parse(txtHoraInicial.getText()));
		tarefa.setHoraFinal(LocalTime.parse(txtHoraFinal.getText()));
		tarefa.setDomingo(chkDomingo.isSelected());
		tarefa.setSegunda(chkSegunda.isSelected());
		tarefa.setTerca(chkTerca.isSelected());
		tarefa.setQuarta(chkQuarta.isSelected());
		tarefa.setQuinta(chkQuinta.isSelected());
		tarefa.setSexta(chkSexta.isSelected());
		tarefa.setSabado(chkSabado.isSelected());
		return tarefa;
	}
	
	public void atualizaTela() {
		btnSalvar.setEnabled(modificado);
		btnCancelar.setEnabled(modificado);
	}
	
	public void setModificado(boolean modificado) {
		this.modificado = modificado;
		atualizaTela();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		setModificado(true);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		setModificado(true);
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		setModificado(true);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		setModificado(true);
	}
}
