package firebird;

import java.io.ByteArrayInputStream;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.Conexao;
import geral.Constante;
import geral.Saida;
import geral.Util;
import modelo.BancoDados;

public class FirebirdReplicador {

	private class Parametro {
		public String nome;
		public String valor;
		public Double valorReal;
		public byte[] valorBinario;
		public boolean filtro;
	}	

	private class InfoBanco {
		public String uuid;
		public String origem;
		public long sequencia;		
	}
	
	private FirebirdConexao conexaoOrigem;
	private FirebirdConexao conexaoDestino;
	private String enderecoGbak;
	private int qtdTransacoesReplicadas;
	private int qtdComandosReplicados;
	private SimpleDateFormat formatoData = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss.SSSS");
	private SimpleDateFormat formatoDataHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
	
	public FirebirdReplicador(BancoDados bancoDadosOrigem, 
			BancoDados bancoDadosDestino, String enderecoGbak) throws SQLException {
		super();
		this.conexaoOrigem = new FirebirdConexao(bancoDadosOrigem);
		this.conexaoDestino = new FirebirdConexao(bancoDadosDestino);
		this.enderecoGbak = enderecoGbak;
	}
	
	private boolean origemPreparado() throws Exception {
		return conexaoOrigem.existeGatilho("R$O$TG$DDL");
	}

	private void preparaOrigem() throws Throwable {
		Saida.escreve(Saida.Tipo.NORMAL, String.format(
			Constante.STR_REP_PREPARANDO_DESTINO, 
			conexaoOrigem.getBancoDados().getNome()));
		conexaoOrigem.executaScript(Util.getResourceAsString("preparaOrigem.sql"));
	}

	private void despreparaOrigem() throws Throwable {
		Saida.escreve(Saida.Tipo.NORMAL, String.format(
			Constante.STR_REP_DESPREPARANDO_ORIGEM, 
			conexaoOrigem.getBancoDados().getNome()));
		conexaoOrigem.executaScript(Util.getResourceAsString("despreparaOrigem.sql"));
	}

