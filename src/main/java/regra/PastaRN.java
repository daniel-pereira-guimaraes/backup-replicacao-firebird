package regra;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.UUID;

import backup.Arquivo;
import backup.PastaBackup;
import dao.Conexao;
import dao.PastaDAO;
import geral.Constante;
import modelo.Pasta;

public class PastaRN {
	
	public static Pasta grava(Pasta pasta) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new PastaDAO(conexao).grava(pasta);
		} finally {
			conexao.desconecta();
		}
		return pasta;
	}
	
	public static void exclui(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			new PastaDAO(conexao).exclui(id);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static List<Pasta> buscaPorNome(String nome) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new PastaDAO(conexao).buscaPorNome(nome);
		} finally {
			conexao.desconecta();
		}
	}
	
	public Pasta buscaPorId(int id) throws Exception {
		Conexao conexao = Conexao.getConexao();
		conexao.conecta();
		try {
			return new PastaDAO(conexao).buscaPorId(id);
		} finally {
			conexao.desconecta();
		}
	}
	
	public static void testaAcesso(Pasta pasta) throws Exception {
		PastaBackup pastaBackup = PastaBackup.getInstance(pasta);
		pastaBackup.conecta();
		try {
			String nome = UUID.randomUUID().toString().toUpperCase().replace("-", "");
			pastaBackup.gravaArquivo(nome, new ByteArrayInputStream(nome.getBytes()));
			try {
				/// Testar isto aqui, pois listaArquivos recebe filtro (pattern) 
				// e estou passando o nome do arquivo
				List<Arquivo> lista = pastaBackup.listaArquivos(nome);
				if (lista.size() != 1)
					throw new Exception(Constante.STR_ERRO_DESCONHECIDO);
			} finally {
				pastaBackup.excluiArquivo(nome);
			}
		} finally {
			pastaBackup.desconecta();
		}
	}

}
