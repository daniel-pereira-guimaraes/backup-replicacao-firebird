package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import geral.Constante;
import geral.Util;
import modelo.Notificacao;
import modelo.Pasta;
import modelo.Tarefa;
import modelo.TarefaDestino;
import modelo.TarefaPendente;

public class TarefaDAO extends DAO {

	public TarefaDAO(Conexao conexao) {
		super(conexao);
	}
	
	public void grava(Tarefa tarefa) throws Exception {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			if (tarefa.getId() == null) {
				String uuid = Util.getUUID();
				pstm = getConexao().prepareStatement(
					"INSERT INTO Tarefa(UUID, Nome, Tipo, BancoDados_Id, Intervalo, "
					+ "HoraInicial, HoraFinal, Domingo, Segunda, Terca, Quarta, "
					+ "Quinta, Sexta, Sabado) "
					+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?) "
					+ "RETURNING Id");
				pstm.setString(1, uuid);
				pstm.setString(2, tarefa.getNome());
				pstm.setInt(3, tarefa.getTipo().getId());
				pstm.setInt(4, tarefa.getBancoDados().getId());
				pstm.setInt(5, tarefa.getIntervalo());
				pstm.setTime(6, Time.valueOf(tarefa.getHoraInicial()));
				pstm.setTime(7, Time.valueOf(tarefa.getHoraFinal()));
				pstm.setBoolean(8, tarefa.isDomingo());
				pstm.setBoolean(9, tarefa.isSegunda());
				pstm.setBoolean(10, tarefa.isTerca());
				pstm.setBoolean(11, tarefa.isQuarta());
				pstm.setBoolean(12, tarefa.isQuinta());
				pstm.setBoolean(13, tarefa.isSexta());
				pstm.setBoolean(14, tarefa.isSabado());
				rs = pstm.executeQuery();
				if (rs.next()) {
					tarefa.setId(rs.getInt(1));
					tarefa.setUUID(uuid);
				} else
					throw new Exception(Constante.STR_INSERT_SEM_RETORNO);
			} else {
				pstm = getConexao().prepareStatement(
					"UPDATE Tarefa SET "
					+ "Nome=?,"
					+ "Tipo=?,"
					+ "BancoDados_Id=?,"
					+ "Intervalo=?,"
					+ "HoraInicial=?,"
					+ "HoraFinal=?,"
					+ "Domingo=?,"
					+ "Segunda=?,"
					+ "Terca=?,"
					+ "Quarta=?,"
					+ "Quinta=?,"
					+ "Sexta=?,"
					+ "Sabado=? "
					+ "WHERE Id=?");
				pstm.setString(1, tarefa.getNome());
				pstm.setInt(2, tarefa.getTipo().getId());
				pstm.setInt(3, tarefa.getBancoDados().getId());
				pstm.setInt(4, tarefa.getIntervalo());
				pstm.setTime(5, Time.valueOf(tarefa.getHoraInicial()));
				pstm.setTime(6, Time.valueOf(tarefa.getHoraFinal()));
				pstm.setBoolean(7, tarefa.isDomingo());
				pstm.setBoolean(8, tarefa.isSegunda());
				pstm.setBoolean(9, tarefa.isTerca());
				pstm.setBoolean(10, tarefa.isQuarta());
				pstm.setBoolean(11, tarefa.isQuinta());
				pstm.setBoolean(12, tarefa.isSexta());
				pstm.setBoolean(13, tarefa.isSabado());
				pstm.setInt(14, tarefa.getId());
				pstm.execute();
			}
		} finally {
			if (pstm != null) pstm.close();
		}
	}
	
	public void exclui(int id) throws Exception {
		exclui(id, "Tarefa");
	}

	public void atualizaDataHoraExecutada(int id) throws Exception {
		PreparedStatement pstm = getConexao().prepareStatement(
			"UPDATE Tarefa SET Executada = CURRENT_TIMESTAMP WHERE Id = ?");
		try {
			pstm.setInt(1, id);
			pstm.execute();
		} finally {
			pstm.close();
		}
	}
	
	private static final String SELECT_PADRAO =
		"SELECT "
		+ "Tarefa.Id, "
		+ "Tarefa.Nome, "
		+ "Tarefa.Tipo, "
		+ "BancoDados.Id, "
		+ "BancoDados.Nome, "
		+ "Tarefa.Intervalo, "
		+ "Tarefa.HoraInicial, "
		+ "Tarefa.HoraFinal, "
		+ "Tarefa.Domingo, "
		+ "Tarefa.Segunda, "
		+ "Tarefa.Terca, "
		+ "Tarefa.Quarta, "
		+ "Tarefa.Quinta, "
		+ "Tarefa.Sexta, "
		+ "Tarefa.Sabado, "
		+ "Tarefa.Executada "
		+ "FROM Tarefa "
		+ "LEFT JOIN BancoDados ON BancoDados.Id = Tarefa.BancoDados_Id";
			
	private Tarefa getRegistro(ResultSet rs) throws SQLException {
		Tarefa tarefa = new Tarefa();
		tarefa.setId(rs.getInt(1));
		tarefa.setNome(rs.getString(2));
		tarefa.setTipo(Tarefa.Tipo.fromId(rs.getInt(3)));
		tarefa.getBancoDados().setId(rs.getInt(4));
		tarefa.getBancoDados().setNome(rs.getString(5));
		tarefa.setIntervalo(rs.getInt(6));
		tarefa.setHoraInicial(toLocalTime(rs.getTime(7)));
		tarefa.setHoraFinal(toLocalTime(rs.getTime(8)));
		tarefa.setDomingo(rs.getBoolean(9));
		tarefa.setSegunda(rs.getBoolean(10));
		tarefa.setTerca(rs.getBoolean(11));
		tarefa.setQuarta(rs.getBoolean(12));
		tarefa.setQuinta(rs.getBoolean(13));
		tarefa.setSexta(rs.getBoolean(14));
		tarefa.setSabado(rs.getBoolean(15));
		tarefa.setExecutada(toLocalDateTime(rs.getTimestamp(16)));
		return tarefa;
	}
	
	public List<Tarefa> buscaPorNome(String nome) throws Exception {
		List<Tarefa> lista = new ArrayList<Tarefa>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = getConexao().prepareStatement(
				SELECT_PADRAO + " WHERE Tarefa.Nome LIKE ? ORDER BY Tarefa.Nome");
			pstm.setString(1, "%" + Util.emptyIfNull(nome) + "%");
			rs = pstm.executeQuery();
			while (rs.next())
				lista.add(getRegistro(rs));
		} finally {
			if (rs != null) rs.close();
			if (pstm != null) pstm.close();
		}
		return lista;
	}

	public Tarefa buscaPorId(int id) throws Exception {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = getConexao().prepareStatement(
				SELECT_PADRAO + " WHERE Tarefa.Id = ?");
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			if (rs.next())
				return getRegistro(rs);
		} finally {
			if (rs != null) rs.close();
			if (pstm != null) pstm.close();
		}
		return null;
	}
	
	public List<TarefaPendente> buscaTarefasPendentes() throws Exception {
		List<TarefaPendente> lista = new ArrayList<TarefaPendente>();
		PreparedStatement pstmTarefa = null, pstmBackup = null, pstmReplicacao = null;
		ResultSet rsTarefa = null, rsBackup = null, rsReplicacao = null;
		try {
			// Busca os dados principais das tarefas pendentes.
			pstmTarefa = getConexao().prepareStatement(
				"SELECT " + 
				"  Tarefa.Id, " + 
				"  Tarefa.Tipo, " +
				"  Tarefa.Nome, " + 
				"  BancoDados.Nome, " +
				"  BancoDados.Maquina, " + 
				"  BancoDados.Porta, " + 
				"  BancoDados.Endereco, " + 
				"  BancoDados.Caracteres, " + 
				"  BancoDados.Usuario, " + 
				"  BancoDados.Senha " +
				"FROM Tarefa " + 
				"JOIN BancoDados ON BancoDados.Id = Tarefa.BancoDados_Id " + 
				"WHERE (Tarefa.Executada IS NULL OR DATEADD(SECOND, Tarefa.Intervalo, Tarefa.Executada) <= CURRENT_TIMESTAMP) " + 
				"  AND CURRENT_TIME BETWEEN Tarefa.HoraInicial AND Tarefa.HoraFinal " + 
				"  AND ((EXTRACT(WEEKDAY FROM CURRENT_DATE) = 0 AND Tarefa.Domingo) OR " + 
				"       (EXTRACT(WEEKDAY FROM CURRENT_DATE) = 1 AND Tarefa.Segunda) OR " + 
				"       (EXTRACT(WEEKDAY FROM CURRENT_DATE) = 2 AND Tarefa.Terca) OR " + 
				"       (EXTRACT(WEEKDAY FROM CURRENT_DATE) = 3 AND Tarefa.Quarta) OR " + 
				"       (EXTRACT(WEEKDAY FROM CURRENT_DATE) = 4 AND Tarefa.Quinta) OR " + 
				"       (EXTRACT(WEEKDAY FROM CURRENT_DATE) = 5 AND Tarefa.Sexta) OR " + 
				"       (EXTRACT(WEEKDAY FROM CURRENT_DATE) = 6 AND Tarefa.Sabado)) " +
				"  AND ((EXISTS(SELECT * FROM TarefaBackup " +
				"        WHERE TarefaBackup.Tarefa_Id = Tarefa.Id AND " +
				"        TarefaBackup.Estado = ?)) OR " +
				"       (EXISTS(SELECT * FROM TarefaReplicacao " +
				"        WHERE TarefaReplicacao.Tarefa_Id = Tarefa.Id " +
				"        AND TarefaReplicacao.Estado = ?))) " +
				"ORDER BY DATEADD(SECOND, Tarefa.Intervalo, Tarefa.Executada)");
			pstmTarefa.setInt(1, TarefaDestino.Estado.NORMAL.getId());
			pstmTarefa.setInt(2, TarefaDestino.Estado.NORMAL.getId());
			rsTarefa = pstmTarefa.executeQuery();
			while (rsTarefa.next()) {
				TarefaPendente tarefa = new TarefaPendente();
				tarefa.setId(rsTarefa.getInt(1));
				tarefa.setTipo(Tarefa.Tipo.fromId(rsTarefa.getInt(2)));
				tarefa.setNome(rsTarefa.getString(3));
				tarefa.getBancoDados().setNome(rsTarefa.getString(4));
				tarefa.getBancoDados().setMaquina(rsTarefa.getString(5));
				tarefa.getBancoDados().setPorta(rsTarefa.getInt(6));
				tarefa.getBancoDados().setEndereco(rsTarefa.getString(7));
				tarefa.getBancoDados().setCaracteres(rsTarefa.getString(8));
				tarefa.getBancoDados().setUsuario(rsTarefa.getString(9));
				tarefa.getBancoDados().setSenha(rsTarefa.getString(10));
				lista.add(tarefa);

				// Busca os destinos de backup.
				pstmBackup = getConexao().prepareStatement(
					"SELECT " + 
					"  TarefaBackup.Id, " + 
					"  TarefaBackup.qtdManter, " +
					"  Pasta.Nome, " +
					"  Pasta.Tipo, " + 
					"  Pasta.Maquina, " + 
					"  Pasta.Porta, " + 
					"  Pasta.Endereco, " + 
					"  Pasta.Usuario, " + 
					"  Pasta.Senha, " +
					"  TarefaBackup.Mensagem " +
					"FROM TarefaBackup " + 
					"JOIN Pasta ON Pasta.Id = TarefaBackup.Pasta_Id " + 
					"WHERE TarefaBackup.Tarefa_Id = ? " +
					"  AND TarefaBackup.Estado = ?");
				pstmBackup.setInt(1, tarefa.getId());
				pstmBackup.setInt(2, TarefaDestino.Estado.NORMAL.getId());
				rsBackup = pstmBackup.executeQuery();
				while (rsBackup.next()) {
					TarefaPendente.TarefaBackup backup = new TarefaPendente.TarefaBackup();
					backup.setId(rsBackup.getInt(1));
					backup.setQtdManter(rsBackup.getInt(2));
					backup.getPasta().setNome(rsBackup.getString(3));
					backup.getPasta().setTipo(Pasta.Tipo.fromId(rsBackup.getInt(4)));
					backup.getPasta().setMaquina(rsBackup.getString(5));
					backup.getPasta().setPorta(rsBackup.getObject(6) == null ? null : rsBackup.getInt(6));
					backup.getPasta().setEndereco(rsBackup.getString(7));
					backup.getPasta().setUsuario(rsBackup.getString(8));
					backup.getPasta().setSenha(rsBackup.getString(9));
					backup.setMensagem(rsBackup.getString(10));
					tarefa.getDestinosBackup().add(backup);
				}
				
				// Busca os destinos de replicação.
				pstmReplicacao = getConexao().prepareStatement(
					"SELECT " + 
					"  TarefaReplicacao.Id, " + 
					"  BancoDados.Nome, " +
					"  BancoDados.Maquina, " + 
					"  BancoDados.Porta, " + 
					"  BancoDados.Endereco, " + 
					"  BancoDados.Caracteres, " + 
					"  BancoDados.Usuario, " + 
					"  BancoDados.Senha, " +
					"  TarefaReplicacao.Mensagem " +
					"FROM TarefaReplicacao " + 
					"JOIN BancoDados ON BancoDados.Id = TarefaReplicacao.BancoDados_Id " + 
					"WHERE TarefaReplicacao.Tarefa_Id = ? " +
					"  AND TarefaReplicacao.Estado = ?");
				pstmReplicacao.setInt(1, tarefa.getId());
				pstmReplicacao.setInt(2, TarefaDestino.Estado.NORMAL.getId());
				rsReplicacao = pstmReplicacao.executeQuery();
				while (rsReplicacao.next()) {
					TarefaPendente.TarefaReplicacao replicacao = new TarefaPendente.TarefaReplicacao();
					replicacao.setId(rsReplicacao.getInt(1));
					replicacao.getBancoDados().setNome(rsReplicacao.getString(2));
					replicacao.getBancoDados().setMaquina(rsReplicacao.getString(3));
					replicacao.getBancoDados().setPorta(rsReplicacao.getInt(4));
					replicacao.getBancoDados().setEndereco(rsReplicacao.getString(5));
					replicacao.getBancoDados().setCaracteres(rsReplicacao.getString(6));
					replicacao.getBancoDados().setUsuario(rsReplicacao.getString(7));
					replicacao.getBancoDados().setSenha(rsReplicacao.getString(8));
					replicacao.setMensagem(rsReplicacao.getString(9));
					tarefa.getDestinosReplicacao().add(replicacao);
				}
			}
		} finally {
			if (rsReplicacao != null) rsReplicacao.close();
			if (pstmReplicacao != null) pstmReplicacao.close();
			if (rsTarefa != null) rsTarefa.close();
			if (pstmTarefa != null) pstmTarefa.close();
		}
		return lista;
	}
	
	public List<Notificacao> buscaNotificacoes(int monitorId) throws Exception {
		List<Notificacao> retorno = new ArrayList<>(); 
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm =getConexao().prepareStatement(
				"WITH Destino AS " +
				"	(SELECT " + 
				"		CAST(? AS SMALLINT) AS Tipo, " + 
				"		TarefaBackup.Tarefa_Id, " +
				"		Pasta.Nome, " + 
				"		TarefaBackup.Estado, " + 
				"		TarefaBackup.Mensagem " + 
				"	FROM TarefaBackup " +
				"	JOIN Pasta ON Pasta.Id = TarefaBackup.Pasta_Id " +
				"	UNION " +
				"	SELECT " + 
				"		CAST(? AS SMALLINT) AS Tipo, " +
				"		TarefaReplicacao.Tarefa_Id, " +
				"		BancoDados.Nome, " +
				"		TarefaReplicacao.Estado, " + 
				"		TarefaReplicacao.Mensagem " +
				"	FROM TarefaReplicacao " +
				"	JOIN BancoDados ON BancoDados.Id = TarefaReplicacao.BancoDados_Id) " +
				"SELECT " +
				"	Tarefa.Tipo, " +
				"	Origem.Nome, " +
				"	Destino.Nome, " +
				"	Destino.Estado, " +
				"	Destino.Mensagem " +
				"FROM Tarefa " +
				"JOIN TarefaMonitor ON TarefaMonitor.Tarefa_Id = Tarefa.Id " +
				"JOIN BancoDados AS Origem ON Origem.Id = Tarefa.BancoDados_Id " +
				"JOIN Destino ON Destino.Tipo = Tarefa.Tipo AND Destino.Tarefa_Id = Tarefa.Id " +
				"WHERE TarefaMonitor.Monitor_Id = ?");
			pstm.setInt(1, Tarefa.Tipo.BACKUP.getId());
			pstm.setInt(2, Tarefa.Tipo.REPLICACAO.getId());
			pstm.setInt(3, monitorId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				Notificacao notificacao = new Notificacao();
				notificacao.setTipo(rs.getInt(1));
				notificacao.setOrigem(rs.getString(2));
				notificacao.setDestino(rs.getString(3));
				notificacao.setEstado(rs.getInt(4));
				notificacao.setMensagem(rs.getString(5));
				retorno.add(notificacao);				
			}
		} finally {
			if (rs != null) rs.close();
			if (pstm != null) pstm.close();
		}
		return retorno;
	}

}
