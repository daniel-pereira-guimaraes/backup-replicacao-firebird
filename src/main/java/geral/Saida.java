package geral;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;

import gui.FormPrincipal;
import gui.FormPrincipal.EventoTableModel;
import gui.componente.DTable;

public class Saida {

	public static enum Tipo { NORMAL, SUCESSO, ERRO }

	private static final String htmlSucesso = "<html><font color='green'><b>%s</b></font></html>";
	private static final String htmlErro = "<html><font color='red'>%s</font></html>";
    private Saida() {}
        
    private static void escreveLog(String s) throws Throwable {
    	LocalDateTime agora = LocalDateTime.now();
    	int ano = agora.getYear();
    	int mes = agora.getMonthValue();
		File file = new File(String.format("GBR-%04d-%02d.log", ano, mes));
		int tentativas = 10;
		for (int i = 1; i <= tentativas; i++) {
			try {
				Files.write(file.toPath(), 
					(Util.toString(agora) + " " + s + "\n").getBytes(), 
					StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				break;
			} catch(Throwable e) {
				if (i == tentativas) 
					throw e;
				else
					Thread.sleep(500);
			}
		}
    }
    
    public static synchronized void escreve(Tipo tipo, String s) {
    	try {
    		if (tipo == Tipo.SUCESSO || tipo == Tipo.ERRO)
    			escreveLog(s);
			FormPrincipal formPrincipal = FormPrincipal.getFormPrincipal();
			if (formPrincipal != null) {
				if (tipo == Tipo.SUCESSO ) 
					s = String.format(htmlSucesso, s);
				else if (tipo == Tipo.ERRO) 
					s = String.format(htmlErro, s);
				EventoTableModel tableModel = formPrincipal.getTableModel();
				DTable tabela = formPrincipal.getTabela();
				int linhaSelecionada =  tabela.getSelectedRow();
				tableModel.adiciona(s);
				tableModel.fireTableDataChanged();
				// Se a linha selecionada era a última, selecionada a nova.
				if (linhaSelecionada == tableModel.getRowCount() -2)
					tabela.selectLastRow();
				else
					tabela.selectRow(linhaSelecionada);
			}
		} catch (Throwable e) {
			
			e.printStackTrace();
		}
    }
    
 

}
