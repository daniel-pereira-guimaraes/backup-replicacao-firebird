package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import geral.Util;
import modelo.Pasta;

public class PastaDAO extends DAO {

	public PastaDAO(Conexao conexao) {
		super(conexao);
	}
	
	public void grava(Pasta pasta) throws Exception {
		PreparedStatement pstm = null;
		try {
			if (pasta.getId() == null) {
				pstm = getConexao().prepareStatement(
					"INSERT INTO Pasta( "
					+ "Nome, Tipo, Maquina, Porta, Endereco, Usuario, Senha) "
					+ "VALUES(?,?,?,?,?,?,?) RETURNING Id");
				pstm.setString(1, pasta.getNome());
				pstm.setInt(2, pasta.getTipo().getId());
				pstm.setString(3, pasta.getMaquina());
				pstm.setObject(4, pasta.getPorta());
				pstm.setString(5, pasta.getEndereco());
				pstm.setString(6, pasta.getUsuario());
				pstm.setString(7, pasta.getSenha());
				executaInsert(pstm, pasta);
			} else {
				pstm = getConexao().prepareStatement(
					"UPDATE Pasta SET "
					+ "Nome=?,"
					+ "Tipo=?,"
					+ "Maquina=?,"
					+ "Porta=?,"
					+ "Endereco=?,"
					+ "Usuario=?,"
					+ "Senha=? "
					+ "WHERE Id=?");
				pstm.setString(1, pasta.getNome());
				pstm.setInt(2, pasta.getTipo().getId());
				pstm.setString(3, pasta.getMaquina());
				pstm.setObject(4, pasta.getPorta());
				pstm.setString(5, pasta.getEndereco());
				pstm.setString(6, pasta.getUsuario());
				pstm.setString(7, pasta.getSenha());
				pstm.setInt(8, pasta.getId());
				pstm.execute();
			}
		} finally {
			if (pstm != null) pstm.close();
		}
	}
	
	public void exclui(int id) throws Exception {
		exclui(id, "Pasta");
	}
	
	private static final String SELECT_PADRAO = 
		"SELECT Id, Nome, Tipo, Maquina, Porta, Endereco, Usuario, Senha "
		+ "FROM Pasta";
		
	private Pasta getRegistro(ResultSet rs) throws SQLException {
		Pasta pasta = new Pasta();
		pasta.setId(rs.getInt(1));
		pasta.setNome(rs.getString(2));
		pasta.setTipo(Pasta.Tipo.fromId(rs.getInt(3)));
		pasta.setMaquina(rs.getString(4));
		pasta.setPorta(rs.getObject(5) == null ? null : rs.getInt(5));
		pasta.setEndereco(rs.getString(6));
		pasta.setUsuario(rs.getString(7));
		pasta.setSenha(rs.getString(8));
		return pasta;
	}

	public List<Pasta> buscaPorNome(String nome) throws Exception {
		List<Pasta> lista = new ArrayList<Pasta>();
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
	
	public Pasta buscaPorId(int id) throws Exception {
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
