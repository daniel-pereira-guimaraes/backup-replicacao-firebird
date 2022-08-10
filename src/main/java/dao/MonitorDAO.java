package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import geral.Util;
import modelo.Monitor;

public class MonitorDAO extends DAO {

	public MonitorDAO(Conexao conexao) {
		super(conexao);
	}
	
	public void grava(Monitor monitor) throws Exception {
		PreparedStatement pstm = null;
		try {
			if (monitor.getId() == null) {
				pstm = getConexao().prepareStatement(
					"INSERT INTO Monitor(Nome, Chave) VALUES(?,?) RETURNING Id");
				pstm.setString(1, monitor.getNome());
				pstm.setString(2, monitor.getChave());
				executaInsert(pstm, monitor);
			} else {
				pstm = getConexao().prepareStatement(
					"UPDATE Monitor SET "
					+ "Nome=?,"
					+ "Chave=?, "
					+ "Controle=? "
					+ "WHERE Id=?");
				pstm.setString(1, monitor.getNome());
				pstm.setString(2, monitor.getChave());
				pstm.setString(3, monitor.getControle());
				pstm.setInt(4, monitor.getId());
				pstm.execute();
			}
		} finally {
			if (pstm != null) pstm.close();
		}
	}
	
	public void gravaControle(Monitor monitor) throws Exception {
		PreparedStatement pstm = getConexao().prepareStatement(
			"UPDATE Monitor SET Controle = ? WHERE Id = ?");
		try {
			pstm.setString(1, monitor.getControle());
			pstm.setInt(2, monitor.getId());
			pstm.execute();
		} finally {
			pstm.close();
		}
	}
	
	public void exclui(int id) throws Exception {
		exclui(id, "Monitor");
	}
	
	private static final String SELECT_PADRAO = 
		"SELECT Id, Nome, Chave, Controle FROM Monitor";
		
	private Monitor getRegistro(ResultSet rs) throws SQLException {
		Monitor monitor = new Monitor();
		monitor.setId(rs.getInt(1));
		monitor.setNome(rs.getString(2));
		monitor.setChave(rs.getString(3));
		monitor.setControle(rs.getString(4));
		return monitor;
	}

	public List<Monitor> buscaPorNome(String nome) throws Exception {
		List<Monitor> lista = new ArrayList<Monitor>();
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = getConexao().prepareStatement(
				SELECT_PADRAO + " WHERE Nome LIKE ? ORDER BY Nome");
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
	
	public Monitor buscaPorId(int id) throws Exception {
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			pstm = getConexao().prepareStatement(SELECT_PADRAO + " WHERE Id=?");
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

}
