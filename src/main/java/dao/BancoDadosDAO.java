package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import geral.Util;
import modelo.BancoDados;

public class BancoDadosDAO extends DAO {

	public BancoDadosDAO(Conexao conexao) {
		super(conexao);
	}
	
	public void grava(BancoDados bancoDados) throws Exception {
		PreparedStatement pstm = null;
		try {
			if (bancoDados.getId() == null) {
				pstm = getConexao().prepareStatement(
					"INSERT INTO BancoDados( "
					+ "Nome, Maquina, Porta, Endereco, "
					+ "Caracteres, Usuario, Senha) "
					+ "VALUES(?,?,?,?,?,?,?) "
					+ "RETURNING Id");
				pstm.setString(1, bancoDados.getNome());
				pstm.setString(2, bancoDados.getMaquina());
				pstm.setInt(3, bancoDados.getPorta());
				pstm.setString(4, bancoDados.getEndereco());
				pstm.setString(5, bancoDados.getCaracteres());
				pstm.setString(6, bancoDados.getUsuario());
				pstm.setString(7, bancoDados.getSenha());
				executaInsert(pstm, bancoDados);
			} else {
				pstm = getConexao().prepareStatement(
					"UPDATE BancoDados SET "
					+ "Nome=?,"
					+ "Maquina=?,"
					+ "Porta=?,"
					+ "Endereco=?,"
					+ "Caracteres=?,"
					+ "Usuario=?,"
					+ "Senha=? "
					+ "WHERE id=?");
				pstm.setString(1, bancoDados.getNome());
				pstm.setString(2, bancoDados.getMaquina());
				pstm.setInt(3, bancoDados.getPorta());
				pstm.setString(4, bancoDados.getEndereco());
				pstm.setString(5, bancoDados.getCaracteres());
				pstm.setString(6, bancoDados.getUsuario());
				pstm.setString(7, bancoDados.getSenha());
				pstm.setInt(8, bancoDados.getId());
				pstm.execute();
			}
		} finally {
			if (pstm != null) pstm.close();
		}
	}
	
	public void exclui(int id) throws Exception {
		exclui(id, "BancoDados");
	}

	private static final String SELECT_PADRAO = 
		"SELECT Id, Nome, Maquina, Porta, Endereco, Caracteres, Usuario, Senha "
		+ "FROM BancoDados";
	
	private BancoDados getRegistro(ResultSet rs) throws SQLException {
		BancoDados bancoDados = new BancoDados();
		bancoDados.setId(rs.getInt(1));
		bancoDados.setNome(rs.getString(2));
		bancoDados.setMaquina(rs.getString(3));
		bancoDados.setPorta(rs.getInt(4));
		bancoDados.setEndereco(rs.getString(5));
		bancoDados.setCaracteres(rs.getString(6));
		bancoDados.setUsuario(rs.getString(7));
		bancoDados.setSenha(rs.getString(8));
		return bancoDados;
	}

	public List<BancoDados> buscaPorNome(String nome) throws Exception {
		List<BancoDados> lista = new ArrayList<BancoDados>();
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
	
	public BancoDados buscaPorId(int id) throws Exception {
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
