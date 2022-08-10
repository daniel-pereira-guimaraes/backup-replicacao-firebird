package regra;

import java.util.List;

import dao.BancoDadosDAO;
import dao.Conexao;
import firebird.FirebirdConexao;
import modelo.BancoDados;

public class BancoDadosRN {
	
	public static BancoDados grava(BancoDados bancoDados) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new BancoDadosDAO(conexao).grava(bancoDados);
		} finally {
			conexao.desconecta();
		}
		return bancoDados;
	}
	
	public static void exclui(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new BancoDadosDAO(conexao).exclui(id);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static List<BancoDados> buscaPorNome(String nome) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new BancoDadosDAO(conexao).buscaPorNome(nome);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static BancoDados buscaPorId(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new BancoDadosDAO(conexao).buscaPorId(id);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static void testaConexao(BancoDados bancoDados) throws Exception {
		FirebirdConexao conexao = new FirebirdConexao(bancoDados);
		conexao.conecta();
		conexao.desconecta();
	}

}
