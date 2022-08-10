package firebird;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.Conexao;
import modelo.BancoDados;

public class FirebirdConexao extends dao.Conexao {

	private BancoDados bancoDados; 
	
	public FirebirdConexao(BancoDados bancoDados) {
		super();
		this.bancoDados = bancoDados;
	}

	@Override
	protected Connection createConnection() throws SQLException {
		return DriverManager.getConnection(bancoDados.getUrl(), 
			bancoDados.getUsuario(), bancoDados.getSenha());
	}

	@Override
	public boolean existeGatilho(String nome) throws Exception {
		PreparedStatement pstm = prepareStatement(
			"SELECT EXISTS(SELECT * FROM RDB$TRIGGERS "
			+ "WHERE RDB$TRIGGER_NAME = ?) "
			+ "FROM RDB$DATABASE");
		pstm.setString(1, nome);
		ResultSet rs = pstm.executeQuery();
		return rs.next() && rs.getBoolean(1);
	}
						
	@Override
	public boolean existeTabela(String nome) throws Exception {
		PreparedStatement pstm = prepareStatement(
			"SELECT EXISTS(SELECT * FROM RDB$RELATIONS "
			+ "WHERE RDB$RELATION_NAME = ?) "
			+ "FROM RDB$DATABASE");
		pstm.setString(1, nome);
		ResultSet rs = pstm.executeQuery();
		return rs.next() && rs.getBoolean(1);
	}

	public BancoDados getBancoDados() {
		return bancoDados;
	}
	
	public static boolean bancoExiste(BancoDados bd) throws Exception {
		boolean existe = false;
		Conexao conexao = new FirebirdConexao(bd);
		try {
			conexao.conecta();
			existe = true;
			conexao.desconecta();
		} catch (SQLException e) {
			existe = !bancoNaoExiste(e);
		}
		return existe;
	}
	
	public static boolean bancoNaoExiste(SQLException e) {
		return e.getSQLState().equals("08001") && e.getErrorCode() == 335544344;		
	}
			
}
