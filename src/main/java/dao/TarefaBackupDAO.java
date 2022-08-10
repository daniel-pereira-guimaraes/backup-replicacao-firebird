package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import modelo.TarefaBackup;
import modelo.TarefaDestino;
import modelo.TarefaPendente;

public class TarefaBackupDAO extends DAO {

	public TarefaBackupDAO(Conexao conexao) {
		super(conexao);
	}
	
	public void grava(TarefaBackup tarefaBackup) throws Exception {
		PreparedStatement pstm = null;
		try {
			if (tarefaBackup.getId() == null) {
				pstm = getConexao().prepareStatement(
					"INSERT INTO TarefaBackup("
					+ "Tarefa_Id, Pasta_Id, QtdManter, Estado) "
					+ "VALUES(?,?,?,?) RETURNING Id");
				pstm.setInt(1, tarefaBackup.getTarefaId());
				pstm.setInt(2, tarefaBackup.getPasta().getId());
				pstm.setInt(3, tarefaBackup.getQtdManter());
				pstm.setInt(4, tarefaBackup.getEstado().getId());
				executaInsert(pstm, tarefaBackup);
			} else {
				pstm = getConexao().prepareStatement(
					"UPDATE TarefaBackup SET "
					+ "Tarefa_Id=?,"
					+ "Pasta_Id=?,"
					+ "QtdManter=?,"
					+ "Estado=? "
					+ "WHERE Id=?");
				pstm.setInt(1, tarefaBackup.getTarefaId());
				pstm.setInt(2, tarefaBackup.getPasta().getId());
				pstm.setInt(3, tarefaBackup.getQtdManter());
				pstm.setInt(4, tarefaBackup.getEstado().getId());
				pstm.setInt(5, tarefaBackup.getId());
				pstm.execute();
			}
		} finally {
			if (pstm != null) pstm.close();
		}
	}
	
	public void gravaMensagem(TarefaPendente.TarefaBackup tarefaBackup) throws Exception {
		PreparedStatement pstm = getConexao().prepareStatement(
			"UPDATE TarefaBackup SET Mensagem = ? WHERE Id = ?");
		try {
			pstm.setString(1, tarefaBackup.getMensagem());
			pstm.setInt(2, tarefaBackup.getId());
			pstm.execute();
		} finally {
			pstm.close();
		}
	}
	
	public void gravaMensagem(TarefaPendente tarefa, String mensagem) throws Exception {
		PreparedStatement pstm = getConexao().prepareStatement(
			"UPDATE TarefaBackup SET Mensagem = ? WHERE Tarefa_Id = ?");
		try {
			pstm.setString(1, mensagem);
			pstm.setInt(2, tarefa.getId());
			pstm.execute();
		} finally {
			pstm.close();
		}
	}
	
	public void exclui(int id) throws Exception {
		exclui(id, "TarefaBackup");
	}
	
	public List<TarefaBackup> buscaPorTarefaId(int tarefaId) throws Exception {
		List<TarefaBackup> lista = new ArrayList<TarefaBackup>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = getConexao().prepareStatement(
				"SELECT "
				+ "TarefaBackup.Id,"
				+ "TarefaBackup.Tarefa_Id,"
				+ "TarefaBackup.Pasta_Id,"
				+ "Pasta.Nome,"
				+ "TarefaBackup.QtdManter,"
				+ "TarefaBackup.Estado,"
				+ "TarefaBackup.Mensagem "
				+ "FROM TarefaBackup "
				+ "JOIN Pasta ON Pasta.Id = TarefaBackup.Pasta_Id "
				+ "WHERE TarefaBackup.Tarefa_Id = ? "
				+ "ORDER BY Pasta.Nome");
			pstm.setInt(1, tarefaId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				TarefaBackup tb = new TarefaBackup();
				tb.setId(rs.getInt(1));
				tb.setTarefaId(rs.getInt(2));
				tb.getPasta().setId(rs.getInt(3));
				tb.getPasta().setNome(rs.getString(4));
				tb.setQtdManter(rs.getInt(5));
				tb.setEstado(TarefaDestino.Estado.fromId(rs.getInt(6)));
				tb.setMensagem(rs.getString(7));
				lista.add(tb);
			}
		} finally {
			if (rs != null) rs.close();
			if (pstm != null) pstm.close();
		}
		return lista;
	}

}
