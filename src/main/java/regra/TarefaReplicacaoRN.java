package regra;

import java.util.List;

import dao.Conexao;
import dao.TarefaReplicacaoDAO;
import modelo.TarefaPendente;
import modelo.TarefaReplicacao;

public class TarefaReplicacaoRN {
	
	public static TarefaReplicacao grava(TarefaReplicacao tarefaReplicacao) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaReplicacaoDAO(conexao).grava(tarefaReplicacao);
		} finally {
			conexao.desconecta();
		}
		return tarefaReplicacao;
	}
	
	public static void gravaMensagem(TarefaPendente.TarefaReplicacao tarefaReplicacao) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaReplicacaoDAO(conexao).gravaMensagem(tarefaReplicacao);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static void gravaMensagem(TarefaPendente tarefa, String mensagem) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaReplicacaoDAO(conexao).gravaMensagem(tarefa, mensagem);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static void exclui(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaReplicacaoDAO(conexao).exclui(id);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static List<TarefaReplicacao> buscaPorTarefaId(int tarefaId) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new TarefaReplicacaoDAO(conexao).buscaPorTarefaId(tarefaId);
		} finally {
			conexao.desconecta();
		}
	}
	

}
