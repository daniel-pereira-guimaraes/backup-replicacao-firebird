package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import modelo.TarefaMonitor;

public class TarefaMonitorDAO extends DAO {

	public TarefaMonitorDAO(Conexao conexao) {
		super(conexao);
	}
	
	public void grava(TarefaMonitor tarefaMonitor) throws Exception {
		PreparedStatement pstm = null;
		try {
			if (tarefaMonitor.getId() == null) {
				pstm = getConexao().prepareStatement(
					"INSERT INTO TarefaMonitor(Tarefa_Id, Monitor_Id) "
					+ "VALUES(?,?) RETURNING Id");
				pstm.setInt(1, tarefaMonitor.getTarefaId());
				pstm.setInt(2, tarefaMonitor.getMonitor().getId());
				executaInsert(pstm, tarefaMonitor);
			} else {
				pstm = getConexao().prepareStatement(
					"UPDATE TarefaMonitor SET "
					+ "Tarefa_Id=?, "
					+ "Monitor_Id=? "
					+ "WHERE Id=?");
				pstm.setInt(1, tarefaMonitor.getTarefaId());
				pstm.setInt(2, tarefaMonitor.getMonitor().getId());
				pstm.setInt(3, tarefaMonitor.getId());
				pstm.execute();
			}
		} finally {
			if (pstm != null) pstm.close();
		}
		
	}
	
	public void exclui(int id) throws Exception {
		exclui(id, "TarefaMonitor");
	}
	
	public List<TarefaMonitor> buscaPorTarefaId(int tarefaId) throws Exception {
		List<TarefaMonitor> lista = new ArrayList<TarefaMonitor>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = getConexao().prepareStatement(
				"SELECT "
				+ "TarefaMonitor.Id,"
				+ "TarefaMonitor.Tarefa_Id,"
				+ "TarefaMonitor.Monitor_Id,"
				+ "Monitor.Nome,"
				+ "Monitor.Chave "
				+ "FROM TarefaMonitor "
				+ "JOIN Monitor ON Monitor.Id = TarefaMonitor.Monitor_Id "
				+ "WHERE TarefaMonitor.Tarefa_Id = ? "
				+ "ORDER BY Monitor.Nome");
			pstm.setInt(1, tarefaId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				TarefaMonitor tm = new TarefaMonitor();
				tm.setId(rs.getInt(1));
				tm.setTarefaId(rs.getInt(2));
				tm.getMonitor().setId(rs.getInt(3));
				tm.getMonitor().setNome(rs.getString(4));
				tm.getMonitor().setChave(rs.getString(5));
				lista.add(tm);
			}
		} finally {
			if (rs != null) rs.close();
			if (pstm != null) pstm.close();
		}
		return lista;
	}

}
