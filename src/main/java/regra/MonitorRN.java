package regra;

import java.util.List;

import dao.Conexao;
import dao.MonitorDAO;
import modelo.Monitor;

public class MonitorRN {
	
	public static Monitor grava(Monitor monitor) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new MonitorDAO(conexao).grava(monitor);
		} finally {
			conexao.desconecta();
		}
		return monitor;
	}
	
	public static void exclui(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new MonitorDAO(conexao).exclui(id);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static List<Monitor> buscaPorNome(String nome) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new MonitorDAO(conexao).buscaPorNome(nome);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static List<Monitor> buscaTudo() throws Exception {
		return buscaPorNome(null);
	}
	
	public Monitor buscaPorId(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new MonitorDAO(conexao).buscaPorId(id);
		} finally {
			conexao.desconecta();
		}
	}

}
