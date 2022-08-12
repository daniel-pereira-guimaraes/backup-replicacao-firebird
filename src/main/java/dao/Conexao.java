package dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import firebird.FirebirdConexao;
import geral.Config;
import geral.Constante;
import geral.Dialogo;
import geral.Util;
import modelo.BancoDados;

public abstract class Conexao {
	
	private static final String nomeBancoDados = "GBR.fdb";
	
	private static String getEnderecoBancoDados() {
		return Util.getAppAbsolutePath() + nomeBancoDados;
	}
	
	private static void verificaBancoDados() throws Exception {
		String enderecoBancoDados = getEnderecoBancoDados();
		File f = new File(enderecoBancoDados);
		if (!f.exists()) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream is = loader.getResourceAsStream(nomeBancoDados);
		    if (is != null) {
		    	File file = new File(enderecoBancoDados);
		    	java.nio.file.Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
		    } else
		    	throw new Exception(String.format(Constante.STR_RECURSO_NAO_ENCONTRADO, nomeBancoDados));
		}
	}
	
	public static Conexao getConexao() throws Exception {
		verificaBancoDados();
		BancoDados bancoDados = new BancoDados();
		bancoDados.setMaquina("localhost");
		bancoDados.setPorta(30530);
		bancoDados.setEndereco(getEnderecoBancoDados());
		bancoDados.setCaracteres("UTF8");
		bancoDados.setUsuario(Config.getConexaoUsuario());
		bancoDados.setSenha(Config.getConexaoSenha());
		return new FirebirdConexao(bancoDados);
	}
	
	private Connection connection;
	
	protected abstract Connection createConnection() throws Exception;
	
	public void conecta() throws Exception {
		if (connection == null || connection.isClosed()) {
			connection = createConnection();
		}
	}
	
	public void desconecta() {
		if (connection != null) {
			try {
				if (!connection.isClosed()) 
					connection.close();
			} catch (Throwable ignora) {
			} finally {
				connection = null;
			}
		}
	}
	
	public boolean isConectado() throws SQLException {
		return connection != null && !connection.isClosed();
	}
	
	private void verificaConectado() throws Exception {
		if (!isConectado())
			throw new Exception(Constante.STR_BANCO_NAO_CONECTADO);
	}
	
	public void iniciaTransacao() throws Exception {
		verificaConectado();
		connection.setAutoCommit(false);
	}
	
	public void confirmaTransacao() throws Exception {
		verificaConectado();
		connection.commit();
		connection.setAutoCommit(true);
	}
	
	public void cancelaTransacao() throws Exception {
		verificaConectado();
		connection.rollback();
		connection.setAutoCommit(true);
	}
	
	private void executa(String comando) throws Exception {
		verificaConectado();
		Statement stm = connection.createStatement();
		stm.execute(comando);
	}

	public Statement createStatement() throws Exception {
		verificaConectado();
		return connection.createStatement();
	}
	
	public PreparedStatement prepareStatement(String comando) throws Exception {
		verificaConectado();
		return connection.prepareStatement(comando);
	}
	
	public abstract boolean existeGatilho(String nome) throws SQLException, Exception;
	public abstract boolean existeTabela(String nome) throws SQLException, Exception;
	
	public void executaScript(String script) throws Throwable {
		String[] comandos = script.split("\\^");
		for (String comando : comandos) {
			comando = comando.trim();
			if (!comando.isEmpty()) {
				if (comando.equalsIgnoreCase("RECONNECT")) {
					if (isConectado()) 
						desconecta();
					conecta();
				} else {
					if (!isConectado())
						conecta();
					iniciaTransacao();
					try {
						executa(comando);
						confirmaTransacao();
					} catch(Throwable e) {
						cancelaTransacao();
						throw e;
					}
				}
			}
		}
	}
}
