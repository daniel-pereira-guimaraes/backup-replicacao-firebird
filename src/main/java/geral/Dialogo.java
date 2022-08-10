package geral;

import java.awt.Component;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

import modelo.Tarefa;

public class Dialogo {
	
	public static void informacao(String msg) {
		JOptionPane.showMessageDialog(null, msg, Constante.STR_INFORMACAO, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static boolean confirmacao(String msg) {
		return JOptionPane.showConfirmDialog(null, msg, Constante.STR_CONFIRMACAO, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}
	
	public static void erro(String msg) {
		JOptionPane.showMessageDialog(null, msg, Constante.STR_ERRO, JOptionPane.ERROR_MESSAGE);
	}
	
	public static Tarefa.Tipo tipoTarefaDlg(Tarefa.Tipo tipo) {
		Tarefa.Tipo [] tipos = {Tarefa.Tipo.BACKUP, Tarefa.Tipo.REPLICACAO};
		return (Tarefa.Tipo) JOptionPane.showInputDialog (null, null, 
			Constante.STR_TIPO, JOptionPane.QUESTION_MESSAGE, null, tipos, tipo);
	}
	
	private static String getMsg(Throwable e) {
		String msg = e.getMessage();
		if (msg == null)
			msg = e.getClass().getName();
		else  {
			String[][] traducao = {
				{"UN_BANCODADOS", Constante.STR_UN_BANCODADOS},
				{"UN_BANCODADOS_NOME", Constante.STR_UN_BANCODADOS_NOME},
				{"UN_PASTA", Constante.STR_UN_PASTA},
				{"UN_PASTA_NOME", Constante.STR_UN_PASTA_NOME},
				{"UN_MONITOR_CHAVE", Constante.STR_UN_MONITOR_CHAVE},
				{"UN_TAREFA_NOME", Constante.STR_UN_TAREFA_NOME},
				{"UN_TAREFABACKUP", Constante.STR_UN_TAREFABACKUP},
				{"UN_TAREFAREPLICACAO", Constante.STR_UN_TAREFAREPLICACAO},
				{"UN_TAREFAMONITOR", Constante.STR_UN_TAREFAMONITOR},
				{"FK_TAREFA_BANCODADOS", Constante.STR_FK_TAREFA_BANCODADOS},
				{"FK_TAREFAREPLICACAO_BANCODADOS", Constante.STR_FK_TAREFA_BANCODADOS},
				{"FK_TAREFABACKUP_PASTA", Constante.STR_FK_TAREFABACKUP_PASTA},
				{"FK_TAREFAMONITOR_MONITOR", Constante.STR_FK_TAREFAMONITOR_MONITOR}
				
			
			};
			for (String[] item : traducao) {
				if (msg.contains("\"" + item[0] + "\"")) {
					msg = item[1];
					break;
				}
			}
		}
		return msg;
	}
	
	public static void erro(Throwable e) {
		Dialogo.erro(getMsg(e));
	}
	
	public static void excecao(String msg) throws Exception {
		throw new Exception(msg);
	}
	
	public static void exception(Component comp, String msg) throws Exception {
		comp.requestFocus();
		if (comp instanceof JTextField) 
			((JTextField) comp).selectAll();
		excecao(msg);
	}
	
	public static void requiredField(Component comp) throws Exception {
		String s = comp.getName();
		if (Util.isNullOrEmpty(s))
			exception(comp, Constante.STR_CAMPO_REQUERIDO);
		else
			exception(comp, String.format(Constante.STR_CAMPO_REQUERIDO_NOME, s));
	}
	
	public static void invalidValue(Component comp, String value) throws Exception {
		String s = comp.getName();
		if (Util.isNullOrEmpty(s))
			exception(comp, String.format(Constante.STR_VALOR_INVALIDO, value));
		else
			exception(comp, String.format(Constante.STR_VALOR_INVALIDO_CAMPO, s));
	}
	
}
