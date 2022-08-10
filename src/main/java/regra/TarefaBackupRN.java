package regra;

import java.util.List;

import dao.Conexao;
import dao.TarefaBackupDAO;
import modelo.TarefaBackup;
import modelo.TarefaPendente;

public class TarefaBackupRN {
	
	public static TarefaBackup grava(TarefaBackup tarefaBackup) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaBackupDAO(conexao).grava(tarefaBackup);
		} finally {
			conexao.desconecta();
		}
		return tarefaBackup;
	}
	
	public static void gravaMensagem(TarefaPendente.TarefaBackup tarefaBackup) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaBackupDAO(conexao).gravaMensagem(tarefaBackup);
		} finally {
			conexao.desconecta();
		}
	}

	public static void gravaMensagem(TarefaPendente tarefa, String mensagem) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaBackupDAO(conexao).gravaMensagem(tarefa, mensagem);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static void exclui(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaBackupDAO(conexao).exclui(id);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static List<TarefaBackup> buscaPorTarefaId(int tarefaId) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new TarefaBackupDAO(conexao).buscaPorTarefaId(tarefaId);
		} finally {
			conexao.desconecta();
		}
	}
	

}
