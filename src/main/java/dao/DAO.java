package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import geral.Constante;
import modelo.Modelo;

public abstract class DAO {
	
	private Conexao conexao;

	public DAO(Conexao conexao) {
		super();
		this.conexao = conexao;
	}

	public Conexao getConexao() {
		return conexao;
	}
	
	protected void exclui(int id, String tabela)  throws Exception {
		PreparedStatement pstm = getConexao().prepareStatement(
				"DELETE FROM " + tabela + " WHERE Id=?");
			try {
				pstm.setInt(1, id);
				pstm.execute();
			} finally {
				pstm.close();
			}
	}
	
	protected void executaInsert(PreparedStatement pstm, Modelo modelo) throws SQLException, Exception {
		ResultSet rs = pstm.executeQuery();
		try {
			if (rs.next())
				modelo.setId(rs.getInt(1));
			else
				throw new Exception(Constante.STR_INSERT_SEM_RETORNO);
		} finally {
			rs.close();
		}
		
	}
		
	protected LocalDateTime toLocalDateTime(java.sql.Timestamp timeStamp) {
		if (timeStamp == null)
			return null;
		else
			return timeStamp.toLocalDateTime();
	}

	protected LocalTime toLocalTime(java.sql.Time time) {
		if (time == null)
			return null;
		else
			return time.toLocalTime();
	}
	
}