	private String pegaUUID(Conexao conexao) throws Exception {
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexao.createStatement();
			rs = stm.executeQuery("SELECT UUID FROM R$C$TB$BANCO_DADOS");
			if (rs.next())
				return rs.getString(1);
			else 
				return null;
		} finally {
			if (rs != null) rs.close();
			if (stm != null) stm.close();
		}
	}

	private void cadastraDestino() throws Exception {
		String uuid = pegaUUID(conexaoDestino);
		if (uuid != null) {
			PreparedStatement pstmOrigem = conexaoOrigem.prepareStatement(
				"INSERT INTO R$O$TB$DESTINO(UUID) VALUES(?)");
			try {
				pstmOrigem.setString(1, uuid);
				pstmOrigem.execute();
			} finally {
				pstmOrigem.close();
			}
		} else
			throw new Exception(Constante.STR_ERRO_DEFINIR_UUID_DESTINO);
	}
	
	private void preparaDestino() throws Throwable {
		Saida.escreve(Saida.Tipo.NORMAL, String.format(
			Constante.STR_REP_PREPARANDO_DESTINO, 
			conexaoDestino.getBancoDados().getNome()));

		// Clona o banco de dados de origem para o destino.
		new FirebirdBackup(enderecoGbak).clonaBancoDados(
			conexaoOrigem.getBancoDados(), conexaoDestino.getBancoDados());
		
		// Conecta ao banco de destino.
		conexaoDestino.conecta();
		
		// Prepara o banco de destino para receber replica��o.
		conexaoDestino.executaScript(Util.getResourceAsString("preparaDestino.sql"));
		
		// Remove do banco de destino os artefatos de origem de replica��o.
		conexaoDestino.executaScript(Util.getResourceAsString("despreparaOrigem.sql"));
		
		// Cadastra o banco de destino no banco de origem como destino de replica��o.
		cadastraDestino();
	}

	private void descadastraDestino() throws Exception {
		String uuid = pegaUUID(conexaoDestino);
		if (uuid != null) {
			removeTransacaoOrigem(uuid, Long.MAX_VALUE);
			PreparedStatement pstmOrigem = conexaoOrigem.prepareStatement(
				"DELETE FROM R$O$TB$DESTINO WHERE UUID = ?");
			try {
				pstmOrigem.setString(1, uuid);
				pstmOrigem.execute();
			} finally {
				pstmOrigem.close();
			}
		}
	}
	
	public void despreparaDestino() throws Throwable {
		try {
			conexaoOrigem.conecta();
			conexaoDestino.conecta();
			conexaoOrigem.iniciaTransacao();
			try {
				configuraSessaoDestino();
				descadastraDestino();
				conexaoOrigem.confirmaTransacao();
			} catch(Throwable e) {
				conexaoOrigem.cancelaTransacao();
				throw e;
			}
			conexaoDestino.executaScript(Util.getResourceAsString("despreparaDestino.sql"));
		} finally {
			conexaoOrigem.desconecta();
			conexaoDestino.desconecta();
		}
	}
	
	private InfoBanco getInfoBanco(Conexao conexao) throws Exception {
		Statement stm = null;
		ResultSet rs = null;
		try {
			stm = conexao.createStatement();
			rs = stm.executeQuery("SELECT UUID, ORIGEM, SEQUENCIA FROM R$C$TB$BANCO_DADOS");
			if (rs.next()) {
				InfoBanco infoBanco = new InfoBanco();
				infoBanco.uuid = rs.getString(1);
				infoBanco.origem = rs.getString(2);
				infoBanco.sequencia = rs.getLong(3);
				return infoBanco;
			}
		} finally {
			if (rs != null) rs.close();
			if (stm != null) stm.close();
		}
		throw new Exception(Constante.STR_BANCO_DESTINO_SEM_ID);
	}
		
	private void conectaOrigem() throws Throwable {
		conexaoOrigem.conecta();
		if (!origemPreparado()) {
			despreparaOrigem(); // Remove restos de instala��o anterior, se houver!
			preparaOrigem();
		}
	}
	
	private void conectaDestino() throws Throwable {
		try {
			conexaoDestino.conecta();
		} catch(SQLException e) {
			if (FirebirdConexao.bancoNaoExiste(e)) {
				preparaDestino();
				conexaoDestino.conecta();
			} else
				throw e;
		}
	}
	
	private void configuraSessaoDestino() throws Exception {
		Statement stm = conexaoDestino.createStatement();
		try {
			stm.execute(
				"EXECUTE BLOCK AS "
				+ "BEGIN "
				+ "  RDB$SET_CONTEXT('USER_SESSION', 'R$C$VAR$REPLICADOR', TRUE);"
				+ "END");
		} finally {
			stm.close();
		}
	}
	
	private String montaInsert(String tabela, List<Parametro> listaParametros) {
		String campos = null, parametros = null;
		for (Parametro p : listaParametros) {
			if (!p.filtro) {
				campos = campos == null ? "" : campos + ",";
				parametros = parametros == null ? "" : parametros + ",";
				campos += p.nome;
				parametros += "?";
			}
		}
		String comando = "INSERT INTO " + tabela;
		if (campos == null) 
			comando += " DEFAULT VALUES";
		else
			comando += "(" + campos + ") VALUES(" + parametros + ")";
		return comando;
	}
	
	private String montaUpdate(String tabela, List<Parametro> listaParametros) {
		String campos = null, filtro = null;
		for (Parametro p : listaParametros) {
			if (p.filtro) {
				filtro = filtro == null ? "" : filtro + " AND ";
				filtro += p.nome + " IS NOT DISTINCT FROM ?";
			} else {
				campos = campos == null ? "" : campos + ",";
				campos += p.nome + " = ?";
			}
		}
		if (campos == null)
			return null;
		else
			return "UPDATE " + tabela + " SET " + campos + " WHERE " + filtro;
	}
	
	private String montaDelete(String tabela, List<Parametro> listaParametros) {
		String filtro = null;
		for (Parametro p : listaParametros) {
			if (p.filtro) {
				filtro = filtro == null ? "" : filtro + " AND ";
				filtro += p.nome + " IS NOT DISTINCT FROM ?"; 
			}
		}
		return "DELETE FROM " + tabela + " WHERE " + filtro;
	}

	private List<Parametro> carregaParametros(long comandoId) throws Exception {
		List<Parametro> lista = new ArrayList<Parametro>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = conexaoOrigem.prepareStatement(
				"SELECT NOME, VALOR, VALOR_REAL, VALOR_BINARIO, FILTRO "
				+ "FROM R$O$TB$PARAMETRO "
				+ "WHERE COMANDO_ID = ? "
				+ "ORDER BY ID");
			pstm.setLong(1, comandoId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Parametro p = new Parametro();
				p.nome = rs.getString(1);
				p.valor = rs.getString(2);
				if (rs.wasNull())
					p.valor = null;
				p.valorReal = rs.getDouble(3);
				if (rs.wasNull())
					p.valorReal = null;
				p.valorBinario = rs.getBytes(4);
				if (rs.wasNull())
					p.valorBinario = null;
				p.filtro = rs.getBoolean(5);
				lista.add(p);
			}
		} finally {
			if (rs != null)	rs.close();
			if (pstm != null) pstm.close();
		}
		return lista;
	}
	
	private java.sql.Date sqlDate(String s) throws ParseException {
		if (!Util.isNullOrEmpty(s)) {
			return new java.sql.Date(formatoData.parse(s).getTime());
		}
		return null;
	}
	
	private java.sql.Time sqlTime(String s) throws ParseException {
		if (!Util.isNullOrEmpty(s)) {
			return new java.sql.Time(formatoHora.parse(s).getTime());
		}
		return null;
	}

	private java.sql.Timestamp sqlTimestamp(String s) throws ParseException {
		if (!Util.isNullOrEmpty(s)) {
			return new java.sql.Timestamp(formatoDataHora.parse(s).getTime());
		}
		return null;
	}
	
	private void passaParametros(PreparedStatement pstm, List<Parametro> listaParametros) throws SQLException, ParseException {
		ParameterMetaData paramMetaData = pstm.getParameterMetaData();
		for (int i = 1; i <= listaParametros.size(); i++) {
			Parametro parametro = listaParametros.get(i-1);
			if (parametro.valorReal != null)
				pstm.setDouble(i, parametro.valorReal);
			else if (parametro.valorBinario != null)
				pstm.setBinaryStream(i, new ByteArrayInputStream(parametro.valorBinario));
			else  {
				switch (paramMetaData.getParameterType(i)) {
					case Types.DATE: pstm.setDate(i, sqlDate(parametro.valor)); break;
					case Types.TIME: pstm.setTime(i, sqlTime(parametro.valor)); break;
					case Types.TIMESTAMP: pstm.setTimestamp(i, sqlTimestamp(parametro.valor)); break;
					default: pstm.setString(i, parametro.valor);
				}
			}
		}
	}	

	private void replicaComando(long comandoId, String tipo, String objeto, String comando) throws Exception {
		boolean insert = tipo.equals("INSERT");
		boolean update = !insert && tipo.equals("UPDATE");
		boolean delete = !insert && !update && tipo.equals("DELETE");
		if (insert || update  || delete)  {
			List<Parametro> listaParametros = carregaParametros(comandoId);
			if (insert)
				comando = montaInsert(objeto, listaParametros);
			else if (update)
				comando = montaUpdate(objeto, listaParametros);
			else
				comando = montaDelete(objeto, listaParametros);
			PreparedStatement pstmDestino = conexaoDestino.prepareStatement(comando);
			try {
				passaParametros(pstmDestino, listaParametros);
				pstmDestino.execute();
			} finally {
					pstmDestino.close();
			}
		} else {
			Statement stm = conexaoDestino.createStatement();
			try {
				stm.execute(comando);
			} finally {
				stm.close();
			}
		}
	}
	
	private int replicaTransacao(long transacaoId) throws Exception {
		int qtdComandos = 0;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = conexaoOrigem.prepareStatement(
				"SELECT ID, EVENTO, OBJETO, COMANDO FROM R$O$TB$COMANDO "
				+ "WHERE TRANSACAO_ID = ? "
				+ "ORDER BY ID");
			pstm.setLong(1, transacaoId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				replicaComando(
					rs.getLong(1),
					rs.getString(2),
					rs.getString(3),
					rs.getString(4));
				qtdComandos++;
			}
		} finally {
			if (pstm != null) pstm.close();
			if (rs != null) rs.close();
		}
		return qtdComandos;
	}
	
	private void gravaSequenciaDestino(long sequencia) throws Exception {
		PreparedStatement pstm = conexaoDestino.prepareStatement(
			"UPDATE R$C$TB$BANCO_DADOS SET SEQUENCIA = ? "
			+ "WHERE COALESCE(SEQUENCIA, 0) = ?");
		try {
			pstm.setLong(1, sequencia);
			pstm.setLong(2, sequencia - 1);
			if (pstm.executeUpdate() != 1) 
				throw new Exception(Constante.STR_ERRO_GRAVAR_SEQUENCIA_REP);
		} finally {
			pstm.close();
		}
	}

	private void removeTransacaoOrigem(String destino, long sequencia) throws Exception {
		PreparedStatement pstm = conexaoOrigem.prepareStatement(
			"EXECUTE PROCEDURE R$O$SP$REMOVE_TRANSACAO(?,?)");
		try {
			pstm.setString(1, destino);
			pstm.setLong(2, sequencia);
			pstm.execute();
		} finally {
			pstm.close();
		}
	}
	
	private void replicaTransacoes(InfoBanco infoDestino, int qtd) throws Exception {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		long sequenciaAtual = infoDestino.sequencia;
		try {
			pstm = conexaoOrigem.prepareStatement(
				"SELECT ID, SEQUENCIA FROM R$O$TB$TRANSACAO "
				+ "WHERE SEQUENCIA > ? "
				+ "ORDER BY SEQUENCIA ROWS ?");
			pstm.setLong(1, sequenciaAtual);
			pstm.setInt(2, qtd);
			rs = pstm.executeQuery();
			while (rs.next()) {
				long transacaoId = rs.getLong(1);
				long sequencia = rs.getLong(2);
				if (sequencia != sequenciaAtual + 1)
					throw new Exception(Constante.STR_SEQUENCIA_REP_QUEBRADA);
				conexaoDestino.iniciaTransacao();
				try {
					gravaSequenciaDestino(sequencia);
					int qtdComandos = replicaTransacao(transacaoId);
					conexaoDestino.confirmaTransacao();
					qtdComandosReplicados += qtdComandos;
					qtdTransacoesReplicadas++;
					sequenciaAtual = sequencia;
				} catch (Throwable e) {
					conexaoDestino.cancelaTransacao();
					throw e;
				}
			}
		} finally {
			if (pstm != null) pstm.close();
			if (rs != null)	rs.close();
		}
		if (sequenciaAtual > infoDestino.sequencia)
			removeTransacaoOrigem(infoDestino.uuid, sequenciaAtual);
	}
	
	public void replica(int qtd) throws Throwable {
		qtdTransacoesReplicadas = 0;
		qtdComandosReplicados = 0;
		Saida.escreve(Saida.Tipo.NORMAL, String.format(
			Constante.STR_REP_REPLICANDO,
				conexaoOrigem.getBancoDados().getNome(), 
				conexaoDestino.getBancoDados().getNome()));
		try {
			conectaOrigem();
			conectaDestino();
			conexaoOrigem.iniciaTransacao();
			try {
				configuraSessaoDestino();
				InfoBanco infoOrigem = getInfoBanco(conexaoOrigem);
				InfoBanco infoDestino = getInfoBanco(conexaoDestino);
				if (infoOrigem.uuid.equals(infoDestino.uuid)) 
					throw new Exception(Constante.STR_BANCO_DESTINO_IGUAL_ORIGEM);
				if (!infoOrigem.uuid.equals(infoDestino.origem))
					throw new Exception(Constante.STR_BANCO_DESTINO_INCOMPATIVEL);
				replicaTransacoes(infoDestino, qtd);
				conexaoOrigem.confirmaTransacao();
			} catch (Throwable e) {
				conexaoOrigem.cancelaTransacao();
				throw e;
			}
		} finally {
			conexaoOrigem.desconecta();
			conexaoDestino.desconecta();
			if (qtdComandosReplicados > 0) { 
				Saida.escreve(Saida.Tipo.SUCESSO, String.format(
					Constante.STR_REP_COMANDOS_TRANSACOES, 
					qtdComandosReplicados, qtdTransacoesReplicadas));
			} else
				Saida.escreve(Saida.Tipo.NORMAL, Constante.STR_REP_NENHUM_REGISTRO);
		}
	}
	
	public String getEnderecoGbak() {
		return enderecoGbak;
	}

	public void setEnderecoGbak(String enderecoGbak) {
		this.enderecoGbak = enderecoGbak;
	}
	
}
