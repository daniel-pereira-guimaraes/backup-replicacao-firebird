package regra;

import java.util.List;

import dao.Conexao;
import dao.TarefaMonitorDAO;
import modelo.TarefaMonitor;

public class TarefaMonitorRN {
	
	public static TarefaMonitor grava(TarefaMonitor tarefaMonitor) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaMonitorDAO(conexao).grava(tarefaMonitor);
		} finally {
			conexao.desconecta();
		}
		return tarefaMonitor;
	}
	
	public static void exclui(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new TarefaMonitorDAO(conexao).exclui(id);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static List<TarefaMonitor> buscaPorTarefaId(int tarefaId) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new TarefaMonitorDAO(conexao).buscaPorTarefaId(tarefaId);
		} finally {
			conexao.desconecta();
		}
	}
	

}
